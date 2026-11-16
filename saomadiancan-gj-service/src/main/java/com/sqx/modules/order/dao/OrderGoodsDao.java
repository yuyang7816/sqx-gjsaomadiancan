package com.sqx.modules.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.order.entity.OrderGoods;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderGoodsDao extends BaseMapper<OrderGoods> {
}
