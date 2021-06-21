package com.xiaowenhou.redis.demo.aspect.model;

import com.xiaowenhou.reids.operate.annotation.RedisKey;
import lombok.Data;

@Data
public class BaseEntity {

    @RedisKey(order = -1)
    private String timeStamp;
}
