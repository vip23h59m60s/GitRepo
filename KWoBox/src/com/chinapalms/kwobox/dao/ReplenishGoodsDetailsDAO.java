package com.chinapalms.kwobox.dao;

import java.util.List;

import com.chinapalms.kwobox.javabean.ReplenishGoodsDetails;

public interface ReplenishGoodsDetailsDAO {

    public boolean addReplenishGoodsDetails(
            ReplenishGoodsDetails replenishGoodsDetails);

    public List<ReplenishGoodsDetails> findReplenishGoodsDetailsByReplenishId(
            String replenishId);
}
