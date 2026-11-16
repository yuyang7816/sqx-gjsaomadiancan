package com.sqx.modules.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.pay.dao.PayDetailsDao;
import com.sqx.modules.pay.entity.PayDetails;
import com.sqx.modules.pay.service.PayDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 充值记录
 */
@Service
public class PayDetailsServiceImpl extends ServiceImpl<PayDetailsDao, PayDetails> implements PayDetailsService {

    /**
     * 充值记录
     */
    @Autowired
    private PayDetailsDao payDetailsDao;


    @Override
    public PageUtils selectPayDetails(int page, int limit, String startTime, String endTime, Long userId, Integer state) {
        Page<Map<String, Object>> pages = new Page<>(page, limit);
        if (state != null && state == -1) {
            state = null;
        }
        return new PageUtils(payDetailsDao.selectPayDetails(pages, startTime, endTime, userId, state));
    }

    @Override
    public Double selectSumPay(String createTime, String endTime, Long userId) {
        if (userId == null || userId == -1) {
            return payDetailsDao.selectSumPay(createTime, endTime, null);
        }
        return payDetailsDao.selectSumPay(createTime, endTime, userId);
    }

    @Override
    public PageUtils payMemberAnalysis(int page, int limit, String time, Integer flag) {
        Page<Map<String, Object>> pages = new Page<>(page, limit);
        return new PageUtils(payDetailsDao.payMemberAnalysis(pages, time, flag));
    }

    @Override
    public PageUtils selectUserMemberList(int page, int limit, String phone) {
        Page<Map<String, Object>> pages = new Page<>(page, limit);
        return new PageUtils(payDetailsDao.selectUserMemberList(pages, phone));
    }

    @Override
    public Double selectSumMember(String time, Integer flag) {
        return payDetailsDao.selectSumMember(time, flag);
    }

    @Override
    public Double selectSumPayByState(String time, Integer flag, Integer state) {
        return payDetailsDao.selectSumPayByState(time, flag, state);
    }

    @Override
    public Double selectSumPayByClassify(String time, Integer flag, Integer classify,Integer payClassify) {
        return payDetailsDao.selectSumPayByClassify(time, flag, classify,payClassify);
    }

    @Override
    public Double instantselectSumPay(String date, Long userId) {
        return payDetailsDao.instantselectSumPay(date, userId);
    }

    @Override
    public Result selectPayDetailsById(Integer page, Integer limit, Long userId, String startTime, String endTime) {
        Page<PayDetails> pages=new Page<>(page,limit);
        PageUtils pageUtils = new PageUtils(payDetailsDao.selectPayDetailsById(pages, userId, startTime, endTime));
        return Result.success().put("data", pageUtils);
    }

    @Override
    public PayDetails queryByOrderNum(String orderNum) {
        PayDetails payDetails = payDetailsDao.selectByOrderId(orderNum);
        return payDetails;
    }

    @Override
    public int updateInfo(Long payDetailsId, Integer status, String updateTime) {
        return baseMapper.updateState(payDetailsId,status,updateTime,null);
    }

    @Override
    public PayDetails queryByStripeId(String stripeId) {
        PayDetails payDetails = baseMapper.selectOne(new QueryWrapper<PayDetails>().eq("stripe_id", stripeId));
        return payDetails;
    }
}
