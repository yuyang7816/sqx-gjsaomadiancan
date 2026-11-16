package com.sqx.modules.banner.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.banner.dao.BannerDao;
import com.sqx.modules.banner.entity.Banner;
import com.sqx.modules.banner.service.BannerService;
import com.sqx.modules.languageType.service.LanguageTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * banner图
 */
@Service
public class BannerServiceImpl extends ServiceImpl<BannerDao, Banner> implements BannerService {

    @Autowired
    private BannerDao bannerDao;
    @Resource
    private LanguageTypeService languageTypeService;


    @Override
    public List<Banner> selectBannerList(Integer classify, String languageType) {
        List<Banner> bannerList = bannerDao.selectList(classify, languageType);
        languageTypeService.fillLanguageName(bannerList);
        return bannerList;
    }

    @Override
    public List<Banner> selectBannerLists(Integer classify, String languageType) {
        List<Banner> bannerList = bannerDao.selectLists(classify, languageType);
        languageTypeService.fillLanguageName(bannerList);
        return bannerList;
    }

    @Override
    public PageUtils selectPage(Integer page, Integer limit, Integer classify, Integer state, String languageType){
        IPage<Banner> pages=new Page<>(page,limit);
        Page<Banner> bannerPage = bannerDao.selectPage(pages, classify, state, languageType);
        languageTypeService.fillLanguageName(bannerPage.getRecords());
        return new PageUtils(bannerPage);
    }


    @Override
    public int saveBody(String image, String url, Integer sort) {
        Banner banner = new Banner();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        banner.setImageUrl(image);
        banner.setCreateTime(sdf.format(now));
        banner.setState(1);
        banner.setUrl(url);
        banner.setSort(sort == null ? 1 : sort);
        return bannerDao.insert(banner);
    }

    @Override
    public int insertBanner(Banner banner) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        banner.setCreateTime(sdf.format(now));
        banner.setState(2);
        return bannerDao.insert(banner);
    }


    @Override
    public Banner selectBannerById(Long id) {
        return bannerDao.selectById(id);
    }

    @Override
    public Result updateBannerStateById(Long id) {
        Banner banner = selectBannerById(id);
        if (banner != null) {
            if (banner.getState() == 1) {
                banner.setState(2);
            } else {
                banner.setState(1);
            }
            bannerDao.updateById(banner);
            return Result.success();
        } else {
            return Result.error("50059");
        }
    }

    @Override
    public int updateBannerById(Banner banner) {
        this.updateById(banner);
        return 0;
    }


}
