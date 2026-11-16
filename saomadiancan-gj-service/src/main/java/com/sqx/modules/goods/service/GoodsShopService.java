package com.sqx.modules.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.goods.entity.GoodsShop;
import com.sqx.modules.goods.entity.GoodsShopRelevancy;

import java.util.List;

public interface GoodsShopService extends IService<GoodsShop> {

    Result insertGoodsShop(GoodsShop goodsShop);

    Result updateGoodsShop(GoodsShop goodsShop);

    Result deleteGoodsShop(Long id);

    Result selectGoodsByShopId(Long shopId, Integer page, Integer limit, String goodsName, Integer classifyId, Integer status, String languageType);

    Result selectGoodsByNotShopId(Long shopId, Integer page, Integer limit, String goodsName);

    Result addGoodsByShopId(List<GoodsShopRelevancy> goodsShopRelevancyList);

    Result deleteGoodsByShopId(Long shopId, Long goodsId);

    Result soldOutOrPutaway(Long shopId, Long goodsId, Integer status);

    Result updateGoodsByShopId(GoodsShopRelevancy goodsShopRelevancy);

    Result selectStoreData(Long shopId, String date, String dateType);

    Result selectStoreMessage(Long shopId, String date, String dateType, Integer page, Integer limit, String orderNumber,Integer status);

    Result selectGoodsByShopIdAndGoodsId(Long shopId, Long goodsId, String languageType);

}
