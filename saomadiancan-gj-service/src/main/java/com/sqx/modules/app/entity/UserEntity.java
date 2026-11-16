package com.sqx.modules.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 用户
 *
 */
@Data
@ApiModel("用户")
@TableName("tb_user")
public class UserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	@ApiModelProperty("用户id")
	@TableId(type = IdType.AUTO,value = "user_id")
	private Long userId;

	/**
	 * 用户类型  1普通用户   2陪玩官
	 */
	@ApiModelProperty("用户类型  1普通用户   2陪玩官")
	@TableField("type")
	private Integer type;
	/**
	 * 用户名
	 */
	@ApiModelProperty("用户名")
	@TableField("user_name")
	private String userName;

	/**
	 * 手机号
	 */
	@ApiModelProperty("手机号")
	private String phone;

	/**
	 * 头像
	 */
	@ApiModelProperty("头像")
	private String avatar;

	/**
	 * 性别 1男 2女
	 */
	@ApiModelProperty("性别 1男 2女")
	private Integer sex;
	/**
	 * 年龄
	 */
	@ApiModelProperty("年龄")
	private Integer age;

	/**
	 * 微信小程序openid
	 */
	@ApiModelProperty("微信小程序openid")
	@TableField("open_id")
	private String openId;

	/**
	 * 微信app openid
	 */
	@ApiModelProperty("微信公众号openid")
	@TableField("wx_open_id")
	private String wxOpenId;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 创建时间
	 */
	@TableField("create_time")
	private String createTime;

	/**
	 * 更新时间
	 */
	@TableField("update_time")
	private String updateTime;

	/**
	 * 苹果id
	 */
	@TableField("apple_id")
	private String appleId;

	/**
	 * 手机类型 1安卓 2ios
	 */
	@TableField("sys_phone")
	private Integer sysPhone;

	/**
	 * 状态 1正常 2禁用
	 */
	private Integer status;

	/**
	 * 来源 app 小程序 公众号
	 */
	private String platform;

	/**
	 * 积分
	 */
	private Integer jifen;

	/**
	 * 邀请码
	 */
	@TableField("invitation_code")
	private String invitationCode;

	/**
	 * 邀请人邀请码
	 */
	@TableField("inviter_code")
	private String inviterCode;

	private String clientid;

	private String zhiFuBao;

	private String zhiFuBaoName;

	@TableField("`member`")
	private Integer member;

	private String endTime;

	@ApiModelProperty("微信号")
	@TableField("wx_code")
	private String wxCode;

	@ApiModelProperty("微信二维码")
	@TableField("wx_qr_code")
	private String wxQrCode;

	@ApiModelProperty("是否是门店用户 1是  2否")
	@TableField("shop_admin_flag")
	private Integer shopAdminFlag;


	/**
	 * 邮件
	 */
	private String email;


	@TableField(exist = false)
	private BigDecimal money;

	@TableField(exist = false)
	private BigDecimal integralNum;

	@TableField(exist = false)
	private Integer orderMonth;

	@TableField(exist = false)
	private BigDecimal topUpMonth;

	/**
	 * 验证码
	 */
	@TableField(exist = false)
	private String code;

}
