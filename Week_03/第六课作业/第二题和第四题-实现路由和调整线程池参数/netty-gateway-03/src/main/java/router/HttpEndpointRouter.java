package router;

import java.util.List;

/**
 * 调用后台Web应用的路由器接口
 */
public interface HttpEndpointRouter {
    String route(List<String> endpoints);
}
