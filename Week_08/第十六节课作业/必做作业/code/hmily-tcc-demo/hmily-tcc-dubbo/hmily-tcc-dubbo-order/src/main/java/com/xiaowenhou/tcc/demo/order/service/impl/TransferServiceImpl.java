package com.xiaowenhou.tcc.demo.order.service.impl;

import com.xiaowenhou.tcc.demo.common.source.api.SourceAccountService;
import com.xiaowenhou.tcc.demo.common.source.dto.SourceDto;
import com.xiaowenhou.tcc.demo.common.target.api.TargetAccountService;
import com.xiaowenhou.tcc.demo.common.target.dto.TargetDto;
import com.xiaowenhou.tcc.demo.order.dao.OrderDao;
import com.xiaowenhou.tcc.demo.order.entity.OrderDo;
import com.xiaowenhou.tcc.demo.order.service.TransferService;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hmily.annotation.HmilyTCC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class TransferServiceImpl implements TransferService {
    private final OrderDao orderDao;

    private final SourceAccountService sourceService;

    private final TargetAccountService targetService;

    @Autowired(required = false)
    public TransferServiceImpl(OrderDao orderDao, SourceAccountService sourceService, TargetAccountService targetService) {
        this.orderDao = orderDao;
        this.sourceService = sourceService;
        this.targetService = targetService;
    }

    @Override
    @HmilyTCC(confirmMethod = "confirmOrderStatus", cancelMethod = "cancelOrderStatus")
    public void doTransfer(OrderDo order) {
        //转账
        sourceService.updateBalance(buildSourceDto(order));
        targetService.updateBalance(buildTargetDto(order));
    }

    public void confirmOrderStatus(OrderDo order) {
        order.setStatus(1);//表示转账成功
        order.setUpdatedTime(System.currentTimeMillis());
        orderDao.updateById(order);
        log.info("======order  confirm操作成功======");
    }

    public void cancelOrderStatus(OrderDo order) {
        order.setStatus(2);//表示转账失败
        order.setUpdatedTime(System.currentTimeMillis());
        orderDao.updateById(order);
        log.info("======order  cancel操作成功======");
    }


    private SourceDto buildSourceDto(OrderDo order) {
        SourceDto sourceDto = new SourceDto();
        sourceDto.setAccountNo(order.getSourceAccountNo());
        sourceDto.setAmount(order.getAmount());
        return sourceDto;
    }

    private TargetDto buildTargetDto(OrderDo order) {
        TargetDto targetDto = new TargetDto();
        targetDto.setAccountNo(order.getTargetAccountNo());
        targetDto.setAmount(order.getAmount());
        return targetDto;
    }
}
