package com.sqx.modules.goods.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.goods.entity.GoodsSku;
import com.sqx.modules.goods.service.GoodsSkuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags={"管理端-sku规格"})
@RestController
@RequestMapping("/admin/sku")
public class GoodsSkuController {

    @Autowired
    private GoodsSkuService goodsSkuService;

    @ApiOperation("添加规格")
    @PostMapping(value = "insert")
    public Result insertSku(@RequestBody GoodsSku goodsSku){

        return goodsSkuService.insertSku(goodsSku);
    }



}
