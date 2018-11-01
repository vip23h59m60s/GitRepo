package com.chinapalms.kwobox.javabean;

import java.util.Date;

public class IdentifyCode {

    private String phoneNumber;// 手机号码
    private String identifyCode;// 验证码
    private Date time;// 获取验证码时间

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIdentifyCode() {
        return identifyCode;
    }

    public void setIdentifyCode(String identifyCode) {
        this.identifyCode = identifyCode;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "IdentifyCode [phoneNumber=" + phoneNumber + ", identifyCode="
                + identifyCode + ", time=" + time + "]";
    }

}
