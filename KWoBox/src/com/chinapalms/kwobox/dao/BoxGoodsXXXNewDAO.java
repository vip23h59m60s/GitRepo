package com.chinapalms.kwobox.dao;

import java.util.List;

import com.chinapalms.kwobox.javabean.BoxGoodsXXX;
import com.chinapalms.kwobox.javabean.BoxGoodsXXXNew;

public interface BoxGoodsXXXNewDAO {

    // 针对称重方案
    public List<BoxGoodsXXXNew> findAllBoxGoodsXXXNew(String boxId);

    // 删除柜子所有商品
    public boolean deleteAllBoxGoodsXXXNew(String boxId);

    public boolean addBoxGoodsXXX(String boxId, int boxType,
            BoxGoodsXXXNew boxGoodsXXXNew);

}
