package com.sqx.modules.goods.entity;

import lombok.Data;

import java.util.List;

@Data
public class GoodsClassifyReq {

    private String classifyName;
    private List<Goods> goodsList;

}
