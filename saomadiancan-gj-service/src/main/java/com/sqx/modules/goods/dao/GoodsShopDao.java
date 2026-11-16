package com.sqx.modules.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.goods.entity.Goods;
import com.sqx.modules.goods.entity.GoodsShop;
import com.sqx.modules.goods.entity.GoodsShopRelevancy;
import com.sqx.modules.order.entity.TbOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface GoodsShopDao extends BaseMapper<GoodsShop> {

    IPage<GoodsShop> selectAllShop(Page<GoodsShop> pages,@Param("shopName") String shopName);

    IPage<GoodsShop> selectAllShopByAdmin(Page<GoodsShop> pages,@Param("shopName") String shopName,@Param("userId") Long userId);

    IPage<Goods> selectAdminGoodsList(Page<Goods> pages,@Param("classifyId") Integer classifyId,
                                      @Param("goodsName") String goodsName,@Param("status") Integer status, @Param("languageType") String languageType);

    IPage<Goods> selectGoodsByShopId(Page<Goods> pages, @Param("shopId") Long shopId, @Param("goodsName") String goodsName,
                                     @Param("classifyId") Integer classifyId, @Param("status") Integer status, @Param("languageType") String languageType);

    Goods selectGoodsByShopIdAndGoodsId(Long shopId, Long goodsId);

    IPage<Goods> selectGoodsByNotShopId(Page<Goods> pages,@Param("shopId") Long shopId, @Param("goodsName") String goodsName);

    int addGoodsByShopId(@Param("goodsShopRelevancyList") List<GoodsShopRelevancy> goodsShopRelevancyList, @Param("date") String date);

    int deleteGoodsByShopId(@Param("shopId") Long shopId, @Param("goodsId") Long goodsId);

    int soldOutOrPutaway(@Param("shopId") Long shopId, @Param("goodsId") Long goodsId,@Param("status") Integer status);

    List<GoodsShop> selectListByAdmin(Long userId);

    int selectStoreCount(@Param("shopId") Long shopId,@Param("date") String date,@Param("dateType") String dateType);

    BigDecimal selectStoreMoney(@Param("shopId") Long shopId,@Param("date") String date, @Param("dateType") String dateType);

    IPage<TbOrder> selectStoreMessage(Page<TbOrder> pages, Long shopId, String date, String dateType, String orderNumber,Integer status);

    int selectTakeCount(Integer orderType, String date, String dateType, Long shopId);

    BigDecimal selectTakeMoney(Integer orderType, String date, String dateType, Long shopId);

    int cancelOrderCount(String date, String dateType, Long shopId);

    BigDecimal cancelOrderMoney(String date, String dateType, Long shopId);


}
