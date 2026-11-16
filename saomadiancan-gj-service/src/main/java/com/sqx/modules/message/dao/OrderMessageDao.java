package com.sqx.modules.message.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.message.entity.MessageInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderMessageDao extends BaseMapper<MessageInfo> {

    IPage<MessageInfo> selectReadFlag(Page<MessageInfo> pages,@Param("shopIds") String[] shopIds);

    int selectReadFlagCount(@Param("shopId") String[] shopId);

    int allcheckOrderMessage(@Param("shopId") String[] shopId);

}
