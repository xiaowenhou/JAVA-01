package com.xiaowenhou.reids.operate.aspect;

import com.xiaowenhou.reids.operate.annotation.AddRedis;
import com.xiaowenhou.reids.operate.key.RedisKeyGenerator;
import com.xiaowenhou.reids.operate.utils.ProceedingJoinPointUtils;
import io.netty.util.internal.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Objects;


@Slf4j
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class AddRedisCacheAspect {


    private final RedisTemplate<String, Object> redisTemplate;

    public AddRedisCacheAspect(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Pointcut("@annotation(com.xiaowenhou.reids.operate.annotation.AddRedis) " +
            "|| @within(com.xiaowenhou.reids.operate.annotation.AddRedis)")
    public void addRedisCache() {
    }

    //需求：定义一个切面， 在方法上添加CLearRedisCache注解， 切面在该方法执行前后自动删除缓存
    //在方法的参数和和参数的字段上加上RedisKey注解，能够自动按照规则生成Redis的key
    @Around("addRedisCache()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        String redisKey = RedisKeyGenerator.generate(point);
        if (log.isDebugEnabled()) {
            log.debug("AddRedisCacheAspect-[around], redisKey is : {}", redisKey);
        }
        Object redisResult = redisTemplate.opsForValue().get(redisKey);
        if (!Objects.isNull(redisResult)) {
            return redisResult;
        }

        Object result = point.proceed();

        setRedis(point, redisKey, result);

        return result;
    }


    private void setRedis(ProceedingJoinPoint point, String redisKey, Object result) {
        AddRedis addRedis = ProceedingJoinPointUtils.getMethodAnnotation(point, AddRedis.class);
        if (addRedis.expiredTime() <= 0) {
            redisTemplate.opsForValue().set(redisKey, result);
        } else {
            long expiredTime;
            if (addRedis.preventBreakDownTime() > 0) {
                long preventBreakDownTime = ThreadLocalRandom.current().nextLong(0, addRedis.preventBreakDownTime());
                expiredTime = addRedis.expiredTime() + preventBreakDownTime;
            } else {
                expiredTime = addRedis.expiredTime();
            }
            redisTemplate.opsForValue().set(redisKey, result, expiredTime, addRedis.unit());
        }
    }
}
