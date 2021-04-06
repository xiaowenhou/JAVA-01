package com.xiaowenhou.redis.demo.clients.service;

import com.xiaowenhou.redis.demo.clients.entity.User;

public interface UserService {
    User findById(Integer id, String redisClientKey);
}
