package com.xiaowenhou.tcc.demo.source;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({"classpath:spring-dubbo.xml"})
@MapperScan("com.xiaowenhou.tcc.demo.source.dao")
public class DubboHmilySourceApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(DubboHmilySourceApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }
}
