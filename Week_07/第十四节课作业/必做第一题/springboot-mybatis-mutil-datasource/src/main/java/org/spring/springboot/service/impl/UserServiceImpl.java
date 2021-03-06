package org.spring.springboot.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.spring.springboot.dao.master.UserDao;
import org.spring.springboot.domain.User;
import org.spring.springboot.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户业务实现层
 *
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    // 主数据源
    @Resource(name = "master")
    private UserDao masterUserDao;


    // 从数据源
    @Resource(name = "slave")
    private org.spring.springboot.dao.slave.UserDao slaveUserDao;


    @Override
    public User findByName(String userName) {
        return slaveUserDao.findByName(userName);
    }

    @Override
    public int saveUser(String name) {
        User user = new User();
        user.setName(name);
        user.setDescription("this is a person");
        return masterUserDao.insert(user);
    }
}
