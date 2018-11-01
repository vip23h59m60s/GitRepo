package com.chinapalms.kwobox.javabean;

public class OrderCallbackCustomization {

    private int customerId;// 商户ID
    private String boxId;// 售货机ID
    private String orderCallbackUrl;// 回调给商户订单的Url

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

    public String getOrderCallbackUrl() {
        return orderCallbackUrl;
    }

    public void setOrderCallbackUrl(String orderCallbackUrl) {
        this.orderCallbackUrl = orderCallbackUrl;
    }

    @Override
    public String toString() {
        return "OrderCallbackCustomization [customerId=" + customerId
                + ", boxId=" + boxId + ", orderCallbackUrl=" + orderCallbackUrl
                + "]";
    }

}
