package com.sqx.modules.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.message.entity.MessageInfo;

public interface OrderMessageService extends IService<MessageInfo> {

    Result selectReadFlag(Integer page, Integer limit, String[] shopIds);

    Result checkOrderMessage(Long id);

    Result allcheckOrderMessage(String[] shopId);

    Result selectReadFlagCount(String[] shopId);

}
