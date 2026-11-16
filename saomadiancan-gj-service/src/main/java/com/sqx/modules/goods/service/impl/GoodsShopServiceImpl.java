package com.sqx.modules.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.goods.dao.GoodsShopDao;
import com.sqx.modules.goods.dao.GoodsShopRelevancyDao;
import com.sqx.modules.goods.entity.Goods;
import com.sqx.modules.goods.entity.GoodsShop;
import com.sqx.modules.goods.entity.GoodsShopRelevancy;
import com.sqx.modules.goods.service.GoodsAttrService;
import com.sqx.modules.goods.service.GoodsShopService;
import com.sqx.modules.goods.service.GoodsSkuService;
import com.sqx.modules.order.dao.OrderGoodsDao;
import com.sqx.modules.order.entity.OrderGoods;
import com.sqx.modules.order.entity.TbOrder;
import com.sqx.modules.utils.fieYun.FeiYunUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class GoodsShopServiceImpl extends ServiceImpl<GoodsShopDao, GoodsShop> implements GoodsShopService {

    @Autowired
    private GoodsShopDao goodsShopDao;
    @Autowired
    private GoodsShopRelevancyDao goodsShopRelevancyDao;
    @Autowired
    private OrderGoodsDao orderGoodsDao;
    @Autowired
    private GoodsAttrService goodsAttrService;
    @Autowired
    private GoodsSkuService goodsSkuService;

    @Override
    public Result insertGoodsShop(GoodsShop goodsShop) {
        if (StringUtils.isNotEmpty(goodsShop.getSnCode())) {
            String s = FeiYunUtils.addPrinter(goodsShop.getFacility(), goodsShop.getSnCode(), goodsShop.getValue(), goodsShop.getShopName());
            if (!"ok".equals(s)) {
                return Result.error(s);
            }
        }
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        goodsShop.setCreateTime(format);
        baseMapper.insert(goodsShop);
        return Result.success();
    }

    @Override
    public Result updateGoodsShop(GoodsShop goodsShop) {
        GoodsShop goodsShop1 = goodsShopDao.selectById(goodsShop.getShopId());
        if (StringUtils.isNotEmpty(goodsShop.getSnCode())) {
            //判断下本次和上次的一样吗 如果一样则不做校验
            if (StringUtils.isNotEmpty(goodsShop1.getSnCode()) && StringUtils.isNotEmpty(goodsShop1.getValue())) {
                if (!goodsShop1.getSnCode().equals(goodsShop.getSnCode()) || !goodsShop1.getValue().equals(goodsShop.getValue())) {
                    String s = FeiYunUtils.addPrinter(goodsShop.getFacility(), goodsShop.getSnCode(), goodsShop.getValue(), goodsShop.getShopName());
                    if (!"ok".equals(s)) {
                        return Result.error(s);
                    }
                    //删除之前的打印机
                    String snlist = goodsShop1.getSnCode();
                    FeiYunUtils.deletePrinter(snlist);
                }
            }
        } else {
            //判断之前有没有写过打印机 如果写过 则删除
            if (StringUtils.isNotEmpty(goodsShop1.getSnCode())) {
                //删除之前的打印机
                String snlist = goodsShop1.getSnCode();
                FeiYunUtils.deletePrinter(snlist);
            }
        }
        baseMapper.updateById(goodsShop);
        return Result.success();
    }

    @Override
    public Result deleteGoodsShop(Long id) {
        baseMapper.deleteById(id);
        return Result.success();
    }

    @Override
    public Result selectGoodsByShopId(Long shopId, Integer page, Integer limit, String goodsName, Integer classifyId, Integer status, String languageType) {
        Page<Goods> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        IPage<Goods> goodsIPage = goodsShopDao.selectGoodsByShopId(pages, shopId, goodsName, classifyId, status, languageType);
        for (Goods record : goodsIPage.getRecords()) {
            record.setSku(goodsSkuService.selectGoodsSkuByGoodsId(record.getGoodsId(), languageType));
            record.setAttr(goodsAttrService.findByGoodsId(record.getGoodsId(), languageType));
        }
        PageUtils pageUtils = new PageUtils(goodsIPage);
        return Result.success().put("data", pageUtils);
    }

    @Override
    public Result selectGoodsByShopIdAndGoodsId(Long shopId, Long goodsId, String languageType) {
        Goods goods = goodsShopDao.selectGoodsByShopIdAndGoodsId(shopId, goodsId);
        if (goods != null) {
            goods.setAttr(goodsAttrService.findByGoodsId(goodsId, languageType));
            goods.setSku(goodsSkuService.selectGoodsSkuByGoodsId(goodsId, languageType));
            return Result.success().put("data", goods);
        }
        return Result.success().put("data", goods);
    }


    @Override
    public Result selectGoodsByNotShopId(Long shopId, Integer page, Integer limit, String goodsName) {
        Page<Goods> pages = new Page<>(page, limit);
        PageUtils pageUtils = new PageUtils(goodsShopDao.selectGoodsByNotShopId(pages, shopId, goodsName));
        return Result.success().put("data", pageUtils);
    }

    @Transactional
    @Override
    public Result addGoodsByShopId(List<GoodsShopRelevancy> goodsShopRelevancyList) {
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        goodsShopDao.addGoodsByShopId(goodsShopRelevancyList, date);
        return Result.success();
    }

    @Override
    public Result deleteGoodsByShopId(Long shopId, Long goodsId) {
        goodsShopDao.deleteGoodsByShopId(shopId, goodsId);
        return Result.success();
    }

    @Transactional
    @Override
    public Result soldOutOrPutaway(Long shopId, Long goodsId, Integer status) {
        goodsShopDao.soldOutOrPutaway(shopId, goodsId, status);
        return Result.success();
    }

    @Override
    public Result updateGoodsByShopId(GoodsShopRelevancy goodsShopRelevancy) {
        goodsShopRelevancyDao.update(goodsShopRelevancy, new QueryWrapper<GoodsShopRelevancy>()
                .eq("shop_id", goodsShopRelevancy.getShopId())
                .eq("goods_id", goodsShopRelevancy.getGoodsId()));
        return Result.success();
    }

    @Override
    public Result selectStoreData(Long shopId, String date, String dateType) {
        HashMap<String, Object> hashMap = new HashMap<>();
        int count = goodsShopDao.selectStoreCount(shopId, date, dateType);
        BigDecimal money = goodsShopDao.selectStoreMoney(shopId, date, dateType);
        //到店订单数
        int takeCount = goodsShopDao.selectTakeCount(1, date, dateType, shopId);
        //外卖订单数
        int takeCount2 = goodsShopDao.selectTakeCount(2, date, dateType, shopId);
        //到店订单金额
        BigDecimal takeMoney = goodsShopDao.selectTakeMoney(1, date, dateType, shopId);
        //外卖订单金额
        BigDecimal takeMoney1 = goodsShopDao.selectTakeMoney(2, date, dateType, shopId);
        //退款订单数
        int cancelOrderCount = goodsShopDao.cancelOrderCount(date, dateType, shopId);
        //退款金额
        BigDecimal cancelOrderMoney = goodsShopDao.cancelOrderMoney(date, dateType, shopId);
        hashMap.put("count", count);
        hashMap.put("money", money);
        hashMap.put("takeCount", takeCount);
        hashMap.put("takeCount2", takeCount2);
        hashMap.put("takeMoney", takeMoney);
        hashMap.put("takeMoney1", takeMoney1);
        hashMap.put("cancelOrderCount", cancelOrderCount);
        hashMap.put("cancelOrderMoney", cancelOrderMoney);
        return Result.success().put("hashMap", hashMap);
    }

    @Override
    public Result selectStoreMessage(Long shopId, String date, String dateType, Integer page, Integer limit, String orderNumber,Integer status) {
        Page<TbOrder> pages = new Page<>(page, limit);
        IPage<TbOrder> tbOrderIPage = goodsShopDao.selectStoreMessage(pages, shopId, date, dateType, orderNumber,status);
        List<TbOrder> records = tbOrderIPage.getRecords();
        for (int i = 0; i < records.size(); i++) {
            List<OrderGoods> orderGoodsList = orderGoodsDao.selectList(new QueryWrapper<OrderGoods>()
                    .eq("order_id", records.get(i).getOrderId()));
            tbOrderIPage.getRecords().get(i).setOrderGoodsList(orderGoodsList);
        }
        PageUtils pageUtils = new PageUtils(tbOrderIPage);
        return Result.success().put("data", pageUtils);
    }


}
