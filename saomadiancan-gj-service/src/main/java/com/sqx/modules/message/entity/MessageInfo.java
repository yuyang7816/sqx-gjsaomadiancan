package com.sqx.modules.message.entity;

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
 * @date 2020/7/13
 */
@Data
@TableName("message_info")
public class MessageInfo implements Serializable, LanguageFillable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * 消息id
     */
    private Long id;

    /**
     * 内容
     */
    private String content;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * 图片
     */
    private String image;

    /**
     * is_see
     */
    private String isSee;

    /**
     * send_state
     */
    private String sendState;

    /**
     * send_time
     */
    private String sendTime;

    /**
     * 分类  4订单消息  5 用户消息
     */
    private String state;

    /**
     * 标题
     */
    private String title;

    /**
     * 地址
     */
    private String url;

    /**
     * type
     */
    private String type;

    /**
     * platform
     */
    private String platform;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 是否已读 1已读 2未读
     */
    private Integer readFlag;

    @TableField(exist = false)
    private String phone;

    @ApiModelProperty("语种")
    @TableField(value = "language_type")
    private String languageType;

    /**
     * 语言名称
     */
    @ApiModelProperty("语言名称")
    @TableField(exist = false)
    private String languageName;



    public MessageInfo() {}

}