package com.sqx.modules.order.entity.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OrderVo implements Serializable {

    private static final long serialVersionUID = 1L;

    Long shopId;
    Integer orderType;
    Integer isSeat;
    Long seatId;
    Long userId;

    List<OrdersGoodsVo> ordersGoodsVoList;

}
