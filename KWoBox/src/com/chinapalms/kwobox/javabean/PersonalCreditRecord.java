package com.chinapalms.kwobox.javabean;

import java.util.Date;

public class PersonalCreditRecord {

    final public static int PAGE_SIZE = 15;

    private int id;
    private String phoneNumber;// 用户电话号码
    private String boxId; // 售货机ID
    private int changeReason;// 个人征信改变原因；0：表示非正常购物行为，1：表示正常购物行为
    private int changeValue;// 征信积分改变值
    private Date changeTime;// 征信积分更新时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    public int getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(int changeReason) {
        this.changeReason = changeReason;
    }

    public int getChangeValue() {
        return changeValue;
    }

    public void setChangeValue(int changeValue) {
        this.changeValue = changeValue;
    }

    public Date getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Date changeTime) {
        this.changeTime = changeTime;
    }

    @Override
    public String toString() {
        return "PersonalCreditRecord [id=" + id + ", phoneNumber="
                + phoneNumber + ", boxId=" + boxId + ", changeReason="
                + changeReason + ", changeValue=" + changeValue
                + ", changeTime=" + changeTime + "]";
    }

}
