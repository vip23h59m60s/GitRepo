package com.chinapalms.kwobox.javabean;

/**
 * @author wangyi
 * 
 */
public class ReplenishmentCallbackCustomization {

    private int customerId;// 商户ID
    private String boxId; // 售货机ID
    private String replenishmentCallbackUrl;// 商户理货单回调Url

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

    public String getReplenishmentCallbackUrl() {
        return replenishmentCallbackUrl;
    }

    public void setReplenishmentCallbackUrl(String replenishmentCallbackUrl) {
        this.replenishmentCallbackUrl = replenishmentCallbackUrl;
    }

    @Override
    public String toString() {
        return "ReplenishmentCallbackCustomization [customerId=" + customerId
                + ", boxId=" + boxId + ", replenishmentCallbackUrl="
                + replenishmentCallbackUrl + "]";
    }

}
