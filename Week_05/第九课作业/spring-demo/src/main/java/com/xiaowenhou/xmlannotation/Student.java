package com.xiaowenhou.xmlannotation;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Data
@Component
public class Student {
    private String name;

    @Resource
    private Course course;
}
