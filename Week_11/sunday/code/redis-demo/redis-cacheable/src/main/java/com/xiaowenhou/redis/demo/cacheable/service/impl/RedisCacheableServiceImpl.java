package com.xiaowenhou.redis.demo.cacheable.service.impl;

import com.xiaowenhou.redis.demo.cacheable.model.User;
import com.xiaowenhou.redis.demo.cacheable.service.RedisCacheableService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class RedisCacheableServiceImpl implements RedisCacheableService {


    @Override
//    @Cacheable(cacheNames = "findUserByNameAndEmail", key = "#name + ':' + #email")
    @Cacheable(cacheNames = "findUserByNameAndEmail")
    public User findUserByNameAndEmail(String name, String email) {

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setAge(18);
        return user;
    }

    @Override
//    @Cacheable(cacheNames = "findUser", key = "#user.name + ':' + #user.age")
    @Cacheable(cacheNames = "findUser")
    public User findUser(User user) {
        user.setEmail("2345234@qq.com");
        return user;
    }

    @Override
    @CacheEvict(cacheNames = "findUser", key = "#user.name + ':' + #user.age")
    public void setUser(User user) {
        System.out.println("do update user at database");
    }
}
