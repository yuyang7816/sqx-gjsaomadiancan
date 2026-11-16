package com.sqx.modules.iPay.enums;

/**
 * @author chengming
 * @since 2025/4/14
 */
public enum PayPlatformEnum {

    WECHAT(1, "微信"),
    ALI(2, "支付宝"),
    PAYPAL(7, "paypal"),
    STRIPE(8, "stripe"),
    ;

    private final Integer code;
    private final String desc;

    PayPlatformEnum(Integer code, String desc) {
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
