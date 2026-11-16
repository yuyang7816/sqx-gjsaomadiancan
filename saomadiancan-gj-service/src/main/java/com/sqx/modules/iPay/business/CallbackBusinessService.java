package com.sqx.modules.iPay.business;

/**
 * @author chengming
 * @since 2024/8/12
 * 回调业务处理
 */
public interface CallbackBusinessService {

    /**
     * 订单成功回调业务处理
     * @param orderNo 订单编号
     * @param tradeNo 第三方平台支付流水号
     */
    void process(String orderNo, String tradeNo);

    /**
     * 订单退款回调业务处理
     * @param orderNo 订单编号
     * @param tradeNo 第三方平台支付流水号
     */
    void refund(String orderNo, String tradeNo);
}
