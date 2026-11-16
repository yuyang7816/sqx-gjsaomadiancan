package com.sqx.modules.address.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.address.dao.AddressDao;
import com.sqx.modules.address.entity.Address;
import com.sqx.modules.address.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class AddressServiceImpl extends ServiceImpl<AddressDao, Address> implements AddressService {

    @Autowired
    private AddressDao addressDao;

    @Override
    public Result selectAddressList(Long userId, Integer page, Integer limit) {
        Page<Address> pages=new Page<>(page,limit);
        PageUtils pageUtils = new PageUtils(addressDao.selectAddressList(pages, userId));
        return Result.success().put("data", pageUtils);
    }

    @Transactional
    @Override
    public Result insertAddress(Address address) {
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        address.setCreateTime(format);
        address.setUpdateTime(format);
        address.setDeleteFlag(0);
        //如果该条地址不是默认地址，则直接添加
        if(address.getAddressDefault()==0){
            baseMapper.insert(address);
            return Result.success();
        }else {
            Address address1 = new Address();
            address1.setAddressDefault(0);
            //先按userId将该用户的所有地址改为非默认
            baseMapper.update(address1, new QueryWrapper<Address>().eq("user_id", address.getUserId()));
            //再添加默认地址
            baseMapper.insert(address);
            return Result.success();
        }

    }

    @Transactional
    @Override
    public Result updateAddress(Address address) {
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        address.setUpdateTime(format);
        //如果该条地址不是默认地址，则直接修改
        if(address.getAddressDefault()==0){
            baseMapper.update(address, new QueryWrapper<Address>().eq("address_id", address.getAddressId()));
        }else {
            Address address1 = new Address();
            address1.setAddressDefault(0);
            //先按userId将该用户的所有地址改为非默认
            baseMapper.update(address1, new QueryWrapper<Address>().eq("user_id", address.getUserId()));
            //再修改
            baseMapper.update(address, new QueryWrapper<Address>().eq("address_id", address.getAddressId()));
        }
        return Result.success();
    }

    @Override
    public Result selectAddressById(Long addressId) {
        Address address = baseMapper.selectById(addressId);
        return Result.success().put("data", address);
    }

    @Override
    public Result deleteAddress(Long addressId) {
        baseMapper.deleteById(addressId);
        return Result.success();
    }
}
