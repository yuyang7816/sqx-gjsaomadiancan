package com.sqx.modules.coupon.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.coupon.entity.TbCoupon;
import com.sqx.modules.coupon.service.TbCouponService;
import com.sqx.modules.coupon.service.TbCouponUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "管理端-优惠券", tags = {"管理端-优惠券"})
@RequestMapping(value = "/admin/coupon")
public class CouponController {

    @Autowired
    private TbCouponService tbCouponService;
    @Autowired
    private TbCouponUserService tbCouponUserService;

    @ApiOperation("查看发布的优惠券")
    @GetMapping(value = "seleteAllCoupon")
    public Result seleteAllCoupon(Integer page, Integer limit){

        return tbCouponService.seleteAllCoupon(page, limit);
    }

    @ApiOperation("发布优惠券")
    @PostMapping(value = "issueCoupon")
    public Result issueCoupon(@RequestBody TbCoupon tbCoupon){

        return tbCouponService.issueCoupon(tbCoupon);
    }

    @ApiOperation("删除发布的优惠券")
    @PostMapping(value = "deleteCoupon")
    public Result deleteCoupon(Long couponId){

        return tbCouponService.deleteCoupon(couponId);
    }

    @ApiOperation("修改优惠券信息")
    @PostMapping(value = "updateCoupon")
    public Result updateCoupon(@RequestBody TbCoupon tbCoupon){

        return tbCouponService.updateCoupon(tbCoupon);
    }


    @ApiOperation("优惠券仓库查看所有优惠券")
    @GetMapping(value = "couponList")
    public Result couponList(Long userId, Integer page, Integer limit, Integer status,String phone){

        return tbCouponUserService.CouponList(userId, page, limit, status,phone);
    }

    @ApiOperation("赠送优惠券")
    @PostMapping(value = "setCoupon")
    public Result setCoupon(String userIds, Long couponId){
        return tbCouponUserService.setCoupon(userIds, couponId);
    }

}