package outbound;

import filter.HttpPreRequestFilter;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.StringUtil;

/**
 * 模板方法父类，定义发送Http请求以及处理响应的算法骨架
 */
public abstract class AbstractHttpOutboundHandler {
    private static final String WEB_URL = "http://localhost:8088/api/hello";


    /**
     * 定义算法的骨架
     * @param fullHttpRequest
     * @param ctx
     */
    public final void handler(FullHttpRequest fullHttpRequest, ChannelHandlerContext ctx, HttpPreRequestFilter filter) {
        //在请求前面加一个前置过滤器， 也可以根据需求在返回时也加一个过滤器，只是修改的比较多，
        //要将下游服务返回的Http的协议，状态码， 以及HttpHeader和Body中的值都封装起来，然后添加过滤器
        preFilter(fullHttpRequest, ctx, filter);
        String responseValue = sendRequest(fullHttpRequest, WEB_URL);
        handleResponse(fullHttpRequest, responseValue, ctx);
    }

    /**
     * 抽象方法，由各自的子类实现具体发送请求的方式
     * @param fullHttpRequest
     * @param webUrl
     * @return
     */
    protected abstract String sendRequest(FullHttpRequest fullHttpRequest, String webUrl);


    /**
     * 前置过滤器实现
     * @param fullHttpRequest   请求
     * @param ctx       ChannelHandler上下文对象
     * @param filter   前置过滤器类
     */
    private void preFilter(FullHttpRequest fullHttpRequest, ChannelHandlerContext ctx, HttpPreRequestFilter filter){
        if (filter == null) {
            return;
        }
        filter.filter(fullHttpRequest, ctx);
    }


    /**
     * 处理下游web服务返回的响应，并向客户端响应
     * @param fullRequest
     * @param value
     * @param ctx
     */
    private void handleResponse(FullHttpRequest fullRequest, String value, ChannelHandlerContext ctx) {
        FullHttpResponse response = null;
        try {
            if (StringUtil.isNullOrEmpty(value)) {
                throw new RuntimeException("请求无响应");
            }
            //初始化Http响应， 并且处理逻辑
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(value.getBytes(CharsetUtil.UTF_8)));
            response.headers().set("Content-Type", "application/json");
            response.headers().setInt("Content-length", response.content().readableBytes());
        } catch (Exception e) {
            //处理出错时修改Http的响应头
            System.out.println("处理Http请求出错: " + e.getMessage());
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NO_CONTENT);
        } finally {
            //将结果写入到管道中， 并且发送回客户端
            if (fullRequest != null) {
                //如果没有开启keepalive， 直接返回并关闭Channel
                if (!HttpUtil.isKeepAlive(fullRequest)) {
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    if (response != null) {
                        //否则， 将结果返回， 不关闭Channel
                        response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                        ctx.write(response);
                    }
                }
            }
        }
    }
}