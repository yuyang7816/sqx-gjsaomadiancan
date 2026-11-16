package com.sqx.modules.datacentre.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.datacentre.entity.TopUpMoney;

public interface TopUpMoneyService extends IService<TopUpMoney> {

    Result selectTopUpMoney();

    Result insertTopUpMoney(TopUpMoney topUpMoney);

    Result updateTopUpMoney(TopUpMoney topUpMoney);

    Result deleteTopUpMoney(Long id);
}
