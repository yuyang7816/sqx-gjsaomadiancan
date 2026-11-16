package com.sqx.modules.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.goods.entity.GoodsShopRelevancy;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GoodsShopRelevancyDao extends BaseMapper<GoodsShopRelevancy> {

    int selectGoodsCount(@Param("orderId") Long orderId1,@Param("goodsId") Long goodsId);

    // GoodsShopRelevancy selectInventory(Long shopId, Long goodsId);

}
