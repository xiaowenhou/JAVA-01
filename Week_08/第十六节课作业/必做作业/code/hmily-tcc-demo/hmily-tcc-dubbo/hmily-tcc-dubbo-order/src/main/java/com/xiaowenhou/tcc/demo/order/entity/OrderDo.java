package com.xiaowenhou.tcc.demo.order.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@TableName("trade_order")
@Data
public class OrderDo {

    @TableId()
    private Long id;

    private String tradeNo;

    private String sourceAccountNo;

    private String sourceBankNo;

    private String targetAccountNo;

    private String targetBankNo;

    private BigDecimal amount;

    private Integer status;

    private Long createdTime;

    private Long updatedTime;
}
