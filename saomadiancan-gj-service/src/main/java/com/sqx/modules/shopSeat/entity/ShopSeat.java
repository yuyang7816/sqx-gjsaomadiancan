package com.sqx.modules.shopSeat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sqx.modules.goods.entity.GoodsShop;
import com.sqx.modules.sys.entity.SysUserEntity;
import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author www.javacoder.top
 * @since 2023-02-06
 */
@Data
public class ShopSeat implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 桌码id
     */
    @TableId(value = "seat_id", type = IdType.AUTO)
    private Long seatId;

    /**
     * 所属分店id
     */
    private Long shopId;
    /**
     * 使用桌码的用户id
     */
    private Long userId;

    /**
     * 总店管理员id
     */
    private Long sysUserId;


    /**
     * 桌位名称
     */
    @TableField(condition = SqlCondition.LIKE)
    private String seatName;

    /**
     * 是否启用 0不启用 1启用
     */
    private Integer isEnable;
    /**
     * 排序
     */
    private Integer sort;

    /**
     * 桌位状态 1正在使用 2空闲中
     */
    private Integer status;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 桌码所属分店信息
     */
    @TableField(exist = false)
    private GoodsShop goodsShop;
    /**
     * 桌码所属管理员信息
     */
    @TableField(exist = false)
    private SysUserEntity sysUserEntity;
    /**
     * 二维码流
     */
    @TableField(exist = false)
    private String qrcode;
}
