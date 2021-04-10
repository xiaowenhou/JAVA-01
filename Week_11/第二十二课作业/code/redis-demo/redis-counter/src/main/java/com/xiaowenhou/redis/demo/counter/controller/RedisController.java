package com.xiaowenhou.redis.demo.counter.controller;

import com.xiaowenhou.redis.demo.counter.entity.User;
import com.xiaowenhou.redis.demo.counter.service.RedisCounterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/counter")
public class RedisController {

    @Resource
    private RedisCounterService counterService;


    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") Integer userId) {
        return counterService.findById(userId);
    }

}
