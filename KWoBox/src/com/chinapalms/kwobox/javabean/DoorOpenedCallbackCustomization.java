package com.chinapalms.kwobox.javabean;

public class DoorOpenedCallbackCustomization {

    private int customerId;// 商户ID
    private String boxId;// 售货机ID
    private String doorOpenedCallbackUrl;// 门已开回调Url

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

    public String getDoorOpenedCallbackUrl() {
        return doorOpenedCallbackUrl;
    }

    public void setDoorOpenedCallbackUrl(String doorOpenedCallbackUrl) {
        this.doorOpenedCallbackUrl = doorOpenedCallbackUrl;
    }

    @Override
    public String toString() {
        return "DoorOpenedCallbackCustomization [customerId=" + customerId
                + ", boxId=" + boxId + ", doorOpenedCallbackUrl="
                + doorOpenedCallbackUrl + "]";
    }

}
