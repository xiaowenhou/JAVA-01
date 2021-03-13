package com.xiaowenhou.shardingjdbcdemo.ids;

import com.xiaowenhou.shardingjdbcdemo.config.RedisTemplateUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.spi.keygen.ShardingKeyGenerator;
import org.springframework.data.redis.core.RedisTemplate;
import java.util.Properties;

@Slf4j
public class SelfID implements ShardingKeyGenerator{

    @Getter
    @Setter
    private Properties properties = new Properties();

    @Override
    public Comparable<?> generateKey() {
        log.info("------执行了自定义主键生成器MyLagouId-------");
        RedisTemplate<String, Object> redisTemplate = RedisTemplateUtil.getRedisTemplate();
        return redisTemplate.opsForValue().increment("orderId");
    }

    @Override
    public String getType() {
        return "REDIS";
    }
}
