package com.xiaowenhou.tcc.demo.common.target.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class TargetDto implements Serializable {

    private String accountNo;

    private BigDecimal amount;
}
