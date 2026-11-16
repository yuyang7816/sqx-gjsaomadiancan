package com.sqx.modules.banner.controller.app;


import com.sqx.common.utils.Result;
import com.sqx.modules.banner.service.BannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liyuan
 * @date 2021/8/9
 */
@Slf4j
@RestController
@Api(value = "app banner图", tags = {"app banner图"})
@RequestMapping(value = "/app/banner")
public class AppBannerController {


    @Autowired
    private BannerService bannerService;

    @RequestMapping(value = "/selectBannerList", method = RequestMethod.GET)
    @ApiOperation("查询所有banner图")
    @ResponseBody
    public Result selectBannerList(HttpServletRequest request, Integer classify) {
        String languageType = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
        return Result.success().put("data", bannerService.selectBannerList(classify, languageType));
    }


}