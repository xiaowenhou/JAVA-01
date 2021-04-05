package com.xiaowenhou.tcc.demo.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@MapperScan("com.xiaowenhou.tcc.demo.order.dao")
@ImportResource({"classpath:spring-dubbo.xml"})
@SpringBootApplication
public class DubboHmilyOrderApplication {
    public static void main(final String[] args) {
        SpringApplication.run(DubboHmilyOrderApplication.class, args);
    }
}