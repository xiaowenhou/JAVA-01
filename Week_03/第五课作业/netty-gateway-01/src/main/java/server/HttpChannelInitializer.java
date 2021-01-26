package server;

import inbound.HttpInboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * 管道初始化器
 */
public class HttpChannelInitializer extends ChannelInitializer<SocketChannel> {
    /**
     * 当Channel初始创建完毕后就会触发该方法的执行， 用于初始化Channel
     * @param socketChannel
     * @throws Exception
     */
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //从channel中获取pipeline
        ChannelPipeline pipeline = socketChannel.pipeline();
        //将HttpServerCodec处理器放入到pipeline的最后
        //HttpServerCodec是解码器和编码器， 将客户端发送过来的server端的二进制数据变为ByteBuffer格式的数据， 并且
        //再将处理后的ByteBuffer格式的数据再转换成二进制的数据， 包含了HttpRequestDecoder和HttpResponseEncoder
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(1024 * 1024));
        //将自定义的处理器放入到Pipeline的最后
        pipeline.addLast(new HttpInboundHandler());
    }
}
