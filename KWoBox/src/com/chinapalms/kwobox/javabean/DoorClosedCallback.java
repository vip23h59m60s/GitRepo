package com.chinapalms.kwobox.javabean;

public class DoorClosedCallback {

    private int customerId;// 商户ID
    private String doorClosedCallbackUrl;// 门已关回调Url

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getDoorClosedCallbackUrl() {
        return doorClosedCallbackUrl;
    }

    public void setDoorClosedCallbackUrl(String doorClosedCallbackUrl) {
        this.doorClosedCallbackUrl = doorClosedCallbackUrl;
    }

    @Override
    public String toString() {
        return "DoorClosedCallback [customerId=" + customerId
                + ", doorClosedCallbackUrl=" + doorClosedCallbackUrl + "]";
    }

}
