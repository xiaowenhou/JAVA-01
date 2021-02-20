package com.xiaowenhou.xml;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestMain {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        Student student = (Student) applicationContext.getBean("student");
        System.out.println(student);


        User user = (User) applicationContext.getBean("user");
        System.out.println(user);

        Teacher teacher = (Teacher) applicationContext.getBean("teacher");
        System.out.println(teacher);
    }
}