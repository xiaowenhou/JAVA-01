package com.xiaowenhou.redis.demo.cacheable.service.impl;

import com.xiaowenhou.redis.demo.cacheable.service.RedisIndirectService;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class RedisIndirectServiceImpl implements RedisIndirectService {


    @Override
    public String getString(String string) {
        return string + "hello";
        //报错， 因为没有pointcut
      // return  ((RedisIndirectServiceImpl)AopContext.currentProxy()).getString2(string);
    }


  /*  @Cacheable(cacheNames = "str", key = "#str")
    public String getString2(String str) {
        System.out.println("----------------------");
        return str + "hello";
    }*/
}
