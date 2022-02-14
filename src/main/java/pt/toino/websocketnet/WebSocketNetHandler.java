package pt.toino.websocketnet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

public interface WebSocketNetHandler<SM extends ServerMessage, CM extends ClientMessage, U extends User> {

    SM decodeServerMessage(ByteBuf bytes);

    CM decodeClientMessage(ByteBuf bytes);

    void onServerMessage(SM serverMessage);

    void onClientMessage(U user, CM clientMessage);

    U onUserConnect(Channel channel, WebSocketServerProtocolHandler.HandshakeComplete handshakeComplete);

    void onUserDisconnect(U user);

    void onConnectToServer();

    void onDisconnectFromServer();

    void onServerStart();

    void onServerStop();

    HttpHeaders createClientHttpHeaders();

}
