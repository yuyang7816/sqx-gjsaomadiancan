package com.sqx.modules.goods.i18n.attrValue.domain.entity;

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
 * 规格属性表
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-19 17:42:39
 */
@Getter
@Setter
@TableName("goods_attr_value_i18n")
@ApiModel(value = "GoodsAttrValueI18n对象", description = "规格属性表")
public class GoodsAttrValueI18n implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("属性id")
    @TableField("attr_value_id")
    private Long attrValueId;

    @ApiModelProperty("属性值组合:{尺寸: '7寸', 颜色: '红底'}")
    @TableField("detail")
    private String detail;

    @ApiModelProperty("规格属性名称")
    @TableField("value")
    private String value;

    @ApiModelProperty("语言类型")
    @TableField("language_type")
    private String languageType;

    @ApiModelProperty("语言名称")
    @TableField("language_name")
    private String languageName;

}
