package com.xiaowenhou.shardingjdbcreadwrite.service.impl;

import com.xiaowenhou.shardingjdbcreadwrite.dao.UserDao;
import com.xiaowenhou.shardingjdbcreadwrite.entity.User;
import com.xiaowenhou.shardingjdbcreadwrite.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户业务实现层
 *
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    // 主数据源
    @Resource()
    private UserDao userDao;


    @Override
    public List<User> findUsers() {
        return userDao.findAll();
    }

    @Override
    public int saveUser(String name) {
        User user = new User();
        user.setName(name);
        user.setDescription("this is a person");
        return userDao.insert(user);
    }
}
