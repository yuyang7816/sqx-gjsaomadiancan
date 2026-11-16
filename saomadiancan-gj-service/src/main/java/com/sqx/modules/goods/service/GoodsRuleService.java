package com.sqx.modules.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.goods.entity.GoodsRule;

public interface GoodsRuleService extends IService<GoodsRule> {

    Result findAll(Integer page, Integer limit, GoodsRule goodsRule);

    Result info();

    Result saveBody(GoodsRule goodsRule);

    Result updateBody(GoodsRule goodsRule);

    Result findOne(Long id, String languageType);

    Result delete(Long id);

}