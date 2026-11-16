package com.sqx.modules.message.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.message.service.OrderMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "门店端-订单消息管理", tags = {"门店端-订单消息管理"})
@RequestMapping(value = "/admin/ordermessage")
public class OrderMessageController {

    @Autowired
    private OrderMessageService orderMessageService;

    @ApiOperation("查看未读消息条数")
    @PostMapping(value = "selectReadFlagCount")
    public Result selectReadFlagCount( String shopIds){

        String[] shopId = shopIds.split(",");
        return orderMessageService.selectReadFlagCount(shopId);
    }

    @ApiOperation("查看所有未读消息列表")
    @PostMapping(value = "selectReadFlag")
    public Result selectReadFlag(Integer page, Integer limit, String shopIds){
        String[] shopId = shopIds.split(",");
        return orderMessageService.selectReadFlag(page, limit, shopId);
    }

    @ApiOperation("店铺查看单个未读消息（改成已读）")
    @GetMapping(value = "checkOrderMessage")
    public Result checkOrderMessage(Long id){

        return orderMessageService.checkOrderMessage(id);
    }

    @ApiOperation("店铺查看所有未读消息（改成已读）")
    @PostMapping(value = "allcheckOrderMessage")
    public Result allcheckOrderMessage(String shopIds){
        String[] shopId = shopIds.split(",");
        return orderMessageService.allcheckOrderMessage(shopId);
    }

}
