package com.sqx.modules.sys.form;

import lombok.Data;

/**
 * 登录表单
 *
 */
@Data
public class SysLoginForm {
    private String username;
    private String password;
    private String captcha;
    private String uuid;
    //为了区分店铺管理员和总后台登录。店铺管理员传2 其他不传
    private Integer userType;


}
