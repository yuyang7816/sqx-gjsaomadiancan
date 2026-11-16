package com.sqx.modules.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("order_goods")
public class OrderGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)

    @ApiModelProperty("主键id")
    private Long id;


    @ApiModelProperty("订单id")
    private Long orderId;


    @ApiModelProperty("商品名字")
    private String goodsName;


    @ApiModelProperty("规格信息")
    private String skuMessage;


    @ApiModelProperty("规格id")
    private Long skuId;


    @ApiModelProperty("商品价格")
    private BigDecimal goodsPrice;


    @ApiModelProperty("商品打包费")
    private BigDecimal goodsPack;


    @ApiModelProperty("商品数量")
    private Integer goodsNum;


    @ApiModelProperty("商品封面")
    private String goodsPicture;


    @ApiModelProperty("商品id")
    private Long goodsId;
    /**
     * 0未付款 1已付款 2已退款
     */
    private Integer status;
    /**
     * 商品备注
     */
    private String goodRemarks;

    public OrderGoods() {
    }
}
