package pt.toino.websocketnet;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

class ServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast("HTTP Codec", new HttpServerCodec());
        pipeline.addLast("HTTP Aggregator", new HttpObjectAggregator(65536));
        pipeline.addLast("WebSocket", new WebSocketServerProtocolHandler("/"));
        pipeline.addLast("Handler", new ServerHandler());
    }

}
