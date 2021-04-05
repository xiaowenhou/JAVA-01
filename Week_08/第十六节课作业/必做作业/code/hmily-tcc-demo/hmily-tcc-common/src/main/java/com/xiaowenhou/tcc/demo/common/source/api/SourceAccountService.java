package com.xiaowenhou.tcc.demo.common.source.api;

import com.xiaowenhou.tcc.demo.common.source.dto.SourceDto;
import org.dromara.hmily.annotation.Hmily;

public interface SourceAccountService {

    @Hmily
    void updateBalance(SourceDto sourceDto);
}
