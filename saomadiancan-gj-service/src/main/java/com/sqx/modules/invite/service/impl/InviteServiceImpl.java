package com.sqx.modules.invite.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.PageUtils;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.invite.dao.InviteDao;
import com.sqx.modules.invite.entity.Invite;
import com.sqx.modules.invite.service.InviteMoneyService;
import com.sqx.modules.invite.service.InviteService;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.message.service.MessageService;
import com.sqx.modules.utils.DataSourceMessageSource;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 邀请记录
 */
@Service
public class InviteServiceImpl extends ServiceImpl<InviteDao, Invite> implements InviteService {


    @Autowired
    private InviteDao inviteDao;
    @Autowired
    private UserService userService;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private InviteMoneyService inviteMoneyService;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private DataSourceMessageSource dataSourceMessageSource;

    @Override
    public PageUtils selectInviteList(int page, int limit, Integer state, Long userId){
        Page<Map<String,Object>> pages=new Page<>(page,limit);
        if(state==null || state==-1){
            state=null;
        }
        return new PageUtils(inviteDao.selectInviteList(pages,state,userId));
    }


    @Override
    public PageUtils selectInviteUser(int page, int limit, Long userId, Integer state){
        Page<Map<String,Object>> pages=new Page<>(page,limit);
        return new PageUtils(inviteDao.selectInviteUser(pages,userId,state));
    }

    @Override
    public Integer selectInviteByUserIdCountNotTime(Long userId) {
        return inviteDao.selectInviteByUserIdCountNotTime(userId);
    }

    @Override
    public Integer selectInviteByUserIdCount(Long userId, Date startTime, Date endTime) {
        return inviteDao.selectInviteByUserIdCount(userId,startTime,endTime);
    }

    @Override
    public Double selectInviteByUserIdSum(Long userId, Date startTime, Date endTime) {
        return inviteDao.selectInviteByUserIdSum(userId,startTime,endTime);
    }

    @Override
    public Double sumInviteMoney(String time, Integer flag) {
        return inviteDao.sumInviteMoney(time,flag);
    }

    @Override
    public PageUtils inviteAnalysis(int page, int limit, String time, Integer flag) {
        Page<Map<String,Object>> pages=new Page<>(page,limit);
        return new PageUtils(inviteDao.inviteAnalysis(pages,time,flag));
    }

    @Override
    public Integer selectInviteCount(Integer state,Long userId){
        if(state==null || state==-1){
            state=null;
        }
        return inviteDao.selectInviteCount(state,userId);
    }

    @Override
    public Double selectInviteSum(Integer state, Long userId) {
        if(state==null || state==-1){
            state=null;
        }
        return inviteDao.selectInviteSum(state,userId);
    }


    @Transactional
    @Override
    public int saveBody(Long userId, UserEntity userEntity){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(new Date());
        Invite invite=new Invite();
        invite.setState(0);
        invite.setMoney(0.00);
        invite.setUserId(userEntity.getUserId());
        invite.setInviteeUserId(userId);
        invite.setCreateTime(format);
        inviteDao.insert(invite);
        //给被邀请者增加上级id
        UserEntity user=new UserEntity();
        user.setUserId(userId);
        user.setInviterCode(userEntity.getInvitationCode());
        userService.updateById(user);
        updateInvite(userEntity,format,userId);
        return 1;
    }


    @Override
    public void updateInvite(UserEntity userEntity, String format, Long userId){
        Invite invite1 = inviteDao.selectInviteByUser(userEntity.getUserId(), userId);
        if(invite1==null){
            Invite invite=new Invite();
            invite.setState(0);
            invite.setMoney(0.00);
            invite.setUserId(userEntity.getUserId());
            invite.setInviteeUserId(userId);
            invite.setCreateTime(format);
            inviteDao.insert(invite);
            invite1 = inviteDao.selectInviteByUser(userEntity.getUserId(), userId);
        }
        if(invite1.getState()==0){
            CommonInfo one = commonInfoService.findOne(189);
            if(one!=null && StringUtils.isNotEmpty(one.getValue()) && Integer.parseInt(one.getValue())>0){
                Double money=Double.parseDouble(one.getValue());
                invite1.setState(1);
                invite1.setMoney(money);
                inviteDao.updateById(invite1);
                inviteMoneyService.updateInviteMoneySum(money,userEntity.getUserId());
                userMoneyService.updateMoney(1,userEntity.getUserId(),money);
                UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
                userMoneyDetails.setUserId(userEntity.getUserId());
                UserEntity userEntity1 = userService.selectUserById(userId);
                userMoneyDetails.setTitle(dataSourceMessageSource.getConfigI18ForCach("60173",userEntity1.getUserName()));
                userMoneyDetails.setContent(dataSourceMessageSource.getConfigI18ForCach("60131", String.valueOf(money)));
                userMoneyDetails.setType(1);
                userMoneyDetails.setMoney(new BigDecimal(money));
                userMoneyDetails.setCreateTime(format);
                userMoneyDetailsService.save(userMoneyDetails);
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setContent(dataSourceMessageSource.getConfigI18ForCach("60174", String.valueOf(money)));
                messageInfo.setTitle(dataSourceMessageSource.getConfigI18ForCach("60175"));
                messageInfo.setState(String.valueOf(5));
                messageInfo.setUserName(userEntity.getUserName());
                messageInfo.setUserId(String.valueOf(userEntity.getUserId()));
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                messageInfo.setCreateAt(sdf.format(new Date()));
                messageInfo.setIsSee("0");
                messageService.saveBody(messageInfo);
                if(StringUtils.isNotEmpty(userEntity.getClientid())){
                    userService.pushToSingle(dataSourceMessageSource.getConfigI18ForCach("60175"),dataSourceMessageSource.getConfigI18ForCach("60174", String.valueOf(money)),userEntity.getClientid());
                }



            }
        }
    }


}
