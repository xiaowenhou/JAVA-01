package com.xiaowenhou.redis.demo.cacheable.controller;

import com.xiaowenhou.redis.demo.cacheable.model.User;
import com.xiaowenhou.redis.demo.cacheable.service.RedisCacheableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisCacheableController {

    @Autowired
    private RedisCacheableService service;

    @GetMapping("/parameter")
    public User getUser() {
        return service.findUserByNameAndEmail("zhangsan", "12341235@qq.com");
    }

    @GetMapping("/user")
    public User getUser1() {
        User user = new User();
        user.setName("lisi");
        user.setAge(25);
        user.setEmail("asjdfkljasd@126.com");
        return service.findUser(user);
    }

    @GetMapping("/user2")
    public User getUser2() {
        User user = new User();
        user.setName("");
        user.setAge(25);
        return service.findUser(user);
    }

    @GetMapping("/user3")
    public User getUser3() {
        User user = new User();
        user.setName(null);
        user.setAge(25);
        return service.findUser(user);
    }


    @GetMapping("/setUser")
    public String setUser() {
        User user = new User();
        user.setName("");
        user.setAge(25);
        service.setUser(user);
        return "success";
    }

}
