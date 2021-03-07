package com.xiaowenhou.shardingjdbcreadwrite.entity;

import lombok.Data;

/**
 * 用户实体类
 *
 * Created by bysocket on 07/02/2017.
 */
@Data
public class User {

    /**
     * 城市编号
     */
    private Long id;

    /**
     * 城市名称
     */
    private String name;


    private String description;
}
