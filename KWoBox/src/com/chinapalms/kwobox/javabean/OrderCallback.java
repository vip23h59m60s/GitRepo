package com.chinapalms.kwobox.javabean;

public class OrderCallback {

    private int customerId;// 商户ID
    private String orderCallbackUrl;// 回调给商户订单的Url

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getOrderCallbackUrl() {
        return orderCallbackUrl;
    }

    public void setOrderCallbackUrl(String orderCallbackUrl) {
        this.orderCallbackUrl = orderCallbackUrl;
    }

    @Override
    public String toString() {
        return "OrderCallback [customerId=" + customerId
                + ", orderCallbackUrl=" + orderCallbackUrl + "]";
    }

}
