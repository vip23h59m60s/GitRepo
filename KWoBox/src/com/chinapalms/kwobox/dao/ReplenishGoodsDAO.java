package com.chinapalms.kwobox.dao;

import java.util.List;

import com.chinapalms.kwobox.javabean.ReplenishGoods;

public interface ReplenishGoodsDAO {

    public boolean addReplenishGoods(ReplenishGoods replenishGoods);

    public List<ReplenishGoods> findReplenishGoodsByBoxDeliveryId(
            int boxDeliveryId);

    public List<ReplenishGoods> findReplenishGoodsByBoxDeliveryIdAndPageNumber(
            int boxDeliveryId, int pageNumber);

    public List<ReplenishGoods> findReplenishGoodsByBoxDeliveryIdAndBoxIdAndPageNumber(
            int boxDeliveryId, String boxId, int pageNumber);

    public int findReplenishGoodsCountByBoxDeliveryId(int boxDeliveryId);

    public List<ReplenishGoods> findReplenishGoodsByBoxId(String boxId);

}
