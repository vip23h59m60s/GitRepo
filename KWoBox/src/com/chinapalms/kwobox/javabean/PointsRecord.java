package com.chinapalms.kwobox.javabean;

import java.util.Date;

public class PointsRecord {

    final public static int PAGE_SIZE = 15;

    private int id;
    private String phoneNumber;// 用户电话号码
    private int pointsChange;// 用户积分变化值
    private int changeReason;// 用户积分变化原因积分变更原因0:表示减少，1表示增加
    private Date changeTime;// 积分变更时间

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

    public int getPointsChange() {
        return pointsChange;
    }

    public void setPointsChange(int pointsChange) {
        this.pointsChange = pointsChange;
    }

    public int getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(int changeReason) {
        this.changeReason = changeReason;
    }

    public Date getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Date changeTime) {
        this.changeTime = changeTime;
    }

    @Override
    public String toString() {
        return "PointsRecord [id=" + id + ", phoneNumber=" + phoneNumber
                + ", pointsChange=" + pointsChange + ", changeReason="
                + changeReason + ", changeTime=" + changeTime + "]";
    }

}
