package com.chinapalms.kwobox.javabean;

import java.util.Date;

public class CloseRecord {

    private int id;
    private String boxId;// 售货机ID
    private String phoneNumber;// 手机号码
    private int customerWorkerId;// 开关门是补货员时的补货员ID
    private String closePicture;// 关门照片
    private Date closeTime;// 关门时间

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

    public String getClosePicture() {
        return closePicture;
    }

    public void setClosePicture(String closePicture) {
        this.closePicture = closePicture;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

    @Override
    public String toString() {
        return "CloseRecord [id=" + id + ", boxId=" + boxId + ", phoneNumber="
                + phoneNumber + ", customerWorkerId=" + customerWorkerId
                + ", closePicture=" + closePicture + ", closeTime=" + closeTime
                + "]";
    }

}
