package com.sqx.modules.integral.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.integral.service.UserIntegralService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags={"积分"})
@RestController
@RequestMapping("/userintegral")
public class UserIntegralController {

    @Autowired
    private UserIntegralService userIntegralService;

    @ApiOperation("查看连续签到天数")
    @GetMapping(value = "continuousDay")
    public Result continuousDay( Long userId){
        return userIntegralService.continuousDay(userId);
    }

    @ApiOperation("每日签到")
    @GetMapping(value = "/signIn")
    public Result signIn( Long userId){
        return userIntegralService.signIn(userId);
    }

    @ApiOperation("查看积分")
    @GetMapping(value = "/selectUserIntegral")
    public Result selectUserIntegral( Long userId){
        return userIntegralService.selectUserIntegral(userId);
    }
    
    @ApiOperation("用户积分、优惠券、钱包余额")
    @GetMapping(value = "/findUserMessage")
    public Result findUserMessage( Long userId){
        return userIntegralService.findUserMessage(userId);
    }


}
