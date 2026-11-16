package com.sqx.modules.integral.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.integral.entity.UserIntegralDetails;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserIntegralDetailsDao extends BaseMapper<UserIntegralDetails> {

    UserIntegralDetails isSignIn(@Param("userId") Long userId,@Param("format") String format);

    IPage<UserIntegralDetails> selectIntegraldetailsList(Page<UserIntegralDetails> pages, Long userId, Integer classify, String languageType);

    IPage<UserIntegralDetails> selectSignIn(Page<UserIntegralDetails> pages,@Param("userId") Long userId);

    UserIntegralDetails selectUserIntegralDetails(@Param("userId") Long userId, @Param("date") String date);
}
