package com.chinapalms.kwobox.service;

import java.math.BigDecimal;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.chinapalms.kwobox.dao.impl.OrderDetailsThirdPartDAOImpl;
import com.chinapalms.kwobox.javabean.OrderDetails;
import com.chinapalms.kwobox.javabean.OrderDetailsThirdPart;

public class OrderDetailsThirdPartService extends OrderDetailsThirdPartDAOImpl {

    @Override
    public boolean addOrderDetailsThirdPart(OrderDetailsThirdPart orderDetails) {
        return super.addOrderDetailsThirdPart(orderDetails);
    }

    @Override
    public List<OrderDetailsThirdPart> findOrderDetailsThirdPartByOrderId(
            String orderId) {
        return super.findOrderDetailsThirdPartByOrderId(orderId);
    }

    public boolean addOrderDetailssThirdPart(JSONObject orderJsonObject) {
        JSONObject orderJsonObjectFromJsonObject = orderJsonObject
                .getJSONObject("Order");
        String orderId = orderJsonObjectFromJsonObject.getString("orderId");
        JSONArray goodsJsonArray = orderJsonObjectFromJsonObject
                .getJSONArray("Goods");
        for (int i = 0; i < goodsJsonArray.size(); i++) {
            JSONObject boxGoodsJsonObject = goodsJsonArray.getJSONObject(i);
            String barCodeId = boxGoodsJsonObject.getString("barCodeId");
            String goodsName = boxGoodsJsonObject
                    .getString("goodsCategoryName");
            int salesMode = Integer.valueOf(boxGoodsJsonObject
                    .getString("salesMode"));
            BigDecimal goodsPrice = BigDecimal.valueOf(Double
                    .valueOf(boxGoodsJsonObject.getString("goodsPrice")));
            BigDecimal goodsActualPrice = BigDecimal.valueOf(Double
                    .valueOf(boxGoodsJsonObject.getString("goodsActualPrice")));
            BigDecimal goodTotalFavorable = goodsPrice
                    .subtract(goodsActualPrice);
            int categoryGoodsNumber = Integer.valueOf(boxGoodsJsonObject
                    .getString("categoryGoodsNumber"));
            int categoryGoodsWeight = Integer.valueOf(boxGoodsJsonObject
                    .getString("categoryGoodsWeight"));
            BigDecimal categoryGoodsTotalPrice = BigDecimal.valueOf(Double
                    .valueOf(boxGoodsJsonObject
                            .getString("categoryGoodsTotalPrice")));
            BigDecimal categoryfavorablePrice = BigDecimal.valueOf(Double
                    .valueOf(boxGoodsJsonObject
                            .getString("categoryGoodsTotalDiscount")));
            BigDecimal actualCategoryPrice = BigDecimal.valueOf(Double
                    .valueOf(boxGoodsJsonObject
                            .getString("actualCategoryPrice")));
            OrderDetailsThirdPart orderDetails = new OrderDetailsThirdPart();
            orderDetails.setOrderId(orderId);
            orderDetails.setBarCodeId(barCodeId);
            orderDetails.setGoodsName(goodsName);
            orderDetails.setSalesMode(salesMode);
            orderDetails.setGoodsPrice(goodsPrice);
            orderDetails.setGoodsFavorablePrice(goodTotalFavorable);
            orderDetails.setGoodsActualPrice(goodsActualPrice);
            orderDetails.setCategoryGoodsNumber(categoryGoodsNumber);
            orderDetails.setCategoryGoodsWeight(categoryGoodsWeight);
            orderDetails.setCategoryGoodsPrice(categoryGoodsTotalPrice);
            orderDetails.setCategoryFavorablePrice(categoryfavorablePrice);
            orderDetails.setActualCategoryPrice(actualCategoryPrice);
            addOrderDetailsThirdPart(orderDetails);
        }
        return true;
    }

}
