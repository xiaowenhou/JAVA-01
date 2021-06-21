package com.xiaowenhou.redis.demo.counter.service.impl;

import com.xiaowenhou.redis.demo.counter.constant.RedisKeyConstant;
import com.xiaowenhou.redis.demo.counter.entity.User;
import com.xiaowenhou.redis.demo.counter.service.RedisCounterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class RedisCounterServiceImpl implements RedisCounterService {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private final static AtomicInteger COUNTER = new AtomicInteger(0);


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

    @Override
    public void initInventory() {
        log.info("init inventory...");
        Map<String, Integer> goodsMap = new HashMap<>();
        goodsMap.put("goodsA", 100);
        goodsMap.put("goodsB", 20);
        goodsMap.put("goodsC", 15);
        goodsMap.put("goodsD", 35);

        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.putAll(RedisKeyConstant.GOODS_KEY, goodsMap);
    }

    @Override
    public void clearInventory() {
        log.info("clear inventory...");
        redisTemplate.delete(RedisKeyConstant.GOODS_KEY);
    }

    @Override
    public boolean decrementInventory() {
        String key = getRandomKey();
        Long inventoryNumber = redisTemplate.opsForHash().increment(RedisKeyConstant.GOODS_KEY, key, -1);

        //成功记录一条， 模拟后续操作， 失败不处理， 模拟返回失败
        if (inventoryNumber >= 0) {
            COUNTER.incrementAndGet();
            return true;
        }
        return false;
    }

    @Override
    public int countResult() {
        return COUNTER.get();
    }


    private String getRandomKey() {
        Map<Object, Object> objectMap = redisTemplate.opsForHash().entries(RedisKeyConstant.GOODS_KEY);
        Set<Object> keySet = objectMap.keySet();
        if (CollectionUtils.isEmpty(keySet)) {
            return "";
        }
        Random random = new Random();
        return (String) keySet.toArray()[random.nextInt(keySet.size())];
    }
}
