package com.sqx.modules.pay.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.Result;
import com.sqx.modules.pay.entity.CashOut;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * @author fang
 * @date 2020/7/8
 */
@Mapper
public interface CashOutDao extends BaseMapper<CashOut> {

    List<CashOut> selectCashOutLimit3();

    Double selectCashOutSum(@Param("userId") Long userId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    Double sumMoney(@Param("time") String time, @Param("flag") Integer flag);

    Integer countMoney(@Param("time") String time, @Param("flag") Integer flag);

    Integer stayMoney(@Param("time") String time, @Param("flag") Integer flag);

    void updateMayMoney(@Param("type") Integer type,@Param("userId")Long userId,@Param("money") Double money);

    Double selectMayMoney(@Param("userId") Long userId);

    void addMoneyDetails(@Param("userId") Long userId,@Param("title") String title, @Param("content")String content, @Param("type")Integer type, @Param("money")BigDecimal money, @Param("createTime")Timestamp createTime);

    BigDecimal cashSumMoney(@Param("date") String date,@Param("dateType") String dateType);

    int cashCount(@Param("date") String date,@Param("dateType") String dateType);

    BigDecimal awaitSumMoney(@Param("date") String date,@Param("dateType") String dateType);

    int awaitCashCount(@Param("date") String date,@Param("dateType") String dateType);

    IPage<CashOut> selectCashOut(Page<CashOut> pages,@Param("userId") Long userId);

    BigDecimal monthCashOutMoney(@Param("userId") Long userId,@Param("date") String date);
}
