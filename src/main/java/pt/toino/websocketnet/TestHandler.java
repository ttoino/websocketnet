package pt.toino.websocketnet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.EmptyHttpHeaders;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

import java.nio.charset.StandardCharsets;

class TestHandler implements WebSocketNetHandler<TestServerMessage, TestClientMessage, User> {

    private int nextId;

    @Override
    public TestServerMessage decodeServerMessage(ByteBuf bytes) {
        return new TestServerMessage(bytes.toString(StandardCharsets.UTF_8));
    }

    @Override
    public TestClientMessage decodeClientMessage(ByteBuf bytes) {
        return new TestClientMessage(bytes.toString(StandardCharsets.UTF_8));
    }

    @Override
    public void onServerMessage(TestServerMessage serverMessage) {
        System.out.println("Server message: " + serverMessage.content);
    }

    @Override
    public void onClientMessage(User user, TestClientMessage clientMessage) {
        System.out.println("Client " + user.id + " message: " + clientMessage.content);
    }

    @Override
    public User onUserConnect(Channel channel, WebSocketServerProtocolHandler.HandshakeComplete handshakeComplete) {
        System.out.println(handshakeComplete.requestHeaders());

        return new User(nextId++, channel) {
        };
    }

    @Override
    public void onUserDisconnect(User user) {
    }

    @Override
    public void onConnectToServer() {
        WebSocketNet.getInstance().sendClientMessage(new TestClientMessage("Hey"));
    }

    @Override
    public void onDisconnectFromServer() {

    }

    @Override
    public void onServerStart() {

    }

    @Override
    public void onServerStop() {

    }

    @Override
    public HttpHeaders createClientHttpHeaders() {
        return EmptyHttpHeaders.INSTANCE;
    }
}
