package com.sqx.modules.goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author fang
 * @date 2021.9.14
 * 商品规格属性
 */
@Data
public class GoodsRuleValue {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("规格id")
    private Long ruleId;

    @ApiModelProperty("规格属性名称")
    private String value;

    @ApiModelProperty("规格属性值")
    private String detail;

    @ApiModelProperty("语种")
    @TableField(value = "language_type")
    private String languageType;

}
