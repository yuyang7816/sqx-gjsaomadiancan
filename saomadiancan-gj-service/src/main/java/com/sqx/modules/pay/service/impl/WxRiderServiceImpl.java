package com.sqx.modules.pay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.pay.config.WXConfig;
import com.sqx.modules.pay.dao.PayDetailsDao;
import com.sqx.modules.pay.entity.PayDetails;
import com.sqx.modules.pay.service.WxRiderService;
import com.sqx.modules.utils.AmountCalUtils;
import com.sqx.modules.utils.MD5Util;
import com.sqx.modules.utils.VerifyIdCardUtils;
import com.sqx.modules.utils.WXConfigUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fang
 * @date 2020/2/26
 */
@Service
@Slf4j
public class WxRiderServiceImpl implements WxRiderService {
    private static final String SPBILL_CREATE_IP = "127.0.0.1";
    private static final String TRADE_TYPE_APP = "APP";
    private static final String TRADE_TYPE_NATIVE = "NATIVE";
    private static final String TRADE_TYPE_JSAPI = "JSAPI";

    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private UserService userService;
    @Autowired
    private PayDetailsDao payDetailsDao;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @Override
    public Result payMoney(Long userId, BigDecimal money, Integer type) throws Exception {
        String generalOrder = getGeneralOrder();
        PayDetails payDetails = new PayDetails();
        payDetails.setState(0);
        payDetails.setCreateTime(sdf.format(new Date()));
        payDetails.setOrderId(generalOrder);
        payDetails.setUserId(userId);
        payDetails.setMoney(money.doubleValue());
        payDetails.setClassify(type);
        payDetails.setType(3);
        payDetailsDao.insert(payDetails);
        return pay(money.doubleValue(), type, userId, generalOrder,1);

    }

    @Override
    public Result payOrder(Long userId, String indentNumber, Integer type) throws Exception {
        /*Indent indent = indentService.findIndentByIndentNumber(indentNumber);
        if(indent==null || !"0".equals(indent.getIndentState())){
            return Result.error("订单不存在，请刷新后重试！");
        }
        PayDetails payDetails = new PayDetails();
        payDetails.setState(0);
        payDetails.setCreateTime(sdf.format(new Date()));
        payDetails.setOrderId(indent.getIndentNumber());
        payDetails.setUserId(userId);
        payDetails.setMoney(indent.getIndentMoney().doubleValue());
        payDetails.setClassify(type);
        payDetails.setType(3);
        payDetailsDao.insert(payDetails);
        return pay(indent.getIndentMoney().doubleValue(), type, indent.getUserId(), indent.getIndentNumber(),2);*/
        return null;
    }


    /**
     * 微信支付订单生成
     *
     * @param moneys     支付金额 带小数点
     * @param type       类型 1app  2 二维码支付  3小程序 公众号支付
     * @param userId     用户id
     * @param outTradeNo 单号
     * @return
     * @throws Exception
     */
    private Result pay(Double moneys, Integer type, Long userId, String outTradeNo, Integer classify) throws Exception {
        //h5服务域名配置
        CommonInfo oneu = commonInfoService.findOne(19);
        String url;
        if (type == 3) {
            url = oneu.getValue() + "/sqx_fast/app/wxPayRider/notifyJsApi";
        } else if (type == 2) {
            url = oneu.getValue() + "/sqx_fast/app/wxPayRider/notifyMp";
        } else {
            url = oneu.getValue() + "/sqx_fast/app/wxPayRider/notify";
        }
        String currentTimeMillis=(System.currentTimeMillis() / 1000)+"";
        //后台服务名称
        CommonInfo one = commonInfoService.findOne(12);
        log.info("回调地址：" + url);
        Double mul = AmountCalUtils.mul(moneys, 100);
        String money = String.valueOf(mul.intValue());
        String generateNonceStr = WXPayUtil.generateNonceStr();
        WXConfig config = new WXConfig();
        //微信小程序APPID        微信公众号APPID
        if(type==1){
            config.setAppId(commonInfoService.findOne(74).getValue());
        }else if(type==2){
            config.setAppId(commonInfoService.findOne(5).getValue());
        }else{
            config.setAppId(commonInfoService.findOne(248).getValue());
        }
        //微信商户key
        config.setKey(commonInfoService.findOne(250).getValue());
        //微信商户号mchId
        config.setMchId(commonInfoService.findOne(251).getValue());
        WXPay wxpay = new WXPay(config);
        Map<String, String> data = new HashMap<>();
        //微信对应appid 小程序  公众号  app id
        data.put("appid", config.getAppID());
        //商户号
        data.put("mch_id", config.getMchID());
        //订单号
        data.put("nonce_str", generateNonceStr);
        String body = one.getValue();
        //订单内容
        data.put("body", body);
        //生成商户订单号，不可重复
        data.put("out_trade_no", outTradeNo);
        data.put("total_fee", money);
        //自己的服务器IP地址
        data.put("spbill_create_ip", SPBILL_CREATE_IP);
        //异步通知地址（请注意必须是外网）
        data.put("notify_url", url);
        //交易类型
        data.put("trade_type", type == 1 ? TRADE_TYPE_APP : TRADE_TYPE_JSAPI );
        //附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
        data.put("attach", "");
        data.put("sign", WXPayUtil.generateSignature(data, config.getKey(),
                WXPayConstants.SignType.MD5));
        if(type==3 || type == 2){
            UserEntity userEntity = userService.queryByUserId(userId);
            if(type==2){
                data.put("openid", userEntity.getWxOpenId());
            }else{
                data.put("openid", userEntity.getOpenId());
            }
        }
        //使用官方API请求预付订单
        Map<String, String> response = wxpay.unifiedOrder(data);
        for (String key : response.keySet()) {
            log.info("微信支付订单微信返回参数：keys:" + key + "    value:" + response.get(key).toString());
        }
        if ("SUCCESS".equals(response.get("return_code"))) {//主要返回以下5个参数
            if (type == 1) {
                Map<String, String> param = new HashMap<>();
                param.put("appid", config.getAppID());
                param.put("partnerid", response.get("mch_id"));
                param.put("prepayid", response.get("prepay_id"));
                param.put("package", "Sign=WXPay");
                param.put("noncestr", generateNonceStr);
                param.put("timestamp", currentTimeMillis);
                param.put("sign", WXPayUtil.generateSignature(param, config.getKey(),
                        WXPayConstants.SignType.MD5));
                param.put("outtradeno", outTradeNo);
                return Result.success().put("data", param);
            } else {
                Map<String, String> param = new HashMap<>();
                param.put("appid", config.getAppID());
                param.put("partnerid", response.get("mch_id"));
                param.put("prepayid", response.get("prepay_id"));
                param.put("noncestr", generateNonceStr);
                param.put("timestamp", currentTimeMillis);
                    /*param.put("sign", WXPayUtil.generateSignature(param, config.getKey(),
                            WXPayConstants.SignType.MD5));*/
                String stringSignTemp = "appId=" + config.getAppID() + "&nonceStr=" + generateNonceStr + "&package=prepay_id=" + response.get("prepay_id") + "&signType=MD5&timeStamp=" + currentTimeMillis + "" + "&key=" + config.getKey();
                String sign = MD5Util.md5Encrypt32Upper(stringSignTemp).toUpperCase();
                param.put("sign", sign);
                param.put("outtradeno", outTradeNo);
                param.put("package", "prepay_id=" + response.get("prepay_id"));//给前端返回的值
                param.put("mweb_url", response.get("mweb_url"));
                param.put("trade_type", response.get("trade_type"));
                param.put("return_msg", response.get("return_msg"));
                param.put("result_code", response.get("result_code"));
                param.put("signType", "MD5");
                return Result.success().put("data", param);
            }
        }
        return Result.error("获取订单失败");
    }

    @Override
    public String payBack(String resXml, Integer type) {
        WXConfig config = null;
        try {
            config = new WXConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.error("进入回调了！！！");
        if(type==1){
            config.setAppId(commonInfoService.findOne(74).getValue());
        }else if(type==2){
            config.setAppId(commonInfoService.findOne(5).getValue());
        }else{
            config.setAppId(commonInfoService.findOne(248).getValue());
        }
        config.setKey(commonInfoService.findOne(250).getValue());
        config.setMchId(commonInfoService.findOne(251).getValue());
        WXPay wxpay = new WXPay(config);
        String xmlBack = "";
        Map<String, String> notifyMap = null;
        try {
            notifyMap = WXPayUtil.xmlToMap(resXml);         // 调用官方SDK转换成map类型数据
            if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {//验证签名是否有效，有效则进一步处理
                log.error("验证成功！！！");
                String return_code = notifyMap.get("return_code");//状态
                String out_trade_no = notifyMap.get("out_trade_no");//商户订单号
                if (return_code.equals("SUCCESS")) {
                    log.error("验证成功222！！！");
                    if (out_trade_no != null) {
                        // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户的订单状态从退款改成支付成功
                        // 注意特殊情况：微信服务端同样的通知可能会多次发送给商户系统，所以数据持久化之前需要检查是否已经处理过了，处理了直接返回成功标志
                        //业务数据持久化
                        log.error("订单号！！！" + out_trade_no);
                        PayDetails payDetails = payDetailsDao.selectByOrderId(out_trade_no);
                        //设置查询条件
                        if (payDetails.getState() == 0) {
                            /*payDetailsDao.updateState(payDetails.getId(), 1, sdf.format(new Date()), "");
                            //调用处理接口
                            if(payDetails.getType()==1){
                                //充值金额
                                payDetails.setClassify(1);//分类：1微信  2支付宝
                                String date = new SimpleDateFormat("yyyy-MM-dd :HH:mm:ss").format(new Date());
                                payDetails.setType(1);
                                userInfoDao.topUpMoney(payDetails.getClassify(), out_trade_no,payDetails.getMoney(),payDetails.getUserId(),payDetails.getState(),date,date,payDetails.getType());
                            }else if(payDetails.getType()==2){
                                //订单支付
                                Indent indentByIndentNumber = indentService.findIndentByIndentNumber(payDetails.getOrderId());
                                indentByIndentNumber.setIndentState("2");
                                indentService.updateById(indentByIndentNumber);
                            }else{
                                String remark = payDetails.getRemark();
                                JSONObject user = JSONObject.parseObject(remark);

                                String format = new SimpleDateFormat("yyyy-MM-dd :HH:mm:ss").format(new Date());
                                userInfoDao.certification(Long.parseLong(user.getString("userId")),user.getString("phone"),
                                        user.getString("userName"), user.getString("identityCardNumber"), user.getString("identityCardFront"),
                                        user.getString("identityCardVerso"),user.getString("selfIdentityCard"), format,payDetails.getOrderId(),
                                        user.getBigDecimal("cashDeposit"));

                            }*/

                        }
                        System.err.println("微信手机支付回调成功订单号:" + out_trade_no + "");
                        xmlBack = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    } else {
                        System.err.println("微信手机支付回调成功订单号:" + out_trade_no + "");
                        xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                    }
                } else {
                }
                return xmlBack;
            } else {
                // 签名错误，如果数据里没有sign字段，也认为是签名错误
                System.err.println("手机支付回调通知签名错误");
                xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                return xmlBack;
            }
        } catch (Exception e) {
            log.error("骑手端微信回调异常"+e.getMessage(),e);
            System.err.println("手机支付回调通知失败" + e);
            xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        return xmlBack;
    }


    public String getGeneralOrder() {
        Date date = new Date();
        String newString = String.format("%0" + 4 + "d", (int) ((Math.random() * 9 + 1) * 1000));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(date);
        return format + newString;
    }



    @Override
    public boolean wxRefund(PayDetails payDetails){
        WXConfigUtil config = null;
        try {
            config = new WXConfigUtil();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int commInfoId = 0;
        Integer payWay = payDetails.getType(); //支付方式（1app微信 2微信公众号 3微信小程序 4app支付宝 5H5支付宝 6零钱）
        switch (payWay){
            case 1 : commInfoId = 74; break; //appId
            case 2 : commInfoId = 5; break; //公众号id
            case 3 : commInfoId = 248; break; //小程序id
        }
        config.setAppId(commonInfoService.findOne(commInfoId).getValue());
        config.setKey(commonInfoService.findOne(250).getValue());
        config.setMchId(commonInfoService.findOne(251).getValue());
        WXPay wxpay = new WXPay(config);
        Map<String, String> data = new HashMap<>();
        data.put("appid", config.getAppID());
        data.put("mch_id", config.getMchID());
        data.put("nonce_str", WXPayUtil.generateNonceStr());
        try{
            data.put("sign", WXPayUtil.generateSignature(data, config.getKey(), WXPayConstants.SignType.MD5));
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        data.put("out_trade_no", payDetails.getOrderId()); //订单号,支付单号一致
        data.put("out_refund_no", payDetails.getOrderId()); //退款单号，同一笔用不同的退款单号
        data.put("total_fee",String.valueOf((BigDecimal.valueOf(payDetails.getMoney()).multiply(BigDecimal.valueOf(100))).intValue())); //1块等于微信支付传入100);
        data.put("refund_fee",String.valueOf((BigDecimal.valueOf(payDetails.getMoney()).multiply(BigDecimal.valueOf(100))).intValue())); //1块等于微信支付传入100);
        //使用官方API退款
        try{
            Map<String, String> response = wxpay.refund(data);
            if ("SUCCESS".equals(response.get("return_code"))) {//主要返回以下5个参数
                System.err.println("退款成功");
                return true;
            }else {
                return false;
            }
        }catch (Exception e){
            log.error("返回");
            e.printStackTrace();
            log.error("微信退款失败！，订单号"+payDetails.getOrderId()+"，失败原因："+e.getMessage(),e);
            return false;
        }
    }
}