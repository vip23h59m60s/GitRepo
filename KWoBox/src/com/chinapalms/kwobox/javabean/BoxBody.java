package com.chinapalms.kwobox.javabean;

import java.util.Date;

public class BoxBody {

    private int boxBodyId;// 售货机柜体ID
    private String boxBodyName;// 售货机名称（店名称）如：智购猫频汉智能科技旗舰店
    private int boxType;// 盘点模式0表示高频，1表示超高频，2表示称重方案
    private String model;// 型号
    private int refrigerationMode;// 制冷模式0表示常温，1表示直冷，2表示风冷
    private String boxModel;// 箱体型号
    private String boxMaker;// 箱体生产商
    private int cardgoRoadNumber;// 货道数量
    private int faceFunction;// 是否支持人脸识别模块
    private String screenModel;// 显示器型号
    private String monitorMaker;// 监控生产商
    private String outsideViewPicture;// 外观图
    private String cardgoRoadPicture;// 货道分布图
    private String boxManagerId;// 设备管理员
    private Date releaseDateTime;// 设备发布时间

    public int getBoxBodyId() {
        return boxBodyId;
    }

    public void setBoxBodyId(int boxBodyId) {
        this.boxBodyId = boxBodyId;
    }

    public String getBoxBodyName() {
        return boxBodyName;
    }

    public void setBoxBodyName(String boxBodyName) {
        this.boxBodyName = boxBodyName;
    }

    public int getBoxType() {
        return boxType;
    }

    public void setBoxType(int boxType) {
        this.boxType = boxType;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getRefrigerationMode() {
        return refrigerationMode;
    }

    public void setRefrigerationMode(int refrigerationMode) {
        this.refrigerationMode = refrigerationMode;
    }

    public String getBoxModel() {
        return boxModel;
    }

    public void setBoxModel(String boxModel) {
        this.boxModel = boxModel;
    }

    public String getBoxMaker() {
        return boxMaker;
    }

    public void setBoxMaker(String boxMaker) {
        this.boxMaker = boxMaker;
    }

    public int getCardgoRoadNumber() {
        return cardgoRoadNumber;
    }

    public void setCardgoRoadNumber(int cardgoRoadNumber) {
        this.cardgoRoadNumber = cardgoRoadNumber;
    }

    public int getFaceFunction() {
        return faceFunction;
    }

    public void setFaceFunction(int faceFunction) {
        this.faceFunction = faceFunction;
    }

    public String getScreenModel() {
        return screenModel;
    }

    public void setScreenModel(String screenModel) {
        this.screenModel = screenModel;
    }

    public String getMonitorMaker() {
        return monitorMaker;
    }

    public void setMonitorMaker(String monitorMaker) {
        this.monitorMaker = monitorMaker;
    }

    public String getOutsideViewPicture() {
        return outsideViewPicture;
    }

    public void setOutsideViewPicture(String outsideViewPicture) {
        this.outsideViewPicture = outsideViewPicture;
    }

    public String getCardgoRoadPicture() {
        return cardgoRoadPicture;
    }

    public void setCardgoRoadPicture(String cardgoRoadPicture) {
        this.cardgoRoadPicture = cardgoRoadPicture;
    }

    public String getBoxManagerId() {
        return boxManagerId;
    }

    public void setBoxManagerId(String boxManagerId) {
        this.boxManagerId = boxManagerId;
    }

    public Date getReleaseDateTime() {
        return releaseDateTime;
    }

    public void setReleaseDateTime(Date releaseDateTime) {
        this.releaseDateTime = releaseDateTime;
    }

    @Override
    public String toString() {
        return "BoxBody [boxBodyId=" + boxBodyId + ", boxBodyName="
                + boxBodyName + ", boxType=" + boxType + ", model=" + model
                + ", refrigerationMode=" + refrigerationMode + ", boxModel="
                + boxModel + ", boxMaker=" + boxMaker + ", cardgoRoadNumber="
                + cardgoRoadNumber + ", faceFunction=" + faceFunction
                + ", screenModel=" + screenModel + ", monitorMaker="
                + monitorMaker + ", outsideViewPicture=" + outsideViewPicture
                + ", cardgoRoadPicture=" + cardgoRoadPicture
                + ", boxManagerId=" + boxManagerId + ", releaseDateTime="
                + releaseDateTime + "]";
    }

}
