package com.xiaowenhou.xmlannotation;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestMain {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContextAnnotation.xml");
        Student student = (Student) applicationContext.getBean("student");
        System.out.println(student);
        student.getCourse().printCourse();
    }
}
