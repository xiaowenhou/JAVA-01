package com.xiaowenhou.bean;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Order {

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
