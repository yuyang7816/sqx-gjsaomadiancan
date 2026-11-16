package com.sqx.modules.shopSeat.controller;


import com.sqx.common.utils.Result;
import com.sqx.modules.shopSeat.entity.ShopSeat;
import com.sqx.modules.shopSeat.service.ShopSeatService;
import com.sqx.modules.sys.controller.AbstractController;
import com.sqx.modules.utils.QRCodeFlowUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
@Api(tags = {"用户端-桌码"})
@RestController
@RequestMapping("/app/shopSeat/")
public class AppShopSeatController extends AbstractController {

    @Autowired
    private ShopSeatService shopSeatService;


    @ApiOperation("获取桌码列表")
    @GetMapping("getShopSeatList")
    public Result getShopSeatList(Integer page, Integer limit, ShopSeat shopSeat) {
        if (shopSeat.getShopId() == null) {
            return Result.error("60181");
        }
        shopSeat.setIsEnable(1);
        return Result.success().put("data", shopSeatService.getShopSeatList(page, limit, shopSeat));
    }

    @ApiOperation("获取桌码信息")
    @GetMapping("getSeatInfo")
    public Result getSeatInfo(Long seatId) {
        ShopSeat shopSeat = shopSeatService.getById(seatId);
        shopSeat.setQrcode(QRCodeFlowUtil.getBase64QRCode("https://dcgj.xianmxkj.com/wechatPage?shopId="+shopSeat.getShopId()+"&seatId="+shopSeat.getSeatId()));
        return Result.success().put("data", shopSeat);
    }
}

