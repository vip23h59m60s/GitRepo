package com.chinapalms.kwobox.javabean;

import java.util.Date;

public class ICS {

    private int icsId;// 公共机ID
    private String model;// 工控机型号
    private String simCardNumber;// 物联网卡号
    private String wifiMacAddress;// WiFI MAC 地址
    private String snNumber;// SN号
    private String imeiNumber;// IMEI号
    private String fotaVersion;// 工控机系统版本
    private String lingMaoVersion;// 灵猫app版本号
    private String lingMaoCoreVersion;// 灵猫core版本号
    private String lingMaoToolsVersion;// 灵猫调试工具版本号
    private String advVersion;// 工控机广告版本
    private int debugStatus;// debug状态：正常运营和debug模式
    private String logLevel;// log等级
    private int checkSelf;// 是否自检
    private int deviceManagerId;// 设备管理员
    private Date releaseTime;// 设备release时间

    public int getIcsId() {
        return icsId;
    }

    public void setIcsId(int icsId) {
        this.icsId = icsId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSimCardNumber() {
        return simCardNumber;
    }

    public void setSimCardNumber(String simCardNumber) {
        this.simCardNumber = simCardNumber;
    }

    public String getWifiMacAddress() {
        return wifiMacAddress;
    }

    public void setWifiMacAddress(String wifiMacAddress) {
        this.wifiMacAddress = wifiMacAddress;
    }

    public String getSnNumber() {
        return snNumber;
    }

    public void setSnNumber(String snNumber) {
        this.snNumber = snNumber;
    }

    public String getImeiNumber() {
        return imeiNumber;
    }

    public void setImeiNumber(String imeiNumber) {
        this.imeiNumber = imeiNumber;
    }

    public String getFotaVersion() {
        return fotaVersion;
    }

    public void setFotaVersion(String fotaVersion) {
        this.fotaVersion = fotaVersion;
    }

    public String getLingMaoVersion() {
        return lingMaoVersion;
    }

    public void setLingMaoVersion(String lingMaoVersion) {
        this.lingMaoVersion = lingMaoVersion;
    }

    public String getLingMaoCoreVersion() {
        return lingMaoCoreVersion;
    }

    public void setLingMaoCoreVersion(String lingMaoCoreVersion) {
        this.lingMaoCoreVersion = lingMaoCoreVersion;
    }

    public String getLingMaoToolsVersion() {
        return lingMaoToolsVersion;
    }

    public void setLingMaoToolsVersion(String lingMaoToolsVersion) {
        this.lingMaoToolsVersion = lingMaoToolsVersion;
    }

    public String getAdvVersion() {
        return advVersion;
    }

    public void setAdvVersion(String advVersion) {
        this.advVersion = advVersion;
    }

    public int getDebugStatus() {
        return debugStatus;
    }

    public void setDebugStatus(int debugStatus) {
        this.debugStatus = debugStatus;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public int getCheckSelf() {
        return checkSelf;
    }

    public void setCheckSelf(int checkSelf) {
        this.checkSelf = checkSelf;
    }

    public int getDeviceManagerId() {
        return deviceManagerId;
    }

    public void setDeviceManagerId(int deviceManagerId) {
        this.deviceManagerId = deviceManagerId;
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    @Override
    public String toString() {
        return "ICS [icsId=" + icsId + ", model=" + model + ", simCardNumber="
                + simCardNumber + ", wifiMacAddress=" + wifiMacAddress
                + ", snNumber=" + snNumber + ", imeiNumber=" + imeiNumber
                + ", fotaVersion=" + fotaVersion + ", lingMaoVersion="
                + lingMaoVersion + ", lingMaoCoreVersion=" + lingMaoCoreVersion
                + ", lingMaoToolsVersion=" + lingMaoToolsVersion
                + ", advVersion=" + advVersion + ", debugStatus=" + debugStatus
                + ", logLevel=" + logLevel + ", checkSelf=" + checkSelf
                + ", deviceManagerId=" + deviceManagerId + ", releaseTime="
                + releaseTime + "]";
    }

}
