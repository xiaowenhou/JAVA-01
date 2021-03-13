package com.xiaowenhou.shardingproxydemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.xiaowenhou.shardingproxydemo.dao")
public class ShardingProxyDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShardingProxyDemoApplication.class, args);
    }

}
