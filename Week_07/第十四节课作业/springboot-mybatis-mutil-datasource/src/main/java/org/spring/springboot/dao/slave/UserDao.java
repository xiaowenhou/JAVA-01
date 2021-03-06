package org.spring.springboot.dao.slave;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.spring.springboot.domain.User;
import org.springframework.stereotype.Repository;


@Mapper
@Repository("slave")
public interface UserDao {


    User findByName(@Param("name") String name);
}
