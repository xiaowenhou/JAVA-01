package router;

import cn.hutool.core.util.RandomUtil;

import java.util.List;

/**
 * 随机算法的路由器
 */
public class RandomRouter implements HttpEndpointRouter{
    @Override
    public String route(List<String> endpoints) {
        int size = endpoints.size();
        int index = RandomUtil.randomInt(0, size);
        return endpoints.get(index);
    }
}