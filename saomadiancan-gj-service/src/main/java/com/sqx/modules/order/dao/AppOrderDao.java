package com.sqx.modules.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.order.entity.TbOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface AppOrderDao extends BaseMapper<TbOrder> {

   // IPage<TbOrder> selectOrderList(Page<TbOrder> pages, @Param("userId") Long userId,@Param("status") Integer status,@Param("orderType") Integer orderType, @Param("shopId")Long shopIs);

    List<TbOrder> selectOrderList(@Param("userId") Long userId, @Param("status") Integer status, @Param("orderType") Integer orderType, @Param("shopId")Long shopIs);

    int insertOrder(TbOrder order);

    IPage<TbOrder> selectOrder(Page<TbOrder> pages,@Param("status") Integer status,@Param("phone") String phone, @Param("shopId") Long shopId,
                               @Param("userName") String userName, @Param("orderNumber") String orderNumber,@Param("orderType") Integer orderType);

    IPage<TbOrder> selectOrderByAdmin(Page<TbOrder> pages,@Param("status") Integer status,@Param("phone") String phone,
                                      @Param("shopId") Long shopId, @Param("userName") String userName,
                                      @Param("userId") Long userId,@Param("orderNumber") String orderNumber,@Param("orderType") Integer orderType,
                                      @Param("isPay") Integer isPay
    );

    int selectCountOrderByUserId(Long userId, String date, String dateType);

    BigDecimal selectSunMoneyByUserId(Long userId, String date, String dateType);

    IPage<TbOrder> selectOrderDetails(Page<TbOrder> pages, Long userId);

    IPage<TbOrder> waitTakeFood(Page<TbOrder> pages, Long userId, Integer orderType, Integer status);

    int selectCountOrderByTime(@Param("payTime") String payTime, @Param("shopId") Long shopId);

    int deleteCouponByOrderId(@Param("orderId") Long orderId);

    BigDecimal selectSumMoney(@Param("date") String date,@Param("dateType") String dateType);

    int selectCountOrder(@Param("date") String date,@Param("dateType") String dateType);

    int updateorderStatus(String date);

    List<TbOrder> selectOrderByTimeList(String date);

    int getShopUser(@Param("shopId") Long shopId);

    TbOrder queryByOrderNum(@Param("orderNum") String orderNum);
}
