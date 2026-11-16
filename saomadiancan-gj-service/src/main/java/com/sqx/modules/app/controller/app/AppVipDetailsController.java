package com.sqx.modules.app.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.entity.VipDetails;
import com.sqx.modules.app.service.VipDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/app/VipDetails")

@Api("app 会员详情信息")
public class AppVipDetailsController {
    @Autowired
    private VipDetailsService appVipDetailsService;
    /**
     * 查询会员的详情信息
     *
     * @return
     */
    @Login
    @ApiOperation("查询会员的详情信息")
    @GetMapping("/selectVipDetails")
    public Result selectVipDetails(HttpServletRequest request) {
        String languageType = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
        return appVipDetailsService.selectVipDetails(languageType);
    }

    /**
     * 添加会员的详情信息
     *
     * @return
     */
    @Login
    @ApiOperation("添加会员的详情信息")
    @GetMapping("/insertVipDetails")
    public Result insertVipDetails(VipDetails vipDetails) {
        return appVipDetailsService.insertVipDetails(vipDetails);

    }
}


