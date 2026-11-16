package com.sqx.modules.member.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sqx.modules.languageType.service.LanguageFillable;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @description member
 * @author fang
 * @date 2021-06-19
 */
@Data
public class Member implements Serializable, LanguageFillable {

    private static final long serialVersionUID = 1L;


    /**
     * 会员特权id
     */
    @TableId(type = IdType.AUTO)
    private Integer memberId;

    /**
     * 特权图标
     */
    private String memberImg;

    /**
     * 特权名称
     */
    private String memberName;

    /**
     * 描述
     */
    private String memberDescribe;

    /**
     * 排序
     */
    private String sort;

    /**
     * 语种
     */
    @ApiModelProperty("语种")
    @TableField(value = "language_type")
    private String languageType;

    /**
     * 语言名称
     */
    @ApiModelProperty("语言名称")
    @TableField(exist = false)
    private String languageName;

    public Member() {}
}
