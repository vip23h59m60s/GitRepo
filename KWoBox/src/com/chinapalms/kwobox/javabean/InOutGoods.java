package com.chinapalms.kwobox.javabean;

import java.math.BigDecimal;
import java.util.Date;

public class InOutGoods {

    private String goodsRFID;// 商品RFID
    private String barCodeId;// 商品条形码ID
    private String goodsName;// 商品名称(包括商品名称和规格（主要指容量或者重量）)
    private BigDecimal goodsPrice;// 商品价格
    private BigDecimal goodsDiscount;// 商品折扣
    private BigDecimal favourable;// 商品优惠减免
    private String manufacturer;// 生产厂商
    private String goodsChannel;// 进货渠道
    private Date makeDate;// 商品生产日期
    private int expiryTime;// 商品保质期
    private String goodsPicture;// 商品六视图
    private int customerId;// 商户ID
    private int workerId;// 商户入库员工ID
    private Date inOutTime;// 商品入库时间

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

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getGoodsChannel() {
        return goodsChannel;
    }

    public void setGoodsChannel(String goodsChannel) {
        this.goodsChannel = goodsChannel;
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

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getWorkerId() {
        return workerId;
    }

    public void setWorkerId(int workerId) {
        this.workerId = workerId;
    }

    public Date getInOutTime() {
        return inOutTime;
    }

    public void setInOutTime(Date inOutTime) {
        this.inOutTime = inOutTime;
    }

    @Override
    public String toString() {
        return "InOutGoods [goodsRFID=" + goodsRFID + ", barCodeId="
                + barCodeId + ", goodsName=" + goodsName + ", goodsPrice="
                + goodsPrice + ", goodsDiscount=" + goodsDiscount
                + ", favourable=" + favourable + ", manufacturer="
                + manufacturer + ", goodsChannel=" + goodsChannel
                + ", makeDate=" + makeDate + ", expiryTime=" + expiryTime
                + ", goodsPicture=" + goodsPicture + ", customerId="
                + customerId + ", workerId=" + workerId + ", inOutTime="
                + inOutTime + "]";
    }

}
