package com.chinapalms.kwobox.javabean;

import java.util.Date;

public class CardgoroadGoodsCalibration {

    private String calibrationRecordId;// 货道商品校准记录ID
    private String replenishmentId;// 与当次理货对应的理货单ID
    private String boxId;// 售货机ID
    private int cardgoRoadNumber;// 货道号
    private String barCodeId;// 商品barCodeId
    private String goodsName;// 商品名称
    private int beforeCalibrationStockNumber;// 校准前货道商品库存
    private int calibrationType;// 校准类型：0:表示不增不减 1:表示减少 2：表示增加
    private int updateGoodsNumber;// 货道商品因校准发生变化数量（减少或增加）
    private int afterCalibrationStockNumber;// 校准后货道商品库存
    private int boxDeliveryId;// 进行货道商品校准的理货员ID
    private Date calibrationTime;// 校准时间

    public String getCalibrationRecordId() {
        return calibrationRecordId;
    }

    public void setCalibrationRecordId(String calibrationRecordId) {
        this.calibrationRecordId = calibrationRecordId;
    }

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

    public int getCardgoRoadNumber() {
        return cardgoRoadNumber;
    }

    public void setCardgoRoadNumber(int cardgoRoadNumber) {
        this.cardgoRoadNumber = cardgoRoadNumber;
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

    public int getBeforeCalibrationStockNumber() {
        return beforeCalibrationStockNumber;
    }

    public void setBeforeCalibrationStockNumber(int beforeCalibrationStockNumber) {
        this.beforeCalibrationStockNumber = beforeCalibrationStockNumber;
    }

    public int getCalibrationType() {
        return calibrationType;
    }

    public void setCalibrationType(int calibrationType) {
        this.calibrationType = calibrationType;
    }

    public int getUpdateGoodsNumber() {
        return updateGoodsNumber;
    }

    public void setUpdateGoodsNumber(int updateGoodsNumber) {
        this.updateGoodsNumber = updateGoodsNumber;
    }

    public int getAfterCalibrationStockNumber() {
        return afterCalibrationStockNumber;
    }

    public void setAfterCalibrationStockNumber(int afterCalibrationStockNumber) {
        this.afterCalibrationStockNumber = afterCalibrationStockNumber;
    }

    public int getBoxDeliveryId() {
        return boxDeliveryId;
    }

    public void setBoxDeliveryId(int boxDeliveryId) {
        this.boxDeliveryId = boxDeliveryId;
    }

    public Date getCalibrationTime() {
        return calibrationTime;
    }

    public void setCalibrationTime(Date calibrationTime) {
        this.calibrationTime = calibrationTime;
    }

    @Override
    public String toString() {
        return "CardgoroadGoodsCalibration [calibrationRecordId="
                + calibrationRecordId + ", replenishmentId=" + replenishmentId
                + ", boxId=" + boxId + ", cardgoRoadNumber=" + cardgoRoadNumber
                + ", barCodeId=" + barCodeId + ", goodsName=" + goodsName
                + ", beforeCalibrationStockNumber="
                + beforeCalibrationStockNumber + ", calibrationType="
                + calibrationType + ", updateGoodsNumber=" + updateGoodsNumber
                + ", afterCalibrationStockNumber="
                + afterCalibrationStockNumber + ", boxDeliveryId="
                + boxDeliveryId + ", calibrationTime=" + calibrationTime + "]";
    }

}
