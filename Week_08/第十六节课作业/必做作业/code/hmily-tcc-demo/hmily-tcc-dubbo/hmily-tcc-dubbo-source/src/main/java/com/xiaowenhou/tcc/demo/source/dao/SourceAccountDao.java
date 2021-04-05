package com.xiaowenhou.tcc.demo.source.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaowenhou.tcc.demo.source.entity.SourceAccount;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

public interface SourceAccountDao extends BaseMapper<SourceAccount> {

    @Update("update account set balance = balance - #{amount}," +
            " freeze_amount= freeze_amount + #{amount} ,updated_time = #{updatedTime}" +
            " where account_no = #{accountNo}  and  balance >= #{amount}")
    int updateBalance(String accountNo, BigDecimal amount, Long updatedTime);

    @Update("update account set freeze_amount = freeze_amount - #{amount} " +
            " where account_no = #{accountNo}  and  freeze_amount >= #{amount}")
    int confirmUpdateBalance(String accountNo, BigDecimal amount);

    @Update("update account set balance = balance + #{amount}," +
            " freeze_amount= freeze_amount - #{amount}" +
            " where account_no = #{accountNo}  and  freeze_amount >= #{amount}")
    int cancelUpdateBalance(String accountNo, BigDecimal amount);
}
