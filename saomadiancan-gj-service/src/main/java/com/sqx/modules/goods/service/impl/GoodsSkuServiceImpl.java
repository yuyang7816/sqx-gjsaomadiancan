package com.sqx.modules.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.goods.dao.GoodsSkuDao;
import com.sqx.modules.goods.entity.GoodsSku;
import com.sqx.modules.goods.i18n.sku.db.service.IGoodsSkuI18nService;
import com.sqx.modules.goods.i18n.sku.domain.entity.GoodsSkuI18n;
import com.sqx.modules.goods.service.GoodsSkuService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsSkuServiceImpl extends ServiceImpl<GoodsSkuDao, GoodsSku> implements GoodsSkuService {

    @Autowired
    private GoodsSkuDao goodsSkuDao;
    @Autowired
    private IGoodsSkuI18nService iGoodsSkuI18nService;

    @Override
    public Result insertSku(GoodsSku goodsSku) {
        baseMapper.insert(goodsSku);
        return Result.success();
    }

    @Override
    public List<GoodsSku> selectGoodsSkuByGoodsId(Long goodsId, String languageType) {
        return baseMapper.selectGoodsSkuByGoodsId(goodsId, languageType);
    }

    @Override
    public int deleteGoodsSkuByGoodsId(Long goodsId){
        LambdaQueryWrapper<GoodsSku> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GoodsSku::getGoodsId, goodsId);

        List<GoodsSku> goodsSkus = baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(goodsSkus)){
            List<Long> goodsSkuIds = goodsSkus.stream().map(GoodsSku::getId).collect(Collectors.toList());

            iGoodsSkuI18nService.remove(new LambdaQueryWrapper<GoodsSkuI18n>().in(GoodsSkuI18n::getSkuId, goodsSkuIds));
        }

        return baseMapper.delete(queryWrapper);
    }


}
