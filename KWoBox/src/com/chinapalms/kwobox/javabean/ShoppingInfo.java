package com.chinapalms.kwobox.javabean;

import java.util.Date;

public class ShoppingInfo {

    final public static int PAGE_SIZE = 10;

    private String shoppingInfoId;// 本次购物信息ID
    private String orderId;// 订单ID
    private int customerId;// shoppingInfo对应的商户ID
    private String boxId;// 售货机Id
    private String phoneNumber;// 手机号码
    private String requestSerialNumber;// 本次购物请求开门的请求序列号
    private String shoppingInfo;// 本次购物相关信息
    private String videoUrlInfo;// 购物监控视频路径
    private int referScore;// 视频处理参考分值
    private String resolveUser;// 视频处理人员
    private int state;// 该次购物视频状态
    private Date openTime;// 购物视频发生时间
    private Date closeTime;// 该视频处理完成时间

    public String getShoppingInfoId() {
        return shoppingInfoId;
    }

    public void setShoppingInfoId(String shoppingInfoId) {
        this.shoppingInfoId = shoppingInfoId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRequestSerialNumber() {
        return requestSerialNumber;
    }

    public void setRequestSerialNumber(String requestSerialNumber) {
        this.requestSerialNumber = requestSerialNumber;
    }

    public String getShoppingInfo() {
        return shoppingInfo;
    }

    public void setShoppingInfo(String shoppingInfo) {
        this.shoppingInfo = shoppingInfo;
    }

    public String getVideoUrlInfo() {
        return videoUrlInfo;
    }

    public void setVideoUrlInfo(String videoUrlInfo) {
        this.videoUrlInfo = videoUrlInfo;
    }

    public int getReferScore() {
        return referScore;
    }

    public void setReferScore(int referScore) {
        this.referScore = referScore;
    }

    public String getResolveUser() {
        return resolveUser;
    }

    public void setResolveUser(String resolveUser) {
        this.resolveUser = resolveUser;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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

    @Override
    public String toString() {
        return "ShoppingInfo [shoppingInfoId=" + shoppingInfoId + ", orderId="
                + orderId + ", customerId=" + customerId + ", boxId=" + boxId
                + ", phoneNumber=" + phoneNumber + ", requestSerialNumber="
                + requestSerialNumber + ", shoppingInfo=" + shoppingInfo
                + ", videoUrlInfo=" + videoUrlInfo + ", referScore="
                + referScore + ", resolveUser=" + resolveUser + ", state="
                + state + ", openTime=" + openTime + ", closeTime=" + closeTime
                + "]";
    }

}
