package com.chinapalms.kwobox.service;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.chinapalms.kwobox.dao.impl.ReplenishGoodsDetailsDAOImpl;
import com.chinapalms.kwobox.javabean.ReplenishGoodsDetails;

public class ReplenishGoodsDetailsService extends ReplenishGoodsDetailsDAOImpl {

    @Override
    public boolean addReplenishGoodsDetails(
            ReplenishGoodsDetails replenishGoodsDetails) {
        return super.addReplenishGoodsDetails(replenishGoodsDetails);
    }

    @Override
    public List<ReplenishGoodsDetails> findReplenishGoodsDetailsByReplenishId(
            String replenishId) {
        return super.findReplenishGoodsDetailsByReplenishId(replenishId);
    }

    public boolean addReplenishGoodsDetailss(JSONObject boxGoodsJsonObject,
            String updateGoodsFlag) {
        ReplenishGoodsDetails replenishGoodsDetails = new ReplenishGoodsDetails();
        JSONArray boxGoodsJsonArrayFromJsonObject = boxGoodsJsonObject
                .getJSONArray("BoxGoods");
        JSONObject updateGoodsJsonObject = boxGoodsJsonArrayFromJsonObject
                .getJSONObject(0).getJSONObject("UpdatedGoods");
        String replenishId = updateGoodsJsonObject.getString("updateId");
        replenishGoodsDetails.setReplenishmentId(replenishId);
        if (updateGoodsFlag.equals("added")) {
            // 仅上货，无下架
            JSONArray addGoodsJsonArray = updateGoodsJsonObject
                    .getJSONArray("AddedGoods");
            for (int i = 0; i < addGoodsJsonArray.size(); i++) {
                JSONObject addGoodsJsonObject = addGoodsJsonArray
                        .getJSONObject(i);
                String barCodeId = addGoodsJsonObject.getString("barCodeId");
                String goodsCategoryName = addGoodsJsonObject
                        .getString("goodsCategoryName");
                int categoryGoodsNumber = addGoodsJsonObject
                        .getInt("categoryGoodsNumber");
                replenishGoodsDetails.setBarCodeId(barCodeId);
                replenishGoodsDetails.setGoodsName(goodsCategoryName);
                replenishGoodsDetails.setGoodsNumber(categoryGoodsNumber);
                // 0表示上货
                replenishGoodsDetails.setReplenishmentState(0);
                addReplenishGoodsDetails(replenishGoodsDetails);
            }
        } else if (updateGoodsFlag.equals("removed")) {
            // 仅下架，无上货
            JSONArray removedGoodsJsonArray = updateGoodsJsonObject
                    .getJSONArray("RemovedGoods");
            for (int i = 0; i < removedGoodsJsonArray.size(); i++) {
                JSONObject removeGoodsJsonObject = removedGoodsJsonArray
                        .getJSONObject(i);
                String barCodeId = removeGoodsJsonObject.getString("barCodeId");
                String goodsCategoryName = removeGoodsJsonObject
                        .getString("goodsCategoryName");
                int categoryGoodsNumber = removeGoodsJsonObject
                        .getInt("categoryGoodsNumber");
                replenishGoodsDetails.setBarCodeId(barCodeId);
                replenishGoodsDetails.setGoodsName(goodsCategoryName);
                replenishGoodsDetails.setGoodsNumber(categoryGoodsNumber);
                // 1表示下架
                replenishGoodsDetails.setReplenishmentState(1);
                addReplenishGoodsDetails(replenishGoodsDetails);
            }
        } else if (updateGoodsFlag.equals("addedAndRemoved")) {
            // 既有上货也有下架
            // 上货部分逻辑
            JSONArray addGoodsJsonArray = updateGoodsJsonObject
                    .getJSONArray("AddedGoods");
            for (int i = 0; i < addGoodsJsonArray.size(); i++) {
                JSONObject addGoodsJsonObject = addGoodsJsonArray
                        .getJSONObject(i);
                String barCodeId = addGoodsJsonObject.getString("barCodeId");
                String goodsCategoryName = addGoodsJsonObject
                        .getString("goodsCategoryName");
                int categoryGoodsNumber = addGoodsJsonObject
                        .getInt("categoryGoodsNumber");
                replenishGoodsDetails.setBarCodeId(barCodeId);
                replenishGoodsDetails.setGoodsName(goodsCategoryName);
                replenishGoodsDetails.setGoodsNumber(categoryGoodsNumber);
                // 0表示上货
                replenishGoodsDetails.setReplenishmentState(0);
                addReplenishGoodsDetails(replenishGoodsDetails);
            }
            // 下架部分逻辑
            JSONArray removedGoodsJsonArray = updateGoodsJsonObject
                    .getJSONArray("RemovedGoods");
            for (int i = 0; i < removedGoodsJsonArray.size(); i++) {
                JSONObject removeGoodsJsonObject = removedGoodsJsonArray
                        .getJSONObject(i);
                String barCodeId = removeGoodsJsonObject.getString("barCodeId");
                String goodsCategoryName = removeGoodsJsonObject
                        .getString("goodsCategoryName");
                int categoryGoodsNumber = removeGoodsJsonObject
                        .getInt("categoryGoodsNumber");
                replenishGoodsDetails.setBarCodeId(barCodeId);
                replenishGoodsDetails.setGoodsName(goodsCategoryName);
                replenishGoodsDetails.setGoodsNumber(categoryGoodsNumber);
                // 1表示下架
                replenishGoodsDetails.setReplenishmentState(1);
                addReplenishGoodsDetails(replenishGoodsDetails);
            }
        }
        return true;
    }
}
