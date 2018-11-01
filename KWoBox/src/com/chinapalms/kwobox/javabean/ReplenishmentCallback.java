package com.chinapalms.kwobox.javabean;

/**
 * @author wangyi
 * 
 */
public class ReplenishmentCallback {

    private int customerId;// 商户ID
    private String replenishmentCallbackUrl;// 商户理货单回调Url

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getReplenishmentCallbackUrl() {
        return replenishmentCallbackUrl;
    }

    public void setReplenishmentCallbackUrl(String replenishmentCallbackUrl) {
        this.replenishmentCallbackUrl = replenishmentCallbackUrl;
    }

    @Override
    public String toString() {
        return "ReplenishmentCallback [customerId=" + customerId
                + ", replenishmentCallbackUrl=" + replenishmentCallbackUrl
                + "]";
    }

}
