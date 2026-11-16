package com.sqx.modules.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.goods.dao.GoodsClassifyDao;
import com.sqx.modules.goods.dao.GoodsDao;
import com.sqx.modules.goods.entity.Goods;
import com.sqx.modules.goods.entity.GoodsClassify;
import com.sqx.modules.goods.entity.GoodsClassifyI18n;
import com.sqx.modules.goods.service.GoodsClassifyI18nService;
import com.sqx.modules.goods.service.GoodsClassifyService;
import com.sqx.modules.languageType.entity.LanguageType;
import com.sqx.modules.languageType.service.LanguageTypeService;
import com.sqx.modules.utils.translate.TransApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class GoodsClassifyServiceImpl extends ServiceImpl<GoodsClassifyDao, GoodsClassify> implements GoodsClassifyService {

    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private GoodsClassifyI18nService goodsClassifyI18nService;
    @Autowired
    private LanguageTypeService languageTypeService;

    @Override
    public Result insertClassify(GoodsClassify goodsClassify) {
        // 校验是否已有分类（系统分类和本店铺分类）
        if (checkExistByName(goodsClassify.getShopId(), goodsClassify.getClassifyName(), goodsClassify.getLanguageType())) {
            return Result.error("60194");   // 商品分类已存在
        }

        int insert = baseMapper.insert(goodsClassify);

        // 保存当前语言
        GoodsClassifyI18n goodsClassifyI18n = new GoodsClassifyI18n();
        goodsClassifyI18n.setClassifyId(goodsClassify.getClassifyId());
        goodsClassifyI18n.setClassifyName(goodsClassify.getClassifyName());
        goodsClassifyI18n.setLanguageType(goodsClassify.getLanguageType());
        String languageName = getLanguageNameByType(goodsClassify.getLanguageType());
        goodsClassifyI18n.setLanguageName(languageName);
        goodsClassifyI18nService.save(goodsClassifyI18n);

        // 翻译其他语言
        asyncSaveGoodsClassifyI18n(goodsClassify);
        return Result.success();
    }

    private boolean checkExistByName(Long shopId, String classifyName, String languageType) {
        int count =  baseMapper.countByName(shopId, classifyName, languageType);
        return count > 0;
    }

    //@Async
    public void asyncSaveGoodsClassifyI18n(GoodsClassify goodsClassify) {
        List<LanguageType> enableLanguageTypeList = languageTypeService.getEnableLanguageTypeList();
        enableLanguageTypeList.removeIf(item -> Objects.equals(goodsClassify.getLanguageType(), item.getLanguage()));
        for (LanguageType languageType : enableLanguageTypeList) {
            String classifyName = null;
            try {
                classifyName = TransApi.getTransResult(goodsClassify.getClassifyName(), goodsClassify.getLanguageType(), languageType.getLanguage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            GoodsClassifyI18n goodsClassifyI18n = new GoodsClassifyI18n();
            goodsClassifyI18n.setClassifyId(goodsClassify.getClassifyId());
            goodsClassifyI18n.setClassifyName(classifyName);
            goodsClassifyI18n.setLanguageType(languageType.getLanguage());
            goodsClassifyI18n.setLanguageName(languageType.getRemark());
            goodsClassifyI18nService.save(goodsClassifyI18n);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String getLanguageNameByType(String languageType) {
        LanguageType language = languageTypeService.getOne(new LambdaQueryWrapper<LanguageType>().eq(LanguageType::getLanguage, languageType));
        return language.getRemark();
    }

    @Override
    public Result updateClassify(GoodsClassify goodsClassify) {
        baseMapper.updateById(goodsClassify);
        goodsClassify=baseMapper.selectById(goodsClassify.getClassifyId());
        // 更新当前的
        goodsClassifyI18nService.update(new GoodsClassifyI18n(), new LambdaUpdateWrapper<GoodsClassifyI18n>()
                .eq(GoodsClassifyI18n::getClassifyId, goodsClassify.getClassifyId())
                .eq(GoodsClassifyI18n::getLanguageType, goodsClassify.getLanguageType())
                .set(GoodsClassifyI18n::getClassifyName, goodsClassify.getClassifyName())
        );

        // 翻译其他语言
        asyncUpdateGoodsClassifyI18n(goodsClassify);
        return Result.success();
    }

    @Async
    public void asyncUpdateGoodsClassifyI18n(GoodsClassify goodsClassify) {
        List<LanguageType> enableLanguageTypeList = languageTypeService.getEnableLanguageTypeList();
        enableLanguageTypeList.removeIf(item -> Objects.equals(goodsClassify.getLanguageType(), item.getLanguage()));
        for (LanguageType languageType : enableLanguageTypeList) {
            String classifyName = null;
            try {
                classifyName = TransApi.getTransResult(goodsClassify.getClassifyName(), goodsClassify.getLanguageType(), languageType.getLanguage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            GoodsClassifyI18n goodsClassifyI18n = goodsClassifyI18nService.getOne(new QueryWrapper<GoodsClassifyI18n>()
                    .eq("language_type", languageType.getLanguage())
                    .eq("classify_id", goodsClassify.getClassifyId()));
            if(goodsClassifyI18n!=null){
                goodsClassifyI18n.setClassifyName(classifyName);
                goodsClassifyI18nService.updateById(goodsClassifyI18n);
            }else{
                goodsClassifyI18n= new GoodsClassifyI18n();
                goodsClassifyI18n.setLanguageType(languageType.getLanguage());
                goodsClassifyI18n.setLanguageName(languageType.getRemark());
                goodsClassifyI18n.setClassifyId(goodsClassify.getClassifyId());
                goodsClassifyI18n.setClassifyName(classifyName);
                goodsClassifyI18nService.save(goodsClassifyI18n);
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Result deleteClassify(Long classifyId) {
        int count = goodsDao.selectCount(new QueryWrapper<Goods>().eq("classify_id", classifyId));
        if (count > 0) {
            return Result.error("60168");
        }
        baseMapper.deleteById(classifyId);

        goodsClassifyI18nService.remove(new LambdaQueryWrapper<GoodsClassifyI18n>()
                .eq(GoodsClassifyI18n::getClassifyId, classifyId)
        );

        return Result.success();
    }
}
