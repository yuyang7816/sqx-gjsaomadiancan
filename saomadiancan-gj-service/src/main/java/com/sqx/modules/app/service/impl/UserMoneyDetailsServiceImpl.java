package com.sqx.modules.app.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.dao.UserMoneyDetailsDao;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.languageType.entity.LanguageType;
import com.sqx.modules.languageType.service.LanguageTypeService;
import com.sqx.modules.utils.DataSourceMessageSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@Service
public class UserMoneyDetailsServiceImpl extends ServiceImpl<UserMoneyDetailsDao, UserMoneyDetails> implements UserMoneyDetailsService {

    @Autowired
    private LanguageTypeService languageTypeService;
    @Autowired
    private DataSourceMessageSource dataSourceMessageSource;


    @Override
    public Result queryUserMoneyDetails(Integer page, Integer limit, Long userId, Integer classify, Integer type, Long shopId,String startTime,String endTime) {
        HashMap<String,Object> map = new HashMap<>();
        BigDecimal money= BigDecimal.ZERO;
        IPage<UserMoneyDetails> page1 = new Page(page, limit);
        IPage<UserMoneyDetails> moneyDetails = baseMapper.queryUserMoneyDetails(page1, userId, classify, type, shopId, startTime, endTime);

        for (UserMoneyDetails record : moneyDetails.getRecords()) {
            money= money.add(record.getMoney());
        }
        map.put("pageUtils",moneyDetails);
        map.put("money", money);
        return Result.success().put("data",map);
    }

    @Override
    public Double monthIncome(String date, Long userId) {
        return baseMapper.monthIncome(date, userId);
    }

    @Override
    public BigDecimal getShopStatistics(Long shopId, String startTime, String endTime) {

        return  baseMapper.getShopStatistics(shopId,startTime,endTime);


    }

    @Override
    public void saveI18n(UserMoneyDetails userMoneyDetails, String titleCode, String[] titleParam, String contentCode, String[] contentParam) {
        List<LanguageType> enableLanguageTypeList = languageTypeService.getEnableLanguageTypeList();
        for (LanguageType languageType : enableLanguageTypeList) {
            UserMoneyDetails moneyDetails = new UserMoneyDetails();
            BeanUtils.copyProperties(userMoneyDetails, moneyDetails);

            if (StringUtils.isNotBlank(titleCode)) {
                String title = dataSourceMessageSource.getSourceFromCache(titleCode, titleParam, new Locale(languageType.getLanguage()));
                moneyDetails.setTitle(title);
            }

            if (StringUtils.isNotBlank(contentCode)) {
                String content = dataSourceMessageSource.getSourceFromCache(contentCode, contentParam, new Locale(languageType.getLanguage()));
                moneyDetails.setContent(content);
            }

            moneyDetails.setLanguageType(languageType.getLanguage());
            moneyDetails.setLanguageName(languageType.getRemark());

            this.save(moneyDetails);
        }
    }
}
