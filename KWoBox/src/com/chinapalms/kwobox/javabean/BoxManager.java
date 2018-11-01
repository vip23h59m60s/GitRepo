package com.chinapalms.kwobox.javabean;

import java.util.Date;

public class BoxManager {

    public static final int PAGE_SIZE = 5;

    private int id;
    private String boxId;// 售货机ID
    private int boxManagerId;// 管理员ID
    private int boxDeliveryId;// 售货机送货员ID
    private int maintainerId;// 设备维护员ID
    private int dataManagerId;// 数据管理员ID
    private int customManagerId;// 商户系统管理员ID
    private Date updateDateTime;// 更新时间

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

    public int getBoxManagerId() {
        return boxManagerId;
    }

    public void setBoxManagerId(int boxManagerId) {
        this.boxManagerId = boxManagerId;
    }

    public int getBoxDeliveryId() {
        return boxDeliveryId;
    }

    public void setBoxDeliveryId(int boxDeliveryId) {
        this.boxDeliveryId = boxDeliveryId;
    }

    public int getMaintainerId() {
        return maintainerId;
    }

    public void setMaintainerId(int maintainerId) {
        this.maintainerId = maintainerId;
    }

    public int getDataManagerId() {
        return dataManagerId;
    }

    public void setDataManagerId(int dataManagerId) {
        this.dataManagerId = dataManagerId;
    }

    public int getCustomManagerId() {
        return customManagerId;
    }

    public void setCustomManagerId(int customManagerId) {
        this.customManagerId = customManagerId;
    }

    public Date getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(Date updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    @Override
    public String toString() {
        return "BoxManager [id=" + id + ", boxId=" + boxId + ", boxManagerId="
                + boxManagerId + ", boxDeliveryId=" + boxDeliveryId
                + ", maintainerId=" + maintainerId + ", dataManagerId="
                + dataManagerId + ", customManagerId=" + customManagerId
                + ", updateDateTime=" + updateDateTime + "]";
    }

}
