package com.sqx.modules.goods.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.goods.entity.GoodsShopRelevancy;
import com.sqx.modules.goods.service.GoodsShopService;
import com.sqx.modules.utils.RequestUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Api(value="管理端-商铺",tags={"管理端-商铺"})
@RequestMapping(value = "/admin/goodsShop")
public class GoodsShopController {

    @Autowired
    private GoodsShopService goodsShopService;

    @ApiOperation("查看该商铺所有商品")
    @GetMapping(value = "/selectGoodsByShopId")
    public Result selectGoodsByShopId(HttpServletRequest request, Long shopId, Integer page, Integer limit, String goodsName, Integer classifyId, Integer status, String languageType){
        if (StringUtils.isBlank(languageType)) {
            languageType = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
        }

        return goodsShopService.selectGoodsByShopId(shopId, page, limit, goodsName, classifyId, status, languageType);
    }

    @ApiOperation("查看商品详情（按门店id和商品id）")
    @GetMapping(value = "/selectGoodsByShopIdAndGoodsId")
    public Result selectGoodsByShopIdAndGoodsId(Long shopId, Long goodsId, String languageType){
        if (StringUtils.isBlank(languageType)) {
            languageType = RequestUtil.getAcceptLanguage();
        }

        return goodsShopService.selectGoodsByShopIdAndGoodsId(shopId, goodsId, languageType);
    }

    //查看该商铺所有未选择商品
    @ApiOperation("查看该商铺所有未选择商品")
    @GetMapping(value = "/selectGoodsByNotShopId")
    public Result selectGoodsByNotShopId(Long shopId, Integer page, Integer limit, String goodsName){

        return goodsShopService.selectGoodsByNotShopId(shopId, page, limit, goodsName);
    }

    @ApiOperation("批量添加商品到该商铺")
    @PostMapping(value = "/addGoodsByShopId")
    public Result addGoodsByShopId(@RequestBody List<GoodsShopRelevancy> goodsShopRelevancyList){
        if(goodsShopRelevancyList.size()<=0){
            return Result.error("60169");
        }
        return goodsShopService.addGoodsByShopId(goodsShopRelevancyList);
    }

    @ApiOperation("修改该商铺商品信息（主要是库存）")
    @PostMapping(value = "updateGoodsByShopId")
    public Result updateGoodsByShopId(@RequestBody GoodsShopRelevancy goodsShopRelevancy){

        return goodsShopService.updateGoodsByShopId(goodsShopRelevancy);
    }

    @ApiOperation("删除该商铺的商品")
    @PostMapping(value = "/deleteGoodsByShopId")
    public Result deleteGoodsByShopId(Long shopId, Long goodsId){

        return goodsShopService.deleteGoodsByShopId(shopId, goodsId);
    }

    @ApiOperation("下架该商铺的商品")
    @PostMapping(value = "/soldOutOrPutaway")
    public Result soldOutOrPutaway(Long shopId, Long goodsId, Integer status){

        return goodsShopService.soldOutOrPutaway(shopId, goodsId, status);
    }

    @ApiOperation("统计门店收入")
    @GetMapping(value = "selectStoreData")
    public Result selectStoreData(Long shopId, String date, String dateType){

        return goodsShopService.selectStoreData(shopId, date, dateType);
    }

    @ApiOperation("统计门店订单详情")
    @GetMapping(value = "selectStoreMessage")
    public Result selectStoreMessage(Long shopId, String date, String dateType, Integer page, Integer limit, String orderNumber,Integer status){

        return goodsShopService.selectStoreMessage(shopId, date, dateType, page, limit, orderNumber,status);
    }


}
