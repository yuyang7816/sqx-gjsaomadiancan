package com.sqx.modules.iPay.utils;

import com.paypal.http.HttpResponse;
import com.paypal.http.serializer.Json;
import com.paypal.orders.*;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import java.io.IOException;

@Slf4j
public class CaptureOrder extends PayPalClient {


    public OrderRequest buildRequestBody() {
        return new OrderRequest();
    }

    /**
     * 用户授权支付成功，进行扣款操作
     */
    public HttpResponse<Order> captureOrder(String orderId,String clientId,String clientSecret,String mode) throws IOException {
        OrdersCaptureRequest request = new OrdersCaptureRequest(orderId);
        request.requestBody(new OrderRequest());
        PayPalClient payPalClient = new PayPalClient();
        HttpResponse<Order> response = null;
        try {
            response = payPalClient.client(mode, clientId, clientSecret).execute(request);
        } catch (IOException e1) {
            try {
                log.error("第1次调用paypal扣款失败");
                response = payPalClient.client(mode, clientId, clientSecret).execute(request);
            } catch (Exception e) {
                try {
                    log.error("第2次调用paypal扣款失败");
                    response = payPalClient.client(mode, clientId, clientSecret).execute(request);
                } catch (Exception e2) {
                    log.error("第3次调用paypal扣款失败，失败原因 {}", e2.getMessage() );
                }
            }
        }
          log.info("Status Code = {}, Status = {}, OrderID = {}", response.statusCode(), response.result().status(), response.result().id());
        for (LinkDescription link : response.result().links()) {
            log.info("Links-{}: {}    \tCall Type: {}", link.rel(), link.href(), link.method());
        }
        Payer buyer = response.result().payer();
        log.info("Buyer Email Address: {}", buyer.email());
        log.info("Buyer Name: {} {}", buyer.name().givenName(), buyer.name().surname());
        String json = new JSONObject(new Json().serialize(response.result())).toString(4);
        log.info("captureOrder response body: {}", json);
        return response;
    }



}
