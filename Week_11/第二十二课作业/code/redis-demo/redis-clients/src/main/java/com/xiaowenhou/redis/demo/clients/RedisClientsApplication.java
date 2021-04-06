package com.xiaowenhou.redis.demo.clients;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class RedisClientsApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisClientsApplication.class, args);
    }
}
