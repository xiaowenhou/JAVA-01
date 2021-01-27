package outbound.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * 基于Netty的HttpClient处理器
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    private final List<String> list;
    private final String uriPath;
    private final FullHttpRequest fullHttpRequest;

    public NettyClientHandler(List<String> list, String uriPath, FullHttpRequest fullHttpRequest) {
        this.list = list;
        this.uriPath = uriPath;
        this.fullHttpRequest = fullHttpRequest;
    }

    /**
     * 在Channel有消息的时候向下游服务发起Http请求调用
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        //将原请求的headers在去除Host参数之后全部放在当前请求上发送给下游服务,
        HttpHeaders headers = fullHttpRequest.headers();
        headers.remove("Host");

        FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET, uriPath);
        request.headers().setAll(headers);
        request.headers().add(HttpHeaderNames.CONNECTION,HttpHeaderValues.KEEP_ALIVE);
        System.out.println(request.headers().toString());
        ctx.writeAndFlush(request);
    }

    /**
     * 当下游服务有响应的时候处理
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof HttpResponse) {
            HttpResponse response = (HttpResponse) msg;
            System.out.println("receive response, headers is :" + response.headers().toString());
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