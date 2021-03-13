package com.xiaowenhou.shardingjdbcdemo.dao;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaowenhou.shardingjdbcdemo.entity.Address;
import com.xiaowenhou.shardingjdbcdemo.entity.Order;
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
        Long[] userIds = {1370239154784382977L, 1370239155002486785L, 1370239154885046274L, 1370239154935377922L};
        for (int i = 0; i < 2; i++) {
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


