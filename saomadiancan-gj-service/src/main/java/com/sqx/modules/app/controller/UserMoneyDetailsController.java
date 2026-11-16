package com.sqx.modules.app.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Details")
@Api(tags = "钱包明细")
public class UserMoneyDetailsController {
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;

    @ApiOperation("钱包明细")
    @GetMapping("/queryUserMoneyDetails")
    public Result queryUserMoneyDetails(Integer page, Integer limit, Long userId, Integer classify, Integer type,Long shopId,String startTime,String endTime) {
        return userMoneyDetailsService.queryUserMoneyDetails(page, limit, userId,classify,type,shopId,startTime,endTime);
    }
    @ApiOperation("收银台统计")
    @GetMapping("/getShopStatistics")
    public Result getShopStatistics(Long shopId,String startTime,String endTime){
        return Result.success().put("data", userMoneyDetailsService.getShopStatistics(shopId,startTime,endTime));
    }




}
