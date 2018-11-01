package com.chinapalms.kwobox.javabean;

import java.math.BigDecimal;
import java.util.Date;

public class BoxGoodsXXX {

    public static final int PAGE_SIZE = 5;
    public static final int CATEGORY_GOODS_BASELINE = 2;
    public static final int TOTAL_GOODS_BASELINE = 20;

    public static final String BOX_SUGGEST_REPLENISHMENT_YES_DESCRIPTION = "库存不足，请及时理货";

    public static final String BOXDETAILS_SUGGEST_REPLENISHMENT_YES_DESCRIPTION = "请及时理货：零售店中某些商品已少于 "
            + CATEGORY_GOODS_BASELINE
            + " 件或商品总数已少于 "
            + TOTAL_GOODS_BASELINE
            + " 件";

    public static final String SUGGEST_REPLENISHMENT_NO_DESCRIPTION = "库存充足";

    public static final String NO_REPLENISHMENT_RECORD = "无理货记录";

    private int id;
    private String boxId;// 售货机ID
    private String goodsRFID;// 商品RFID
    private String barCodeId;// 商品条形码
    private String goodsName;// 商品名称（商品名称+规格，主要这容量和或者重量）
    private int cardgoRoadId;// 货道号
    private int weight;// 商品重量
    private int stockNumber;// 当前货道库存数量
    private String brandCompany;// 商品品牌商
    private int salesMode;// 商品销售方式，0：表示按件销售的标准商品，1：表示按重量销售的非标准商品
    private BigDecimal goodsPrice;// 商品价格
    private BigDecimal goodsDiscount;// 商品折扣
    private BigDecimal favourable;// 商品优惠减免
    private Date makeDate;// 商品生产日期
    private int expiryTime;// 商品保质期
    private String goodsPicture;// 商品六视图
    private int deliveryManagerId;// 商品上货员ID
    private Date deliveryTime;// 商品上货时间

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

    public String getGoodsRFID() {
        return goodsRFID;
    }

    public void setGoodsRFID(String goodsRFID) {
        this.goodsRFID = goodsRFID;
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

    public int getCardgoRoadId() {
        return cardgoRoadId;
    }

    public void setCardgoRoadId(int cardgoRoadId) {
        this.cardgoRoadId = cardgoRoadId;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getStockNumber() {
        return stockNumber;
    }

    public void setStockNumber(int stockNumber) {
        this.stockNumber = stockNumber;
    }

    public String getBrandCompany() {
        return brandCompany;
    }

    public void setBrandCompany(String brandCompany) {
        this.brandCompany = brandCompany;
    }

    public int getSalesMode() {
        return salesMode;
    }

    public void setSalesMode(int salesMode) {
        this.salesMode = salesMode;
    }

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public BigDecimal getGoodsDiscount() {
        return goodsDiscount;
    }

    public void setGoodsDiscount(BigDecimal goodsDiscount) {
        this.goodsDiscount = goodsDiscount;
    }

    public BigDecimal getFavourable() {
        return favourable;
    }

    public void setFavourable(BigDecimal favourable) {
        this.favourable = favourable;
    }

    public Date getMakeDate() {
        return makeDate;
    }

    public void setMakeDate(Date makeDate) {
        this.makeDate = makeDate;
    }

    public int getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(int expiryTime) {
        this.expiryTime = expiryTime;
    }

    public String getGoodsPicture() {
        return goodsPicture;
    }

    public void setGoodsPicture(String goodsPicture) {
        this.goodsPicture = goodsPicture;
    }

    public int getDeliveryManagerId() {
        return deliveryManagerId;
    }

    public void setDeliveryManagerId(int deliveryManagerId) {
        this.deliveryManagerId = deliveryManagerId;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    @Override
    public String toString() {
        return "BoxGoodsXXX [id=" + id + ", boxId=" + boxId + ", goodsRFID="
                + goodsRFID + ", barCodeId=" + barCodeId + ", goodsName="
                + goodsName + ", cardgoRoadId=" + cardgoRoadId + ", weight="
                + weight + ", stockNumber=" + stockNumber + ", brandCompany="
                + brandCompany + ", salesMode=" + salesMode + ", goodsPrice="
                + goodsPrice + ", goodsDiscount=" + goodsDiscount
                + ", favourable=" + favourable + ", makeDate=" + makeDate
                + ", expiryTime=" + expiryTime + ", goodsPicture="
                + goodsPicture + ", deliveryManagerId=" + deliveryManagerId
                + ", deliveryTime=" + deliveryTime + "]";
    }

}
