package com.sqx.modules.datacentre.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.datacentre.service.TopUpMoneyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "用户端-充值金额配置", tags = {"用户端-充值金额配置"})
@RequestMapping(value = "/app/topupmoney")
public class AppTopUpMoneyController {

    @Autowired
    private TopUpMoneyService topUpMoneyService;

    @ApiOperation("查")
    @GetMapping(value = "selectTopUpMoney")
    public Result selectTopUpMoney(){

        return topUpMoneyService.selectTopUpMoney();
    }

}
