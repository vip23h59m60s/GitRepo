package com.chinapalms.kwobox.javabean;

public class DoorOpenedCallback {

    private int customerId;// 商户ID
    private String doorOpenedCallbackUrl;// 门已开回调Url

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getDoorOpenedCallbackUrl() {
        return doorOpenedCallbackUrl;
    }

    public void setDoorOpenedCallbackUrl(String doorOpenedCallbackUrl) {
        this.doorOpenedCallbackUrl = doorOpenedCallbackUrl;
    }

    @Override
    public String toString() {
        return "DoorOpenedCallback [customerId=" + customerId
                + ", doorOpenedCallbackUrl=" + doorOpenedCallbackUrl + "]";
    }

}
