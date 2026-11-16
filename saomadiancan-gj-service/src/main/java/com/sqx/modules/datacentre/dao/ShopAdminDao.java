package com.sqx.modules.datacentre.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.datacentre.entity.SysUserShop;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShopAdminDao extends BaseMapper<SysUserShop> {

    List<SysUserShop> selectShopList(Long userId);

}
