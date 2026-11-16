package com.sqx.modules.app.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.entity.UserMoney;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

@Mapper
public interface UserCashOutDao extends BaseMapper<UserEntity> {

    UserMoney findBalanceById(@Param("userId") Long userId);

    int updateMoney(@Param("type") int type, @Param("userId") Long userId, @Param("money") Double moneySum);

    int updateRetreatMoney(@Param("userId") Long userId,@Param("sumMoney") BigDecimal sumMoney);

}
