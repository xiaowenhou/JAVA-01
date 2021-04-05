package com.xiaowenhou.tcc.demo.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaowenhou.tcc.demo.order.dao.OrderDao;
import com.xiaowenhou.tcc.demo.order.entity.OrderDo;
import com.xiaowenhou.tcc.demo.order.service.OrderService;
import com.xiaowenhou.tcc.demo.order.service.TransferService;
import com.xiaowenhou.tcc.demo.order.vo.TransferVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderDo> implements OrderService {
    @Resource
    private TransferService transferService;

    @Override
    public void transfer(TransferVO transferVO) {
        OrderDo order = saveOrder(transferVO);

        System.out.println(order);

        transferService.doTransfer(order);
    }


    private OrderDo saveOrder(TransferVO transferVO) {
        OrderDo orderDo = buildOrder(transferVO);
        this.save(orderDo);
        return orderDo;
    }

    private OrderDo buildOrder(TransferVO transferVO) {
        OrderDo orderDo = new OrderDo();
        orderDo.setSourceAccountNo(transferVO.getSourceAccountNo());
        orderDo.setSourceBankNo("1111");
        orderDo.setTargetAccountNo(transferVO.getTargetAccountNo());
        orderDo.setTargetBankNo("2222");
        orderDo.setTradeNo(transferVO.getTradeNo());
        orderDo.setStatus(0);//表示转账中
        orderDo.setAmount(transferVO.getAmount());
        long now = System.currentTimeMillis();
        orderDo.setCreatedTime(now);
        orderDo.setUpdatedTime(now);
        return orderDo;
    }
}
