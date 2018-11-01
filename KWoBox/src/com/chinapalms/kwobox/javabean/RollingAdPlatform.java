package com.chinapalms.kwobox.javabean;

import java.util.Date;

public class RollingAdPlatform {

    /**
     * 智购猫平台等级的轮播广告
     */
    private int platformAdId;// 智购猫平台广告ID
    private String platformAdName;// 智购猫平台广告名称
    private String platformAdUrl;// 广告轮播图片URL地址
    private Date startTime;// 广告开始时间
    private Date endTime;// 广告结束时间
    private int adState;// 广告状态1:正常推广状态：0：关闭状态

    public int getPlatformAdId() {
        return platformAdId;
    }

    public void setPlatformAdId(int platformAdId) {
        this.platformAdId = platformAdId;
    }

    public String getPlatformAdName() {
        return platformAdName;
    }

    public void setPlatformAdName(String platformAdName) {
        this.platformAdName = platformAdName;
    }

    public String getPlatformAdUrl() {
        return platformAdUrl;
    }

    public void setPlatformAdUrl(String platformAdUrl) {
        this.platformAdUrl = platformAdUrl;
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
        return "RollingAdPlatform [platformAdId=" + platformAdId
                + ", platformAdName=" + platformAdName + ", platformAdUrl="
                + platformAdUrl + ", startTime=" + startTime + ", endTime="
                + endTime + ", adState=" + adState + "]";
    }

}
