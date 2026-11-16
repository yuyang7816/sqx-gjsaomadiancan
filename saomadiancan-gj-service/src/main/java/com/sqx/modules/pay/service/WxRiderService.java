package com.sqx.modules.pay.service;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.pay.entity.PayDetails;

import java.math.BigDecimal;


/**
 * @author fang
 * @date 2020/2/26
 */
public interface WxRiderService {

    Result payMoney(Long id, BigDecimal money, Integer type) throws Exception;

    Result payOrder(Long userId, String indentNumber, Integer type) throws Exception;

    String payBack(String resXml,Integer type);;

    boolean wxRefund(PayDetails payDetails);

}