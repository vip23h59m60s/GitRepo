package com.chinapalms.kwobox.javabean;

import java.util.Date;

public class SerializeBoxBody {

    private int serializeBoxBodyId;// 序列化boxBodyId
    private String boxBodySnNumber;// 柜体序列号
    private int boxBodyId;// 柜体型号id
    private int structureId;// 柜体层架货道结构ID
    private int workerId;// 操作员工ID
    private Date updateTime;// 更新时间

    public int getSerializeBoxBodyId() {
        return serializeBoxBodyId;
    }

    public void setSerializeBoxBodyId(int serializeBoxBodyId) {
        this.serializeBoxBodyId = serializeBoxBodyId;
    }

    public String getBoxBodySnNumber() {
        return boxBodySnNumber;
    }

    public void setBoxBodySnNumber(String boxBodySnNumber) {
        this.boxBodySnNumber = boxBodySnNumber;
    }

    public int getBoxBodyId() {
        return boxBodyId;
    }

    public void setBoxBodyId(int boxBodyId) {
        this.boxBodyId = boxBodyId;
    }

    public int getStructureId() {
        return structureId;
    }

    public void setStructureId(int structureId) {
        this.structureId = structureId;
    }

    public int getWorkerId() {
        return workerId;
    }

    public void setWorkerId(int workerId) {
        this.workerId = workerId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "SerializeBoxBody [serializeBoxBodyId=" + serializeBoxBodyId
                + ", boxBodySnNumber=" + boxBodySnNumber + ", boxBodyId="
                + boxBodyId + ", structureId=" + structureId + ", workerId="
                + workerId + ", updateTime=" + updateTime + "]";
    }

}
