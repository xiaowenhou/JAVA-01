package com.xiaowenhou.shardingjdbcreadwrite.service;


import com.xiaowenhou.shardingjdbcreadwrite.entity.User;

import java.util.List;

/**
 * 用户业务接口层
 */
public interface UserService {

    /**
     * 根据用户名获取用户信息，包括从库的地址信息
     *
     * @return
     */
    List<User> findUsers();


    int saveUser(String name);
}
