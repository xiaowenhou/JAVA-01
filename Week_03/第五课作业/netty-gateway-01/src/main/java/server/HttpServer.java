package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class HttpServer {
    public static void main(String[] args) {
        //用于处理客户端连接请求， 将请求发送给childGroup中的eventLoop
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        //用于处理客户端请求
        EventLoopGroup childGroup = new NioEventLoopGroup();

        try {
            //启动ServerChannel
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //Netty调优参数
            serverBootstrap.option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.SO_RCVBUF, 32 * 1024)
                    .option(ChannelOption.SO_SNDBUF, 32 * 1024)
                    .option(EpollChannelOption.SO_REUSEPORT, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

            serverBootstrap.group(parentGroup, childGroup) //指定使用的eventLoopGroup
                    //使用Nio的Channel
                    .channel(NioServerSocketChannel.class)
                    //指定parentGroup中绑定的线程要处理的处理器
                    .handler(new LoggingHandler(LogLevel.INFO))
                    //指定childGroup中绑定的线程要处理的处理器
                    //初始化一个管道
                    .childHandler(new HttpChannelInitializer());

            //当前服务器监听的端口号， 但bind的执行是异步的，用sync()方法将该方法转换为同步的
            ChannelFuture channelFuture = serverBootstrap.bind(8888).sync();
            System.out.println("服务器监听在8888端口启动成功");
            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //优雅关闭两个EventLoopGroup
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }
}






















