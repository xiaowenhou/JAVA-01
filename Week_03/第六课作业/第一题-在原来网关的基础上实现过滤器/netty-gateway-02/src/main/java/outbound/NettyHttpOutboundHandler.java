package outbound;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import outbound.client.NettyClientHandler;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建一个netty的HttpClient去向下游服务发送请求
 */
public class NettyHttpOutboundHandler extends AbstractHttpOutboundHandler {

    @Override
    protected String sendRequest(FullHttpRequest fullHttpRequest, String webUrl) {
        System.out.println("NettyHttpClient execute...");

        //创建Netty client端
        EventLoopGroup workerGroup = new NioEventLoopGroup(2);
        List<String> list = new ArrayList<>();
        try {
            URI uri = new URI(webUrl);
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new HttpClientCodec());
                            pipeline.addLast(new NettyClientHandler(list, uri.getPath(), fullHttpRequest));
                        }
                    });

            ChannelFuture future = bootstrap.connect(uri.getHost(), uri.getPort()).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException | URISyntaxException e) {
            e.printStackTrace();
            return "";
        } finally {
            workerGroup.shutdownGracefully();
        }
        return list.get(0);
    }
}