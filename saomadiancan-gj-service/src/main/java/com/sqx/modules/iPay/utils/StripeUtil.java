package com.sqx.modules.iPay.utils;

import com.sqx.common.exception.SqxException;
import com.sqx.modules.iPay.enums.PayChannelEnum;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author chengming
 * @since 2025/4/15
 */
public class StripeUtil {
    private static Logger logger = LoggerFactory.getLogger(StripeUtil.class);

    /**
     * stripe支付
     * @param apiKey stripe支付秘钥
     * @param orderNo 订单号
     * @param name 商品名称
     * @param amount 金额
     * @param payChannel 支付渠道 web app
     * @param successUrl 支付成功后跳转地址
     * @param cancelUrl 支付取消后跳转地址
     * @return
     */
    public static String stripePay(String apiKey, String orderNo, String name, BigDecimal amount, Integer payChannel, String successUrl, String cancelUrl) {
        Stripe.apiKey = apiKey;

        String res;
        if (Objects.equals(PayChannelEnum.WEB.getCode(), payChannel)) {
            try {
                res = StripeUtil.createWebSession(orderNo, name, amount, successUrl, cancelUrl);
            } catch (StripeException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } else if (Objects.equals(PayChannelEnum.APP.getCode(), payChannel)) {
            try {
                res = StripeUtil.createAppSession(orderNo, amount);
            } catch (StripeException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } else {
            throw new SqxException("不支持的支付渠道");
        }

        return res;
    }

    /**
     * 创建stripe session
     * @param name 商品名称
     * @param amount 金额
     * @param successUrl 订单成功后跳转地址
     * @param cancelUrl 订单取消后跳转地址
     * @return session
     */
    private static String createWebSession(String orderNo, String name, BigDecimal amount, String successUrl, String cancelUrl) throws StripeException {
        // 自定义业务参数
        Map<String, String> metaData = new HashMap<>();
        metaData.put("orderNo", orderNo);

        // 商品信息
        SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(name)  // 商品名称
                .build();

        // 金额信息
        SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
                .setUnitAmountDecimal(amount.multiply(new BigDecimal(100))) // 金额
                .setProductData(productData)
                .setCurrency("usd") // 货币
                .build();

        // line信息
        SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                .setPriceData(priceData)
                .setQuantity(1L) // 商品数量
                .build();

        SessionCreateParams params = SessionCreateParams.builder()
                .addLineItem(lineItem)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .putAllMetadata(metaData)
                .build();

        Session session = Session.create(params);
        logger.info("stripe 返回值: {}", session.getUrl());
        return session.getUrl();
    }

    /**
     *
     * @param orderNo 订单号
     * @param amount 金额
     * @return
     * @throws StripeException
     */
    private static String createAppSession(String orderNo, BigDecimal amount) throws StripeException {
        // 自定义业务参数
        Map<String, String> metaData = new HashMap<>();
        metaData.put("orderNo", orderNo);

        PaymentIntentCreateParams.AutomaticPaymentMethods automaticPaymentMethods = PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                .setEnabled(true)
                .build();

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount.longValue())
                .setCurrency("usd")
                .putAllMetadata(metaData)
                .setAutomaticPaymentMethods(automaticPaymentMethods)
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);
        logger.info("stripe 返回值: {}", paymentIntent.getClientSecret());
        return paymentIntent.getClientSecret();
    }
}
