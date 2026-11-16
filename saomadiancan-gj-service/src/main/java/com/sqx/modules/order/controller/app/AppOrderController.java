package com.sqx.modules.order.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.order.entity.TbOrder;
import com.sqx.modules.order.entity.vo.OrderVo;
import com.sqx.modules.order.service.AppOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = {"用户端-订单"})
@RestController
@RequestMapping("/app/order")
public class AppOrderController {

    @Autowired
    private AppOrderService appOrderService;

    @Login
    @ApiOperation("生成订单(未结算状态)")
    @PostMapping(value = "/insertOrder")
    public Result insertOrder(@RequestAttribute("userId") Long userId, @RequestBody OrderVo orderVo) {
        //Long userId = orderVo.getUserId();
        return appOrderService.insertOrder(userId,orderVo);
    }

    @Login
    @ApiOperation("直接购买单个商品")
    @PostMapping(value = "buyGoods")
    public Result buyGoods(HttpServletRequest request, @RequestAttribute Long userId, Long shopId, Long goodsId, Integer num, Long skuId, String skuMessage, Integer orderType, String orderRemarks) {
        String languageType = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
        return appOrderService.buyGoods(userId, shopId, goodsId, num, skuId, skuMessage, orderType,orderRemarks, languageType);
    }

    @Login
    @ApiOperation("被邀请人加入订单")
    @PostMapping(value = "/joinOrder")
    public Result joinOrder(@RequestAttribute Long userId, Long goodsId, Long parentId) {

        return appOrderService.joinOrder(userId, goodsId, parentId);
    }

    @Login
    @ApiOperation("修改订单")
    @PostMapping(value = "/updateOrder")
    public Result updateOrder(@RequestBody TbOrder order) {

        return appOrderService.updateOrder(order);
    }

    @Login
    @ApiOperation("使用红包")
    @PostMapping(value = "employCoupon")
    public Result employCoupon(@RequestAttribute Long userId, Long parentId, Long couponId) {

        return appOrderService.employCoupon(userId, parentId, couponId);
    }

    @Login
    @ApiOperation("重新进入订单结算页面时，删除选中的优惠券")
    @PostMapping(value = "deleteCouponByOrderId")
    public Result deleteCouponByOrderId(Long orderId) {

        return appOrderService.deleteCouponByOrderId(orderId);
    }

    @Login
    @ApiOperation("删除未结算订单")
    @PostMapping(value = "/deleteOrderList")
    public Result deleteOrderList(List<Integer> orderId) {

        return appOrderService.deleteOrderList(orderId);
    }

    @Login
    @ApiOperation("查看所有订单")
    @GetMapping(value = "/selectAllOrderList")
    public Result selectOrderList(HttpServletRequest request, @RequestAttribute Long userId, Long shopId, Integer status, Integer orderType) {
        String languageType = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
        return appOrderService.selectOrderList(userId, shopId, status, orderType, languageType);
    }

    @Login
    @ApiOperation("查看直接购买商品详情")
    @GetMapping(value = "selectBuyGoods")
    public Result selectBuyGoods(@RequestAttribute Long userId, Long orderId) {

        return appOrderService.selectBuyGoods(userId, orderId);
    }

    @Login
    @ApiOperation("用户在该商品下的订单回显")
    @GetMapping(value = "echoOrder")
    public Result echoOrder(@RequestAttribute Long userId, Long shopId, Long goodsId) {

        return appOrderService.echoOrder(userId, shopId, goodsId);
    }

    @Login
    @ApiOperation("修改购物车中的商品数量")
    @GetMapping(value = "updateGoodsNum")
    public Result updateGoodsNum(Long orderGoodsId, Integer type, Integer num, Long shopId) {

        return appOrderService.updateGoodsNum(orderGoodsId, type, num, shopId);
    }

    @Login
    @ApiOperation("清空购物车")
    @PostMapping(value = "emptyShoppingTrolley")
    public Result emptyShoppingTrolley(@RequestAttribute Long userId, Long shopId) {

        return appOrderService.emptyShoppingTrolley(userId, shopId);
    }

    @Login
    @ApiOperation("待取餐/配送")
    @GetMapping(value = "/waitTakeFood")
    public Result waitTakeFood(@RequestAttribute Long userId, Integer orderType, Integer page, Integer limit, Integer status) {

        return appOrderService.waitTakeFood(userId, orderType, page, limit, status);
    }

    @Login
    @ApiOperation("查看订单详情")
    @GetMapping(value = "selectOrderById")
    public Result selectOrderById(Long orderId) {

        return appOrderService.selectOrderById(orderId);
    }

    @Login
    @ApiOperation("用户选择完成订单")
    @PostMapping(value = "accomplishOrder")
    public Result accomplishOrder(@RequestAttribute Long userId, Long orderId) {

        return appOrderService.accomplishOrder(userId, orderId);
    }

    //   @Scheduled(cron = "0 * * * * ?", zone = "Asia/Shanghai")
    public void orderEnd() {

        appOrderService.orderEnd();
    }

    @ApiOperation("打印票")
    @PostMapping(value = "/print")
    public Result print(Long orderId) {
        return appOrderService.print(orderId);
    }

    /**
     * 获取桌码订单
     * @return
     */
    @GetMapping(value = "getSeatOrder")
    public Result getSeatOrder(Long shopId, Long seatId){
        return Result.success().put("data", appOrderService.getSeatOrder(shopId,seatId));
    }

    /**
     * 商家完成桌码订单
     * @param orderId
     * @return
     */
    @GetMapping("completeOrder")
    public Result completeOrder(Long orderId) {
        appOrderService.completeOrder(orderId);
        return Result.success();
    }

}
