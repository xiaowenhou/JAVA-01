package com.xiaowenhou.redis.demo.clients.controller;

import com.xiaowenhou.redis.demo.clients.entity.User;
import com.xiaowenhou.redis.demo.clients.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/redis")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/{redisClientType}/{userId}")
    public User getUser(@PathVariable("redisClientType") String redisClientType,
                        @PathVariable("userId") Integer userId) {
        return userService.findById(userId, redisClientType);
    }
}
