package com.xiaowenhou.tcc.demo.target.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("account")
public class TargetAccount {

    @TableId
    private Long id;

    private String accountName;

    private String accountNo;

    private BigDecimal balance;

    private BigDecimal freezeAmount;

    private String bankNo;

    private Long createdTime;

    private Long updatedTime;
}
