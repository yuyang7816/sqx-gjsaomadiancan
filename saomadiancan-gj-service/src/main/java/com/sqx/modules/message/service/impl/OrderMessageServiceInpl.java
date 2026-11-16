package com.sqx.modules.message.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.message.dao.OrderMessageDao;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.message.service.OrderMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderMessageServiceInpl extends ServiceImpl<OrderMessageDao, MessageInfo> implements OrderMessageService {

    @Autowired
    private OrderMessageDao orderMessageDao;

    @Override
    public Result selectReadFlag(Integer page, Integer limit, String[] shopIds) {
        Page<MessageInfo> pages=new Page<>(page,limit);
        PageUtils pageUtils = new PageUtils(orderMessageDao.selectReadFlag(pages, shopIds));
        return Result.success().put("data", pageUtils);
    }

    @Transactional
    @Override
    public Result checkOrderMessage(Long id) {
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setId(id);
        messageInfo.setReadFlag(1);
        baseMapper.updateById(messageInfo);
        return Result.success();
    }

    @Transactional
    @Override
    public Result allcheckOrderMessage(String[] shopId) {

        baseMapper.allcheckOrderMessage(shopId);
        return Result.success();
    }

    @Override
    public Result selectReadFlagCount(String[] shopId) {
        int i = orderMessageDao.selectReadFlagCount(shopId);
        return Result.success().put("data", i);
    }
}
