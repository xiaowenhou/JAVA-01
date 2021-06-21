package com.xiaowenhou.redis.demo.counter.service;

import com.xiaowenhou.redis.demo.counter.entity.User;

public interface RedisCounterService {

    User findById(Integer id);


    void initInventory();
    void clearInventory();


    boolean decrementInventory();


    int countResult();

}
