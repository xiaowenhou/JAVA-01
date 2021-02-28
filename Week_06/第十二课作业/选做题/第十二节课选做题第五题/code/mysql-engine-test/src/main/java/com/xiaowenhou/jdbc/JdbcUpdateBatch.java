package com.xiaowenhou.jdbc;

import cn.hutool.core.util.RandomUtil;
import com.xiaowenhou.bean.Order;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcUpdateBatch {
    private static int update(List<Order> orderList) {
        Connection conn = ConnectionUtils.getConnection();
        String sql = "update `order` set payment= ? where id= ?";
        PreparedStatement pstmt = null;
        int count = 0;
        try {
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            for (Order order : orderList) {
                pstmt.setBigDecimal(1, order.getPayment());
                pstmt.setLong(2, order.getId());
                pstmt.addBatch();
                count++;
                if (count % 100000 == 0) {
                    pstmt.executeBatch();
                }
            }
            pstmt.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionUtils.closeResource(conn, pstmt);
        }
        return count;
    }

    public static void main(String[] args) {
        List<Order> orderList = new ArrayList<>(1000000);
        BigDecimal payment = new BigDecimal("50.55");

        for (int i = 0; i < 800000; i++) {
            Order order = new Order();
            order.setPayment(payment);
            order.setId(RandomUtil.randomLong(1, 1000000));
            orderList.add(order);
        }

        long before = System.currentTimeMillis();
        int result = update(orderList);
        long after = System.currentTimeMillis();

        System.out.println("update result is: " + result);
        System.out.println("cost time is : " + (after - before) + " ms");
    }
}