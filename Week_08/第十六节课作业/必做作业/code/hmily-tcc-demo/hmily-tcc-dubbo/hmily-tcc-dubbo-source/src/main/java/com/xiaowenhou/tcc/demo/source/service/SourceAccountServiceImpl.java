package com.xiaowenhou.tcc.demo.source.service;

import com.xiaowenhou.tcc.demo.common.source.api.SourceAccountService;
import com.xiaowenhou.tcc.demo.common.source.dto.SourceDto;
import com.xiaowenhou.tcc.demo.source.dao.SourceAccountDao;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hmily.annotation.HmilyTCC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;

@Slf4j
@Service("sourceAccountService")
public class SourceAccountServiceImpl implements SourceAccountService {

    @Resource
    private SourceAccountDao sourceDao;

    @Override
    @HmilyTCC(confirmMethod = "confirmUpdateBalance", cancelMethod = "cancelUpdateBalance")
    public void updateBalance(SourceDto sourceDto) {
        //里面最好只有一步事务操作
        int result = sourceDao.updateBalance(sourceDto.getAccountNo(), sourceDto.getAmount(), System.currentTimeMillis());
        if (result != 1) {
            throw new RuntimeException("扣减余额异常");
        }
    }

    public void confirmUpdateBalance(SourceDto sourceDto) {
        log.info("==========执行Source的confirm阶段============");
        sourceDao.confirmUpdateBalance(sourceDto.getAccountNo(), sourceDto.getAmount());
    }

    public void cancelUpdateBalance(SourceDto sourceDto) {
        log.info("==========执行Source的cancel阶段============");
        sourceDao.cancelUpdateBalance(sourceDto.getAccountNo(), sourceDto.getAmount());
    }
}