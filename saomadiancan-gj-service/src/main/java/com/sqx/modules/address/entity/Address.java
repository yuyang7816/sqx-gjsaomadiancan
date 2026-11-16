package com.sqx.modules.address.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)

    @ApiModelProperty("主键id")
    private Long addressId;


    @ApiModelProperty("用户id")
    private Long userId;


    @ApiModelProperty("用户姓名")
    private String userName;


    @ApiModelProperty("用户电话")
    private String userPhone;


    @ApiModelProperty("用户地址")
    private String address;


    @ApiModelProperty("用户详细地址（用户手动输入的地址）")
    private String addressDetail;

    @ApiModelProperty("经度")
    private Double lng;

    @ApiModelProperty("维度")
    private Double lat;


    @ApiModelProperty("创建时间")
    private String createTime;


    @ApiModelProperty("是否删除（0：未删除；1：删除）")
    private Integer deleteFlag;


    @ApiModelProperty("默认地址 0：非默认  1：默认")
    private Integer addressDefault;


    @ApiModelProperty("修改时间")
    private String updateTime;

    public Address() {}
}