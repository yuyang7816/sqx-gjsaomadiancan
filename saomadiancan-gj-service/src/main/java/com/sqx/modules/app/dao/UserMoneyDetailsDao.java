package com.sqx.modules.app.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.app.entity.UserMoneyDetails;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

@Mapper
public interface UserMoneyDetailsDao extends BaseMapper<UserMoneyDetails> {

    Double monthIncome(@Param("date") String date,@Param("userId") Long userId);

    Double selectMyProfit(Long userId);

    IPage<UserMoneyDetails> selectUserMoneyDetails(Page<UserMoneyDetails> pages,@Param("userId") Long userId, @Param("languageType") String languageType);

    IPage<UserMoneyDetails> selectPwCashDetails(Page<UserMoneyDetails> pages, @Param("userName") String userName, @Param("date") String date,
                                                @Param("dateType") String dateType,@Param("zhifubao") String zhifubao, @Param("zhifubaoName") String zhifubaoName,
                                                @Param("userId") Long userId);

    IPage<UserMoneyDetails> queryUserMoneyDetails(@Param("page1") IPage<UserMoneyDetails> page1, @Param("userId") Long userId, @Param("classify") Integer classify, @Param("type") Integer type, @Param("shopId") Long shopId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    BigDecimal getShopStatistics(Long shopId, String startTime, String endTime);
}
