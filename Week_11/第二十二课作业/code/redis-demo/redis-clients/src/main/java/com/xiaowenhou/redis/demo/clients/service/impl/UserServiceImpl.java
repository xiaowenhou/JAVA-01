package com.xiaowenhou.redis.demo.clients.service.impl;

import com.xiaowenhou.redis.demo.clients.entity.User;
import com.xiaowenhou.redis.demo.clients.factory.RedisTemplateFactory;
import com.xiaowenhou.redis.demo.clients.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private RedisTemplateFactory templateFactory;

    @Override
    public User findById(Integer id, String redisClientKey) {
        RedisTemplate<String, Object> redisTemplate = templateFactory.getRedisTemplate(redisClientKey);
        //先查缓存
        User user = (User) redisTemplate.opsForValue().get(redisClientKey + "." + id);

        System.out.println("connection factory is : " + Objects.requireNonNull(redisTemplate.getConnectionFactory()).toString());
        if (Objects.isNull(user)) {
            log.info("cache not hint...");
            //模拟查数据库
            user = new User();
            user.setId(id);
            user.setName("Tom");
            user.setAge(27);

            //没有命中， 写入缓存
            redisTemplate.opsForValue().set(redisClientKey + "." + id, user, 180, TimeUnit.SECONDS);
        }

        return user;
    }
}
