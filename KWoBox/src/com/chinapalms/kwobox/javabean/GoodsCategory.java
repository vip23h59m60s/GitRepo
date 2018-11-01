package com.chinapalms.kwobox.javabean;

import java.math.BigDecimal;
import java.util.Date;

public class GoodsCategory {

    private String barCodeId;// 商品条形码ID
    private String goodsName;// 商品名称
    private String brandCompany;// 商品条码发布厂商（商品品牌商）
    private String nationalStandard;// 商品是否为国标产品（分为国标产品和自定义产品）
    private int salesMode;// 商品销售方式，0：表示按件销售的标准商品，1：表示按重量销售的非标准商品
    private int goodsSpec;// 商品规格（容量或重量等）
    private int weight;// 商品重量（单位g）
    private int weightError;// 商品重量误差（单位g）
    private int goodsUnit;// 商品单位0:表示无单位，1：表示ml，2表示g
    private String packageType;// 商品包装形式
    private int length;// 商品长度（单位mm）
    private int width;// 商品宽度（单位mm）
    private int height;// 商品高度（单位mm）
    private String tradeMark;// 商品商标
    private String storageCondition;// 商品储存条件
    private int expiryTime;// 商品保质期
    private String goodsPicture;// 商品六视图
    private BigDecimal goodsPurchasePrice;// 商品参考进价
    private BigDecimal recommendedRetailPrice;// 商品建议零售价
    private int goodsManager;// 商品管理员
    private Date storageTime;// 商品入库时间

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

    public String getBrandCompany() {
        return brandCompany;
    }

    public void setBrandCompany(String brandCompany) {
        this.brandCompany = brandCompany;
    }

    public String getNationalStandard() {
        return nationalStandard;
    }

    public void setNationalStandard(String nationalStandard) {
        this.nationalStandard = nationalStandard;
    }

    public int getSalesMode() {
        return salesMode;
    }

    public void setSalesMode(int salesMode) {
        this.salesMode = salesMode;
    }

    public int getGoodsSpec() {
        return goodsSpec;
    }

    public void setGoodsSpec(int goodsSpec) {
        this.goodsSpec = goodsSpec;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getWeightError() {
        return weightError;
    }

    public void setWeightError(int weightError) {
        this.weightError = weightError;
    }

    public int getGoodsUnit() {
        return goodsUnit;
    }

    public void setGoodsUnit(int goodsUnit) {
        this.goodsUnit = goodsUnit;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getTradeMark() {
        return tradeMark;
    }

    public void setTradeMark(String tradeMark) {
        this.tradeMark = tradeMark;
    }

    public String getStorageCondition() {
        return storageCondition;
    }

    public void setStorageCondition(String storageCondition) {
        this.storageCondition = storageCondition;
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

    public BigDecimal getGoodsPurchasePrice() {
        return goodsPurchasePrice;
    }

    public void setGoodsPurchasePrice(BigDecimal goodsPurchasePrice) {
        this.goodsPurchasePrice = goodsPurchasePrice;
    }

    public BigDecimal getRecommendedRetailPrice() {
        return recommendedRetailPrice;
    }

    public void setRecommendedRetailPrice(BigDecimal recommendedRetailPrice) {
        this.recommendedRetailPrice = recommendedRetailPrice;
    }

    public int getGoodsManager() {
        return goodsManager;
    }

    public void setGoodsManager(int goodsManager) {
        this.goodsManager = goodsManager;
    }

    public Date getStorageTime() {
        return storageTime;
    }

    public void setStorageTime(Date storageTime) {
        this.storageTime = storageTime;
    }

    @Override
    public String toString() {
        return "GoodsCategory [barCodeId=" + barCodeId + ", goodsName="
                + goodsName + ", brandCompany=" + brandCompany
                + ", nationalStandard=" + nationalStandard + ", salesMode="
                + salesMode + ", goodsSpec=" + goodsSpec + ", weight=" + weight
                + ", weightError=" + weightError + ", goodsUnit=" + goodsUnit
                + ", packageType=" + packageType + ", length=" + length
                + ", width=" + width + ", height=" + height + ", tradeMark="
                + tradeMark + ", storageCondition=" + storageCondition
                + ", expiryTime=" + expiryTime + ", goodsPicture="
                + goodsPicture + ", goodsPurchasePrice=" + goodsPurchasePrice
                + ", recommendedRetailPrice=" + recommendedRetailPrice
                + ", goodsManager=" + goodsManager + ", storageTime="
                + storageTime + "]";
    }

}
