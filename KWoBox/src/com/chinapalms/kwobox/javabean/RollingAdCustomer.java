package com.chinapalms.kwobox.javabean;

import java.util.Date;

public class RollingAdCustomer {

    /**
     * 商户等级的轮播广告
     */
    private int customerAdId;// 商户等级广告ID
    private String customerAdName;// 商户广告名称
    private String customerAdUrl;// 广告轮播图片URL地址
    private int customerId;// 商户ID
    private Date startTime;// 广告开始时间
    private Date endTime;// 广告结束时间
    private int adState;// 广告状态1:正常推广状态：0：关闭状态

    public int getCustomerAdId() {
        return customerAdId;
    }

    public void setCustomerAdId(int customerAdId) {
        this.customerAdId = customerAdId;
    }

    public String getCustomerAdName() {
        return customerAdName;
    }

    public void setCustomerAdName(String customerAdName) {
        this.customerAdName = customerAdName;
    }

    public String getCustomerAdUrl() {
        return customerAdUrl;
    }

    public void setCustomerAdUrl(String customerAdUrl) {
        this.customerAdUrl = customerAdUrl;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getAdState() {
        return adState;
    }

    public void setAdState(int adState) {
        this.adState = adState;
    }

    @Override
    public String toString() {
        return "RollingAdCustomer [customerAdId=" + customerAdId
                + ", customerAdName=" + customerAdName + ", customerAdUrl="
                + customerAdUrl + ", customerId=" + customerId + ", startTime="
                + startTime + ", endTime=" + endTime + ", adState=" + adState
                + "]";
    }

}
