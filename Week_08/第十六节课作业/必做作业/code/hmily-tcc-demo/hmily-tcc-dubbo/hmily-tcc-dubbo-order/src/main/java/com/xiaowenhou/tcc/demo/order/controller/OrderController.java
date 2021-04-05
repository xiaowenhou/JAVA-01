package com.xiaowenhou.tcc.demo.order.controller;

import com.xiaowenhou.tcc.demo.order.service.OrderService;
import com.xiaowenhou.tcc.demo.order.vo.TransferVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;


    @PostMapping("/transfer")
    public String transfer(@RequestBody TransferVO transferVO) {

        orderService.transfer(transferVO);
        return "success";
    }

}
