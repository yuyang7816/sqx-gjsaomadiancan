package com.sqx.modules.goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("goods_shop_relevancy")
public class GoodsShopRelevancy implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)

    @ApiModelProperty("id")
    private Long id;


    @ApiModelProperty("商户id")
    private Long shopId;


    @ApiModelProperty("商品id")
    private Long goodsId;

    @ApiModelProperty("销量")
    private Integer sales;

    @ApiModelProperty("库存")
    private Integer inventory;

    @ApiModelProperty("商铺是否上架  0上架  1下架")
    private Integer status;

    @ApiModelProperty("添加时间")
    private String createTime;

    public GoodsShopRelevancy() {}
}