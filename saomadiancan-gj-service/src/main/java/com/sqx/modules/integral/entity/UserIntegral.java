package com.sqx.modules.integral.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("user_integral")
public class UserIntegral implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)

    @ApiModelProperty("id")
    private Long id;


    @ApiModelProperty("用户id")
    private Long userId;


    @ApiModelProperty("积分数量")
    private Integer integralNum;

    public UserIntegral() {}
}