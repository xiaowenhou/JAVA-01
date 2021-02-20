package com.xiaowenhou.impl;

import com.xiaowenhou.beans.Klass;
import com.xiaowenhou.beans.Student;
import com.xiaowenhou.interfaces.ISchool;
import lombok.Data;

@Data
public class School implements ISchool {

    private Klass class1;

    private Student student100;

    @Override
    public void ding() {

        System.out.println("Class1 have " + this.class1.getStudents().size() + " students and one is " + this.student100);

    }

}
