package inbound;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import outbound.*;

import java.util.HashMap;
import java.util.Map;

//自定义服务端处理器
//处理核心业务逻辑
public class HttpInboundHandler extends ChannelInboundHandlerAdapter {

    private final Map<String, AbstractHttpOutboundHandler> outboundMap = new HashMap<>(8);
    public HttpInboundHandler(){
        outboundMap.put("ok", new OkHttpOutboundHandler());
        outboundMap.put("client", new HttpClientOutboundHandler());
        NettyHttpOutboundHandler nettyHandler = new NettyHttpOutboundHandler();
        outboundMap.put("netty", nettyHandler);
        outboundMap.put("default", nettyHandler);
    }

    /**
     * 当Channel中有来自于客户端的数据时就会触发该方法的执行
     *
     * @param ctx 上下文对象
     * @param msg 来自于Channel(客户端)的数据
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //获取到客户端的Http请求
        FullHttpRequest fullRequest = (FullHttpRequest) msg;
        String uri = fullRequest.uri();

        //只处理带/test的请求
        if (!uri.contains("/test")) {
            return;
        }

        //根据不同的请求后缀选择对应的HttpClient请求后台web服务
        int index = uri.lastIndexOf("/");
        String suffix = uri.substring(index + 1);
        System.out.println("suffix is : " + suffix);
        AbstractHttpOutboundHandler handler = outboundMap.get(suffix);
        if (handler == null) {
            handler = outboundMap.get("default");
        }

        handler.handler(fullRequest, ctx);
    }



    /**
     * 读结束后就会触发该方法的执行， 将数据刷新到管道中，即向客户端返回
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }
/*

    *//**
     * 获取到请求之后的核心处理逻辑
     *
     * @param fullRequest
     * @param ctx
     *//*
    private void handlerTest(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        //用OkHttpClient去请求后端的web服务
        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .get().url("http://localhost:8088/api/hello").build();

        FullHttpResponse response = null;
        try {
            Response webRes = httpClient.newCall(request).execute();
            //将后端的web服务返回的结果封装到Netty的响应中
            String value = webRes.body().string();
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


    *//**
     * 读结束后就会触发该方法的执行， 将数据刷新到管道中，即向客户端返回
     *
     * @param ctx
     * @throws Exception
     *//*
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    *//**
     * 当Channel中的数据在处理过程中出现异常时会触发该方法的执行
     *
     * @param ctx
     * @param cause
     * @throws Exception
     *//*
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }*/
}