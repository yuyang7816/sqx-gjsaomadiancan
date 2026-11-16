package com.sqx.modules.languageType.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.exception.SqxException;
import com.sqx.common.utils.Result;
import com.sqx.modules.languageType.dao.LanguageTypeDao;
import com.sqx.modules.languageType.entity.LanguageType;
import com.sqx.modules.languageType.service.LanguageFillable;
import com.sqx.modules.languageType.service.LanguageTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 语种 服务实现类
 * @author wuChang
 * @since 2023-12-13
 */
@Slf4j
@Service
public class LanguageTypeServiceImp extends ServiceImpl<LanguageTypeDao, LanguageType> implements LanguageTypeService {


    @Override
    public Result saveLanguageType(LanguageType languageType) {
        if (languageType.getId() != null) {
            return Result.upStatus(baseMapper.updateById(languageType));
        } else {
            languageType.setCreateTime(new Date());
            return Result.upStatus(baseMapper.insert(languageType));
        }
    }

    @Override
    public IPage<LanguageType> getLanguageTypeList(Integer page, Integer limit, LanguageType languageType) {
        Page<LanguageType> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        return baseMapper.selectPage(pages, new QueryWrapper<>(languageType).orderByAsc("sort"));
    }

    @Override
    public <T extends LanguageFillable> void fillLanguageName(List<T> list) {
        if (list.isEmpty()) {
            return;
        }

        List<LanguageType> languageTypeList = this.list();
        Map<String, String> languageTypeMap = languageTypeList.stream().collect(HashMap::new, (map, item) -> map.put(item.getLanguage(), item.getRemark()), HashMap::putAll);

        for (T languageInfo : list) {
            if (languageTypeMap.containsKey(languageInfo.getLanguageType())) {
                languageInfo.setLanguageName(languageTypeMap.get(languageInfo.getLanguageType()));
            }
        }
    }

    @Override
    public <T extends LanguageFillable> void fillLanguageName(T entity) {
        if (Objects.isNull(entity)) {
            return;
        }

        List<LanguageType> languageTypeList = this.list();
        Map<String, String> languageTypeMap = languageTypeList.stream().collect(HashMap::new, (map, item) -> map.put(item.getLanguage(), item.getRemark()), HashMap::putAll);
        if (languageTypeMap.containsKey(entity.getLanguageType())) {
            entity.setLanguageName(entity.getLanguageType());
        }
    }

    @Override
    public List<LanguageType> getEnableLanguageTypeList() {
        return this.list(new LambdaQueryWrapper<LanguageType>().eq(LanguageType::getIsEnable, 1));
    }

    @Override
    public String getNameByType(String language) {
        LambdaQueryWrapper<LanguageType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LanguageType::getLanguage, language);

        LanguageType languageType = this.getOne(queryWrapper);
        if (Objects.isNull(languageType)) {
            log.error("语言类型不存在: {}", language);
            throw new SqxException("语言类型不存在");
        }

        return languageType.getRemark();
    }
}
