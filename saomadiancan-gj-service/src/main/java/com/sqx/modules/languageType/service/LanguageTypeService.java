package com.sqx.modules.languageType.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.languageType.entity.LanguageType;

import java.util.List;

/**
 * 语种 服务类
 * @author wuChang
 * @since 2023-12-13
 */

public interface LanguageTypeService extends IService<LanguageType> {
    /**
     * 新增或修改语种
     */
    Result saveLanguageType (LanguageType languageType);

    /**
     * 获取改语种列表
     */
    IPage<LanguageType> getLanguageTypeList(Integer page, Integer limit, LanguageType languageType);

    <T extends LanguageFillable> void fillLanguageName(List<T> list);

    /**
     * 填充语言名称
     * @param entity 填充对象
     */
    <T extends LanguageFillable> void fillLanguageName(T entity);

    List<LanguageType> getEnableLanguageTypeList();

    String getNameByType(String languageType);
}
