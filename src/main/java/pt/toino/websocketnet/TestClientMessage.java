package pt.toino.websocketnet;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

public class TestClientMessage extends ClientMessage {

    final String content;

    TestClientMessage(String s) {
        content = s;
    }

    @Override
    public void encode(ByteBuf bytes) {
        bytes.writeCharSequence(content, StandardCharsets.UTF_8);
    }
}
