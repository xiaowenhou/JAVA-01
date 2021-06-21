package com.xiaowenhou.redis.demo.aspect.service.impl;


import com.xiaowenhou.redis.demo.aspect.model.UserEntity;
import com.xiaowenhou.redis.demo.aspect.service.RedisTestService;
import com.xiaowenhou.reids.operate.annotation.AddRedis;
import com.xiaowenhou.reids.operate.annotation.ClearRedis;
import com.xiaowenhou.reids.operate.annotation.KeyPrefix;
import com.xiaowenhou.reids.operate.annotation.RedisKey;
import com.xiaowenhou.reids.operate.aspect.enums.ClearCacheStrategy;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@KeyPrefix(bizName = "umap", tableName = "User")
public class RedisTestServiceImpl implements RedisTestService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void doSomeThing(String paramA, String paramB, UserEntity entity) {
        System.out.println("at method doSomeThing, redisTemplate is : " + redisTemplate);

        firstDeal();
        ((RedisTestServiceImpl) AopContext.currentProxy()).secondDeal(paramA, paramB, entity);
    }


    @Override
    @AddRedis(expiredTime = 5, unit = TimeUnit.MINUTES, preventBreakDownTime = 3)
    @KeyPrefix(bizName = "umap", tableName = "User")
    public UserEntity getUser(@RedisKey String param, UserEntity entity) {
        UserEntity userEntity = new UserEntity();
        userEntity.setTimeStamp(System.currentTimeMillis()+"");
        userEntity.setName("lisi");
        userEntity.setEmail("lisi@163.com");
        userEntity.setAge(25);
        return userEntity;
    }

    @ClearRedis(strategy = ClearCacheStrategy.AROUND, delayTime = 200)
    void firstDeal() {
        System.out.println("Method firstDeal execute....");
    }


    @ClearRedis(strategy = ClearCacheStrategy.AROUND, delayTime = 200)
    void secondDeal(@RedisKey(alias = "param") String paramA, String paramB, UserEntity entity) {
        System.out.println("Method secondDeal execute, paramA is :" + paramA + ", paramB is :" + paramB + ", entity is : " + entity.toString());
    }
}
