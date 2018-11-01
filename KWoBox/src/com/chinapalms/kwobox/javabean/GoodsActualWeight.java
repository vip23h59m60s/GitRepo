package com.chinapalms.kwobox.javabean;

import java.math.BigDecimal;
import java.util.Date;

public class GoodsActualWeight {

    private int id;
    private String boxId;// 售货机ID
    private String barCodeId;// 商品条形码ID;
    private BigDecimal goodsWeight;// 商品重量
    private Date updateDateTime;// 更新记录时间

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

    public Date getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(Date updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    @Override
    public String toString() {
        return "GoodsActualWeight [id=" + id + ", boxId=" + boxId
                + ", barCodeId=" + barCodeId + ", goodsWeight=" + goodsWeight
                + ", updateDateTime=" + updateDateTime + "]";
    }

}
