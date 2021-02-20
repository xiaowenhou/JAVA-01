package com.xiaowenhou.xmlannotation;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Course {

    private String courseName;


    public void printCourse() {
        System.out.println("This is my course...");
    }
}
