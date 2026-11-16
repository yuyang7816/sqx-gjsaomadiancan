package com.sqx.modules.goods.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.goods.entity.Goods;
import com.sqx.modules.goods.entity.GoodsAttr;
import com.sqx.modules.goods.entity.GoodsClassify;
import com.sqx.modules.goods.entity.GoodsShop;
import com.sqx.modules.goods.service.GoodsClassifyService;
import com.sqx.modules.goods.service.GoodsService;
import com.sqx.modules.goods.service.GoodsShopService;
import com.sqx.modules.sys.controller.AbstractController;
import com.sqx.modules.sys.entity.SysUserEntity;
import com.sqx.modules.utils.RequestUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = {"管理端-商品"})
@RestController
@RequestMapping("/admin/goods")
public class GoodsController extends AbstractController {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsShopService goodsShopService;
    @Autowired
    private GoodsClassifyService goodsClassifyService;

    @ApiOperation(value = "添加商品")
    @PostMapping(value = "/insertGoods")
    public Result insertGoods(@RequestBody Goods goods) {

        return goodsService.insertGoods(goods);
    }

    @ApiOperation(value = "修改")
    @PostMapping("/update")
    public Result update(@RequestBody Goods goods) {
        return goodsService.updateGoods(goods);
    }

    @ApiOperation("查看所有商品")
    @GetMapping(value = "/selectGoodsList")
    public Result selectGoodsList(Integer page, Integer limit, Integer classifyId, String goodsName, Integer status, String languageType) {

        return goodsService.selectAdminGoodsList(page, limit, classifyId, goodsName, status, languageType);
    }

    @ApiOperation("删除商品")
    @PostMapping(value = "deleteGoodsById")
    public Result deleteGoodsById(Long goodsId) {

        return goodsService.deleteGoodsById(goodsId);
    }

    @ApiOperation("给商铺批量添加商品")
    @PostMapping(value = "shopAddGoods")
    public Result shopAddGoods(Long shopId, List goodsIdList) {

        return goodsService.shopAddGoods(shopId, goodsIdList);
    }

    @ApiOperation(value = "查看商品详情")
    @GetMapping("/selectGoodsById")
    public Result selectGoodsById(Long goodsId,Long shopId, String languageType) {
        if (StringUtils.isBlank(languageType)) {
            languageType = RequestUtil.getAcceptLanguage();
        }

        return goodsService.selectGoodsById(goodsId, shopId, languageType);
    }

    @ApiOperation(value = "单规格生成sku")
    @GetMapping(value = "/onlyFormatAttr")
    public Result onlyFormatSku(@ApiParam("商品图片") @RequestParam(required = false) String coverImg,
                                @ApiParam("售价") @RequestParam(required = false) String price) {
        return goodsService.onlyFormatAttr(coverImg, price);
    }

    @ApiOperation(value = "多规格生成sku")
    @PostMapping(value = "/isFormatAttr")
    public Result isFormatSku(@RequestBody GoodsAttr attr,
                              @ApiParam("商品图片") @RequestParam(required = false) String coverImg,
                              @ApiParam("售价") @RequestParam(required = false) String price) {
        return goodsService.isFormatAttr(attr, coverImg, price);
    }

    @ApiOperation(value = "回显属性")
    @GetMapping(value = "/formatAttr")
    public Result formatAttr(Long goodsId, String languageType) {
        return goodsService.formatAttr(goodsId, languageType);
    }

    @ApiOperation(value = "回显规格")
    @GetMapping(value = "/findAttrValue")
    public Result findAttrValue(Long goodsId, String languageType) {
        return goodsService.findAttrValue(goodsId, languageType);
    }

    @ApiOperation("查看所有商铺")
    @GetMapping(value = "selectAllShop")
    public Result selectAllShop(Integer page, Integer limit, String shopName) {
        SysUserEntity user = getUser();
        Long userId = user.getUserId();
        return goodsService.selectAllShop(page, limit, shopName, userId);
    }

    @ApiOperation("查看所有商铺(不分页)")
    @GetMapping(value = "selectAllShopList")
    public Result selectAllShopList() {
        SysUserEntity user = getUser();
        Long userId = user.getUserId();
        return goodsService.selectAllShopList(userId);
    }

    @ApiOperation("添加商铺")
    @PostMapping(value = "insertGoodsShop")
    public Result insertGoodsShop(@RequestBody GoodsShop goodsShop) {

        return goodsShopService.insertGoodsShop(goodsShop);
    }

    @ApiOperation("修改商铺信息")
    @PostMapping(value = "updateGoodsShop")
    public Result updateGoodsShop(@RequestBody GoodsShop goodsShop) {

        return goodsShopService.updateGoodsShop(goodsShop);
    }

    @ApiOperation("删除商铺")
    @GetMapping(value = "deleteGoodsShop")
    public Result deleteGoodsShop(Long id) {

        return goodsShopService.deleteGoodsShop(id);
    }

    @ApiOperation("获取所有商品类型")
    @GetMapping(value = "selectAllClassify")
    public Result selectAllClassify(HttpServletRequest request, Long shopId, String languageType) {
        if (StringUtils.isBlank(languageType)) {
            languageType = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
        }
        return goodsService.selectAllClassify(shopId, languageType);
    }

    @ApiOperation("添加商品类型")
    @PostMapping(value = "insertClassify")
    public Result insertClassify(HttpServletRequest request, @RequestBody GoodsClassify goodsClassify) {
        if (StringUtils.isBlank(goodsClassify.getLanguageType())) {
            String languageType = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
            goodsClassify.setLanguageType(languageType);
        }
        return goodsClassifyService.insertClassify(goodsClassify);
    }

    @ApiOperation("修改商品类型")
    @PostMapping(value = "updateClassify")
    public Result updateClassify(@RequestBody GoodsClassify goodsClassify) {

        return goodsClassifyService.updateClassify(goodsClassify);
    }

    @ApiOperation("删除商品类型")
    @GetMapping(value = "deleteClassify")
    public Result deleteClassify(Long classifyId) {

        return goodsClassifyService.deleteClassify(classifyId);
    }


}
