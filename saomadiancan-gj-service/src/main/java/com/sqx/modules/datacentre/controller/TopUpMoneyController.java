package com.sqx.modules.datacentre.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.datacentre.entity.TopUpMoney;
import com.sqx.modules.datacentre.service.TopUpMoneyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "管理端-充值金额配置", tags = {"管理端-充值金额配置"})
@RequestMapping(value = "/admin/topupmoney")
public class TopUpMoneyController {

    @Autowired
    private TopUpMoneyService topUpMoneyService;

    @ApiOperation("查")
    @GetMapping(value = "selectTopUpMoney")
    public Result selectTopUpMoney(){

        return topUpMoneyService.selectTopUpMoney();
    }

    @ApiOperation("增")
    @PostMapping(value = "insertTopUpMoney")
    public Result insertTopUpMoney(@RequestBody TopUpMoney topUpMoney){

        return topUpMoneyService.insertTopUpMoney(topUpMoney);
    }

    @ApiOperation("改")
    @PostMapping(value = "updateTopUpMoney")
    public Result updateTopUpMoney(@RequestBody TopUpMoney topUpMoney){

        return topUpMoneyService.updateTopUpMoney(topUpMoney);
    }

    @ApiOperation("删")
    @GetMapping(value = "deleteTopUpMoney")
    public Result deleteTopUpMoney(Long id){

        return topUpMoneyService.deleteTopUpMoney(id);
    }


}
