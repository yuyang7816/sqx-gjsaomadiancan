package com.sqx.modules.goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("goods_classify")
public class GoodsClassify implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)

    @ApiModelProperty("classify_id")
    private Integer classifyId;


    @ApiModelProperty("商品类型名称")
    private String classifyName;

    @ApiModelProperty("店铺id")
    private Long shopId;


    @TableField(exist = false)
    @ApiModelProperty("店铺名称")
    private String shopName;

    /**
     * 语言
     */
    @TableField(value = "language_type")
    private String languageType;

    @TableField(exist = false)
    private String languageName;

    public GoodsClassify() {
    }
}