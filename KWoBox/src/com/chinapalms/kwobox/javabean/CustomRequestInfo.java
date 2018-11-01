package com.chinapalms.kwobox.javabean;

public class CustomRequestInfo {

    private String boxId;// 售货机ID
    private String openDoorRequestSerial;// 商户服务器发送开门请求时携带的requestSerial参数

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    public String getOpenDoorRequestSerial() {
        return openDoorRequestSerial;
    }

    public void setOpenDoorRequestSerial(String openDoorRequestSerial) {
        this.openDoorRequestSerial = openDoorRequestSerial;
    }

    @Override
    public String toString() {
        return "CustomRequestInfo [boxId=" + boxId + ", openDoorRequestSerial="
                + openDoorRequestSerial + "]";
    }

}
