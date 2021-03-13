package com.xiaowenhou.shardingjdbcdemo.config;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class RedisTemplateUtil {
    private final static List<RedisTemplate<String, Object>> LIST = new ArrayList<>();

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public static RedisTemplate<String, Object> getRedisTemplate() {
        return LIST.get(0);
    }

    @PostConstruct
    public void init() {
        LIST.add(redisTemplate);
    }
}
