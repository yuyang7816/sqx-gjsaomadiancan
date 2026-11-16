package com.sqx.modules.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.dao.*;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.entity.UserMoney;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.utils.DataSourceMessageSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class UserMoneyServiceImpl extends ServiceImpl<UserMoneyDao, UserMoney> implements UserMoneyService {
    @Autowired
    private UserMoneyDetailsDao userMoneyDetailsDao;
    @Autowired
    private UserVipDao userVipDao;
    @Autowired
    private VipDetailsDao vipDetailsDao;
    @Autowired
    private UserDao userDao;

    @Autowired
    private UserMoneyDao userMoneyDao;
    @Autowired
    private DataSourceMessageSource dataSourceMessageSource;

    @Override
    public void updateMoney(int i, Long userId, double money) {
        baseMapper.updateMayMoney(i, userId, money);
    }

    @Override
    public UserMoney selectUserMoneyByUserId(Long userId) {
        return baseMapper.selectOne(new QueryWrapper<UserMoney>().eq("user_id", userId));
    }

    @Override
    public Double selectMyProfit(Long userId) {

        return userMoneyDetailsDao.selectMyProfit(userId);
    }

    @Override
    public Result balanceDetailed(Long userId, Integer page, Integer limit, String languageType) {
        //余额明细
        Page<UserMoneyDetails> pages = new Page<>(page, limit);
        PageUtils pageUtils = new PageUtils(userMoneyDetailsDao.selectUserMoneyDetails(pages, userId, languageType));
        return Result.success().put("data",pageUtils);
    }

    @Override
    public Result profitDetailed(Long userId, IPage ipage) {
        //收益明细
        return null;
    }

    @Transactional
    @Override
    public Result addUserMoney(Long userId, BigDecimal money, Integer type) {
        userMoneyDao.addUserMoney(userId, money, type);
        UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
        userMoneyDetails.setUserId(userId);
        if(type==1){
            userMoneyDetails.setTitle(dataSourceMessageSource.getConfigI18ForCach("60130"));
            userMoneyDetails.setContent(dataSourceMessageSource.getConfigI18ForCach("60131", String.valueOf(money)));
        }else{
            userMoneyDetails.setTitle(dataSourceMessageSource.getConfigI18ForCach("60132"));
            userMoneyDetails.setContent(dataSourceMessageSource.getConfigI18ForCach("60133", String.valueOf(money)));
        }
        userMoneyDetails.setType(type);
        userMoneyDetails.setMoney(money);
        userMoneyDetails.setCreateTime(DateUtils.format(new Date()));
        userMoneyDetailsDao.insert(userMoneyDetails);
        return Result.success();
    }


    /**
     * 开通成功会员后  奖励金币
     *
     * @param userId
     * @param type
     */
    public void addMoney(Long userId, Integer type) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //是否填写过邀请码
        UserEntity userEntity = userDao.selectById(userId);
        if (userEntity.getInviterCode() != null) {
            //查询填写邀请码的所属人
            UserEntity user = userDao.selectOne(new QueryWrapper<UserEntity>().eq("", userEntity.getInviterCode()));
            //创建钱包详情模板
            UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
            //设置收益人
            userMoneyDetails.setUserId(user.getUserId());
            //设置购买人
     //       userMoneyDetails.setByUserId(userId);
            //设置类型
     //       userMoneyDetails.setClassify(2);
            //设置title
            userMoneyDetails.setTitle(dataSourceMessageSource.getConfigI18ForCach("60134"));
            //设置创建时间
            userMoneyDetails.setCreateTime(simpleDateFormat.format(new Date()));
            //填写过邀请码
            if (type.equals(0)) {
                //月奖励1金币
                //设置奖励金币
                userMoneyDetails.setMoney(BigDecimal.valueOf(1));
                //设置内容
                userMoneyDetails.setContent(dataSourceMessageSource.getConfigI18ForCach("60135"));
            } else if (type.equals(1)) {
                //季奖励3金币
                //设置奖励金币
                userMoneyDetails.setMoney(BigDecimal.valueOf(3));
                //设置内容
                userMoneyDetails.setContent(dataSourceMessageSource.getConfigI18ForCach("60135"));
            } else {
                //年奖励10金币
                //设置奖励金币
                userMoneyDetails.setMoney(BigDecimal.valueOf(10));
                //设置内容
                userMoneyDetails.setContent(dataSourceMessageSource.getConfigI18ForCach("60135"));
            }
            userMoneyDetailsDao.insert(userMoneyDetails);
            //将奖励的金币添加到受益人的钱包里
            UserMoney userMoney = baseMapper.selectOne(new QueryWrapper<UserMoney>().eq("user_id", user.getUserId()));
            userMoney.setMoney(userMoney.getMoney().subtract(userMoneyDetails.getMoney()));
            baseMapper.updateById(userMoney);
            log.info("奖励成功！");
        } else {
            log.error("没有填写过邀请码 无法奖励金币");
        }
    }
}
