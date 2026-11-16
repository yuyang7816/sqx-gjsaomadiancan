package com.sqx.modules.goods.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.goods.service.GoodsService;
import com.sqx.modules.goods.service.GoodsShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Api(tags={"用户端-商品"})
@RestController
@RequestMapping("/app/goods")
public class AppGoodsController {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsShopService goodsShopService;
    @ApiOperation("查看附近的店铺")
    @PostMapping(value = "selectShortShop")
    public Result selectShortShop(double lng, double lat, String shopName, Integer page, Integer limit, Long shopId){

        return goodsService.selectShortShop(lng, lat, shopName, page, limit, shopId);
    }

    @ApiOperation("根据商铺id查看商品列表")
    @GetMapping(value = "/selectGoodsList")
    public Result selectGoodsList(HttpServletRequest request, Long shopId){
        String language = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
        return goodsService.selectGoodsList(shopId, language);
    }

    @ApiOperation("查看商品详情")
    @GetMapping(value = "/selectGoodsById")
    public Result selectGoodsById(HttpServletRequest request, Long goodsId,Long shopId){
        String language = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
        return goodsService.selectGoodsById(goodsId, shopId, language);
    }

    @ApiOperation("获取所有商品类型")
    @GetMapping(value = "selectAllClassify")
    public Result selectAllClassify(HttpServletRequest request, Long shopId) {
        String languageType = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
        return goodsService.selectAllClassify(shopId, languageType);
    }

    @ApiOperation("查看该商铺所有商品")
    @GetMapping(value = "/selectGoodsByShopId")
    public Result selectGoodsByShopId(HttpServletRequest request, Long shopId, Integer page, Integer limit, String goodsName, Integer classifyId, Integer status){
        String language = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
        return goodsShopService.selectGoodsByShopId(shopId, page, limit, goodsName, classifyId, status, language);
    }
}
