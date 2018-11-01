package com.chinapalms.kwobox.javabean;

import java.util.Date;

public class CustomerWorker {

    private int customerWorkerId;// 商户员工ID
    private String identityCardNumber;// 员工身份证号码
    private String name;// 员工姓名
    private String phoneNumber;// 手机号码
    private String userName;// 用户名
    private String password;// 密码
    private int customerId;// 商户ID
    private int customerAdminId;// 商户管理员ID
    private int superPermission;// 0:表示普通worker，1：表示具有智购猫super权限
    private Date updateDateTime;// 更新授权时间

    public int getCustomerWorkerId() {
        return customerWorkerId;
    }

    public void setCustomerWorkerId(int customerWorkerId) {
        this.customerWorkerId = customerWorkerId;
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

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getCustomerAdminId() {
        return customerAdminId;
    }

    public void setCustomerAdminId(int customerAdminId) {
        this.customerAdminId = customerAdminId;
    }

    public int getSuperPermission() {
        return superPermission;
    }

    public void setSuperPermission(int superPermission) {
        this.superPermission = superPermission;
    }

    public Date getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(Date updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    @Override
    public String toString() {
        return "CustomerWorker [customerWorkerId=" + customerWorkerId
                + ", identityCardNumber=" + identityCardNumber + ", name="
                + name + ", phoneNumber=" + phoneNumber + ", userName="
                + userName + ", password=" + password + ", customerId="
                + customerId + ", customerAdminId=" + customerAdminId
                + ", superPermission=" + superPermission + ", updateDateTime="
                + updateDateTime + "]";
    }

}
