package com.sqx.modules.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.goods.entity.GoodsAttr;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoodsAttrDao extends BaseMapper<GoodsAttr> {

    int insertGoodsAttr(GoodsAttr goodsAttr);

    List<GoodsAttr> listByGoodsId(@Param("goodsId") Long goodsId, @Param("languageType") String languageType);

    List<GoodsAttr> getByGoodsId(@Param("goodsId") Long goodsId, @Param("languageType") String languageType);
}