package com.sqx.modules.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.dao.VipDetailsDao;
import com.sqx.modules.app.entity.VipDetails;
import com.sqx.modules.app.service.VipDetailsService;
import com.sqx.modules.languageType.service.LanguageTypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class VipDetailsServiceImpl extends ServiceImpl<VipDetailsDao, VipDetails> implements VipDetailsService {

    @Resource
    private LanguageTypeService languageTypeService;

    @Override
    public Result selectVipDetails(String languageType) {
        List<VipDetails> vipDetailsList = baseMapper.selectList(new LambdaQueryWrapper<VipDetails>().eq(StringUtils.isNotBlank(languageType), VipDetails::getLanguageType, languageType));
        languageTypeService.fillLanguageName(vipDetailsList);
        return Result.success().put("data", vipDetailsList);
    }

    @Override
    public Result insertVipDetails(VipDetails vipDetails) {
        int cpunt = baseMapper.insert(vipDetails);
        if (cpunt > 0) {
            return Result.success("50034");
        } else {
            return Result.error("50033");
        }
    }
}
