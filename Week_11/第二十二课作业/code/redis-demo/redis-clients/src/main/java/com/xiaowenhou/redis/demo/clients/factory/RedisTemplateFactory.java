package com.xiaowenhou.redis.demo.clients.factory;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class RedisTemplateFactory {

    @Resource(name = "jedis")
    private RedisConnectionFactory jedis;
    @Resource(name = "lettuce")
    private RedisConnectionFactory lettuce;
    @Resource(name = "redisson")
    private RedisConnectionFactory redisson;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    private final static Map<String, RedisConnectionFactory> REDIS_TEMPLATE_MAP = new HashMap<>();

    @PostConstruct
    public void init() {
        REDIS_TEMPLATE_MAP.put("jedis", jedis);
        REDIS_TEMPLATE_MAP.put("lettuce", lettuce);
        REDIS_TEMPLATE_MAP.put("redisson", redisson);
    }

    public RedisTemplate<String, Object> getRedisTemplate(String key) {
        RedisConnectionFactory connectionFactory = REDIS_TEMPLATE_MAP.get(key);
        if (Objects.isNull(connectionFactory)) {
            return redisTemplate;
        }
        this.redisTemplate.setConnectionFactory(connectionFactory);
        this.redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

}
