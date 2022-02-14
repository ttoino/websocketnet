package pt.toino.websocketnet;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;

class Server {

    public static void main(String[] args) throws Exception {
        WebSocketNet<TestServerMessage, TestClientMessage, User> net = new WebSocketNet<>(new TestHandler());

        net.startServer(35353);
    }

    private Channel channel;
    private EventLoopGroup bossGroup, workerGroup;
    private ServerBootstrap bootstrap;

    public Server(int port) {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler())
                .childHandler(new ServerInitializer())
                .localAddress(port);
    }

    public void start() {
        try {
            channel = bootstrap.bind().sync().channel();

            WebSocketNet.getInstance().serverStart();

            channel.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();

            WebSocketNet.getInstance().serverStop();
        }
    }

    public void stop() {
        channel.close();
    }

}
