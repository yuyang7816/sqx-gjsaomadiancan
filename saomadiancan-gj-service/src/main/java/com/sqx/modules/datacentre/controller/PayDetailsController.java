package com.sqx.modules.datacentre.controller;

import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.pay.service.PayDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "管理端-消费记录", tags = {"管理端-消费记录"})
@RequestMapping(value = "/admin/paydetails")
public class PayDetailsController {

    @Autowired
    private PayDetailsService payDetailsService;

    @ApiOperation("查看消费记录")
    @GetMapping(value = "selectPayDetails")
    public Result selectPayDetails(Integer page, Integer limit, Long userId, String startTime, String endTime){

        return payDetailsService.selectPayDetailsById(page, limit, userId, startTime, endTime);
    }

}
