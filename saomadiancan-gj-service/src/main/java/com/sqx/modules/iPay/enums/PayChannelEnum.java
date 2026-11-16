package com.sqx.modules.iPay.enums;

/**
 * @author chengming
 * @since 2025/4/14
 */
public enum PayChannelEnum {
    WEB(1, "WEB支付"),
    APP(2, "APP支付")
    ;

    private final Integer code;
    private final String desc;

    PayChannelEnum(Integer code, String desc) {
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
