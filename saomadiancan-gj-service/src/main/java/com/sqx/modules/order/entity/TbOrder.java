package com.sqx.modules.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@TableName(value = "tb_order")
public class TbOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)

    @ApiModelProperty("order_id")
    private Long orderId;


    @ApiModelProperty("优惠券id")
    @TableField(value = "coupon_id")
    private Long couponId;


    @ApiModelProperty("用户id")
    private Long userId;


    @ApiModelProperty("用户名")
    private String userName;


    @ApiModelProperty("手机号")
    private String phone;


    @ApiModelProperty("地址信息")
    private String address;


    @ApiModelProperty("订单号")
    private String orderNumber;


    @ApiModelProperty("是否支付 0未支付 1已支付")
    private Integer isPay;


    @ApiModelProperty("承担跑腿费的角色 1商家 2用户")
    private Integer errandUser;


    @ApiModelProperty("下单时间")
    private String createTime;

    @ApiModelProperty("最后一次修改订单状态时间")
    private String updateTime;


    @ApiModelProperty("支付时间")
    private String payTime;


    @ApiModelProperty("支付方式  1微信支付  2余额支付")
    private Integer payType;


    @ApiModelProperty("取餐号码")
    private String orderCode;


    @ApiModelProperty("订单类型 1上门 2外卖 3桌码订单")
    private Integer orderType;


    @ApiModelProperty("订单父id")
    private Long parentId;


    @ApiModelProperty("打包费")
    private BigDecimal packMoney;

    @ApiModelProperty("退款金额")
    private BigDecimal cancelMoney;


    @ApiModelProperty("跑腿费")
    private BigDecimal errandMoney;


    @ApiModelProperty("订单状态 0待结算 1待支付 2已支付 3待取餐/派送中 4已完成 5已取消")
    private Integer status;


    @ApiModelProperty("是否删除该订单 0未删除 1删除")
    private Integer deleteFlag;

    @ApiModelProperty("支付金额")
    private BigDecimal payMoney;

    @ApiModelProperty("创建拼单人id")
    private Long parentUserId;

/*    @ApiModelProperty("购买数量")
    private Integer num;*/

    @ApiModelProperty("商铺id")
    private Long shopId;
    @ApiModelProperty("桌码id")
    private Long seatId;
    @ApiModelProperty("是否是桌码单 0否 1是")
    private Integer isSeat;
    @ApiModelProperty("订单价格")
    private BigDecimal orderMoney;
    @ApiModelProperty("订单备注")
    private String orderRemarks;

    @ApiModelProperty("商铺名")
    @TableField(exist = false)
    private String shopName;

    @TableField(exist = false)
    private List<OrderGoods> orderGoodsList;

    @TableField(exist = false)
    private String avatar;

    @TableField(exist = false)
    private Integer countOrder;

    @TableField(exist = false)
    private BigDecimal skuPrice;

    @TableField(exist = false)
    private BigDecimal couponMoney;

    @TableField(exist = false)
    private String classifyName;

    @TableField(exist = false)
    private Integer Rank;

    @TableField(exist = false)
    private BigDecimal shopMoney;

    public TbOrder() {
    }
}
