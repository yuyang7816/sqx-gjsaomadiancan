package com.sqx.modules.iPay.business.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.sqx.common.exception.SqxException;
import com.sqx.common.utils.DateUtils;
import com.sqx.modules.app.dao.UserDao;
import com.sqx.modules.app.dao.UserMoneyDetailsDao;
import com.sqx.modules.app.dao.UserVipDao;
import com.sqx.modules.app.dao.VipDetailsDao;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.entity.UserVip;
import com.sqx.modules.app.entity.VipDetails;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.coupon.dao.TbCouponUserDao;
import com.sqx.modules.coupon.entity.TbCouponUser;
import com.sqx.modules.goods.dao.GoodsShopDao;
import com.sqx.modules.goods.dao.GoodsShopRelevancyDao;
import com.sqx.modules.goods.entity.GoodsShop;
import com.sqx.modules.goods.entity.GoodsShopRelevancy;
import com.sqx.modules.iPay.business.CallbackBusinessService;
import com.sqx.modules.iPay.enums.BusinessTypeEnum;
import com.sqx.modules.message.dao.MessageInfoDao;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.order.dao.AppOrderDao;
import com.sqx.modules.order.dao.OrderGoodsDao;
import com.sqx.modules.order.entity.OrderGoods;
import com.sqx.modules.order.entity.TbOrder;
import com.sqx.modules.order.service.AppOrderService;
import com.sqx.modules.pay.dao.PayDetailsDao;
import com.sqx.modules.pay.entity.PayDetails;
import com.sqx.modules.utils.DataSourceMessageSource;
import com.sqx.modules.utils.SenInfoCheckUtil;
import com.sqx.modules.utils.fieYun.FeiYunUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author chengming
 * @since 2025/4/14
 *
 * 业务处理类
 */
@Slf4j
@Service
public class CallbackBusinessServiceImpl implements CallbackBusinessService {

    @Autowired
    private PayDetailsDao payDetailsDao;
    @Autowired
    private VipDetailsDao vipDetailsDao;
    @Autowired
    private UserService userService;
    @Autowired
    private UserVipDao userVipDao;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private AppOrderDao appOrderDao;
    @Autowired
    private OrderGoodsDao orderGoodsDao;
    @Autowired
    private TbCouponUserDao tbCouponUserDao;
    @Autowired
    private GoodsShopRelevancyDao goodsShopRelevancyDao;
    @Autowired
    private UserMoneyDetailsDao userMoneyDetailsDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private MessageInfoDao messageInfoDao;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private GoodsShopDao goodsShopDao;
    @Autowired
    private AppOrderService appOrderService;
    @Autowired
    private DataSourceMessageSource dataSourceMessageSource;

    @Override
    public void process(String orderNo, String tradeNo) {
        PayDetails payDetails = getPayDetailsByOrderNo(orderNo);
        if (Objects.isNull(payDetails)) {
            throw new SqxException("支付详情不存在");
        }

        if (0 != payDetails.getState()) {
            return;
        }

        if (Objects.equals(BusinessTypeEnum.RECHARGE.getCode(), payDetails.getClassify())) {
            rechargeBusiness(payDetails);
        } else if (Objects.equals(BusinessTypeEnum.VIP.getCode(), payDetails.getClassify())) {
            vipBusiness(payDetails);
        } else if (Objects.equals(BusinessTypeEnum.ORDER.getCode(), payDetails.getClassify())) {
            orderBusiness(payDetails);
        } else if (4 == payDetails.getClassify()) {
            TbOrder tbOrder = appOrderService.getOne(new QueryWrapper<TbOrder>().eq("order_number", payDetails.getOrderId()));
            appOrderService.accomplishOrder(payDetails.getUserId(), tbOrder.getOrderId());
        } else if (5 == payDetails.getClassify()) {
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
            userMoneyDetails.setCreateTime(DateUtil.now());
            userMoneyDetailsDao.insert(userMoneyDetails);

            //用户钱包明细
            userMoneyDetails.setShopId(null);
            userMoneyDetails.setUserId(payDetails.getUserId());
            userMoneyDetails.setType(2);
            userMoneyDetails.setTitle("付款金额" + payDetails.getMoney() + "元");
            userMoneyDetails.setContent("付款金额" + payDetails.getMoney() + "元");
            userMoneyDetailsDao.insert(userMoneyDetails);
        } else {
            log.error("不支持的业务");
            throw new SqxException("不支持的业务");
        }

        updateSuccessPayDetail(orderNo, tradeNo);
    }

    /**
     * 订单业务
     * @param payDetails
     */
    private void orderBusiness(PayDetails payDetails) {
        //订单支付
        TbOrder tbOrder = appOrderDao.selectById(payDetails.getOrderId());
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
            appOrderDao.updateById(tbOrder1);
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
        userMoneyDetails.setCreateTime(DateUtil.now());
        userMoneyDetailsService.saveI18n(userMoneyDetails, "55006", null, "55007", new String[]{String.valueOf(tbOrder.getPayMoney())});

        //用户端添加消息记录
        MessageInfo messageInfo = new MessageInfo();
        UserEntity userEntity = userDao.selectById(tbOrder.getUserId());
//        messageInfo.setTitle("下单");
        messageInfo.setTitle(dataSourceMessageSource.getConfigI18ForCach("55008"));
        messageInfo.setContent("55009");
//        messageInfo.setContent("恭喜您，下单成功！");
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        messageInfo.setCreateAt(format);
        messageInfo.setUserId(tbOrder.getUserId().toString());
        messageInfo.setUserName(userEntity.getUserName());
        messageInfoDao.insert(messageInfo);

        //店铺端添加消息记录
        MessageInfo messageInfo1 = new MessageInfo();
//        messageInfo1.setTitle("有新订单");
        messageInfo1.setTitle("55010");
        messageInfo1.setCreateAt(format);
//        messageInfo1.setContent("有新订单，请及时处理！");
        messageInfo1.setContent("55011");
        messageInfo1.setShopId(tbOrder.getShopId());
        messageInfo1.setReadFlag(2);
        messageInfoDao.insert(messageInfo1);

        //设置小程序消息推送
        CommonInfo one = commonInfoService.findOne(269);
        List<String> msgList = new ArrayList<>();
        Long shopId = tbOrder.getShopId();
        GoodsShop goodsShop = goodsShopDao.selectById(shopId);
        String shopName = goodsShop.getShopName();
        String orderNum = tbOrder.getOrderNumber();
//        msgList.add("下单成功");
        msgList.add("55012");
        msgList.add(orderNum);
        msgList.add(shopName);
        msgList.add(format);
        SenInfoCheckUtil.sendMsg(userEntity.getOpenId(), one.getValue(), msgList, 1);
        if (StringUtils.isNotEmpty(goodsShop.getSnCode())) {
            TbOrder tbOrder1 = appOrderService.selectOrderByIds(tbOrder.getOrderId());
            FeiYunUtils.print(goodsShop.getSnCode(), null, tbOrder1);
        }
    }

    /**
     * 会员业务
     * @param payDetails
     */
    private void vipBusiness(PayDetails payDetails) {
        VipDetails vipDetails = vipDetailsDao.selectById(payDetails.getRelationId());
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
        userMoneyDetailsService.saveI18n(userMoneyDetails, "55005", null, "55005", null);
        userVip.setEndTime(DateUtils.format(cal.getTime()));
        if (userVip.getVipId() != null) {
            userVipDao.updateById(userVip);
        } else {
            userVipDao.insert(userVip);
        }
        userEntity.setEndTime(userVip.getEndTime());
        userService.updateById(userEntity);
    }

    /**
     * 充值业务
     * @param payDetails
     */
    private void rechargeBusiness(PayDetails payDetails) {
        //充值金额
        userMoneyService.updateMoney(1, payDetails.getUserId(), payDetails.getMoney());

        //添加钱包消费记录
        UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
        userMoneyDetails.setUserId(payDetails.getUserId());
        userMoneyDetails.setTitle("充值");
        userMoneyDetails.setType(1);
        userMoneyDetails.setState(2);
        userMoneyDetails.setMoney(BigDecimal.valueOf(payDetails.getMoney()));
        userMoneyDetails.setContent("账户充值到账：" + payDetails.getMoney() + "元");
        userMoneyDetails.setCreateTime(DateUtils.format(new Date()));
        userMoneyDetailsService.saveI18n(userMoneyDetails, "55003", null, "55004", new String[]{String.valueOf(payDetails.getMoney())});
    }

    private PayDetails getPayDetailsByOrderNo(String orderNo) {
        QueryWrapper<PayDetails> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PayDetails::getOrderId, orderNo);

        return payDetailsDao.selectOne(queryWrapper);
    }

    /**
     * 订单成功 更新支付详情
     * @param orderNo
     * @param traceNo
     */
    private void updateSuccessPayDetail(String orderNo, String traceNo) {
        UpdateWrapper<PayDetails> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                .eq(PayDetails::getOrderId, orderNo)
                .set(PayDetails::getState, 1)
                .set(PayDetails::getPayTime, DateUtil.now())
                .set(PayDetails::getTradeNo, traceNo);

        payDetailsDao.update(new PayDetails(), updateWrapper);
    }

    @Override
    public void refund(String orderNo, String tradeNo) {

    }
}
