package com.chinapalms.kwobox.javabean;

public class CustomerSecretKey {

    private int customerId;// 商户ID
    private String secretKey;// 商户API秘钥

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public String toString() {
        return "CustomerSecretKey [customerId=" + customerId + ", secretKey="
                + secretKey + "]";
    }

}
