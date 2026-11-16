package com.sqx.modules.goods.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.goods.dao.*;
import com.sqx.modules.goods.entity.*;
import com.sqx.modules.goods.i18n.attr.db.service.IGoodsAttrI18nService;
import com.sqx.modules.goods.i18n.attr.domain.entity.GoodsAttrI18n;
import com.sqx.modules.goods.i18n.attrValue.db.service.IGoodsAttrValueI18nService;
import com.sqx.modules.goods.i18n.attrValue.domain.entity.GoodsAttrValueI18n;
import com.sqx.modules.goods.i18n.goods.db.service.IGoodsI18nService;
import com.sqx.modules.goods.i18n.goods.domain.entity.GoodsI18n;
import com.sqx.modules.goods.i18n.sku.db.service.IGoodsSkuI18nService;
import com.sqx.modules.goods.i18n.sku.domain.entity.GoodsSkuI18n;
import com.sqx.modules.goods.service.GoodsAttrService;
import com.sqx.modules.goods.service.GoodsAttrValueService;
import com.sqx.modules.goods.service.GoodsService;
import com.sqx.modules.goods.service.GoodsSkuService;
import com.sqx.modules.goods.utils.SkuUtil;
import com.sqx.modules.languageType.entity.LanguageType;
import com.sqx.modules.languageType.service.LanguageTypeService;
import com.sqx.modules.sys.dao.SysUserRoleDao;
import com.sqx.modules.sys.entity.SysUserRoleEntity;
import com.sqx.modules.utils.DataSourceMessageSource;
import com.sqx.modules.utils.HttpClientUtil;
import com.sqx.modules.utils.RequestUtil;
import com.sqx.modules.utils.translate.TransApi;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsDao, Goods> implements GoodsService {

    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private GoodsClassifyDao goodsClassifyDao;
    @Autowired
    private GoodsAttrService goodsAttrService;
    @Autowired
    private GoodsAttrDao goodsAttrDao;
    @Autowired
    private GoodsSkuService goodsSkuService;
    @Autowired
    private GoodsAttrValueService goodsAttrValueService;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private GoodsShopDao goodsShopDao;
    @Autowired
    private SysUserRoleDao sysUserRoleDao;
    @Autowired
    private GoodsShopRelevancyDao goodsShopRelevancyDao;
    @Autowired
    private DataSourceMessageSource dataSourceMessageSource;
    @Autowired
    private LanguageTypeService languageTypeService;
    @Autowired
    private IGoodsI18nService iGoodsI18nService;
    @Autowired
    private IGoodsSkuI18nService iGoodsSkuI18nService;
    @Autowired
    private IGoodsAttrI18nService iGoodsAttrI18nService;
    @Autowired
    private IGoodsAttrValueI18nService iGoodsAttrValueI18nService;

    @Transactional
    @Override
    public Result insertGoods(Goods goods) {
        if (StringUtils.isBlank(goods.getLanguageType())) {
            goods.setLanguageType(RequestUtil.getAcceptLanguage());
        }

        //添加商品信息
        goods.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        goods.setStatus(0);
        goods.setSalesVolume(0);
        goodsDao.insert(goods);

        // 先保存当前语言的商品信息
        GoodsI18n goodsI18n = new GoodsI18n();
        BeanUtils.copyProperties(goods, goodsI18n);
        goodsI18n.setGoodsId(goods.getGoodsId());
        goodsI18n.setLanguageType(goods.getLanguageType());
        goodsI18n.setLanguageName(languageTypeService.getNameByType(goods.getLanguageType()));
        iGoodsI18nService.save(goodsI18n);

        // 保存其他语言信息
        saveGoodsI18n(goods);

        //添加规格信息
        if (!goods.getAttr().isEmpty()) {
            List<GoodsAttr> attr = goods.getAttr();
            for (GoodsAttr a : attr) {
                a.setGoodsId(goods.getGoodsId());
                a.setLanguageType(goods.getLanguageType());
                goodsAttrService.saveGoodsAttr(a);
            }
        }

        List<GoodsSku> sku = goods.getSku();
        for (GoodsSku v : sku) {
            v.setGoodsId(goods.getGoodsId());
            v.setLanguageType(goods.getLanguageType());
            goodsSkuService.save(v);

            saveSkuI18n(v);
        }

        if (goods.getShopId() != null) {
            GoodsShop goodsShop = goodsShopDao.selectById(goods.getShopId());
            if (goodsShop != null) {
                GoodsShopRelevancy relevancy = new GoodsShopRelevancy();
                relevancy.setShopId(goods.getShopId());
                relevancy.setGoodsId(goods.getGoodsId());
                relevancy.setSales(0);
                relevancy.setInventory(goods.getInventory());
                relevancy.setStatus(0);
                relevancy.setCreateTime(DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
                goodsShopRelevancyDao.insert(relevancy);
            }
        }
        return Result.success();


        /*//添加规格信息
        List<GoodsSku> goodsSkuList = goods.getGoodsSkuList();
        get(goodsSkuList, 0L, goods.getGoodsId());
        return Result.success();*/
    }

    private void saveSkuI18n(GoodsSku v) {
        GoodsSkuI18n skuI18n = new GoodsSkuI18n();
        skuI18n.setSkuId(v.getId());
        skuI18n.setDetailJson(v.getDetailJson());
        skuI18n.setLanguageType(v.getLanguageType());
        skuI18n.setLanguageName(languageTypeService.getNameByType(v.getLanguageType()));
        iGoodsSkuI18nService.save(skuI18n);

        List<LanguageType> enableLanguageTypeList = languageTypeService.getEnableLanguageTypeList();
        enableLanguageTypeList = enableLanguageTypeList.stream()
                .filter(item -> !Objects.equals(v.getLanguageType(), item.getLanguage()))
                .collect(Collectors.toList());

        String detailJson = v.getDetailJson();
        if (StringUtils.isBlank(detailJson)) {
            return;
        }

        List<GoodsSkuI18n> skuI18nList = new ArrayList<>();
        for (LanguageType languageType : enableLanguageTypeList) {
            StringBuilder stringBuilder=new StringBuilder();
            String[] split = detailJson.split(",");
            for (String str:split){
                String tranDetail = null;
                try {
                    tranDetail = TransApi.getTransResult(str, v.getLanguageType(), languageType.getLanguage());
                    Thread.sleep(1000);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                stringBuilder.append(tranDetail).append(",");
            }


            GoodsSkuI18n goodsSkuI18n = new GoodsSkuI18n();
            goodsSkuI18n.setSkuId(v.getId());
            String deleteWhitespace = StringUtils.deleteWhitespace(stringBuilder.substring(0,stringBuilder.length()-1));
            goodsSkuI18n.setDetailJson(deleteWhitespace);
            goodsSkuI18n.setLanguageType(languageType.getLanguage());
            goodsSkuI18n.setLanguageName(languageType.getRemark());

            skuI18nList.add(goodsSkuI18n);
        }

        iGoodsSkuI18nService.saveBatch(skuI18nList);
    }

    @Async
    public void saveGoodsI18n(Goods goods) {
        // 系统支持的语言列表
        List<LanguageType> enableLanguageTypeList = languageTypeService.getEnableLanguageTypeList();
        enableLanguageTypeList = enableLanguageTypeList.stream()
                .filter(item -> !Objects.equals(goods.getLanguageType(), item.getLanguage()))
                .collect(Collectors.toList());

        // 原语言类型
        String goodsLanguageType = goods.getLanguageType();

        // 需要翻译的字段
        String goodsName = goods.getGoodsName();
        String goodsSynopsis = goods.getGoodsSynopsis();
        String goodsDescribe = goods.getGoodsDescribe();
        String goodsLabel = goods.getGoodsLabel();

        String src = goodsName + "|" + goodsSynopsis + "|" + goodsDescribe + "|" + goodsLabel;

        List<GoodsI18n> goodsI18nList = new ArrayList<>();
        for (LanguageType languageType : enableLanguageTypeList) {
            // 翻译
            String transResult = null;
            try {
                transResult = TransApi.getTransResult(src, goodsLanguageType, languageType.getLanguage());
                Thread.sleep(1000);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            // 百度翻译api限制, 需要等待1秒
            waitTranslationInterval();

            String[] tranArr = transResult.split("\\|");

            // 翻译完成之后，保存商品信息
            GoodsI18n goodsI18n = new GoodsI18n();
            BeanUtils.copyProperties(goods, goodsI18n);
            goodsI18n.setGoodsId(goods.getGoodsId());
            goodsI18n.setGoodsName(StringUtils.deleteWhitespace(tranArr[0]));
            goodsI18n.setGoodsSynopsis(StringUtils.deleteWhitespace(tranArr[1]));
            goodsI18n.setGoodsDescribe(StringUtils.deleteWhitespace(tranArr[2]));
            goodsI18n.setGoodsLabel(StringUtils.deleteWhitespace(tranArr[3]));
            goodsI18n.setLanguageType(languageType.getLanguage());
            goodsI18n.setLanguageName(languageType.getRemark());
            goodsI18nList.add(goodsI18n);
        }

        iGoodsI18nService.saveBatch(goodsI18nList);
    }

    private void waitTranslationInterval() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /*public void get(List<GoodsSku> goodsSkuList, Long parentId, Long goodsId){
        for(int i = 0;i<goodsSkuList.size();i++){
            GoodsSku goodsSku = goodsSkuList.get(i);
            goodsSku.setParentId(parentId);
            goodsSku.setGoodsId(goodsSku.getGoodsId());
            goodsSku.setGoodsId(goodsId);
            goodsSkuDao.insertGoodsSku(goodsSku);
            if(goodsSku.getGoodsSkuList().size()>0){
                get(goodsSku.getGoodsSkuList(), goodsSku.getSkuId(), goodsId);
            }
        }
    }*/


    @Override
    public Result selectGoodsList(Long shopId, String language) {
        // 店铺商品
        List<Goods> shopGoodsList = goodsDao.selectGoodsClassifyList(shopId);
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < shopGoodsList.size(); i++) {
            Map<String, Object> hashMap = new HashMap<>();
            GoodsClassify goodsClassify = goodsDao.selectClassify(shopGoodsList.get(i).getClassifyId(), shopId, language);
            List<Goods> goodsList1 = goodsDao.selectGoodsByClassify(shopGoodsList.get(i).getClassifyId(), shopId, language);
            hashMap.put("classifyId", shopGoodsList.get(i).getClassifyId());
            hashMap.put("classifyName", goodsClassify.getClassifyName());
            hashMap.put("goodsList", goodsList1);
            list.add(hashMap);
        }

        return Result.success().put("data", list);
    }

    @Transactional
    @Override
    public Result updateGoods(Goods goods) {
        if (StringUtils.isBlank(goods.getLanguageType())) {
            goods.setLanguageType(RequestUtil.getAcceptLanguage());
        }

        //1.修改attr
        if (goods.getAttr() != null && goods.getAttr().size() > 0) {
            for (GoodsAttr attr : goods.getAttr()) {
                attr.setGoodsId(goods.getGoodsId());
                attr.setLanguageType(goods.getLanguageType());
                goodsAttrService.updateGoodsAttr(attr);
            }
        } else { //修改为单规格，删除所有attr
            goodsAttrService.deleteAttrAndValue(goods.getGoodsId());
        }
        //2.修改所有sku
        goodsSkuService.deleteGoodsSkuByGoodsId(goods.getGoodsId());
        List<GoodsSku> sku = goods.getSku();
        for (GoodsSku goodsSku : sku) {
            goodsSku.setLanguageType(goods.getLanguageType());
            goodsSku.setGoodsId(goods.getGoodsId());
            goodsSkuService.save(goodsSku);

            saveSkuI18n(goodsSku);
        }
        if (goods.getShopId()!=null && goods.getShopId()!=0){
            GoodsShopRelevancy goodsShopRelevancy = goodsShopRelevancyDao.selectOne(new QueryWrapper<GoodsShopRelevancy>().eq("shop_id", goods.getShopId()).eq("goods_id", goods.getGoodsId()));
            goodsShopRelevancy.setInventory(goods.getInventory());
            goodsShopRelevancyDao.updateById(goodsShopRelevancy);
        }
        goodsDao.updateById(goods);

        updateGoodsI18n(goods);
        return Result.success();
    }

    private void updateGoodsI18n(Goods goods) {
        String goodsName = goods.getGoodsName();
        String goodsSynopsis = goods.getGoodsSynopsis();
        String goodsDescribe = goods.getGoodsDescribe();
        String goodsLabel = goods.getGoodsLabel();

        LambdaQueryWrapper<GoodsI18n> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(GoodsI18n::getGoodsId, goods.getGoodsId())
                .eq(GoodsI18n::getLanguageType, goods.getLanguageType());
        GoodsI18n goodsI18n = iGoodsI18nService.getOne(queryWrapper);

        if (!Objects.equals(goodsName, goodsI18n.getGoodsName())
                || !Objects.equals(goodsSynopsis, goodsI18n.getGoodsSynopsis())
                || !Objects.equals(goodsDescribe, goodsI18n.getGoodsDescribe())
                || !Objects.equals(goodsLabel, goodsI18n.getGoodsLabel())
        ) {
            // 删除原来的国际化
            iGoodsI18nService.remove(new LambdaQueryWrapper<GoodsI18n>().eq(GoodsI18n::getGoodsId, goods.getGoodsId()));

            // 保存当前语言
            GoodsI18n i18n = new GoodsI18n();
            i18n.setGoodsId(goods.getGoodsId());
            i18n.setLanguageType(goods.getLanguageType());
            i18n.setLanguageName(languageTypeService.getNameByType(goods.getLanguageType()));
            i18n.setGoodsName(goodsName);
            i18n.setGoodsSynopsis(goodsSynopsis);
            i18n.setGoodsDescribe(goodsDescribe);
            i18n.setGoodsLabel(goodsLabel);
            iGoodsI18nService.save(i18n);

            // 翻译其他语言
            List<LanguageType> enableLanguageTypeList = languageTypeService.getEnableLanguageTypeList();
            enableLanguageTypeList = enableLanguageTypeList.stream()
                    .filter(item -> !Objects.equals(goods.getLanguageType(), item.getLanguage()))
                    .collect(Collectors.toList());

            String src = goodsName + "|" + goodsSynopsis + "|" + goodsLabel;

            List<GoodsI18n> goodsI18nList = new ArrayList<>();
            for (LanguageType languageType : enableLanguageTypeList) {
                String resp = null;
                try {
                    resp = TransApi.getTransResult(src, goodsI18n.getLanguageType(), languageType.getLanguage());
                    Thread.sleep(1000);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }


                String[] respArr = resp.split("\\|");

                GoodsI18n tranGoodsI18n = new GoodsI18n();
                tranGoodsI18n.setGoodsId(goods.getGoodsId());
                tranGoodsI18n.setLanguageType(languageType.getLanguage());
                tranGoodsI18n.setLanguageName(languageType.getRemark());
                tranGoodsI18n.setGoodsName(StringUtils.deleteWhitespace(respArr[0]));
                tranGoodsI18n.setGoodsSynopsis(StringUtils.deleteWhitespace(respArr[1]));
//                tranGoodsI18n.setGoodsDescribe(StringUtils.deleteWhitespace(respArr[2]));
                tranGoodsI18n.setGoodsLabel(StringUtils.deleteWhitespace(respArr[2]));

                goodsI18nList.add(tranGoodsI18n);
            }

            iGoodsI18nService.saveBatch(goodsI18nList);
        }
    }

    @Transactional
    @Override
    public Result isFormatAttr(GoodsAttr goodsAttr, String coverImg,  String price) {
        if (StringUtils.isBlank(goodsAttr.getLanguageType())) {
            goodsAttr.setLanguageType(RequestUtil.getAcceptLanguage());
        }

        //1.获取商品规格值
        List<GoodsAttrValue> attr = goodsAttr.getAttrValue();
        int attrSize = attr.size();
        //2.准备返回值
        Map<String, Object> map = new HashMap<>();
        List<String> header = new ArrayList<>(); //表头
        List<Map<String, Object>> value = new ArrayList<>(); //规格排列组合
        //3.sku规格处理集合
        List<List<String>> skuInputList = new ArrayList<>();
        //4.循环规格值
        for (int i = 0; i < attrSize; i++) {
            //5.放入表头数据
            header.add(attr.get(i).getValue());
            //6.sku规格计算
            String detail = attr.get(i).getDetail();
            String[] split = detail.split(",");
            List<String> skuList = new ArrayList<>();
            for (int j = 0; j < split.length; j++) {
                skuList.add(split[j]);
            }
            skuInputList.add(skuList);
        }
        //8.sku规格种类
        List<List<String>> skuList = SkuUtil.skuSort(skuInputList); //计算规格种类
        for (int i = 0; i < skuList.size(); i++) {
            Map<String, Object> sku = new HashMap<>();
            List<String> strings = skuList.get(i);
            String[] arr = strings.toArray(new String[strings.size()]);
            String json = "";
            for (int j = 0; j < attrSize; j++) {
                String arrString = arr[j];
                sku.put("value" + j, arrString);
                json += arrString + ",";
            }
            json = json.substring(0, json.length() - 1);
            sku.put("json", json);
            sku.put("detailJson", json);
            sku.put("skuImg", coverImg);
            sku.put("skuPrice", price);
            //    sku.put("stock", 999);
            //    sku.put("sales", 0);
            value.add(sku);
        }
        //表头
        header.add(dataSourceMessageSource.getConfigI18ForCach("60165"));
        header.add(dataSourceMessageSource.getConfigI18ForCach("60166"));
        //  header.add("库存");
        //   header.add("销量");
        header.add(dataSourceMessageSource.getConfigI18ForCach("60167"));
        //数据放入返回map
        map.put("header", header);
        map.put("value", value);
        return Result.success().put("data", map);
    }

    @Transactional
    @Override
    public Result onlyFormatAttr(String coverImg,  String price) {
        Map<String, Object> map = new HashMap<>();
        List<String> header = new ArrayList<>(); //表头
        //表头
        header.add(dataSourceMessageSource.getConfigI18ForCach("60165"));
        header.add(dataSourceMessageSource.getConfigI18ForCach("60166"));
        //  header.add("库存");
        //  header.add("销量");
        List<Map<String, Object>> value = new ArrayList<>(); //规格排列组合
        Map<String, Object> sku = new HashMap<>();
        sku.put("json", null);
        sku.put("detailJson", null);
        sku.put("skuImg", coverImg);
        sku.put("skuPrice", price);
        //   sku.put("'%Y-%m-%d'", 999);
        //   sku.put("sales", 0);
        value.add(sku);
        //数据放入返回map
        map.put("header", header);
        map.put("value", value);
        return Result.success().put("data", map);
    }

    @Transactional
    @Override
    public Result formatAttr(Long goodsId, String languageType) {
        //1.获取商品规格值
        List<GoodsAttrValue> attr = goodsAttrValueService.findAllByGoodsId(goodsId, languageType);
        //sku集合
        List<GoodsSku> goodsSkuList = goodsSkuService.selectGoodsSkuByGoodsId(goodsId, languageType);
        int attrSize = attr.size();
        //2.准备返回值
        Map<String, Object> map = new HashMap<>();
        List<String> header = new ArrayList<>(); //表头
        List<Map<String, Object>> value = new ArrayList<>(); //规格排列组合
        if (attrSize > 0) {
            //4.循环规格值
            for (int i = 0; i < attrSize; i++) {
                //5.放入表头数据
                header.add(attr.get(i).getValue());
            }
        }
        header.add(dataSourceMessageSource.getConfigI18ForCach("60165"));
        header.add(dataSourceMessageSource.getConfigI18ForCach("60166"));
        //   header.add("库存");
        //   header.add("销量");
        header.add(dataSourceMessageSource.getConfigI18ForCach("60167"));
        //5.sku数据放入
        for (GoodsSku s : goodsSkuList) {
            Map<String, Object> sku = new HashMap<>();
            String detailJson = s.getDetailJson();
            if (detailJson != null) {
                String[] split = detailJson.split(",");
                for (int i = 0; i < split.length; i++) {
                    sku.put("value" + i, split[i]);
                }
            }
            sku.put("detailJson", detailJson);
            sku.put("skuOriginalPrice", s.getSkuOriginalPrice());
            sku.put("skuPrice", s.getSkuPrice());
            //  sku.put("stock", s.getStock());
            //   sku.put("sales", s.getSales());
            sku.put("skuImg", s.getSkuImg());
            value.add(sku);
        }
        //数据放入返回map
        map.put("header", header);
        map.put("value", value);
        return Result.success().put("data", map);
    }

    @Override
    public Result findAttrValue(Long goodsId, String languageType) {
        List<GoodsAttr> attrList = goodsAttrService.findAllByGoodsId(goodsId);
        GoodsAttr s = new GoodsAttr();
        if (attrList.size() > 0 && attrList != null) {
            s = attrList.get(0);
            s.setAttrValue(goodsAttrValueService.findAllByGoodsId(goodsId, languageType));
        }
        return Result.success().put("data", s);
    }

    @Override
    public Result selectShortShop(double lng, double lat, String shopName, Integer page, Integer limit, Long shopId) {
        Page<GoodsShop> pages = new Page<>(page, limit);
        IPage<GoodsShop> goodsShopIPage = goodsDao.selectShortShop(pages, lng, lat, shopName, shopId);
        CommonInfo one = commonInfoService.findOne(235);
        for (int i = 0; i < goodsShopIPage.getRecords().size(); i++) {
            String url = "https://apis.map.qq.com/ws/geocoder/v1/";
            Map<String, String> maps = new HashMap<>();
            maps.put("location", goodsShopIPage.getRecords().get(i).getShopLat() + "," + goodsShopIPage.getRecords().get(i).getShopLng());
            maps.put("key", one.getValue());
            String data = HttpClientUtil.doGet(url, maps);
            JSONObject jsonObject = JSON.parseObject(data);
            String status = jsonObject.getString("status");
            if ("0".equals(status)) {
                JSONObject result = jsonObject.getJSONObject("result");
                JSONObject adInfo = result.getJSONObject("ad_info");
                String province = adInfo.get("province").toString();
                String city = adInfo.get("city").toString();
                String district = adInfo.get("district").toString();
                goodsShopIPage.getRecords().get(i).setProvince(province);
                goodsShopIPage.getRecords().get(i).setCity(city);
                goodsShopIPage.getRecords().get(i).setDistrict(district);
            } else {
                log.error("转换失败！！！原因:" + jsonObject.getString("message"));
            }
        }
        PageUtils pageUtils = new PageUtils(goodsShopIPage);
        return Result.success().put("data", pageUtils);
    }

    @Override
    public Result selectAllShop(Integer page, Integer limit, String shopName, Long userId) {
        List<SysUserRoleEntity> sysUserRoleEntityList = sysUserRoleDao.selectList(new QueryWrapper<SysUserRoleEntity>().eq("user_id", userId));
        List list = new ArrayList();
        for (int a = 0; a < sysUserRoleEntityList.size(); a++) {
            list.add(sysUserRoleEntityList.get(a).getRoleId());
        }
        if (list.size() == 1 && list.contains(12L)) {
            Page<GoodsShop> pages = new Page<>(page, limit);
            PageUtils pageUtils = new PageUtils(goodsShopDao.selectAllShopByAdmin(pages, shopName, userId));
            return Result.success().put("data", pageUtils);
        } else {
            Page<GoodsShop> pages = new Page<>(page, limit);
            PageUtils pageUtils = new PageUtils(goodsShopDao.selectAllShop(pages, shopName));
            return Result.success().put("data", pageUtils);
        }
    }

    @Override
    public Result selectAdminGoodsList(Integer page, Integer limit, Integer classifyId, String goodsName, Integer status, String languageType) {
        Page<Goods> pages = new Page<>(page, limit);
        PageUtils pageUtils = new PageUtils(goodsShopDao.selectAdminGoodsList(pages, classifyId, goodsName, status, languageType));
        return Result.success().put("data", pageUtils);
    }

    @Override
    public Result selectAllClassify(Long shopId, String languageType) {
        List<GoodsClassify> goodsClassifies = goodsDao.selectAllClassify(shopId, languageType);
        for (GoodsClassify goodsClassify : goodsClassifies) {
            LanguageType languageTypes = languageTypeService.getOne(new LambdaQueryWrapper<LanguageType>().eq(LanguageType::getLanguage, goodsClassify.getLanguageType()));
            if (Objects.isNull(languageTypes)) {
                goodsClassify.setLanguageName(languageTypes.getRemark());
            }
        }


        return Result.success().put("data", goodsClassifies);
    }

    @Transactional
    @Override
    public Result shopAddGoods(Long shopId, List goodsIdList) {
        goodsDao.shopAddGoods(shopId, goodsIdList);
        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result deleteGoodsById(Long goodsId) {
        // 删除商品规格值
        LambdaQueryWrapper<GoodsAttrValue> attrValueLambdaQueryWrapper = new LambdaQueryWrapper<GoodsAttrValue>()
                .eq(GoodsAttrValue::getGoodsId, goodsId);

        List<GoodsAttrValue> attrValues = goodsAttrValueService.list(attrValueLambdaQueryWrapper);
        List<Long> attrValueIds = attrValues.stream().map(GoodsAttrValue::getId).collect(Collectors.toList());
        goodsAttrValueService.remove(attrValueLambdaQueryWrapper);

        iGoodsAttrValueI18nService.remove(new LambdaQueryWrapper<GoodsAttrValueI18n>().in(GoodsAttrValueI18n::getAttrValueId, attrValueIds));

        // 删除商品规格
        LambdaQueryWrapper<GoodsAttr> queryWrapper = new LambdaQueryWrapper<GoodsAttr>()
                .eq(GoodsAttr::getGoodsId, goodsId);

        List<GoodsAttr> goodsAttrs = goodsAttrDao.selectList(queryWrapper);
        List<Long> goodsAttrIds = goodsAttrs.stream().map(GoodsAttr::getId).collect(Collectors.toList());

        goodsAttrDao.delete(queryWrapper);
        iGoodsAttrI18nService.remove(new LambdaQueryWrapper<GoodsAttrI18n>().in(GoodsAttrI18n::getAttrId, goodsAttrIds));

        // 删除商品国际化
        iGoodsI18nService.remove(new LambdaQueryWrapper<GoodsI18n>().eq(GoodsI18n::getGoodsId, goodsId));

        // 删除商品
        goodsDao.delete(new QueryWrapper<Goods>().eq("goods_id", goodsId));
        return Result.success();
    }

    @Override
    public Result selectAllShopList(Long userId) {
        List<SysUserRoleEntity> sysUserRoleEntityList = sysUserRoleDao.selectList(new QueryWrapper<SysUserRoleEntity>().eq("user_id", userId));
        List list = new ArrayList();
        for (int a = 0; a < sysUserRoleEntityList.size(); a++) {
            list.add(sysUserRoleEntityList.get(a).getRoleId());
        }
        if (list.size() == 1 && list.contains(12L)) {
            List<GoodsShop> goodsShops = goodsShopDao.selectListByAdmin(userId);
            return Result.success().put("data", goodsShops);
        } else {
            List<GoodsShop> goodsShops = goodsShopDao.selectList(null);
            return Result.success().put("data", goodsShops);
        }
    }

    @Override
    public Result selectGoodsById(Long goodsId, Long shopId, String languageType) {
        Goods goods = goodsDao.getById(goodsId, languageType);
        if (goods != null) {
            GoodsClassify goodsClassify = goodsClassifyDao.getById(goods.getClassifyId(), languageType);
            goods.setClassifyName(goodsClassify.getClassifyName());
            goods.setAttr(goodsAttrService.findByGoodsId(goodsId, languageType));
            goods.setSku(goodsSkuService.selectGoodsSkuByGoodsId(goodsId, languageType));
            if (shopId!=null){
                goods.setInventory(goodsShopRelevancyDao.selectOne(new QueryWrapper<GoodsShopRelevancy>().eq("goods_id", goodsId).eq("shop_id", shopId)).getInventory());
            }
            return Result.success().put("data", goods);
        }
        return Result.success();
    }

    /*@Override
    public Result selectGoodsById(Long goodsId) {
        Goods goods = goodsDao.selectById(goodsId);
        List<GoodsSku> goodsSkuList = goodsSkuDao.selectList(new QueryWrapper<GoodsSku>().eq("goods_id", goodsId));
        List<GoodsSku> goodsSkus=new ArrayList<>();
        goodsSkus = setGoodsSkuList(goodsSkuList, goodsSkus, 0L);
        goods.setGoodsSkuList(goodsSkus);
        return Result.success().put("data", goods);
    }

    public List<GoodsSku> setGoodsSkuList(List<GoodsSku> goodsSkusList,List<GoodsSku> goodsSkus,Long parentId ){
        for(GoodsSku goodsSku:goodsSkusList){
            if(goodsSku.getParentId().equals(parentId)){
                goodsSkus.add(goodsSku);
                List<GoodsSku> goodsSkuList = goodsSku.getGoodsSkuList();
                if(goodsSkuList==null){
                    goodsSkuList=new ArrayList<>();
                }
                List<GoodsSku> goodsSkus1 = setGoodsSkuList(goodsSkusList, goodsSkuList, goodsSku.getSkuId());
                goodsSku.setGoodsSkuList(goodsSkus1);
            }
        }
        return goodsSkus;
    }*/


}
