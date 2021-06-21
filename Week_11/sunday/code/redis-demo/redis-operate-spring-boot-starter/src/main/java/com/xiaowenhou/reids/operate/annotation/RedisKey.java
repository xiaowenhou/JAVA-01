package com.xiaowenhou.reids.operate.annotation;


import java.lang.annotation.*;

@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface RedisKey {

    //标注参数的顺序
    int order() default 0;

    //别名
    String alias() default "";
}
