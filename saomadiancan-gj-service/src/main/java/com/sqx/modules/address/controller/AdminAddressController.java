package com.sqx.modules.address.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.address.entity.Address;
import com.sqx.modules.address.service.AddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "管理端-地址", tags = {"管理端-地址"})
@RequestMapping(value = "/admin/address")
public class AdminAddressController {

    @Autowired
    private AddressService addressService;

    @ApiOperation("用户地址列表")
    @GetMapping(value = "selectAddressList")
    public Result selectAddressList(Integer page, Integer limit, Long userId){

        return addressService.selectAddressList(userId, page, limit);
    }

    @ApiOperation("添加")
    @PostMapping(value = "insertAddress")
    public Result insertAddress(@RequestBody Address address){

        return addressService.insertAddress(address);
    }

    @ApiOperation("修改")
    @PostMapping(value = "updateAddress")
    public Result updateAddress(@RequestBody Address address){

        return addressService.updateAddress(address);
    }

    @ApiOperation("删除")
    @PostMapping(value = "deleteAddress")
    public Result deleteAddress(Long addressId){

        return addressService.deleteAddress(addressId);
    }



}
