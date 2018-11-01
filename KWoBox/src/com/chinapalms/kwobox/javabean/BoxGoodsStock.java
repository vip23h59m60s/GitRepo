package com.chinapalms.kwobox.javabean;

public class BoxGoodsStock {

    private int id;
    private String replenishmentId;// 补货单号
    private String boxId;// 售货机ID
    private int cardgoRoadId;// 货道号
    private String barCodeId;// 商品barCodeId
    private int stockNumber;// 货道商品库存

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

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    public int getCardgoRoadId() {
        return cardgoRoadId;
    }

    public void setCardgoRoadId(int cardgoRoadId) {
        this.cardgoRoadId = cardgoRoadId;
    }

    public String getBarCodeId() {
        return barCodeId;
    }

    public void setBarCodeId(String barCodeId) {
        this.barCodeId = barCodeId;
    }

    public int getStockNumber() {
        return stockNumber;
    }

    public void setStockNumber(int stockNumber) {
        this.stockNumber = stockNumber;
    }

    @Override
    public String toString() {
        return "BoxGoodsStock [id=" + id + ", replenishmentId="
                + replenishmentId + ", boxId=" + boxId + ", cardgoRoadId="
                + cardgoRoadId + ", barCodeId=" + barCodeId + ", stockNumber="
                + stockNumber + "]";
    }

}
