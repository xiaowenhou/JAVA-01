package com.xiaowenhou.tcc.demo.common.target.api;

import com.xiaowenhou.tcc.demo.common.target.dto.TargetDto;
import org.dromara.hmily.annotation.Hmily;

public interface TargetAccountService {

    /**
     * @param targetDto 目标账户信息
     */
    @Hmily
    void updateBalance(TargetDto targetDto);
}
