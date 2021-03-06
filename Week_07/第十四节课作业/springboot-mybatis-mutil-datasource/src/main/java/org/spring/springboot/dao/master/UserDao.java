package org.spring.springboot.dao.master;

import org.apache.ibatis.annotations.Mapper;
import org.spring.springboot.domain.User;
import org.springframework.stereotype.Repository;

/**
 * 用户 DAO 接口类
 *
 */
@Mapper
@Repository("master")
public interface UserDao {

    int insert(User user);
}
