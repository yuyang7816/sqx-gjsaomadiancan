package com.sqx.modules.goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author fang
 * @date 2021.9.14
 * 商品规格
 */
@Data
public class GoodsRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("规格名称")
    private String ruleName;

    @ApiModelProperty("创建时间")
    private String createTime;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @TableField(exist = false)
    @ApiModelProperty("店铺名称")
    private String shopName;

    @ApiModelProperty("语种")
    @TableField(exist = false)
    private String languageType;

    @ApiModelProperty("语种名称")
    @TableField(exist = false)
    private String languageName;


    @ApiModelProperty("规格值")
    @TableField(exist = false)
    private List<GoodsRuleValue> ruleValue;

}
