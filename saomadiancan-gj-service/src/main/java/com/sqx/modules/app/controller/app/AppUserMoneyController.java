package com.sqx.modules.app.controller.app;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.service.UserMoneyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/app/userMoney")
@Api(tags={"用户端-钱包"})
public class AppUserMoneyController {

    @Autowired
    private UserMoneyService userMoneyService;

    @GetMapping("/selectMyMoney")
    @Login
    @ApiOperation("我的钱包余额")
    public Result selectMyMoney(@RequestAttribute Long userId) {
        return Result.success().put("data", userMoneyService.selectUserMoneyByUserId(userId));
    }

    /**
     * 我的收益
     */
    @GetMapping("/selectMyProfit")
    @Login
    @ApiOperation("我的收益")
    public Result selectMyProfit(@RequestAttribute Long userId) {
        return Result.success().put("data", userMoneyService.selectMyProfit(userId));
    }

    /**
     * 余额明细
     */
    @GetMapping("/balanceDetailed")
    @Login
    @ApiOperation("余额明细")
    public Result balanceDetailed(HttpServletRequest request,  @RequestAttribute Long userId, Integer page, Integer limit) {
        String languageType = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
        return userMoneyService.balanceDetailed(userId, page, limit, languageType);
    }


    /**
     * 收益明细
     */
    @GetMapping("/profitDetailed")
    @Login
    @ApiOperation("收益明细")
    public Result profitDetailed(@RequestAttribute Long userId, Long page, Long limit) {
        if (page != null || limit != null) {
            return Result.error("50002");
        } else {
            IPage ipage = new Page(page, limit);
            return Result.success().put("data", userMoneyService.profitDetailed(userId, ipage));
        }
    }



}
