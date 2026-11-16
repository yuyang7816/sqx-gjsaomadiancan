package com.sqx.modules.goods.i18n.sku.domain.entity;

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
 * 商品sku信息
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-19 18:31:54
 */
@Getter
@Setter
@TableName("goods_sku_i18n")
@ApiModel(value = "GoodsSkuI18n对象", description = "商品sku信息")
public class GoodsSkuI18n implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("sku id")
    @TableField("sku_id")
    private Long skuId;

    @ApiModelProperty("sku信息，json封装")
    @TableField("detail_json")
    private String detailJson;

    @ApiModelProperty("语言类型")
    @TableField("language_type")
    private String languageType;

    @ApiModelProperty("语言名称")
    @TableField("language_name")
    private String languageName;

}
