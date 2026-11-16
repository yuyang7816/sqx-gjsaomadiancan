package com.sqx.modules.goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author chengming
 * @since 2025/5/6
 */
@Data
public class GoodsClassifyI18n {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Integer classifyId;

    private String classifyName;

    private String languageType;

    private String languageName;
}
