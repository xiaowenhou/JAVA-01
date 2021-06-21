package com.xiaowenhou.redis.demo.aspect.controller;

import com.xiaowenhou.redis.demo.aspect.model.UserEntity;
import com.xiaowenhou.redis.demo.aspect.service.RedisTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisTestController {

    private final RedisTestService redisTestService;

    @Autowired
    public RedisTestController(RedisTestService redisTestService) {
        this.redisTestService = redisTestService;
    }

    @GetMapping("/redis/aspect/delete")
    public String testRedisAspect() {
        UserEntity entity = new UserEntity();
        entity.setAge(16);
        entity.setName("lisi");
        entity.setEmail("25345432523@qq.com");
        entity.setTimeStamp("1111111");
        redisTestService.doSomeThing("hehehe", "paramB", entity);

        return "success";
    }


    @GetMapping("/redis/aspect/add")
    public UserEntity getUserEntity() {
        UserEntity entity = new UserEntity();
        entity.setAge(16);
        entity.setName("lisi");
        entity.setEmail("25345432523@qq.com");
        entity.setTimeStamp("1111111");


        return redisTestService.getUser("hehehe", entity);
    }
}
