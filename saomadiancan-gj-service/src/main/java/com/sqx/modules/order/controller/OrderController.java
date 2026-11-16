package com.sqx.modules.order.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.order.entity.OrderGoods;
import com.sqx.modules.order.entity.TbOrder;
import com.sqx.modules.order.entity.vo.OrderVo;
import com.sqx.modules.order.service.AppOrderService;
import com.sqx.modules.order.service.OrderGoodsService;
import com.sqx.modules.sys.controller.AbstractController;
import com.sqx.modules.sys.entity.SysUserEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"管理端-订单"})
@RestController
@RequestMapping("/admin/order")
public class OrderController extends AbstractController {

    @Autowired
    private AppOrderService appOrderService;
    @Autowired
    private OrderGoodsService orderGoodsService;

    @ApiOperation("生成订单(未结算状态)")
    @PostMapping(value = "/insertOrder")
    public Result insertOrder(Long userId, @RequestBody OrderVo orderVo) {
        return appOrderService.insertOrder(userId, orderVo);
    }

    @ApiOperation("条件查询所有订单")
    @GetMapping(value = "selectAllOrder")
    public Result selectAllOrder(Integer page, Integer limit, Integer status, String phone, Long shopId, String userName, String orderNumber, Integer orderType, Integer isPay) {
        SysUserEntity user = getUser();
        Long userId = user.getUserId();
        return appOrderService.selectAllOrder(page, limit, status, phone, shopId, userName, userId, orderNumber, orderType, isPay);
    }

    @ApiOperation("管理订单")
    @PostMapping(value = "updateOrder")
    public Result updateOrder(@RequestBody TbOrder tborder) {

        return appOrderService.updateOrder(tborder);
    }

    @ApiOperation("修改订单备注")
    @PostMapping(value = "updateOrderRemarks")
    public Result updateOrderRemarks(Long orderId, String orderRemarks) {
        TbOrder tbOrder = new TbOrder();
        tbOrder.setOrderId(orderId);
        tbOrder.setOrderRemarks(orderRemarks);
        return appOrderService.updateById(tbOrder) ? Result.success() : Result.error();
    }

    @ApiOperation("修改订单备注")
    @PostMapping(value = "updateGoodRemarks")
    public Result updateGoodRemarks(Long id, String goodRemarks) {
        OrderGoods orderGoods = new OrderGoods();
        orderGoods.setId(id);
        orderGoods.setGoodRemarks(goodRemarks);
        return orderGoodsService.updateById(orderGoods) ? Result.success() : Result.error();
    }



    @ApiOperation("管理端退款")
    @PostMapping(value = "adminCancelOrder")
    public Result adminCancelOrder(Long orderId, Long orderGoodsId) {

        return appOrderService.adminCancelOrder(orderId, orderGoodsId);
    }

    @ApiOperation("查询退款详情")
    @GetMapping("selectCancelMoney")
    public Result selectCancelMoney(Long orderId, Long orderGoodsId) {
        return Result.success().put("data", appOrderService.selectCancelMoney(orderId, orderGoodsId));
    }

    @ApiOperation("打印票")
    @PostMapping(value = "/print")
    public Result print(Long orderId) {
        return appOrderService.print(orderId);
    }

    @ApiOperation("管理员完成订单")
    @PostMapping(value = "/adminCompleteOrder")
    public Result adminCompleteOrder(Long orderId) {
        return appOrderService.accomplishOrder(null, orderId);
    }

    /**
     * 获取桌码订单
     *
     * @return
     */
    @GetMapping(value = "getSeatOrder")
    public Result getSeatOrder(Long shopId, Long seatId) {
        return Result.success().put("data", appOrderService.getSeatOrder(shopId, seatId));
    }

    @ApiOperation("修改购物车中的商品数量")
    @GetMapping(value = "updateGoodsNum")
    public Result updateGoodsNum(Long orderGoodsId, Integer type, Integer num, Long shopId) {

        return appOrderService.updateGoodsNum(orderGoodsId, type, num, shopId);
    }

    /**
     * 订单统计
     *
     * @param shopId
     * @return
     */
    @GetMapping(value = "shopOrderStatistics")
    public Result shopOrderStatistics(Long shopId) {
        return Result.success().put("data", appOrderService.shopOrderStatistics(shopId));
    }

    /**
     * 商品统计
     *
     * @param shopId
     * @return
     */
    @GetMapping(value = "goodStatusCount")
    public Result goodStatusCount(Long shopId) {
        return Result.success().put("data", appOrderService.goodStatusCount(shopId));
    }


}
