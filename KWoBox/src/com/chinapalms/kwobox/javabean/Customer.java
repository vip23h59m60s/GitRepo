package com.chinapalms.kwobox.javabean;

import java.math.BigDecimal;
import java.util.Date;

public class Customer {

    private int customerId;// 商户ID
    private String mchId;// 商户ID，主要用于第三方服务器逻辑流程对接判断
    private String companyName;// 商户公司名称
    private String shortName;// 商户简称
    private String owner;// 公司法人
    private String identityCardNumber;// 法人身份证号码
    private String registerAddress;// 公司注册地址
    private BigDecimal registerMoney;// 注册资金
    private String socialCreditCode;// 社会信用代码
    private String operateCity;// 经营城市
    private String taxInfo;// 税票信息
    private String workAddress;// 办公地址
    private String accountNumber;// 收款账号
    private String licencePicture;// 营业执照照片
    private int cooperationMode;// 合作模式 0:智购猫自营；1：代理经营（例如Yoho）；2：三方对接（例如小e）
    private int bussinessManagerId;// 商务管理员ID
    private Date cooperateDatetime;// 开始商务合作时间

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getIdentityCardNumber() {
        return identityCardNumber;
    }

    public void setIdentityCardNumber(String identityCardNumber) {
        this.identityCardNumber = identityCardNumber;
    }

    public String getRegisterAddress() {
        return registerAddress;
    }

    public void setRegisterAddress(String registerAddress) {
        this.registerAddress = registerAddress;
    }

    public BigDecimal getRegisterMoney() {
        return registerMoney;
    }

    public void setRegisterMoney(BigDecimal registerMoney) {
        this.registerMoney = registerMoney;
    }

    public String getSocialCreditCode() {
        return socialCreditCode;
    }

    public void setSocialCreditCode(String socialCreditCode) {
        this.socialCreditCode = socialCreditCode;
    }

    public String getOperateCity() {
        return operateCity;
    }

    public void setOperateCity(String operateCity) {
        this.operateCity = operateCity;
    }

    public String getTaxInfo() {
        return taxInfo;
    }

    public void setTaxInfo(String taxInfo) {
        this.taxInfo = taxInfo;
    }

    public String getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(String workAddress) {
        this.workAddress = workAddress;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getLicencePicture() {
        return licencePicture;
    }

    public void setLicencePicture(String licencePicture) {
        this.licencePicture = licencePicture;
    }

    public int getCooperationMode() {
        return cooperationMode;
    }

    public void setCooperationMode(int cooperationMode) {
        this.cooperationMode = cooperationMode;
    }

    public int getBussinessManagerId() {
        return bussinessManagerId;
    }

    public void setBussinessManagerId(int bussinessManagerId) {
        this.bussinessManagerId = bussinessManagerId;
    }

    public Date getCooperateDatetime() {
        return cooperateDatetime;
    }

    public void setCooperateDatetime(Date cooperateDatetime) {
        this.cooperateDatetime = cooperateDatetime;
    }

    @Override
    public String toString() {
        return "Customer [customerId=" + customerId + ", mchId=" + mchId
                + ", companyName=" + companyName + ", shortName=" + shortName
                + ", owner=" + owner + ", identityCardNumber="
                + identityCardNumber + ", registerAddress=" + registerAddress
                + ", registerMoney=" + registerMoney + ", socialCreditCode="
                + socialCreditCode + ", operateCity=" + operateCity
                + ", taxInfo=" + taxInfo + ", workAddress=" + workAddress
                + ", accountNumber=" + accountNumber + ", licencePicture="
                + licencePicture + ", cooperationMode=" + cooperationMode
                + ", bussinessManagerId=" + bussinessManagerId
                + ", cooperateDatetime=" + cooperateDatetime + "]";
    }

}
