package com.chinapalms.kwobox.service;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.chinapalms.kwobox.dao.impl.BoxGoodsXXXNewDAOImpl;
import com.chinapalms.kwobox.javabean.BoxBody;
import com.chinapalms.kwobox.javabean.BoxGoodsXXX;
import com.chinapalms.kwobox.javabean.BoxGoodsXXXNew;
import com.chinapalms.kwobox.javabean.Boxes;

public class BoxGoodsXXXNewService extends BoxGoodsXXXNewDAOImpl {

    @Override
    public List<BoxGoodsXXXNew> findAllBoxGoodsXXXNew(String boxId) {
        return super.findAllBoxGoodsXXXNew(boxId);
    }

    @Override
    public boolean deleteAllBoxGoodsXXXNew(String boxId) {
        return super.deleteAllBoxGoodsXXXNew(boxId);
    }

    @Override
    public boolean addBoxGoodsXXX(String boxId, int boxType,
            BoxGoodsXXXNew boxGoodsXXXNew) {
        return super.addBoxGoodsXXX(boxId, boxType, boxGoodsXXXNew);
    }

    public String doGetNewBoxInfo(String boxId) {
        JSONObject boxGoodssJsonObject = new JSONObject();
        JSONArray boxGoodssJsonArray = new JSONArray();

        BoxesService boxesService = new BoxesService();
        BoxBodyService boxBodyService = new BoxBodyService();
        Boxes boxes = boxesService.findBoxesByBoxId(boxId);
        // 获取当前柜子货道总数量
        if (boxes != null) {
            BoxBody boxBody = boxBodyService.findBoxBodyByBoxBodyId(boxes
                    .getBoxBodyId());
            int cardgoRoadNumber = boxBody.getCardgoRoadNumber();
            boxGoodssJsonObject.put("boxCardgoRoadNumber", cardgoRoadNumber);
        }

        List<BoxGoodsXXXNew> boxGoodsXXXNewList = findAllBoxGoodsXXXNew(boxId);
        if (boxGoodsXXXNewList.size() > 0) {
            for (int i = 0; i < boxGoodsXXXNewList.size(); i++) {
                BoxGoodsXXXNew boxGoodsXXXNew = boxGoodsXXXNewList.get(i);
                JSONObject boxGoodsJsonObject = JSONObject
                        .fromObject(boxGoodsXXXNew);
                boxGoodssJsonArray.add(boxGoodsJsonObject);
            }
            boxGoodssJsonObject.put("BoxGoodsInfo", boxGoodssJsonArray);
        } else {
            boxGoodssJsonObject.put("BoxGoodsInfo", "noBoxGoods");
        }
        return boxGoodssJsonObject.toString();
    }

}
