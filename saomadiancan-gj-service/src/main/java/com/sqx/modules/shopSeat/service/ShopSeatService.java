package com.sqx.modules.shopSeat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.common.utils.Result;
import com.sqx.modules.shopSeat.entity.ShopSeat;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author www.javacoder.top
 * @since 2023-02-06
 */
public interface ShopSeatService extends IService<ShopSeat> {

    Result saveSeat(ShopSeat shopSeat);

    IPage<ShopSeat> getShopSeatList(Integer page, Integer limit, ShopSeat shopSeat);

    /**
     * 将座位改为空闲中
     * @param seatId
     * @return
     */
    int updateSeatStatus(Long seatId);
}
