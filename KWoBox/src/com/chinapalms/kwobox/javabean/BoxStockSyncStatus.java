package com.chinapalms.kwobox.javabean;

public class BoxStockSyncStatus {

    private String boxId;// 售货机ID
    private int state;// 异常视频处理后库存状态同步1:表示已失败0：表示未同步;2,表示同步完成

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

    @Override
    public String toString() {
        return "BoxStockSyncStatus [boxId=" + boxId + ", state=" + state + "]";
    }

}
