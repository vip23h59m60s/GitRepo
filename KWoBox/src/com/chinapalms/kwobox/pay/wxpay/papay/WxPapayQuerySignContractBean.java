package com.chinapalms.kwobox.pay.wxpay.papay;

public class WxPapayQuerySignContractBean {

    // 微信支付分配的公众账号id
    private String appid;
    // 微信支付分配的商户号
    private String mch_id;
    // 免密委托代扣协议模板id
    private String plan_id;
    // 用户openid
    private String openid;
    // 微信支付规定固定值1.0
    private String version;
    // 签名
    private String sign;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(String plan_id) {
        this.plan_id = plan_id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "WxPapayQuerySignContractBean [appid=" + appid + ", mch_id="
                + mch_id + ", plan_id=" + plan_id + ", openid=" + openid
                + ", version=" + version + ", sign=" + sign + "]";
    }
}
