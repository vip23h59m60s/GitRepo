package com.chinapalms.kwobox.javabean;

public class DoorClosedCallbackCustomization {

    private int customerId;// 商户ID
    private String boxId;// 售货机ID
    private String doorClosedCallbackUrl;// 门已关回调Url

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    public String getDoorClosedCallbackUrl() {
        return doorClosedCallbackUrl;
    }

    public void setDoorClosedCallbackUrl(String doorClosedCallbackUrl) {
        this.doorClosedCallbackUrl = doorClosedCallbackUrl;
    }

    @Override
    public String toString() {
        return "DoorClosedCallbackCustomization [customerId=" + customerId
                + ", boxId=" + boxId + ", doorClosedCallbackUrl="
                + doorClosedCallbackUrl + "]";
    }

}
