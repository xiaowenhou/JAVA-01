package com.xiaowenhou.redis.demo.counter.service.impl;

import com.xiaowenhou.redis.demo.counter.entity.User;
import com.xiaowenhou.redis.demo.counter.service.RedisCounterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RedisCounterServiceImpl implements RedisCounterService {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public User findById(Integer id) {
        //先查缓存
        User user = (User) redisTemplate.opsForValue().get("user" + "." + id);

        if (Objects.isNull(user)) {
            log.info("cache not hint...");
            //模拟查数据库
            user = new User();
            user.setId(id);
            user.setName("Tom");
            user.setAge(27);

            //没有命中， 写入缓存
            redisTemplate.opsForValue().set("user" + "." + id, user, 180, TimeUnit.SECONDS);
        }

        return user;
    }
}
