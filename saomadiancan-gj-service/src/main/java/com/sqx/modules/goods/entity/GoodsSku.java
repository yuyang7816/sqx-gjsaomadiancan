package com.sqx.modules.goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author fang
 * @date 2021.9.14
 * 商品sku
 */

@Data
@ApiModel("goods_sku")
public class GoodsSku implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)

    @ApiModelProperty("id")
    private Long id;


    @ApiModelProperty("商品id")
    private Long goodsId;


    @ApiModelProperty("sku图片")
    private String skuImg;


    @ApiModelProperty("sku原价")
    private BigDecimal skuOriginalPrice;


    @ApiModelProperty("sku商品售价")
    private BigDecimal skuPrice;


    @ApiModelProperty("库存")
    private Integer stock;


    /*@ApiModelProperty("销量")
    private Integer sales;*/


    @ApiModelProperty("sku信息，json封装")
    private String detailJson;

    @TableField(exist = false)
    private String languageType;

    public GoodsSku() {}
}