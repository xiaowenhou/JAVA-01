package com.xiaowenhou.tcc.demo.target;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:spring-dubbo.xml")
@MapperScan("com.xiaowenhou.tcc.demo.target.dao")
public class DubboHmilyTargetApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(DubboHmilyTargetApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }
}
