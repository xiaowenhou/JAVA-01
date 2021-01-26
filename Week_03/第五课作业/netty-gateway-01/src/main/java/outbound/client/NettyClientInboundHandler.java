package outbound.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.List;

public class NettyClientInboundHandler extends ChannelInboundHandlerAdapter {

    private final List<String> list;
    private final String uriPath;

    public NettyClientInboundHandler(List<String> list, String uriPath) {
        this.list = list;
        this.uriPath = uriPath;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET, uriPath);
        request.headers().add(HttpHeaderNames.CONNECTION,HttpHeaderValues.KEEP_ALIVE);
        request.headers().add(HttpHeaderNames.CONTENT_LENGTH,request.content().readableBytes());
        ctx.writeAndFlush(request);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof HttpResponse) {
            HttpResponse response = (HttpResponse) msg;
            System.out.println("reveive response, headers is :" + response.headers().toString());
        }

        if (msg instanceof HttpContent) {
            HttpContent content = (HttpContent) msg;
            ByteBuf buf = content.content();
            System.out.println("receive response content, content is:" + buf.toString(CharsetUtil.UTF_8));
            list.add(buf.toString(CharsetUtil.UTF_8));
            buf.release();
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        this.list.add("");
    }
}