package com.xiaowenhou.test100w;

import cn.hutool.core.util.RandomUtil;
import com.xiaowenhou.bean.Order;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcInsertWithPerparedAndHikari {

    private static int insert(List<Order> orderList, boolean isAutoCommit, boolean isBatch) {
        Connection conn = ConnectionUtilsWithHikari.getConnection();
        String sql = "insert into `order` (payment, payment_type, status, payment_time, consign_time, end_time, close_time, user_id, address_id, create_time, update_time) " +
                "values (?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pstmt = null;
        int count = 0;
        try {
            if (!isAutoCommit) {
                conn.setAutoCommit(false);
            }

            pstmt = conn.prepareStatement(sql);
            for (Order order : orderList) {
                pstmt.setBigDecimal(1, order.getPayment());
                pstmt.setInt(2, order.getPaymentType());
                pstmt.setInt(3, order.getStatus());
                pstmt.setLong(4, order.getPaymentTime());
                pstmt.setLong(5, order.getConsignTime());
                pstmt.setLong(6, order.getEndTime());
                pstmt.setLong(7, order.getCloseTime());
                pstmt.setLong(8, order.getUserId());
                pstmt.setLong(9, order.getAddressId());
                pstmt.setLong(10, order.getCreateTime());
                pstmt.setLong(11, order.getUpdateTime());
                count++;
                if (isBatch) {
                    pstmt.addBatch();
                    if (count % 10000 == 0) {
                        pstmt.executeBatch();
                        conn.commit();
                    }
                } else {
                    pstmt.executeUpdate();
                }
            }
            if (isBatch) {
                pstmt.executeBatch();
                conn.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionUtilsWithHikari.closeResource(conn, pstmt);
        }
        return count;
    }

    public static void main(String[] args) {
        List<Order> orderList = new ArrayList<>(1000000);
        BigDecimal payment = new BigDecimal("2.5");

        for (int i = 0; i < 1000000; i++) {
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


        //int result = insert(orderList, true, false);
        long before = System.currentTimeMillis();
        int result = insert(orderList, false, true);
        long after = System.currentTimeMillis();

        System.out.println("insert result is: " + result);
        System.out.println("cost time is : " + (after - before) + " ms");
    }
}
