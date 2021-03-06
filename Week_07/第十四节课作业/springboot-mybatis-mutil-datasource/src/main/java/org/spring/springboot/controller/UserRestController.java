package org.spring.springboot.controller;

import org.spring.springboot.domain.User;
import org.spring.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户控制层
 *
 * @author xiaowenhou
 */
@RestController
public class UserRestController {

    @Resource
    private UserService userService;

    @RequestMapping(value = "/api/user", method = RequestMethod.POST)
    public String insert(@RequestParam(value = "name", required = true) String userName) {
         userService.saveUser(userName);
         return "success";
    }

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public User findUser(@RequestParam(value = "name") String name) {
        return userService.findByName(name);
    }

}
