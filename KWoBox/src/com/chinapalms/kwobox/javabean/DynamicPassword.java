package com.chinapalms.kwobox.javabean;

import java.util.Date;

public class DynamicPassword {

    private String phoneNumber;// 手机号码
    private String dynamicPassword;// 8位动态密码
    private Date passwordTime;// 生成8位动态密码的时间

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDynamicPassword() {
        return dynamicPassword;
    }

    public void setDynamicPassword(String dynamicPassword) {
        this.dynamicPassword = dynamicPassword;
    }

    public Date getPasswordTime() {
        return passwordTime;
    }

    public void setPasswordTime(Date passwordTime) {
        this.passwordTime = passwordTime;
    }

    @Override
    public String toString() {
        return "DynamicPassword [phoneNumber=" + phoneNumber
                + ", dynamicPassword=" + dynamicPassword + ", passwordTime="
                + passwordTime + "]";
    }

}
