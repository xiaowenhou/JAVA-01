**第十五节课作业必做题-sharding-jdbc实现分库分表**

题目：设计对前面的订单表数据进行水平分库分表，拆分2个库，每个库16张表。并在新结构在演示常见的增删改查操作。代码、sql 和配置文件，上传到 Github。



**首先， 确定分库分表规则**

​	和sharding-proxy类似， 分表规则为：

​		user表根据id % 2拆分到ds0和ds1中

​		address表根据user_id % 2 拆分到ds0和ds1中

​		order表先根据user_id % 2拆分到ds0和ds1中， 然后再根据id % 16拆分到t_order{0..15}中

其次，在代码中进行配置

​	1、在pom文件中引入sharding-jdbc的包

```pom
 <dependency>
 	<groupId>org.apache.shardingsphere</groupId>
 	<artifactId>sharding-jdbc-spring-boot-starter</artifactId>
 	<version>${shardingsphere.version}</version>
 </dependency>
```

​	2、增加配置文件

```properties
#sharding的数据源
spring.shardingsphere.datasource.names=ds0, ds1
#打印sql
spring.shardingsphere.props.sql.show=true

#ds0数据源的配置
spring.shardingsphere.datasource.ds0.type=com.zaxxer.hikari.HikariDataSource
spring.shardingsphere.datasource.ds0.driver-class-name=com.mysql.cj.jdbc.Driver
spring.shardingsphere.datasource.ds0.jdbc-url=jdbc:mysql://127.0.0.1:3306/sharding01?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false
spring.shardingsphere.datasource.ds0.username=root
spring.shardingsphere.datasource.ds0.password=zx5708923
#ds1数据源的配置
spring.shardingsphere.datasource.ds1.type=com.zaxxer.hikari.HikariDataSource
spring.shardingsphere.datasource.ds1.driver-class-name=com.mysql.cj.jdbc.Driver
spring.shardingsphere.datasource.ds1.jdbc-url=jdbc:mysql://127.0.0.1:3306/sharding02?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false
spring.shardingsphere.datasource.ds1.username=root
spring.shardingsphere.datasource.ds1.password=zx5708923

#user表的分片规则
spring.shardingsphere.sharding.tables.user.actual-data-nodes=ds$->{0..1}.user
spring.shardingsphere.sharding.tables.user.database-strategy.inline.sharding-column=id
spring.shardingsphere.sharding.tables.user.database-strategy.inline.algorithm-expression=ds$->{id % 2}
spring.shardingsphere.sharding.tables.user.key-generator.column=id
spring.shardingsphere.sharding.tables.user.key-generator.type=SNOWFLAKE
#address表的分片规则
spring.shardingsphere.sharding.tables.address.actual-data-nodes=ds$->{0..1}.address
spring.shardingsphere.sharding.tables.address.database-strategy.inline.sharding-column=user_id
spring.shardingsphere.sharding.tables.address.database-strategy.inline.algorithm-expression=ds$->{user_id % 2}
spring.shardingsphere.sharding.tables.address.key-generator.column=id
spring.shardingsphere.sharding.tables.address.key-generator.type=SNOWFLAKE
#t_order表的分片规则， 使用自定义的REDIS主键生成策略
spring.shardingsphere.sharding.tables.t_order.actual-data-nodes=ds$->{0..1}.t_order_$->{0..15}
spring.shardingsphere.sharding.tables.t_order.database-strategy.inline.sharding-column=user_id
spring.shardingsphere.sharding.tables.t_order.database-strategy.inline.algorithm-expression=ds$->{user_id % 2}
spring.shardingsphere.sharding.tables.t_order.table-strategy.inline.sharding-column=id
spring.shardingsphere.sharding.tables.t_order.table-strategy.inline.algorithm-expression=t_order_$->{id % 16}
spring.shardingsphere.sharding.tables.t_order.key-generator.column=id
spring.shardingsphere.sharding.tables.t_order.key-generator.type=REDIS

#配置默认的主键生成策略为自定义的REDIS，这两个配置必须配置， 如果不配置， 则自定义的主键生成策略不生效
spring.shardingsphere.sharding.default-key-generator.column=id
spring.shardingsphere.sharding.default-key-generator.type=REDIS
```

引入Redis作为全局的主键生成策略， 利用Redis的原子性和高性能确保主键生成的唯一性和自增性， 并且利用increment命令的连续性， 生成的主键ID%16之后能够均匀的分布在各个表中

3、加入主键生成的相关代码

```java
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

```

![image-20210313235855645](C:\Users\xiaowenhou\AppData\Roaming\Typora\typora-user-images\image-20210313235855645.png)

在resources目录下配置SPI接口的目录和文件

4、配置Redis

```java
@Configuration
public class RedisConfig {
    @Resource
    private RedisConnectionFactory factory;


    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(factory);
        return redisTemplate;
    }

}
```

由于spi接口创建的实例， 不能通过Spring容器进行管理，因此采用spring bean注入的方式在SelfID中会报NPE， 为解决这个问题， 采用曲线救国的方式， 创建一个工具类， 工具类中注入RedisTemplate对象， 并且在初始化的时候将RedisTemplate对象缓存在List中， 然后通过api对外获取该对象的引用。

```java
@Component
public class RedisTemplateUtil {
    private final static List<RedisTemplate<String, Object>> LIST = new ArrayList<>();

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public static RedisTemplate<String, Object> getRedisTemplate() {
        return LIST.get(0);
    }

    @PostConstruct
    public void init() {
        LIST.add(redisTemplate);
    }
}

```

5、执行单元测试

​	分别执行UserDaoTest， AddressDaoTest， OrderDaoTest， 结果均正常。

​	同样， 在两个库中调用以下sql

```sql
SELECT "t_order_0", COUNT(*) FROM `t_order_0` UNION
SELECT "t_order_1", COUNT(*) FROM `t_order_1` UNION
SELECT "t_order_2", COUNT(*) FROM `t_order_2` UNION
SELECT "t_order_3", COUNT(*) FROM `t_order_3` UNION
SELECT "t_order_4", COUNT(*) FROM `t_order_4` UNION
SELECT "t_order_5", COUNT(*) FROM `t_order_5` UNION
SELECT "t_order_6", COUNT(*) FROM `t_order_6` UNION
SELECT "t_order_7", COUNT(*) FROM `t_order_7` UNION
SELECT "t_order_8", COUNT(*) FROM `t_order_8` UNION
SELECT "t_order_9", COUNT(*) FROM `t_order_9` UNION
SELECT "t_order_10", COUNT(*) FROM `t_order_10` UNION
SELECT "t_order_11", COUNT(*) FROM `t_order_11` UNION
SELECT "t_order_12", COUNT(*) FROM `t_order_12` UNION
SELECT "t_order_13", COUNT(*) FROM `t_order_13` UNION
SELECT "t_order_14", COUNT(*) FROM `t_order_14` UNION
SELECT "t_order_15", COUNT(*) FROM `t_order_15`;
```

结果分别为：

​	![image-20210314000953297](C:\Users\xiaowenhou\AppData\Roaming\Typora\typora-user-images\image-20210314000953297.png)

![image-20210314001023896](C:\Users\xiaowenhou\AppData\Roaming\Typora\typora-user-images\image-20210314001023896.png)

可以看到， 数据几乎平均的分布到了各个表中， 解决了数据倾斜的问题

