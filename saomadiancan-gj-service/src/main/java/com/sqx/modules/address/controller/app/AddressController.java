package com.sqx.modules.address.controller.app;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sqx.common.utils.Result;
import com.sqx.modules.address.entity.Address;
import com.sqx.modules.address.service.AddressService;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.utils.HttpClientUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Api(value = "用户端-地址", tags = {"用户端-地址"})
@RequestMapping(value = "/app/address")
@Slf4j
public class AddressController {

    @Autowired
    private AddressService addressService;
    @Autowired
    private CommonInfoService commonInfoService;

    @Login
    @ApiOperation("获取地址列表")
    @GetMapping(value = "/selectAddressList")
    public Result selectAddressList(@RequestAttribute Long userId, Integer page, Integer limit){

        return addressService.selectAddressList(userId, page, limit);
    }

    @Login
    @ApiOperation("查看单个地址信息")
    @GetMapping(value = "/selectAddressById")
    public Result selectAddressById(Long addressId){

        return addressService.selectAddressById(addressId);
    }

    @Login
    @ApiOperation("新增地址")
    @PostMapping(value = "/insertAddress")
    public Result insertAddress(@RequestAttribute Long userId, @RequestBody Address address){
        address.setUserId(userId);
        return addressService.insertAddress(address);
    }

    @Login
    @ApiOperation("修改地址")
    @PostMapping(value = "/updateAddress")
    public Result updateAddress(@RequestAttribute Long userId, @RequestBody Address address){
        address.setUserId(userId);
        return addressService.updateAddress(address);
    }

    @Login
    @ApiOperation("删除地址")
    @PostMapping(value = "/deleteAddress")
    public Result deleteAddress(Long addressId){

        return addressService.deleteAddress(addressId);
    }

    @GetMapping("/selectCity")
    @ApiOperation("根据经纬度获取城市")
    public Result selectCity(String lat,String lng){
        CommonInfo one = commonInfoService.findOne(235);
        String url="https://apis.map.qq.com/ws/geocoder/v1/";
        Map<String,String> maps=new HashMap<>();
        maps.put("location",lat+","+lng);
        maps.put("key",one.getValue());
        String data = HttpClientUtil.doGet(url, maps);
        JSONObject jsonObject = JSON.parseObject(data);
        String status = jsonObject.getString("status");
        if("0".equals(status)){
            JSONObject result = jsonObject.getJSONObject("result");
            JSONObject adInfo = result.getJSONObject("ad_info");
            return Result.success().put("data",adInfo);
        }else{
            log.error("转换失败！！！原因:"+jsonObject.getString("message"));
        }
        return Result.error("50038");
    }

}
