package com.sqx.modules.integral.controller.app;

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

@Api(tags={"用户端-积分"})
@RestController
@RequestMapping("/app/userintegral")
public class AppUserIntegralController {

    @Autowired
    private UserIntegralService userIntegralService;

    @Login
    @ApiOperation("判断用户是否已签到")
    @GetMapping(value = "/isSignIn")
    public Result isSignIn(@RequestAttribute Long userId){

        return userIntegralService.isSignIn(userId);
    }

    @Login
    @ApiOperation("计算今日签到应得积分数")
    @GetMapping(value = "/todayIntegral")
    public Result todayIntegral(@RequestAttribute Long userId){

        return userIntegralService.todayIntegral(userId);
    }

    @Login
    @ApiOperation("查看连续签到天数")
    @GetMapping(value = "continuousDay")
    public Result continuousDay(@RequestAttribute Long userId){

        return userIntegralService.continuousDay(userId);
    }


    @Login
    @ApiOperation("每日签到")
    @GetMapping(value = "/signIn")
    public Result signIn(@RequestAttribute Long userId){

        return userIntegralService.signIn(userId);
    }

    @Login
    @ApiOperation("查看积分")
    @GetMapping(value = "/selectUserIntegral")
    public Result selectUserIntegral(@RequestAttribute Long userId){

        return userIntegralService.selectUserIntegral(userId);
    }

    @Login
    @ApiOperation("用户积分、优惠券、钱包余额")
    @GetMapping(value = "/findUserMessage")
    public Result findUserMessage(@RequestAttribute Long userId){

        return userIntegralService.findUserMessage(userId);
    }


}
