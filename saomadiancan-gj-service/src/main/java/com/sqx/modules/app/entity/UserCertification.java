package com.sqx.modules.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liyuan
 * @description user_certification
 * @date 2021-08-13
 */
@Data
@ApiModel("user_certification")
public class UserCertification implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * 用户实名认证id
     */
    @ApiModelProperty("用户实名认证id")
    private Long id;

    /**
     * 真实姓名
     */
    @ApiModelProperty("真实姓名")
    private String name;

    /**
     * 身份证号码
     */
    @ApiModelProperty("身份证号码")
    private String idNumber;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

    @TableField(exist = false)
    private UserEntity userEntity;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private String createTime;
    /**
     * 正面
     */
    private String front;
    /**
     * 反面
     */
    private String back;



    /**
     * 状态
     */
    private Long status;

    /**
     * 说明
     */
    private String remek;

    /**
     * 修改时间
     */
    private String updateTime;

    public UserCertification() {
    }
}
