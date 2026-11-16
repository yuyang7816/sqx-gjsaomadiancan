package com.sqx.modules.coupon.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.coupon.dao.TbCouponUserDao;
import com.sqx.modules.coupon.service.TbCouponService;
import com.sqx.modules.coupon.service.TbCouponUserService;
import com.sqx.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@Api(value = "用户端-优惠券", tags = {"用户端-优惠券"})
@RequestMapping(value = "/app/coupon")
public class AppTbCouponUserController extends AbstractController {

    @Autowired
    private TbCouponUserService tbCouponUserService;
    @Autowired
    private TbCouponUserDao tbCouponUserDao;
    @Autowired
    private TbCouponService tbCouponService;

    @Login
    @ApiOperation("使用时查看所有优惠券")
    @GetMapping(value = "selectUserCouponList")
    public Result selectCouponList(@RequestAttribute Long userId, Integer page, Integer limit, BigDecimal minMoney){

        return tbCouponUserService.selectCouponList(userId, page, limit, minMoney);
    }

    @Login
    @ApiOperation("优惠券仓库查看所有优惠券")
    @GetMapping(value = "CouponList")
    public Result CouponList(@RequestAttribute Long userId, Integer page, Integer limit, Integer status,String phone){

        return tbCouponUserService.CouponList(userId, page, limit, status,phone);
    }

    @Login
    @ApiOperation("查看可购买的优惠券")
    @GetMapping(value = "selectCouponList")
    public Result selectCouponList(Integer page, Integer limit){

        return tbCouponService.selectCouponList(page, limit);
    }

    @Login
    @ApiOperation("购买优惠券")
    @PostMapping(value = "buyCoupon")
    public Result buyCoupon(@RequestAttribute Long userId, Long couponId){

        return tbCouponUserService.buyCoupon(userId, couponId);
    }


    //将所有超过失效时间的优惠券改为失效状态
    @Scheduled(cron = "0 0 0 * * ?", zone = "Asia/Shanghai")
//    @GetMapping(value = "aaa")
    public void couponEnd(){

        tbCouponUserService.updateExpiration();
    }


}