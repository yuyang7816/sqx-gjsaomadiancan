package com.sqx.modules.app.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.app.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 用户
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {


    IPage<UserEntity> selectUserPage(Page<UserEntity> page,Integer member, String search, Integer sex, String platform, String sysPhone, Integer status,
                                     Integer type, String userName, Integer shopAdminFlag, String email);

    int queryInviterCount(@Param("inviterCode") String inviterCode);

    int queryUserCount(@Param("type") int type, @Param("date") String date);

    Double queryPayMoney(@Param("type") int type, @Param("date") String date);

    IPage<Map<String, Object>> queryCourseOrder(Page iPage, @Param("type") int type, @Param("date") String date);

    int userMessage(String date, int type);

    int insertUser(UserEntity userEntity);

    IPage<Map<String, Object>> takingOrdersMessage(Page<Map<String, Object>> iPage, @Param("type") Long type, @Param("date") String date);

    IPage<UserEntity> selectAllUser(Page<UserEntity> pages,@Param("phone") String phone,@Param("userType") Integer userType);

    UserEntity findUserMessage(@Param("userId") Long userId);

    IPage<UserEntity> selectUser(Page<UserEntity> pages, @Param("userName") String userName,@Param("phone") String phone);

    UserEntity selectUserById(@Param("userId") Long userId);
}
