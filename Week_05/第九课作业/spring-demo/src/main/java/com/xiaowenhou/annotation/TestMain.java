package com.xiaowenhou.annotation;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.xiaowenhou.annotation")
public class TestMain {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(TestMain.class);
        Student student = applicationContext.getBean(Student.class);
        System.out.println(student);
        student.getCourse().printCourse();
    }
}
