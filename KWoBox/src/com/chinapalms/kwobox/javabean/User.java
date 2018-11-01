package com.chinapalms.kwobox.javabean;

import java.util.Date;

public class User {

    private String phoneNumber;// 用户电话号码
    private int vipLevel; // 会员等级：0：SVIP,1:普通用户
    private String userSource;// 用户来源
    private String identityCardNumber;// 身份证号码
    private String name;// 姓名
    private int sex;// 性别1.男,2女
    private Date birthday;// 出生日期
    private String address;// 通信地址
    private String wxId;// 微信ID
    private String wxOpenId;// 用户在当前微信小程序的openId
    private String alipayId;// 支付宝ID
    private int wxCredit;// 微信信用评分
    private int alipayCredit;// 支付宝信用评分
    private int personalCredit; // 个人信用评分
    private int faceFunction;// 是否开通人脸识别功能0：未开通，1：已开通，2：因存在长相相同的人脸暂停该功能
    private String faceFunctionPassword;// 人脸识别功能对应密码
    private String faceFunctionPicture;// 人脸识别功能对应人脸照片
    private int userPoints;// 用户积分
    private int userUnReadOrderNumber;// 用户未读订单数量
    private Date registerTime;// 用户注册时间

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }

    public String getUserSource() {
        return userSource;
    }

    public void setUserSource(String userSource) {
        this.userSource = userSource;
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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWxId() {
        return wxId;
    }

    public void setWxId(String wxId) {
        this.wxId = wxId;
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    public String getAlipayId() {
        return alipayId;
    }

    public void setAlipayId(String alipayId) {
        this.alipayId = alipayId;
    }

    public int getWxCredit() {
        return wxCredit;
    }

    public void setWxCredit(int wxCredit) {
        this.wxCredit = wxCredit;
    }

    public int getAlipayCredit() {
        return alipayCredit;
    }

    public void setAlipayCredit(int alipayCredit) {
        this.alipayCredit = alipayCredit;
    }

    public int getPersonalCredit() {
        return personalCredit;
    }

    public void setPersonalCredit(int personalCredit) {
        this.personalCredit = personalCredit;
    }

    public int getFaceFunction() {
        return faceFunction;
    }

    public void setFaceFunction(int faceFunction) {
        this.faceFunction = faceFunction;
    }

    public String getFaceFunctionPassword() {
        return faceFunctionPassword;
    }

    public void setFaceFunctionPassword(String faceFunctionPassword) {
        this.faceFunctionPassword = faceFunctionPassword;
    }

    public String getFaceFunctionPicture() {
        return faceFunctionPicture;
    }

    public void setFaceFunctionPicture(String faceFunctionPicture) {
        this.faceFunctionPicture = faceFunctionPicture;
    }

    public int getUserPoints() {
        return userPoints;
    }

    public void setUserPoints(int userPoints) {
        this.userPoints = userPoints;
    }

    public int getUserUnReadOrderNumber() {
        return userUnReadOrderNumber;
    }

    public void setUserUnReadOrderNumber(int userUnReadOrderNumber) {
        this.userUnReadOrderNumber = userUnReadOrderNumber;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    @Override
    public String toString() {
        return "User [phoneNumber=" + phoneNumber + ", vipLevel=" + vipLevel
                + ", userSource=" + userSource + ", identityCardNumber="
                + identityCardNumber + ", name=" + name + ", sex=" + sex
                + ", birthday=" + birthday + ", address=" + address + ", wxId="
                + wxId + ", wxOpenId=" + wxOpenId + ", alipayId=" + alipayId
                + ", wxCredit=" + wxCredit + ", alipayCredit=" + alipayCredit
                + ", personalCredit=" + personalCredit + ", faceFunction="
                + faceFunction + ", faceFunctionPassword="
                + faceFunctionPassword + ", faceFunctionPicture="
                + faceFunctionPicture + ", userPoints=" + userPoints
                + ", userUnReadOrderNumber=" + userUnReadOrderNumber
                + ", registerTime=" + registerTime + "]";
    }

}
