package com.xiaowenhou.redis.demo.cacheable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@SpringBootApplication
public class RedisCacheableApplication {
    public static void main(String[] args) {
        SpringApplication.run(RedisCacheableApplication.class, args);
    }
}
