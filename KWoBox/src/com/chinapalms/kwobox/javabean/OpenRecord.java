package com.chinapalms.kwobox.javabean;

import java.util.Date;

public class OpenRecord {

    private int id;
    private String boxId;// 售货机ID
    private String phoneNumber;// 手机号码
    private int customerWorkerId;// 补货员开门时的补货员ID
    private String openWay;// 开门方式
    private String openPicture;// 开门照片
    private Date openTime;// 开门时间

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getCustomerWorkerId() {
        return customerWorkerId;
    }

    public void setCustomerWorkerId(int customerWorkerId) {
        this.customerWorkerId = customerWorkerId;
    }

    public String getOpenWay() {
        return openWay;
    }

    public void setOpenWay(String openWay) {
        this.openWay = openWay;
    }

    public String getOpenPicture() {
        return openPicture;
    }

    public void setOpenPicture(String openPicture) {
        this.openPicture = openPicture;
    }

    public Date getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Date openTime) {
        this.openTime = openTime;
    }

    @Override
    public String toString() {
        return "OpenRecord [id=" + id + ", boxId=" + boxId + ", phoneNumber="
                + phoneNumber + ", customerWorkerId=" + customerWorkerId
                + ", openWay=" + openWay + ", openPicture=" + openPicture
                + ", openTime=" + openTime + "]";
    }

}
