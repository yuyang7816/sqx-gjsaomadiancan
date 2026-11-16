package com.sqx.modules.iPay.param;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author chengming
 * @since 2025/4/22
 *
 * 业务参数
 */
@Data
public class BusinessParam {

    /**
     * 客户端id
     */
    @NotNull(message = "客户端id不能为空")
    private Integer clientId;

    /**
     * 业务类型 1-下单 2-充值金额 3-购买会员
     */
    @NotNull(message = "业务类型不能为空")
    private Integer businessType;

    /**
     * 支付成功后重定向uri
     */
    private String redirectUri;

    /**
     * 订单金额
     */
    @NotNull(message = "订单金额不能为空")
    @Min(value = 0L, message = "金额不能小于0")
    private BigDecimal amount;

    /**
     * 支付渠道 1-web支付 2-app支付
     */
    private Integer payChannel;

    /**
     * 用户id
     */
    private Long userId;

    /* 其他业务参数 */

    /* 会员业务 */
    // 会员id
    private Long vipId;

    /* 下单业务 */
    // 订单号
    private String outTraceNo;

    // 优惠券id
    private Long couponId;

    // 地址id
    private Long addressId;

    // 订单类型
    private Integer orderType;
}
