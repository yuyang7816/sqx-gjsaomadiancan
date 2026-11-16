package com.sqx.modules.integral.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.integral.entity.UserIntegralDetails;

public interface UserIntegralDetailsService extends IService<UserIntegralDetails> {

    Result selectIntegraldetailsList(Long userId, Integer page, Integer limit, Integer classify, String languageType);

    Result selectSignIn(Integer page, Integer limit, Long userId);

    void saveI8n(UserIntegralDetails userIntegralDetails, String content);
}
