package com.sqx.modules.integral.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("user_integral_details")
public class UserIntegralDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)

    @ApiModelProperty("积分详情id")
    private Integer id;


    @ApiModelProperty("内容")
    private String content;


    @ApiModelProperty("获取类型 1签到 2积分兑换优惠券")
    private Integer classify;


    @ApiModelProperty("分类 1增加 2减少")
    private Integer type;


    @ApiModelProperty("数量")
    private Integer num;


    @ApiModelProperty("用户id")
    private Long userId;


    @ApiModelProperty("创建时间")
    private String createTime;

    @ApiModelProperty("连续签到天数")
    private Integer day;

    private String languageType;

    private String languageName;

    public UserIntegralDetails() {}
}