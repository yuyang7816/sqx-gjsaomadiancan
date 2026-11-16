package com.sqx.modules.datacentre.service;

import com.sqx.common.utils.Result;

public interface DataCentreService {

    Result dataCentre();

    Result selectPayOrderAnalyze(Integer page, Integer limit, String date, String dateType);

    Result selectAllUser(Integer page, Integer limit, String userName, String phone);

    Result selectUserById(Long userId);

    Result selectOrderData(String date, String dateType);

    Result selectTopUpStatistics(String date, String dateType);

    Result selectShopCenter(String date, String dateType, Integer page, Integer limit);

    Result selectUserCenter(String date, String dateType);

    Result dataCentreNew(Long shopId);

    Result selectShopMoney(Long shopId, String languageType);

}
