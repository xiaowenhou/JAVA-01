package com.xiaowenhou.reids.operate.aspect;

import com.xiaowenhou.reids.operate.annotation.ClearRedis;
import com.xiaowenhou.reids.operate.aspect.enums.ClearCacheStrategy;
import com.xiaowenhou.reids.operate.key.RedisKeyGenerator;
import com.xiaowenhou.reids.operate.utils.ProceedingJoinPointUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;


@Slf4j
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ClearRedisCacheAspect {

    private final RedisTemplate<String, Object> redisTemplate;

    public ClearRedisCacheAspect(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Pointcut("@annotation(com.xiaowenhou.reids.operate.annotation.ClearRedis) " +
            "|| @within(com.xiaowenhou.reids.operate.annotation.ClearRedis)")
    public void clearRedisPointCut() {
    }

    //需求：定义一个切面， 在方法上添加CLearRedisCache注解， 切面在该方法执行前后自动删除缓存
    //在方法的参数和和参数的字段上加上RedisKey注解，能够自动按照规则生成Redis的key
    @Around("clearRedisPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        String redisKey = RedisKeyGenerator.generate(point);
        log.info("clear redis cache, redisKey is : {}", redisKey);

        ClearRedis clearRedis = ProceedingJoinPointUtils.getMethodAnnotation(point, ClearRedis.class);
        ClearCacheStrategy strategy = clearRedis.strategy();

        //如果不是指定方法执行后删除， 则先删除一遍redis
        if (!strategy.equals(ClearCacheStrategy.AFTER)) {
            log.info("before method execute, clear redis......");
            redisTemplate.delete(redisKey);
        }

        Object result = point.proceed();
        long before = System.currentTimeMillis();
        //指定了延迟双删， 则线程休眠相应的时间
        if (strategy.equals(ClearCacheStrategy.AROUND)) {
            clearRedis.unit().sleep(clearRedis.delayTime());
            log.info("thread delay {} {}", System.currentTimeMillis() - before, clearRedis.unit());
        }

        //如果不是指定方法执行前删除， 则继续删一遍redis
        if (!strategy.equals(ClearCacheStrategy.BEFORE)) {
            redisTemplate.delete(redisKey);
            log.info("after method execute, clear redis......");
        }

        return result;
    }
}
