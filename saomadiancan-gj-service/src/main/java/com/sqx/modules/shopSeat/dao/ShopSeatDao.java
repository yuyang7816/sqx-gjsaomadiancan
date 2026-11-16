package com.sqx.modules.shopSeat.dao;

import com.sqx.modules.shopSeat.entity.ShopSeat;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author www.javacoder.top
 * @since 2023-02-06
 */
@Mapper
public interface ShopSeatDao extends BaseMapper<ShopSeat> {

    int updateSeatStatus(@Param("seatId") Long seatId);
}
