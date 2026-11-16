package com.sqx.modules.iPay.business.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sqx.common.exception.SqxException;
import com.sqx.modules.address.dao.AddressDao;
import com.sqx.modules.address.entity.Address;
import com.sqx.modules.app.dao.VipDetailsDao;
import com.sqx.modules.app.entity.VipDetails;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.coupon.dao.TbCouponUserDao;
import com.sqx.modules.coupon.entity.TbCouponUser;
import com.sqx.modules.goods.dao.GoodsDao;
import com.sqx.modules.goods.dao.GoodsShopRelevancyDao;
import com.sqx.modules.goods.entity.Goods;
import com.sqx.modules.goods.entity.GoodsShopRelevancy;
import com.sqx.modules.iPay.business.BusinessService;
import com.sqx.modules.iPay.enums.BusinessTypeEnum;
import com.sqx.modules.iPay.param.BusinessParam;
import com.sqx.modules.order.dao.AppOrderDao;
import com.sqx.modules.order.dao.OrderGoodsDao;
import com.sqx.modules.order.entity.OrderGoods;
import com.sqx.modules.order.entity.TbOrder;
import com.sqx.modules.pay.dao.PayDetailsDao;
import com.sqx.modules.pay.entity.PayDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * @author chengming
 * @since 2025/4/24
 */
@Slf4j
@Service
public class BusinessServiceImpl implements BusinessService {
    @Autowired
    private VipDetailsDao vipDetailsDao;
    @Autowired
    private PayDetailsDao payDetailsDao;
    @Autowired
    private AppOrderDao appOrderDao;
    @Autowired
    private OrderGoodsDao orderGoodsDao;
    @Autowired
    private GoodsShopRelevancyDao goodsShopRelevancyDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private TbCouponUserDao tbCouponUserDao;
    @Autowired
    private AddressDao addressDao;
    @Autowired
    private CommonInfoService commonInfoService;

    @Override
    public void process(String orderNo, Integer platform, BusinessParam param) {
        log.info("支付业务处理");

        if (Objects.equals(param.getBusinessType(), BusinessTypeEnum.ORDER.getCode())) {
            orderBiz(orderNo, platform, param);
            return;
        }

        if (Objects.equals(param.getBusinessType(), BusinessTypeEnum.RECHARGE.getCode())) {
            rechargeBiz(orderNo, platform, param);
            return;
        }

        if (Objects.equals(param.getBusinessType(), BusinessTypeEnum.VIP.getCode())) {
            vipBiz(orderNo, platform, param);
            return;
        }

        log.error("不支持的业务: {}", param.getBusinessType());
        throw new SqxException("暂不支持此业务");
    }

    /**
     * 订单业务
     * @param orderNo 订单号
     * @param platform 平台
     * @param param 业务参数
     */
    private void orderBiz(String orderNo, Integer platform, BusinessParam param) {
        TbOrder tbOrder = appOrderDao.selectOne(new LambdaQueryWrapper<TbOrder>().eq(TbOrder::getOrderId, param.getOutTraceNo()));
        if (Objects.isNull(tbOrder)) {
            throw new SqxException("订单信息不存在");
        }

        if (1 != tbOrder.getStatus() && 2 != tbOrder.getStatus()) {
            throw new SqxException("订单状态异常");
        }

        // 校验商品库存
        checkGoodsRelevancy(tbOrder);

        // 处理优惠券
        checkCoupon(param);

        // 跑腿费
        BigDecimal errandMoney = new BigDecimal(commonInfoService.getValue(123));
        // 外卖订单免配送费最低金额
        BigDecimal noneMoney = new BigDecimal(commonInfoService.getValue(124));
        if (2 == param.getOrderType()) {
            // 处理订单地址
            if (Objects.isNull(param.getAddressId())) {
                throw new SqxException("请填写地址");
            }

            Address address = addressDao.selectById(param.getAddressId());
            String addressJson = JSONObject.toJSONString(address);
            tbOrder.setAddress(addressJson);

            // 计算是否减免用户跑腿费
            if (param.getAmount().compareTo(noneMoney) < 0) {
                tbOrder.setErrandMoney(errandMoney);
                tbOrder.setErrandUser(2);
            } else {
                tbOrder.setErrandUser(1);
            }
        }

        // 更新订单信息
        tbOrder.setPayMoney(param.getAmount());
        tbOrder.setPackMoney(param.getAmount());
        tbOrder.setPayType(platform);
        tbOrder.setOrderType(param.getOrderType());
        appOrderDao.updateById(tbOrder);

        // 支付详情
        PayDetails payDetails = new PayDetails();
        payDetails.setOrderId(orderNo);
        payDetails.setUserId(param.getUserId());
        payDetails.setClassify(param.getBusinessType());
        payDetails.setType(platform);
        payDetails.setMoney(param.getAmount().doubleValue());
        payDetails.setState(0);

        // 支付成功后重定向地址
        JSONObject extend = new JSONObject();
        extend.put("clientId", param.getClientId());
        extend.put("redirectUri", param.getRedirectUri());
        payDetails.setExtend(extend.toJSONString());
        payDetailsDao.insert(payDetails);
    }

    /**
     * 处理订单优惠券
     * @param param
     */
    private void checkCoupon(BusinessParam param) {
        if (Objects.isNull(param.getCouponId())) {
            return;
        }

        TbCouponUser tbCouponUser = tbCouponUserDao.selectById(param.getCouponId());
        if (!tbCouponUser.getUserId().equals(param.getUserId())) {
            throw new SqxException("优惠券与登录用户不匹配");
        }

        if (0 != tbCouponUser.getStatus()) {
            throw new SqxException("该优惠券已失效");
        }

        if (0 < tbCouponUser.getMinMoney().compareTo(param.getAmount())) {
            throw new SqxException("订单金额不足，无法使用该优惠券");
        }
    }

    /**
     * 校验商品库存
     * @param tbOrder
     */
    private void checkGoodsRelevancy(TbOrder tbOrder) {
        List<OrderGoods> orderGoodsList = orderGoodsDao.selectList(new LambdaQueryWrapper<OrderGoods>().eq(OrderGoods::getOrderId, tbOrder.getOrderId()));
        if (CollectionUtils.isEmpty(orderGoodsList)) {
            return;
        }

        for (OrderGoods orderGoods : orderGoodsList) {
            Goods goods = goodsDao.selectById(orderGoods.getGoodsId());
            if (Objects.isNull(goods)) {
                throw new SqxException("商品信息不存在");
            }

            if (goods.getStatus() == 1) {
                throw new SqxException("商品[" + goods.getGoodsName() + "]已下架,请删除后重新结算");
            }

            GoodsShopRelevancy goodsShopRelevancy = goodsShopRelevancyDao.selectOne(new LambdaQueryWrapper<GoodsShopRelevancy>()
                    .eq(GoodsShopRelevancy::getShopId, tbOrder.getShopId())
                    .eq(GoodsShopRelevancy::getGoodsId, orderGoods.getGoodsId())
            );
            if (Objects.isNull(goodsShopRelevancy)) {
                log.error("店铺: {} 商品: {} 库存信息不存在", tbOrder.getOrderId(), orderGoods.getGoodsId());
                throw new SqxException("商品[" + goods.getGoodsName() + "]库存信息不存在");
            }

            if (goodsShopRelevancy.getStatus() == 1) {
                throw new SqxException("商品[" + goods.getGoodsName() + "]已下架,请删除后重新结算");
            }

            if (goodsShopRelevancy.getInventory() < orderGoods.getGoodsNum()) {
                throw new SqxException("商品库存不足");
            }
        }
    }

    /**
     * 充值业务
     * @param orderNo 订单号
     * @param platform 平台
     * @param param 业务参数
     */
    private void rechargeBiz(String orderNo, Integer platform, BusinessParam param) {
        PayDetails payDetails = new PayDetails();
        payDetails.setOrderId(orderNo);
        payDetails.setUserId(param.getUserId());
        payDetails.setClassify(param.getBusinessType());
        payDetails.setType(platform);
        payDetails.setMoney(param.getAmount().doubleValue());
        payDetails.setState(0);

        // 支付成功后重定向地址
        JSONObject extend = new JSONObject();
        extend.put("clientId", param.getClientId());
        extend.put("redirectUri", param.getRedirectUri());
        payDetails.setExtend(extend.toJSONString());
        payDetailsDao.insert(payDetails);
    }

    /**
     * 会员业务
     * @param orderNo 订单号
     * @param param 业务参数
     */
    private void vipBiz(String orderNo, Integer platform, BusinessParam param) {
        VipDetails vipDetails = vipDetailsDao.selectOne(new LambdaQueryWrapper<VipDetails>().eq(VipDetails::getId, param.getVipId()));
        if (Objects.isNull(vipDetails)) {
            throw new SqxException("会员信息不存在");
        }

        if (0 != param.getAmount().compareTo(vipDetails.getMoney())) {
            throw new SqxException("金额错误");
        }

        PayDetails payDetails = new PayDetails();
        payDetails.setOrderId(orderNo);
        payDetails.setUserId(param.getUserId());
        payDetails.setClassify(param.getBusinessType());
        payDetails.setType(platform);
        payDetails.setMoney(param.getAmount().doubleValue());
        payDetails.setState(0);
        payDetails.setRelationId(param.getVipId());

        // 支付成功后重定向地址
        JSONObject extend = new JSONObject();
        extend.put("clientId", param.getClientId());
        extend.put("redirectUri", param.getRedirectUri());
        payDetails.setExtend(extend.toJSONString());
        payDetailsDao.insert(payDetails);
    }
}
