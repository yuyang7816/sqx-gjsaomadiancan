package com.sqx.modules.datacentre.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "用户端-用户信息", tags = {"用户端-用户信息"})
@RequestMapping(value = "/app/user")
public class UserMessageController {

    @Autowired
    private UserService userService;

    @Login
    @ApiOperation("查看用户信息")
    @GetMapping(value = "/selectUserMessage")
    public Result selectUserMessage(@RequestAttribute Long userId){

        return userService.selectUserMessage(userId);
    }

    @Login
    @ApiOperation("修改用户信息")
    @PostMapping(value = "/updateUserMessage")
    public Result updateUserMessage(@RequestAttribute Long userId, @RequestBody UserEntity userEntity){
        userEntity.setUserId(userId);
        return userService.updateUserMessage(userEntity);
    }

}
