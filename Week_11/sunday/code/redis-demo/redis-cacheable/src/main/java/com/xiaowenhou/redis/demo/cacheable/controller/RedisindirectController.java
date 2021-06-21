package com.xiaowenhou.redis.demo.cacheable.controller;

import com.xiaowenhou.redis.demo.cacheable.service.RedisIndirectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/indirect")
public class RedisindirectController {



    @Autowired
    private RedisIndirectService indirectService;

    @GetMapping("/string")
    public String getString() {
        return indirectService.getString("hehehe");
    }

}
