package com.chinapalms.kwobox.javabean;

import java.util.Date;

public class BoxStatus {

    public final static String BOX_STATUS_DESCRIPTION_NORMAL = "正常营业中";
    public final static String BOX_STATUS_DESCRIPTION_SERVING = "设备服务中";
    public final static String BOX_STATUS_DESCRIPTION_SERVING_WITH_NORMAL_USER = "设备服务中";
    public final static String BOX_STATUS_DESCRIPTION_SERVING_WITH_MANAGER = "设备理货中";
    public final static String BOX_STATUS_DESCRIPTION_BREAKDOWN = "设备故障,请及时上报维修";
    public final static String BOX_NEARBY_STATUS_DESCRIPTION_BREAKDOWN = "设备维护中";

    String boxId;// 售货机ID
    int state;// 售货机上报状态0表示正常工作状态,1表示忙碌状态,2表示...
    Date reportTime;// 售货机上报状态时间

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getReportTime() {
        return reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

    @Override
    public String toString() {
        return "BoxStatus [boxId=" + boxId + ", state=" + state
                + ", reportTime=" + reportTime + "]";
    }

}
