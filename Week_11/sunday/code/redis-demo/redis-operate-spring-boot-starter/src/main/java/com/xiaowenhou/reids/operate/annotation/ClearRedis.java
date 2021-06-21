package com.xiaowenhou.reids.operate.annotation;

import com.xiaowenhou.reids.operate.aspect.enums.ClearCacheStrategy;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;


@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ClearRedis {

    ClearCacheStrategy strategy() default ClearCacheStrategy.BEFORE;

    long delayTime() default 0;

    //单位
    TimeUnit unit() default TimeUnit.MILLISECONDS;
}
