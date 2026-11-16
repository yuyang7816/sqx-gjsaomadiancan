package com.sqx.modules.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Query;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.dao.UserCashOutDao;
import com.sqx.modules.app.dao.UserMoneyDao;
import com.sqx.modules.app.dao.UserMoneyDetailsDao;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.entity.UserMoney;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.message.dao.MessageInfoDao;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.pay.dao.CashOutDao;
import com.sqx.modules.pay.entity.CashOut;
import com.sqx.modules.pay.service.CashOutService;
import com.sqx.modules.utils.AmountCalUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weixin.popular.api.MessageAPI;
import weixin.popular.bean.message.templatemessage.TemplateMessage;
import weixin.popular.bean.message.templatemessage.TemplateMessageItem;
import weixin.popular.bean.message.templatemessage.TemplateMessageResult;
import weixin.popular.support.TokenManager;

import javax.websocket.SendResult;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 提现申请记录
 */
@Service
public class CashOutServiceImpl extends ServiceImpl<CashOutDao, CashOut> implements CashOutService {

    /**
     * 提现申请记录
     */
    @Autowired
    private CashOutDao cashOutDao;
    /**
     * 通用配置
     */
    @Autowired
    private CommonInfoService commonInfoService;
    /**
     * app用户
     */
    @Autowired
    private UserService userService;
    @Autowired
    private MessageInfoDao messageInfoDao;
    @Autowired
    private UserCashOutDao userCashOutDao;
    @Autowired
    private UserMoneyDetailsDao userMoneyDetailsDao;
    @Autowired
    private UserMoneyDao userMoneyDao;
    private ReentrantReadWriteLock reentrantReadWriteLock=new ReentrantReadWriteLock(true);

    @Override
    public PageUtils selectCashOutList(Map<String, Object> params) {
        String zhifubaoName = (String) params.get("zhifubaoName");
        String zhifubao = (String) params.get("zhifubao");
        String userId = String.valueOf(params.get("userId"));
        String type = String.valueOf(params.get("type"));
        IPage<CashOut> page = this.page(
                new Query<CashOut>().getPage(params),
                new QueryWrapper<CashOut>()
                        .eq(StringUtils.isNotBlank(zhifubaoName) && !"null".equals(zhifubaoName), "zhifubao_name", zhifubaoName)
                        .eq(StringUtils.isNotBlank(zhifubao) && !"null".equals(zhifubao), "zhifubao", zhifubao)
                        .eq(StringUtils.isNotBlank(userId) && !"null".equals(userId), "user_id", userId)
                        .eq(StringUtils.isNotBlank(type) && !"null".equals(type),"type",type)
                        .orderByDesc("id")
        );
        return new PageUtils(page);
    }


    @Override
    public CashOut selectById(Long id) {
        return cashOutDao.selectById(id);
    }

    @Override
    public int saveBody(CashOut cashOut) {
        return cashOutDao.insert(cashOut);
    }


    @Override
    public int update(CashOut cashOut) {
        return cashOutDao.updateById(cashOut);
    }


    @Override
    public void cashOutSuccess(UserEntity userByWxId, String date, String money, String payWay, String url) {
        if(userByWxId!=null){
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setState(String.valueOf(5));
            messageInfo.setContent("您好，您的提现转账成功，请注意查收！提现金额【" + money + "元】！支付宝收款账号 " + payWay + "感谢您的使用！如有疑问请在公众号中发送您的问题联系客服");
            messageInfo.setTitle("提现成功通知");
            messageInfo.setUserName(userByWxId.getUserName());
            messageInfo.setUserId(String.valueOf(userByWxId.getUserId()));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
            messageInfo.setCreateAt(sdf.format(now));
            messageInfo.setIsSee("0");
            messageInfoDao.insert(messageInfo);
            if (userByWxId.getClientid() != null) {
                userService.pushToSingle("提现成功通知", "您好，您的提现转账成功，请注意查收！提现金额【" + money + "元】！支付宝收款账号 " + payWay + "感谢您的使用！如有疑问请在公众号中发送您的问题联系客服", userByWxId.getClientid());
            }
            CommonInfo three = commonInfoService.findOne(39);
            String apkey = "";
            if (three != null) {
                apkey = three.getValue();
            }
            if(StringUtils.isNotBlank(userByWxId.getOpenId())){
                LinkedHashMap<String, TemplateMessageItem> data = new LinkedHashMap<>();
                data.put("first", new TemplateMessageItem("您好，您的提现转账成功，请注意查收", "#d71345"));
                data.put("keyword1", new TemplateMessageItem(money + " 元", "#d71345"));
                data.put("keyword2", new TemplateMessageItem(date, "#d71345"));
                data.put("remark", new TemplateMessageItem("支付宝收款账号 " + payWay + "感谢您的使用！如有疑问请在公众号中发送您的问题联系客服", null));
                sendWxMessage(apkey, data, userByWxId.getOpenId(), url);
            }
        }

    }

    /**
     * 退款成功通知
     *
     * @param
     * @param date
     * @param money
     * @param url
     */
    @Override
    public void refundSuccess(UserEntity userByWxId, String date, String money, String url, String content) {
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setState(String.valueOf(5));
        messageInfo.setContent(content);
        messageInfo.setTitle("提现失败提醒");
        messageInfo.setUserName(userByWxId.getUserName());
        messageInfo.setUserId(String.valueOf(userByWxId.getUserId()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        messageInfo.setCreateAt(sdf.format(now));
        messageInfo.setIsSee("0");
        messageInfoDao.insert(messageInfo);
        if (userByWxId.getClientid() != null) {
            userService.pushToSingle("提现失败提醒", content, userByWxId.getClientid());
        }
        CommonInfo three = commonInfoService.findOne(77);
        String apkey = "";
        if (three != null) {
            apkey = three.getValue();
        }
        if(StringUtils.isNotBlank(userByWxId.getOpenId())){
            LinkedHashMap<String, TemplateMessageItem> data = new LinkedHashMap<>();
            data.put("first", new TemplateMessageItem("您好，您发起的提现失败了", "#d71345"));
            data.put("keyword1", new TemplateMessageItem(money + " 元", "#d71345"));
            data.put("keyword2", new TemplateMessageItem(date, "#d71345"));
            data.put("keyword3", new TemplateMessageItem(content, "#d71345"));
            data.put("remark", new TemplateMessageItem("请您按照失败原因修改相关信息后，重新提现！", null));
            sendWxMessage(apkey, data, userByWxId.getOpenId(), url);
        }

    }

    @Override
    public Double selectCashOutSum(Long userId, Date startTime, Date endTime) {
        return cashOutDao.selectCashOutSum(userId, startTime, endTime);
    }

    @Override
    public Double sumMoney(String time, Integer flag) {
        return cashOutDao.sumMoney(time, flag);
    }

    @Override
    public Integer countMoney(String time, Integer flag) {
        return cashOutDao.countMoney(time, flag);
    }

    @Override
    public Integer stayMoney(String time, Integer flag) {
        return cashOutDao.stayMoney(time, flag);
    }

    @Override
    public void updateMayMoney(int i, Long userId, Double money) {
        cashOutDao.updateMayMoney(i, userId, money);
    }


    @Override
    public List<CashOut> selectCashOutLimit3() {
        return cashOutDao.selectCashOutLimit3();
    }

    private void sendWxMessage(String templateId, LinkedHashMap<String, TemplateMessageItem> data, String openid, String url) {
        TemplateMessage templateMessage = new TemplateMessage();
        templateMessage.setTouser(openid);
        templateMessage.setTemplate_id(templateId);
        templateMessage.setData(data);
        templateMessage.setUrl(url);
        TemplateMessageResult templateMessageResult = MessageAPI.messageTemplateSend(getWxToken(), templateMessage);
        if (templateMessageResult.isSuccess()) {
            new SendResult();
        } else {
            new SendResult();
        }
    }

    private String getWxToken() {
        try {
            //微信appid
            CommonInfo one = commonInfoService.findOne(5);
            return TokenManager.getToken(one.getValue());
        } catch (Exception e) {
            throw new RuntimeException("GET_ACCESS_TOKEN_FAIL");
        }
    }


    @Override
    public Result cashMoney(Long userId, Double money, Integer classify) {
        reentrantReadWriteLock.writeLock().lock();
        try{
        return cashMoneys(userId, money, classify);
        }catch (Exception e){
            e.printStackTrace();
            log.error("发起提现出错！"+e.getMessage(),e);
        }finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
        return Result.error("系统繁忙，请稍后再试！");
    }

    @Transactional
    public Result cashMoneys(Long userId, Double money, Integer classify){

            if (money == null || money <= 0.00) {
                return Result.error("请不要输入小于0的数字,请输入正确的提现金额！");
            }
            //最低提现金额
            CommonInfo one = commonInfoService.findOne(112);
            if (one != null && money < Double.parseDouble(one.getValue())) {
                return Result.error("输入金额不满足最低提现金额，请重新输入！");
            }
            //最高提现金额
            CommonInfo one2 = commonInfoService.findOne(153);
            if (one2 != null && money > Double.parseDouble(one2.getValue())) {
                return Result.error(-100, "输入金额过大，不能大于" + one2.getValue() + "，请重新输入！");
            }
            //手续费
            CommonInfo one1 = commonInfoService.findOne(114);

            //计算提现金额所需要的手续费  小于0.01 的按0.01来算
            Double mul = AmountCalUtils.mul(money, Double.parseDouble(one1.getValue()));
            if (mul < 0.01) {
                mul = 0.01;
            }
            //查询账户的余额
            UserMoney userMoney = userMoneyDao.selectMoney(userId);
            UserEntity userEntity = userService.selectUserById(userId);
            //用户到账
            Double moneySum = AmountCalUtils.sub(new BigDecimal(money), new BigDecimal(mul)).doubleValue(); //金额=提现金额+手续费
            if (userMoney.getMoney().compareTo(BigDecimal.valueOf(money)) > -1) { //用户金额足够
                //扣除可提现金额直接在数据库进行操作
                //增加金额操作记录
                userCashOutDao.updateMoney(2,userId,money);
                UserMoneyDetails moneyDetails = new UserMoneyDetails();
                moneyDetails.setUserId(userId);
                moneyDetails.setTitle("提现：" + money);
                if(classify==1){
                    moneyDetails.setContent("支付宝提现：" + moneySum + "，扣除总金额："+money+"，手续费：" + mul );
                }else{
                    moneyDetails.setContent("微信提现：" + moneySum + "，扣除总金额："+money+"，手续费：" + mul );
                }
                moneyDetails.setType(2);
                moneyDetails.setMoney(new BigDecimal(money));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                moneyDetails.setCreateTime(sdf.format(new Date()));
                userMoneyDetailsDao.insert(moneyDetails);
                CashOut cashOut = new CashOut();
                cashOut.setState(0);
                cashOut.setZhifubao(userEntity.getZhiFuBao());
                cashOut.setZhifubaoName(userEntity.getZhiFuBaoName());
                cashOut.setMoney(moneySum.toString());
                cashOut.setCreateAt(sdf.format(new Date()));
                cashOut.setUserId(userId);
                cashOut.setRate(mul);
                cashOut.setOrderNumber(String.valueOf(System.currentTimeMillis()));
                cashOut.setClassify(classify);
                baseMapper.insert(cashOut);
                //扣除金额直接在数据库进行操作
                return Result.success("提现成功，将在三个工作日内到账，请耐心等待！");
            }else{
                return Result.error("金额不足，请输入正确的金额！");
            }
    }


}
