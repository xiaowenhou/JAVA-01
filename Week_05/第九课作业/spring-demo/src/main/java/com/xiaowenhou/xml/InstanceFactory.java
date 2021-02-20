package com.xiaowenhou.xml;

public class InstanceFactory {

    public Teacher createTeacher() {
        return new Teacher("lisi", 35);
    }
}
