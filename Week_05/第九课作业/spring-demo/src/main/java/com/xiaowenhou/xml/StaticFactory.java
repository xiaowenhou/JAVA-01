package com.xiaowenhou.xml;

/**
 * 模拟静态工厂， 用于创建对象
 */
public class StaticFactory {

    public static User createStudent() {
        return new User("zhangsan", 18);
    }
}
