package com.xiaowenhou.shardingproxydemo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("t_order")
public class Order {
    @TableId
    private Long id;

    private BigDecimal payment;

    private Integer paymentType;

    private Integer status;

    private Long paymentTime;

    private Long consignTime;

    private Long endTime;

    private Long closeTime;

    private Long userId;

    private Long addressId;

    private Long createTime;

    private Long updateTime;
}
