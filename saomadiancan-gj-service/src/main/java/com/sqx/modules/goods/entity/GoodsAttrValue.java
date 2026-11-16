package com.sqx.modules.goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("goods_attr_value")
public class GoodsAttrValue implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("属性id")
    private Long id;


    @ApiModelProperty("属性值组合:{尺寸: 7寸 ， 颜色: 红底 }")
    private String detail;


    @ApiModelProperty("商品id")
    private Long goodsId;


    @ApiModelProperty("商品规格id")
    private Long attrId;


    @ApiModelProperty("规格属性名称")
    private String value;

    private String languageType;
    private String languageName;

    public GoodsAttrValue() {}
}