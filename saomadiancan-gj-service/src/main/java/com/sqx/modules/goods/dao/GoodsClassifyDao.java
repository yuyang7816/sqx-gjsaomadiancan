package com.sqx.modules.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.goods.entity.GoodsClassify;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GoodsClassifyDao extends BaseMapper<GoodsClassify> {
    GoodsClassify getById(@Param("classifyId") Integer classifyId, @Param("languageType") String languageType);

    int countByName(@Param("shopId") Long shopId, @Param("classifyName") String classifyName, @Param("languageType") String languageType);
}
