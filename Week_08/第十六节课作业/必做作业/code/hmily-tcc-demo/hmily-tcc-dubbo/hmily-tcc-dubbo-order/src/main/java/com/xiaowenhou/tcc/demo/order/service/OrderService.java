package com.xiaowenhou.tcc.demo.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaowenhou.tcc.demo.order.entity.OrderDo;
import com.xiaowenhou.tcc.demo.order.vo.TransferVO;

public interface OrderService extends IService<OrderDo> {

    void transfer(TransferVO transferVO);
}
