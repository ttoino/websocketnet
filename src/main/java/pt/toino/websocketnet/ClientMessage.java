package pt.toino.websocketnet;

import io.netty.buffer.ByteBuf;

public abstract class ClientMessage {

    public abstract void encode(ByteBuf bytes);

}
