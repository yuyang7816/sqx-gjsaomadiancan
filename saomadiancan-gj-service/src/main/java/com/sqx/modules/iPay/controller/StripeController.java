package com.sqx.modules.iPay.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.iPay.param.BusinessParam;
import com.sqx.modules.iPay.service.StripeService;
import com.stripe.exception.StripeException;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/3/29 9:40
 */
@Slf4j
@RestController
@RequestMapping("/app/stripe")
public class StripeController {
    @Autowired
    private StripeService stripeService;

    /**
     * 支付订单
     * @return
     * @throws StripeException
     */
    @PostMapping("pay")
    @Login
    public Result pay(@RequestAttribute("userId") Long userId, @RequestBody @Validated BusinessParam businessParam) throws StripeException {
        businessParam.setUserId(userId);
        return stripeService.pay(businessParam);
    }

    @ApiOperation("stripe支付回调")
    @PostMapping("/webhook")
    public void webhook(HttpServletRequest request, HttpServletResponse response) {
        stripeService.callback(request, response);
    }
}
