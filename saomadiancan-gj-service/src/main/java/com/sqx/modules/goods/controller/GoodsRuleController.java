package com.sqx.modules.goods.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.goods.entity.GoodsRule;
import com.sqx.modules.goods.service.GoodsRuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value="自营商城商品规格",tags={"自营商城商品规格"})
@RequestMapping(value = "/selfGoodsRule")
public class GoodsRuleController {
    @Autowired
    private GoodsRuleService goodsRuleService;

    @GetMapping("/list")
    @ApiOperation("列表")
    public Result findAll(Integer page, Integer limit,GoodsRule goodsRule) {
        return goodsRuleService.findAll(page, limit,goodsRule);
    }

    @GetMapping("/find")
    @ApiOperation("查询")
    public Result findOne(Long id, String languageType) {
        return goodsRuleService.findOne(id, languageType);
    }

    @PostMapping("/save")
    @ApiOperation("添加")
    public Result saveBody(@RequestBody GoodsRule entity) {
        return goodsRuleService.saveBody(entity);
    }

    @PostMapping("/update")
    @ApiOperation("修改")
    public Result updateBody(@RequestBody GoodsRule entity) {
        return goodsRuleService.updateBody(entity);
    }


    @GetMapping("/delete")
    @ApiOperation("删除")
    public Result delete(Long id) {
        return goodsRuleService.delete(id);
    }

}
