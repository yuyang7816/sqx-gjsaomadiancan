package com.sqx.modules.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.goods.entity.GoodsRuleValue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoodsRuleValueMapper extends BaseMapper<GoodsRuleValue> {


    List<GoodsRuleValue> list(@Param("ruleId") Long ruleId, @Param("languageType") String languageType);
}