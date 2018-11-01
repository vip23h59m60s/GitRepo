package com.chinapalms.kwobox.dao;

import com.chinapalms.kwobox.javabean.InOutGoods;

public interface InOutGoodsDAO {

    public boolean addInOutGoods(InOutGoods inOutGoods);

    public InOutGoods findInOutGoodsByGoodsRFID(String goodsRFID);

    public boolean deleteInOutGoodsByGoodsRFID(String goodsRFID);

    public boolean updateInOutGoods(InOutGoods inOutGoods);

}
