package com.sqx.modules.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.modules.goods.entity.GoodsAttr;

import java.util.List;

public interface GoodsAttrService extends IService<GoodsAttr> {

    int updateGoodsAttr(GoodsAttr goodsAttr);

    void deleteAttrAndValue(Long goodsId);

    int deleteGoodsAttr(Long goodsId);

    int saveGoodsAttr(GoodsAttr goodsAttr);

    List<GoodsAttr> findByGoodsId(Long goodsId, String languageType);

    List<GoodsAttr> findAllByGoodsId(Long goodsId);

    List<GoodsAttr> listByGoodsId(Long goodsId, String languageType);
}