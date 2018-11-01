package com.chinapalms.kwobox.javabean;

import java.math.BigDecimal;

public class WeightBoxGoodsUpdate {

    // 称重售货柜商品变更的信息报过售卖和上货
    private String barCodeId;// 商品条形码ID
    private int cardgoRoadId;// 称重售货柜货到号
    private BigDecimal cardgoRoadGoodsTotalWeight;// 货道商品总重量(当为上货时，表示当前货道商品的总重量，当为正常售卖时为当前货道卖出商品总重量（目前只关注每个货道只售出单个商品的情况录入统计数据库）)
    private int goodsNumber;// 每个货道上商品更新数量

    public String getBarCodeId() {
        return barCodeId;
    }

    public void setBarCodeId(String barCodeId) {
        this.barCodeId = barCodeId;
    }

    public int getCardgoRoadId() {
        return cardgoRoadId;
    }

    public void setCardgoRoadId(int cardgoRoadId) {
        this.cardgoRoadId = cardgoRoadId;
    }

    public BigDecimal getCardgoRoadGoodsTotalWeight() {
        return cardgoRoadGoodsTotalWeight;
    }

    public void setCardgoRoadGoodsTotalWeight(
            BigDecimal cardgoRoadGoodsTotalWeight) {
        this.cardgoRoadGoodsTotalWeight = cardgoRoadGoodsTotalWeight;
    }

    public int getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(int goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    @Override
    public String toString() {
        return "WeightBoxGoodsUpdate [barCodeId=" + barCodeId
                + ", cardgoRoadId=" + cardgoRoadId
                + ", cardgoRoadGoodsTotalWeight=" + cardgoRoadGoodsTotalWeight
                + ", goodsNumber=" + goodsNumber + "]";
    }

}
