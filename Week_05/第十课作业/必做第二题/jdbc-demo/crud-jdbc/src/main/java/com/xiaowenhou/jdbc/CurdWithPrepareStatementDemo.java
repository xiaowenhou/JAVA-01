package com.xiaowenhou.jdbc;

import com.xiaowenhou.bean.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CurdWithPrepareStatementDemo {

    private static int insert(User user) {
        Connection conn = ConnectionUtilsWithHikari.getConnection();
        int i = 0;
        String sql = "insert into user (id, name) values(?,?)";
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, user.getId());
            pstmt.setString(2, user.getName());
            i = pstmt.executeUpdate();
            System.out.println("插入执行结果: " + i);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionUtilsWithHikari.closeResource(conn, pstmt);
        }
        return i;
    }


    private static int update(User user) {
        Connection conn = ConnectionUtilsWithHikari.getConnection();
        int i = 0;
        String sql = "update user set name='" + user.getName() + "' where id='" + user.getId() + "'";
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            i = pstmt.executeUpdate();
            System.out.println("更新执行结果: " + i);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionUtilsWithHikari.closeResource(conn, pstmt);
        }
        return i;
    }


    private static Integer getAll() {
        Connection conn = ConnectionUtilsWithHikari.getConnection();
        String sql = "select * from user";
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= col; i++) {
                    System.out.print(rs.getString(i) + "\t");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionUtilsWithHikari.closeResource(conn, pstmt);
        }
        return null;
    }


    private static int delete(String name) {
        Connection conn = ConnectionUtilsWithHikari.getConnection();
        int i = 0;
        String sql = "delete from user where name='" + name + "'";
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            i = pstmt.executeUpdate();
            System.out.println("删除执行结果: " + i);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionUtilsWithHikari.closeResource(conn, pstmt);
        }
        return i;
    }


    public static void main(String[] args) {
        getAll();
        insert(new User(15L, "zhangsan"));
        update(new User(15L, "heiheihei"));
        delete("heiheihei");
    }
}
