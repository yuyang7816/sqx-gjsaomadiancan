package com.sqx.modules.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.dao.UserVipDao;
import com.sqx.modules.app.dao.VipDetailsDao;
import com.sqx.modules.app.entity.*;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.app.service.UserVipService;
import com.sqx.modules.utils.DataSourceMessageSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestAttribute;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service
public class UserVipServiceImpl extends ServiceImpl<UserVipDao, UserVip> implements UserVipService {

    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private VipDetailsDao vipDetailsDao;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private DataSourceMessageSource dataSourceMessageSource;

    @Override
    public UserVip selectUserVipByUserId(Long userId) {
        QueryWrapper<UserVip> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public Result isUserVip(Long userId) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //查询用户是否是会员
        QueryWrapper<UserVip> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        UserVip userVip = baseMapper.selectOne(queryWrapper);
        if (userVip != null) {
            //获取用户到期时间
            Date date = null;
            try {
                date = simpleDateFormat.parse(userVip.getEndTime());
            } catch (Exception e) {
                e.getMessage();
            }
            if (date.getTime() >= System.currentTimeMillis()) {
                userVip.setIsVip((long) 1);
                baseMapper.updateById(userVip);
                return Result.success().put("data", true);
            } else {
                userVip.setIsVip((long) 2);
                baseMapper.updateById(userVip);
                return Result.success().put("data", false);
            }
        } else {
            return Result.success().put("data", false);
        }
    }

    @Override
    public Result payVip(Long userId, Long vipTypeId){
        UserMoney userMoney = userMoneyService.selectUserMoneyByUserId(userId);
        VipDetails vipDetails = vipDetailsDao.selectOne(new QueryWrapper<VipDetails>().eq("id", vipTypeId));
        if(userMoney.getMoney().doubleValue()<vipDetails.getMoney().doubleValue()){
            return Result.error("60136");
        }
        userMoneyService.updateMoney(2,userId,vipDetails.getMoney().doubleValue());
        UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
        userMoneyDetails.setMoney(vipDetails.getMoney());
        userMoneyDetails.setUserId(userId);
        UserEntity userEntity = userService.selectUserById(userId);
        userEntity.setMember(1);

        UserVip userVip = baseMapper.selectOne(new QueryWrapper<UserVip>().eq("user_id", userId));
        Calendar cal = Calendar.getInstance();
        if(userVip==null){
            userVip=new UserVip();
            userVip.setUserId(userId);
            userVip.setIsVip(1L);
            userVip.setCreateTime(DateUtils.format(cal.getTime()));
        }else{
            //如果有会员 判断会员是否过期
            Date date = DateUtils.stringToDate(userVip.getEndTime());
            if(date.getTime()>cal.getTime().getTime()){
                cal.setTime(date);
            }
        }

        if ((vipDetails.getVipNameType().equals(0))) {
            //月
            userMoneyDetails.setContent(dataSourceMessageSource.getConfigI18ForCach("60137"));
            cal.add(Calendar.MONTH, 1);
        } else if ((vipDetails.getVipNameType()).equals(1)) {
            //季
            userMoneyDetails.setContent(dataSourceMessageSource.getConfigI18ForCach("60138"));
            cal.add(Calendar.MONTH, 3);
        } else {
            //年
            userMoneyDetails.setContent(dataSourceMessageSource.getConfigI18ForCach("60139"));
            cal.add(Calendar.YEAR, 1);
        }
        userMoneyDetails.setTitle(dataSourceMessageSource.getConfigI18ForCach("60140"));
        userMoneyDetails.setType(2);
        userMoneyDetails.setCreateTime(DateUtils.format(new Date()));
        userMoneyDetailsService.save(userMoneyDetails);
        userVip.setEndTime(DateUtils.format(cal.getTime()));
        if(userVip.getVipId()!=null){
            baseMapper.updateById(userVip);
        }else{
            baseMapper.insert(userVip);
        }
        userEntity.setEndTime(userVip.getEndTime());
        userService.updateById(userEntity);
        return Result.success();
    }

    @Override
    public Result setUserVip(Long userId){

        UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
        userMoneyDetails.setMoney(BigDecimal.ZERO);
        userMoneyDetails.setUserId(userId);
        UserEntity userEntity = userService.selectUserById(userId);
        userEntity.setMember(1);

        UserVip userVip = baseMapper.selectOne(new QueryWrapper<UserVip>().eq("user_id", userId));
        Calendar cal = Calendar.getInstance();
        if(userVip==null){
            userVip=new UserVip();
            userVip.setUserId(userId);
            userVip.setIsVip(1L);
            userVip.setCreateTime(DateUtils.format(cal.getTime()));
        }else{
            //如果有会员 判断会员是否过期
            Date date = DateUtils.stringToDate(userVip.getEndTime());
            if(date.getTime()>cal.getTime().getTime()){
                cal.setTime(date);
            }
        }
        userMoneyDetails.setContent(dataSourceMessageSource.getConfigI18ForCach("60137"));
        cal.add(Calendar.MONTH, 1);

        userMoneyDetails.setTitle(dataSourceMessageSource.getConfigI18ForCach("60141"));
        userMoneyDetails.setType(2);
        userMoneyDetails.setCreateTime(DateUtils.format(new Date()));
        userMoneyDetailsService.save(userMoneyDetails);
        userVip.setEndTime(DateUtils.format(cal.getTime()));
        if(userVip.getVipId()!=null){
            baseMapper.updateById(userVip);
        }else{
            baseMapper.insert(userVip);
        }
        userEntity.setEndTime(userVip.getEndTime());
        userService.updateById(userEntity);
        return Result.success();
    }



    @Scheduled(fixedDelay = 60 * 1000)
    public void closeUserVip(){
        baseMapper.updateUserVip();
        baseMapper.updateUserMember();
    }

}
