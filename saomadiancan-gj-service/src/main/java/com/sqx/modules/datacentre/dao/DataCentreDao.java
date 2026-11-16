package com.sqx.modules.datacentre.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.order.entity.TbOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

@Mapper
public interface DataCentreDao {

    int findAllCount();

    int findDayAddUser(String format);

    int findMonthAddUser(String format);

    int findYearAddUser(String format);

    BigDecimal findDayIncome(String format);

    BigDecimal findMonthIncome(String format);

    BigDecimal findYearIncome(String format);

    int findIndentCount(String format);

    IPage<TbOrder> selectPayOrderAnalyze(Page<TbOrder> pages, String date, String dateType);

    int sumOrderMonth(Long userId, String date);

    BigDecimal sumTopUpMonth(Long userId, String date);

    BigDecimal selectTopUpStatistics(@Param("date") String date,@Param("dateType") String dateType);

    int selectTakeCount(Integer orderType,Long shopId, String date, String dateType);

    BigDecimal selectTakeMoney(Integer orderType,Long shopId, String date, String dateType);

    int cancelOrderCount(Long shopId, String date, String dateType);

    BigDecimal cancelOrderMoney(Long shopId, String date, String dateType);

    int selectNewShopCount(String date, String dateType);

    int allUserCount(String date, String dateType);

    int phoneUserCount(String date, String dateType);

    IPage<TbOrder> selectRankingList(Page<TbOrder> pages, String date, String dateType);

    BigDecimal shopMoney(Long shopId, String date, String dateType);

    BigDecimal moneyDayMoney(@Param("shopId") Long shopId, @Param("date") String date, @Param("dateType") String dateType, @Param("languageType") String languageType);
}
