package com.chinapalms.kwobox.javabean;

import java.math.BigDecimal;
import java.util.Date;

public class GoodsAverageWeight {

    private int id;
    private String boxId;// 售货机ID
    private String barCodeId;// 商品条形码ID
    private BigDecimal goodsWeight;// 商品重量
    private int goodsNumber;// 商品数量
    private BigDecimal averageWeight;// 商品平均重量
    private Date updateDateTime;// 重量采集时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    public String getBarCodeId() {
        return barCodeId;
    }

    public void setBarCodeId(String barCodeId) {
        this.barCodeId = barCodeId;
    }

    public BigDecimal getGoodsWeight() {
        return goodsWeight;
    }

    public void setGoodsWeight(BigDecimal goodsWeight) {
        this.goodsWeight = goodsWeight;
    }

    public int getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(int goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    public BigDecimal getAverageWeight() {
        return averageWeight;
    }

    public void setAverageWeight(BigDecimal averageWeight) {
        this.averageWeight = averageWeight;
    }

    public Date getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(Date updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    @Override
    public String toString() {
        return "GoodsWeight [id=" + id + ", boxId=" + boxId + ", barCodeId="
                + barCodeId + ", goodsWeight=" + goodsWeight + ", goodsNumber="
                + goodsNumber + ", averageWeight=" + averageWeight
                + ", updateDateTime=" + updateDateTime + "]";
    }

}
