package com.sqx.modules.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.goods.entity.GoodsClassify;

public interface GoodsClassifyService extends IService<GoodsClassify> {

    Result insertClassify(GoodsClassify goodsClassify);

    Result updateClassify(GoodsClassify goodsClassify);

    Result deleteClassify(Long classifyId);
}
