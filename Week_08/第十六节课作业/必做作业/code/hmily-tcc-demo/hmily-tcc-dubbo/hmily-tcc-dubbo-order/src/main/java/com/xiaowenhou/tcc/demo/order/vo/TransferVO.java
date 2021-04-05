package com.xiaowenhou.tcc.demo.order.vo;

import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 表现层参数对象
 * @author xiaowenhou
 */
@Data
public class TransferVO {
    @NotBlank(message = "源账号不能为空")
    private String sourceAccountNo;

    @NotBlank(message = "目标账号不能为空")
    private String targetAccountNo;

    @NotNull(message = "转账金额不能为空")
    @DecimalMin( message = "转账金额必须大于0.01元",value = "0.01")
    @DecimalMax( message = "转账金额超出上限",value = "9223372036854775807")
    private BigDecimal amount;
    @NotBlank(message = "交易单号不能为空")
    private String tradeNo;
}
