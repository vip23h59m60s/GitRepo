package com.chinapalms.kwobox.pay.wxpay.paywx;

public class WxPayWxQueryPayWxBean {

    private String mch_id;// 微信支付分配的商户号
    private String partner_trade_no;// 商户调用企业付款API时使用的商户订单号
    private String nonce_str;// 随机字符串，不长于32位
    private String appid;// 商户号的appid
    private String sign;// 生成签名方式查看3.2.1节

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getPartner_trade_no() {
        return partner_trade_no;
    }

    public void setPartner_trade_no(String partner_trade_no) {
        this.partner_trade_no = partner_trade_no;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "WxPayWxQueryPayWxBean [mch_id=" + mch_id
                + ", partner_trade_no=" + partner_trade_no + ", nonce_str="
                + nonce_str + ", appid=" + appid + ", sign=" + sign + "]";
    }

}
