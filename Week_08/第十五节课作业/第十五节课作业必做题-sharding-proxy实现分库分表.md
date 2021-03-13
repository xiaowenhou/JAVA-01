**第十五节课作业必做题-sharding-proxy实现**

题目：设计对前面的订单表数据进行水平分库分表，拆分2个库，每个库16张表。并在新结构上演示常见的增删改查操作。代码、sql 和配置文件，上传到 Github。

**首先， 确定拆分规则**

​	分析库中订单表和其他表之间的关系后，发现和订单表关联的其他表的字段为用户表ID（user_id）和地址表ID(address_id)，并且地址表也和用户表ID（user_id）关联， 为了避免跨库join的出现， 因此在拆分的时候根据user_id对订单表， 用户表，地址表进行数据库的拆分， 然后再对订单表根据订单ID进行分表。

​	因此，user表根据id % 2拆分到ds0和ds1中

​				address表根据user_id % 2 拆分到ds0和ds1中

​				order表先根据user_id % 2拆分到ds0和ds1中， 然后再根据id % 16拆分到t_order{0..15}中

**其次，根据拆分规则对sharding-proxy进行配置并启动**

server.yaml配置为：

```yaml
authentication:
  users:
    root:
      password: sharding
    sharding:
#      password: sharding 
      authorizedSchemas: homework_db
#
props:
  max.connections.size.per.query: 1
  acceptor.size: 16  # The default value is available processors count * 2.
  executor.size: 16  # Infinite by default.
  proxy.frontend.flush.threshold: 128  # The default value is 128.
#    # LOCAL: Proxy will run with LOCAL transaction.
#    # XA: Proxy will run with XA transaction.
#    # BASE: Proxy will run with B.A.S.E transaction.
  proxy.transaction.type: LOCAL
  proxy.opentracing.enabled: false
  proxy.hint.enabled: false
  query.with.cipher.column: true
  sql.show: true
  allow.range.query.with.inline.sharding: false
```

config-sharding.yaml配置为：

```yaml
schemaName: homework_db
#
dataSources:
  ds_0:
    url: jdbc:mysql://127.0.0.1:3306/sharding01?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false
    username: root
    password: zx5708923
    connectionTimeoutMilliseconds: 30000
    idleTimeoutMilliseconds: 60000
    maxLifetimeMilliseconds: 1800000
    maxPoolSize: 16
    minPoolSize: 8
    maintenanceIntervalMilliseconds: 30000
  ds_1:
    url: jdbc:mysql://127.0.0.1:3306/sharding02?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false
    username: root
    password: zx5708923
    connectionTimeoutMilliseconds: 30000
    idleTimeoutMilliseconds: 60000
    maxLifetimeMilliseconds: 1800000
    maxPoolSize: 16
    minPoolSize: 8
    maintenanceIntervalMilliseconds: 30000

shardingRule:
  tables:
    t_order:
      actualDataNodes: ds_${0..1}.t_order_${0..15}
      databaseStrategy:
        inline:
          shardingColumn: user_id
          algorithmExpression: ds_${user_id % 2}
      tableStrategy:
        inline:
          shardingColumn: id
          algorithmExpression: t_order_${id % 16}
      keyGenerator:
        type: SNOWFLAKE
        column: id
    user:
      actualDataNodes: ds_${0..1}.user
      databaseStrategy:
        inline:
          shardingColumn: id
          algorithmExpression: ds_${id % 2}
      keyGenerator:
        type: SNOWFLAKE
        column: id
    address:
      actualDataNodes: ds_${0..1}.address
      databaseStrategy:
        inline:
          shardingColumn: user_id
          algorithmExpression: ds_${user_id % 2}
      keyGenerator:
        type: SNOWFLAKE
        column: id
```

通过

```
mysql -h 127.0.0.1 -P 3307 -u root -p
```

命令连接到sharding-proxy代理上， 查看库表如下：

![image-20210311204013633](C:\Users\xiaowenhou\AppData\Roaming\Typora\typora-user-images\image-20210311204013633.png)

**最后， 创建项目工程， 在工程中操作虚拟表**

配置文件：

```yaml

#mybatis
mybatis-plus:
  mapper-locations: classpath*:/mapper/*.xml
  typeAliasesPackage: com.xiaowenhou.shardingproxydemo.entity.*
  global-config:
    #db-config:
     # id-type: AUTO
    banner: false
  configuration:
    map-underscore-to-camel-case: true
    cache-enabled: false
    call-setters-on-nulls: true
    jdbc-type-for-null: 'null'

#数据源配置， 这里配置成sharding-proxy的连接和用户名密码
spring:
  datasource:
    url: jdbc:mysql://localhost:3307/homework_db?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: sharding
```

项目结构：

![image-20210311204154226](C:\Users\xiaowenhou\AppData\Roaming\Typora\typora-user-images\image-20210311204154226.png)

用mybatis-plus简化模板代码的开发和编写，测试类的代码为：

```java
package com.xiaowenhou.shardingproxydemo.dao;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaowenhou.shardingproxydemo.entity.Address;
import com.xiaowenhou.shardingproxydemo.entity.Order;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderDaoTest {

    @Resource
    private OrderDao orderDao;

    @Resource
    private AddressDao addressDao;

    @Test
    public void insertTest() {
        Long[] userIds = {1369970959263588353L, 1369971003333124097L, 1369971003333124098L, 1369970958609276930L};
        for (int i = 0; i < 10000; i++) {
            Order order = new Order();
            order.setPayment(new BigDecimal(i));
            order.setPaymentType(1);
            order.setStatus(0);
            long now = System.currentTimeMillis();
            order.setPaymentTime(now);
            order.setConsignTime(now);
            order.setEndTime(now);
            order.setCloseTime(now);
            Long userId = userIds[RandomUtil.randomInt(0, userIds.length)];
            order.setUserId(userId);
            QueryWrapper<Address> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId);
            Long addressId = addressDao.selectList(queryWrapper).get(0).getId();
            order.setAddressId(addressId);
            order.setCreateTime(now);
            order.setUpdateTime(now);
            int result = orderDao.insert(order);

            Assert.assertEquals(1, result);
        }

    }

    @Test
    public void selectTest() {
        List<Order> orderList = orderDao.selectList(new QueryWrapper<>());
        int count = orderDao.selectCount(new QueryWrapper<>());
        Assert.assertEquals(orderList.size(), count);
    }

    @Test
    public void updateTest() {
        List<Order> orderList = orderDao.selectList(new QueryWrapper<>());
        Order order = orderList.get(0);

        System.out.println(order.getId());
        long now = System.currentTimeMillis();
        order.setUpdateTime(now);
        order.setEndTime(now);
        order.setStatus(1);
        order.setPayment(new BigDecimal(RandomUtil.randomInt(10, 190)));
        //不能更新用于分库的键
        order.setUserId(null);
        int result = orderDao.updateById(order);
        Assert.assertEquals(1, result);
    }

    @Test
    public void deleteTest() {
        List<Order> orderList = orderDao.selectList(new QueryWrapper<>());
        Order order = orderList.get(RandomUtil.randomInt(orderList.size()));
        System.out.println(order.getId());
        int result = orderDao.deleteById(order.getId());
        Assert.assertEquals(1, result);

        int count = orderDao.selectCount(new QueryWrapper<>());
        Assert.assertEquals(orderList.size() - 1, count);
    }
}
```

PS：**需要注意的是， 在更新记录的时候， 不能够修改或者更新用于分库或者分表的键， 比如address和t_order表，都是根据user_id进行分库， 因此不能更新这个字段**

执行结果如下：

![image-20210311191914608](C:\Users\xiaowenhou\AppData\Roaming\Typora\typora-user-images\image-20210311191914608.png)

![image-20210311192304890](C:\Users\xiaowenhou\AppData\Roaming\Typora\typora-user-images\image-20210311192304890.png)

![image-20210311192328611](C:\Users\xiaowenhou\AppData\Roaming\Typora\typora-user-images\image-20210311192328611.png)

**插入的user记录都按照id % 2的方式分布在两个数据库中**

以下是address插入后的结果， address的记录按照user_id % 2分布在不同的数据库中， 因此根据用户ID去关联查询地址时， 不需要跨库join

![image-20210311193344377](C:\Users\xiaowenhou\AppData\Roaming\Typora\typora-user-images\image-20210311193344377.png)

order也是一样， 现根据user_id进行分库， 然后再根据id进行分表

![image-20210311204754794](C:\Users\xiaowenhou\AppData\Roaming\Typora\typora-user-images\image-20210311204754794.png)



PS：使用雪花算法生成ID， 然后用ID对16取模进行分表时存在一个问题，由于雪花算法虽然是自增，但不是连续的， 因此雪花算法生成的ID对16取模后出现了比较严重的数据倾斜问题， 即大部分数据都插入到有限的几个表当中，没能平均分配到各个表中。如：

在其中一个数据库中执行如下sql：

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

结果：

![image-20210311205611852](C:\Users\xiaowenhou\AppData\Roaming\Typora\typora-user-images\image-20210311205611852.png)

可以看到， 绝大部分数据落在了1， 2， 3， 4这四个表中， 其他的11个表几乎没有数据。。。

**解决：**

​	1、如果确定使用id取模的方式进行分库分表， 不使用雪花算法，比如可以使用redis生成连续的主键ID

​	2、根据业务的实际情况采用其他的分库分表方式， 比如可以根据时间范围， 或者地域， 或者采用hash方式进行拆分

