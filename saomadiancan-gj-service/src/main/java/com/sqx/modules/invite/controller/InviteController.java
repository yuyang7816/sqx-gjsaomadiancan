package com.sqx.modules.invite.controller;


import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.goods.entity.GoodsShop;
import com.sqx.modules.goods.service.GoodsShopService;
import com.sqx.modules.invite.entity.InviteMoney;
import com.sqx.modules.invite.service.InviteMoneyService;
import com.sqx.modules.invite.service.InviteService;
import com.sqx.modules.shopSeat.service.ShopSeatService;
import com.sqx.modules.utils.InvitationCodeUtil;
import com.sqx.modules.utils.QRCodeFlowUtil;
import com.sqx.modules.utils.SenInfoCheckUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fang
 * @date 2020/7/9
 */
@Slf4j
@RestController
@Api(value = "邀请收益", tags = {"邀请收益"})
@RequestMapping(value = "/invite")
public class InviteController {

    @Autowired
    private InviteService inviteService;
    @Autowired
    private UserService userService;
    @Autowired
    private InviteMoneyService inviteMoneyService;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private ShopSeatService seatService;
    @Autowired
    private GoodsShopService shopService;

    @RequestMapping(value = "/selectInviteCount", method = RequestMethod.GET)
    @ApiOperation("查看我邀请的人员数量")
    @ResponseBody
    public Result selectInviteCount(Integer state, Long userId) {
        return Result.success().put("data", inviteService.selectInviteCount(state, userId));
    }

    @RequestMapping(value = "/selectInviteAndPoster", method = RequestMethod.GET)
    @ApiOperation("查看我的邀请码和海报二维码")
    @ResponseBody
    public Result selectInviteAndPoster(Long userId) {
        UserEntity userEntity = userService.queryByUserId(userId);
        CommonInfo one = commonInfoService.findOne(19);
        Map<String, Object> map = new HashMap<>();
        map.put("url", one.getValue());
        map.put("user", userEntity);
        return Result.success().put("data", map);
    }

    @RequestMapping(value = "/selectInviteByUserIdList", method = RequestMethod.GET)
    @ApiOperation("查看我邀请的人员列表(只查看邀请成功成为会员))")
    @ResponseBody
    public Result selectInviteByUserIdList(int page, int limit, Long userId) {
        PageUtils pageUtils = inviteService.selectInviteUser(page, limit, userId, 1);
        InviteMoney inviteMoney = inviteMoneyService.selectInviteMoneyByUserId(userId);
        Map<String, Object> map = new HashMap<>();
        map.put("pageUtils", pageUtils);
        map.put("inviteMoney", inviteMoney);
        return Result.success().put("data", map);
    }

    @GetMapping("/mpCreateQr")
    @ApiOperation("小程序推广二维码")
    public void mpCreateQr(@RequestParam String relation, HttpServletResponse response) {
        SenInfoCheckUtil.getPoster(relation, response);
    }

    @GetMapping("/shopQr")
    @ApiOperation("门店二维码")
    public String shopQr(String shopId) {
        String value = commonInfoService.findOne(18).getValue();
        //value = "http://192.168.0.126:8080";
        return QRCodeFlowUtil.getBase64QRCode(value+"/pages/index/index?shopId=" + shopId);
    }

    @GetMapping("/shopSeatQr")
    @ApiOperation("桌码二维码")
    public String shopSeatQr(String shopId, String seatId) {
        //ShopSeat shopSeat = seatService.getById(seatId);
        String value = commonInfoService.findOne(19).getValue();
        //return QRCodeFlowUtil.getBase64QRCode(value+"/wechatPage?shopId=" + shopSeat.getShopId() + "&seatId=" + shopSeat.getSeatId());

        return QRCodeFlowUtil.getBase64QRCode(value + "/my/meals/mealInfo?shopId=" + shopId  +"&seatId=" + seatId);
    }

    @GetMapping("/shopPaySeatQr")
    @ApiOperation("门店收款二维码")
    public String shopPaySeatQr(String shopId) {
        GoodsShop shop = shopService.getById(shopId);
        String value = commonInfoService.findOne(18).getValue();
        return QRCodeFlowUtil.getBase64QRCode(value+"/wechatPay?shopId=" + shop.getShopId());
    }

    @RequestMapping(value = "/selectInviteByUserIdLists", method = RequestMethod.GET)
    @ApiOperation("查看我邀请的人员列表(查看所有邀请列表)")
    @ResponseBody
    public Result selectInviteByUserIdLists(int page, int limit, Long userId) {
        PageUtils pageUtils = inviteService.selectInviteUser(page, limit, userId, null);
        Map<String, Object> map = new HashMap<>();
        map.put("pageUtils", pageUtils);
        return Result.success().put("data", map);
    }

    @RequestMapping(value = "/insertInvitationCode", method = RequestMethod.POST)
    @ApiOperation("填写邀请码")
    @ResponseBody
    public Result insertInvitationCode(Long userId, String invitationCode) {
        if (StringUtils.isBlank(invitationCode)) {
            return Result.error("50061");
        }
        long inviteeUserId = InvitationCodeUtil.codeToId(invitationCode);
        UserEntity userEntity = userService.queryByUserId(inviteeUserId);
        if (userEntity == null) {
            return Result.error("50129");
        }
        inviteService.saveBody(userId, userEntity);
        return Result.success();
    }


}