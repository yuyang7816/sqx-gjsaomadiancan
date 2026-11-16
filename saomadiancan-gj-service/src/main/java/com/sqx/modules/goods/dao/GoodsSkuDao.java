package com.sqx.modules.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.goods.entity.GoodsSku;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoodsSkuDao extends BaseMapper<GoodsSku> {

    int insertGoodsSku(GoodsSku goodsSku);

    List<GoodsSku> selectGoodsSkuByGoodsId(@Param("goodsId") Long goodsId, @Param("languageType") String languageType);
}
