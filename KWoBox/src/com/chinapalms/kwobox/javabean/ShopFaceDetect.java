package com.chinapalms.kwobox.javabean;

import java.util.Date;

public class ShopFaceDetect {

    // 门店模式人脸识别
    private int shopId;// 门店ID
    private String phoneNumber;// 电话号码
    private int state;// 当前门店人脸识别是否可用1：可用 0 ：不可用，主要用来处理一个人脸多个手机号的问题
    private Date lastFaceDetectShoppingTime;// 最近一次在该店人脸购物时间

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getLastFaceDetectShoppingTime() {
        return lastFaceDetectShoppingTime;
    }

    public void setLastFaceDetectShoppingTime(Date lastFaceDetectShoppingTime) {
        this.lastFaceDetectShoppingTime = lastFaceDetectShoppingTime;
    }

    @Override
    public String toString() {
        return "ShopFaceDetect [shopId=" + shopId + ", phoneNumber="
                + phoneNumber + ", state=" + state
                + ", lastFaceDetectShoppingTime=" + lastFaceDetectShoppingTime
                + "]";
    }

}
