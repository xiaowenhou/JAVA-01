package com.xiaowenhou.startertest.controller;

import com.xiaowenhou.beans.Klass;
import com.xiaowenhou.interfaces.ISchool;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class StarterTestController {

    @Resource
    private ISchool school;

    @Resource
    private Klass klass;

    @RequestMapping("/test")
    public String testStarter() {
        school.ding();
        klass.dong();
        return "success";
    }
}
