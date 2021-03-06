package org.spring.springboot.domain;

import lombok.Data;

/**
 * 用户实体类
 *
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

    /**
     * 描述
     */
    private String description;
}
