package com.sqx.modules.integral.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.integral.entity.UserIntegral;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserIntegralDao extends BaseMapper<UserIntegral> {

    int addUserIntegral(@Param("num") Integer num, @Param("userId") Long userId);

    int updateUserIntegral(@Param("userId") Long userId,@Param("needIntegral") Integer needIntegral);

}
