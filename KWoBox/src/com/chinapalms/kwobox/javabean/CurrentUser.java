package com.chinapalms.kwobox.javabean;

public class CurrentUser {

    private String phoneNumber;// 当前售货柜使用者手机号码
    private int customerWorkerId;// 当前售货柜使用者的workerId;
    private String boxId;// 售货柜ID
    private String customType; // 当前客户端类型:WeChat或者Alipay
    private int vipLevel;// vip等级：0：超级VIP；1：普通用户
    private String userType; // 普通消费者或上货员或者管理员
    private String agreementNO;// 委托代扣ID,对于支付宝是agreementNO,对于微信相当于contractId

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

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    public String getCustomType() {
        return customType;
    }

    public void setCustomType(String customType) {
        this.customType = customType;
    }

    public int getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getAgreementNO() {
        return agreementNO;
    }

    public void setAgreementNO(String agreementNO) {
        this.agreementNO = agreementNO;
    }

    @Override
    public String toString() {
        return "CurrentUser [phoneNumber=" + phoneNumber
                + ", customerWorkerId=" + customerWorkerId + ", boxId=" + boxId
                + ", customType=" + customType + ", vipLevel=" + vipLevel
                + ", userType=" + userType + ", agreementNO=" + agreementNO
                + "]";
    }

}
