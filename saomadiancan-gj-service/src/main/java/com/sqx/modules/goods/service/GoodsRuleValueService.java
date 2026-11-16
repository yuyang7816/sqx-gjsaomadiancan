package com.sqx.modules.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.modules.goods.entity.GoodsRuleValue;

import java.util.List;

public interface GoodsRuleValueService extends IService<GoodsRuleValue> {

    List<GoodsRuleValue> selectGoodsRuleValue(Long ruleId, String languageType);

    int deleteGoodsRuleValue( Long ruleId);

}