package com.sqx.modules.app.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.app.entity.UserMoney;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

@Mapper
public interface UserMoneyDao extends BaseMapper<UserMoney> {

    void updateMayMoney(@Param("type") Integer type, @Param("userId") Long userId, @Param("money") Double money);

    int addMoney(@Param("pwUserId") Long pwUserId, @Param("payMoney") BigDecimal payMoney);

    UserMoney selectMoney(@Param("userId") Long userId);

    int addUserMoney(@Param("userId") Long userId,@Param("money") BigDecimal money, @Param("type") Integer type);
}
