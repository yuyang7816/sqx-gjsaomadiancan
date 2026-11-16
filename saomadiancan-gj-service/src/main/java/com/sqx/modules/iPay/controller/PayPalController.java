package com.sqx.modules.iPay.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.iPay.param.BusinessParam;
import com.sqx.modules.iPay.service.PayPalCheckoutService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/3/29 9:40
 */
@Slf4j
@RestController
@Api(value = "paypal支付", tags = {"paypal支付"})
@RequestMapping("/app/paypal")
public class PayPalController {

    @Autowired
    private PayPalCheckoutService payPalCheckoutService;

    /**
     * @param userId
     * @return
     */
    @Login
    @PostMapping("pay")
    public Result pay(@RequestAttribute("userId") Long userId, @RequestBody @Validated BusinessParam businessParam) {
        businessParam.setUserId(userId);
        return payPalCheckoutService.pay(businessParam);
    }

    @GetMapping("checkPay")
    @ApiOperation("执行支付")
    public String checkPay(String token,String PayerID,Integer payWay,HttpServletResponse response){
        return payPalCheckoutService.callback(token, PayerID, response);
    }

    /**
     * paypal支付回调
     * @param response 响应头
     */
    @ApiOperation("paypal支付回调")
    @GetMapping("/paypalCallback")
    public String paypalBack(String token, String PayerID, HttpServletResponse response){
        return payPalCheckoutService.callback(token, PayerID, response);
    }
}
