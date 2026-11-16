package com.sqx.modules.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sqx.modules.languageType.service.LanguageFillable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName("vip_details")
@ApiModel("会员详情")
public class VipDetails implements Serializable, LanguageFillable {

    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("会员类型")
    @TableField("vip_name_type")
    private Integer vipNameType;

    @ApiModelProperty("会员价格")
    private BigDecimal money;
    @TableField
    private String vipName;

    /**
     * 语种
     */
    @ApiModelProperty("语种")
    @TableField(value = "language_type")
    private String languageType;

    /**
     * 语言名称
     */
    @ApiModelProperty("语言名称")
    @TableField(exist = false)
    private String languageName;
}
