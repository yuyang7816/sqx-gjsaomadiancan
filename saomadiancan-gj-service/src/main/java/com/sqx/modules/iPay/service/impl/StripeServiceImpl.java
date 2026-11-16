package com.sqx.modules.iPay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sqx.common.utils.Result;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.iPay.business.BusinessService;
import com.sqx.modules.iPay.business.CallbackBusinessService;
import com.sqx.modules.iPay.constants.PayCommonInfoCode;
import com.sqx.modules.iPay.enums.PayPlatformEnum;
import com.sqx.modules.iPay.param.BusinessParam;
import com.sqx.modules.iPay.service.StripeService;
import com.sqx.modules.iPay.utils.StripeUtil;
import com.sqx.modules.utils.GenUtil;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.net.Webhook;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * @author chengming
 * @since 2025/4/15
 */
@Slf4j
@Service
public class StripeServiceImpl implements StripeService {

    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private CallbackBusinessService callbackBusinessService;
    @Autowired
    private BusinessService businessService;

    @Override
    public Result pay(BusinessParam businessParam) {
        // 订单号
        String orderNo = GenUtil.genOrderNo();

        // 处理业务
        businessService.process(orderNo, PayPlatformEnum.STRIPE.getCode(), businessParam);

        String domain = commonInfoService.getValue(businessParam.getClientId());
        String successUrl = domain + "/sqx_fast/app/stripe/webhook";
        String cancelUrl = domain + "/sqx_fast/app/stripe/notifyCancel";
        Integer payChannel = businessParam.getPayChannel();

        String apiKey = commonInfoService.findOne(PayCommonInfoCode.STRIPE_API_KEY_CODE).getValue();
        String res = StripeUtil.stripePay(apiKey, orderNo, domain, businessParam.getAmount(), payChannel, successUrl, cancelUrl);
        return Result.success(res);
    }

    @Override
    public void callback(HttpServletRequest request, HttpServletResponse response) {
        log.info("开始stripe回调");
        String endpointSecret = commonInfoService.findOne(PayCommonInfoCode.STRIPE_SIGN_CODE).getValue();

        Event event = null;
        try {
            String sigHeader = request.getHeader("Stripe-Signature");
            log.debug("sigHeader: {}", sigHeader);

            String payload = getRequestPostStr(request);
            log.debug("payload: {}", payload);

            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException | IOException e) {
            e.printStackTrace();
            response.setStatus(400);
            return;
        }

        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        JSONObject data = JSONObject.parseObject(dataObjectDeserializer.getRawJson());
        log.info("event type -> {}", event.getType());

        // Handle the event
        switch (event.getType()) {
            case "payment_intent.created": {
                // 创建支付订单成功
                break;
            }
            case "payment_intent.succeeded": {
                // 用户支付订单成功

                break;
            }
            case "checkout.session.completed": {
                // 用户支付订单完成, 执行业务逻辑
                Map metadata = (Map) data.get("metadata"); // 自定义的参数
                String orderNo = metadata.get("orderNo").toString();
                String tradeNo = data.getString("payment_intent");

                callbackBusinessService.process(orderNo, tradeNo);
                break;
            }
            case "charge.refunded": {
                //发起退款时(告诉银行我要退款)
                break;
            }
            case "charge.refund.updated": {
                //用户收到退款(银行退款到账) 正常情况下会返回对象
                break;
            }
            case "charge.succeeded": {

                break;
            }
            default: {
                log.error("Unhandled event type: {}", event.getType());
                response.setStatus(400);
                return;
            }
        }

        response.setStatus(200);
    }

    public String getRequestPostStr(HttpServletRequest request) throws IOException {
        byte[] buffer = getRequestPostBytes(request);
        if (Objects.isNull(buffer)) {
            log.error("stripe回调失败, 请求头为空");
            throw new RuntimeException();
        }

        String charEncoding = request.getCharacterEncoding();
        if (charEncoding == null) {
            charEncoding = "UTF-8";
        }

        return new String(buffer, charEncoding);
    }

    public byte[] getRequestPostBytes(HttpServletRequest request) throws IOException {
        int contentLength = request.getContentLength();
        if (contentLength < 0) {
            return null;
        }

        byte[] buffer = new byte[contentLength];
        for (int i = 0; i < contentLength; ) {
            int readlen = request.getInputStream().read(buffer, i, contentLength - i);
            if (readlen == -1) {
                break;
            }

            i += readlen;
        }

        return buffer;
    }
}
