package com.sqx.modules.app.service;


import com.sqx.common.utils.Result;

/**
 * @author liyuan
 * @description user_follow
 * @date 2021-08-12
 */
public interface UserFollowService {

    /**
     * 关注 / 取消关注
     */
    Result insert(Long userId, Long followUserId);

    /**
     * 查看我的关注
     */
    Result selectMyFollow(Long userId,Long page,Long limit);

    /**
     * 查看我的粉丝
     */
    Result selectFans(Long userId,Long page,Long limit);

    Result selectFollowUser( Long userId, Long followUserId);
    /**
     * 查询用户的最好
     */
}
