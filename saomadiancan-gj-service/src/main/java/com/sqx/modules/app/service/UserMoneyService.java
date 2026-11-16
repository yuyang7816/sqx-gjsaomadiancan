package com.sqx.modules.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserMoney;
import org.springframework.web.bind.annotation.RequestAttribute;

import java.math.BigDecimal;

public interface UserMoneyService extends IService<UserMoney> {

    UserMoney selectUserMoneyByUserId(Long userId);

    void updateMoney(int i, Long userId, double money);

    Double selectMyProfit(Long userId);

    Result balanceDetailed(@RequestAttribute Long userId, Integer page , Integer limit, String languageType);

    Result profitDetailed(@RequestAttribute Long userId, IPage ipage);

    Result addUserMoney(Long userId, BigDecimal money, Integer type);

}
