package com.xiaowenhou.redis.demo.aspect.service;

import com.xiaowenhou.redis.demo.aspect.model.UserEntity;

public interface RedisTestService {

    void doSomeThing(String paramA, String paramB, UserEntity entity);


    UserEntity getUser(String param, UserEntity entity);
}
