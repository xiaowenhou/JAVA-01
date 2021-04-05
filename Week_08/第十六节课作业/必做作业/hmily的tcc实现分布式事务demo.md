

**十六课作业必做题-使用hmily的tcc实现一个分布式事务demo**



**基础环境搭建：** 使用两个dubbo服务 + 一个web服务模拟跨数据库转账来作为demo的基本实现

**项目结构**：

![image-20210405135010896](C:\Users\xiaowenhou\AppData\Roaming\Typora\typora-user-images\image-20210405135010896.png)

common为公共实体和接口模块，dubbo为pom项目， 指定了使用的dubbo和zookeeper注册中心的版本

涉及到的数据库表为：

![image-20210405135556409](C:\Users\xiaowenhou\AppData\Roaming\Typora\typora-user-images\image-20210405135556409.png)

​    order是spring boot web项目， 模拟提供接口供前端或者其他系统调用， source和target为两个dubbo服务， 其中source操作hmily_tcc_bank_source库，表示A账户； target操作hmily_tcc_bank_target库，表示B账户 ；

整体调用流程为： 调用order项目的/order/transfer接口， order service中保存订单， 调用transfer方法进行转账， transfer中分别调用source 和 target的服务进行A账户的扣钱以及B账户的加钱操作。

​	transfer：  使用hmily的事务， try中订单状态为处理中， confirm中将订单状态修改为处理成功， cancel中将订单修改为处理失败

​	source    updateBalance： 使用hmily的事务， try中扣钱A中的钱， 并且将金额锁定（表中的freeze_amount字段); confirm中将锁定去除， cancel中将金额退回。

​	target  updateBalance：使用hmily的事务， try中加B中的钱， 并且将金额锁定（freeze_amount字段），confirm中将锁定去除， cancel中将加的钱扣掉， 并且将锁定的金额去除。

​	**关键代码：**

​		1、OrderServiceImpl类， 主要是transfer方法， 先保存订单， 然后再调用TransferService中的doTransfer方法进行转账处理

```java
@Service
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderDo> implements OrderService {
    @Resource
    private TransferService transferService;

    @Override
    public void transfer(TransferVO transferVO) {
        OrderDo order = saveOrder(transferVO);

        System.out.println(order);

        transferService.doTransfer(order);
    }


    private OrderDo saveOrder(TransferVO transferVO) {
        OrderDo orderDo = buildOrder(transferVO);
        this.save(orderDo);
        return orderDo;
    }

    private OrderDo buildOrder(TransferVO transferVO) {
        OrderDo orderDo = new OrderDo();
        orderDo.setSourceAccountNo(transferVO.getSourceAccountNo());
        orderDo.setSourceBankNo("1111");
        orderDo.setTargetAccountNo(transferVO.getTargetAccountNo());
        orderDo.setTargetBankNo("2222");
        orderDo.setTradeNo(transferVO.getTradeNo());
        orderDo.setStatus(0);//表示转账中
        orderDo.setAmount(transferVO.getAmount());
        long now = System.currentTimeMillis();
        orderDo.setCreatedTime(now);
        orderDo.setUpdatedTime(now);
        return orderDo;
    }
}
```

​	2、TransferServiceImpl类，主要是doTransfer方法， 是分布式事务的try方法，实际中应该先处理order的其他前置状态； confirm中则修改订单状态为成功， cancel中修改订单状态为失败

```java
@Slf4j
@Service
public class TransferServiceImpl implements TransferService {
    private final OrderDao orderDao;

    private final SourceAccountService sourceService;

    private final TargetAccountService targetService;

    @Autowired(required = false)
    public TransferServiceImpl(OrderDao orderDao, SourceAccountService sourceService, TargetAccountService targetService) {
        this.orderDao = orderDao;
        this.sourceService = sourceService;
        this.targetService = targetService;
    }

    /**
    	真正执行转账操作的方法， 用分布式事务包裹
    */
    @Override
    @HmilyTCC(confirmMethod = "confirmOrderStatus", cancelMethod = "cancelOrderStatus")
    public void doTransfer(OrderDo order) {
        //如果order还有其他的前置状态， 可以放在try中
        //转账
        sourceService.updateBalance(buildSourceDto(order));
        targetService.updateBalance(buildTargetDto(order));
    }

    public void confirmOrderStatus(OrderDo order) {
        order.setStatus(1);//表示转账成功
        order.setUpdatedTime(System.currentTimeMillis());
        orderDao.updateById(order);
        log.info("======order  confirm操作成功======");
    }

    public void cancelOrderStatus(OrderDo order) {
        order.setStatus(2);//表示转账失败
        order.setUpdatedTime(System.currentTimeMillis());
        orderDao.updateById(order);
        log.info("======order  cancel操作成功======");
    }


    private SourceDto buildSourceDto(OrderDo order) {
        SourceDto sourceDto = new SourceDto();
        sourceDto.setAccountNo(order.getSourceAccountNo());
        sourceDto.setAmount(order.getAmount());
        return sourceDto;
    }

    private TargetDto buildTargetDto(OrderDo order) {
        TargetDto targetDto = new TargetDto();
        targetDto.setAccountNo(order.getTargetAccountNo());
        targetDto.setAmount(order.getAmount());
        return targetDto;
    }
}
```

​	3、SourceAccountServiceImpl类，主要是updateBalance方法， 在try中进行账户余额的扣减以及锁定扣钱的金额， 在confirm中去除锁定金额， 表示执行成功， 在cancel中将扣减的金额加回来，并去除锁定金额， 表示执行失败回滚。

```java
@Slf4j
@Service("sourceAccountService")
public class SourceAccountServiceImpl implements SourceAccountService {

    @Resource
    private SourceAccountDao sourceDao;

    @Override
    @HmilyTCC(confirmMethod = "confirmUpdateBalance", cancelMethod = "cancelUpdateBalance")
    public void updateBalance(SourceDto sourceDto) {
        //里面最好只有一步事务操作， 如果有多个事务操作， 需要用@Transaction注解控制
        int result = sourceDao.updateBalance(sourceDto.getAccountNo(), sourceDto.getAmount(), System.currentTimeMillis());
        if (result != 1) {
            throw new RuntimeException("扣减余额异常");
        }
    }

    public void confirmUpdateBalance(SourceDto sourceDto) {
        log.info("==========执行Source的confirm阶段============");
        sourceDao.confirmUpdateBalance(sourceDto.getAccountNo(), sourceDto.getAmount());
    }

    public void cancelUpdateBalance(SourceDto sourceDto) {
        log.info("==========执行Source的cancel阶段============");
        sourceDao.cancelUpdateBalance(sourceDto.getAccountNo(), sourceDto.getAmount());
    }
}
```

PS：1、这些方法中因为只有一条数据库更新语句， 因此没有加Transaction控制事务， 如果在实际业务处理中需要有多次数据库操作， 那么需要增加事务控制。因为分布式事务主要是用于协调跨进程之间的事务过程， 如果是try， confirm， cancel中内部有多个事务操作， 需要自己处理。

​		2、dubbo调用要设置重试， 防止因为业务操作而导致业务处理失败， 那么相应的/order/transfer这个接口， 以及SourceAccountService和TargetAccountService中的try， confirm和cancel都要保证幂等性， 防止重试或者重复提交造成数据错乱。

SourceAccountDao类，实现各种sql操作

```java
public interface SourceAccountDao extends BaseMapper<SourceAccount> {

    @Update("update account set balance = balance - #{amount}," +
            " freeze_amount= freeze_amount + #{amount} ,updated_time = #{updatedTime}" +
            " where account_no = #{accountNo}  and  balance >= #{amount}")
    int updateBalance(String accountNo, BigDecimal amount, Long updatedTime);

    @Update("update account set freeze_amount = freeze_amount - #{amount} " +
            " where account_no = #{accountNo}  and  freeze_amount >= #{amount}")
    int confirmUpdateBalance(String accountNo, BigDecimal amount);

    @Update("update account set balance = balance + #{amount}," +
            " freeze_amount= freeze_amount - #{amount}" +
            " where account_no = #{accountNo}  and  freeze_amount >= #{amount}")
    int cancelUpdateBalance(String accountNo, BigDecimal amount);
}
```

4、TargetAccountServiceImpl类， 主要是updateBalance方法，和SourceAccountServiceImpl类类似

```java
@Slf4j
@Service("targetAccountService")
public class TargetAccountServiceImpl implements TargetAccountService {

    @Resource
    private TargetAccountDao targetDao;

    @Override
    @HmilyTCC(confirmMethod = "confirmUpdateBalance", cancelMethod = "cancelUpdateBalance")
    public void updateBalance(TargetDto targetDto) {
       int result = targetDao.updateBalance(targetDto.getAccountNo(), targetDto.getAmount(), System.currentTimeMillis());
       if (result != 1) {
           throw new RuntimeException("执行更新账户余额失败...");
       }
    }

    public void confirmUpdateBalance(TargetDto targetDto) {
        log.info("=============执行目标账户的confirm方法===================");
        targetDao.confirmUpdateBalance(targetDto.getAccountNo(), targetDto.getAmount());
    }

    public void cancelUpdateBalance(TargetDto targetDto) {
        log.info("=============执行目标账户的cancel方法===================");
        targetDao.cancelUpdateBalance(targetDto.getAccountNo(), targetDto.getAmount());
    }
}
```

TargetAccountDao类和SourceAccountDao类类似， 用于实现各种sql操作

```java
public interface TargetAccountDao extends BaseMapper<TargetAccount> {

    @Update("update account set balance = balance + #{amount}," +
            " freeze_amount = freeze_amount + #{amount} ,updated_time = #{updatedTime}" +
            " where account_no = #{accountNo}")
    int updateBalance(String accountNo, BigDecimal amount, Long updatedTime);

    @Update("update account set freeze_amount = freeze_amount - #{amount} " +
            " where account_no = #{accountNo}  and  freeze_amount >= #{amount}")
    int confirmUpdateBalance(String accountNo, BigDecimal amount);

    @Update("update account set balance = balance - #{amount}," +
            " freeze_amount= freeze_amount - #{amount}" +
            " where account_no = #{accountNo}  and  freeze_amount >= #{amount}")
    int cancelUpdateBalance(String accountNo, BigDecimal amount);
}
```

​	经过测试， **正常情况下，TransferService， SourceAccountService， TargetAccountService都能够执行confirm操作， 顺利转账；如果在各个方法的try阶段抛异常， 已经执行过try的服务就会调用cancel进行回滚， 而如果在try阶段执行成功， 那么就都会执行confirm方法， 如果confirm方法执行的过程中发生异常了， 也不会回滚， 还是会继续执行， 因此tcc也不是完全可靠的， 还是需要加入定时任务进行对账和补错操作， 确保业务的正确性。**

​	**踩坑记录：**

​	1、版本问题：

​		hmily最新稳定版为2.1.1   对应的dubbo的版本为2.6.5   如果将dubbo的版本替换为2.7.x以上， 启动可以正常启动， 但是通过rpc调用的时候会报一个NoSuchMethodError。

```
java.lang.NoSuchMethodError: com.alibaba.dubbo.rpc.RpcContext.getContext()Lcom/alibaba/dubbo/rpc/RpcContext;
```

​		zookeeper包的版本与连接的zookeeper Server尽可能一致，以及zookeeper客户端的包和dubbo的版本都要相互契合，简单来说就是：

​		hmily， dubbo， zookeeper包， zookeeper对应的客户端（curator或者zkClient）,zookeeper服务器的版本必须相互契合。

2、项目放在了带中文的目录下

​		一开始由于将项目放在了带中文的目录下， 因此总是提示报以下错误：

```
org.dromara.hmily.config.api.exception.ConfigException: ConfigLoader:loader config error,file path:/E:/%e7%b3%bb%e7%bb%9f%e6%80%a7%e8%af%be%e7%a8%8b/%e6%9e%81%e5%ae%a2%e5%a4%a7%e5%ad%a6-Java%e8%bf%9b%e9%98%b6%e8%ae%ad%e7%bb%83%e8%90%a5/code/JAVA-01/Week_08/%e7%ac%ac%e5%8d%81%e5%85%ad%e8%8a%82%e8%af%be%e4%bd%9c%e4%b8%9a/%e5%bf%85%e5%81%9a%e4%bd%9c%e4%b8%9a/code/hmily-tcc-demo/hmily-tcc-dubbo/hmily-tcc-dubbo-target/target/classes/hmily.yml

```

​	为了解决这个问题， 一直以为是hmily的配置文件中的配置项有问题， 直到最后把官网的源代码down下来， 顺利启动成功， 然后又在本地按照官网的源代码重新配置了一遍， 发现还是同样的问题， 最后才意识到有可能是有中文目录的原因， 最后将源代码换了一个目录， 问题解决。



**复盘**

​	**目标达成情况**： **初始目标是在两天内完成hmily-demo的搭建和运行， 但实际上花费的时间超过了一周，所耗费的时间大大超过预定时间， 没有达成预期目标。**

​	**行为推演：**一开始，计划先按照作业描述中所说， 先搭一个dubbo的demo， 然后加入hmily将该demo改成分布式事务的demo。

​		搭建dubbo的demo的时候， 从网上找资料， 找到一个按照原生dubbo搭建的例子， 搭建到一半的时候， 觉得在实际工作中， dubbo都要和springboot进行整合， 因为虽然通过rpc调用， 但是也离不开spring boot， 因此又重新开始搭建例子。（返工第一次）

​		搭建dubbo和springboot整合时， 需要用到zookeeper， 一开始用本地的zookeeper， 发现启动不起来， 或者启动起来之后也连不上，然后决定使用本机的虚拟机， 将虚拟机中的zookeeper集群作为注册中心， 这样也更加符合生产环境，但又遇到本机访问不到虚拟机的问题， 然后又解决本机访问不到虚拟机的问题。

​		解决本机访问不到虚拟机之后， 又花了点时间搞了下zookeeper的图形化客户端；然后又开始整合dubbo 和springboot， 最后， 终于将dubbo和spring boot整合完成， 这时， 已经花了差不多三天多了（周末+周一，二，三的晚上）。

​		现在终于开始用dubbo集成hmily了， 在网上找了几篇博客， 按照博客试着集成了以下， 发现报错， 一直提示加载不了配置文件（其实就是项目放在了中文目录下，但当时一直没发现， 折腾了好久， 没结果）， 最后想起来去官网看看官网的demo， 将官网的的源代码下载下来之后， 看了看官网提供的demo， 发现官网提供的demo的例子和我自己写的差别很大，包括版本、项目结构等等都有很大差别。

​		感觉把官网的demo跑起来太麻烦了， 就按照自己对官网demo的理解， 改造自己的项目， 虽然最终改造好了，但是发现死活跑不起来， 由于自己的版本和官网差别太大， 一时之间不知道从哪个方向入手，另外， 看了官网的demo， 发现官网项目的结构很合理， 自己的强迫症又犯了， 于是又将项目删掉， 重新按照官网的结构搭建例子（返工第二次）

​		按照官网手把手把例子搭建起来， 运行， 还是报错， 此时还不知道是版本的问题，于是又开始在网上找资料， 无果。最终还是回到官网， 看了下官网提供的视频， 决定试试官网提供的demo是不是能跑起来， 然后按照官网的步骤， 下载依赖， 修改配置文件， 结果顺利运行。

​		到这个时候，我知道官网的没有问题， 自己和官网的项目结构， 配置也基本一致， 再回头看这个配置文件的报错， 终于怀疑是中文目录的问题， 将项目复制到另一个目录下， 果然不再报配置文件的错误了。

​		项目终于顺利运行起来了， 但是通过接口调用rpc时依然报错， 但是官网demo就没有问题， 仔细比对之后， 发现是我的依赖的版本和官网的不一致，我一直用的是比较新的版本， 把版本改成和官网一致后，终于没有问题了。

​		终于可以测试hmily的tcc了， 但这时候又发生了另外一个问题， 虚拟机的zookeeper的连接不稳定， 总是要重试好几次才能连上， 又查了好久的资料， 一种说法是网络不稳定， 但按理说不应该啊， 自己用的本机的虚拟机， 应该属于内网才对， 又查了查资料， 按照博客改了改配置， 还是没用， 最后又决定使用本地的zookeeper试试， 这次很顺利， 一下子就连上了， 终于正常了。

​		测试hmily时， 正常情况很顺利， 然后测试异常情况， 当在Source端的try方法中的数据库操作之后抛出一个异常之后， 我发现虽然Order端会执行cancel方法， 但是Source端却不会执行cancel方法， 一开始我以为是自己配置的有问题， 于是又回过头来研究官网demo， 发现官网的demo也是如此， 我才恍然大悟， hmily管理的是整个分布式事务， 如果try，confrim或者cancel中嵌入多个事务， 当在try中发生了异常， hmily会将之前已经执行成功的try执行cancel方法，但是本地的事务还是由本机去控制， hmily只是起到了事务协调者的作用， 本地的事务提交还是由自己控制， 所以try，confirm， cancel方法中如果有多个事务， 需要自己加上@Transaction注解管理。

​	接着， 写记录， 做复盘， 这个作业才算真正完成。

**分析：**

​		**需要保持的点：**

​			**1、中间遇到了很多困难， 但是都没有放弃， 每天都会去思考， 每天前进一点点， 最终解决了整个问题**

​			**2、学习时参考了官网这一最权威的资料**

​			**3、能够尽可能的以贴近实际工作的标准去完成作业**

​			**4、进行了复盘，并且总结了经验和踩过的坑**

​		**存在的问题：**

​			**1、经验不足，dubbo和springboot的整合，dubbo的各种特性，zookeeper的具体使用， hmily的使用对自己来说几乎都是新的东西，以往只是知道有， 但是没有动手实践过， 实践过之后才知道细节是魔鬼， 有很多需要注意的细节，任何一个细节不解决， 都有可能卡死在这。。。**

​			**2、面对一个新的技术， 一开始居然怕麻烦没有去看官网的demo， 而是去看各种网络博客中的二手资料， 导致浪费了时间不说， 结果最终还是要回来看官网的demo和文档才最终解决问题。说明自己太急于求成， 过于浮躁**

​			**3、不能够循序渐进， 总想一步到位。比如dubbo和springboot整合的出发点没错， 在实际工作中确实dubbo和springboot整合用的比较多， 但是作为练习， 应该先从基本出发， 然后一点点的改造，而不是眼高手低，觉得用dubbo的xml太麻烦， 就要用配置文件和注解， 最后变量太多， 自己hold不住，最终还是用的xml这种方式去解决了问题。**

​			**4、练习太少。版本问题， 虚拟机问题，中文目录问题， zookeeper连不上问题本质还是自己用的太少， 以前学到的只是很多仅仅停留在知道， 而并不能活学活用来解决实际问题， 知行不合一， 遇到报错还是只能百度，谷歌。**