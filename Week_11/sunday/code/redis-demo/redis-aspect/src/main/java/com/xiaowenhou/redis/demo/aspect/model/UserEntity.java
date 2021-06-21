package com.xiaowenhou.redis.demo.aspect.model;

import com.xiaowenhou.reids.operate.annotation.RedisKey;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserEntity extends BaseEntity {

    @RedisKey(order = 1)
    private String name;

    private Integer age;

    private String email;
}
