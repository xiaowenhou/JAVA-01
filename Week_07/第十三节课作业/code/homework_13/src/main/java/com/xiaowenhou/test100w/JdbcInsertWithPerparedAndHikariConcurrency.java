package com.xiaowenhou.test100w;

import cn.hutool.core.util.RandomUtil;
import com.xiaowenhou.bean.Order;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class JdbcInsertWithPerparedAndHikariConcurrency {

    private static CountDownLatch countDownLatch = new CountDownLatch(20);
    private static void insert(List<Order> orderList, boolean isAutoCommit, boolean isBatch) {
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

        countDownLatch.countDown();
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        BigDecimal payment = new BigDecimal("2.5");

        ExecutorService service = Executors.newFixedThreadPool(22);
        long before = System.currentTimeMillis();
        for (int i = 0; i < 20; i++) {
            List<Order> orderList = new ArrayList<>(500000);
            for (int j = 0; j < 500000; j++) {
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

           service.execute(() -> insert(orderList, false, true));
        }

        countDownLatch.await();
        long after = System.currentTimeMillis();

        System.out.println("cost time is : " + (after - before) + " ms");
        service.shutdown();
    }
}
