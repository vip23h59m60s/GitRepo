package com.chinapalms.kwobox.javabean;

public class DynamicPasswordCallback {

    private int customerId;// 对接第三方商户商户ID
    private String dynamicPasswordCallbackUrl;// 对接第三方商户的动态密码回调Url

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getDynamicPasswordCallbackUrl() {
        return dynamicPasswordCallbackUrl;
    }

    public void setDynamicPasswordCallbackUrl(String dynamicPasswordCallbackUrl) {
        this.dynamicPasswordCallbackUrl = dynamicPasswordCallbackUrl;
    }

    @Override
    public String toString() {
        return "DynamicpasswordCallback [customerId=" + customerId
                + ", dynamicPasswordCallbackUrl=" + dynamicPasswordCallbackUrl
                + "]";
    }

}
