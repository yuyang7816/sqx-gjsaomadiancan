package com.sqx.modules.shopSeat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.Result;
import com.sqx.modules.goods.service.GoodsShopService;
import com.sqx.modules.shopSeat.entity.ShopSeat;
import com.sqx.modules.shopSeat.dao.ShopSeatDao;
import com.sqx.modules.shopSeat.service.ShopSeatService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.modules.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author www.javacoder.top
 * @since 2023-02-06
 */
@Service
public class ShopSeatServiceImpl extends ServiceImpl<ShopSeatDao, ShopSeat> implements ShopSeatService {
    @Autowired
    private GoodsShopService goodsShopService;
    @Autowired
    private SysUserService sysUserService;

    @Override
    public Result saveSeat(ShopSeat shopSeat) {
        if (shopSeat.getSeatId() == null) {
            shopSeat.setCreateTime(LocalDateTime.now());
            return Result.upStatus(baseMapper.insert(shopSeat));
        } else {
            return Result.upStatus(baseMapper.updateById(shopSeat));
        }


    }

    @Override
    public IPage<ShopSeat> getShopSeatList(Integer page, Integer limit, ShopSeat shopSeat) {
        Page<ShopSeat> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        IPage<ShopSeat> selectPage = baseMapper.selectPage(pages, new QueryWrapper<>(shopSeat));

        List<ShopSeat> records = selectPage.getRecords();
        for (ShopSeat record : records) {
            record.setGoodsShop(goodsShopService.getById(record.getShopId()));
            record.setSysUserEntity(sysUserService.getById(record.getSysUserId()));
        }
        return selectPage;
    }

    @Override
    public int updateSeatStatus(Long seatId) {

        return baseMapper.updateSeatStatus(seatId);


    }


}
