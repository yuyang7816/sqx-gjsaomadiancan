package com.sqx.modules.goods.i18n.ruleValue.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 商品规格属性表
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-22 09:09:15
 */
@Getter
@Setter
@TableName("goods_rule_value_i18n")
@ApiModel(value = "GoodsRuleValueI18n对象", description = "商品规格属性表")
public class GoodsRuleValueI18n implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("规格id")
    @TableField("rule_value_id")
    private Long ruleValueId;

    @ApiModelProperty("规格属性值")
    @TableField("detail")
    private String detail;

    @ApiModelProperty("规格属性名称")
    @TableField("value")
    private String value;

    @ApiModelProperty("语种")
    @TableField("language_type")
    private String languageType;

    @ApiModelProperty("语言名称")
    @TableField("language_name")
    private String languageName;

}
