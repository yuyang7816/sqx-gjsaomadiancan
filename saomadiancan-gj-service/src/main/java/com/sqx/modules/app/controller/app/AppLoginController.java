package com.sqx.modules.app.controller.app;


import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.form.LoginForm;
import com.sqx.modules.app.service.IAppleService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.app.utils.UserConstantInterface;
import com.sqx.modules.app.utils.WxPhone;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import weixin.popular.api.SnsAPI;
import weixin.popular.bean.sns.SnsToken;

/**
 * APP登录授权
 *
 */
@RestController
@RequestMapping("/app/Login")
@Api(tags = "APP登录接口")
@Slf4j
public class AppLoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private IAppleService appleService;
    @Autowired
    private CommonInfoService commonInfoService;

    @ApiOperation("微信小程序登陆")
    @RequestMapping(value = "/wxLogin", method = RequestMethod.GET)
    public Result wxLogin(@ApiParam("小程序code码") String code){
        return userService.wxLogin(code);
    }


    @ApiOperation("小程序登录新增或修改个人信息")
    @RequestMapping(value = "/insertWxUser", method = RequestMethod.POST)
    public Result insertWxUser(@RequestBody UserEntity userInfo){
        return userService.wxRegister(userInfo);
    }

    @RequestMapping(value = "/wxAppLogin", method = RequestMethod.POST)
    @ApiOperation("微信APP登录")
    @ResponseBody
    public Result wxAppLogin(@RequestParam String wxOpenId,@RequestParam String token) {
        return userService.wxAppLogin(wxOpenId,token);
    }

    @RequestMapping(value = "/wxBindMobile", method = RequestMethod.POST)
    @ApiOperation("微信登录绑定手机号")
    @ResponseBody
    public Result wxBindMobile(@RequestParam String phone,@RequestParam String code,@RequestParam String wxOpenId,@RequestParam String token,@RequestParam String platform,@RequestParam String sysPhone) {
        return userService.wxBindMobile(phone, code, wxOpenId, token, platform, sysPhone);
    }


    @RequestMapping(value = "/registerCode", method = RequestMethod.POST)
    @ApiOperation("app或h5注册或登录")
    @ResponseBody
    public Result registerCode(@RequestParam String phone,
                               @RequestParam String password,
                               @RequestParam(required = false) String msg,
                               @RequestParam(required = false) String platform,
                               @RequestParam(required = false) Integer sysPhone) {
        return userService.registerCode(phone,msg,platform,sysPhone,password);
    }


    @GetMapping("/wxOpenIdLogin")
    @ApiOperation("公众号openId登录")
    public Result wxOpenIdLogin(String openId){
        return userService.wxOpenIdLogin(openId);
    }


    @GetMapping("/getOpenId")
    @ApiOperation("公众号根据code换取openId")
    public Result getOpenId(String code) {
        try {
            //微信appid
            CommonInfo one = commonInfoService.findOne(5);
            //微信秘钥
            CommonInfo two = commonInfoService.findOne(21);
            SnsToken snsToken = SnsAPI.oauth2AccessToken(one.getValue(), two.getValue(), code);
            String openid = snsToken.getOpenid();
            return Result.success().put("data",openid);
        } catch (Exception e) {
            throw new RuntimeException("GET_OPENID_FAIL");
        }
    }

    @GetMapping("/bindOpenId")
    @ApiOperation("用户绑定公众号openId")
    public Result bindOpenId(Long userId,String openId){
        UserEntity userEntity=new UserEntity();
        userEntity.setUserId(userId);
        userEntity. setWxOpenId(openId);
        userService.updateById(userEntity);
        return Result.success();
    }

    @ApiOperation("用户端发送验证码")
    @RequestMapping(value = "/sendMsg/{phone}/{state}", method = RequestMethod.GET)
    @ResponseBody
    public Result sendMsg(@PathVariable String phone, @PathVariable String state) {
        return userService.sendMsg(phone, state);
    }

    @ApiOperation("解密手机号")
    @RequestMapping(value = "/selectPhone",method = RequestMethod.POST)
    public Result getPhoneNumberBeanS5(@RequestBody WxPhone wxPhone) {
        return UserConstantInterface.decryptS5(wxPhone.getDecryptData(), wxPhone.getKey(), wxPhone.getIv());
    }
    @ApiParam("登录app")
    @RequestMapping(value = "/loginApp",method = RequestMethod.POST)
    public  Result loginApp(@RequestParam String phone,@RequestParam String password){
        return userService.loginApp(phone,password);
    }
    @CrossOrigin
    @ApiParam("注册app")
    @RequestMapping(value = "/registApp",method = RequestMethod.POST)
    public  Result registApp(@RequestParam String userName,@RequestParam String phone,@RequestParam String password,String msg,@RequestParam String platform,String inviterCode){
       return userService.registApp(userName,phone,password,msg,platform,inviterCode);
    }

    @RequestMapping(value = "/emailRegister", method = RequestMethod.POST)
    @ApiOperation("邮箱注册")
    @ResponseBody
    public Result emailRegister(UserEntity userInfo) {
        return userService.emailRegister(userInfo);
    }

    @PostMapping("emailLogin")
    @ApiOperation("邮箱登录")
    public Result emailLogin(@RequestBody LoginForm form){
        return userService.emailLogin(form);
    }

    @ApiOperation("邮箱发送验证码")
    @RequestMapping(value = "/emailSendMsg", method = RequestMethod.GET)
    @ResponseBody
    public Result emailSendMsg(String email, String state) {
        return userService.emailSendMsg(email, state);
    }

    @ApiOperation("用户端邮箱忘记密码")
    @RequestMapping(value = "/emailForgetPwd", method = RequestMethod.POST)
    @ResponseBody
    public Result emailForgetPwd(String pwd, String email, String msg) {
        return userService.emailForgetPwd(pwd, email, msg);
    }


//    @ApiOperation("苹果登录")
//    @RequestMapping(value = "/insertAppleUser", method = RequestMethod.GET)
//    public Result insertAppleUser(@RequestParam String appleId){
//        return userService.iosRegister(appleId);
//    }
//
//    @RequestMapping(value = "/iosBindMobile", method = RequestMethod.POST)
//    @ApiOperation("苹果登录绑定手机号")
//    @ResponseBody
//    public Result iosBindMobile(@RequestParam String phone,@RequestParam String code,@RequestParam String appleId,@RequestParam String platform,@RequestParam Integer sysPhone) {
//        return userService.iosBindMobile(phone, code, appleId, platform, sysPhone);
//    }
//
//    @RequestMapping(value = "/appleLogin", method = RequestMethod.GET)
//    @ApiOperation("苹果登陆获取appleUserId")
//    public Result loginVerify(@RequestParam("identityToken") String identityToken) {
//        try {
//            log.info("苹果token：{}", identityToken);
//            JSONObject jsonObject = JSON.parseObject(identityToken);
//            JSONObject userInfo = jsonObject.getJSONObject("userInfo");
//            String identityTokens = userInfo.getString("identityToken");
//            return appleService.getAppleUserInfo(identityTokens);
//        } catch (Exception e) {
//            log.error("苹果token校验失败：{}", identityToken, e);
//            return Result.error("苹果账号验证失败，请退出重试！");
//        }
//    }
}
