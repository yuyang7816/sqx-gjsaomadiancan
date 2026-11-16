package com.sqx.modules.integral.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.integral.service.UserIntegralDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Api(tags={"用户端-积分明细"})
@RestController
@RequestMapping("/app/userintegraldetails")
public class AppUserIntegralDetailsController {

    @Autowired
    private UserIntegralDetailsService userIntegralDetailsService;

    @Login
    @ApiOperation("查看积分明细")
    @GetMapping(value = "selectIntegraldetailsList")
    public Result selectIntegraldetailsList(HttpServletRequest request, @RequestAttribute Long userId, Integer page, Integer limit, Integer classify){
        String languageType = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
        return userIntegralDetailsService.selectIntegraldetailsList(userId, page, limit, classify, languageType);
    }



}
