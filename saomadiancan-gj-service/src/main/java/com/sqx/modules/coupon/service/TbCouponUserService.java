package com.sqx.modules.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.coupon.entity.TbCouponUser;

import java.math.BigDecimal;

public interface TbCouponUserService extends IService<TbCouponUser> {

    Result selectCouponList(Long userId, Integer page, Integer limit, BigDecimal minMoney);

    Result CouponList(Long userId, Integer page, Integer limit, Integer status,String phone);

    void updateExpiration();

    Result buyCoupon(Long userId, Long couponId);

    Result setCoupon(String userIds, Long couponId);

    Result selectCouponByUserId(Integer page, Integer limit, Long userId, Integer status);

}
