package filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * Http的前置过滤器
 */
public interface HttpPreRequestFilter {

    void filter(FullHttpRequest fullHttpRequest, ChannelHandlerContext ctx);
}
