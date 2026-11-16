package com.sqx.modules.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserMoneyDetails;

import java.math.BigDecimal;

public interface UserMoneyDetailsService extends IService<UserMoneyDetails> {
    Result queryUserMoneyDetails(Integer page, Integer limit, Long userId, Integer classify, Integer type, Long shopId,String startTime,String endTime);
    Double monthIncome(String date,Long userId);

    BigDecimal getShopStatistics(Long shopId, String startTime, String endTime);

    void saveI18n(UserMoneyDetails userMoneyDetails, String titleCode, String[] titleParam, String contentCode, String[] contentParam);

}
