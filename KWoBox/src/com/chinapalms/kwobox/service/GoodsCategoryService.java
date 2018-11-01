package com.chinapalms.kwobox.service;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.chinapalms.kwobox.dao.impl.GoodsCategoryDAOImpl;
import com.chinapalms.kwobox.javabean.GoodsCategory;
import com.chinapalms.kwobox.utils.ResponseStatus;

public class GoodsCategoryService extends GoodsCategoryDAOImpl {

    @Override
    public GoodsCategory findGoodsCategoryByBarCodeId(String barCodeId) {
        return super.findGoodsCategoryByBarCodeId(barCodeId);
    }

    @Override
    public List<GoodsCategory> findAllGoodsCategory() {
        return super.findAllGoodsCategory();
    }

    public String doGetAllGoodsCategoryJSONString() {
        List<GoodsCategory> categoryList = findAllGoodsCategory();
        JSONObject goodsCategoriesJsonObject = new JSONObject();
        JSONArray goodsCategoriesJsonArray = new JSONArray();
        if (categoryList.size() > 0) {
            for (int i = 0; i < categoryList.size(); i++) {
                GoodsCategory goodsCategory = categoryList.get(i);
                JSONObject goodsCategoryBeanJsonObject = JSONObject
                        .fromObject(goodsCategory);
                goodsCategoriesJsonArray.add(goodsCategoryBeanJsonObject);
            }
            goodsCategoriesJsonObject.put("GoodsCategories",
                    goodsCategoriesJsonArray);
        } else {
            goodsCategoriesJsonObject.put("GoodsCategories",
                    ResponseStatus.FAIL);
        }
        return goodsCategoriesJsonObject.toString();
    }

}
