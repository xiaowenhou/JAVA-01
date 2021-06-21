package com.xiaowenhou.redis.demo.cacheable.service;

import com.xiaowenhou.redis.demo.cacheable.model.User;

public interface RedisCacheableService {

    User findUserByNameAndEmail(String name, String email);

    User findUser(User user);


    void setUser(User user);
}
