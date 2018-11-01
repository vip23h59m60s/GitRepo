package com.chinapalms.kwobox.pay.wxpay.paybank;

public class WxPayBankRSAPubKeyBean {

    // 支付商户ID
    private String mch_id;
    // 随机子串，用于生成签名
    private String nonce_str;
    // 签名类型
    private String sign_type;
    // 签名
    private String sign;

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "WxPayBankRSAPubKeyBean [mch_id=" + mch_id + ", nonce_str="
                + nonce_str + ", sign_type=" + sign_type + ", sign=" + sign
                + "]";
    }

}
