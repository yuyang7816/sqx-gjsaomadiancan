package com.sqx.modules.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName("user_money")
@ApiModel("用户钱包")
public class UserMoney implements Serializable {
    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    @TableField("user_id")
    private Long userId;

    /**
     * 钱包金额
     */
    @ApiModelProperty("钱包金额")
    private BigDecimal money;

    /**
     * 累计收入金额
     *//*
    @ApiModelProperty("累计收入金额")
    @TableField("income_money")
    private BigDecimal IncomeMoney;

    *//**
     * 累计提现金额
     *//*
    @ApiModelProperty("累计提现金额")
    @TableField("withdraw_deposit_money")
    private BigDecimal withdrawDepositMoney;*/


}
