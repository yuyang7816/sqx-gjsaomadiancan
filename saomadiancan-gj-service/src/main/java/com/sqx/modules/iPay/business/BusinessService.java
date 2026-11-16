package com.sqx.modules.iPay.business;

import com.sqx.modules.iPay.param.BusinessParam;

/**
 * @author chengming
 * @since 2025/4/24
 */
public interface BusinessService {

    /**
     * 支付业务处理
     * @param orderNo 订单号
     * @param platform 支付平台 1微信 2支付宝 7paypal 8stripe
     * @param businessParam 支付参数
     */
    void process(String orderNo, Integer platform, BusinessParam businessParam);
}
