package com.chinapalms.kwobox.javabean;

import java.util.Date;

public class RollingAdBox {

    /**
     * 售货机等级的轮播广告
     */
    private int boxAdId;// 售货机等级广告ID
    private String boxAdName;// 售货机等级广告名称
    private String boxAdUrl;// 广告轮播图片URL地址
    private String boxId;// 售货机ID
    private Date startTime;// 广告开始时间
    private Date endTime;// 广告结束时间
    private int adState;// 广告状态1:正常推广状态：0：关闭状态

    public int getBoxAdId() {
        return boxAdId;
    }

    public void setBoxAdId(int boxAdId) {
        this.boxAdId = boxAdId;
    }

    public String getBoxAdName() {
        return boxAdName;
    }

    public void setBoxAdName(String boxAdName) {
        this.boxAdName = boxAdName;
    }

    public String getBoxAdUrl() {
        return boxAdUrl;
    }

    public void setBoxAdUrl(String boxAdUrl) {
        this.boxAdUrl = boxAdUrl;
    }

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
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
        return "RollingAdBox [boxAdId=" + boxAdId + ", boxAdName=" + boxAdName
                + ", boxAdUrl=" + boxAdUrl + ", boxId=" + boxId
                + ", startTime=" + startTime + ", endTime=" + endTime
                + ", adState=" + adState + "]";
    }

}
