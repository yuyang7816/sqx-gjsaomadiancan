package com.sqx.modules.datacentre.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.dao.UserDao;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.datacentre.dao.DataCentreDao;
import com.sqx.modules.datacentre.service.DataCentreService;
import com.sqx.modules.order.dao.AppOrderDao;
import com.sqx.modules.order.entity.TbOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@Service
public class DataCentreServiceImpl implements DataCentreService {

    @Autowired
    private DataCentreDao dataCentreDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private AppOrderDao appOrderDao;

    @Override
    public Result dataCentre() {
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        HashMap<String,Object> dataCenter = new HashMap();
        //总用户数
        int allCount = dataCentreDao.findAllCount();
        //今日新增用户数
        int dayAddUser = dataCentreDao.findDayAddUser(format);
        //本月新增用户数
        int monthAddUser = dataCentreDao.findMonthAddUser(format);
        //本年新增用户数
        int yearAddUser = dataCentreDao.findYearAddUser(format);
        //今日收入
        BigDecimal dayIncome = dataCentreDao.findDayIncome(format);
        //本月收入
        BigDecimal monthIncome = dataCentreDao.findMonthIncome(format);
        //本年收入
        BigDecimal yearIncome = dataCentreDao.findYearIncome(format);
        //今日接单数量
        int indentCount = dataCentreDao.findIndentCount(format);
        dataCenter.put("allCount", allCount);
        dataCenter.put("dayAddUser", dayAddUser);
        dataCenter.put("monthAddUser", monthAddUser);
        dataCenter.put("yearAddUser", yearAddUser);
        dataCenter.put("dayIncome", dayIncome);
        dataCenter.put("monthIncome", monthIncome);
        dataCenter.put("yearIncome", yearIncome);
        dataCenter.put("indentCount", indentCount);
        return Result.success().put("data", dataCenter);
    }

    @Override
    public Result dataCentreNew(Long shopId) {
        HashMap dataCenter = new HashMap();
        /*//到店订单数
        int takeCount = dataCentreDao.selectTakeCount(1, shopId);
        //外卖订单数
        int takeCount2 = dataCentreDao.selectTakeCount(1, shopId);
        //到店订单金额
        BigDecimal takeMoney = dataCentreDao.selectTakeMoney(1, shopId);
        //外卖订单金额
        BigDecimal takeMoney1 = dataCentreDao.selectTakeMoney(1, shopId);
        //退款订单数
        int cancelOrderCount = dataCentreDao.cancelOrderCount(shopId);
        //退款金额
        BigDecimal cancelOrderMoney = dataCentreDao.cancelOrderMoney(shopId);
        dataCenter.put("takeCount", takeCount);
        dataCenter.put("takeCount2", takeCount2);
        dataCenter.put("takeMoney", takeMoney);
        dataCenter.put("takeMoney1", takeMoney1);
        dataCenter.put("cancelOrderCount", cancelOrderCount);
        dataCenter.put("cancelOrderMoney", cancelOrderMoney);*/
        return Result.success().put("data", dataCenter);
    }

    @Override
    public Result selectShopMoney(Long shopId, String languageType) {
        HashMap<String, Object> hashMap = new HashMap<>();
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        BigDecimal dayMoney = dataCentreDao.shopMoney(shopId, date, "day");
        BigDecimal monthMoney = dataCentreDao.shopMoney(shopId, date, "month");
        BigDecimal yearMoney = dataCentreDao.shopMoney(shopId, date, "year");

        BigDecimal cashierDayMoney = dataCentreDao.moneyDayMoney(shopId, date, "day", languageType);
        BigDecimal cashierMonthMoney = dataCentreDao.moneyDayMoney(shopId, date, "month", languageType);
        BigDecimal cashierYearMoney = dataCentreDao.moneyDayMoney(shopId, date, "year", languageType);


        hashMap.put("dayMoney", dayMoney);
        hashMap.put("monthMoney", monthMoney);
        hashMap.put("yearMoney", yearMoney);

        hashMap.put("cashierDayMoney", cashierDayMoney);
        hashMap.put("cashierMonthMoney", cashierMonthMoney);
        hashMap.put("cashierYearMoney", cashierYearMoney);


        return Result.success().put("data", hashMap);
    }


    @Override
    public Result selectPayOrderAnalyze(Integer page, Integer limit, String date, String dateType) {
        Page<TbOrder> pages=new Page<>(page,limit);
        PageUtils pageUtils = new PageUtils(dataCentreDao.selectPayOrderAnalyze(pages, date, dateType));
        return Result.success().put("data", pageUtils);
    }

    @Override
    public Result selectAllUser(Integer page, Integer limit, String userName, String phone) {
        Page<UserEntity> pages=new Page<>(page,limit);
        PageUtils pageUtils = new PageUtils(userDao.selectUser(pages, userName, phone));
        return Result.success().put("data", pageUtils);
    }

    @Override
    public Result selectUserById(Long userId) {
        UserEntity userEntity = userDao.selectUserById(userId);
        //本月发单数量， 本月充值金额，
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        int orderMonth = dataCentreDao.sumOrderMonth(userId, date);
        BigDecimal topUpMonth = dataCentreDao.sumTopUpMonth(userId, date);
        userEntity.setOrderMonth(orderMonth);
        userEntity.setTopUpMonth(topUpMonth);
        return Result.success().put("data", userEntity);
    }

    @Override
    public Result selectOrderData(String date, String dateType) {
        HashMap hashMap = new HashMap();
        BigDecimal sumMoney = appOrderDao.selectSumMoney(date, dateType);
        int countOrder = appOrderDao.selectCountOrder(date, dateType);
        //到店订单数
        int takeCount = dataCentreDao.selectTakeCount(1, null, date, dateType);
        //外卖订单数
        int takeCount2 = dataCentreDao.selectTakeCount(2, null, date, dateType);
        //到店订单金额
        BigDecimal takeMoney = dataCentreDao.selectTakeMoney(1, null, date, dateType);
        //外卖订单金额
        BigDecimal takeMoney1 = dataCentreDao.selectTakeMoney(2, null, date, dateType);
        //退款订单数
        int cancelOrderCount = dataCentreDao.cancelOrderCount(null, date, dateType);
        //退款金额
        BigDecimal cancelOrderMoney = dataCentreDao.cancelOrderMoney(null, date, dateType);
        hashMap.put("sumMoney", sumMoney);
        hashMap.put("countOrder", countOrder);
        hashMap.put("takeCount", takeCount);
        hashMap.put("takeCount2", takeCount2);
        hashMap.put("takeMoney", takeMoney);
        hashMap.put("takeMoney1", takeMoney1);
        hashMap.put("cancelOrderCount", cancelOrderCount);
        hashMap.put("cancelOrderMoney", cancelOrderMoney);
        return Result.success().put("data", hashMap);
    }

    @Override
    public Result selectTopUpStatistics(String date, String dateType) {
        BigDecimal topUpStatistics = dataCentreDao.selectTopUpStatistics(date, dateType);

        return Result.success().put("data", topUpStatistics);
    }

    @Override
    public Result selectShopCenter(String date, String dateType, Integer page, Integer limit) {
        HashMap hashMap = new HashMap();
        //新增门店数量
        int newShopCount = dataCentreDao.selectNewShopCount(date, dateType);
        //门店收益排行榜
        Page<TbOrder> pages=new Page<>(page,limit);
        IPage<TbOrder> tbOrderIPage = dataCentreDao.selectRankingList(pages, date, dateType);
        for(int i = 0;i<limit;i++){
            Integer a = ((page-1)*limit)+i+1;
            if(tbOrderIPage.getRecords().size()>i){
                tbOrderIPage.getRecords().get(i).setRank(a);
            }
        }
        PageUtils pageUtils = new PageUtils(tbOrderIPage);
        hashMap.put("newShopCount", newShopCount);
        hashMap.put("pageUtils", pageUtils);
        return Result.success().put("data", hashMap);
    }

    @Override
    public Result selectUserCenter(String date, String dateType) {
        HashMap hashMap = new HashMap();
        int userCount = dataCentreDao.allUserCount(date, dateType);
        int phoneUserCount = dataCentreDao.phoneUserCount(date, dateType);
        hashMap.put("allUserCount", userCount);
        hashMap.put("phoneUserCount", phoneUserCount);
        return Result.success().put("data", hashMap);
    }



}
