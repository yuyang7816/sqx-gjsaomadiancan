package com.sqx.modules.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.goods.entity.Goods;
import com.sqx.modules.goods.entity.GoodsAttr;

import java.util.List;

public interface GoodsService extends IService<Goods> {

    Result insertGoods(Goods goods);

    Result selectGoodsById(Long goodsId, Long shopId, String languageType);

    Result selectGoodsList(Long shopId, String language);

    Result updateGoods(Goods goods);

    Result isFormatAttr(GoodsAttr goodsAttr, String coverImg, String price);

    Result onlyFormatAttr(String coverImg,String price);

    Result formatAttr(Long goodsId, String languageType);

    Result findAttrValue(Long goodsId, String languageType);

    Result selectShortShop(double lng, double lat, String shopName, Integer page, Integer limit, Long shopId);

    Result selectAllShop(Integer page, Integer limit, String shopName, Long userId);

    Result selectAdminGoodsList(Integer page, Integer limit, Integer classifyId, String goodsName, Integer status, String languageType);

    Result selectAllClassify(Long shopId, String languageType);

    Result shopAddGoods(Long shopId, List goodsIdList);

    Result deleteGoodsById(Long goodsId);

    Result selectAllShopList(Long userId);

}
