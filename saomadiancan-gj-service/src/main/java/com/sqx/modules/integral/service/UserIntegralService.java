package com.sqx.modules.integral.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.integral.entity.UserIntegral;

public interface UserIntegralService extends IService<UserIntegral> {

    Result isSignIn(Long userId);

    Result signIn(Long userId);

    Result selectUserIntegral(Long userId);

    Result todayIntegral(Long userId);

    Result findUserMessage(Long userId);

    Result continuousDay(Long userId);
}
