package com.xiaowenhou.reids.operate.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;


@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AddRedis {
    //过期时间
    long expiredTime() default 0;

    //单位
    TimeUnit unit() default TimeUnit.SECONDS;

    //防止缓存击穿，在key的过期时间上随机加一段
    long preventBreakDownTime() default 0;
}
