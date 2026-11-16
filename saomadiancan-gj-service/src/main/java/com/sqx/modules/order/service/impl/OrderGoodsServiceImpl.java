package com.sqx.modules.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.modules.order.dao.OrderGoodsDao;
import com.sqx.modules.order.entity.OrderGoods;
import com.sqx.modules.order.service.OrderGoodsService;
import org.springframework.stereotype.Service;

@Service
public class OrderGoodsServiceImpl extends ServiceImpl<OrderGoodsDao, OrderGoods> implements OrderGoodsService {
}
