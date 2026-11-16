package com.sqx.modules.goods.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.goods.dao.GoodsRuleMapper;
import com.sqx.modules.goods.entity.GoodsRule;
import com.sqx.modules.goods.entity.GoodsRuleValue;
import com.sqx.modules.goods.i18n.rule.db.service.IGoodsRuleI18nService;
import com.sqx.modules.goods.i18n.rule.domain.entity.GoodsRuleI18n;
import com.sqx.modules.goods.i18n.ruleValue.db.service.IGoodsRuleValueI18nService;
import com.sqx.modules.goods.i18n.ruleValue.domain.entity.GoodsRuleValueI18n;
import com.sqx.modules.goods.service.GoodsRuleService;
import com.sqx.modules.goods.service.GoodsRuleValueService;
import com.sqx.modules.languageType.entity.LanguageType;
import com.sqx.modules.languageType.service.LanguageTypeService;
import com.sqx.modules.utils.RequestUtil;
import com.sqx.modules.utils.translate.TransApi;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class GoodsRuleServiceImpl extends ServiceImpl<GoodsRuleMapper, GoodsRule> implements GoodsRuleService {

    @Autowired
    private GoodsRuleValueService goodsRuleValueService;
    @Autowired
    private LanguageTypeService languageTypeService;
    @Autowired
    private IGoodsRuleI18nService iGoodsRuleI18nService;
    @Autowired
    private IGoodsRuleValueI18nService iGoodsRuleValueI18nService;

    @Override
    public Result findAll(Integer page, Integer limit, GoodsRule goodsRule) {
        if (StringUtils.isBlank(goodsRule.getLanguageType())) {
            goodsRule.setLanguageType(RequestUtil.getAcceptLanguage());
        }

        List<GoodsRule> goodsRules = baseMapper.list(goodsRule);
//        List<GoodsRule> goodsRules = baseMapper.selectList(new QueryWrapper<>(goodsRule));
        for (GoodsRule r : goodsRules) {
            r.setRuleValue(goodsRuleValueService.selectGoodsRuleValue(r.getId(), goodsRule.getLanguageType()));

            LanguageType languageType = languageTypeService.getOne(new LambdaQueryWrapper<LanguageType>().eq(LanguageType::getLanguage, r.getLanguageType()));
            if (Objects.nonNull(languageType)) {
                r.setLanguageName(languageType.getRemark());
            }
        }
        return Result.success().put("data",goodsRules);
    }

    @Override
    public Result info() {
        List<GoodsRule> all = baseMapper.selectList(null);
        for (GoodsRule r : all) {
//            r.setRuleValue(goodsRuleValueService.selectGoodsRuleValue(r.getId()));
        }
        return Result.success().put("data",all);
    }

    @Transactional
    @Override
    public Result saveBody(GoodsRule goodsRule) {
        String languageType = RequestUtil.getAcceptLanguage();
        goodsRule.setLanguageType(languageType);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        goodsRule.setCreateTime(sdf.format(new Date())); //创建时间
        baseMapper.insert(goodsRule);

        saveRuleI18n(goodsRule);

        List<GoodsRuleValue> list = goodsRule.getRuleValue();
        for (GoodsRuleValue goodsRuleValue : list) {
            goodsRuleValue.setRuleId(goodsRule.getId());
            goodsRuleValue.setLanguageType(goodsRule.getLanguageType());
            goodsRuleValueService.save(goodsRuleValue);

            saveRuleValueI18n(goodsRuleValue);
        }
        return Result.success();
    }

    private void saveRuleValueI18n(GoodsRuleValue goodsRuleValue) {
        // 先保存当前语言
        GoodsRuleValueI18n goodsRuleValueI18n = new GoodsRuleValueI18n();
        goodsRuleValueI18n.setRuleValueId(goodsRuleValue.getId());
        goodsRuleValueI18n.setDetail(goodsRuleValue.getDetail());
        goodsRuleValueI18n.setValue(goodsRuleValue.getValue());
        goodsRuleValueI18n.setLanguageType(goodsRuleValue.getLanguageType());
        goodsRuleValueI18n.setLanguageName(languageTypeService.getNameByType(goodsRuleValue.getLanguageType()));
        iGoodsRuleValueI18nService.save(goodsRuleValueI18n);

        List<LanguageType> enableLanguageTypeList = languageTypeService.getEnableLanguageTypeList();
        enableLanguageTypeList = enableLanguageTypeList
                .stream()
                .filter(item -> !Objects.equals(goodsRuleValue.getLanguageType(), item.getLanguage()))
                .collect(Collectors.toList());

        String detail = goodsRuleValue.getDetail();
        String value = goodsRuleValue.getValue();

        List<GoodsRuleValueI18n> goodsRuleValueI18nList = new ArrayList<>();
        for (LanguageType languageType : enableLanguageTypeList) {
            String tranDetail = null;
            try {
                tranDetail = TransApi.getTransResult(detail, goodsRuleValue.getLanguageType(), languageType.getLanguage());
                Thread.sleep(1000);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


            String tranValue = null;
            try {
                tranValue = TransApi.getTransResult(value, goodsRuleValue.getLanguageType(), languageType.getLanguage());
                Thread.sleep(1000);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


            GoodsRuleValueI18n ruleValueI18n = new GoodsRuleValueI18n();
            ruleValueI18n.setRuleValueId(goodsRuleValue.getId());
            String deleteWhitespace = StringUtils.deleteWhitespace(tranDetail);
            ruleValueI18n.setDetail(deleteWhitespace);
            ruleValueI18n.setValue(tranValue);
            ruleValueI18n.setLanguageType(languageType.getLanguage());
            ruleValueI18n.setLanguageName(languageType.getRemark());
            goodsRuleValueI18nList.add(ruleValueI18n);
        }

        iGoodsRuleValueI18nService.saveBatch(goodsRuleValueI18nList);
    }

    private void saveRuleI18n(GoodsRule goodsRule) {
        // 先保存当前语言
        GoodsRuleI18n goodsRuleI18n = new GoodsRuleI18n();
        goodsRuleI18n.setRuleId(goodsRule.getId());
        goodsRuleI18n.setRuleName(goodsRule.getRuleName());
        goodsRuleI18n.setLanguageType(goodsRule.getLanguageType());
        goodsRuleI18n.setLanguageName(languageTypeService.getNameByType(goodsRule.getLanguageType()));
        iGoodsRuleI18nService.save(goodsRuleI18n);

        List<LanguageType> enableLanguageTypeList = languageTypeService.getEnableLanguageTypeList();
        enableLanguageTypeList = enableLanguageTypeList
                .stream()
                .filter(item -> !Objects.equals(goodsRule.getLanguageType(), item.getLanguage()))
                .collect(Collectors.toList());

        String ruleName = goodsRule.getRuleName();

        List<GoodsRuleI18n> goodsRuleI18nList = new ArrayList<>();
        for (LanguageType languageType : enableLanguageTypeList) {
            String tranRuleName = null;
            try {
                tranRuleName = TransApi.getTransResult(ruleName, goodsRule.getLanguageType(), languageType.getLanguage());
                Thread.sleep(1000);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


            GoodsRuleI18n ruleI18n = new GoodsRuleI18n();
            ruleI18n.setRuleId(goodsRule.getId());
            ruleI18n.setRuleName(tranRuleName);
            ruleI18n.setLanguageType(languageType.getLanguage());
            ruleI18n.setLanguageName(languageType.getRemark());
            goodsRuleI18nList.add(ruleI18n);
        }

        iGoodsRuleI18nService.saveBatch(goodsRuleI18nList);
    }

    @Transactional
    @Override
    public Result updateBody(GoodsRule goodsRule) {
        if (StringUtils.isBlank(goodsRule.getLanguageType())) {
            goodsRule.setLanguageType(RequestUtil.getAcceptLanguage());
        }

        goodsRuleValueService.deleteGoodsRuleValue(goodsRule.getId());
        //放入新值
        List<GoodsRuleValue> list = goodsRule.getRuleValue();
        for (GoodsRuleValue goodsRuleValue : list) {
            goodsRuleValue.setLanguageType(goodsRule.getLanguageType());
            goodsRuleValue.setRuleId(goodsRule.getId());
            goodsRuleValueService.save(goodsRuleValue);

            saveRuleI18n(goodsRule);
        }
        baseMapper.updateById(goodsRule);
        return Result.success();
    }

    @Override
    public Result findOne(Long id, String languageType) {
        GoodsRule s = baseMapper.selectById(id);
        s.setRuleValue(goodsRuleValueService.selectGoodsRuleValue(id, languageType));
        return Result.success().put("data",s);
    }

    @Override
    public Result delete(Long id) {
        // 删除规格值
        LambdaQueryWrapper<GoodsRuleValue> queryWrapper = new LambdaQueryWrapper<GoodsRuleValue>()
                .eq(GoodsRuleValue::getRuleId, id);
        List<GoodsRuleValue> goodsRuleValues = goodsRuleValueService.list(queryWrapper);
        List<Long> goodsRuleValueIds = goodsRuleValues.stream().map(GoodsRuleValue::getId).collect(Collectors.toList());

        iGoodsRuleValueI18nService.remove(new LambdaQueryWrapper<GoodsRuleValueI18n>().in(GoodsRuleValueI18n::getRuleValueId, goodsRuleValueIds));
        goodsRuleValueService.remove(new LambdaQueryWrapper<GoodsRuleValue>().eq(GoodsRuleValue::getRuleId, id));

        // 删除规格国际化
        iGoodsRuleI18nService.remove(new LambdaQueryWrapper<GoodsRuleI18n>().eq(GoodsRuleI18n::getRuleId, id));

        // 删除规格
        baseMapper.deleteById(id);
        return Result.success();
    }


}