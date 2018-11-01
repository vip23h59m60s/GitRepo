package com.chinapalms.kwobox.javabean;

public class QRCodeContentCustomer {

    private int customerId;// 商户ID
    private String qRCodeContentPrefix;// 商户二维码内容前缀

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getqRCodeContentPrefix() {
        return qRCodeContentPrefix;
    }

    public void setqRCodeContentPrefix(String qRCodeContentPrefix) {
        this.qRCodeContentPrefix = qRCodeContentPrefix;
    }

    @Override
    public String toString() {
        return "QRCodeContentCustomer [customerId=" + customerId
                + ", qRCodeContentPrefix=" + qRCodeContentPrefix + "]";
    }

}
