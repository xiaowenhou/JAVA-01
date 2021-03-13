package com.xiaowenhou.shardingjdbcdemo.dao;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaowenhou.shardingjdbcdemo.entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDaoTest {

    @Resource
    private UserDao userDao;

    @Test
    public void insertTest() {
        for (int i = 0; i < 20; i++) {
            User user = new User();
            user.setName("zhangsan" + i);
            user.setNickName("luck-boy" + i);
            user.setUsername("兰陵王"+ RandomUtil.randomInt(100, 100000));
            user.setPassword("123456789"+ i);
            user.setBirthday("2000-12-16");
            user.setSex(1);
            user.setHeadPic("https://www.baidu.com");
            user.setPhone("1356666888"+ i);
            user.setEmail("888888"+ i + "@qq.com");
            user.setStatus(1);
            user.setIsMobileCheck(0);
            user.setIsEmailCheck(0);
            user.setCreatedTime(System.currentTimeMillis());
            user.setUpdatedTime(System.currentTimeMillis());
            int result = userDao.insert(user);

            Assert.assertEquals(1, result);
        }
    }

    @Test
    public void selectTest() {
        List<User> userList = userDao.selectList(new QueryWrapper<>());
        int count = userDao.selectCount(new QueryWrapper<>());
        Assert.assertEquals(userList.size(), count);
    }

    @Test
    public void updateTest() {
        List<User> userList = userDao.selectList(new QueryWrapper<>());
        User user = userList.get(0);

        System.out.println(user.getId());
        user.setUpdatedTime(System.currentTimeMillis());
        user.setEmail("alsighlrihgrlg@163.com");
        user.setIsEmailCheck(1);
        user.setPhone("15788889999");
        int result = userDao.updateById(user);
        Assert.assertEquals(1, result);
    }

    @Test
    public void deleteTest() {
        List<User> userList = userDao.selectList(new QueryWrapper<>());
        User user = userList.get(RandomUtil.randomInt(userList.size()));
        System.out.println(user.getId());
        int result = userDao.deleteById(user.getId());
        Assert.assertEquals(1, result);

        int count = userDao.selectCount(new QueryWrapper<>());
        Assert.assertEquals(userList.size() - 1, count);
    }
}
