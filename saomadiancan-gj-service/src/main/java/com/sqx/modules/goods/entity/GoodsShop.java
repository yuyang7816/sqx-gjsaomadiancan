package com.sqx.modules.goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("goods_shop")
public class GoodsShop implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)

    @ApiModelProperty("shop_id")
    private Integer shopId;


    @ApiModelProperty("商铺名")
    private String shopName;

    @ApiModelProperty("详细地址")
    private String detailedAddress;

    @ApiModelProperty("商铺维度")
    private Double shopLat;


    @ApiModelProperty("商铺经度")
    private Double shopLng;

    @ApiModelProperty("开门时间")
    private String openTime;

    @ApiModelProperty("关门时间")
    private String closeTime;

    @ApiModelProperty("商户banner图片")
    private String shopBanner;

    @ApiModelProperty("创建店铺时间")
    private String createTime;

    @ApiModelProperty("店铺收款二维码")
    private String qrCode;

    @ApiModelProperty("收银台金额")
    private BigDecimal income;

    /**
     * 上班还是打烊 1上班 2打烊
     */
    private Integer putawayFlag;

    /**
     * 设备类型 1飞鹅 2pos机
     */
    private Integer facility;

    /**
     * sn码 飞鹅打印机参数
     */
    private String snCode;

    /**
     * value 飞鹅打印机参数
     */
    private String value;

    @TableField(exist = false)
    private Double distancess;

    @TableField(exist = false)
    private String province;

    @TableField(exist = false)
    private String city;

    @TableField(exist = false)
    private String district;

    public GoodsShop() {}
}