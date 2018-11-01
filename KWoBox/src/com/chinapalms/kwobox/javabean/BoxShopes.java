package com.chinapalms.kwobox.javabean;

import java.util.Date;

public class BoxShopes {

    private int shopId;// 店ID
    private String shopName;// 店名
    private String carrieroperator;// 运营商
    private String equipmentDealer;// 设备商
    private String siteDealer;// 场地商
    private String address;// 地址
    private String environmentPicture;// 环境照片
    private int shopType;// 商铺类型
    private int siteType;// 场地类型
    private Date openTime;// 开始营业时间
    private Date closeTime;// 结束营业时间
    private int customerId;// 店铺所属商户ID
    private Date shopCreateTime;// 店铺创建时间

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getCarrieroperator() {
        return carrieroperator;
    }

    public void setCarrieroperator(String carrieroperator) {
        this.carrieroperator = carrieroperator;
    }

    public String getEquipmentDealer() {
        return equipmentDealer;
    }

    public void setEquipmentDealer(String equipmentDealer) {
        this.equipmentDealer = equipmentDealer;
    }

    public String getSiteDealer() {
        return siteDealer;
    }

    public void setSiteDealer(String siteDealer) {
        this.siteDealer = siteDealer;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEnvironmentPicture() {
        return environmentPicture;
    }

    public void setEnvironmentPicture(String environmentPicture) {
        this.environmentPicture = environmentPicture;
    }

    public int getShopType() {
        return shopType;
    }

    public void setShopType(int shopType) {
        this.shopType = shopType;
    }

    public int getSiteType() {
        return siteType;
    }

    public void setSiteType(int siteType) {
        this.siteType = siteType;
    }

    public Date getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Date openTime) {
        this.openTime = openTime;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Date getShopCreateTime() {
        return shopCreateTime;
    }

    public void setShopCreateTime(Date shopCreateTime) {
        this.shopCreateTime = shopCreateTime;
    }

    @Override
    public String toString() {
        return "BoxShopes [shopId=" + shopId + ", shopName=" + shopName
                + ", carrieroperator=" + carrieroperator + ", equipmentDealer="
                + equipmentDealer + ", siteDealer=" + siteDealer + ", address="
                + address + ", environmentPicture=" + environmentPicture
                + ", shopType=" + shopType + ", siteType=" + siteType
                + ", openTime=" + openTime + ", closeTime=" + closeTime
                + ", customerId=" + customerId + ", shopCreateTime="
                + shopCreateTime + "]";
    }

}
