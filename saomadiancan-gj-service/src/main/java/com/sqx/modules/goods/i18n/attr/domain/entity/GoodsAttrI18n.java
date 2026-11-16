package com.sqx.modules.goods.i18n.attr.domain.entity;

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
 * 商品规格表
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-19 17:42:21
 */
@Getter
@Setter
@TableName("goods_attr_i18n")
@ApiModel(value = "GoodsAttrI18n对象", description = "商品规格表")
public class GoodsAttrI18n implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("规格id")
    @TableField("attr_id")
    private Long attrId;

    @ApiModelProperty("属性名称")
    @TableField("attr_name")
    private String attrName;

    @ApiModelProperty("语言类型")
    @TableField("language_type")
    private String languageType;

    @ApiModelProperty("语言名称")
    @TableField("language_name")
    private String languageName;

}
