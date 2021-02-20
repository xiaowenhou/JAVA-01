package com.xiaowenhou.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CurdDemo {
    public static void main(String[] args) throws Exception {
        //通过工具类获取数据库连接对象
        Connection con = ConnectionUtils.getConnection();
        //通过连接创建数据库执行对象
        Statement sta = con.createStatement();
        //为查询的结果集准备接收对象
        ResultSet rs = null;
        //查询
        String sqlStatement = "select * from user";
        qry(sta, sqlStatement);
        //增加
        sqlStatement = "insert into user values('15', 'Lucy')";
        System.out.println("插入执行结果:"+update(sta, sqlStatement));
        //更新
        sqlStatement = "update user set name='Lily' where id = '11'";
        System.out.println("更新执行结果:"+update(sta, sqlStatement));
        //删除
        sqlStatement = "delete from user where name = 'Lucy'";
        System.out.println("删除执行结果:"+update(sta, sqlStatement));
        ConnectionUtils.closeResource(con, sta, rs);
    }
    /**
     * 查询
     * @param sta
     * @param sql
     * @throws SQLException
     */
    private static void qry(Statement sta, String sql) throws SQLException {
        ResultSet rs = sta.executeQuery(sql);
        while(rs.next()) {
            System.out.println("id is : " + rs.getObject("id") + ", name is : " + rs.getObject("name"));
        }
    }
    /**
     * 增删改
     * @param sta
     * @param sql
     * @return
     * @throws SQLException
     */
    private static int update(Statement sta, String sql) throws SQLException {
        return sta.executeUpdate(sql);
    }
}
