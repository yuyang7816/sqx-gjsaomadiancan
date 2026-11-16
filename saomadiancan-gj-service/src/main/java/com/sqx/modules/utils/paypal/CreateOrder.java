package com.sqx.modules.utils.paypal;

import com.paypal.http.HttpResponse;
import com.paypal.http.serializer.Json;
import com.paypal.orders.*;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CreateOrder {


    public static final String CAPTURE = "CAPTURE";


    /**
     * LOGIN。当客户单击PayPal Checkout时，客户将被重定向到页面以登录PayPal并批准付款。
     * BILLING。当客户单击PayPal Checkout时，客户将被重定向到一个页面，以输入信用卡或借记卡以及完成购买所需的其他相关账单信息
     * NO_PREFERENCE。当客户单击“ PayPal Checkout”时，将根据其先前的交互方式将其重定向到页面以登录PayPal并批准付款，或重定向至页面以输入信用卡或借记卡以及完成购买所需的其他相关账单信息使用PayPal。
     * 默认值：NO_PREFERENCE
     */
    public static final String LANDINGPAGE = "NO_PREFERENCE";
    /**
     * CONTINUE。将客户重定向到PayPal付款页面后，将出现“ 继续”按钮。当结帐流程启动时最终金额未知时，请使用此选项，并且您想将客户重定向到商家页面而不处理付款。
     * PAY_NOW。将客户重定向到PayPal付款页面后，出现“ 立即付款”按钮。当启动结帐时知道最终金额并且您要在客户单击“ 立即付款”时立即处理付款时，请使用此选项。
     */
    public static final String USERACTION = "CONTINUE";
    /**
     * GET_FROM_FILE。使用贝宝网站上客户提供的送货地址。
     * NO_SHIPPING。从PayPal网站编辑送货地址。推荐用于数字商品
     * SET_PROVIDED_ADDRESS。使用商家提供的地址。客户无法在PayPal网站上更改此地址
     */
    public static final String SHIPPINGPREFERENCE = "GET_FROM_FILE";
    //支付方式 H5小程序
    public static final int PAY_WAY_H5=1;
    //支付方式 APP
    public static final int PAY_WAY_APP=2;

    /**
     * 生成订单主体信息
     */
    private OrderRequest buildRequestBody(String name,String ordersNo,String payMoney,String successUrl,String cancelUrl) {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent(CAPTURE);
        ApplicationContext applicationContext = new ApplicationContext()
                .brandName(name)
                .landingPage(LANDINGPAGE)
                .cancelUrl(cancelUrl).returnUrl(successUrl)
                .userAction(USERACTION)
                .shippingPreference(SHIPPINGPREFERENCE);
        orderRequest.applicationContext(applicationContext);

        List<PurchaseUnitRequest> purchaseUnitRequests = new ArrayList<PurchaseUnitRequest>();
        @SuppressWarnings("serial")
        PurchaseUnitRequest purchaseUnitRequest = new PurchaseUnitRequest()
                .description(name)
                .customId(ordersNo)
                .invoiceId(ordersNo)
                .amountWithBreakdown(new AmountWithBreakdown()
                        .currencyCode("USD")
                        .value(payMoney)// value = itemTotal + shipping + handling + taxTotal + shippingDiscount;
                        .amountBreakdown(new AmountBreakdown()
                                .itemTotal(new Money().currencyCode("USD").value(payMoney)) // itemTotal = Item[Supernote A6](value × quantity) + Item[帆布封套](value × quantity)
                                .shipping(new Money().currencyCode("USD").value("0.00"))
                                .handling(new Money().currencyCode("USD").value("0.00"))
                                .taxTotal(new Money().currencyCode("USD").value("0.00"))
                                .shippingDiscount(new Money().currencyCode("USD").value("0.00"))));
        purchaseUnitRequests.add(purchaseUnitRequest);
        orderRequest.purchaseUnits(purchaseUnitRequests);
        return orderRequest;
    }

    /**
     * 创建订单的方法
     * @throws
     */
    public String createOrder(String clientId,String clientSecret,String mode,String name,String ordersNo,String payMoney,String successUrl,String cancelUrl,
                              String payWay) throws IOException {
        OrdersCreateRequest request = new OrdersCreateRequest();
        request.header("prefer","return=representation");
        request.requestBody(buildRequestBody(name,ordersNo,payMoney,successUrl,cancelUrl));
        PayPalClient payPalClient = new PayPalClient();
        HttpResponse<Order> response = null;
        try {
            response = payPalClient.client(mode, clientId, clientSecret).execute(request);
        } catch (IOException e1) {
            try {
                log.error("第1次调用paypal订单创建失败");
                response = payPalClient.client(mode, clientId, clientSecret).execute(request);
            } catch (Exception e) {
                try {
                    log.error("第2次调用paypal订单创建失败");
                    response = payPalClient.client(mode, clientId, clientSecret).execute(request);
                } catch (Exception e2) {
                    log.error("第3次调用paypal订单创建失败，失败原因：{}", e2.getMessage());
                }
            }
        }
        String approve = "";
        if (response.statusCode() == 201) {
            log.info("Status Code = {}, Status = {}, OrderID = {}, Intent = {}", response.statusCode(), response.result().status(), response.result().id(), response.result().checkoutPaymentIntent());
            for (LinkDescription link : response.result().links()) {
                log.info("Links-{}: {}    \tCall Type: {}", link.rel(), link.href(), link.method());
                if(link.rel().equals("approve")) {
                    approve = link.href();
                }
            }

            String totalAmount = response.result().purchaseUnits().get(0).amountWithBreakdown().currencyCode() + ":" + response.result().purchaseUnits().get(0).amountWithBreakdown().value();
            log.info("Total Amount: {}", totalAmount);
            String json= new JSONObject(new Json().serialize(response.result())).toString(4);
            log.info("createOrder response body: {}", json);
            if(payWay.equals("1")){
                return approve;
            }else{
                return response.result().id();
            }
        }
        return approve;
    }



}
