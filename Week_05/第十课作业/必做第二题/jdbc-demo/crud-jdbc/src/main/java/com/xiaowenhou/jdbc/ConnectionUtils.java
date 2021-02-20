package com.xiaowenhou.jdbc;

import java.sql.*;

public class ConnectionUtils {

    private static String url = "jdbc:mysql://localhost:3306/test";
    private static String username = "root";
    private static String password = "zx5708923";

    /**
     * 通过静态代码块，初始化数据库连接配置数据，并且注册数据库驱动
     */
    static {
        try {
            String driver = "com.mysql.jdbc.Driver";
            Class.forName(driver); //classLoader,加载对应驱动
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("获取数据库连接异常，请检查配置数据");
        }
    }


    /**
     * 获取数据库连接对象
     * @return
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("获取数据库连接异常，请检查配置数据");
        }
        return conn;
    }

    /**
     * 关闭JDBC相关资源
     * @param con
     * @param sta
     * @param rs
     */
    public static void closeResource(Connection con, Statement sta, ResultSet rs) {
        try {
            if(con!=null) {
                con.close();
            }
            if(sta!=null) {
                sta.close();
            }
            if(rs!=null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeResource(Connection con, PreparedStatement ps) {
        try {
            if (con != null) {
                con.close();
            }
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
