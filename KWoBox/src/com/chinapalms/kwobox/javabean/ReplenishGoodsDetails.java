package com.chinapalms.kwobox.javabean;

public class ReplenishGoodsDetails {

    private int id;
    private String replenishmentId;// 补货单号
    private String barCodeId;// 商品条形码
    private String goodsName;// 补货商品名称
    private int goodsNumber;// 类别商品上货数量
    private int replenishmentState;// 补货状态：0表示上货，1表示下架

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReplenishmentId() {
        return replenishmentId;
    }

    public void setReplenishmentId(String replenishmentId) {
        this.replenishmentId = replenishmentId;
    }

    public String getBarCodeId() {
        return barCodeId;
    }

    public void setBarCodeId(String barCodeId) {
        this.barCodeId = barCodeId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(int goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    public int getReplenishmentState() {
        return replenishmentState;
    }

    public void setReplenishmentState(int replenishmentState) {
        this.replenishmentState = replenishmentState;
    }

    @Override
    public String toString() {
        return "ReplenishGoodsDetails [id=" + id + ", replenishmentId="
                + replenishmentId + ", barCodeId=" + barCodeId + ", goodsName="
                + goodsName + ", goodsNumber=" + goodsNumber
                + ", replenishmentState=" + replenishmentState + "]";
    }

}
