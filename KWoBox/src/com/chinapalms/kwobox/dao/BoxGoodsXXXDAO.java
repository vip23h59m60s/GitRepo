package com.chinapalms.kwobox.dao;

import java.util.List;

import com.chinapalms.kwobox.javabean.BoxGoodsXXX;

public interface BoxGoodsXXXDAO {

    // 针对RFID和称重方案柜子
    public boolean addBoxGoodsXXX(String boxId, int boxType, BoxGoodsXXX boxXXX);

    // 针对RFID方案柜子
    public BoxGoodsXXX findBoxGoodsXXXByGoodsDistinguishId(String boxId,
            String goodsDistinguishId);

    // 针对RFID方案柜子
    public boolean deleteBoxGoodsXXXByGoodsDistinguishId(String boxId,
            String goodsDistinguishId);

    // 针对称重方案和RFID方案柜子
    public List<BoxGoodsXXX> findAllBoxGoodsXXX(String boxId);

    // 针对称重方案售货柜
    public BoxGoodsXXX findBoxGoodsXXXByBarCodeIdAndCardgoRoadId(String boxId,
            String barCodeId, int CardgoRoadId);

    // 针对称重方案柜子
    public boolean updateBoxGoodsByBarCodeIdAndCardgoRoadIdAndNumberOfChange(
            String boxId, String barCodeId, int cardgoRoadId, int changeNumber);

    // 针对称重方案柜子
    public boolean updateBoxGoodsByBarCodeIdAndCardgoRoadIdAndStockNumber(
            String boxId, String barCodeId, int cardgoRoadId, int goodNumber);

    // 针对RFID方案柜子
    public boolean updateBoxGoods(String boxId, BoxGoodsXXX boxGoodsXXX);

    // 针对称重方案柜子
    public boolean updateBoxGoodsXXXByCardgoRoadId(String boxId,
            BoxGoodsXXX boxGoodsXXX);

    // 针对称重方案柜子
    public boolean updateBoxGoodsXXXPriceByCardgoRoadId(String boxId,
            BoxGoodsXXX boxGoodsXXX);

    // 针对称重方案和RFID方案柜子
    public boolean emptyBoxGoodsByBoxId(String boxId, int boxType);

    // 针对称重方案柜子
    public List<BoxGoodsXXX> findBoxGoodsByBarCodeId(String boxId,
            String barCodeId);

    // 针对RFID和称重柜子
    public boolean deleteAllBoxGoodsXXX(String boxId);

}
