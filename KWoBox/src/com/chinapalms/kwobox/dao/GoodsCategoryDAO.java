package com.chinapalms.kwobox.dao;

import java.util.List;

import com.chinapalms.kwobox.javabean.GoodsCategory;

public interface GoodsCategoryDAO {

    public GoodsCategory findGoodsCategoryByBarCodeId(String barCodeId);

    public List<GoodsCategory> findAllGoodsCategory();

}
