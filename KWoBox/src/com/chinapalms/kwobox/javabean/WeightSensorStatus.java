package com.chinapalms.kwobox.javabean;

import java.util.Date;

public class WeightSensorStatus {

    private int id;
    private String boxId;// 售货机ID
    private int state;// 0：层架断开；1：层架恢复正常
    private Date reportTime;// 上报层架异常时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
        return "WeightSensorStatus [id=" + id + ", boxId=" + boxId + ", state="
                + state + ", reportTime=" + reportTime + "]";
    }

}
