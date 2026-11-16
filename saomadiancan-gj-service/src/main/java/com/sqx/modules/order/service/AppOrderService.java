package com.sqx.modules.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.order.entity.TbOrder;
import com.sqx.modules.order.entity.vo.OrderVo;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public interface AppOrderService extends IService<TbOrder> {

    Result insertOrder(Long userId,OrderVo orderVo);
    Result updateOrder(TbOrder order);

    Result deleteOrderList(List<Integer> orderId);

    Result selectOrderList(Long userId, Long shopId, Integer status, Integer orderType, String languageType);

    Result joinOrder(Long userId, Long goodsId, Long parentId);

    TbOrder selectOrderByNum(String orderNum);

    Result selectAllOrder(Integer page, Integer limit, Integer status, String phone, Long shopId, String userName, Long userId, String orderNumber, Integer orderType, Integer isPay);

    Result selectOrderByUserId(Long userId, String date, String dateType);

    Result selectOrderDetails(Integer page, Integer limit, Long userId);

    Result waitTakeFood(Long userId, Integer orderType, Integer page, Integer limit, Integer status);

    List<TbOrder> selectOrderByParentId(Long parentId);

    Result selectOrderById(Long orderId);

    Result updateGoodsNum(Long orderGoodsId, Integer type, Integer num, Long shopId);


    Result emptyShoppingTrolley(Long userId, Long shopId);

    Result employCoupon(Long userId, Long parentId, Long couponId);

    Result deleteCouponByOrderId(Long orderId);

    Result buyGoods(Long userId, Long shopId, Long goodsId, Integer num, Long skuId, String skuMessage, Integer orderType, String orderRemarks, String languageType);

    Result echoOrder(Long userId, Long shopId, Long goodsId);

    Result selectBuyGoods(Long userId, Long orderId);

    Result adminCancelOrder(Long orderId, Long orderGoodsId);

    Result accomplishOrder(Long userId, Long orderId);

    void orderEnd();

    TbOrder selectOrderByIds(Long orderId);

    Result print(Long orderId);

    TbOrder getSeatOrder(Long shopId, Long seatId);

    HashMap<String,Object> shopOrderStatistics(Long shopId);

    HashMap<String,Object> goodStatusCount(Long shopId);

    BigDecimal selectCancelMoney(Long orderId, Long orderGoodsId);

    TbOrder queryInfoByOrderNum(String orderNum);

    void completeOrder(Long orderId);
}
