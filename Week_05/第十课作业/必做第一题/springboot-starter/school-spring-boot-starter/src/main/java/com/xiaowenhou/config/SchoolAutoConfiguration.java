package com.xiaowenhou.config;

import com.xiaowenhou.beans.Klass;
import com.xiaowenhou.beans.Student;
import com.xiaowenhou.impl.School;
import com.xiaowenhou.interfaces.ISchool;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Configuration
@ConditionalOnClass
public class SchoolAutoConfiguration {
    static {
        System.out.println("SchoolAutoConfiguration init....");
    }

    @Resource(name = "student100")
    private Student student100;

    @Resource(name = "student123")
    private Student student123;

    @Bean("student100")
    public Student studentBeanFirst() {
        return new Student(100, "xiaowenhou100");
    }

    @Bean("student123")
    public Student studentBeanSecond() {
        return new Student(123, "xiaowenhou123");
    }

    @Bean
    public Klass klassBean() {
        Klass klass = new Klass();
        List<Student> list = new ArrayList<>();
        list.add(student100);
        list.add(student123);
        klass.setStudents(list);
        return klass;

    }

    @Bean
    public ISchool schooolBean(Klass klass) {
        School school = new School();
        school.setClass1(klass);
        school.setStudent100(student100);
        return school;
    }
}
