package com.xiaowenhou.redis.demo.cacheable.configuration;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;


public class CustomKeyGenerator implements KeyGenerator {
    private static final String PUBLIC_KEY = "table:user";

    @Override
    public Object generate(Object target, Method method, Object... params) {
        return PUBLIC_KEY + ":" + method.getName() + ":" +
                StringUtils.arrayToDelimitedString(params, ":");
    }
}
