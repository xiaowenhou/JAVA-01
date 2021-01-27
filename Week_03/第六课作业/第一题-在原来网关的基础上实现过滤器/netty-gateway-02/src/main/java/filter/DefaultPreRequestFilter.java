package filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.internal.StringUtil;


/**
 * 默认的前置过滤器实现
 */
public class DefaultPreRequestFilter implements HttpPreRequestFilter{
    @Override
    public void filter(FullHttpRequest fullHttpRequest, ChannelHandlerContext ctx) {
        HttpHeaders headers = fullHttpRequest.headers();
        if (StringUtil.isNullOrEmpty(headers.get("self-header"))) {
            headers.set("self-header", "this is a default filter");
        }
    }
}