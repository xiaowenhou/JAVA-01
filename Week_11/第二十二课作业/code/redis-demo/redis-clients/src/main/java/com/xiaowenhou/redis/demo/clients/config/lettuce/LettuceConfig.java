package com.xiaowenhou.redis.demo.clients.config.lettuce;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.time.Duration;

import static io.lettuce.core.ReadFrom.REPLICA_PREFERRED;

@Configuration
public class LettuceConfig {

    @Bean
    public LettuceClientConfiguration lettuceClientConfiguration() {
        return LettuceClientConfiguration.builder()
                //读写分离， 只从从库读
                .readFrom(REPLICA_PREFERRED)
                .clientName("lettuce")
                .commandTimeout(Duration.ofSeconds(3))
                .shutdownTimeout(Duration.ZERO)
                .build();
    }

    @Bean("lettuce")
    public RedisConnectionFactory lettuceConnectionFactory(RedisSentinelConfiguration sentinelConfig, LettuceClientConfiguration lettuceConfig) {
        return new LettuceConnectionFactory(sentinelConfig, lettuceConfig);
    }
}