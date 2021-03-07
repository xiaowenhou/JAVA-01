package com.xiaowenhou.dynamicmultipledatasource.dao;

import com.xiaowenhou.dynamicmultipledatasource.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户 DAO 接口类
 */
@Mapper
public interface UserDao {

    int insert(User user);

    List<User> findAll();
}
