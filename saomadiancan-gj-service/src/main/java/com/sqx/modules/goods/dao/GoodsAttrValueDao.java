package com.sqx.modules.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.goods.entity.GoodsAttrValue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoodsAttrValueDao extends BaseMapper<GoodsAttrValue> {


    List<GoodsAttrValue> listByAttrIds(@Param("attrIdList") List<Long> attrIdList);

    List<GoodsAttrValue> findAllByGoodsId(@Param("goodsId") Long goodsId, @Param("languageType") String languageType);
}