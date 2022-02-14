package pt.toino.websocketnet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

import java.util.HashSet;
import java.util.Set;

public class WebSocketNet<SM extends ServerMessage, CM extends ClientMessage, U extends User> {

    public static void main(String[] args) throws Exception {
        WebSocketNet<TestServerMessage, TestClientMessage, User> net = new WebSocketNet<>(new TestHandler());

        net.startServer(35353);
        net.connectClient("localhost", 35353);

        Thread.sleep(2000);

        net.closeClient();

        Thread.sleep(500);
        net.stopServer();

        System.exit(0);
    }

    private static WebSocketNet instance;

    private Set<U> users;

    private WebSocketNetHandler<SM, CM, U> handler;

    private Server server;
    private Client client;

    public WebSocketNet(WebSocketNetHandler<SM, CM, U> handler) {
        this.handler = handler;
        instance = this;
    }

    // SERVER
    public void startServer(final int port) {
        if (server != null)
            stopServer();

        users = new HashSet<>();

        Thread serverThread = new Thread(() -> {
            server = new Server(port);
            server.start();
        }, "Server Thread");

        serverThread.start();
    }

    public void stopServer() {
        if (server != null) {
            for (U user : users) {
                user.channel.writeAndFlush(new CloseWebSocketFrame());
            }

            server.stop();
        }
    }

    public void sendServerMessage(U user, SM serverMessage) {
        ByteBuf bytes = Unpooled.directBuffer(0);

        serverMessage.encode(bytes);

        user.channel.writeAndFlush(new BinaryWebSocketFrame(bytes));
    }

    public void sendServerMessage(SM serverMessage) {
        for (U user : users)
            sendServerMessage(user, serverMessage);
    }

    void receiveServerMessage(ByteBuf bytes) {
        SM serverMessage = handler.decodeServerMessage(bytes);

        handler.onServerMessage(serverMessage);
    }

    void serverStart() {
        handler.onServerStart();
    }

    void serverStop() {
        handler.onServerStop();
    }

    //CLIENT
    public void connectClient(final String ip, final int port) {
        Thread clientThread = new Thread(() -> {
            client = new Client(ip, port, handler.createClientHttpHeaders());
            client.connect();
        }, "Client Thread");

        clientThread.start();
    }

    public void closeClient() {
        if (client != null) {
            client.close();
        }
    }

    public void sendClientMessage(CM clientMessage) {
        ByteBuf bytes = Unpooled.directBuffer(0);

        clientMessage.encode(bytes);

        client.channel.writeAndFlush(new BinaryWebSocketFrame(bytes));
    }

    void receiveClientMessage(Channel channel, ByteBuf bytes) {
        CM clientMessage = handler.decodeClientMessage(bytes);

        U user = getUserFromChannel(channel);

        handler.onClientMessage(user, clientMessage);
    }

    //USERS
    void connectToServer() {
        handler.onConnectToServer();
    }

    void disconnectFromServer() {
        handler.onDisconnectFromServer();
    }

    void connectUser(Channel channel, WebSocketServerProtocolHandler.HandshakeComplete handshakeComplete) {
        U user = handler.onUserConnect(channel, handshakeComplete);

        if (user != null)
            users.add(user);
    }

    void disconnectUser(Channel channel) {
        U user = getUserFromChannel(channel);

        handler.onUserDisconnect(user);

        users.remove(user);
    }

    public U getUserFromId(int id) {
        for (U u : users) {
            if (u.id == id) {
                return u;
            }
        }

        return null;
    }

    public U getUserFromChannel(Channel channel) {
        for (U u : users) {
            if (u.channel.equals(channel)) {
                return u;
            }
        }

        return null;
    }

    //OTHER
    static WebSocketNet getInstance() {
        return instance;
    }

    public Set<U> getUsers() {
        return users;
    }
}
