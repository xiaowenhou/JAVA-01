package com.xiaowenhou.redis.demo.clients.config.jedis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;


@Configuration
public class JedisConfig {
    /**
     * 连接池配置
     *
     * @return
     */
    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        //连接池最大连接数
        poolConfig.setMaxTotal(16);
        //连接池分配连接最大阻塞等待时间
        poolConfig.setMaxWaitMillis(30 * 60 * 1000);
        //连接池中的最大空闲连接数
        poolConfig.setMaxIdle(8);
        //连接池中的最小空闲连接数
        poolConfig.setMinIdle(8);
        return poolConfig;
    }

    @Bean("jedis")
    @Primary
    public RedisConnectionFactory jedisConnectionFactory(RedisSentinelConfiguration sentinelConfig, JedisPoolConfig poolConfig) {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(sentinelConfig, poolConfig);
        //必须要加这行代码，该方法将之前设置的属性写入到配置中，主要是pool这个属性，会将sentinel相关的配置写入到masterListener中，否则客户端使用的连接是默认的standaloneConfiguration，即localhost:6379
        jedisConnectionFactory.afterPropertiesSet();

        return jedisConnectionFactory;
    }
}
