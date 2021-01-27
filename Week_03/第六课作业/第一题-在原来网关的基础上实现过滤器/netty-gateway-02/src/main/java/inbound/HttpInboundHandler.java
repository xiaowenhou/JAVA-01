package inbound;

import filter.DefaultPreRequestFilter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import outbound.AbstractHttpOutboundHandler;
import outbound.HttpClientOutboundHandler;
import outbound.NettyHttpOutboundHandler;
import outbound.OkHttpOutboundHandler;

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
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
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

        //给请求添加默认的过滤器，实际中可以做成配置项， 并且可以自定义，配置了则加入响应的实现类对象，没有配置则默认没有过滤器， 传null即可
        handler.handler(fullRequest, ctx, new DefaultPreRequestFilter());
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
}