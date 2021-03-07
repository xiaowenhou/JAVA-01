package com.xiaowenhou.shardingjdbcreadwrite.dao;

import com.xiaowenhou.shardingjdbcreadwrite.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户 DAO 接口类
 */
@Mapper
public interface UserDao {

    int insert(User user);

    List<User> findAll();
}
