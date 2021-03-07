package com.xiaowenhou.dynamicmultipledatasource.controller;

import com.xiaowenhou.dynamicmultipledatasource.entity.User;
import com.xiaowenhou.dynamicmultipledatasource.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping(value = "/api/user", method = RequestMethod.POST)
    public String insert(@RequestParam(value = "name", required = true) String userName) {
        userService.saveUser(userName);
        return "success";
    }

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public List<User> findUsers() {
        return userService.findUsers();
    }
}
