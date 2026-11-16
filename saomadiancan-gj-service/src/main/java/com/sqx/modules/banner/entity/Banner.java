package com.sqx.modules.banner.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sqx.modules.languageType.service.LanguageFillable;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author fang
 * @date 2020/7/9
 */
@Data
@TableName("banner")
public class Banner implements Serializable, LanguageFillable {

    private static final long serialVersionUID = 1L;
    /**
     * banner图id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 名称
     */
    private String name;

    /**
     * 图片地址
     */
    private String imageUrl;

    /**
     * 状态 1正常 2隐藏
     */
    private Integer state;

    /**
     * 分类 1 banner图  2 首页分类
     */
    private Integer classify;

    /**
     * 跳转地址
     */
    private String url;

    /**
     * 顺序
     */
    private Integer sort;

    /**
     * 描述
     */
    private String describess;

    @ApiModelProperty("语种")
    @TableField(value = "language_type")
    private String languageType;

    @TableField(exist = false)
    private String languageName;
}