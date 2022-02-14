package pt.toino.websocketnet;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

class TestServerMessage extends ServerMessage {

    final String content;

    TestServerMessage(String s) {
        content = s;
    }

    @Override
    public void encode(ByteBuf bytes) {
        bytes.writeCharSequence(content, StandardCharsets.UTF_8);
    }
}
