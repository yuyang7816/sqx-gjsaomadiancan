package com.sqx.modules.shopSeat.controller;


import com.sqx.common.utils.Result;
import com.sqx.modules.shopSeat.entity.ShopSeat;
import com.sqx.modules.shopSeat.service.ShopSeatService;
import com.sqx.modules.sys.controller.AbstractController;
import com.sqx.modules.sys.entity.SysUserEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 桌码管理
 * </p>
 *
 * @author www.javacoder.top
 * @since 2023-02-06
 */
@Api(tags = {"管理端-桌码"})
@RestController
@RequestMapping("/admin/shopSeat/")
public class ShopSeatController extends AbstractController {

    @Autowired
    private ShopSeatService shopSeatService;

    @ApiOperation("新增或修改桌码")
    @PostMapping(value = "insertSeat")
    public Result saveSeat(ShopSeat shopSeat) {
        SysUserEntity user = getUser();
        Long userId = user.getUserId();
        shopSeat.setSysUserId(userId);
        return shopSeatService.saveSeat(shopSeat);
    }

    @ApiOperation("获取桌码列表")
    @GetMapping("getShopSeatList")
    public Result getShopSeatList(Integer page, Integer limit, ShopSeat shopSeat) {
        return Result.success().put("data", shopSeatService.getShopSeatList(page, limit, shopSeat));
    }

    @ApiOperation("获取桌码信息")
    @GetMapping("getSeatInfo")
    public Result getSeatInfo(Long seatId) {
        return Result.success().put("data", shopSeatService.getById(seatId));
    }

    @ApiOperation("删除桌码")
    @GetMapping("deleteShopSeatById")
    public Result deleteShopSeatById(Long seatId) {
        return shopSeatService.removeById(seatId) ? Result.success() : Result.error();
    }
}

