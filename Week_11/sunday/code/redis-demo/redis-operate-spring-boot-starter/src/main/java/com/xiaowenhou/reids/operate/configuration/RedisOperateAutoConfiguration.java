package com.xiaowenhou.reids.operate.configuration;

import com.xiaowenhou.reids.operate.aspect.AddRedisCacheAspect;
import com.xiaowenhou.reids.operate.aspect.ClearRedisCacheAspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author xiaowenhou
 */
@Configuration
@ConditionalOnClass(RedisOperateAutoConfiguration.class)
@ConditionalOnBean(RedisTemplate.class)
public class RedisOperateAutoConfiguration {
    private final Logger log = LoggerFactory.getLogger(RedisOperateAutoConfiguration.class);

    @Bean
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public ClearRedisCacheAspect createAspect(RedisTemplate<String, Object> redisTemplate) {
        log.info("at create clear aspect Aspect, redisTemplate is : {}", redisTemplate);

        return new ClearRedisCacheAspect(redisTemplate);
    }


    @Bean
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public AddRedisCacheAspect createAddAspect(RedisTemplate<String, Object> redisTemplate) {
        log.info("at create add aspect, redisTemplate is : {}", redisTemplate);
        return new AddRedisCacheAspect(redisTemplate);
    }
}
