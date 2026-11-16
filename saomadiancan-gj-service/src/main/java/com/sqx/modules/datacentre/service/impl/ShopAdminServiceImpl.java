package com.sqx.modules.datacentre.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.modules.datacentre.dao.ShopAdminDao;
import com.sqx.modules.datacentre.entity.SysUserShop;
import com.sqx.modules.datacentre.service.ShopAdminService;
import org.springframework.stereotype.Service;

@Service
public class ShopAdminServiceImpl extends ServiceImpl<ShopAdminDao, SysUserShop> implements ShopAdminService {
}
