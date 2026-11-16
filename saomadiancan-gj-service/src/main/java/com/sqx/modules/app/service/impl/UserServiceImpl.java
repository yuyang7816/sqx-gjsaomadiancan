package com.sqx.modules.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.ClientException;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.getui.push.v2.sdk.ApiHelper;
import com.getui.push.v2.sdk.GtApiConfiguration;
import com.getui.push.v2.sdk.api.PushApi;
import com.getui.push.v2.sdk.common.ApiResult;
import com.getui.push.v2.sdk.dto.req.Audience;
import com.getui.push.v2.sdk.dto.req.message.PushDTO;
import com.getui.push.v2.sdk.dto.req.message.PushMessage;
import com.getui.push.v2.sdk.dto.req.message.android.GTNotification;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.sqx.common.exception.SqxException;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.dao.MsgDao;
import com.sqx.modules.app.dao.UserDao;
import com.sqx.modules.app.dao.UserMoneyDao;
import com.sqx.modules.app.entity.AppUserInfo;
import com.sqx.modules.app.entity.Msg;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.entity.UserMoney;
import com.sqx.modules.app.form.LoginForm;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.app.utils.JwtUtils;
import com.sqx.modules.app.utils.UserConstantInterface;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.file.utils.Md5Utils;
import com.sqx.modules.integral.dao.UserIntegralDao;
import com.sqx.modules.integral.entity.UserIntegral;
import com.sqx.modules.invite.service.InviteService;
import com.sqx.modules.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weixin.popular.api.SnsAPI;
import weixin.popular.util.JsonUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 用户
 *
 * @author fang
 * @date 2021/2/27
 */

@Service("userService")
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private MsgDao msgDao;
    @Autowired
    private JwtUtils jwtUtils;
    private int number = 1;
    @Autowired
    private InviteService inviteService;
    @Autowired
    private UserMoneyDao userMoneyDao;
    @Autowired
    private UserIntegralDao userIntegralDao;
    @Autowired
    private DataSourceMessageSource dataSourceMessageSource;

    @Override
    public UserEntity queryByPhone(String phone) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("phone", phone));
    }

    @Override
    public UserEntity queryByOpenId(String openId) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("open_id", openId));
    }

    @Override
    public UserEntity queryByWxOpenId(String openId) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("wx_open_id", openId));
    }

    @Override
    public UserEntity queryByAppleId(String appleId) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("apple_id", appleId));
    }

    @Override
    public UserEntity queryByUserId(Long userId) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("user_id", userId));
    }

    @Override
    public UserEntity queryByInvitationCode(String invitationCode) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("invitation_code", invitationCode));
    }

    @Override
    public Result updatePhone(String phone, String msg, Long userId) {
        Msg msg1 = msgDao.findByPhoneAndCode(phone, msg);
        //校验短信验证码
        if (msg1 != null) {
            UserEntity userInfo = queryByPhone(phone);
            if (userInfo != null) {
                return Result.error("50051");
            } else {
                UserEntity one = baseMapper.selectById(userId);
                one.setPhone(phone);
                baseMapper.updateById(one);
                return Result.success();
            }
        }
        return Result.error("50011");
    }

    //@Override
  //  public Result iosRegister(String appleId) {
//        if (StringUtils.isEmpty(appleId)) {
//            return Result.error("账号信息获取失败，请退出重试！");
//        }
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String date = sdf.format(new Date());
//        // 根据返回的user实体类，判断用户是否是新用户，不是的话，更新最新登录时间，是的话，将用户信息存到数据库
//        UserEntity userInfo = queryByAppleId(appleId);
//        if (userInfo != null) {
//            if (userInfo.getStatus().equals(2)) {
//                return Result.error("账号已被封禁，请联系客服处理！");
//            }
//            userInfo.setUpdateTime(date);
//            baseMapper.updateById(userInfo);
//            //返回用户信息
//            UserEntity user = queryByAppleId(appleId);
//            return getResult(user);
//        } else {
//            return Result.error(-200, "请先绑定手机号账号！");
//        }
  //  }

    @Override
    public Result wxLogin(String code) {
        try {
            //微信小程序APPID
            String appid = commonInfoService.findOne(45).getValue();
            //微信小程序秘钥
            String secret = commonInfoService.findOne(46).getValue();
            // 配置请求参数
            Map<String, String> param = new HashMap<>();
            param.put("appid", appid);
            param.put("secret", secret);
            param.put("js_code", code);
            param.put("grant_type", UserConstantInterface.WX_LOGIN_GRANT_TYPE);
            param.put("scope", "snsapi_userinfo");
            // 发送请求
            String wxResult = HttpClientUtil.doGet(UserConstantInterface.WX_LOGIN_URL, param);
            log.info(wxResult);
            JSONObject jsonObject = JSONObject.parseObject(wxResult);
            // 获取参数返回的
            String session_key = jsonObject.get("session_key").toString();
            //返回微信小程序openId
            String open_id = jsonObject.get("openid").toString();
            //判断是否注册过
            UserEntity userEntity = queryByOpenId(open_id);
            Map<String, String> map = new HashMap<>();
            // 封装返回小程序
            map.put("session_key", session_key);
            map.put("open_id", open_id);
            if (jsonObject.get("unionid") != null) {
                String unionid = jsonObject.get("unionid").toString();
                map.put("unionid", unionid);
            } else {
                map.put("unionid", "-1");
            }

            if (userEntity == null) {
                map.put("flag", "1");
            } else {
                map.put("flag", userEntity.getPhone());
            }
            return Result.success("50107").put("data", map);
        } catch (Exception e) {
            System.err.println(e.toString());
            return Result.success("50078");
        }
    }


    @Override
    public Result wxRegister(UserEntity userInfo1) {

        if (StringUtils.isEmpty(userInfo1.getOpenId())) {
            return Result.error("50049");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        // 根据返回的user实体类，判断用户是否是新用户，不是的话，更新最新登录时间，是的话，将用户信息存到数据库
        UserEntity userInfo = queryByOpenId(userInfo1.getOpenId());
        if (userInfo != null) {
            if (userInfo.getStatus().equals(2)) {
                return Result.error("50050");
            }
            baseMapper.updateById(userInfo);
        } else {
            //判断是否在app登陆过  手机号是否有账号
            UserEntity userByMobile = queryByPhone(userInfo1.getPhone());
            if (userByMobile != null) {
                //有账号则绑定账号
                userByMobile.setOpenId(userInfo1.getOpenId());
                baseMapper.updateById(userByMobile);
                if (userByMobile.getStatus().equals(2)) {
                    return Result.error("50050");
                }
            } else {
                //没有则生成新账号
                userInfo1.setCreateTime(date);
                userInfo1.setPlatform(dataSourceMessageSource.getConfigI18ForCach("50235"));
                userInfo1.setStatus(1);
                userInfo1.setType(1);
                baseMapper.insertUser(userInfo1);
                //注册新账号的时候，创建钱包
                UserMoney userMoney = new UserMoney();
                userMoney.setUserId(userInfo1.getUserId());
                userMoney.setMoney(new BigDecimal(0));
                userMoneyDao.insert(userMoney);
                //注册新账号得时候，创建积分仓库
                UserIntegral userIntegral = new UserIntegral();
                userIntegral.setUserId(userInfo1.getUserId());
                userIntegral.setIntegralNum(0);
                userIntegralDao.insert(userIntegral);
            }
        }
        //返回用户信息
        UserEntity user = queryByOpenId(userInfo1.getOpenId());
        return getResult(user);
    }


    @Override
    public Result wxBindMobile(String phone, String code, String wxOpenId, String token, String platform, String sysPhone) {
        Msg byPhoneAndCode = msgDao.findByPhoneAndCode(phone, code);
        if (byPhoneAndCode == null) {
            return Result.error("50214");
        }
        msgDao.deleteById(byPhoneAndCode.getId());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(new Date());
        UserEntity userInfo = queryByPhone(phone);
        if (userInfo != null) {
            if (StringUtils.isNotEmpty(userInfo.getOpenId())) {
                return Result.error("50052");
            }
            //小程序登陆过
            userInfo.setOpenId(wxOpenId);
            String s = HttpClientUtil.doGet("https://api.weixin.qq.com/sns/userinfo?access_token=" + token + "&openid=" + wxOpenId);
            AppUserInfo user = JsonUtil.parseObject(s, AppUserInfo.class);
            if (user != null && user.getNickname() != null) {
                if (user.getHeadimgurl() != null) {
                    userInfo.setAvatar(user.getHeadimgurl());
                }
                userInfo.setSex(user.getSex());
                if (user.getNickname() != null) {
                    userInfo.setUserName(user.getNickname().replaceAll("(\\d{3})\\d*([0-9a-zA-Z]{4})", "$1****$2"));
                }
            }
            baseMapper.updateById(userInfo);
        } else {
            //小程序没有登陆过
            userInfo = new UserEntity();
            String s = HttpClientUtil.doGet("https://api.weixin.qq.com/sns/userinfo?access_token=" + token + "&openid=" + wxOpenId);
            AppUserInfo user = JsonUtil.parseObject(s, AppUserInfo.class);
            if (user != null && user.getNickname() != null) {
                if (user.getHeadimgurl() != null) {
                    userInfo.setAvatar(user.getHeadimgurl());
                }
                userInfo.setSex(user.getSex());
                if (user.getNickname() != null) {
                    userInfo.setUserName(user.getNickname().replaceAll("(\\d{3})\\d*([0-9a-zA-Z]{4})", "$1****$2"));
                }
            }
            userInfo.setOpenId(wxOpenId);
            userInfo.setPhone(phone);
            //userInfo.setPlatform(platform);
            userInfo.setCreateTime(time);
            userInfo.setPhone(sysPhone);
            userInfo.setStatus(1);
            //userInfo.setUpdateTime(time);
            baseMapper.insert(userInfo);
        }
        UserEntity userEntity = queryByWxOpenId(userInfo.getOpenId());
        return getResult(userEntity);
    }

//    @Override
//    public Result iosBindMobile(String phone, String code, String appleId, String platform, String sysPhone) {
//        Msg byPhoneAndCode = msgDao.findByPhoneAndCode(phone, code);
//        if (byPhoneAndCode == null) {
//            return Result.error("验证码错误");
//        }
//        msgDao.deleteById(byPhoneAndCode.getId());
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String time = simpleDateFormat.format(new Date());
 //       UserEntity userInfo = queryByPhone(phone);
//        if (userInfo != null) {
//            if (StringUtils.isNotEmpty(userInfo.getA)) {
//                return Result.error("当前手机号已经被其他苹果绑定");
//            }
//            userInfo.setAppleId(appleId);
//            userInfo.setUpdateTime(simpleDateFormat.format(new Date()));
//            baseMapper.updateById(userInfo);
//        } else {
//            userInfo = new UserEntity();
//            userInfo.setSex(0);
//            userInfo.setUserName(phone.replaceAll("(\\d{3})\\d*([0-9a-zA-Z]{4})", "$1****$2"));
//            userInfo.setPhone(phone);
//            userInfo.setPlatform(platform);
//            userInfo.setCreateTime(time);
//            userInfo.setSysPhone(sysPhone);
//            userInfo.setStatus(1);
//            userInfo.setUpdateTime(time);
//            baseMapper.insert(userInfo);
//        }
//      //  UserEntity userEntity = queryByAppleId(userInfo.getAppleId());
        //return getResult(userEntity);
 //   }


    @Override
    public Result wxAppLogin(String wxOpenId, String token) {
        UserEntity userEntity = queryByWxOpenId(wxOpenId);
        if (userEntity != null) {
            if (userEntity.getStatus().equals(2)) {
                return Result.error("50057");
            }
            String s = HttpClientUtil.doGet("https://api.weixin.qq.com/sns/userinfo?access_token=" + token + "&openid=" + wxOpenId);
            AppUserInfo user = JsonUtil.parseObject(s, AppUserInfo.class);
            if (user != null) {
                userEntity.setSex(user.getSex());
            }
            baseMapper.updateById(userEntity);
            return getResult(userEntity);
        } else {
            return Result.error("50053");
        }
    }


    @Override
    public Result registerCode(String phone, String msg, String platform, Integer sysPhone, String password) {
        //校验手机号是否存在
        UserEntity userInfo = queryByPhone(phone);
        if (userInfo != null) {
            if (userInfo.getStatus().equals(2)) {
                return Result.error("50057");
            }

            if (!Objects.equals(MD5Util.md5Encrypt32Upper(password), userInfo.getPassword())) {
                return Result.error("50014");
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            userInfo.setUpdateTime(sdf.format(new Date()));
            baseMapper.updateById(userInfo);
            return getResult(userInfo);
        } else {
//            Msg msg1 = msgDao.findByPhoneAndCode(phone, msg);
//            //校验短信验证码
//            if (msg1 == null) {
//                return Result.error("50011");
//            }
//            msgDao.deleteById(msg1.getId());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = simpleDateFormat.format(new Date());
            userInfo = new UserEntity();
            userInfo.setPhone(phone);
            userInfo.setPassword(MD5Util.md5Encrypt32Upper(password));
            userInfo.setUserName(phone.replaceAll("(\\d{3})\\d*([0-9a-zA-Z]{4})", "$1****$2"));

            userInfo.setPlatform(platform);
            userInfo.setCreateTime(time);
            userInfo.setSysPhone(sysPhone);
            userInfo.setType(1);
            userInfo.setStatus(1);
            CommonInfo one = commonInfoService.findOne(262);
            userInfo.setAvatar(one.getValue());
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            userInfo.setUpdateTime(sdf.format(new Date()));
            baseMapper.insert(userInfo);
            UserMoney userMoney = new UserMoney();
            userMoney.setUserId(userInfo.getUserId());
            userMoney.setMoney(new BigDecimal(0));
           /* userMoney.setIncomeMoney(new BigDecimal(0));
            userMoney.setWithdrawDepositMoney(new BigDecimal(0));*/
            userMoneyDao.insert(userMoney);
            //注册新账号得时候，创建积分仓库
            UserIntegral userIntegral = new UserIntegral();
            userIntegral.setUserId(userInfo.getUserId());
            userIntegral.setIntegralNum(0);
            userIntegralDao.insert(userIntegral);
//            msgDao.deleteById(msg1.getId());
            return getResult(userInfo);
        }
    }


    @Override
    public Result login(String phone, String pwd) {
        UserEntity userEntity = queryByPhone(phone);
        if (userEntity == null) {
            return Result.error("50067");
        }
        if (!userEntity.getPassword().equals(DigestUtils.sha256Hex(pwd))) {
            return Result.error("50020");
        }
        if (userEntity.getStatus().equals(2)) {
            return Result.error("50057");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       // userEntity.setUpdateTime(sdf.format(new Date()));
        baseMapper.updateById(userEntity);
        return getResult(userEntity);
    }


    @Override
    public Result getResult(UserEntity user) {
        //生成token
        String token = jwtUtils.generateToken(user.getUserId());
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("expire", jwtUtils.getExpire());
        map.put("user", user);
        return Result.success(map);
    }


    @Override
    public Result sendMsg(String phone, String state) {
        int code = (int) ((Math.random() * 9 + 1) * 100000);
        System.out.println("sendMsg code is " + code);
        SmsSingleSenderResult result = null;
    /*    if ("bindWx".equals(state)) {
            UserEntity userByPhone = queryByPhone(phone);
            if (userByPhone != null && StringUtils.isNotEmpty(userByPhone.getWxOpenId())) {
                return Result.error("当前手机号已被其他微信账号绑定");
            }
        } else if ("bindIos".equals(state)) {
            UserEntity userByPhone = queryByPhone(phone);
            if (userByPhone != null && StringUtils.isNotEmpty(userByPhone.getAppleId())) {
                return Result.error("当前手机号已被其他苹果账号绑定");
            }
        }*/
        CommonInfo three = commonInfoService.findOne(89);
        //默认使用短信宝
        if (three == null || "1".equals(three.getValue())) {
            //腾讯云短信发送
            return sendMsgTencent(phone, state, code);
        } else if("2".equals(three.getValue())) {
            //阿里云短信发送
            return sendMsgAlibaba(phone, state,code);
        }else {
            //短信宝短信发送
            return sendMsgDXB(phone, state, code);
        }
    }


    private Result sendMsgDXB(String phone, String state, int code) {
        CommonInfo three = commonInfoService.findOne(164);
        CommonInfo four = commonInfoService.findOne(165);
        CommonInfo name = commonInfoService.findOne(12);
        String testUsername = three.getValue(); //在短信宝注册的用户名
        String testPassword = four.getValue(); //在短信宝注册的密码
        String value="";
        switch (state) {
            case "register":
                value = dataSourceMessageSource.getConfigI18ForCach("50229",name.getValue(), String.valueOf(code));
//                value = "【" + name.getValue() + "】验证码: " + code + "，此验证码可用于登录或注册，10分钟内有效，如非您本人操作，可忽略本条消息";
                break;
            case "forget":
                value = dataSourceMessageSource.getConfigI18ForCach("50227",name.getValue(),String.valueOf(code));
//                value = "【" + name.getValue() + "】验证码: " + code + "，您正在执行找回密码操作，10分钟内有效，如非您本人操作，可忽略本条消息";
                break;
            case "bind":
                value = dataSourceMessageSource.getConfigI18ForCach("50228",name.getValue(),String.valueOf(code));
//                value = "【" + name.getValue() + "】验证码: " + code + "，您正在执行绑定手机号操作，10分钟内有效，如非您本人操作，可忽略本条消息";
                break;
            case "hint":
                value = dataSourceMessageSource.getConfigI18ForCach("60129",name.getValue());
//                value = "【" + name.getValue() + "】您报名的订单已被选中，请前往查看";
                break;
            default:
                value = dataSourceMessageSource.getConfigI18ForCach("50229",name.getValue(), String.valueOf(code));
//                value = "【" + name.getValue() + "】验证码: " + code + "，此验证码可用于登录或注册，10分钟内有效，如非您本人操作，可忽略本条消息";
                break;
        }
        String phones = "";
        Integer way = 0;
        if (phone.contains("+86")){
            phones = phone.replace("+86", "");
            way = 1;
        }else {
            try {
                phones = URLEncoder.encode(phone, "UTF-8");
                log.error("编码后的URL:"+phones);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        StringBuilder httpArg = new StringBuilder();
        httpArg.append("u=").append(testUsername).append("&");
        httpArg.append("p=").append(Md5Utils.md5s(testPassword)).append("&");
        httpArg.append("m=").append(phones).append("&");
        httpArg.append("c=").append(Md5Utils.encodeUrlString(value, "UTF-8"));
//        String result = Md5Utils.request("https://api.smsbao.com/sms", httpArg.toString());
        String result;
        if (way != 1){
            result = Md5Utils.request("http://api.smsbao.com/wsms", httpArg.toString());
        }else {
            result = Md5Utils.request("http://api.smsbao.com/sms", httpArg.toString());
        }
        log.error("短信包返回值："+result);
        if ("0".equals(result)) {
            Msg byPhone = msgDao.findByPhone(phone);
            if (byPhone != null) {
                byPhone.setCode(String.valueOf(code));
                byPhone.setPhone(phone);
                msgDao.updateById(byPhone);
            } else {
                Msg msg = new Msg();
                msg.setCode(String.valueOf(code));
                msg.setPhone(phone);
                msgDao.insert(msg);
            }
            UserEntity userByPhone = queryByPhone(phone);
            if (userByPhone != null) {
                return Result.success("login");
            } else {
                return Result.success("register");
            }
        } else {
//            return ResultUtil.error(6, result.errMsg);
            if("30".equals(result)){
                return Result.error( "50221");
            }else if("40".equals(result)){
                return Result.error( "50222");
            }else if("41".equals(result)){
                return Result.error( "50223");
            }else if("43".equals(result)){
                return Result.error( "50224");
            }else if("50".equals(result)){
                return Result.error( "50225");
            }else if("51".equals(result)){
                return Result.error( "50226");
            }
        }

        return Result.error("50202");
    }


    private Result sendMsgAlibaba(String phone,String state, int code) {
        //阿里云短信accessKeyId
        CommonInfo three = commonInfoService.findOne(83);
        String accessKeyId = three.getValue();
        //阿里云短信accessSecret
        CommonInfo four = commonInfoService.findOne(84);
        String accessSecret = four.getValue();
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonInfo name = commonInfoService.findOne(6);
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", name.getValue());

        String value;
        switch (state) {
            case "register":
                value = commonInfoService.findOne(90).getValue();
                request.putQueryParameter("TemplateCode", value);
                request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");
                break;
            case "forget":
                value = commonInfoService.findOne(91).getValue();
                request.putQueryParameter("TemplateCode", value);
                request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");
                break;
            case "bind":
                value = commonInfoService.findOne(92).getValue();
                request.putQueryParameter("TemplateCode", value);
                request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");
                break;
            case "hint":
                value = commonInfoService.findOne(95).getValue();
                request.putQueryParameter("TemplateCode", value);
                break;
            default:
                value = commonInfoService.findOne(90).getValue();
                request.putQueryParameter("TemplateCode", value);
                request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");
                break;
        }
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            String data = response.getData();
            JSONObject jsonObject = JSON.parseObject(data);
            if ("OK".equals(jsonObject.get("Code"))) {
                Msg byPhone = msgDao.findByPhone(phone);
                if (byPhone != null) {
                    byPhone.setCode(String.valueOf(code));
                    byPhone.setPhone(phone);
                    msgDao.updateById(byPhone);
                } else {
                    Msg msg = new Msg();
                    msg.setCode(String.valueOf(code));
                    msg.setPhone(phone);
                    msgDao.insert(msg);
                }
           /*     UserEntity userByPhone = queryByPhone(phone);
                if (userByPhone != null) {
                    return Result.success("login");
                } else {
                    return Result.success("register");
                }*/
                return Result.success("login");
            } else {
                if (jsonObject.get("Message").toString().contains("分钟")) {
                    return Result.error("50118");
                } else if (jsonObject.get("Message").toString().contains("小时")) {
                    return Result.error("50119");
                } else if (jsonObject.get("Message").toString().contains("天")) {
                    return Result.error("50120");
                }
                log.info(jsonObject.get("Message").toString());
                return Result.error("50121");
            }
        } catch (ClientException | com.aliyuncs.exceptions.ClientException e) {
            e.printStackTrace();
        }
        return Result.error("50122");
    }


    private Result sendMsgTencent(String phone, String state, int code) {
        SmsSingleSenderResult result = null;
        try {
            CommonInfo three = commonInfoService.findOne(31);

            String clientId = three.getValue();

            CommonInfo four = commonInfoService.findOne(32);
            String clientSecret = four.getValue();
            CommonInfo name = commonInfoService.findOne(12);
            int anInt = Integer.parseInt(clientId);
            /**
             * 发送短信验证码的状态、
             *
             * 在h5登录环境中 传的状态不是以下三种状态
             */
            SmsSingleSender ssender = new SmsSingleSender(anInt, clientSecret);
            switch (state) {
                case "register":
                    result = ssender.send(0, "86", phone, dataSourceMessageSource.getConfigI18ForCach("50229",name.getValue(), String.valueOf(code)), "", "");
                    break;
                case "forget":
                    result = ssender.send(0, "86", phone, dataSourceMessageSource.getConfigI18ForCach("50227",name.getValue(), String.valueOf(code)), "", "");
                    break;
                case "bind":
                    result = ssender.send(0, "86", phone, dataSourceMessageSource.getConfigI18ForCach("50228",name.getValue(), String.valueOf(code)), "", "");
                    break;
                case "hint":
                    result = ssender.send(0, "86", phone,  dataSourceMessageSource.getConfigI18ForCach("60129",name.getValue()), "", "");
                    break;
                default:
                    result = ssender.send(0, "86", phone, dataSourceMessageSource.getConfigI18ForCach("50229",name.getValue(), String.valueOf(code)), "", "");
                    break;
            }


            System.out.println(result);
            if (result.result == 0) {
                Msg byPhone = msgDao.findByPhone(phone);
                if (byPhone != null) {
                    byPhone.setCode(String.valueOf(code));
                    byPhone.setPhone(phone);
                    msgDao.updateById(byPhone);
                } else {
                    Msg msg = new Msg();
                    msg.setCode(String.valueOf(code));
                    msg.setPhone(phone);
                    msgDao.insert(msg);
                }
                UserEntity userByPhone = queryByPhone(phone);
                if (userByPhone != null) {
                    return Result.success("login");
                } else {
                    return Result.success("register");
                }
            } else {
                return Result.error(6, result.errMsg);
            }
        } catch (HTTPException | JSONException | IOException e) {
            // HTTP 响应码错误
            e.printStackTrace();
        }
        return Result.error("50202");
    }


    @Override
    public Result getOpenId(String code, Long userId) {
        try {
            //微信appid
            CommonInfo one = commonInfoService.findOne(5);
            //微信秘钥
            CommonInfo two = commonInfoService.findOne(21);
            String openid = SnsAPI.oauth2AccessToken(one.getValue(), two.getValue(), code).getOpenid();
            if (StringUtils.isNotEmpty(openid)) {
                UserEntity userEntity = new UserEntity();
                userEntity.setUserId(userId);
                userEntity.setOpenId(openid);
                baseMapper.updateById(userEntity);
                return Result.success().put("data", openid);
            }
            return Result.error("50116");
        } catch (Exception e) {
            log.error("GET_OPENID_FAIL");
            return Result.error("50117");
        }
    }

    @Override
    public UserEntity selectUserById(Long userId) {
        return baseMapper.selectById(userId);
    }


    @Override
    public PageUtils selectUserPage(Integer page, Integer limit,Integer member, String search, Integer sex, String platform, String sysPhone, Integer status,
                                    Integer type, String userName, Integer shopAdminFlag, String email) {
        Page<UserEntity> pages = new Page<>(page, limit);
        return new PageUtils(baseMapper.selectUserPage(pages,member, search, sex, platform, sysPhone, status, type, userName, shopAdminFlag, email));
    }

    @Override
    public int queryInviterCount(String inviterCode) {
        return baseMapper.queryInviterCount(inviterCode);
    }

    @Override
    public int queryUserCount(int type, String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
        if (date == null || date == "") {
            date = simpleDateFormat.format(new Date());
        }
        return baseMapper.queryUserCount(type, date);
    }

    @Override
    public Double queryPayMoney(int type) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
        String date = simpleDateFormat.format(new Date());
        return baseMapper.queryPayMoney(type, date);
    }

    @Override
    public IPage<Map<String, Object>> queryCourseOrder(Page<Map<String, Object>> iPage, int type, String date) {
        return baseMapper.queryCourseOrder(iPage, type, date);
    }

    @Override
    public int userMessage(String date, int type) {
        return baseMapper.userMessage(date, type);
    }


    @Override
    public void pushToSingle(String title, String content, String clientId) {
        GtApiConfiguration apiConfiguration = new GtApiConfiguration();
        //填写应用配置
        apiConfiguration.setAppId(commonInfoService.findOne(61).getValue());
        apiConfiguration.setAppKey(commonInfoService.findOne(60).getValue());
        apiConfiguration.setMasterSecret(commonInfoService.findOne(62).getValue());
        // 接口调用前缀，请查看文档: 接口调用规范 -> 接口前缀, 可不填写appId
        apiConfiguration.setDomain("https://restapi.getui.com/v2/");
        // 实例化ApiHelper对象，用于创建接口对象
        ApiHelper apiHelper = ApiHelper.build(apiConfiguration);
        // 创建对象，建议复用。目前有PushApi、StatisticApi、UserApi
        PushApi pushApi = apiHelper.creatApi(PushApi.class);
        //根据cid进行单推
        PushDTO<Audience> pushDTO = new PushDTO<Audience>();
        // 设置推送参数
        pushDTO.setRequestId(System.currentTimeMillis() + "");
        PushMessage pushMessage = new PushMessage();
        GTNotification notification = new GTNotification();
        pushDTO.setPushMessage(pushMessage);
        // 配置通知栏图标
        notification.setLogo("icon.png"); //配置通知栏图标，需要在客户端开发时嵌入，默认为push.png
        // 配置通知栏网络图标
        notification.setLogoUrl(commonInfoService.findOne(19).getValue() + "/logo.png");
        notification.setTitle(title);
        notification.setBody(content);
        notification.setClickType("url");
        notification.setUrl(commonInfoService.findOne(19).getValue());
        pushMessage.setNotification(notification);
        // 设置接收人信息
        Audience audience = new Audience();
        audience.addCid(clientId);
        pushDTO.setAudience(audience);
        // 进行cid单推
        ApiResult<Map<String, Map<String, String>>> apiResult = pushApi.pushToSingleByCid(pushDTO);
        if (apiResult.isSuccess()) {
            // success
            log.error("消息推送成功：" + apiResult.getData());
        } else {
            // failed
            log.error("消息推送成功失败：code:" + apiResult.getCode() + ", msg: " + apiResult.getMsg());
        }
    }

    @Override
    public Result loginApp(String phone, String password) {
        //md5加密
        String pwd = MD5Util.md5Encrypt32Upper(password);
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        UserEntity userEntity = baseMapper.selectOne(queryWrapper);
        if (userEntity == null) {
            return Result.error("50067");
        }
        if (!userEntity.getPassword().equals(pwd)) {
            return Result.error("50020");
        }
        if (userEntity.getStatus().equals(2)) {
            return Result.error("50057");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //userEntity.setUpdateTime(sdf.format(new Date()));
        baseMapper.updateById(userEntity);
        return getResult(userEntity);

    }

    @Override
    public Result registApp(String userName, String phone, String password, String msg, String platform, String inviterCode) {
        Msg msg1 = msgDao.findByPhoneAndCode(phone, msg);
        //校验短信验证码
        if (msg1 == null) {
            return Result.error("50011");
        }
        msgDao.deleteById(msg1.getId());
        //校验手机号是否存在
        UserEntity userInfo = queryByPhone(phone);
        if (userInfo != null) {
            return Result.error("50010");

        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = simpleDateFormat.format(new Date());
            userInfo = new UserEntity();
            userInfo.setPhone(phone);
            userInfo.setUserName(userName);
            //对密码进行MD5加密
            userInfo.setPassword(MD5Util.md5Encrypt32Upper(password));
            //userInfo.setPlatform(platform);
            userInfo.setCreateTime(time);
            userInfo.setStatus(1);
            if (inviterCode != null || inviterCode != "") {
                userInfo.setInviterCode(inviterCode);
            }
            userInfo.setInvitationCode(InvitationCodeUtil.toSerialCode(1));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            userInfo.setCreateTime(sdf.format(new Date()));
            baseMapper.insert(userInfo);
            UserEntity userEntity = baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("phone", phone));
            userInfo.setInvitationCode(InvitationCodeUtil.toSerialCode(userEntity.getUserId()));
            //设置自己邀请码
            baseMapper.updateById(userInfo);
            //爲用戶創建錢包
            UserMoney userMoney = new UserMoney();
            userMoney.setUserId(userEntity.getUserId());
            userMoney.setMoney(BigDecimal.valueOf(0));
            userMoneyDao.insert(userMoney);
            //注册新账号得时候，创建积分仓库
            UserIntegral userIntegral = new UserIntegral();
            userIntegral.setUserId(userInfo.getUserId());
            userIntegral.setIntegralNum(0);
            userIntegralDao.insert(userIntegral);
            msgDao.deleteById(msg1.getId());
            return getResult(userInfo);
        }

    }

    @Override
    public Result takingOrdersMessage(Page<Map<String, Object>> iPage, Long type, String date) {
        //接单分析
        return Result.success().put("data", new PageUtils(baseMapper.takingOrdersMessage(iPage, type, date)));
    }

    @Override
    public Result updateUserInfo(UserEntity userEntity) {
        UserEntity userEntity1 = new UserEntity();
        userEntity1.setUserName(userEntity.getUserName());
        userEntity1.setAvatar(userEntity.getAvatar());
        userEntity1.setUserId(userEntity.getUserId());
        baseMapper.updateById(userEntity1);

        return Result.success();
    }

    @Override
    public Result wxOpenIdLogin(String openId){
        UserEntity userEntity = queryByWxOpenId(openId);
        if(userEntity!=null){
            return getResult(userEntity);
        }
        return Result.error("50058");
    }

    @Override
    public Result selectUserMessage(Long userId) {
        UserEntity userEntity = baseMapper.selectById(userId);
        return Result.success().put("data", userEntity);
    }

    @Override
    public Result updateUserMessage(UserEntity userEntity) {
        baseMapper.updateById(userEntity);
        return Result.success();
    }

    @Override
    public Result updateShopAdminFlag(Long userId, Integer shopAdminFlag) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(userId);
        userEntity.setShopAdminFlag(shopAdminFlag);
        baseMapper.updateById(userEntity);
        return Result.success();
    }

    @Override
    public Result emailRegister(UserEntity userInfo) {
        Msg msg1 = msgDao.findByPhoneAndCode(userInfo.getEmail(), userInfo.getCode());
        //校验短信验证码
        if (msg1 == null) {
            //return Result.error("验证码不正确");
            return Result.error("10102");
        }
        msgDao.deleteById(msg1.getId());

        //校验手机号是否存在
        boolean emailExist = checkEmailExist(userInfo.getEmail());
        if (emailExist) {
            //return Result.error("手机号已经被注册！");
            return Result.error("10138");

        }

        UserEntity userEntity = null;
        String inviterCode = userEntity.getInviterCode();
        if (StringUtils.isBlank(inviterCode)) {
            inviterCode = commonInfoService.findOne(88).getValue();
        }else {
            userEntity = queryByInvitationCode(inviterCode);
            if (userEntity == null) {
                //return Result.error("邀请码不正确");
                return Result.error("10139");
            }
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(new Date());
        userInfo = new UserEntity();
        userInfo.setEmail(userInfo.getEmail());
        userInfo.setUserName(userInfo.getUserName());
        //对密码进行MD5加密
        userInfo.setPassword(DigestUtils.sha256Hex(userInfo.getPassword()));
        userInfo.setPlatform(userInfo.getPlatform());
        userInfo.setCreateTime(time);
        userInfo.setStatus(1);
        userInfo.setInviterCode(inviterCode);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        userInfo.setCreateTime(sdf.format(new Date()));
        userInfo.setInvitationCode(InvitationCodeUtil.toSerialCode(6));
        baseMapper.insert(userInfo);
        //设置自己邀请码
        baseMapper.updateById(userInfo);
        //给用户创建钱包
        UserMoney userMoney = new UserMoney();
        userMoney.setUserId(userInfo.getUserId());
        userMoney.setMoney(BigDecimal.valueOf(0));
        userMoneyDao.insert(userMoney);

        //注册新账号得时候，创建积分仓库
        UserIntegral userIntegral = new UserIntegral();
        userIntegral.setUserId(userInfo.getUserId());
        userIntegral.setIntegralNum(0);
        userIntegralDao.insert(userIntegral);

        return getResult(userEntity);
    }

    @Override
    public Result emailLogin(LoginForm form) {
        UserEntity userInfo = queryByEmail(form.getEmail());
        if (Objects.isNull(userInfo)) {
            return Result.error(201, "10115");
        }

        if(userInfo.getStatus().equals(2)){
            return Result.error(500,"10104");
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        userInfo.setUpdateTime(sdf.format(new Date()));
        baseMapper.updateById(userInfo);
        return getResult(userInfo);
    }

    @Override
    public Result emailSendMsg(String email, String state) {
        int code = (int) ((Math.random() * 9 + 1) * 100000);

        //获取邮箱发送服务器地址
        String host = commonInfoService.getValue(433);
        //邮箱端口号
        String port = commonInfoService.getValue(435);
        //邮箱授权码
        String password = commonInfoService.getValue(436);
        // 发送邮箱
        String fromUser = commonInfoService.getValue(434);
        String baseName = commonInfoService.getValue(12);

        String subject = dataSourceMessageSource.getConfigI18ForCach("50203", baseName);
        String content = dataSourceMessageSource.getConfigI18ForCach("50204", baseName, String.valueOf(code));
        EmailUtil.sendTextEmail(host, port, fromUser, password, email, subject, content);

        Msg oldMsg = msgDao.findByPhone(email);
        if (Objects.nonNull(oldMsg)) {
            oldMsg.setCode(String.valueOf(code));
            msgDao.updateById(oldMsg);
        } else {
            Msg msg = new Msg();
            msg.setPhone(email);
            msg.setCode(String.valueOf(code));

            msgDao.insert(msg);
        }

        return Result.success("50205");
    }

    @Override
    public Result emailForgetPwd(String pwd, String email, String msg) {
        try {
            Msg byPhoneAndCode = msgDao.findByPhoneAndCode(email, msg);
            //校验短信验证码
            if (byPhoneAndCode == null) {
                return Result.error("10102");
            }

            msgDao.delete(new LambdaQueryWrapper<Msg>()
                    .eq(Msg::getPhone, email)
                    .eq(Msg::getCode, msg)
            );

            UserEntity user = this.getOne(new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getEmail, email));
            if (Objects.isNull(user)) {
                throw new SqxException("10083");
            }
            user.setPassword(DigestUtils.sha256Hex(pwd));
            msgDao.deleteById(byPhoneAndCode.getId());
            baseMapper.updateById(user);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("10057");
        }
    }

    private UserEntity queryByEmail(String email) {
        return this.getOne(new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getEmail, email));
    }

    private boolean checkEmailExist(String email) {
        int count = this.count(new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getEmail, email));

        return 0 < count;
    }

}
