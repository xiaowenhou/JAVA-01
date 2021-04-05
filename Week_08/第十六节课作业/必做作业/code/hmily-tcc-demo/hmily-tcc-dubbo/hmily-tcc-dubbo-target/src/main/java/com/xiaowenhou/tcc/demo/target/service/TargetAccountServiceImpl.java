package com.xiaowenhou.tcc.demo.target.service;

import com.xiaowenhou.tcc.demo.common.target.api.TargetAccountService;
import com.xiaowenhou.tcc.demo.common.target.dto.TargetDto;
import com.xiaowenhou.tcc.demo.target.dao.TargetAccountDao;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hmily.annotation.HmilyTCC;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service("targetAccountService")
public class TargetAccountServiceImpl implements TargetAccountService {

    @Resource
    private TargetAccountDao targetDao;

    @Override
    @HmilyTCC(confirmMethod = "confirmUpdateBalance", cancelMethod = "cancelUpdateBalance")
    public void updateBalance(TargetDto targetDto) {
       int result = targetDao.updateBalance(targetDto.getAccountNo(), targetDto.getAmount(), System.currentTimeMillis());
       if (result != 1) {
           throw new RuntimeException("执行更新账户余额失败...");
       }
    }

    public void confirmUpdateBalance(TargetDto targetDto) {
        log.info("=============执行目标账户的confirm方法===================");
        targetDao.confirmUpdateBalance(targetDto.getAccountNo(), targetDto.getAmount());
    }

    public void cancelUpdateBalance(TargetDto targetDto) {
        log.info("=============执行目标账户的cancel方法===================");
        targetDao.cancelUpdateBalance(targetDto.getAccountNo(), targetDto.getAmount());
    }
}