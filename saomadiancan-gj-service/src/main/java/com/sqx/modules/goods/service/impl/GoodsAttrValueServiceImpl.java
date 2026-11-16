package com.sqx.modules.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.modules.goods.dao.GoodsAttrValueDao;
import com.sqx.modules.goods.entity.GoodsAttrValue;
import com.sqx.modules.goods.service.GoodsAttrValueService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsAttrValueServiceImpl extends ServiceImpl<GoodsAttrValueDao, GoodsAttrValue> implements GoodsAttrValueService {


    @Override
    public List<GoodsAttrValue> findAllByGoodsId(Long goodsId, String languageType) {
        return baseMapper.findAllByGoodsId(goodsId, languageType);
    }


    @Override
    public int deleteGoodsAttrValueByGoodsId(Long goodsId){
        return baseMapper.delete(new QueryWrapper<GoodsAttrValue>().eq("goods_id",goodsId));
    }

    @Override
    public List<GoodsAttrValue> listByAttrIds(List<Long> attrIdList) {
        return baseMapper.listByAttrIds(attrIdList);
    }

}