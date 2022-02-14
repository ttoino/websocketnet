package pt.toino.websocketnet;

import io.netty.buffer.ByteBuf;

public abstract class ServerMessage {

    public abstract void encode(ByteBuf bytes);

}
