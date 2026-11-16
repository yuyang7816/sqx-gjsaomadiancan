package com.sqx.modules.order.entity.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrdersGoodsVo implements Serializable {

    private static final long serialVersionUID = 1L;
    //商品id
    private Long goodsId;
    //商品skuId
    private Long skuId;
    //sku信息
    private String skuMessage;
    //数量
    private Integer num;
    //商品备注
    private String goodRemarks;
}
