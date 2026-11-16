package com.sqx.modules.iPay.enums;

/**
 * @author chengming
 * @since 2025/4/14
 */
public enum BusinessTypeEnum {

    ORDER(1, "订单"),
    RECHARGE(2, "充值"),
    VIP(3, "购买会员")
    ;

    private final Integer code;
    private final String desc;

    BusinessTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
