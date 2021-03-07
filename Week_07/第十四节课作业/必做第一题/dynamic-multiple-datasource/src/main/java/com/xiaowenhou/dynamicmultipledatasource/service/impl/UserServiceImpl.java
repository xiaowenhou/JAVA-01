package com.xiaowenhou.dynamicmultipledatasource.service.impl;

import com.xiaowenhou.dynamicmultipledatasource.dao.UserDao;
import com.xiaowenhou.dynamicmultipledatasource.datasource.DataSourceTypeEnum;
import com.xiaowenhou.dynamicmultipledatasource.datasource.annotation.DataSource;
import com.xiaowenhou.dynamicmultipledatasource.entity.User;
import com.xiaowenhou.dynamicmultipledatasource.service.UserService;
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
    @Resource
    private UserDao userDao;


    @DataSource(DataSourceTypeEnum.SLAVE)
    @Override
    public List<User> findUsers() {
        return userDao.findAll();
    }

    @DataSource(DataSourceTypeEnum.MASTER)
    @Override
    public int saveUser(String name) {
        User user = new User();
        user.setName(name);
        user.setDescription("this is a person");
        return userDao.insert(user);
    }
}
