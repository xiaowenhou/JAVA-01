package com.xiaowenhou.redis.demo.counter.test.hash;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class RedisHashTest {
    private final static String KEY = "SecondKillGoods";

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @BeforeEach
    public void testInit() {
        Map<String, Integer> goodsMap = new HashMap<>();
        goodsMap.put("goodsA", 100);
        goodsMap.put("goodsB", 20);
        goodsMap.put("goodsC", 15);
        goodsMap.put("goodsD", 35);

        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        goodsMap.forEach((k, v) -> hashOperations.put(KEY, k, v));
    }

    @Test
    public void testIncrementHash() {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.increment(KEY, "goodsA", -1);
        hashOperations.increment(KEY, "goodsB", -1);
        hashOperations.increment(KEY, "goodsC", -1);

        Integer goodsAValue = (Integer) hashOperations.get(KEY, "goodsA");
        Integer goodsBValue = (Integer) hashOperations.get(KEY, "goodsB");
        Integer goodsCValue = (Integer) hashOperations.get(KEY, "goodsC");

        Assertions.assertEquals(99, goodsAValue);
        Assertions.assertEquals(19, goodsBValue);
        Assertions.assertEquals(14, goodsCValue);

    }

    @Test
    public void testGetHash() {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        Map<Object, Object> objectMap = hashOperations.entries(KEY);
        objectMap.forEach((k, v) -> System.out.println("key is : " + k + ", value is : " + v));
        Assertions.assertEquals(100, objectMap.get("goodsA"));
        Assertions.assertEquals(15, objectMap.get("goodsC"));
    }

    @Test
    public void testLua() {
//        redisTemplate.execute()

    }

    @AfterEach
    public void testClear() {
        Boolean clearResult = redisTemplate.delete(KEY);
        Assertions.assertEquals(true, clearResult);
    }
}