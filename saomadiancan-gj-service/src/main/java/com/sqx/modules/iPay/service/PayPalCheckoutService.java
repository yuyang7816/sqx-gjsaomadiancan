package com.sqx.modules.iPay.service;

import com.sqx.common.utils.Result;
import com.sqx.modules.iPay.param.BusinessParam;

import javax.servlet.http.HttpServletResponse;

/**
 * @author chengming
 * @since 2025/4/14
 */
public interface PayPalCheckoutService {
    String callback(String token, String PayerID, HttpServletResponse response);

    Result pay(BusinessParam businessParam);
}
