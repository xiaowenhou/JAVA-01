package com.xiaowenhou.reids.operate.annotation;

import java.lang.annotation.*;

@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface KeyPrefix {

    //业务名或者数据库名
    String bizName() default "";

    //表名
    String tableName() default "";
}
