**第二十二课选做题：Redis多客户端集成SpringBoot**

题目：分别基于 jedis，RedisTemplate，Lettuce，Redission 实现 redis 基本操作的 demo，可以使用 spring-boot 集成上述工具。

项目结构：

​	![image-20210406223051888](C:\Users\xiaowenhou\AppData\Roaming\Typora\typora-user-images\image-20210406223051888.png)

关键类： 

​	1、RedisConfig   -----  >  RedisTemplate相关的配置

```java
@Configuration
public class RedisConfig{
    /**
     * sentinel配置
     *
     * @return
     */
    @Bean
    public RedisSentinelConfiguration redisSentinelConfiguration() {
        return new RedisSentinelConfiguration()
                .master("test-master")
                .sentinel("192.168.198.100", 26379)
                .sentinel("192.168.198.110", 26379)
                .sentinel("192.168.198.120", 26379);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(@Qualifier("jedis") RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // 使用Jackson2JsonRedisSerialize 替换默认的jdkSerializeable序列化
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }
}
```

​	2、JedisConfig   ---->Jedis配置

```java
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
```

​	3、LettuceConfig  ----->Lettuce配置

```java
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
```

​	4、RedissonConfig   ------>Redisson配置

```java
@Configuration
public class RedissonConfig {

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient(@Value("classpath:/redisson.yml") Resource configFile) throws IOException {
        Config config = Config.fromYAML(configFile.getInputStream());
        return Redisson.create(config);
    }

    @Bean("redisson")
    public RedisConnectionFactory redissonConnectionFactory(RedissonClient redisson) {
        return new RedissonConnectionFactory(redisson);
    }
}
```

redisson.yml

```yaml
sentinelServersConfig:
  idleConnectionTimeout: 10000
  connectTimeout: 10000
  timeout: 3000
  retryAttempts: 3
  retryInterval: 1500
  failedSlaveReconnectionInterval: 3000
  failedSlaveCheckInterval: 60000
  password: null
  subscriptionsPerConnection: 5
  clientName: null
  loadBalancer: !<org.redisson.connection.balancer.RoundRobinLoadBalancer> {}
  subscriptionConnectionMinimumIdleSize: 1
  subscriptionConnectionPoolSize: 16
  slaveConnectionMinimumIdleSize: 4
  slaveConnectionPoolSize: 4
  masterConnectionMinimumIdleSize: 8
  masterConnectionPoolSize: 8
  readMode: "SLAVE"
  subscriptionMode: "SLAVE"
  sentinelAddresses:
    - "redis://192.168.198.100:26379"
    - "redis://192.168.198.110:26379"
    - "redis://192.168.198.120:26379"
  masterName: "test-master"
  database: 0
threads: 16
nettyThreads: 32
codec: !<org.redisson.codec.MarshallingCodec> {}
transportMode: "NIO"
```

5、RedisTemplateFactory -------> RedisTemplateFactory工厂类， 将各个客户端的RedisConnectionFactory封装到			RedisTemplateFactory，后续可以改为配置方式， 灵活使用各种客户端

```java
@Component
public class RedisTemplateFactory {

    @Resource(name = "jedis")
    private RedisConnectionFactory jedis;
    @Resource(name = "lettuce")
    private RedisConnectionFactory lettuce;
    @Resource(name = "redisson")
    private RedisConnectionFactory redisson;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    private final static Map<String, RedisConnectionFactory> REDIS_TEMPLATE_MAP = new HashMap<>();

    @PostConstruct
    public void init() {
        REDIS_TEMPLATE_MAP.put("jedis", jedis);
        REDIS_TEMPLATE_MAP.put("lettuce", lettuce);
        REDIS_TEMPLATE_MAP.put("redisson", redisson);
    }

    /**
    	设置connectionFactory，默认为jedis，根据传进来的参数确定用哪个客户端
    */
    public RedisTemplate<String, Object> getRedisTemplate(String key) {
        RedisConnectionFactory connectionFactory = REDIS_TEMPLATE_MAP.get(key);
        if (Objects.isNull(connectionFactory)) {
            return redisTemplate;
        }
        this.redisTemplate.setConnectionFactory(connectionFactory);
        this.redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

}
```

6、UserServiceImpl和UserController

```java
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private RedisTemplateFactory templateFactory;

    @Override
    public User findById(Integer id, String redisClientKey) {
        RedisTemplate<String, Object> redisTemplate = templateFactory.getRedisTemplate(redisClientKey);
        //先查缓存
        User user = (User) redisTemplate.opsForValue().get(redisClientKey + "." + id);

        System.out.println("connection factory is : " + Objects.requireNonNull(redisTemplate.getConnectionFactory()).toString());
        if (Objects.isNull(user)) {
            log.info("cache not hint...");
            //模拟查数据库
            user = new User();
            user.setId(id);
            user.setName("Tom");
            user.setAge(27);

            //没有命中， 写入缓存
            redisTemplate.opsForValue().set(redisClientKey + "." + id, user, 180, TimeUnit.SECONDS);
        }

        return user;
    }
}
```

```java
@RestController
@RequestMapping("/redis")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/{redisClientType}/{userId}")
    public User getUser(@PathVariable("redisClientType") String redisClientType,
                        @PathVariable("userId") Integer userId) {
        return userService.findById(userId, redisClientType);
    }
}
```

执行结果：

**connection factory is : org.springframework.data.redis.connection.jedis.JedisConnectionFactory@43b74cd3**
2021-04-06 21:57:49.820  INFO 5180 --- [nio-8088-exec-2] c.x.r.d.c.service.impl.UserServiceImpl   : cache not hint...
**connection factory is : org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory@24b3afd1**
2021-04-06 21:57:54.987  INFO 5180 --- [nio-8088-exec-1] c.x.r.d.c.service.impl.UserServiceImpl   : cache not hint...
connection factory is : org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory@24b3afd1
connection factory is : org.springframework.data.redis.connection.jedis.JedisConnectionFactory@43b74cd3
connection factory is : org.redisson.spring.data.connection.RedissonConnectionFactory@6455ef11
2021-04-06 21:58:27.011  INFO 5180 --- [nio-8088-exec-5] c.x.r.d.c.service.impl.UserServiceImpl   : cache not hint...
**connection factory is : org.redisson.spring.data.connection.RedissonConnectionFactory@6455ef11**
connection factory is : org.redisson.spring.data.connection.RedissonConnectionFactory@6455ef11

![未命名截图](C:\Users\xiaowenhou\Desktop\未命名截图.png)

优化点：

​	1、改进UserServiceImpl中使用缓存的方式， 将CacheManager和Redis进行集成

​	2、当前是在接口的参数中指定使用的客户端， 但可以改造为通过配置文件指定或者随机确定使用哪个客户端