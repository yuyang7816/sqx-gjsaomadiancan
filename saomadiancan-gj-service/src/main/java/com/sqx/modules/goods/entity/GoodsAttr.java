package com.sqx.modules.goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("goods_attr")
public class GoodsAttr implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)

    @ApiModelProperty("规格id")
    private Long id;


    @ApiModelProperty("属性名称")
    private String attrName;


    @ApiModelProperty("商品id")
    private Long goodsId;


    @ApiModelProperty("规格模板id")
    private Long ruleId;

    @TableField(exist = false)
    private String languageType;
    @TableField(exist = false)
    private String languageName;

    @ApiModelProperty("规格值")
    @TableField(exist = false)
    private List<GoodsAttrValue> attrValue;

    public GoodsAttr() {}
}