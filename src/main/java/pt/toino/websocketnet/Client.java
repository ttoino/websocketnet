package pt.toino.websocketnet;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;

class Client {
    public static void main(String[] args) throws Exception {
        WebSocketNet<TestServerMessage, TestClientMessage, User> net = new WebSocketNet<>(new TestHandler());

        net.connectClient("localhost", 35353);
    }

    Channel channel;
    private EventLoopGroup eventLoopGroup;
    private Bootstrap bootstrap;

    public Client(String ip, int port, HttpHeaders httpHeaders) {
        eventLoopGroup = new NioEventLoopGroup();

        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ClientInitializer(ip, port, httpHeaders))
                .remoteAddress(ip, port);
    }

    public void connect() {
        try {
            channel = bootstrap.connect().sync().channel();

            channel.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

    public void close() {
        channel.writeAndFlush(new CloseWebSocketFrame());
    }

}
