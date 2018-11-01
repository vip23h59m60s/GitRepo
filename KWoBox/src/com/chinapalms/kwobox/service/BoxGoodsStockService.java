package com.chinapalms.kwobox.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.impl.BoxGoodsStockDAOImpl;
import com.chinapalms.kwobox.javabean.BoxGoodsStock;
import com.chinapalms.kwobox.javabean.BoxGoodsXXX;

public class BoxGoodsStockService extends BoxGoodsStockDAOImpl {

    Log log = LogFactory.getLog(BoxGoodsStockService.class);

    @Override
    public boolean addBoxGoodsStock(BoxGoodsStock boxGoodsStock) {
        return super.addBoxGoodsStock(boxGoodsStock);
    }

    public void saveCurrentBoxGoodsStock(String replenishId, String boxId) {
        try {
            BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
            List<BoxGoodsXXX> boxGoodsXXXList = boxGoodsXXXService
                    .findAllBoxGoodsXXX(boxId);
            for (int i = 0; i < boxGoodsXXXList.size(); i++) {
                BoxGoodsXXX boxGoodsXXX = boxGoodsXXXList.get(i);
                BoxGoodsStock boxGoodsStock = new BoxGoodsStock();
                boxGoodsStock.setReplenishmentId(replenishId);
                boxGoodsStock.setBoxId(boxId);
                boxGoodsStock.setCardgoRoadId(boxGoodsXXX.getCardgoRoadId());
                boxGoodsStock.setBarCodeId(boxGoodsXXX.getBarCodeId());
                boxGoodsStock.setStockNumber(boxGoodsXXX.getStockNumber());
                addBoxGoodsStock(boxGoodsStock);
            }
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

}
