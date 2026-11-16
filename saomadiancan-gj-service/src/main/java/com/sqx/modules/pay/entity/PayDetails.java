package com.sqx.modules.pay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 充值记录
 *
 * @author fang 2020-05-14
 */
@Data
@TableName("pay_details")
public class PayDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 充值记录id
     */
    @TableId(type = IdType.INPUT)
    private Long id;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 分类 1支付订单  2充值
     */
    private Integer classify;

    /**
     * 支付类型 1app支付  2公众号支付  3小程序支付 7paypal支付 8stripe支付
     */
    private Integer type;

    /**
     * 充值金额
     */
    private Double money;

    /**
     * 0待支付 1支付成功 2失败
     */
    private Integer state;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 支付时间
     */
    private String payTime;

    /**
     * 备注
     */
    private String remark;
    /**
     * 退款单号
     */
    private String refundNumber;

    /**
     * 关联id
     */
    private Long relationId;

    /**
     * 支付宝交易订单号
     */
    private String tradeNo;
    /*
    * stripeId
    * */
    private String stripeId;

    /**
     * 扩展参数
     * 当前存储:
     * {
     * clientId: "客户端id",
     * redirectUri: "paypal支付成功后重定向地址"
     * }
     */
    private String extend;

    @TableField(exist = false)
    private String refundContent;

    @TableField(exist = false)
    private String outRequestNo;

}
