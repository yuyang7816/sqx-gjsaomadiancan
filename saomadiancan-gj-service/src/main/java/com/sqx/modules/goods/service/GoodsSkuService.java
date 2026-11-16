package com.sqx.modules.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.goods.entity.GoodsSku;

import java.util.List;

public interface GoodsSkuService extends IService<GoodsSku> {

    Result insertSku(GoodsSku goodsSku);

    int deleteGoodsSkuByGoodsId(Long goodsId);

    List<GoodsSku> selectGoodsSkuByGoodsId(Long goodsId, String languageType);



}
