package com.chinapalms.kwobox.javabean;

import java.math.BigDecimal;

public class OrderDetailsModify {

    private int id;
    private String orderId;// 订单号
    private String barCodeId;// 商品条形码
    private String goodsName;// 商品名称（商品名称+规格+...）
    private int salesMode;// 商品销售方式，0：表示按件销售的标准商品，1：表示按重量销售的非标准商品
    private BigDecimal goodsPrice;// 商品单价
    private BigDecimal goodsFavorablePrice;// 单个商品减免（折扣+优惠减免）
    private BigDecimal goodsActualPrice;// 单个商品实际价格
    private int categoryGoodsNumber;// 同类商品购买数量
    private int categoryGoodsWeight;// 类别商品重量（针对非标按重量售卖商品）
    private BigDecimal categoryGoodsPrice;// 同类商品本应该付费用
    private BigDecimal categoryFavorablePrice;// 同类商品总减免
    private BigDecimal actualCategoryPrice;// 同类商品实际价格

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public BigDecimal getGoodsFavorablePrice() {
        return goodsFavorablePrice;
    }

    public void setGoodsFavorablePrice(BigDecimal goodsFavorablePrice) {
        this.goodsFavorablePrice = goodsFavorablePrice;
    }

    public BigDecimal getGoodsActualPrice() {
        return goodsActualPrice;
    }

    public void setGoodsActualPrice(BigDecimal goodsActualPrice) {
        this.goodsActualPrice = goodsActualPrice;
    }

    public int getCategoryGoodsNumber() {
        return categoryGoodsNumber;
    }

    public void setCategoryGoodsNumber(int categoryGoodsNumber) {
        this.categoryGoodsNumber = categoryGoodsNumber;
    }

    public int getCategoryGoodsWeight() {
        return categoryGoodsWeight;
    }

    public void setCategoryGoodsWeight(int categoryGoodsWeight) {
        this.categoryGoodsWeight = categoryGoodsWeight;
    }

    public BigDecimal getCategoryGoodsPrice() {
        return categoryGoodsPrice;
    }

    public void setCategoryGoodsPrice(BigDecimal categoryGoodsPrice) {
        this.categoryGoodsPrice = categoryGoodsPrice;
    }

    public BigDecimal getCategoryFavorablePrice() {
        return categoryFavorablePrice;
    }

    public void setCategoryFavorablePrice(BigDecimal categoryFavorablePrice) {
        this.categoryFavorablePrice = categoryFavorablePrice;
    }

    public BigDecimal getActualCategoryPrice() {
        return actualCategoryPrice;
    }

    public void setActualCategoryPrice(BigDecimal actualCategoryPrice) {
        this.actualCategoryPrice = actualCategoryPrice;
    }

    @Override
    public String toString() {
        return "OrderDetails [id=" + id + ", orderId=" + orderId
                + ", barCodeId=" + barCodeId + ", goodsName=" + goodsName
                + ", salesMode=" + salesMode + ", goodsPrice=" + goodsPrice
                + ", goodsFavorablePrice=" + goodsFavorablePrice
                + ", goodsActualPrice=" + goodsActualPrice
                + ", categoryGoodsNumber=" + categoryGoodsNumber
                + ", categoryGoodsWeight=" + categoryGoodsWeight
                + ", categoryGoodsPrice=" + categoryGoodsPrice
                + ", categoryFavorablePrice=" + categoryFavorablePrice
                + ", actualCategoryPrice=" + actualCategoryPrice + "]";
    }

}
