package com.sqx.modules.pay.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.pay.service.WxRiderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;

/**
 * @author fang
 * @date 2020/2/26
 */
@RestController
@Api(value = "跑腿师傅微信支付", tags = {"跑腿师傅微信支付"})
@RequestMapping("/app/wxPayRider")
@Slf4j
public class ApiWeiXinRiderPayController {

    @Autowired
    private WxRiderService wxService;


    @Login
    @ApiOperation("微信app充值")
    @PostMapping("/payAppMoney")
    public Result payAppMoney(@RequestAttribute("userId") Long userId, BigDecimal money) throws Exception {
        return wxService.payMoney(userId,money,1);
    }

    @Login
    @ApiOperation("微信app支付订单")
    @PostMapping("/payAppOrder")
    public Result payAppOrder(@RequestAttribute("userId") Long userId, String indentNumber) throws Exception {
        return wxService.payOrder(userId,indentNumber,1);
    }


    @Login
    @ApiOperation("微信小程序充值")
    @PostMapping("/wxPayJsApiMoney")
    public Result wxPayJsApiMoney(@RequestAttribute("userId") Long userId, BigDecimal money) throws Exception {
        return wxService.payMoney(userId,money,3);
    }

    @Login
    @ApiOperation("微信小程序支付订单")
    @PostMapping("/wxPayJsApiOrder")
    public Result wxPayJsApiOrder(@RequestAttribute("userId") Long userId, String indentNumber) throws Exception {
        return wxService.payOrder(userId,indentNumber,3);
    }

    @Login
    @ApiOperation("微信公众号充值")
    @PostMapping("/wxPayMpMoney")
    public Result wxPayMpMoney(@RequestAttribute("userId") Long userId, BigDecimal money) throws Exception {
        return wxService.payMoney(userId,money,2);
    }

    @Login
    @ApiOperation("微信公众号支付订单")
    @PostMapping("/wxPayMpOrder")
    public Result wxPayMpOrder(@RequestAttribute("userId") Long userId, String indentNumber) throws Exception {
        return wxService.payOrder(userId,indentNumber,2);
    }


    @PostMapping("/notify")
    @ApiOperation("微信回调")
    public String wxPayNotify(HttpServletRequest request) {
        String resXml = "";
        try {
            InputStream inputStream = request.getInputStream();
            //将InputStream转换成xmlString
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                log.info(e.getMessage());
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            resXml = sb.toString();
            String result = wxService.payBack(resXml,1);
            log.info("成功");
            log.info(result);

            return result;
        } catch (Exception e) {
            log.info("微信手机支付失败:" + e.getMessage());
            String result = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            log.info("失败");
            log.info(result);
            return result;
        }
    }

    @PostMapping("/notifyJsApi")
    @ApiOperation("微信回调")
    public String notifyJsApi(HttpServletRequest request) {
        String resXml = "";
        try {
            InputStream inputStream = request.getInputStream();
            //将InputStream转换成xmlString
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                log.info(e.getMessage());
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            resXml = sb.toString();
            String result = wxService.payBack(resXml,3);
            log.info("成功");
            log.info(result);
            return result;
        } catch (Exception e) {
            log.info("微信手机支付失败:" + e.getMessage());
            String result = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            log.info("失败");
            log.info(result);
            return result;
        }
    }

    @PostMapping("/notifyMp")
    @ApiOperation("微信回调")
    public String notifyMp(HttpServletRequest request) {
        String resXml = "";
        try {
            InputStream inputStream = request.getInputStream();
            //将InputStream转换成xmlString
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                log.info(e.getMessage());
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            resXml = sb.toString();
            String result = wxService.payBack(resXml,2);
            log.info("成功");
            log.info(result);
            return result;
        } catch (Exception e) {
            log.info("微信手机支付失败:" + e.getMessage());
            String result = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            log.info("失败");
            log.info(result);
            return result;
        }
    }

}
