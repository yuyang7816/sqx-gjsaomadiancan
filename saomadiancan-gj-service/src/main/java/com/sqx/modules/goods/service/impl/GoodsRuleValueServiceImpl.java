package com.sqx.modules.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.modules.goods.dao.GoodsRuleValueMapper;
import com.sqx.modules.goods.entity.GoodsRuleValue;
import com.sqx.modules.goods.i18n.ruleValue.db.service.IGoodsRuleValueI18nService;
import com.sqx.modules.goods.i18n.ruleValue.domain.entity.GoodsRuleValueI18n;
import com.sqx.modules.goods.service.GoodsRuleValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class GoodsRuleValueServiceImpl extends ServiceImpl<GoodsRuleValueMapper, GoodsRuleValue> implements GoodsRuleValueService {

    @Autowired
    private IGoodsRuleValueI18nService iGoodsRuleValueI18nService;

    @Override
    public List<GoodsRuleValue> selectGoodsRuleValue( Long ruleId, String languageType) {
        return baseMapper.list(ruleId, languageType);
    }

    @Override
    public int deleteGoodsRuleValue( Long ruleId){
        LambdaQueryWrapper<GoodsRuleValue> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GoodsRuleValue::getRuleId, ruleId);

        List<GoodsRuleValue> goodsRuleValueList = baseMapper.selectList(queryWrapper);
        List<Long> goodsRuleValueIdList = goodsRuleValueList.stream().map(GoodsRuleValue::getId).collect(Collectors.toList());

        iGoodsRuleValueI18nService.remove(new LambdaQueryWrapper<GoodsRuleValueI18n>().in(GoodsRuleValueI18n::getRuleValueId, goodsRuleValueIdList));

        return baseMapper.delete(queryWrapper);
    }

}