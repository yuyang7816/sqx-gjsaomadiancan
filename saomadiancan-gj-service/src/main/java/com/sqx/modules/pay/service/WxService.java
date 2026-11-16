package com.sqx.modules.pay.service;

import com.sqx.common.utils.Result;
import com.sqx.modules.pay.entity.PayDetails;

import java.math.BigDecimal;


/**
 * @author fang
 * @date 2020/2/26
 */
public interface WxService {

    Result payMoney(Long userId, BigDecimal money, Integer type) throws Exception;

    Result payOrder(Long userId, Long parentId, Integer type, Long addressId, Integer orderType, String orderRemarks) throws Exception;

    String payBack(String resXml,Integer type);;

    boolean wxRefund(PayDetails payDetails, BigDecimal refundMoney);

    Result balanceOrder(Long userId, Long parentId, Integer orderType, Long addressId, String orderRemarks);

    Result payVip(Long userId,Long vipTypeId,Integer type) throws Exception;

    Result wxPayMoney(Long userId, Long shopId, BigDecimal money, Integer type) throws Exception;
}