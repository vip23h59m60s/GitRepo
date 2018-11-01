package com.chinapalms.kwobox.javabean;

import java.util.Date;

public class ReplenishGoods {

    final public static int PAGE_SIZE = 5;

    private String replenishmentId;// 补货单号
    private String boxId;// 售货柜ID
    private int boxDeliveryId;// 补货员ID
    private int replenishmentGoodsNumber;// 补货总数量
    private int takeOffGoodsNumber;// 下架总数量
    private int replenishType;// 理货单类型：0表示无上下架理货单；1表示有上下架理货单
    private Date replenishmentTime;// 补货时间

    public String getReplenishmentId() {
        return replenishmentId;
    }

    public void setReplenishmentId(String replenishmentId) {
        this.replenishmentId = replenishmentId;
    }

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    public int getBoxDeliveryId() {
        return boxDeliveryId;
    }

    public void setBoxDeliveryId(int boxDeliveryId) {
        this.boxDeliveryId = boxDeliveryId;
    }

    public int getReplenishmentGoodsNumber() {
        return replenishmentGoodsNumber;
    }

    public void setReplenishmentGoodsNumber(int replenishmentGoodsNumber) {
        this.replenishmentGoodsNumber = replenishmentGoodsNumber;
    }

    public int getTakeOffGoodsNumber() {
        return takeOffGoodsNumber;
    }

    public void setTakeOffGoodsNumber(int takeOffGoodsNumber) {
        this.takeOffGoodsNumber = takeOffGoodsNumber;
    }

    public int getReplenishType() {
        return replenishType;
    }

    public void setReplenishType(int replenishType) {
        this.replenishType = replenishType;
    }

    public Date getReplenishmentTime() {
        return replenishmentTime;
    }

    public void setReplenishmentTime(Date replenishmentTime) {
        this.replenishmentTime = replenishmentTime;
    }

    @Override
    public String toString() {
        return "ReplenishGoods [replenishmentId=" + replenishmentId
                + ", boxId=" + boxId + ", boxDeliveryId=" + boxDeliveryId
                + ", replenishmentGoodsNumber=" + replenishmentGoodsNumber
                + ", takeOffGoodsNumber=" + takeOffGoodsNumber
                + ", replenishType=" + replenishType + ", replenishmentTime="
                + replenishmentTime + "]";
    }

}
