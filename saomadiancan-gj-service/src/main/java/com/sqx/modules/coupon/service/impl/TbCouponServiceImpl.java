package com.sqx.modules.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.coupon.dao.TbCouponDao;
import com.sqx.modules.coupon.entity.TbCoupon;
import com.sqx.modules.coupon.service.TbCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TbCouponServiceImpl extends ServiceImpl<TbCouponDao, TbCoupon> implements TbCouponService {

    @Autowired
    private TbCouponDao tbCouponDao;

    @Override
    public Result seleteAllCoupon(Integer page, Integer limit) {
        Page<TbCoupon> pages = new Page<>(page, limit);
        PageUtils pageUtils = new PageUtils(baseMapper.selectPage(pages, null));
        return Result.success().put("data", pageUtils);
    }

    @Transactional
    @Override
    public Result issueCoupon(TbCoupon tbCoupon) {
        tbCoupon.setDeleteFlag(0);
        tbCouponDao.insert(tbCoupon);
        return Result.success();
    }

    @Override
    public Result updateCoupon(TbCoupon tbCoupon) {
        tbCouponDao.updateById(tbCoupon);
        return Result.success();
    }

    @Override
    public Result selectCouponList(Integer page, Integer limit) {
        Page<TbCoupon> pages = new Page<>(page, limit);
        PageUtils pageUtils = new PageUtils(baseMapper.selectPage(pages, new QueryWrapper<TbCoupon>().eq("delete_flag", 0)));
        return Result.success().put("data", pageUtils);
    }

    @Override
    public Result deleteCoupon(Long couponId) {
        tbCouponDao.deleteById(couponId);
        return Result.success();
    }


}
