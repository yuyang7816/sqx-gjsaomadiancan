package com.sqx.modules.integral.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.integral.dao.UserIntegralDetailsDao;
import com.sqx.modules.integral.entity.UserIntegralDetails;
import com.sqx.modules.integral.service.UserIntegralDetailsService;
import com.sqx.modules.languageType.entity.LanguageType;
import com.sqx.modules.languageType.service.LanguageTypeService;
import com.sqx.modules.utils.DataSourceMessageSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class UserIntegralDetailsServiceImpl extends ServiceImpl<UserIntegralDetailsDao, UserIntegralDetails> implements UserIntegralDetailsService {

    @Autowired
    private UserIntegralDetailsDao userIntegralDetailsDao;
    @Autowired
    private LanguageTypeService languageTypeService;
    @Autowired
    private DataSourceMessageSource dataSourceMessageSource;

    @Override
    public Result selectIntegraldetailsList(Long userId, Integer page, Integer limit, Integer classify, String languageType) {
        Page<UserIntegralDetails> pages=new Page<>(page,limit);
        PageUtils pageUtils = new PageUtils(userIntegralDetailsDao.selectIntegraldetailsList(pages, userId, classify, languageType));
        return Result.success().put("data", pageUtils);
    }

    @Override
    public Result selectSignIn(Integer page, Integer limit, Long userId) {
        Page<UserIntegralDetails> pages=new Page<>(page,limit);
        PageUtils pageUtils = new PageUtils(userIntegralDetailsDao.selectSignIn(pages, userId));
        return Result.success().put("data", pageUtils);
    }

    @Override
    public void saveI8n(UserIntegralDetails userIntegralDetails, String content) {
        List<LanguageType> enableLanguageTypeList = languageTypeService.getEnableLanguageTypeList();

        for (LanguageType languageType : enableLanguageTypeList) {

            if (StringUtils.isNotBlank(content)) {
                String title = dataSourceMessageSource.getConfigI18ForCach(content, new Locale(languageType.getLanguage()));
                userIntegralDetails.setContent(title);
            }

            userIntegralDetails.setLanguageType(languageType.getLanguage());
            userIntegralDetails.setLanguageName(languageType.getRemark());

            this.save(userIntegralDetails);
        }

    }
}
