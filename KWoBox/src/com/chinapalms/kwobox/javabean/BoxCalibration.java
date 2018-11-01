package com.chinapalms.kwobox.javabean;

import java.util.Date;

public class BoxCalibration {

    private String boxId;// 售货机ID
    private String fileUrl;// 校准文件的路径
    private Date calibrateTime;// 校准时间

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Date getCalibrateTime() {
        return calibrateTime;
    }

    public void setCalibrateTime(Date calibrateTime) {
        this.calibrateTime = calibrateTime;
    }

    @Override
    public String toString() {
        return "BoxCalibration [boxId=" + boxId + ", fileUrl=" + fileUrl
                + ", calibrateTime=" + calibrateTime + "]";
    }

}
