package com.sqx.modules.message.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.message.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author fang
 * @date 2020/7/13
 */
@RestController
@Api(value = "消息管理", tags = {"消息管理"})
@RequestMapping(value = "/app/message")
public class AppMessageController {

    @Autowired
    private MessageService messageService;

    @Login
    @RequestMapping(value = "/selectMessageByUserId", method = RequestMethod.GET)
    @ApiOperation("查询用户消息")
    @ResponseBody
    public Result selectUserRecharge(HttpServletRequest request, int page, int limit, @RequestAttribute("userId") Long userId, Integer state){
        String languageType = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);

        Map<String,Object> map=new HashMap<>();
        map.put("page",page);
        map.put("limit",limit);
        map.put("userId",userId);
        map.put("state",state);
        map.put("languageType", languageType);
        PageUtils pageUtils = messageService.selectMessageList(map);
      //  messageService.updateSendState(userId,state);
        return Result.success().put("data",pageUtils);
    }

    @Login
    @RequestMapping(value = "/selectMessageByUserIdLimit1", method = RequestMethod.GET)
    @ApiOperation("查询用户消息")
    @ResponseBody
    public Result selectMessageByUserIdLimit1(HttpServletRequest request, @RequestAttribute("userId") Long userId){
        String languageType = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);

        Map<String,Object> map=new HashMap<>();
        map.put("page",1);
        map.put("limit",1);
        map.put("userId",userId);
        map.put("languageType", languageType);
        return Result.success().put("data",messageService.selectMessageList(map));
    }


    @Login
    @PostMapping("/insertMessage")
    @ApiOperation("添加投诉")
    public Result insertMessage(@RequestBody MessageInfo messageInfo){
        messageService.saveBody(messageInfo);
        return Result.success();
    }

    @PostMapping("/saveMessageUser")
    @ApiOperation("添加地址")
    public Result saveMessageUser(@RequestBody JSONObject jsonObject) {
        String userId=jsonObject.getString("userId");
        String uuid=jsonObject.getString("uuid");
        JSONObject contentObj=jsonObject.getJSONObject("content");
        JSONObject content = null;
        if (contentObj != null){
            content = jsonObject.getJSONObject("content");
        }
        String type=jsonObject.getString("type");
        String platform=jsonObject.getString("platform");
        MessageInfo messageInfo=new MessageInfo();
        if (StringUtils.isNotBlank(String.valueOf(content))){
            messageInfo.setContent(String.valueOf(content));
        }
        messageInfo.setState("10");
        messageInfo.setType(type);
        messageInfo.setPlatform(platform);
        messageInfo.setTitle(uuid);
        messageInfo.setUserId(userId);
        messageInfo.setCreateAt(DateUtils.format(new Date()));
        messageService.saveBody(messageInfo);
        return Result.success().put("data",jsonObject);
    }


    @GetMapping("/selectMessageUser")
    @ApiOperation("查询地址")
    public Result selectMessageUser(Long userId,String uuid){
        MessageInfo messageInfo = messageService.getOne(new QueryWrapper<MessageInfo>()
                .eq(Objects.nonNull(userId), "user_id", userId)
                .eq("title", uuid)
                .eq("state", 10)
                .last("order by create_at desc limit 1"));
        return Result.success().put("data",messageInfo);
    }
}