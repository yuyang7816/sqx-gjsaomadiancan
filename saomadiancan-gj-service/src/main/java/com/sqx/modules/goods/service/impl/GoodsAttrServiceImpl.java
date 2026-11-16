package com.sqx.modules.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.modules.goods.dao.GoodsAttrDao;
import com.sqx.modules.goods.entity.GoodsAttr;
import com.sqx.modules.goods.entity.GoodsAttrValue;
import com.sqx.modules.goods.i18n.attr.db.service.IGoodsAttrI18nService;
import com.sqx.modules.goods.i18n.attr.domain.entity.GoodsAttrI18n;
import com.sqx.modules.goods.i18n.attrValue.db.service.IGoodsAttrValueI18nService;
import com.sqx.modules.goods.i18n.attrValue.domain.entity.GoodsAttrValueI18n;
import com.sqx.modules.goods.service.GoodsAttrService;
import com.sqx.modules.goods.service.GoodsAttrValueService;
import com.sqx.modules.languageType.entity.LanguageType;
import com.sqx.modules.languageType.service.LanguageTypeService;
import com.sqx.modules.utils.translate.TransApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GoodsAttrServiceImpl extends ServiceImpl<GoodsAttrDao, GoodsAttr> implements GoodsAttrService {

    @Autowired
    private GoodsAttrValueService goodsAttrValueService;
    @Autowired
    private LanguageTypeService languageTypeService;
    @Autowired
    private IGoodsAttrI18nService iGoodsAttrI18nService;
    @Autowired
    private IGoodsAttrValueI18nService iGoodsAttrValueI18nService;

    @Override
    public int updateGoodsAttr(GoodsAttr goodsAttr) {
        //删除之前的值
        deleteAttrValueI18n(goodsAttr.getGoodsId());
        goodsAttrValueService.deleteGoodsAttrValueByGoodsId(goodsAttr.getGoodsId());

        //放入新值
//        for (GoodsAttrValue v : goodsAttr.getAttrValue()) {
//            v.setGoodsId(goodsAttr.getGoodsId());
//            v.setAttrId(goodsAttr.getId());
//            goodsAttrValueService.save(v);
//
//            GoodsAttrValueI18n goodsAttrValueI18n = new GoodsAttrValueI18n();
//            goodsAttrValueI18n.setAttrValueId(v.getId());
//            goodsAttrValueI18n.setValue(v.getValue());
//            String deleteWhitespace = StringUtils.deleteWhitespace(v.getDetail());
//            goodsAttrValueI18n.setDetail(deleteWhitespace);
//            goodsAttrValueI18n.setLanguageType(goodsAttr.getLanguageType());
//            goodsAttrValueI18n.setLanguageName(languageTypeService.getNameByType(goodsAttr.getLanguageType()));
//            iGoodsAttrValueI18nService.save(goodsAttrValueI18n);
//
//            saveAttrValueI18n(v);
//        }

        deleteAttrI18n(goodsAttr.getGoodsId());
        deleteGoodsAttr(goodsAttr.getGoodsId());

//        baseMapper.insert(goodsAttr);
        saveGoodsAttr(goodsAttr);
        return 1;
    }

    private void deleteAttrValueI18n(Long goodsId) {
        LambdaQueryWrapper<GoodsAttrValue> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GoodsAttrValue::getGoodsId, goodsId);

        List<GoodsAttrValue> attrValues = goodsAttrValueService.list(queryWrapper);
        if (CollectionUtils.isEmpty(attrValues)) {
            return;
        }

        List<Long> attrValueIds = attrValues.stream()
                .map(GoodsAttrValue::getAttrId)
                .collect(Collectors.toList());

        iGoodsAttrValueI18nService.remove(new LambdaQueryWrapper<GoodsAttrValueI18n>().in(GoodsAttrValueI18n::getAttrValueId, attrValueIds));
    }

    @Override
    public void deleteAttrAndValue(Long goodsId){
        deleteAttrValueI18n(goodsId);
        goodsAttrValueService.deleteGoodsAttrValueByGoodsId(goodsId);

        deleteAttrI18n(goodsId);
        deleteGoodsAttr(goodsId);
    }

    private void deleteAttrI18n(Long goodsId) {
        LambdaQueryWrapper<GoodsAttr> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GoodsAttr::getGoodsId, goodsId);
        List<GoodsAttr> goodsAttrs = baseMapper.selectList(queryWrapper);

        List<Long> goodsAttrIds = goodsAttrs.stream().map(GoodsAttr::getId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(goodsAttrIds)) {
            return;
        }

        iGoodsAttrI18nService.remove(new LambdaQueryWrapper<GoodsAttrI18n>().in(GoodsAttrI18n::getAttrId, goodsAttrIds));
    }

    @Override
    public int deleteGoodsAttr(Long goodsId){
        return baseMapper.delete(new QueryWrapper<GoodsAttr>().eq("goods_id",goodsId));
    }

    @Override
    public List<GoodsAttr> findAllByGoodsId(Long goodsId){
        return baseMapper.selectList(new QueryWrapper<GoodsAttr>().eq("goods_id",goodsId));
    }

    @Override
    public List<GoodsAttr> listByGoodsId(Long goodsId, String languageType) {
        return baseMapper.listByGoodsId(goodsId, languageType);
    }

    @Override
    public int saveGoodsAttr(GoodsAttr goodsAttr) {
//       baseMapper.insertGoodsAttr(goodsAttr);
//        List<GoodsAttrValue> list = goodsAttr.getAttrValue();
//        for (GoodsAttrValue goodsAttrValue : list) {
//            goodsAttrValue.setId(null);
//            goodsAttrValue.setGoodsId(goodsAttr.getGoodsId());
//            goodsAttrValue.setAttrId(goodsAttr.getId());
//            goodsAttrValueService.save(goodsAttrValue);
//        }
//
//        return 1;

        // 重构逻辑 2025/09/19

        // 保存当前语言
        this.save(goodsAttr);

        // 保存其他语言
        GoodsAttrServiceImpl currentProxy = (GoodsAttrServiceImpl) AopContext.currentProxy();
        saveGoodsAttrI18n(goodsAttr);

        // 处理规格值
        List<GoodsAttrValue> attrValueList = goodsAttr.getAttrValue();
        for (GoodsAttrValue attrValue : attrValueList) {
            attrValue.setGoodsId(goodsAttr.getGoodsId());
            attrValue.setAttrId(goodsAttr.getId());
            attrValue.setLanguageType(goodsAttr.getLanguageType());

            goodsAttrValueService.save(attrValue);

            // 保存当前语言的规格
            GoodsAttrValueI18n goodsAttrValueI18n = new GoodsAttrValueI18n();
            goodsAttrValueI18n.setAttrValueId(attrValue.getId());
            goodsAttrValueI18n.setValue(attrValue.getValue());
            goodsAttrValueI18n.setDetail(attrValue.getDetail());
            goodsAttrValueI18n.setLanguageType(attrValue.getLanguageType());
            goodsAttrValueI18n.setLanguageName(languageTypeService.getNameByType(attrValue.getLanguageType()));
            iGoodsAttrValueI18nService.save(goodsAttrValueI18n);

            saveAttrValueI18n(attrValue);
        }

        return 1;
    }

    @Async
    public void saveAttrValueI18n(GoodsAttrValue attrValue) {
        List<LanguageType> enableLanguageTypeList = languageTypeService.getEnableLanguageTypeList();
        enableLanguageTypeList = enableLanguageTypeList.stream()
                .filter(item -> !Objects.equals(attrValue.getLanguageType(), item.getLanguage()))
                .collect(Collectors.toList());

        String value = attrValue.getValue();
        String detail = attrValue.getDetail();

        List<GoodsAttrValueI18n> goodsAttrValueI18nList = new ArrayList<>();
        for (LanguageType languageType : enableLanguageTypeList) {
            StringBuilder stringBuilder=new StringBuilder();
            String[] split = detail.split(",");
            for (String str:split){
                String details = null;
                try {
                    details = TransApi.getTransResult(str, attrValue.getLanguageType(), languageType.getLanguage());
                    Thread.sleep(1000);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                stringBuilder.append(details).append(",");
            }

            String tranValue = null;
            try {
                tranValue = TransApi.getTransResult(value, attrValue.getLanguageType(), languageType.getLanguage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            GoodsAttrValueI18n goodsAttrValueI18n = new GoodsAttrValueI18n();
            goodsAttrValueI18n.setAttrValueId(attrValue.getId());
            goodsAttrValueI18n.setValue(tranValue);
            String deleteWhitespace = StringUtils.deleteWhitespace(stringBuilder.substring(0,stringBuilder.length()-1));
            goodsAttrValueI18n.setDetail(deleteWhitespace);
            goodsAttrValueI18n.setLanguageType(languageType.getLanguage());
            goodsAttrValueI18n.setLanguageName(languageType.getRemark());

            goodsAttrValueI18nList.add(goodsAttrValueI18n);
        }

        iGoodsAttrValueI18nService.saveBatch(goodsAttrValueI18nList);
    }

    @Async
    public void saveGoodsAttrI18n(GoodsAttr goodsAttr) {
        GoodsAttrI18n attrI18n = new GoodsAttrI18n();
        attrI18n.setAttrId(goodsAttr.getId());
        attrI18n.setAttrName(goodsAttr.getAttrName());
        attrI18n.setLanguageType(goodsAttr.getLanguageType());
        attrI18n.setLanguageName(languageTypeService.getNameByType(goodsAttr.getLanguageType()));
        iGoodsAttrI18nService.save(attrI18n);

        List<LanguageType> enableLanguageTypeList = languageTypeService.getEnableLanguageTypeList();
        enableLanguageTypeList = enableLanguageTypeList.stream()
                .filter(item -> !Objects.equals(goodsAttr.getLanguageType(), item.getLanguage()))
                .collect(Collectors.toList());

        String attrName = goodsAttr.getAttrName();

        List<GoodsAttrI18n> goodsAttrI18nList = new ArrayList<>();
        for (LanguageType languageType : enableLanguageTypeList) {

            String tranAttrName = null;
            try {
                tranAttrName = TransApi.getTransResult(attrName, goodsAttr.getLanguageType(), languageType.getLanguage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            GoodsAttrI18n goodsAttrI18n = new GoodsAttrI18n();
            goodsAttrI18n.setAttrId(goodsAttr.getId());
            goodsAttrI18n.setAttrName(tranAttrName);
            goodsAttrI18n.setLanguageType(languageType.getLanguage());
            goodsAttrI18n.setLanguageName(languageType.getRemark());

            goodsAttrI18nList.add(goodsAttrI18n);
        }

        iGoodsAttrI18nService.saveBatch(goodsAttrI18nList);
    }

    @Override
    public List<GoodsAttr> findByGoodsId(Long goodsId, String languageType) {
        List<GoodsAttr> list = listByGoodsId(goodsId, languageType);
        for (GoodsAttr goodsAttr : list) {
            List<GoodsAttrValue> valueList = goodsAttrValueService.findAllByGoodsId(goodsAttr.getGoodsId(), languageType);
            goodsAttr.setAttrValue(valueList);
        }
        return list;
    }


}