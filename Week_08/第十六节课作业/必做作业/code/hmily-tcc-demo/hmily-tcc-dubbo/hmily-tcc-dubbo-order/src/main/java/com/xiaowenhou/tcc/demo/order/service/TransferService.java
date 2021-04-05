package com.xiaowenhou.tcc.demo.order.service;

import com.xiaowenhou.tcc.demo.order.entity.OrderDo;
import org.dromara.hmily.annotation.Hmily;

public interface TransferService {

    @Hmily
    void doTransfer(OrderDo order);
}
