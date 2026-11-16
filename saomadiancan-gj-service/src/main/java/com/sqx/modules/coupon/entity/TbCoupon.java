package com.sqx.modules.coupon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("tb_coupon")
public class TbCoupon implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)

    @ApiModelProperty("coupon_id")
    private Long couponId;


    @ApiModelProperty("优惠券名称")
    private String couponName;


    @ApiModelProperty("优惠券图片")
    private String couponPicture;


    @ApiModelProperty("有效期天数")
    private Integer endDate;


    @ApiModelProperty("优惠券可使用订单最低金额")
    private BigDecimal minMoney;


    @ApiModelProperty("优惠券可使用商品类型")
    private String goodsType;


    @ApiModelProperty("购买此优惠券需要积分数")
    private Integer needIntegral;


    @ApiModelProperty("优惠券金额")
    private BigDecimal money;


    @ApiModelProperty("是否删除 0未删除 1已删除")
    private Integer deleteFlag;

    public TbCoupon() {}
}