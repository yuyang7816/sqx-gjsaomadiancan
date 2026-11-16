package com.sqx.modules.pay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.sqx.common.exception.SqxException;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.address.dao.AddressDao;
import com.sqx.modules.address.entity.Address;
import com.sqx.modules.app.dao.UserDao;
import com.sqx.modules.app.dao.UserMoneyDetailsDao;
import com.sqx.modules.app.dao.UserVipDao;
import com.sqx.modules.app.dao.VipDetailsDao;
import com.sqx.modules.app.entity.*;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.coupon.dao.TbCouponUserDao;
import com.sqx.modules.coupon.entity.TbCouponUser;
import com.sqx.modules.coupon.service.TbCouponUserService;
import com.sqx.modules.goods.dao.GoodsShopDao;
import com.sqx.modules.goods.dao.GoodsShopRelevancyDao;
import com.sqx.modules.goods.dao.GoodsSkuDao;
import com.sqx.modules.goods.entity.Goods;
import com.sqx.modules.goods.entity.GoodsShop;
import com.sqx.modules.goods.entity.GoodsShopRelevancy;
import com.sqx.modules.goods.service.GoodsService;
import com.sqx.modules.message.dao.MessageInfoDao;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.order.dao.AppOrderDao;
import com.sqx.modules.order.dao.OrderGoodsDao;
import com.sqx.modules.order.entity.OrderGoods;
import com.sqx.modules.order.entity.TbOrder;
import com.sqx.modules.order.service.AppOrderService;
import com.sqx.modules.pay.config.WXConfig;
import com.sqx.modules.pay.dao.PayDetailsDao;
import com.sqx.modules.pay.entity.PayDetails;
import com.sqx.modules.pay.service.WxService;
import com.sqx.modules.utils.AmountCalUtils;
import com.sqx.modules.utils.MD5Util;
import com.sqx.modules.utils.SenInfoCheckUtil;
import com.sqx.modules.utils.WXConfigUtil;
import com.sqx.modules.utils.fieYun.FeiYunUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author fang
 * @date 2020/2/26
 */
@Service
@Slf4j
public class WxServiceImpl implements WxService {
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
    @Autowired
    private UserDao userDao;
    @Autowired
    private AppOrderService appOrderService;
    @Autowired
    private AppOrderDao appOrderDao;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private TbCouponUserService tbCouponUserService;
    @Autowired
    private UserMoneyDetailsDao userMoneyDetailsDao;
    @Autowired
    private GoodsShopDao goodsShopDao;
    @Autowired
    private AddressDao addressDao;
    @Autowired
    private GoodsSkuDao goodsSkuDao;
    @Autowired
    private OrderGoodsDao orderGoodsDao;
    @Autowired
    private TbCouponUserDao tbCouponUserDao;
    @Autowired
    private MessageInfoDao messageInfoDao;
    @Autowired
    private GoodsShopRelevancyDao goodsShopRelevancyDao;
    @Autowired
    private VipDetailsDao vipDetailsDao;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private UserVipDao userVipDao;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(true);

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Result balanceOrder(Long userId, Long parentId, Integer orderType, Long addressId, String orderRemarks) {
        reentrantReadWriteLock.writeLock().lock();
        try {
            return balanceOrders(userId, parentId, orderType, addressId, orderRemarks);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("下单异常：" + e.getMessage(), e);
        } finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
        return Result.error("系统繁忙，请稍后再试！");
    }

    @Transactional
    public Result balanceOrders(Long userId, Long parentId, Integer orderType, Long addressId, String orderRemarks) {
        TbOrder tbOrder2 = appOrderDao.selectById(parentId);
        if (tbOrder2.getIsPay() != 0) {
            return Result.error("当前订单已支付");
        }
        tbOrder2.setOrderType(orderType);
        tbOrder2.setOrderRemarks(orderRemarks);
        //判断商品数量不能超过库存
        Long orderId1 = tbOrder2.getOrderId();
        List<OrderGoods> orderGoodsList1 = orderGoodsDao.selectList(new QueryWrapper<OrderGoods>().eq("order_id", orderId1));
        for (int b = 0; b < orderGoodsList1.size(); b++) {
            Long goodsId = orderGoodsList1.get(b).getGoodsId();
            GoodsShopRelevancy goodsShopRelevancy = goodsShopRelevancyDao.selectOne(new QueryWrapper<GoodsShopRelevancy>()
                    .eq("goods_id", goodsId).eq("shop_id", tbOrder2.getShopId()));
            int i = goodsShopRelevancyDao.selectGoodsCount(orderId1, goodsId);
            if (goodsShopRelevancy.getInventory() < i) {
                return Result.error("库存不足");
            }
            Goods goods = goodsService.getById(goodsId);
            if (Objects.isNull(goods)) {
                throw new SqxException("商品信息不存在");
            }
            if (goodsShopRelevancy.getStatus() == 1) {
                return Result.error("商品《" + goods.getGoodsName() + "》已下架,请删除后重新结算");
            }
            if (goods.getStatus() == 1) {
                return Result.error("商品《" + goods.getGoodsName() + "》已下架,请删除后重新结算");
            }
        }
        List<TbOrder> tbOrderList = appOrderService.selectOrderByParentId(parentId);
        BigDecimal payMoney = new BigDecimal(0);
        BigDecimal goodsPack = new BigDecimal(0);
        for (int i = 0; i < tbOrderList.size(); i++) {
            List<OrderGoods> orderGoodsList = orderGoodsDao.selectList(new QueryWrapper<OrderGoods>().eq("order_id", tbOrderList.get(i).getOrderId()));
            for (OrderGoods goods : orderGoodsList) {
                BigDecimal goodsMoney = goods.getGoodsPrice().multiply(new BigDecimal(goods.getGoodsNum()));
                BigDecimal goodsPackMoney = goods.getGoodsPack().multiply(new BigDecimal(goods.getGoodsNum()));
                goodsPack = goodsPack.add(goodsPackMoney);
                payMoney = payMoney.add(goodsMoney.add(goodsPackMoney));

            }
        }
        int errandUser = 0;
        CommonInfo one = commonInfoService.findOne(123);
        Double value = Double.valueOf(one.getValue());
        CommonInfo one1 = commonInfoService.findOne(124);
        Double value1 = Double.valueOf(one1.getValue());
        if (orderType == 2) {
            if (payMoney.doubleValue() < value1) {
                //加跑腿费
                payMoney = payMoney.add(BigDecimal.valueOf(value));
                errandUser = 2;
            } else {
                errandUser = 1;
            }
            tbOrder2.setErrandMoney(BigDecimal.valueOf(value));
        }
        if (tbOrder2.getCouponId() != null) {
            TbCouponUser tbCouponUser = tbCouponUserDao.selectById(tbOrder2.getCouponId());
            //计算总支付金额
            if (!tbCouponUser.getUserId().equals(userId)) {
                return Result.error("优惠券与登录用户不匹配");
            }
            if (tbCouponUser.getStatus() != 0) {
                return Result.error("该优惠券已失效");
            }
            if (tbCouponUser.getMinMoney().doubleValue() > payMoney.doubleValue()) {
                return Result.error("订单金额不足，无法使用该优惠券");
            }
            payMoney = payMoney.subtract(tbCouponUser.getMoney());
            //如果使用红包后，订单价格小于0，则改为0.01元
            if (payMoney.doubleValue() <= 0) {
                payMoney = new BigDecimal(0.01);
            }
        }
        if (orderType == 2 && addressId == null) {
            return Result.error("请填写地址地址！");
        }

        /*if(!tbOrderList.get(0).getOrderId().equals(tbOrderList.get(0).getParentId())){
            return Result.error("您不是订单发起人，无法支付该订单！");
        }*/
        UserEntity userEntity1 = userService.selectUserById(userId);
        if (userEntity1 != null && userEntity1.getMember() != null && userEntity1.getMember() == 1) {
            String value2 = commonInfoService.findOne(329).getValue();
            payMoney = payMoney.multiply(new BigDecimal(value2));
        }
        UserMoney userMoney = userMoneyService.selectUserMoneyByUserId(userId);
        if (payMoney.doubleValue() > userMoney.getMoney().doubleValue()) {
            return Result.error("您的余额不足，请充值后再支付！");
        }
        tbOrder2.setPayMoney(payMoney);
        userMoneyService.updateMoney(2, userId, tbOrder2.getPayMoney().doubleValue());
        //支付成功，增加该商品销量，减少该商品库存
        Long shopId1 = tbOrder2.getShopId();
        Long orderId = tbOrder2.getOrderId();
        List<OrderGoods> orderGoodsList = orderGoodsDao.selectList(new QueryWrapper<OrderGoods>().eq("order_id", orderId));
        for (int a = 0; a < orderGoodsList.size(); a++) {
            Integer goodsNum = orderGoodsList.get(a).getGoodsNum();
            Long goodsId = orderGoodsList.get(a).getGoodsId();
            GoodsShopRelevancy goodsShopRelevancy = goodsShopRelevancyDao.selectOne(new QueryWrapper<GoodsShopRelevancy>()
                    .eq("shop_id", shopId1).eq("goods_id", goodsId));
            GoodsShopRelevancy goodsShopRelevancy1 = new GoodsShopRelevancy();
            goodsShopRelevancy1.setId(goodsShopRelevancy.getId());
            goodsShopRelevancy1.setSales(goodsShopRelevancy.getSales() + goodsNum);//加销量
            goodsShopRelevancy1.setInventory(goodsShopRelevancy.getInventory() - goodsNum);//减库存
            goodsShopRelevancyDao.updateById(goodsShopRelevancy1);
        }
        //支付成功，修改订单状态
        TbOrder tbOrder1 = new TbOrder();
        tbOrder1.setOrderId(tbOrder2.getOrderId());
        Integer i = (int) ((Math.random() * 9 + 1) * 10000);
        tbOrder1.setOrderCode(i.toString());
        tbOrder1.setIsPay(1);
        tbOrder1.setOrderRemarks(orderRemarks);
        tbOrder1.setErrandUser(errandUser);
        tbOrder1.setErrandMoney(tbOrder2.getErrandMoney());
        tbOrder1.setPackMoney(goodsPack);
        tbOrder1.setOrderType(orderType);
        tbOrder1.setStatus(6);
        tbOrder1.setPayMoney(payMoney);
        tbOrder1.setPayType(2);
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        tbOrder1.setPayTime(format);
        if (orderType == 2) {
            //添加地址
            Address address = addressDao.selectById(addressId);
            String addressJson = JSONObject.toJSONString(address);
            tbOrder1.setAddress(addressJson);
        }

        appOrderDao.updateById(tbOrder1);

        OrderGoods orderGoods = new OrderGoods();
        orderGoods.setStatus(1);
        orderGoodsDao.update(orderGoods, new QueryWrapper<OrderGoods>().eq("order_id", tbOrder1.getOrderId()));
        //将优惠券改为已使用状态
        if (tbOrder2.getCouponId() != null) {
            TbCouponUser tbCouponUser = new TbCouponUser();
            tbCouponUser.setId(tbOrder2.getCouponId());
            tbCouponUser.setStatus(1);
            tbCouponUserService.updateById(tbCouponUser);
        }
        //添加钱包消费记录
        UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
        userMoneyDetails.setUserId(userId);
        userMoneyDetails.setTitle("余额支付");
        userMoneyDetails.setType(2);
        userMoneyDetails.setState(2);
        userMoneyDetails.setMoney(tbOrder2.getPayMoney());
        String money = String.format("%.2f", tbOrder2.getPayMoney());
        userMoneyDetails.setContent("使用余额支付：" + money + "元");
        userMoneyDetails.setCreateTime(sdf.format(new Date()));
        userMoneyDetailsService.saveI18n(userMoneyDetails, "55001", null, "55002", new String[]{money});

        //设置小程序消息推送
        CommonInfo one2 = commonInfoService.findOne(269);
        List<String> msgList = new ArrayList<>();
        MessageInfo messageInfo = new MessageInfo();
        MessageInfo messageInfo1 = new MessageInfo();
        Long shopId = tbOrder2.getShopId();
        GoodsShop goodsShop = goodsShopDao.selectById(shopId);
        String shopName = goodsShop.getShopName();
        String orderNum1 = tbOrder2.getOrderNumber();
        msgList.add(orderNum1);
        msgList.add(shopName);
        msgList.add(format);

        if (orderType == 3) {
            appOrderService.accomplishOrder(userId, tbOrder1.getOrderId());
            //添加消息记录
            UserEntity userEntity = userDao.selectById(userId);
            messageInfo.setTitle("结账");
            messageInfo.setContent("支付金额" + tbOrder1.getPayMoney() + "元");
            messageInfo.setCreateAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            messageInfo.setUserId(userId.toString());
            messageInfo.setUserName(userEntity.getUserName());
            messageInfoDao.insert(messageInfo);
            msgList.add("结账成功");
            SenInfoCheckUtil.sendMsg(userEntity.getOpenId(), one2.getValue(), msgList, 1);
            if (StringUtils.isNotEmpty(goodsShop.getSnCode())) {
                TbOrder tbOrder = appOrderService.selectOrderByIds(tbOrder1.getOrderId());
                FeiYunUtils.print(goodsShop.getSnCode(), null, tbOrder);
            }
        } else {
            //添加消息记录
            UserEntity userEntity = userDao.selectById(userId);
            messageInfo.setTitle("下单");
            messageInfo.setContent("恭喜您，下单成功！");
            messageInfo.setCreateAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            messageInfo.setUserId(tbOrder2.getUserId().toString());
            messageInfo.setUserName(userEntity.getUserName());
            messageInfoDao.insert(messageInfo);
            //店铺端添加消息记录
            messageInfo1.setTitle("有新订单");
            messageInfo1.setCreateAt(format);
            messageInfo1.setContent("有新订单，请及时处理！");
            messageInfo1.setShopId(tbOrder2.getShopId());
            messageInfo1.setReadFlag(2);
            messageInfoDao.insert(messageInfo1);
            //设置小程序消息推送
            msgList.add("下单成功");
            SenInfoCheckUtil.sendMsg(userEntity.getOpenId(), one2.getValue(), msgList, 1);
            if (StringUtils.isNotEmpty(goodsShop.getSnCode())) {
                TbOrder tbOrder = appOrderService.selectOrderByIds(tbOrder1.getOrderId());
                FeiYunUtils.print(goodsShop.getSnCode(), null, tbOrder);
            }
        }

        return Result.success();
    }

    @Override
    public Result payMoney(Long userId, BigDecimal money, Integer type) throws Exception {
        String generalOrder = getGeneralOrder();
        return pay(money.doubleValue(), type, userId, generalOrder, 2, null, null);
    }

    @Override
    public Result payVip(Long userId, Long vipTypeId, Integer type) throws Exception {
        String generalOrder = getGeneralOrder();
        VipDetails vipDetails = vipDetailsDao.selectOne(new QueryWrapper<VipDetails>().eq("id", vipTypeId));
        return pay(vipDetails.getMoney().doubleValue(), type, userId, generalOrder, 1, String.valueOf(vipTypeId), null);
    }

    @Override
    public Result wxPayMoney(Long userId, Long shopId, BigDecimal money, Integer type) throws Exception {
        String generalOrder = getGeneralOrder();
        return pay(money.doubleValue(), type, userId, generalOrder, 5, null, shopId);
    }


    @Override
    public Result payOrder(Long userId, Long parentId, Integer type, Long addressId, Integer orderType, String orderRemarks) throws Exception {
        TbOrder tbOrder2 = appOrderDao.selectById(parentId);
        if (tbOrder2.getIsPay() != 0) {
            return Result.error("当前订单已支付");
        }
        //判断商品数量不能超过库存
        Long orderId1 = tbOrder2.getOrderId();
        List<OrderGoods> orderGoodsList1 = orderGoodsDao.selectList(new QueryWrapper<OrderGoods>().eq("order_id", orderId1));
        for (int b = 0; b < orderGoodsList1.size(); b++) {
            Long goodsId = orderGoodsList1.get(b).getGoodsId();
            GoodsShopRelevancy goodsShopRelevancy = goodsShopRelevancyDao.selectOne(new QueryWrapper<GoodsShopRelevancy>()
                    .eq("goods_id", goodsId).eq("shop_id", tbOrder2.getShopId()));
            int i = goodsShopRelevancyDao.selectGoodsCount(orderId1, goodsId);
            if (goodsShopRelevancy.getInventory() < i) {
                return Result.error("库存不足");
            }
            Goods goods = goodsService.getById(goodsShopRelevancy.getGoodsId());
            if (goodsShopRelevancy.getStatus() == 1) {
                return Result.error("商品《" + goods.getGoodsName() + "》已下架,请删除后重新结算");
            }
            if (goods.getStatus() == 1) {
                return Result.error("商品《" + goods.getGoodsName() + "》已下架,请删除后重新结算");
            }
        }
        List<TbOrder> tbOrder = appOrderService.selectOrderByParentId(parentId);
        BigDecimal payMoney;
        BigDecimal goodsAllMoney = new BigDecimal(0);
        BigDecimal goodsAllPack = new BigDecimal(0);
        for (int i = 0; i < tbOrder.size(); i++) {
            List<OrderGoods> orderGoodsList = orderGoodsDao.selectList(new QueryWrapper<OrderGoods>().eq("order_id", tbOrder.get(i).getOrderId()));
            for (OrderGoods goods : orderGoodsList) {
                BigDecimal goodsMoney = goods.getGoodsPrice().multiply(new BigDecimal(goods.getGoodsNum()));
                goodsAllMoney = goodsAllMoney.add(goodsMoney);

                BigDecimal goodsPackMoney = goods.getGoodsPack().multiply(new BigDecimal(goods.getGoodsNum()));
                goodsAllPack = goodsAllPack.add(goodsPackMoney);

            }
        }
        payMoney = goodsAllMoney.add(goodsAllPack);
        int errandUser = 0;
        BigDecimal errandMoney = new BigDecimal(commonInfoService.findOne(123).getValue());
        BigDecimal noneMoney = new BigDecimal(commonInfoService.findOne(124).getValue());
        if (orderType == 2) {
            if (payMoney.compareTo(noneMoney) < 0) {
                payMoney = payMoney.add(errandMoney);
                tbOrder2.setErrandMoney(errandMoney);
                errandUser = 2;
            } else {
                errandUser = 1;
            }
            tbOrder2.setOrderRemarks(orderRemarks);
        }
        if (tbOrder2.getCouponId() != null) {
            TbCouponUser tbCouponUser = tbCouponUserDao.selectById(tbOrder2.getCouponId());
            //计算总支付金额
            if (!tbCouponUser.getUserId().equals(userId)) {
                return Result.error("优惠券与登录用户不匹配");
            }
            if (tbCouponUser.getStatus() != 0) {
                return Result.error("该优惠券已失效");
            }
            if (tbCouponUser.getMinMoney().doubleValue() > payMoney.doubleValue()) {
                return Result.error("订单金额不足，无法使用该优惠券");
            }
            payMoney = payMoney.subtract(tbCouponUser.getMoney());
            if (payMoney.doubleValue() <= 0) {
                payMoney = new BigDecimal(0.01);
            }
        }
        if (orderType == 2 && addressId == null) {
            return Result.error("请填写地址地址！");
        }

        tbOrder2.setOrderId(tbOrder.get(0).getOrderId());
        UserEntity userEntity1 = userService.selectUserById(userId);
        if (userEntity1 != null && userEntity1.getMember() != null && userEntity1.getMember() == 1) {
            String value2 = commonInfoService.findOne(329).getValue();
            payMoney = payMoney.multiply(new BigDecimal(value2));
        }
        tbOrder2.setPayMoney(payMoney);
        tbOrder2.setPackMoney(payMoney);
        tbOrder2.setOrderRemarks(orderRemarks);
        tbOrder2.setOrderType(orderType);
        tbOrder2.setPayType(1);
        tbOrder2.setErrandUser(errandUser);
        String generalOrder = getGeneralOrder();
        tbOrder2.setOrderNumber(generalOrder);
        if (orderType == 2) {
            //添加地址
            Address address = addressDao.selectById(addressId);
            String addressJson = JSONObject.toJSONString(address);
            tbOrder2.setAddress(addressJson);
        }
        appOrderDao.updateById(tbOrder2);
        if (orderType == 3) {
            return pay(payMoney.doubleValue(), type, tbOrder2.getUserId() == null ? userId : tbOrder2.getUserId(), generalOrder, 4, null, null);

        } else {
            return pay(payMoney.doubleValue(), type, tbOrder2.getUserId() == null ? userId : tbOrder2.getUserId(), generalOrder, 3, null, null);

        }

    }

    /**
     * 微信支付订单生成
     *
     * @param moneys     支付金额 带小数点
     * @param type       类型 1app  2 二维码支付  3小程序 公众号支付
     * @param userId     用户id
     * @param outTradeNo 单号
     * @param classify   支付类型  1支付订单  2充值
     * @return
     * @throws Exception
     */
    private Result pay(Double moneys, Integer type, Long userId, String outTradeNo, Integer classify, String remark, Long relationId) throws Exception {
        //h5服务域名配置
        CommonInfo oneu = commonInfoService.findOne(19);
        String url;
        if (type == 3) {
            url = oneu.getValue() + "/sqx_fast/app/wxPay/notifyJsApi";//小程序支付
        } else if (type == 2) {
            url = oneu.getValue() + "/sqx_fast/app/wxPay/notifyMp";//公众号支付
        } else {
            url = oneu.getValue() + "/sqx_fast/app/wxPay/notify";//app支付
        }
        String currentTimeMillis = (System.currentTimeMillis() / 1000) + "";
        //后台服务名称
        CommonInfo one = commonInfoService.findOne(12);
        log.info("回调地址：" + url);
        Double mul = AmountCalUtils.mul(moneys, 100);
        String money = String.valueOf(mul.intValue());
        String generateNonceStr = WXPayUtil.generateNonceStr();
        WXConfig config = new WXConfig();
        //微信小程序APPID        微信公众号APPID
        if (type == 1) {
            config.setAppId(commonInfoService.findOne(74).getValue());
        } else if (type == 2) {
            config.setAppId(commonInfoService.findOne(5).getValue());
        } else if (type == 3) {
            config.setAppId(commonInfoService.findOne(45).getValue());
        }
        //微信商户key
        config.setKey(commonInfoService.findOne(75).getValue());
        //微信商户号mchId
        config.setMchId(commonInfoService.findOne(76).getValue());
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
        data.put("trade_type", type == 1 ? TRADE_TYPE_APP : TRADE_TYPE_JSAPI);
        //附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
        data.put("attach", "");
        data.put("sign", WXPayUtil.generateSignature(data, config.getKey(),
                WXPayConstants.SignType.MD5));
        if (type == 3 || type == 2) {
            UserEntity userEntity = userService.queryByUserId(userId);
            if (type == 2) {
                data.put("openid", userEntity.getWxOpenId());
            } else {
                data.put("openid", userEntity.getOpenId());
            }
        }
        //使用官方API请求预付订单
        Map<String, String> response = wxpay.unifiedOrder(data);
        for (String key : response.keySet()) {
            log.info("微信支付订单微信返回参数：keys:" + key + "    value:" + response.get(key).toString());
        }
        if ("SUCCESS".equals(response.get("return_code"))) {//主要返回以下5个参数
            PayDetails payDetails = new PayDetails();
            payDetails.setState(0);
            payDetails.setCreateTime(sdf.format(new Date()));
            payDetails.setOrderId(outTradeNo);
            payDetails.setUserId(userId);
            payDetails.setMoney(AmountCalUtils.divide(Double.parseDouble(money), 100));
            payDetails.setClassify(classify);
            payDetails.setType(type);
            payDetails.setRemark(remark);
            if (relationId != null) {
                payDetails.setRelationId(relationId);
            }
            payDetailsDao.insert(payDetails);
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
        if (type == 1) {
            config.setAppId(commonInfoService.findOne(74).getValue());
        } else if (type == 2) {
            config.setAppId(commonInfoService.findOne(5).getValue());
        } else {
            config.setAppId(commonInfoService.findOne(45).getValue());
        }
        config.setKey(commonInfoService.findOne(75).getValue());
        config.setMchId(commonInfoService.findOne(76).getValue());
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
                            payDetailsDao.updateState(payDetails.getId(), 1, sdf.format(new Date()), "");
                            //调用处理接口
                            //业务操作
                            if (payDetails.getClassify() == 1) {
                                VipDetails vipDetails = vipDetailsDao.selectById(Long.parseLong(payDetails.getRemark()));
                                Long userId = payDetails.getUserId();
                                UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
                                userMoneyDetails.setMoney(vipDetails.getMoney());
                                userMoneyDetails.setUserId(userId);
                                UserEntity userEntity = userService.selectUserById(userId);
                                userEntity.setMember(1);

                                UserVip userVip = userVipDao.selectOne(new QueryWrapper<UserVip>().eq("user_id", userId));
                                Calendar cal = Calendar.getInstance();
                                if (userVip == null) {
                                    userVip = new UserVip();
                                    userVip.setUserId(userId);
                                    userVip.setIsVip(1L);
                                    userVip.setCreateTime(DateUtils.format(cal.getTime()));
                                } else {
                                    //如果有会员 判断会员是否过期
                                    Date date = DateUtils.stringToDate(userVip.getEndTime());
                                    if (date.getTime() > cal.getTime().getTime()) {
                                        cal.setTime(date);
                                    }
                                }

                                if ((vipDetails.getVipNameType().equals(0))) {
                                    //月
                                    userMoneyDetails.setContent("开通月卡会员");
                                    cal.add(Calendar.MONTH, 1);
                                } else if ((vipDetails.getVipNameType()).equals(1)) {
                                    //季
                                    userMoneyDetails.setContent("开通季卡会员");
                                    cal.add(Calendar.MONTH, 3);
                                } else {
                                    //年
                                    userMoneyDetails.setContent("开通年费会员");
                                    cal.add(Calendar.YEAR, 1);
                                }
                                userMoneyDetails.setTitle("开通会员");
                                userMoneyDetails.setType(2);
                                userMoneyDetails.setCreateTime(DateUtils.format(new Date()));
                                userMoneyDetailsService.save(userMoneyDetails);
                                userVip.setEndTime(DateUtils.format(cal.getTime()));
                                if (userVip.getVipId() != null) {
                                    userVipDao.updateById(userVip);
                                } else {
                                    userVipDao.insert(userVip);
                                }
                                userEntity.setEndTime(userVip.getEndTime());
                                userService.updateById(userEntity);
                            } else if (payDetails.getClassify() == 2) {
                                //充值金额
                                userMoneyService.updateMoney(1, payDetails.getUserId(), payDetails.getMoney());
                                //修改本充值记录状态
                                PayDetails payDetails1 = new PayDetails();
                                String date = new SimpleDateFormat("yyyy-MM-dd :HH:mm:ss").format(new Date());
                                payDetails1.setId(payDetails.getId());
                                payDetails1.setState(1);
                                payDetails1.setPayTime(date);
                                payDetailsDao.updateById(payDetails1);
                                //添加钱包消费记录
                                UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
                                userMoneyDetails.setUserId(payDetails.getUserId());
                                userMoneyDetails.setTitle("微信充值");
                                userMoneyDetails.setType(1);
                                userMoneyDetails.setState(2);
                                userMoneyDetails.setMoney(BigDecimal.valueOf(payDetails.getMoney()));
                                userMoneyDetails.setContent("账户充值到账：" + payDetails.getMoney() + "元");
                                userMoneyDetails.setCreateTime(sdf.format(new Date()));
                                userMoneyDetailsDao.insert(userMoneyDetails);
                            } else if (payDetails.getClassify() == 3) {
                                //订单支付
                                TbOrder tbOrder = appOrderService.selectOrderByNum(payDetails.getOrderId());
                                List<TbOrder> tbOrderList = appOrderDao.selectList(new QueryWrapper<TbOrder>().eq("parent_id", tbOrder.getParentId()));
                                for (int i = 0; i < tbOrderList.size(); i++) {
                                    TbOrder tbOrder1 = new TbOrder();
                                    Integer code = (int) ((Math.random() * 9 + 1) * 10000);
                                    tbOrder1.setOrderCode(code.toString());
                                    tbOrder1.setOrderId(tbOrderList.get(i).getOrderId());
                                    tbOrder1.setIsPay(1);
                                    tbOrder1.setUserId(payDetails.getUserId());
                                    tbOrder1.setStatus(6);
                                    tbOrder1.setPayTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                    appOrderService.updateById(tbOrder1);
                                    OrderGoods orderGoods = new OrderGoods();
                                    orderGoods.setStatus(1);
                                    orderGoodsDao.update(orderGoods, new QueryWrapper<OrderGoods>().eq("order_id", tbOrder1.getOrderId()));
                                }
                                //将红包改为已使用状态
                                TbCouponUser tbCouponUser = new TbCouponUser();
                                tbCouponUser.setId(tbOrder.getCouponId());
                                tbCouponUser.setStatus(1);
                                tbCouponUserDao.updateById(tbCouponUser);
                                //支付成功，增加该商品销量，减少该商品库存
                                Long shopId1 = tbOrder.getShopId();
                                Long orderId = tbOrder.getOrderId();
                                List<OrderGoods> orderGoodsList = orderGoodsDao.selectList(new QueryWrapper<OrderGoods>().eq("order_id", orderId));
                                for (int a = 0; a < orderGoodsList.size(); a++) {
                                    Integer goodsNum = orderGoodsList.get(a).getGoodsNum();
                                    Long goodsId = orderGoodsList.get(a).getGoodsId();
                                    GoodsShopRelevancy goodsShopRelevancy = goodsShopRelevancyDao.selectOne(new QueryWrapper<GoodsShopRelevancy>()
                                            .eq("shop_id", shopId1).eq("goods_id", goodsId));
                                    GoodsShopRelevancy goodsShopRelevancy1 = new GoodsShopRelevancy();
                                    goodsShopRelevancy1.setId(goodsShopRelevancy.getId());
                                    goodsShopRelevancy1.setSales(goodsShopRelevancy.getSales() + goodsNum);//加销量
                                    goodsShopRelevancy1.setInventory(goodsShopRelevancy.getInventory() - goodsNum);//减库存
                                    goodsShopRelevancyDao.updateById(goodsShopRelevancy1);
                                }
                                //添加钱包消费记录
                                UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
                                userMoneyDetails.setUserId(tbOrder.getUserId());
                                userMoneyDetails.setTitle("微信支付");
                                userMoneyDetails.setType(2);
                                userMoneyDetails.setState(2);
                                userMoneyDetails.setMoney(tbOrder.getPayMoney());
                                userMoneyDetails.setContent("使用微信支付：" + tbOrder.getPayMoney() + "元");
                                userMoneyDetails.setCreateTime(sdf.format(new Date()));
                                userMoneyDetailsDao.insert(userMoneyDetails);
                                //用户端添加消息记录
                                MessageInfo messageInfo = new MessageInfo();
                                UserEntity userEntity = userDao.selectById(tbOrder.getUserId());
                                messageInfo.setTitle("下单");
                                messageInfo.setContent("恭喜您，下单成功！");
                                String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                                messageInfo.setCreateAt(format);
                                messageInfo.setUserId(tbOrder.getUserId().toString());
                                messageInfo.setUserName(userEntity.getUserName());
                                messageInfoDao.insert(messageInfo);

                                //店铺端添加消息记录
                                MessageInfo messageInfo1 = new MessageInfo();
                                messageInfo1.setTitle("有新订单");
                                messageInfo1.setCreateAt(format);
                                messageInfo1.setContent("有新订单，请及时处理！");
                                messageInfo1.setShopId(tbOrder.getShopId());
                                messageInfo1.setReadFlag(2);
                                messageInfoDao.insert(messageInfo1);

                                //设置小程序消息推送
                                CommonInfo one = commonInfoService.findOne(269);
                                List<String> msgList = new ArrayList<>();
                                Long shopId = tbOrder.getShopId();
                                GoodsShop goodsShop = goodsShopDao.selectById(shopId);
                                String shopName = goodsShop.getShopName();
                                String goodsMoney = tbOrder.getPayMoney().toString();
                                String orderNum = tbOrder.getOrderNumber();
                                String payTime = tbOrder.getPayTime();
                                msgList.add("下单成功");
                                msgList.add(orderNum);
                                msgList.add(shopName);
                                msgList.add(format);
                                SenInfoCheckUtil.sendMsg(userEntity.getOpenId(), one.getValue(), msgList, 1);
                                if (StringUtils.isNotEmpty(goodsShop.getSnCode())) {
                                    TbOrder tbOrder1 = appOrderService.selectOrderByIds(tbOrder.getOrderId());
                                    FeiYunUtils.print(goodsShop.getSnCode(), null, tbOrder1);
                                }
                                //订单付款并结束订单
                            } else if (payDetails.getClassify() == 4) {
                                TbOrder tbOrder = appOrderService.getOne(new QueryWrapper<TbOrder>().eq("order_number", payDetails.getOrderId()));
                                appOrderService.accomplishOrder(payDetails.getUserId(), tbOrder.getOrderId());
                            } else if (payDetails.getClassify() == 5) {
                                GoodsShop goodsShop = goodsShopDao.selectById(payDetails.getRelationId());
                                goodsShop.setIncome(goodsShop.getIncome().add(BigDecimal.valueOf(payDetails.getMoney())));
                                goodsShopDao.updateById(goodsShop);
                                UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
                                userMoneyDetails.setShopId(payDetails.getRelationId());
                                userMoneyDetails.setMoney(BigDecimal.valueOf(payDetails.getMoney()));
                                userMoneyDetails.setTitle("收银台收款" + payDetails.getMoney() + "元");
                                userMoneyDetails.setContent("收银台收款" + payDetails.getMoney() + "元");
                                userMoneyDetails.setType(1);
                                userMoneyDetails.setState(2);
                                userMoneyDetails.setUserId(payDetails.getUserId());
                                userMoneyDetails.setCreateTime(DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
                                userMoneyDetailsDao.insert(userMoneyDetails);
                                //用户钱包明细
                                userMoneyDetails.setShopId(null);
                                userMoneyDetails.setUserId(payDetails.getUserId());
                                userMoneyDetails.setType(2);
                                userMoneyDetails.setTitle("付款金额" + payDetails.getMoney() + "元");
                                userMoneyDetails.setContent("付款金额" + payDetails.getMoney() + "元");
                                userMoneyDetailsDao.insert(userMoneyDetails);
                            }

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
            System.err.println("手机支付回调通知失败" + e);
            xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            log.error("支付回调异常！", e.getMessage(), e);
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


    /*@Override
    public boolean refund(Orders orders) {
        WXConfigUtil config = null;
        try {
            config = new WXConfigUtil();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int commInfoId = 0;
        Integer payWay = null;
        //orders.getPayWay(); //支付方式（1app微信 2微信公众号 3微信小程序 4app支付宝 5H5支付宝 6零钱）
        switch (payWay) {
            case 1:
                commInfoId = 5;
                break; //appId
            case 2:
                commInfoId = 74;
                break; //公众号id
            case 3:
                commInfoId = 45;
                break; //小程序id
        }
        config.setAppId(commonInfoService.findOne(commInfoId).getValue());
        config.setKey(commonInfoService.findOne(75).getValue());
        config.setMchId(commonInfoService.findOne(76).getValue());
        WXPay wxpay = new WXPay(config);
        Map<String, String> data = new HashMap<>();
        data.put("appid", config.getAppID());
        data.put("mch_id", config.getMchID());
        data.put("nonce_str", WXPayUtil.generateNonceStr());
        try {
            data.put("sign", WXPayUtil.generateSignature(data, config.getKey(), WXPayConstants.SignType.MD5));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        data.put("out_trade_no", orders.getOrdersNo()); //订单号,支付单号一致
        data.put("out_refund_no", orders.getOrdersNo()); //退款单号，同一笔用不同的退款单号
        double total_fee = 0.00;
        data.put("total_fee", new Double(orders.getPayMoney().doubleValue() * 100).intValue() + ""); //1块等于微信支付传入100);
        data.put("refund_fee", new Double(orders.getPayMoney().doubleValue() * 100).intValue() + ""); //1块等于微信支付传入100);
        //使用官方API退款
        try {
            Map<String, String> response = wxpay.refund(data);
            if ("SUCCESS".equals(response.get("return_code"))) {//主要返回以下5个参数
                System.err.println("退款成功");
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.info("返回");
            e.printStackTrace();
            return false;
        }
    }*/

    @Override
    public boolean wxRefund(PayDetails payDetails, BigDecimal refundMoney) {
        if (StringUtils.isBlank(payDetails.getRefundNumber())) {
            payDetails.setRefundNumber(getGeneralOrder());
        }
        WXConfigUtil config = null;
        try {
            config = new WXConfigUtil();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int commInfoId = 0;
        Integer payWay = payDetails.getType(); //支付方式（1app微信 2微信公众号 3微信小程序 4app支付宝 5H5支付宝 6零钱）
        switch (payWay) {
            case 1:
                commInfoId = 74;
                break; //appId
            case 2:
                commInfoId = 5;
                break; //公众号id
            case 3:
                commInfoId = 45;
                break; //小程序id
        }
        config.setAppId(commonInfoService.findOne(commInfoId).getValue());
        config.setKey(commonInfoService.findOne(75).getValue());
        config.setMchId(commonInfoService.findOne(76).getValue());
        WXPay wxpay = new WXPay(config);
        Map<String, String> data = new HashMap<>();
        data.put("appid", config.getAppID());
        data.put("mch_id", config.getMchID());
        data.put("nonce_str", WXPayUtil.generateNonceStr());
        try {
            data.put("sign", WXPayUtil.generateSignature(data, config.getKey(), WXPayConstants.SignType.MD5));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        data.put("out_trade_no", payDetails.getOrderId()); //订单号,支付单号一致
        data.put("out_refund_no", payDetails.getRefundNumber()); //退款单号，同一笔用不同的退款单号
        data.put("total_fee", String.valueOf((BigDecimal.valueOf(payDetails.getMoney()).multiply(BigDecimal.valueOf(100))).intValue())); //1块等于微信支付传入100);
        data.put("refund_fee", String.valueOf((refundMoney.multiply(BigDecimal.valueOf(100))).intValue())); //1块等于微信支付传入100);
        //使用官方API退款
        try {
            Map<String, String> response = wxpay.refund(data);
            if ("SUCCESS".equals(response.get("return_code"))) {//主要返回以下5个参数
                //修改成功后将退款订单号置空
                payDetailsDao.updateRefund(payDetails.getId());
                System.err.println("退款成功");
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error("返回");
            e.printStackTrace();
            log.error("微信退款失败！，订单号" + payDetails.getOrderId() + "，失败原因：" + e.getMessage(), e);
            return false;
        }
    }


}