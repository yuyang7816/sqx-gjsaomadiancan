package com.sqx.modules.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.modules.goods.entity.GoodsAttrValue;

import java.util.List;

public interface GoodsAttrValueService extends IService<GoodsAttrValue> {

    List<GoodsAttrValue> findAllByGoodsId(Long goodsId, String languageType);

    int deleteGoodsAttrValueByGoodsId(Long goodsId);

    List<GoodsAttrValue> listByAttrIds(List<Long> attrIdList);
}