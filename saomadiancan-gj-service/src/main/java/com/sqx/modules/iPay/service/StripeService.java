package com.sqx.modules.iPay.service;

import com.sqx.common.utils.Result;
import com.sqx.modules.iPay.param.BusinessParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author chengming
 * @since 2025/4/15
 */
public interface StripeService {

    void callback(HttpServletRequest request, HttpServletResponse response);

    Result pay(BusinessParam businessParam);
}
