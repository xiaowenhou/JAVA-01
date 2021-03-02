package com.xiaowenhou.test100w;

import cn.hutool.core.util.RandomUtil;
import com.google.common.base.Joiner;
import com.xiaowenhou.bean.Order;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JdbcInsertNormalBatch {


    public static int insert(List<Order> orderList) {
        //通过工具类获取数据库连接对象
        Connection con = ConnectionUtils.getConnection();
        //通过连接创建数据库执行对象
        Statement sta = null;
        int result = 0;
        try {
            sta = con.createStatement();

            StringBuilder sql = new StringBuilder("insert into `order` (payment, payment_type, status, payment_time, consign_time, end_time, close_time, user_id, address_id, create_time, update_time) " +
                    "values ");
            for (int i = 0; i < orderList.size(); i++) {
                Order order = orderList.get(i);
                String str = "(";
                str += Joiner.on(",").join(order.getPayment(),order.getPaymentType(), order.getStatus()
                        ,order.getPaymentTime(), order.getConsignTime(), order.getEndTime(), order.getCloseTime()
                        ,order.getUserId(), order.getAddressId(), order.getCreateTime(), order.getUpdateTime());
                str += "),";
                sql.append(str);

                //每一万提交一次
                if (i != 0 && i % 10000 == 0) {
                    sql.deleteCharAt(sql.length() - 1);
                    result += sta.executeUpdate(sql.toString());
                    sql = new StringBuilder("insert into `order` (payment, payment_type, status, payment_time, consign_time, end_time, close_time, user_id, address_id, create_time, update_time) " +
                            "values ");
                }
            }
            sql.deleteCharAt(sql.length() - 1);
            result += sta.executeUpdate(sql.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionUtils.closeResource(con, sta, null);
        }

        return result;
    }


    public static void main(String[] args) {
        List<Order> orderList = new ArrayList<>(1000000);
        BigDecimal payment = new BigDecimal("2.5");

        for (int i = 0; i < 10000000; i++) {
            Long currTimeStamp = System.currentTimeMillis();
            Order order = new Order();
            order.setPayment(payment);
            order.setPaymentType(1);
            order.setStatus(1);
            order.setPaymentTime(currTimeStamp);
            order.setConsignTime(currTimeStamp);
            order.setEndTime(currTimeStamp);
            order.setCloseTime(currTimeStamp);
            order.setCreateTime(currTimeStamp);
            order.setUpdateTime(currTimeStamp);
            order.setUserId(RandomUtil.randomLong(200000));
            order.setAddressId(RandomUtil.randomLong(200000));
            orderList.add(order);
        }

        long before = System.currentTimeMillis();
        int result = insert(orderList);
        long after = System.currentTimeMillis();

        System.out.println("insert result is: " + result);
        System.out.println("cost time is : " + (after - before) + " ms");
    }
}


