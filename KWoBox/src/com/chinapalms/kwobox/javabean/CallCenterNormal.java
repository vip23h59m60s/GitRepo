package com.chinapalms.kwobox.javabean;

import java.util.Date;

public class CallCenterNormal {

    private int callCenterId;// 内部监控中心ID
    private String identityCardNumber;// 身份证号码
    private String name;// 姓名
    private String phoneNumber;// 手机号码
    private String userName;// 用户名
    private String password;// 登录密码
    private String employeeNumber;// 员工号
    private String email;// 电子邮箱
    private int superAdminId;// 超级管理员ID
    private int workState;// 1和2为工作状态，0位非工作状态
    private Date registerTime;// 用户分配时间

    public int getCallCenterId() {
        return callCenterId;
    }

    public void setCallCenterId(int callCenterId) {
        this.callCenterId = callCenterId;
    }

    public String getIdentityCardNumber() {
        return identityCardNumber;
    }

    public void setIdentityCardNumber(String identityCardNumber) {
        this.identityCardNumber = identityCardNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSuperAdminId() {
        return superAdminId;
    }

    public void setSuperAdminId(int superAdminId) {
        this.superAdminId = superAdminId;
    }

    public int getWorkState() {
        return workState;
    }

    public void setWorkState(int workState) {
        this.workState = workState;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    @Override
    public String toString() {
        return "CallCenterNormal [callCenterId=" + callCenterId
                + ", identityCardNumber=" + identityCardNumber + ", name="
                + name + ", phoneNumber=" + phoneNumber + ", userName="
                + userName + ", password=" + password + ", employeeNumber="
                + employeeNumber + ", email=" + email + ", superAdminId="
                + superAdminId + ", workState=" + workState + ", registerTime="
                + registerTime + "]";
    }

}
