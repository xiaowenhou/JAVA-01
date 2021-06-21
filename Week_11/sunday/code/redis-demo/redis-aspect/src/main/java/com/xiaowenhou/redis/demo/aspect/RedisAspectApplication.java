package com.xiaowenhou.redis.demo.aspect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication
public class RedisAspectApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisAspectApplication.class, args);
    }
}
