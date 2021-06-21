package com.xiaowenhou.reids.operate.key;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RedisKeyParam {

    private int order;

    private String paramName;

    private String paramValue;
}
