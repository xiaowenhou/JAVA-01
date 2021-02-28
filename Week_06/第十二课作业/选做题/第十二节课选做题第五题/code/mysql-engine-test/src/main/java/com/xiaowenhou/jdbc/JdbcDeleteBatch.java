package com.xiaowenhou.jdbc;

import cn.hutool.core.util.RandomUtil;
import com.xiaowenhou.bean.Order;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcDeleteBatch {

    private static int delete(List<Long> idList) {
        Connection conn = ConnectionUtils.getConnection();
        String sql = "delete from `order` where id= ?";
        PreparedStatement pstmt = null;
        int count = 0;
        try {
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            for (Long id : idList) {
                pstmt.setLong(1, id);
                pstmt.addBatch();
                count++;
                if (count % 10000 == 0) {
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
        List<Long> idList = new ArrayList<>();
        for (int i = 0; i < 800000; i++) {
            idList.add(RandomUtil.randomLong(1, 1000000));
        }

        long before = System.currentTimeMillis();
        int result = delete(idList);
        long after = System.currentTimeMillis();

        System.out.println("delete result is: " + result);
        System.out.println("cost time is : " + (after - before) + " ms");
    }
}
