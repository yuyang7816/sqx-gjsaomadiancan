package com.sqx.modules.address.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.address.entity.Address;

public interface AddressService extends IService<Address> {

    Result selectAddressList(Long userId, Integer page, Integer limit);

    Result insertAddress(Address address);

    Result updateAddress(Address address);

    Result selectAddressById(Long addressId);

    Result deleteAddress(Long addressId);

}
