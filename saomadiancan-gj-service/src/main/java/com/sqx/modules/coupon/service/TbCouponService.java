package com.sqx.modules.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.coupon.entity.TbCoupon;

public interface TbCouponService extends IService<TbCoupon> {

    Result issueCoupon(TbCoupon tbCoupon);

    Result deleteCoupon(Long couponId);

    Result seleteAllCoupon(Integer page, Integer limit);

    Result updateCoupon(TbCoupon tbCoupon);

    Result selectCouponList(Integer page, Integer limit);

}
