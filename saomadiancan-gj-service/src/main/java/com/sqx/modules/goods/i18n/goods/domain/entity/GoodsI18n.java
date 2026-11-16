package com.sqx.modules.goods.i18n.goods.domain.entity;

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
 * 商品表
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-19 17:21:08
 */
@Getter
@Setter
@TableName("goods_i18n")
@ApiModel(value = "GoodsI18n对象", description = "商品表")
public class GoodsI18n implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableField("goods_id")
    private Long goodsId;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("商品名称")
    @TableField("goods_name")
    private String goodsName;

    @ApiModelProperty("商品简介")
    @TableField("goods_synopsis")
    private String goodsSynopsis;

    @ApiModelProperty("商品描述")
    @TableField("goods_describe")
    private String goodsDescribe;

    @ApiModelProperty("商品标签")
    @TableField("goods_label")
    private String goodsLabel;

    @ApiModelProperty("语言类型")
    @TableField("language_type")
    private String languageType;

    @ApiModelProperty("语言名称")
    @TableField("language_name")
    private String languageName;

}
