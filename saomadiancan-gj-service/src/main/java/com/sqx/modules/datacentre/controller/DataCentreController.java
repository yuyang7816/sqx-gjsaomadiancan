package com.sqx.modules.datacentre.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.coupon.service.TbCouponUserService;
import com.sqx.modules.datacentre.service.DataCentreService;
import com.sqx.modules.integral.service.UserIntegralDetailsService;
import com.sqx.modules.order.service.AppOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@RestController
@Api(value = "数据中心-管理端", tags = {"数据中心-管理端"})
@RequestMapping(value = "/admin/dataCentre")
public class DataCentreController {

    @Autowired
    private DataCentreService dataCentreService;
    @Autowired
    private AppOrderService appOrderService;
    @Autowired
    private TbCouponUserService tbCouponUserService;
    @Autowired
    private UserIntegralDetailsService userIntegralDetailsService;
    @Autowired
    private UserMoneyService userMoneyService;

    @ApiOperation("数据中心")
    @GetMapping(value = "/dataCentre")
    public Result dataCentre() {
        return dataCentreService.dataCentre();
    }

    @ApiOperation("数据中心(新增)")
    @GetMapping(value = "dataCentreNew")
    public Result dataCentreNew(Long shopId) {

        return dataCentreService.dataCentreNew(shopId);
    }

    @ApiOperation("任务分析")
    @GetMapping(value = "selectOrderData")
    public Result selectOrderData(String date, String dateType) {

        return dataCentreService.selectOrderData(date, dateType);
    }

    @ApiOperation("任务详情分析")
    @GetMapping(value = "/selectOrderAnalyze")
    public Result selectPayOrderAnalyze(Integer page, Integer limit, String date, String dateType) {

        return dataCentreService.selectPayOrderAnalyze(page, limit, date, dateType);
    }

    @ApiOperation("条件筛选所有用户")
    @GetMapping(value = "selectAllUser")
    public Result selectAllUser(Integer page, Integer limit, String userName, String phone) {

        return dataCentreService.selectAllUser(page, limit, userName, phone);
    }

    @ApiOperation("根据userId查询用户信息")
    @GetMapping(value = "selectUserById")
    public Result selectUserById(Long userId) {

        return dataCentreService.selectUserById(userId);
    }

    @ApiOperation("给用户账户增加余额")
    @GetMapping(value = "addUserMoney")
    public Result addUserMoney(Long userId, BigDecimal money, Integer type) {

        return userMoneyService.addUserMoney(userId, money, type);
    }

    @ApiOperation("查看该用户下单信息(下单数、下单金额)")
    @GetMapping(value = "selectOrderByUserId")
    public Result selectOrderByUserId(Long userId, String date, String dateType) {

        return appOrderService.selectOrderByUserId(userId, date, dateType);
    }

    @ApiOperation("查看该用户下单详情")
    @GetMapping(value = "selectOrderDetails")
    public Result selectOrderDetails(Integer page, Integer limit, Long userId) {

        return appOrderService.selectOrderDetails(page, limit, userId);
    }

    @ApiOperation("查看用户拥有的优惠券")
    @GetMapping(value = "selectCouponByUserId")
    public Result selectCouponByUserId(Integer page, Integer limit, Long userId, Integer status) {

        return tbCouponUserService.selectCouponByUserId(page, limit, userId, status);
    }

    @ApiOperation("查看积分明细")
    @GetMapping(value = "/selectSignIn")
    public Result selectSignIn(Integer page, Integer limit, Long userId) {

        return userIntegralDetailsService.selectSignIn(page, limit, userId);
    }

    @ApiOperation("财务中心-充值统计")
    @GetMapping(value = "selectTopUpStatistics")
    public Result selectTopUpStatistics(String date, String dateType) {

        return dataCentreService.selectTopUpStatistics(date, dateType);
    }

    @ApiOperation("门店统计")
    @GetMapping(value = "selectShopCenter")
    public Result selectShopCenter(String date, String dateType, Integer page, Integer limit) {

        return dataCentreService.selectShopCenter(date, dateType, page, limit);
    }

    @ApiOperation("用户统计")
    @GetMapping(value = "selectUserCenter")
    public Result selectUserCenter(String date, String dateType) {

        return dataCentreService.selectUserCenter(date, dateType);
    }

    @ApiOperation("店铺年月日收益")
    @GetMapping(value = "selectShopMoney")
    public Result selectShopMoney(HttpServletRequest request, Long shopId) {
        String languageType = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
        return dataCentreService.selectShopMoney(shopId, languageType);
    }

}
