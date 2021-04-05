package com.xiaowenhou.tcc.demo.common.source.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SourceDto implements Serializable {

    private String accountNo;

    private BigDecimal amount;

}
