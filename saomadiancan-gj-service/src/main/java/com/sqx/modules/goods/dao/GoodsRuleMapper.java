package com.sqx.modules.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.goods.entity.GoodsRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoodsRuleMapper extends BaseMapper<GoodsRule> {

    int insertGoodsRule(GoodsRule goodsRule);

    List<GoodsRule> list(@Param("param") GoodsRule goodsRule);
}