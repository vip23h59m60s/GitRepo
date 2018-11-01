package com.chinapalms.kwobox.javabean;

import java.util.Date;

public class Boxes {

    public final static int PAGE_SIZE = 5;
    public final static int NEARBY_BOXES_PAGE_SIZE = 50;

    private int id;
    private String boxId;// 售货机ID
    private String boxName; // 售货机名称（商店名称）
    private int customerId;// 商户ID
    private int boxBodyId; // 售货机柜体ID
    private int serializeBoxBodyId;// 序列号柜体ID（后续去掉boxBodyId字段仅保留此字段）
    private int icsId;// 工控机ID
    private String virtualNumber;// 虚拟机号
    private String boxAddress;// 售货机部署地点
    private String latitude;// 售货机所在位置纬度
    private String longitude; // 售货机所在位置经度
    private Double distance; // 当前所在位置与数据库中经纬度点之前的距离
    private int locationClassification;// 地段等级
    private int networkType;// 网络方式 0:4G,1:WIFI,2:网线
    private int placeEnvironment;// 安放环境
    private int isOpen;// 安放环境是否开放
    private String environmentPicture;// 环境照片
    private int boxManagerId;// 设备管理员
    private int boxState;// 0:表示已撤柜，1：表示正常运营
    private int shopId;// 售货机店铺ID
    private Date placeDateTime;// 设备安放时间

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

    public String getBoxName() {
        return boxName;
    }

    public void setBoxName(String boxName) {
        this.boxName = boxName;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getBoxBodyId() {
        return boxBodyId;
    }

    public void setBoxBodyId(int boxBodyId) {
        this.boxBodyId = boxBodyId;
    }

    public int getSerializeBoxBodyId() {
        return serializeBoxBodyId;
    }

    public void setSerializeBoxBodyId(int serializeBoxBodyId) {
        this.serializeBoxBodyId = serializeBoxBodyId;
    }

    public int getIcsId() {
        return icsId;
    }

    public void setIcsId(int icsId) {
        this.icsId = icsId;
    }

    public String getVirtualNumber() {
        return virtualNumber;
    }

    public void setVirtualNumber(String virtualNumber) {
        this.virtualNumber = virtualNumber;
    }

    public String getBoxAddress() {
        return boxAddress;
    }

    public void setBoxAddress(String boxAddress) {
        this.boxAddress = boxAddress;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public int getLocationClassification() {
        return locationClassification;
    }

    public void setLocationClassification(int locationClassification) {
        this.locationClassification = locationClassification;
    }

    public int getNetworkType() {
        return networkType;
    }

    public void setNetworkType(int networkType) {
        this.networkType = networkType;
    }

    public int getPlaceEnvironment() {
        return placeEnvironment;
    }

    public void setPlaceEnvironment(int placeEnvironment) {
        this.placeEnvironment = placeEnvironment;
    }

    public int getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(int isOpen) {
        this.isOpen = isOpen;
    }

    public String getEnvironmentPicture() {
        return environmentPicture;
    }

    public void setEnvironmentPicture(String environmentPicture) {
        this.environmentPicture = environmentPicture;
    }

    public int getBoxManagerId() {
        return boxManagerId;
    }

    public void setBoxManagerId(int boxManagerId) {
        this.boxManagerId = boxManagerId;
    }

    public int getBoxState() {
        return boxState;
    }

    public void setBoxState(int boxState) {
        this.boxState = boxState;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public Date getPlaceDateTime() {
        return placeDateTime;
    }

    public void setPlaceDateTime(Date placeDateTime) {
        this.placeDateTime = placeDateTime;
    }

    @Override
    public String toString() {
        return "Boxes [id=" + id + ", boxId=" + boxId + ", boxName=" + boxName
                + ", customerId=" + customerId + ", boxBodyId=" + boxBodyId
                + ", serializeBoxBodyId=" + serializeBoxBodyId + ", icsId="
                + icsId + ", virtualNumber=" + virtualNumber + ", boxAddress="
                + boxAddress + ", latitude=" + latitude + ", longitude="
                + longitude + ", distance=" + distance
                + ", locationClassification=" + locationClassification
                + ", networkType=" + networkType + ", placeEnvironment="
                + placeEnvironment + ", isOpen=" + isOpen
                + ", environmentPicture=" + environmentPicture
                + ", boxManagerId=" + boxManagerId + ", boxState=" + boxState
                + ", shopId=" + shopId + ", placeDateTime=" + placeDateTime
                + "]";
    }

}
