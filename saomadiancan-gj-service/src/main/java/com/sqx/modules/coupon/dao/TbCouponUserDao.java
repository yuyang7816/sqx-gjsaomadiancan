package com.sqx.modules.coupon.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.coupon.entity.TbCouponUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

@Mapper
public interface TbCouponUserDao extends BaseMapper<TbCouponUser> {

    IPage<TbCouponUser> selectCouponList(Page<TbCouponUser> pages, @Param("userId") Long userId,
                                         @Param("minMoney") BigDecimal minMoney, @Param("format") String format);

    IPage<TbCouponUser> CouponList(Page<TbCouponUser> pages,@Param("userId") Long userId,@Param("status") Integer status,@Param("phone") String phone);

    void updateExpiration(@Param("date") String date);

    IPage<TbCouponUser> selectCouponByUserId(Page<TbCouponUser> pages, @Param("userId") Long userId,@Param("status") Integer status);

    int selectCountCoupon(Long userId);

}
