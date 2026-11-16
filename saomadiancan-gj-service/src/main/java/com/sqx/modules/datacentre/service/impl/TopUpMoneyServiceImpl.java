package com.sqx.modules.datacentre.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.datacentre.dao.TopUpMoneyDao;
import com.sqx.modules.datacentre.entity.TopUpMoney;
import com.sqx.modules.datacentre.service.TopUpMoneyService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopUpMoneyServiceImpl extends ServiceImpl<TopUpMoneyDao, TopUpMoney> implements TopUpMoneyService {


    @Override
    public Result selectTopUpMoney() {
        List<TopUpMoney> topUpMonies = baseMapper.selectList(null);
        return Result.success().put("data", topUpMonies);
    }

    @Override
    public Result insertTopUpMoney(TopUpMoney topUpMoney) {
        baseMapper.insert(topUpMoney);
        return Result.success();
    }

    @Override
    public Result updateTopUpMoney(TopUpMoney topUpMoney) {
        baseMapper.updateById(topUpMoney);
        return Result.success();
    }

    @Override
    public Result deleteTopUpMoney(Long id) {
        baseMapper.deleteById(id);
        return Result.success();
    }
}
