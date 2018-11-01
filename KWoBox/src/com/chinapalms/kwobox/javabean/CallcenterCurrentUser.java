package com.chinapalms.kwobox.javabean;

public class CallcenterCurrentUser {

    private String boxId;// 异常监控人员当前处理的柜子
    private int callCenterId;// 异常监控人员id

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    public int getCallCenterId() {
        return callCenterId;
    }

    public void setCallCenterId(int callCenterId) {
        this.callCenterId = callCenterId;
    }

    @Override
    public String toString() {
        return "callcenterCurrentUser [boxId=" + boxId + ", callCenterId="
                + callCenterId + "]";
    }

}
