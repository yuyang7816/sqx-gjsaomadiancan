package com.sqx.modules.app.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.VipDetails;
import com.sqx.modules.app.service.UserVipService;
import com.sqx.modules.app.service.VipDetailsService;
import com.sqx.modules.languageType.service.LanguageTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Api(value = "会员管理", tags = {"会员管理"})
@RequestMapping(value = "/vipDetails")
public class VipDetailsController {

    @Autowired
    private VipDetailsService vipDetailsService;
    @Autowired
    private UserVipService userVipService;
    @Resource
    private LanguageTypeService languageTypeService;

    @ApiParam("添加会员的详情信息")
    @PostMapping("/insertVipDetails")
    public Result insertVipDetails(@RequestBody VipDetails vipDetails) {
        return vipDetailsService.insertVipDetails(vipDetails);
    }


    @ApiParam("修改会员的详情信息")
    @PostMapping("/updateVipDetails")
    public Result updateVipDetails(@RequestBody VipDetails vipDetails) {
        vipDetailsService.updateById(vipDetails);
        return Result.success();
    }

    @ApiParam("删除的详情信息")
    @PostMapping("/deleteVipDetails")
    public Result deleteVipDetails(Long id) {
        vipDetailsService.removeById(id);
        return Result.success();
    }

    @ApiParam("查询会员列表")
    @GetMapping("/selectVipDetailsList")
    public Result selectVipDetailsList(Integer page,Integer limit, String languageType) {
        IPage<VipDetails> detailsIPage = vipDetailsService.page(new Page<>(page, limit), new LambdaQueryWrapper<VipDetails>().eq(StringUtils.isNotBlank(languageType), VipDetails::getLanguageType, languageType));
        languageTypeService.fillLanguageName(detailsIPage.getRecords());
        return Result.success().put("data",new PageUtils(detailsIPage));
    }

    @PostMapping("/setUserVip")
    @ApiOperation("设置会员")
    public Result setUserVip(Long userId){
        return userVipService.setUserVip(userId);
    }

}
