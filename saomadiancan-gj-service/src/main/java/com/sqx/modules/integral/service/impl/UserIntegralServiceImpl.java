package com.sqx.modules.integral.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.dao.UserMoneyDao;
import com.sqx.modules.app.entity.UserMoney;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.coupon.dao.TbCouponUserDao;
import com.sqx.modules.integral.dao.UserIntegralDao;
import com.sqx.modules.integral.dao.UserIntegralDetailsDao;
import com.sqx.modules.integral.entity.UserIntegral;
import com.sqx.modules.integral.entity.UserIntegralDetails;
import com.sqx.modules.integral.service.UserIntegralDetailsService;
import com.sqx.modules.integral.service.UserIntegralService;
import com.sqx.modules.utils.DataSourceMessageSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

@Service
public class UserIntegralServiceImpl extends ServiceImpl<UserIntegralDao, UserIntegral> implements UserIntegralService {

    @Autowired
    private UserIntegralDao userIntegralDao;
    @Autowired
    private UserIntegralDetailsDao userIntegralDetailsDao;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private UserIntegralService userIntegralService;
    @Autowired
    private TbCouponUserDao tbCouponUserDao;
    @Autowired
    private UserMoneyDao userMoneyDao;
    @Autowired
    private DataSourceMessageSource dataSourceMessageSource;
    @Autowired
    private UserIntegralDetailsService userIntegralDetailsService;

    @Override
    public Result isSignIn(Long userId) {
        String format = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        UserIntegralDetails signIn = userIntegralDetailsDao.isSignIn(userId, format);
        if(signIn!=null){
            return Result.success().put("data", dataSourceMessageSource.getConfigI18ForCach("60170"));
        }
        return Result.success().put("data", dataSourceMessageSource.getConfigI18ForCach("60171"));
    }

    @Override
    public Result selectUserIntegral(Long userId) {
        UserIntegral userIntegral = userIntegralDao.selectOne(new QueryWrapper<UserIntegral>().eq("user_id", userId));
        return Result.success().put("data", userIntegral);
    }

    @Override
    public Result continuousDay(Long userId) {
        HashMap hashMap = new HashMap();
        Integer dayNum;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String yesterday = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
        UserIntegralDetails userIntegralDetailsYesterday = userIntegralDetailsDao.selectUserIntegralDetails(userId, yesterday);
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        UserIntegralDetails userIntegralDetailsToday = userIntegralDetailsDao.selectUserIntegralDetails(userId, date);
        if(userIntegralDetailsYesterday==null){
            if(userIntegralDetailsToday==null){
                dayNum = 0;
            }else {
                dayNum = 1;
            }
        }else {
            if(userIntegralDetailsToday==null){
                dayNum = userIntegralDetailsYesterday.getDay();
            }else {
                dayNum = userIntegralDetailsToday.getDay();
            }
        }
        hashMap.put("dayNum", dayNum);
        CommonInfo one = commonInfoService.findOne(118);
        Integer daySum = Integer.valueOf(one.getValue());
        hashMap.put("daySum", daySum);
        return Result.success().put("data", hashMap);
    }

    @Override
    public Result findUserMessage(Long userId) {
        HashMap hashMap = new HashMap();
        UserIntegral userIntegral = userIntegralDao.selectOne(new QueryWrapper<UserIntegral>().eq("user_id", userId));
        int countCoupon = tbCouponUserDao.selectCountCoupon(userId);
        UserMoney userMoney = userMoneyDao.selectOne(new QueryWrapper<UserMoney>().eq("user_id", userId));

        hashMap.put("userIntegral", userIntegral.getIntegralNum());
        hashMap.put("countCoupon", countCoupon);
        hashMap.put("userMoney", userMoney.getMoney());
        return Result.success().put("data", hashMap);
    }

    public static String getCurrDayOfWeek() {
        Date date = new Date();
        SimpleDateFormat dateFm = new SimpleDateFormat("EEEE", Locale.CHINA);
        return dateFm.format(date);
    }

    //计算今日签到应得积分数
    @Override
    public Result todayIntegral(Long userId) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
        UserIntegralDetails userIntegralDetails = userIntegralDetailsDao.selectUserIntegralDetails(userId, date);
        if(userIntegralDetails==null){
            CommonInfo one = commonInfoService.findOne(119);
            Integer num = Integer.valueOf(one.getValue());
            return Result.success().put("data", num);
        }else {
            Integer todayIntegral;
            CommonInfo one = commonInfoService.findOne(118);
            Integer value = Integer.valueOf(one.getValue());
            CommonInfo one1 = commonInfoService.findOne(120);
            Integer add = Integer.valueOf(one1.getValue());
            if(userIntegralDetails.getDay()<value){
                //如果昨天已签到，且连续签到天数小于等于签到周期，则今天签到获得的积分=昨天的签到积分+10
                todayIntegral = userIntegralDetails.getNum() + add;
            }else {
                //如果昨天已签到，且连续签到天数大于签到周期，则今天签到获得的积分=昨天的签到积分
                todayIntegral = userIntegralDetails.getNum();
            }
        //    todayIntegral = userIntegralDetails.getNum() + add;
            return Result.success().put("data", todayIntegral);
        }
    }

    /*@Override
    public Result todayIntegral(Long userId) {
        String currDayOfWeek = getCurrDayOfWeek();
        if("星期一".equals(currDayOfWeek)){
            CommonInfo one = commonInfoService.findOne(119);
            Integer num = Integer.valueOf(one.getValue());
            return Result.success().put("data", num);
        }
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
        UserIntegralDetails userIntegralDetails = userIntegralDetailsDao.selectUserIntegralDetails(userId, date);
        if(userIntegralDetails==null){
            CommonInfo one = commonInfoService.findOne(119);
            Integer num = Integer.valueOf(one.getValue());
            return Result.success().put("data", num);
        }
        CommonInfo one = commonInfoService.findOne(120);
        Integer add = Integer.valueOf(one.getValue());
        Integer todayIntegral = userIntegralDetails.getNum() + add;
        return Result.success().put("data", todayIntegral);
    }*/

    //每日签到
    @Transactional
    @Override
    public Result signIn(Long userId) {
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        //先判断今日是否已签到
        UserIntegralDetails signIn = userIntegralDetailsDao.isSignIn(userId, format);
        if(signIn!=null) {
            return Result.success().put("data", dataSourceMessageSource.getConfigI18ForCach("60170"));
        }
        //计算今日获取积分
        UserIntegralDetails userIntegralDetails1 = new UserIntegralDetails();
        Integer todayIntegral;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String date = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss").format(cal.getTime());
        //昨天的签到记录
        UserIntegralDetails userIntegralDetails = userIntegralDetailsDao.selectUserIntegralDetails(userId, date);
        if(userIntegralDetails==null){
            CommonInfo one = commonInfoService.findOne(119);
            //如果昨天未签到，则今天签到获得的积分是初始积分
            todayIntegral = Integer.valueOf(one.getValue());
            //则今天是连续签到的第一天
            userIntegralDetails1.setDay(1);
        }else {
            CommonInfo one = commonInfoService.findOne(118);
            Integer value = Integer.valueOf(one.getValue());
            CommonInfo one1 = commonInfoService.findOne(120);
            Integer add = Integer.valueOf(one1.getValue());
            if(userIntegralDetails.getDay()<value){
                //如果昨天已签到，且连续签到天数小于等于签到周期，则今天签到获得的积分=昨天的签到积分+10
                todayIntegral = userIntegralDetails.getNum() + add;
            }else {
                //如果昨天已签到，且连续签到天数大于签到周期，则今天签到获得的积分=昨天的签到积分
                todayIntegral = userIntegralDetails.getNum();
            }
            //如果昨天已签到，则今天的连续签到天数=昨天的连续签到天数+1
            userIntegralDetails1.setDay(userIntegralDetails.getDay()+1);
        }
        //新增签到获取积分记录
        userIntegralDetails1.setContent(dataSourceMessageSource.getConfigI18ForCach("60172"));
        userIntegralDetails1.setClassify(1);
        userIntegralDetails1.setType(1);
        userIntegralDetails1.setNum(todayIntegral);
        userIntegralDetails1.setCreateTime(format);
        userIntegralDetails1.setUserId(userId);
//        userIntegralDetailsDao.insert(userIntegralDetails1);
        userIntegralDetailsService.saveI8n(userIntegralDetails1, "60172");
        //添加积分
        userIntegralDao.addUserIntegral(todayIntegral, userId);
        return Result.success();
    }

   /* @Override
    public Result signIn(Long userId) {
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        //先判断今日是否已签到
        UserIntegralDetails signIn = userIntegralDetailsDao.isSignIn(userId, format);
        if(signIn!=null){
            return Result.success().put("data", "今日已签到");
        }
        UserIntegralDetails userIntegralDetails1 = new UserIntegralDetails();
        //计算今日获取积分
        Integer todayIntegral;
        String currDayOfWeek = getCurrDayOfWeek();
        if("星期一".equals(currDayOfWeek)){
            CommonInfo one = commonInfoService.findOne(119);
            todayIntegral = Integer.valueOf(one.getValue());
            userIntegralDetails1.setDay(1);
        }else{
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            String date = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss").format(cal.getTime());
            UserIntegralDetails userIntegralDetails = userIntegralDetailsDao.selectUserIntegralDetails(userId, date);
            if(userIntegralDetails==null){
                CommonInfo one = commonInfoService.findOne(119);
                todayIntegral = Integer.valueOf(one.getValue());
                userIntegralDetails1.setDay(1);
            }else {
                CommonInfo one = commonInfoService.findOne(120);
                Integer add = Integer.valueOf(one.getValue());
                todayIntegral = userIntegralDetails.getNum() + add;
                userIntegralDetails1.setDay(userIntegralDetails.getDay()+1);
            }
        }
        //新增签到获取积分记录
        userIntegralDetails1.setContent("每日签到");
        userIntegralDetails1.setClassify(1);
        userIntegralDetails1.setType(1);
        userIntegralDetails1.setNum(todayIntegral);
        userIntegralDetails1.setCreateTime(format);
        userIntegralDetails1.setUserId(userId);
        userIntegralDetailsDao.insert(userIntegralDetails1);
        //添加积分
        userIntegralDao.addUserIntegral(todayIntegral, userId);
        return Result.success();
    }*/

}
