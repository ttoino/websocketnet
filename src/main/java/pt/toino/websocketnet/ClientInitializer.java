package pt.toino.websocketnet;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;

import java.net.URI;

class ClientInitializer extends ChannelInitializer<SocketChannel> {

    private final String url;
    private final HttpHeaders httpHeaders;

    ClientInitializer(String ip, int port, HttpHeaders httpHeaders) {
        url = "ws://" + ip + ":" + port;
        this.httpHeaders = httpHeaders;
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast("HTTP Codec", new HttpClientCodec());
        pipeline.addLast("HTTP Aggregator", new HttpObjectAggregator(8192));
        pipeline.addLast("WebSocket Protocol", new WebSocketClientProtocolHandler(new URI(url), WebSocketVersion.V13, null, false, httpHeaders, 8192));
        pipeline.addLast("Client Handler", new ClientHandler());
    }

}
