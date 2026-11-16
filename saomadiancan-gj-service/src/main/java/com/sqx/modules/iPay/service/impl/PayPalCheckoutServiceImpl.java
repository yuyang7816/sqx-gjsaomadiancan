package com.sqx.modules.iPay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.paypal.http.HttpResponse;
import com.paypal.orders.Capture;
import com.paypal.orders.Order;
import com.paypal.orders.PurchaseUnit;
import com.sqx.common.exception.SqxException;
import com.sqx.common.utils.Result;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.iPay.business.BusinessService;
import com.sqx.modules.iPay.business.CallbackBusinessService;
import com.sqx.modules.iPay.constants.PayCommonInfoCode;
import com.sqx.modules.iPay.enums.PayPlatformEnum;
import com.sqx.modules.iPay.param.BusinessParam;
import com.sqx.modules.iPay.service.PayPalCheckoutService;
import com.sqx.modules.iPay.utils.CaptureOrder;
import com.sqx.modules.iPay.utils.CreateOrder;
import com.sqx.modules.pay.dao.PayDetailsDao;
import com.sqx.modules.pay.entity.PayDetails;
import com.sqx.modules.utils.GenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author chengming
 * @since 2025/4/14
 */
@Slf4j
@Service
public class PayPalCheckoutServiceImpl implements PayPalCheckoutService {
    @Autowired
    private CallbackBusinessService callbackBusinessService;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private PayDetailsDao payDetailsDao;
    @Autowired
    private BusinessService businessService;


    @Override
    public Result pay(BusinessParam businessParam) {
        // 订单号
        String orderNo = GenUtil.genOrderNo();

        // 处理业务
        businessService.process(orderNo, PayPlatformEnum.PAYPAL.getCode(), businessParam);

        String clientId = commonInfoService.getValue(PayCommonInfoCode.PAYPAL_CLIENT_ID_CODE);
        String clientSecret = commonInfoService.getValue(PayCommonInfoCode.PAYPAL_CLIENT_SECRET_CODE);
        String domain = commonInfoService.getValue(businessParam.getClientId());

        String successUrl = domain + "/sqx_fast/app/paypal/paypalCallback";
        String cancelUrl = domain + "/sqx_fast/app/paypal/notifyCancel";

        String subject = commonInfoService.getValue(12);
        String mode = commonInfoService.getValue(PayCommonInfoCode.PAYPAL_MODE_CODE);
        String payment;
        try {
            payment = new CreateOrder().createOrder(clientId, clientSecret, mode, subject, orderNo, businessParam.getAmount().toString(), successUrl, cancelUrl, businessParam.getPayChannel());
        } catch (IOException e) {
            log.error("支付异常: {}", e.getMessage());
            throw new SqxException("支付异常");
        }

        return  Result.success(payment);
    }

    @Override
    public String callback(String token, String PayerID, HttpServletResponse response) {
        log.info("开始paypal回调");
        String clientId = commonInfoService.getValue(PayCommonInfoCode.PAYPAL_CLIENT_ID_CODE);
        String clientSecret = commonInfoService.getValue(PayCommonInfoCode.PAYPAL_CLIENT_SECRET_CODE);
        String mode = commonInfoService.getValue(PayCommonInfoCode.PAYPAL_MODE_CODE);

        try {
            //请求PayPal接口 进行扣款操作
            HttpResponse<Order> orderHttpResponse = new CaptureOrder().captureOrder(token, clientId, clientSecret, mode);

            for (PurchaseUnit purchaseUnit : orderHttpResponse.result().purchaseUnits()) {
                for (Capture capture : purchaseUnit.payments().captures()) {
                    log.info("paypal流水号: {}", capture.id());
                    log.info("支付状态: {}", capture.status());
                    log.info("订单号: {}", capture.invoiceId());

                    String outTradeNo = capture.id();
                    String orderNo = capture.invoiceId();
                    String paymentStatus = capture.status();

                    String redirectUrl = getRedirectUrl(orderNo);

                    if ("COMPLETED".equals(paymentStatus)) {
                        // 业务处理
                        callbackBusinessService.process(orderNo, outTradeNo);
                        log.info("支付成功,状态为=COMPLETED");
                        response.sendRedirect(redirectUrl);
                        return "success";
                    }

                    if ("REFUNDED".equals(paymentStatus)) {
                        // 业务处理 TODO
                        log.info("退款成功");
                        response.sendRedirect(redirectUrl);
                        return "success";
                    }

                    if ("PENDING".equals(paymentStatus)) {
                        // 业务处理 TODO
                        log.info("订单支付成功,状态为=PENDING");
                        response.sendRedirect(redirectUrl);
                        return "success";
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return "fail";
    }

    private String getRedirectUrl(String orderNo) {
        PayDetails payDetails = getPayDetails(orderNo);
        if (Objects.isNull(payDetails)) {
            throw new SqxException("支付详情不存在");
        }

        JSONObject extend = JSONObject.parseObject(payDetails.getExtend());
        String redirectUri = extend.getString("redirectUri");
        if (StringUtils.isBlank(redirectUri)) {
            throw new SqxException("重定向地址不存在");
        }

        //String domain = commonInfoService.getValue(extend.getInteger("clientId"));
        // TODO 本地测试使用前端域名
        String domain = "http://192.168.0.126:8080";

        return domain + redirectUri;
    }

    private PayDetails getPayDetails(String orderNo) {
        QueryWrapper<PayDetails> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PayDetails::getOrderId, orderNo);

        return payDetailsDao.selectOne(queryWrapper);
    }
}
