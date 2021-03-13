package com.xiaowenhou.shardingjdbcdemo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("address")
public class Address {
    @TableId()
    private Long id;

    private Long userId;

    private Long provinceId;

    private String cityId;

    private String areaId;

    private String mobile;

    private String address;

    private String contact;

    private Integer isDefault;

    private Long createdTime;

    private Long updatedTime;
}
