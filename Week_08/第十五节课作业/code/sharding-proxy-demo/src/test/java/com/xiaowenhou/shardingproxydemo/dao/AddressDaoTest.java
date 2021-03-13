package com.xiaowenhou.shardingproxydemo.dao;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaowenhou.shardingproxydemo.entity.Address;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AddressDaoTest {

    @Resource
    private AddressDao addressDao;

    @Test
    public void insertTest() {

        Long[] userIds = {1369970959263588353L, 1369971003333124097L, 1369971003333124098L, 1369970958609276930L};
        for (int i = 0; i < 20; i++) {
            Address address = new Address();
            address.setUserId(userIds[RandomUtil.randomInt(0, userIds.length)]);
            address.setProvinceId((long) i);
            address.setCityId(i + "");
            address.setAreaId(i + "");
            address.setMobile("2000-12-16");
            address.setMobile("1356666888" + i);
            address.setAddress("北京市东城区xxxxxxx");
            address.setContact("备注");
            address.setIsDefault(0);
            address.setCreatedTime(System.currentTimeMillis());
            address.setUpdatedTime(System.currentTimeMillis());
            int result = addressDao.insert(address);

            Assert.assertEquals(1, result);
        }

    }

    @Test
    public void selectTest() {
        List<Address> addressList = addressDao.selectList(new QueryWrapper<>());
        int count = addressDao.selectCount(new QueryWrapper<>());
        Assert.assertEquals(addressList.size(), count);
    }

    @Test
    public void updateTest() {
        List<Address> addressList = addressDao.selectList(new QueryWrapper<>());
        Address address = addressList.get(0);

        System.out.println(address.getId());
        address.setUpdatedTime(System.currentTimeMillis());
        address.setAddress("成都市武侯区hhahahahahah");
        address.setIsDefault(1);
        address.setMobile("15788889999");
        //sharding jdbc中不能更新用于分库的键
        address.setUserId(null);
        int result = addressDao.updateById(address);
        Assert.assertEquals(1, result);
    }

    @Test
    public void deleteTest() {
        List<Address> addressList = addressDao.selectList(new QueryWrapper<>());
        Address address = addressList.get(RandomUtil.randomInt(addressList.size()));
        System.out.println(address.getId());
        int result = addressDao.deleteById(address.getId());
        Assert.assertEquals(1, result);

        int count = addressDao.selectCount(new QueryWrapper<>());
        Assert.assertEquals(addressList.size() - 1, count);
    }
}
