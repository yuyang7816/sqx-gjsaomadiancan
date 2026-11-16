package com.sqx.modules.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.exception.SqxException;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Query;
import com.sqx.common.utils.Result;
import com.sqx.modules.languageType.service.LanguageTypeService;
import com.sqx.modules.message.dao.MessageInfoDao;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.message.service.MessageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * 消息
 */
@Service
public class MessageServiceImpl extends
        ServiceImpl<MessageInfoDao, MessageInfo> implements MessageService {

    @Autowired
    private MessageInfoDao messageInfoDao;
    @Resource
    private LanguageTypeService languageTypeService;


    @Override
    public PageUtils selectMessageList(Map<String,Object> params){
        Long userId = (Long)params.get("userId");
        Integer state = (Integer)params.get("state");
        Integer type = (Integer)params.get("type");
        String languageType = (String) params.get("languageType");
        IPage<MessageInfo> page = this.page(
                new Query<MessageInfo>().getPage(params),
                new QueryWrapper<MessageInfo>()
                        .eq(userId!=null,"user_id", userId)
                       // .eq(state!=null,"state", state)
                        .eq(StringUtils.isNotBlank(languageType), "language_type", languageType)
                        .eq(type!=null,"type", type).orderByDesc("create_at")
        );
        languageTypeService.fillLanguageName(page.getRecords());
        return new PageUtils(page);
    }

    @Override
    public int saveBody(MessageInfo messageInfo){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        messageInfo.setCreateAt(sdf.format(now));
        return messageInfoDao.insert(messageInfo);
    }

    @Override
    public int update(MessageInfo messageInfo){
        return messageInfoDao.updateById(messageInfo);
    }

    @Override
    public int delete(Long id){
        return messageInfoDao.deleteById(id);
    }

    @Override
    public MessageInfo selectMessageById(Long id){
        MessageInfo messageInfo = messageInfoDao.selectById(id);
        if (Objects.isNull(messageInfo)) {
            throw new SqxException("信息不存在");
        }
        languageTypeService.fillLanguageName(messageInfo);
        return messageInfo;
    }

    @Override
    public int updateSendState(Long userId, Integer state) {
        return messageInfoDao.updateSendState(userId, state);
    }

    @Override
    public Result findMessageList(Integer page, int limit, Long userId, String languageType) {
        Page<MessageInfo> pages=new Page<>(page,limit);
        IPage<MessageInfo> messageInfoIPage = messageInfoDao.findMessageList(pages, userId, languageType);
        languageTypeService.fillLanguageName(messageInfoIPage.getRecords());
        PageUtils pageUtils = new PageUtils(messageInfoIPage);
        return Result.success().put("data", pageUtils);
    }


}
