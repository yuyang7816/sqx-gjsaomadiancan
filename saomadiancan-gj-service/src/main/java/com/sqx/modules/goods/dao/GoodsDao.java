package com.sqx.modules.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.goods.entity.Goods;
import com.sqx.modules.goods.entity.GoodsClassify;
import com.sqx.modules.goods.entity.GoodsShop;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoodsDao extends BaseMapper<Goods> {

    List<Goods> selectGoodsClassifyList(@Param("shopId") Long shopId);

    GoodsClassify selectClassify(@Param("classifyId") Integer classifyId, @Param("shopId") Long shopId, @Param("languageType") String language);

    IPage<GoodsShop> selectShortShop(Page<GoodsShop> pages, @Param("lng") double lng, @Param("lat") double lat, @Param("shopName") String shopName, @Param("shopId") Long shopId);

    List<GoodsClassify> selectAllClassify(@Param("shopId") Long shopId, @Param("languageType") String languageType);

    int shopAddGoods(@Param("shopId") Long shopId,@Param("goodsIdList") List goodsIdList);

    List<Goods> selectGoodsByClassify(@Param("classifyId") Integer classifyId, @Param("shopId") Long shopId, @Param("languageType") String languageType);

    Goods getById(@Param("goodsId") Long goodsId, @Param("languageType") String languageType);
}
