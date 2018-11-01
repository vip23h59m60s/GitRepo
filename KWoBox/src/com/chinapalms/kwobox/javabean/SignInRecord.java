package com.chinapalms.kwobox.javabean;

import java.util.Date;

public class SignInRecord {

    private int id;
    private String phoneNumber;// 电话号码
    private String latitude;// 签到位置纬度
    private String longitude;// 签到位置经度
    private String address;// 签到地址
    private int scores;// 签到获得积分
    private Date signInDateTime;// 签到时间

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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getScores() {
        return scores;
    }

    public void setScores(int scores) {
        this.scores = scores;
    }

    public Date getSignInDateTime() {
        return signInDateTime;
    }

    public void setSignInDateTime(Date signInDateTime) {
        this.signInDateTime = signInDateTime;
    }

    @Override
    public String toString() {
        return "SignInRecord [id=" + id + ", phoneNumber=" + phoneNumber
                + ", latitude=" + latitude + ", longitude=" + longitude
                + ", address=" + address + ", scores=" + scores
                + ", signInDateTime=" + signInDateTime + "]";
    }

}
