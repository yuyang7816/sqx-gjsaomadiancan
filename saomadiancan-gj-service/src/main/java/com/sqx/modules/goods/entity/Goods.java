package com.sqx.modules.goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel("商品")
@TableName("goods")
public class Goods implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("goods_id")
    private Long goodsId;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("商品描述")
    private String goodsDescribe;

    @ApiModelProperty("商品简介")
    private String goodsSynopsis;

    /*@ApiModelProperty("商品类型")
    private String goodsType;*/

    @ApiModelProperty("商品分类（商品左侧菜单）")
    private Integer classifyId;

    @ApiModelProperty("商品封面图")
    private String goodsCover;

    @ApiModelProperty("商品图片")
    private String goodsPicture;

    @ApiModelProperty("商品标签")
    private String goodsLabel;

    @ApiModelProperty("上架时间")
    private String createTime;

    @ApiModelProperty("销量")
    private Integer salesVolume;

    @ApiModelProperty("是否上架 0上架  1下架")
    private Integer status;

    @ApiModelProperty("打包费")
    private BigDecimal packMoney;

    @ApiModelProperty("商品实际价格")
    private BigDecimal goodsMoney;

    @ApiModelProperty("商品原价")
    private BigDecimal originalMoney;

    @ApiModelProperty("门店id")
    private Long shopId;

    @TableField(exist = false)
    private List<GoodsSku> goodsSkuList;


    @ApiModelProperty("商品规格")
    @TableField(exist = false)
    private List<GoodsAttr> attr;

    @ApiModelProperty("商品sku")
    @TableField(exist = false)
    private List<GoodsSku> sku;

    @TableField(exist = false)
    private String classifyName;

    @ApiModelProperty("商品销量")
    @TableField(exist = false)
    private Integer sales;

    @ApiModelProperty("商品库存")
    @TableField(exist = false)
    private Integer inventory;

    @ApiModelProperty("商铺-商品状态")
    @TableField(exist = false)
    private Integer gsrStatus;

    @TableField(exist = false)
    private String languageType;
    @TableField(exist = false)
    private String languageName;

}