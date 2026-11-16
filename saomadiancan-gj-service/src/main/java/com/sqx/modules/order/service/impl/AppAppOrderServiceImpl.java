package com.sqx.modules.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.exception.SqxException;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.dao.UserDao;
import com.sqx.modules.app.dao.UserMoneyDao;
import com.sqx.modules.app.dao.UserMoneyDetailsDao;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.coupon.dao.TbCouponUserDao;
import com.sqx.modules.coupon.entity.TbCouponUser;
import com.sqx.modules.goods.dao.GoodsDao;
import com.sqx.modules.goods.dao.GoodsShopDao;
import com.sqx.modules.goods.dao.GoodsShopRelevancyDao;
import com.sqx.modules.goods.dao.GoodsSkuDao;
import com.sqx.modules.goods.entity.Goods;
import com.sqx.modules.goods.entity.GoodsShop;
import com.sqx.modules.goods.entity.GoodsShopRelevancy;
import com.sqx.modules.goods.entity.GoodsSku;
import com.sqx.modules.goods.service.GoodsService;
import com.sqx.modules.message.dao.MessageInfoDao;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.order.dao.AppOrderDao;
import com.sqx.modules.order.dao.OrderGoodsDao;
import com.sqx.modules.order.entity.OrderGoods;
import com.sqx.modules.order.entity.TbOrder;
import com.sqx.modules.order.entity.vo.OrderVo;
import com.sqx.modules.order.entity.vo.OrdersGoodsVo;
import com.sqx.modules.order.service.AppOrderService;
import com.sqx.modules.order.service.OrderGoodsService;
import com.sqx.modules.pay.entity.PayDetails;
import com.sqx.modules.pay.service.PayDetailsService;
import com.sqx.modules.pay.service.WxService;
import com.sqx.modules.shopSeat.entity.ShopSeat;
import com.sqx.modules.shopSeat.service.ShopSeatService;
import com.sqx.modules.sys.dao.SysUserRoleDao;
import com.sqx.modules.sys.entity.SysUserRoleEntity;
import com.sqx.modules.utils.DataSourceMessageSource;
import com.sqx.modules.utils.RequestUtil;
import com.sqx.modules.utils.SenInfoCheckUtil;
import com.sqx.modules.utils.fieYun.FeiYunUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AppAppOrderServiceImpl extends ServiceImpl<AppOrderDao, TbOrder> implements AppOrderService {

    @Autowired
    private AppOrderDao appOrderDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private OrderGoodsDao orderGoodsDao;
    @Autowired
    private GoodsSkuDao goodsSkuDao;
    @Autowired
    private TbCouponUserDao tbCouponUserDao;
    @Autowired
    private GoodsShopDao goodsShopDao;
    @Autowired
    private MessageInfoDao messageInfoDao;
    @Autowired
    private GoodsShopRelevancyDao goodsShopRelevancyDao;
    @Autowired
    private SysUserRoleDao sysUserRoleDao;
    @Autowired
    private UserMoneyDao userMoneyDao;
    @Autowired
    private WxService wxService;
    @Autowired
    private UserMoneyDetailsDao userMoneyDetailsDao;
    @Autowired
    private ShopSeatService seatService;
    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private PayDetailsService detailsService;
    @Autowired
    private DataSourceMessageSource dataSourceMessageSource;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result insertOrder(Long userId, OrderVo orderVo) {

        GoodsShop goodsShop = goodsShopDao.selectById(orderVo.getShopId());
        if (goodsShop.getPutawayFlag() != null && goodsShop.getPutawayFlag() == 2) {
            return Result.error("60145");
        }
        QueryWrapper<TbOrder> wrapper = new QueryWrapper<TbOrder>().eq("shop_id", orderVo.getShopId()).eq("status", 1);

        if (orderVo.getOrderType() != null && orderVo.getOrderType() == 3) {
            wrapper.eq("seat_id", orderVo.getSeatId());
        } else {
            wrapper.eq("user_id", userId);
        }
        TbOrder order = appOrderDao.selectOne(wrapper);
        TbOrder tbOrder = new TbOrder();
        if (orderVo.getOrderType() != null && orderVo.getOrderType() == 3) {
            if (orderVo.getSeatId() == null) {
                return Result.error("60146");
            } else {
                ShopSeat shopSeat = seatService.getOne(new QueryWrapper<ShopSeat>().eq("shop_id", orderVo.getShopId()).eq("seat_id", orderVo.getSeatId()).eq("is_enable", 1));
                if (shopSeat == null) {
                    return Result.error("60147");
                } else {
                    shopSeat.setStatus(1);
                    shopSeat.setUserId(userId);
                    seatService.updateById(shopSeat);
                    tbOrder.setIsSeat(1);
                    tbOrder.setOrderType(3);
                    tbOrder.setSeatId(orderVo.getSeatId());
                }
            }
        }
        if (order == null) {
            UserEntity userEntity = userDao.selectById(userId);
            if (orderVo.getOrderType() == null || userEntity != null) {
                tbOrder.setUserId(userId);
                tbOrder.setUserName(userEntity.getUserName());
                tbOrder.setPhone(userEntity.getPhone());
            }

            tbOrder.setOrderNumber(getGeneralOrder());
            tbOrder.setIsPay(0);
            tbOrder.setDeleteFlag(0);
            tbOrder.setStatus(1);
            tbOrder.setShopId(orderVo.getShopId());
            tbOrder.setOrderMoney(BigDecimal.ZERO);
            //创建订单，订单发起人就是该登录用户
            tbOrder.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            appOrderDao.insert(tbOrder);

            TbOrder tbOrder1 = new TbOrder();
            tbOrder1.setOrderId(tbOrder.getOrderId());
            tbOrder1.setParentId(tbOrder.getOrderId());
            appOrderDao.updateById(tbOrder1);
            //添加订单-商品表
            return setOrderGoods(orderVo.getOrdersGoodsVoList(), tbOrder);
        } else {
            //添加订单-商品表
            return setOrderGoods(orderVo.getOrdersGoodsVoList(), order);
        }
    }

    private Result setOrderGoods(List<OrdersGoodsVo> orderGoodsVoList, TbOrder order) {
        for (OrdersGoodsVo orderGoodsVo : orderGoodsVoList) {

            BigDecimal orderMoney;
            //先判断该商品的库存是否足够
            GoodsShopRelevancy goodsShopRelevancy = goodsShopRelevancyDao.selectOne(new QueryWrapper<GoodsShopRelevancy>().eq("shop_id", order.getShopId()).eq("goods_id", orderGoodsVo.getGoodsId()));
            if (goodsShopRelevancy.getInventory() == null) {
                return Result.error("60148");
            }
            if (goodsShopRelevancy.getInventory() < orderGoodsVo.getNum()) {
                return Result.error("60149");
            }
            String languageType = RequestUtil.getAcceptLanguage();
            Goods goods = goodsDao.getById(orderGoodsVo.getGoodsId(), languageType);
            if (goodsShopRelevancy.getStatus() == 1) {
                return Result.error("60150",goods.getGoodsName());
            }
            if (goods.getStatus() == 1) {
                return Result.error("60150",goods.getGoodsName());
            }
            Long orderId = order.getOrderId();
            OrderGoods orderGoods = new OrderGoods();
            OrderGoods orderGoodsOne = orderGoodsService.getOne(new QueryWrapper<OrderGoods>().eq("goods_id", orderGoodsVo.getGoodsId()).eq("sku_id", orderGoodsVo.getSkuId()).eq("order_id", order.getOrderId()));
            if (orderGoodsOne != null) {
                orderGoodsOne.setGoodRemarks(orderGoodsVo.getGoodRemarks());
                orderGoodsOne.setGoodsNum(orderGoodsOne.getGoodsNum() + orderGoodsVo.getNum());
                orderGoodsDao.updateById(orderGoodsOne);
                orderMoney = order.getOrderMoney().add(orderGoodsOne.getGoodsPrice().multiply(new BigDecimal(orderGoodsVo.getNum()))).add(orderGoodsOne.getGoodsPack().multiply(new BigDecimal(orderGoodsVo.getNum())));
            } else {
                orderGoods.setOrderId(orderId);
                orderGoods.setGoodRemarks(orderGoodsVo.getGoodRemarks());
                orderGoods.setGoodsName(goods.getGoodsName());
                orderGoods.setSkuId(orderGoodsVo.getSkuId());
                GoodsSku goodsSku = goodsSkuDao.selectById(orderGoodsVo.getSkuId());
                orderGoods.setGoodsPrice(goodsSku.getSkuPrice());
                if (order.getOrderType() == null || order.getOrderType() != 3) {
                    orderGoods.setGoodsPack(goods.getPackMoney());
                } else {
                    orderGoods.setGoodsPack(BigDecimal.ZERO);
                }
                orderGoods.setSkuMessage(orderGoodsVo.getSkuMessage());
                orderGoods.setGoodsNum(orderGoodsVo.getNum());
                orderGoods.setGoodsPicture(goods.getGoodsPicture());
                orderGoods.setGoodsId(goods.getGoodsId());
                orderGoodsDao.insert(orderGoods);
                if (order.getOrderMoney() == null) {
                    order.setOrderMoney(BigDecimal.ZERO);
                }
                orderMoney = order.getOrderMoney().add(orderGoods.getGoodsPrice().multiply(new BigDecimal(orderGoods.getGoodsNum()))).add(orderGoods.getGoodsPack().multiply(new BigDecimal(orderGoods.getGoodsNum())));
            }
            order.setOrderMoney(orderMoney);
            baseMapper.updateById(order);
        }

        return Result.success("50034");
    }


    @Override
    public Result print(Long orderId) {
        TbOrder tbOrder = selectOrderByIds(orderId);
        GoodsShop goodsShop = goodsShopDao.selectById(tbOrder.getShopId());
        if (StringUtils.isNotEmpty(goodsShop.getSnCode())) {
            FeiYunUtils.print(goodsShop.getSnCode(), null, tbOrder);
        } else {
            return Result.error("60151");
        }
        return Result.success();
    }

    @Override
    public TbOrder getSeatOrder(Long shopId, Long seatId) {
        TbOrder tbOrder = baseMapper.selectOne(new QueryWrapper<TbOrder>().eq("shop_id", shopId).eq("seat_id", seatId).eq("status", 1));
        if (tbOrder != null) {
            tbOrder.setOrderGoodsList(orderGoodsDao.selectList(new QueryWrapper<OrderGoods>().eq("order_id", tbOrder.getOrderId())));
            return tbOrder;
        } else {
            return null;
        }


    }

    @Override
    public HashMap<String, Object> shopOrderStatistics(Long shopId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        int forgeCount = baseMapper.selectCount(new QueryWrapper<TbOrder>().eq("shop_id", shopId).eq("status", 6));
        int pickUpCount = baseMapper.selectCount(new QueryWrapper<TbOrder>().eq("shop_id", shopId).eq("status", 3));
        int finishCount = baseMapper.selectCount(new QueryWrapper<TbOrder>().eq("shop_id", shopId).eq("status", 4));
        int cancelCount = baseMapper.selectCount(new QueryWrapper<TbOrder>().eq("shop_id", shopId).eq("status", 5));
        //制作中
        hashMap.put("forgeCount", forgeCount);
        //待取餐
        hashMap.put("pickUpCount", pickUpCount);
        //已完成
        hashMap.put("finishCount", finishCount);
        //已取消
        hashMap.put("cancelCount", cancelCount);
        return hashMap;
    }

    @Override
    public HashMap<String, Object> goodStatusCount(Long shopId) {
        HashMap<String, Object> hashMap = new HashMap<>();

        int enableCount = goodsShopRelevancyDao.selectCount(new QueryWrapper<GoodsShopRelevancy>().eq("shop_id", shopId).eq("status", 0));
        int notEnableCount = goodsShopRelevancyDao.selectCount(new QueryWrapper<GoodsShopRelevancy>().eq("shop_id", shopId).eq("status", 1));
        //上架中
        hashMap.put("enableCount", enableCount);
        //已下架
        hashMap.put("notEnableCount", notEnableCount);
        return hashMap;
    }

    @Override
    public BigDecimal selectCancelMoney(Long orderId, Long orderGoodsId) {
        BigDecimal refundMoney = BigDecimal.ZERO;
        TbOrder tbOrder = appOrderDao.selectById(orderId);
        if (tbOrder.getIsPay() != 0) {
            refundMoney = getAllMoney(orderId, refundMoney);
            if (orderGoodsId != null) {
                OrderGoods orderGoods = orderGoodsDao.selectById(orderGoodsId);
                if (orderGoods.getStatus() == 1) {
                    refundMoney = orderGoods.getGoodsPrice().add(orderGoods.getGoodsPack()).multiply(new BigDecimal(orderGoods.getGoodsNum()));
                }
            }
            if (tbOrder.getCouponId() != null) {
                TbCouponUser tbCouponUser = tbCouponUserDao.selectById(tbOrder.getCouponId());
                refundMoney = refundMoney.subtract(refundMoney.multiply(tbCouponUser.getMoney().divide(tbOrder.getOrderMoney(), 2, BigDecimal.ROUND_HALF_UP)));
            }

        }
        return refundMoney;
    }

    /**
     * 根据订单商品表查询未退款金额
     *
     * @param orderId
     * @param refundMoney
     * @return
     */
    private BigDecimal getAllMoney(Long orderId, BigDecimal refundMoney) {
        List<OrderGoods> list = orderGoodsService.list(new QueryWrapper<OrderGoods>().eq("order_id", orderId).eq("status", 1));
        for (OrderGoods orderGoods : list) {
            refundMoney = refundMoney.add(orderGoods.getGoodsPrice().add(orderGoods.getGoodsPack()).multiply(new BigDecimal(orderGoods.getGoodsNum())));
        }
        return refundMoney;
    }

    @Override
    public TbOrder selectOrderByIds(Long orderId) {
        TbOrder tbOrder = appOrderDao.selectById(orderId);
        int i = appOrderDao.selectCountOrderByTime(tbOrder.getPayTime(), tbOrder.getShopId());
        tbOrder.setCountOrder(i);
        if (tbOrder.getCouponId() != null) {
            TbCouponUser tbCouponUser = tbCouponUserDao.selectById(tbOrder.getCouponId());
            tbOrder.setCouponMoney(tbCouponUser.getMoney());
        }
        List<OrderGoods> orderGoodsList = orderGoodsDao.selectList(new QueryWrapper<OrderGoods>().eq("order_id", tbOrder.getOrderId()));
        tbOrder.setOrderGoodsList(orderGoodsList);
        GoodsShop goodsShop = goodsShopDao.selectById(tbOrder.getShopId());
        if (goodsShop != null) {
            tbOrder.setShopName(goodsShop.getShopName());
        }
        return tbOrder;
    }


    @Transactional
    @Override
    public Result buyGoods(Long userId, Long shopId, Long goodsId, Integer num, Long skuId, String skuMessage, Integer orderType, String orderRemarks, String languageType) {
        Goods goods = goodsDao.getById(goodsId, languageType);
        if (goods.getStatus() == 1) {
            return Result.error("60150" ,goods.getGoodsName());
        }
        GoodsShopRelevancy goodsShopRelevancy = goodsShopRelevancyDao.selectOne(new QueryWrapper<GoodsShopRelevancy>()
                .eq("goods_id", goodsId).eq("shop_id", shopId));

        if (goodsShopRelevancy.getStatus() == 1) {
            return Result.error("60150" , goods.getGoodsName() );
        }
        UserEntity userEntity = userDao.selectById(userId);
        TbOrder tbOrder = new TbOrder();
        tbOrder.setIsSeat(1);
        tbOrder.setOrderType(3);
        tbOrder.setUserId(userId);
        tbOrder.setOrderRemarks(orderRemarks);
        tbOrder.setUserName(userEntity.getUserName());
        tbOrder.setPhone(userEntity.getPhone());
        tbOrder.setOrderNumber(getGeneralOrder());
        tbOrder.setIsPay(0);
        tbOrder.setDeleteFlag(0);
        tbOrder.setStatus(2);
        tbOrder.setShopId(shopId);
        //创建订单，订单发起人就是该登录用户
        tbOrder.setParentUserId(userId);
        tbOrder.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        appOrderDao.insert(tbOrder);

        OrderGoods orderGoods = new OrderGoods();
        orderGoods.setOrderId(tbOrder.getOrderId());
        orderGoods.setGoodsName(goods.getGoodsName());
        orderGoods.setSkuId(skuId);
        GoodsSku goodsSku = goodsSkuDao.selectById(skuId);
        orderGoods.setGoodsPrice(goodsSku.getSkuPrice());
        orderGoods.setGoodsPack(goods.getPackMoney());
        orderGoods.setSkuMessage(skuMessage);
        orderGoods.setGoodsNum(num);
        orderGoods.setGoodsPicture(goods.getGoodsPicture());
        orderGoods.setGoodsId(goods.getGoodsId());
        orderGoodsDao.insert(orderGoods);

        BigDecimal multiply = orderGoods.getGoodsPrice().multiply(new BigDecimal(num));
        TbOrder tbOrder1 = new TbOrder();
        tbOrder1.setOrderId(tbOrder.getOrderId());
        tbOrder1.setParentId(tbOrder.getOrderId());
        tbOrder1.setPayMoney(multiply);
        appOrderDao.updateById(tbOrder1);
        return Result.success().put("data", tbOrder.getOrderId());
    }

    @Override
    public Result echoOrder(Long userId, Long shopId, Long goodsId) {
        TbOrder tbOrder = appOrderDao.selectOne(new QueryWrapper<TbOrder>().eq("user_id", userId).eq("shop_id", shopId).eq("status", 1));
        if (tbOrder == null) {
//            return Result.error("60152");
            return Result.success().put("data", Collections.emptyList());
        }
        Long orderId = tbOrder.getOrderId();
        List<OrderGoods> orderGoodsList = orderGoodsDao.selectList(new QueryWrapper<OrderGoods>().eq("order_id", orderId).eq("goods_id", goodsId));
        return Result.success().put("data", orderGoodsList);
    }


    @Transactional
    @Override
    public Result joinOrder(Long userId, Long goodsId, Long parentId) {
        Goods goods = goodsDao.selectById(goodsId);
        UserEntity userEntity = userDao.selectById(userId);
        net.sf.json.JSONObject goodsJson = net.sf.json.JSONObject.fromObject(goods);

        TbOrder order = new TbOrder();
        //order.setGoodsMessage(goodsJson.toString());
        order.setUserId(userId);
        order.setUserName(userEntity.getUserName());
        order.setPhone(userEntity.getPhone());
        //  String generalOrder = getGeneralOrder();
        //  order.setOrderNumber(generalOrder);
        order.setIsPay(0);
        order.setDeleteFlag(0);
        order.setStatus(0);
        //   order.setPackMoney(goods.getPackMoney());

        order.setParentId(parentId);
        appOrderDao.insertOrder(order);
        return Result.success();
    }

    @Override
    public TbOrder selectOrderByNum(String orderNum) {
        TbOrder tbOrder = appOrderDao.selectOne(new QueryWrapper<TbOrder>().eq("order_number", orderNum));
        return tbOrder;
    }

    @Override
    public Result selectAllOrder(Integer page, Integer limit, Integer status, String phone, Long shopId, String userName, Long userId, String orderNumber, Integer orderType, Integer isPay) {
        Page<TbOrder> pages = new Page<>(page, limit);
        List<SysUserRoleEntity> sysUserRoleEntityList = sysUserRoleDao.selectList(new QueryWrapper<SysUserRoleEntity>().eq("user_id", userId));
        List list = new ArrayList();
        for (int a = 0; a < sysUserRoleEntityList.size(); a++) {
            list.add(sysUserRoleEntityList.get(a).getRoleId());
        }
        if (list.size() == 1 && list.contains(12L)) {
            IPage<TbOrder> tbOrderIPage = appOrderDao.selectOrderByAdmin(pages, status, phone, shopId, userName, userId, orderNumber, orderType, isPay);
            for (int i = 0; i < tbOrderIPage.getRecords().size(); i++) {
                List<OrderGoods> orderGoodsList = orderGoodsDao.selectList(new QueryWrapper<OrderGoods>()
                        .eq("order_id", tbOrderIPage.getRecords().get(i).getOrderId()));
                tbOrderIPage.getRecords().get(i).setOrderGoodsList(orderGoodsList);
            }
            PageUtils pageUtils = new PageUtils(tbOrderIPage);
            return Result.success().put("data", pageUtils);
        } else {
            IPage<TbOrder> tbOrderIPage = appOrderDao.selectOrder(pages, status, phone, shopId, userName, orderNumber, orderType);
            for (int i = 0; i < tbOrderIPage.getRecords().size(); i++) {
                List<OrderGoods> orderGoodsList = orderGoodsDao.selectList(new QueryWrapper<OrderGoods>()
                        .eq("order_id", tbOrderIPage.getRecords().get(i).getOrderId()));
                tbOrderIPage.getRecords().get(i).setOrderGoodsList(orderGoodsList);
            }
            PageUtils pageUtils = new PageUtils(tbOrderIPage);
            return Result.success().put("data", pageUtils);
        }
    }

    @Override
    public Result selectOrderByUserId(Long userId, String date, String dateType) {
        HashMap hashMap = new HashMap();
        int count = appOrderDao.selectCountOrderByUserId(userId, date, dateType);
        BigDecimal bigDecimal = appOrderDao.selectSunMoneyByUserId(userId, date, dateType);
        hashMap.put("bigDecimal", bigDecimal);
        hashMap.put("count", count);
        return Result.success().put("data", hashMap);
    }

    @Override
    public Result selectOrderDetails(Integer page, Integer limit, Long userId) {
        Page<TbOrder> pages = new Page<>(page, limit);
        IPage<TbOrder> tbOrderIPage = appOrderDao.selectOrderDetails(pages, userId);
        for (int i = 0; i < tbOrderIPage.getRecords().size(); i++) {
            Long orderId = tbOrderIPage.getRecords().get(i).getOrderId();
            List<OrderGoods> orderGoodsList = orderGoodsDao.selectList(new QueryWrapper<OrderGoods>().eq("order_id", orderId));
            tbOrderIPage.getRecords().get(i).setOrderGoodsList(orderGoodsList);
        }


        PageUtils pageUtils = new PageUtils(tbOrderIPage);
        return Result.success().put("data", pageUtils);
    }

    @Override
    public Result waitTakeFood(Long userId, Integer orderType, Integer page, Integer limit, Integer status) {
        Page<TbOrder> pages = new Page<>(page, limit);
        IPage<TbOrder> tbOrderIPage = appOrderDao.waitTakeFood(pages, userId, orderType, status);
        List<TbOrder> records = tbOrderIPage.getRecords();
        int size = records.size();
        for (int i = 0; i < size; i++) {
            List<OrderGoods> orderGoodsList = orderGoodsDao.selectList(new QueryWrapper<OrderGoods>()
                    .eq("order_id", records.get(i).getOrderId()));
            tbOrderIPage.getRecords().get(i).setOrderGoodsList(orderGoodsList);
        }
        PageUtils pageUtils = new PageUtils(tbOrderIPage);
        return Result.success().put("data", pageUtils);
    }

    @Override
    public List<TbOrder> selectOrderByParentId(Long parentId) {
        List<TbOrder> tbOrder = appOrderDao.selectList(new QueryWrapper<TbOrder>().eq("parent_id", parentId));
        return tbOrder;
    }

    @Override
    public Result selectOrderById(Long orderId) {
        TbOrder tbOrder = appOrderDao.selectById(orderId);
        int i = appOrderDao.selectCountOrderByTime(tbOrder.getPayTime(), tbOrder.getShopId());
        tbOrder.setCountOrder(i);
        if (tbOrder.getCouponId() != null) {
            TbCouponUser tbCouponUser = tbCouponUserDao.selectById(tbOrder.getCouponId());
            tbOrder.setCouponMoney(tbCouponUser.getMoney());
        }
        List<OrderGoods> orderGoodsList = orderGoodsDao.selectList(new QueryWrapper<OrderGoods>().eq("order_id", tbOrder.getOrderId()));
        tbOrder.setOrderGoodsList(orderGoodsList);
        return Result.success().put("data", tbOrder);
    }


    @Transactional
    @Override
    public Result updateGoodsNum(Long orderGoodsId, Integer type, Integer num, Long shopId) {
        /*reentrantReadWriteLock.writeLock().lock();
        try{*/
        //type = 1增加商品数  = 2 减少商品数

        OrderGoods orderGoods = orderGoodsDao.selectById(orderGoodsId);
        GoodsShopRelevancy goodsShopRelevancy = goodsShopRelevancyDao.selectOne(new QueryWrapper<GoodsShopRelevancy>()
                .eq("shop_id", shopId).eq("goods_id", orderGoods.getGoodsId()));
        if (type == 1 && goodsShopRelevancy.getInventory() <= orderGoods.getGoodsNum()) {
            return Result.error("60153");
        }
        Goods shopGoods = goodsService.getById(goodsShopRelevancy.getGoodsId());
        if (shopGoods.getStatus() == 1) {
            return Result.error("60150" , shopGoods.getGoodsName());
        }
        if (goodsShopRelevancy.getStatus() == 1) {
            return Result.error("60150" , shopGoods.getGoodsName());
        }
        TbOrder tbOrder = baseMapper.selectById(orderGoods.getOrderId());
        if (type == 1) {
            OrderGoods orderGoods1 = new OrderGoods();
            orderGoods1.setId(orderGoodsId);
            orderGoods1.setGoodsNum(orderGoods.getGoodsNum() + num);

            orderGoodsDao.updateById(orderGoods1);
        } else {
            if (orderGoods.getGoodsNum() <= 1) {
                orderGoodsDao.deleteById(orderGoodsId);
            } else {
                OrderGoods orderGoods2 = new OrderGoods();
                orderGoods2.setId(orderGoodsId);
                orderGoods2.setGoodsNum(orderGoods.getGoodsNum() - num);
                orderGoodsDao.updateById(orderGoods2);
            }
        }

        List<OrderGoods> goodsList = orderGoodsDao.selectList(new QueryWrapper<OrderGoods>().eq("order_id", tbOrder.getOrderId()));
        if (goodsList.size() <= 0) {
            baseMapper.deleteById(tbOrder.getOrderId());
        } else {
            //重新统一计算订单价格
            BigDecimal orderMoney = BigDecimal.ZERO;
            for (OrderGoods goods : goodsList) {
                orderMoney = orderMoney.add(goods.getGoodsPrice().multiply(new BigDecimal(goods.getGoodsNum())).add(goods.getGoodsPack().multiply(new BigDecimal(goods.getGoodsNum()))));
            }
            tbOrder.setOrderMoney(orderMoney);
            baseMapper.updateById(tbOrder);
        }

        return Result.success();
    }

    @Transactional
    @Override
    public Result emptyShoppingTrolley(Long userId, Long shopId) {
        appOrderDao.delete(new QueryWrapper<TbOrder>().eq("user_id", userId).eq("shop_id", shopId).eq("status", 1));
        return Result.success();
    }

    @Transactional
    @Override
    public Result employCoupon(Long userId, Long parentId, Long couponId) {
        TbOrder tbOrder = appOrderDao.selectById(parentId);
        TbCouponUser tbCouponUser = tbCouponUserDao.selectById(couponId);

        TbOrder tbOrder1 = new TbOrder();
        tbOrder1.setOrderId(tbOrder.getOrderId());
        tbOrder1.setCouponId(tbCouponUser.getId());
        appOrderDao.updateById(tbOrder1);
        return Result.success();
    }

    @Override
    public Result deleteCouponByOrderId(Long orderId) {
        appOrderDao.deleteCouponByOrderId(orderId);
        return Result.success();
    }

    @Transactional
    @Override
    public Result updateOrder(TbOrder order) {
        if (order.getStatus() == 3) {
            TbOrder tbOrder = selectOrderByIds(order.getOrderId());
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setTitle(dataSourceMessageSource.getConfigI18ForCach("60154"));
            if (tbOrder.getOrderType() != null && tbOrder.getOrderType() == 1) {
                messageInfo.setContent(dataSourceMessageSource.getConfigI18ForCach("60155"));
            } else if (tbOrder.getOrderType() != null && tbOrder.getOrderType() == 2) {
                messageInfo.setContent(dataSourceMessageSource.getConfigI18ForCach("60156"));
            }
            messageInfo.setCreateAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            messageInfo.setUserId(tbOrder.getUserId().toString());
            messageInfoDao.insert(messageInfo);
            //设置小程序消息推送
            CommonInfo one = commonInfoService.findOne(267);
            List<String> msgList = new ArrayList<>();
            Long shopId = tbOrder.getShopId();
            GoodsShop goodsShop = goodsShopDao.selectById(shopId);
            String shopName = goodsShop.getShopName();
            String orderNum1 = tbOrder.getOrderNumber();
            msgList.add(shopName);
            msgList.add(orderNum1);
            msgList.add(dataSourceMessageSource.getConfigI18ForCach("60157"));
            msgList.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            if (tbOrder.getOrderType() != null && tbOrder.getOrderType() == 1) {
                msgList.add(dataSourceMessageSource.getConfigI18ForCach("60155"));
            } else if (tbOrder.getOrderType() == 2) {
                msgList.add(dataSourceMessageSource.getConfigI18ForCach("60156"));
            }
            UserEntity userEntity = userDao.selectById(tbOrder.getUserId());
            SenInfoCheckUtil.sendMsg(userEntity.getOpenId(), one.getValue(), msgList, 4);
        }
        if (order.getStatus() == 4) {
            //管理端完成订单，则消息通知用户
            //添加消息记录
            TbOrder tbOrder = appOrderDao.selectById(order.getOrderId());
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setTitle(dataSourceMessageSource.getConfigI18ForCach("60154"));
            messageInfo.setContent(dataSourceMessageSource.getConfigI18ForCach("60158"));
            messageInfo.setCreateAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            messageInfo.setUserId(tbOrder.getUserId().toString());
            messageInfoDao.insert(messageInfo);
            //设置小程序消息推送
            CommonInfo one = commonInfoService.findOne(269);
            List<String> msgList = new ArrayList<>();
            Long shopId = tbOrder.getShopId();
            GoodsShop goodsShop = goodsShopDao.selectById(shopId);
            String shopName = goodsShop.getShopName();
            String orderNum1 = tbOrder.getOrderNumber();
            msgList.add(dataSourceMessageSource.getConfigI18ForCach("60154"));
            msgList.add(orderNum1);
            msgList.add(shopName);
            msgList.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            UserEntity userEntity = userDao.selectById(tbOrder.getUserId());
            SenInfoCheckUtil.sendMsg(userEntity.getOpenId(), one.getValue(), msgList, 1);
        }
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        order.setUpdateTime(format);
        baseMapper.updateById(order);
        return Result.success();
    }

    @Transactional
    @Override
    public Result accomplishOrder(Long userId, Long orderId) {

        List<OrderGoods> orderGoodsList = orderGoodsDao.selectList(new QueryWrapper<OrderGoods>().eq("order_id", orderId));
        BigDecimal payMoney = BigDecimal.ZERO;
        for (OrderGoods orderGoods : orderGoodsList) {
            payMoney = payMoney.add(orderGoods.getGoodsPrice().multiply(new BigDecimal(orderGoods.getGoodsNum())));
        }

        TbOrder tbOrder1 = new TbOrder();
        tbOrder1.setOrderId(orderId);
        tbOrder1.setStatus(4);
        tbOrder1.setPayMoney(payMoney);
        tbOrder1.setPayTime(DateUtils.format(new Date()));
        appOrderDao.updateById(tbOrder1);
        TbOrder tbOrder = appOrderDao.selectById(orderId);
        if (tbOrder.getSeatId() != null) {
            seatService.updateSeatStatus(tbOrder.getSeatId());
        }
        //管理端完成订单，则消息通知用户
        //添加消息记录
        if (userId != null) {
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setTitle(dataSourceMessageSource.getConfigI18ForCach("60154"));
            messageInfo.setContent(dataSourceMessageSource.getConfigI18ForCach("60158"));
            messageInfo.setCreateAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            messageInfo.setUserId(userId.toString());
            messageInfoDao.insert(messageInfo);
            //设置小程序消息推送
            CommonInfo one = commonInfoService.findOne(269);
            List<String> msgList = new ArrayList<>();
            Long shopId = tbOrder.getShopId();
            GoodsShop goodsShop = goodsShopDao.selectById(shopId);
            String shopName = goodsShop.getShopName();
            String orderNum1 = tbOrder.getOrderNumber();
            msgList.add(dataSourceMessageSource.getConfigI18ForCach("60154"));
            msgList.add(orderNum1);
            msgList.add(shopName);
            msgList.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            UserEntity userEntity = userDao.selectById(userId);
            SenInfoCheckUtil.sendMsg(userEntity.getOpenId(), one.getValue(), msgList, 1);
        }

        return Result.success();
    }

    @Override
    public void orderEnd() {
        CommonInfo one = commonInfoService.findOne(268);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - Integer.valueOf(one.getValue()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(calendar.getTime());
        List<TbOrder> tbOrders = appOrderDao.selectOrderByTimeList(date);

        for (int i = 0; i < tbOrders.size(); i++) {
            //管理端完成订单，则消息通知用户
            //添加消息记录
            TbOrder tbOrder = appOrderDao.selectById(tbOrders.get(i).getOrderId());
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setTitle(dataSourceMessageSource.getConfigI18ForCach("60154"));
            messageInfo.setContent(dataSourceMessageSource.getConfigI18ForCach("60158"));
            messageInfo.setCreateAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            messageInfo.setUserId(tbOrder.getUserId().toString());
            messageInfoDao.insert(messageInfo);
            //设置小程序消息推送
            CommonInfo one1 = commonInfoService.findOne(264);
            List<String> msgList = new ArrayList<>();
            Long shopId = tbOrder.getShopId();
            GoodsShop goodsShop = goodsShopDao.selectById(shopId);
            String shopName = goodsShop.getShopName();
            String orderNum1 = tbOrder.getOrderNumber();
            msgList.add(dataSourceMessageSource.getConfigI18ForCach("60158"));
            msgList.add(orderNum1);
            msgList.add(shopName);
            msgList.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            UserEntity userEntity = userDao.selectById(tbOrder.getUserId());
            SenInfoCheckUtil.sendMsg(userEntity.getOpenId(), one1.getValue(), msgList, 1);
        }
        //将所有超时订单改成已完成
        appOrderDao.updateorderStatus(date);
    }

    @Transactional
    @Override
    public Result deleteOrderList(List<Integer> orderId) {
        for (int i = 0; i < orderId.size(); i++) {
            baseMapper.deleteById(orderId.get(i));
        }
        return Result.success();
    }

    @Override
    public Result selectBuyGoods(Long userId, Long orderId) {
        TbOrder tbOrder = appOrderDao.selectOne(new QueryWrapper<TbOrder>().eq("order_id", orderId).eq("user_id", userId));
        List<OrderGoods> orderGoodsList = orderGoodsDao.selectList(new QueryWrapper<OrderGoods>().eq("order_id", tbOrder.getOrderId()));
        tbOrder.setOrderGoodsList(orderGoodsList);
        return Result.success().put("data", tbOrder);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result adminCancelOrder(Long orderId, Long orderGoodsId) {
        BigDecimal refundMoney = BigDecimal.ZERO;
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        TbOrder tbOrder = appOrderDao.selectById(orderId);
        if (tbOrder.getIsPay() != 0) {
            refundMoney = getAllMoney(orderId, refundMoney);
            if (orderGoodsId != null) {
                OrderGoods orderGoods = orderGoodsDao.selectById(orderGoodsId);
                if (orderGoods.getStatus() != 1) {
                    return Result.error("60159");
                }
                refundMoney = orderGoods.getGoodsPrice().add(orderGoods.getGoodsPack()).multiply(new BigDecimal(orderGoods.getGoodsNum()));

            }
            //订单有优惠券的计算。
            if (tbOrder.getCouponId() != null) {
                TbCouponUser tbCouponUser = tbCouponUserDao.selectById(tbOrder.getCouponId());
                refundMoney = refundMoney.subtract(refundMoney.multiply(tbCouponUser.getMoney().divide(tbOrder.getOrderMoney(), 2, BigDecimal.ROUND_HALF_UP)));
            }
            if (orderGoodsId != null) {
                int goodCount = orderGoodsDao.selectCount(new QueryWrapper<OrderGoods>().eq("order_id", orderId).eq("status", 1).ne("id", orderGoodsId));
                if (goodCount <= 0) {
                    if (tbOrder.getErrandUser() == 2) {
                        refundMoney = refundMoney.add(tbOrder.getErrandMoney());
                    }
                }
                //没有订单商品关联id表示是退款或取消订单
            } else {
                if (tbOrder.getErrandUser() == 2) {
                    refundMoney = refundMoney.add(tbOrder.getErrandMoney());
                }
            }

            //已支付判断支付方式
            if (tbOrder.getPayType() == 2) {
                //余额支付，直接退还钱包
                userMoneyDao.updateMayMoney(1, tbOrder.getUserId(), refundMoney.doubleValue());
            } else {
                //微信支付调支付退款接口
                PayDetails payDetails = detailsService.getOne(new QueryWrapper<PayDetails>().eq("order_id", tbOrder.getOrderNumber()));
                if (!wxService.wxRefund(payDetails, refundMoney)) {
                    return Result.error("60160");
                }

            }
            //如果是退款商品，只把当前商品改为已退款
            if (orderGoodsId != null) {
                OrderGoods orderGoods = orderGoodsDao.selectById(orderGoodsId);
                orderGoods.setStatus(2);
                orderGoodsDao.updateById(orderGoods);
                //恢复库存
                GoodsShopRelevancy goodsShopRelevancy = goodsShopRelevancyDao.selectOne(new QueryWrapper<GoodsShopRelevancy>().eq("goods_id", orderGoods.getGoodsId()).eq("shop_id", tbOrder.getShopId()));
                goodsShopRelevancy.setInventory(goodsShopRelevancy.getInventory() + orderGoods.getGoodsNum());
                goodsShopRelevancyDao.updateById(goodsShopRelevancy);
                //如果是订单退款，则把订单全部商品修改为已退款
            } else {
                //恢复库存
                List<OrderGoods> orderGoodsList = orderGoodsDao.selectList(new QueryWrapper<OrderGoods>().eq("order_id", tbOrder.getOrderId()));
                for (OrderGoods orderGoods : orderGoodsList) {
                    GoodsShopRelevancy goodsShopRelevancy = goodsShopRelevancyDao.selectOne(new QueryWrapper<GoodsShopRelevancy>().eq("goods_id", orderGoods.getGoodsId()).eq("shop_id", tbOrder.getShopId()));
                    goodsShopRelevancy.setInventory(goodsShopRelevancy.getInventory() + orderGoods.getGoodsNum());
                    goodsShopRelevancyDao.updateById(goodsShopRelevancy);
                }
                OrderGoods orderGoods = new OrderGoods();
                orderGoods.setStatus(2);
                orderGoodsDao.update(orderGoods, new QueryWrapper<OrderGoods>().eq("order_id", orderId));
            }
            //如果订单的所有商品都已经退款,则将订单改为已取消
            Integer count = orderGoodsDao.selectCount(new QueryWrapper<OrderGoods>().eq("order_id", orderId).eq("status", 1));
            if (count <= 0) {
                tbOrder.setStatus(5);
                appOrderDao.updateById(tbOrder);
                TbCouponUser tbCouponUser = new TbCouponUser();
                tbCouponUser.setStatus(0);
                tbCouponUser.setId(tbOrder.getCouponId());
                tbCouponUserDao.updateById(tbCouponUser);
            }
//            tbOrder.setCancelMoney(tbOrder.getCancelMoney().add(refundMoney));
            tbOrder.setCancelMoney(refundMoney);
            appOrderDao.updateById(tbOrder);
            if (refundMoney.compareTo(BigDecimal.ZERO) > 0) {
                //添加退款记录
                UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
                userMoneyDetails.setUserId(tbOrder.getUserId());
                userMoneyDetails.setTitle(dataSourceMessageSource.getConfigI18ForCach("60161"));
                userMoneyDetails.setType(1);
                userMoneyDetails.setState(2);
                userMoneyDetails.setMoney(refundMoney);
                userMoneyDetails.setContent(dataSourceMessageSource.getConfigI18ForCach("60162", String.valueOf(refundMoney)));
                userMoneyDetails.setCreateTime(format);
                userMoneyDetailsDao.insert(userMoneyDetails);

            }
            //设置小程序消息推送
            CommonInfo one = commonInfoService.findOne(266);
            List<String> msgList = new ArrayList<>();
            msgList.add(tbOrder.getOrderNumber());
            if (orderGoodsId != null) {
                msgList.add(dataSourceMessageSource.getConfigI18ForCach("60163"));
            } else {
                msgList.add(dataSourceMessageSource.getConfigI18ForCach("60164"));
            }
            UserEntity userEntity = userDao.selectById(tbOrder.getUserId());
            msgList.add(refundMoney.toString());
            msgList.add(format);
            if (StringUtils.isNotBlank(userEntity.getOpenId())) {
                SenInfoCheckUtil.sendMsg(userEntity.getOpenId(), one.getValue(), msgList, 3);
            }

            return Result.success();
        } else {
            tbOrder.setStatus(5);
            appOrderDao.updateById(tbOrder);
        }
        return Result.success();
    }


    @Override
    public Result selectOrderList(Long userId, Long shopId, Integer status, Integer orderType, String languageType) {
        HashMap<String, Object> hashMap = new HashMap<>();
        List<TbOrder> tbOrderList = baseMapper.selectOrderList(userId, status, orderType, shopId);
        BigDecimal money = new BigDecimal(0);
        ArrayList list = new ArrayList();
        for (int i = 0; i < tbOrderList.size(); i++) {
            List<OrderGoods> orderGoodsList = orderGoodsDao.selectList(new QueryWrapper<OrderGoods>()
                    .eq("order_id", tbOrderList.get(i).getOrderId()));
            for (int a = 0; a < orderGoodsList.size(); a++) {
                BigDecimal goodsPrice = orderGoodsList.get(a).getGoodsPrice().setScale(2, BigDecimal.ROUND_HALF_UP);
                Integer goodsNum = orderGoodsList.get(a).getGoodsNum();
                BigDecimal multiply = goodsPrice.multiply(new BigDecimal(goodsNum));
                money = money.add(multiply);
            }
            list.add(orderGoodsList);
            tbOrderList.get(i).setOrderGoodsList(list);
        }

        hashMap.put("tbOrderList", tbOrderList);
        hashMap.put("money", money);
        return Result.success().put("data", hashMap);
    }

    public String getGeneralOrder() {
        Date date = new Date();
        String newString = String.format("%0" + 4 + "d", (int) ((Math.random() * 9 + 1) * 1000));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(date);
        return format + newString;
    }

    @Override
    public TbOrder queryInfoByOrderNum(String orderNum) {
        TbOrder tbOrder = baseMapper.queryByOrderNum(orderNum);
        return tbOrder;
    }

    @Override
    public void completeOrder(Long orderId) {
        TbOrder tbOrder = this.getById(orderId);
        if (Objects.isNull(tbOrder)) {
            throw new SqxException("订单信息不存在");
        }

        if (3 != tbOrder.getOrderType()) {
            throw new SqxException("只能完成桌码订单");
        }

        this.update(new TbOrder(), new LambdaUpdateWrapper<TbOrder>()
                .eq(TbOrder::getOrderId,orderId)
                .set(TbOrder::getStatus, 4)
        );
    }
}
