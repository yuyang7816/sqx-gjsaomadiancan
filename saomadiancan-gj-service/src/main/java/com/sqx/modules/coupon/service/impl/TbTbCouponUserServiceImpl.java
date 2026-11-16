package com.sqx.modules.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.coupon.dao.TbCouponDao;
import com.sqx.modules.coupon.dao.TbCouponUserDao;
import com.sqx.modules.coupon.entity.TbCoupon;
import com.sqx.modules.coupon.entity.TbCouponUser;
import com.sqx.modules.coupon.service.TbCouponUserService;
import com.sqx.modules.integral.dao.UserIntegralDao;
import com.sqx.modules.integral.dao.UserIntegralDetailsDao;
import com.sqx.modules.integral.entity.UserIntegral;
import com.sqx.modules.integral.entity.UserIntegralDetails;
import com.sqx.modules.integral.service.UserIntegralService;
import com.sqx.modules.utils.DataSourceMessageSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service
public class TbTbCouponUserServiceImpl extends ServiceImpl<TbCouponUserDao, TbCouponUser> implements TbCouponUserService {

    @Autowired
    private TbCouponUserDao tbCouponUserDao;
    @Autowired
    private UserIntegralDao userIntegralDao;
    @Autowired
    private TbCouponDao tbCouponDao;
    @Autowired
    private UserIntegralDetailsDao userIntegralDetailsDao;
    @Autowired
    private DataSourceMessageSource dataSourceMessageSource;

    @Override
    public Result selectCouponList(Long userId, Integer page, Integer limit, BigDecimal minMoney) {
        Page<TbCouponUser> pages=new Page<>(page,limit);
        String format = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        PageUtils pageUtils = new PageUtils(tbCouponUserDao.selectCouponList(pages, userId, minMoney, format));
        return Result.success().put("data", pageUtils);
    }

    @Override
    public Result CouponList(Long userId, Integer page, Integer limit, Integer status,String phone) {
        Page<TbCouponUser> pages=new Page<>(page,limit);
        PageUtils pageUtils = new PageUtils(tbCouponUserDao.CouponList(pages, userId, status,phone));
        return Result.success().put("data", pageUtils);
    }

    @Override
    public void updateExpiration() {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        tbCouponUserDao.updateExpiration(date);
    }

    @Transactional
    @Override
    public Result buyCoupon(Long userId, Long couponId) {
        UserIntegral userIntegral = userIntegralDao.selectOne(new QueryWrapper<UserIntegral>().eq("user_id", userId));
        TbCoupon tbCoupon = tbCouponDao.selectById(couponId);
        //todo
        if(userIntegral.getIntegralNum()<tbCoupon.getNeedIntegral()){
            return Result.error("60143");
        }
        //扣除用户积分
        userIntegralDao.updateUserIntegral(userId, tbCoupon.getNeedIntegral());
        //将优惠券添加到用户优惠券仓库
        TbCouponUser tbCouponUser = new TbCouponUser();
        tbCouponUser.setUserId(userId);
        tbCouponUser.setCouponName(tbCoupon.getCouponName());
        tbCouponUser.setCouponPicture(tbCoupon.getCouponPicture());
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        tbCouponUser.setCreateTime(format);
        //计算优惠券过期时间
        Integer endDate = tbCoupon.getEndDate();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, endDate);
        calendar.add(Calendar.DATE, 1);
        Date date = calendar.getTime();
        String format1 = new SimpleDateFormat("yyyy-MM-dd").format(date);
        tbCouponUser.setExpirationTime(format1);
        tbCouponUser.setMinMoney(tbCoupon.getMinMoney());
        tbCouponUser.setGoodsType(tbCoupon.getGoodsType());
        tbCouponUser.setMoney(tbCoupon.getMoney());
        tbCouponUser.setStatus(0);
        tbCouponUser.setEndTime(tbCoupon.getEndDate().toString());
        tbCouponUser.setNeedIntegral(tbCoupon.getNeedIntegral().toString());
        tbCouponUserDao.insert(tbCouponUser);
        //添加优惠券购买记录
        //新增签到获取积分记录
        UserIntegralDetails userIntegralDetails1 = new UserIntegralDetails();
        userIntegralDetails1.setContent(dataSourceMessageSource.getConfigI18ForCach("60144"));
        userIntegralDetails1.setClassify(2);
        userIntegralDetails1.setType(2);
        userIntegralDetails1.setNum(tbCoupon.getNeedIntegral());
        userIntegralDetails1.setCreateTime(format);
        userIntegralDetails1.setUserId(userId);
        userIntegralDetailsDao.insert(userIntegralDetails1);
        return Result.success();
    }


    @Transactional
    @Override
    public Result setCoupon(String userIds, Long couponId) {
        for(String strUserId:userIds.split(",")){
            long userId = Long.parseLong(strUserId);
            TbCoupon tbCoupon = tbCouponDao.selectById(couponId);
            //将优惠券添加到用户优惠券仓库
            TbCouponUser tbCouponUser = new TbCouponUser();
            tbCouponUser.setUserId(userId);
            tbCouponUser.setCouponName(tbCoupon.getCouponName());
            tbCouponUser.setCouponPicture(tbCoupon.getCouponPicture());
            String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            tbCouponUser.setCreateTime(format);
            //计算优惠券过期时间
            Integer endDate = tbCoupon.getEndDate();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, endDate);
            calendar.add(Calendar.DATE, 1);
            Date date = calendar.getTime();
            String format1 = new SimpleDateFormat("yyyy-MM-dd").format(date);
            tbCouponUser.setExpirationTime(format1);
            tbCouponUser.setMinMoney(tbCoupon.getMinMoney());
            tbCouponUser.setGoodsType(tbCoupon.getGoodsType());
            tbCouponUser.setMoney(tbCoupon.getMoney());
            tbCouponUser.setStatus(0);
            tbCouponUser.setEndTime(tbCoupon.getEndDate().toString());
            tbCouponUser.setNeedIntegral(tbCoupon.getNeedIntegral().toString());
            tbCouponUserDao.insert(tbCouponUser);
        }


        return Result.success();
    }


    @Override
    public Result selectCouponByUserId(Integer page, Integer limit, Long userId, Integer status) {
        Page<TbCouponUser> pages = new Page<>(page, limit);
        PageUtils pageUtils = new PageUtils(tbCouponUserDao.selectCouponByUserId(pages, userId, status));
        return Result.success().put("data", pageUtils);
    }
}
