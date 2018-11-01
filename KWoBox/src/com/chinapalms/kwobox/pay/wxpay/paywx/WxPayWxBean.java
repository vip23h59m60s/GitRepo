package com.chinapalms.kwobox.pay.wxpay.paywx;

import sun.security.krb5.internal.crypto.DesCbcCrcEType;

public class WxPayWxBean {

    private String mch_appid;// 申请商户号的appid或商户号绑定的appid
    private String mchid;// 微信支付分配的商户号
    private String nonce_str;// 随机字符串，不长于32位
    private String partner_trade_no;// 商户订单号，需保持唯一性(只能是字母或者数字，不能包含有符号)
    private String openid;// 商户appid下，某用户的openid
    private String check_name;// NO_CHECK：不校验真实姓名 FORCE_CHECK：强校验真实姓名
    private int amount;// 企业付款金额，单位为分
    private String desc;// 企业付款操作说明信息。必填。
    private String spbill_create_ip;// 该IP同在商户平台设置的IP白名单中的IP没有关联，该IP可传用户端或者服务端的IP。
    private String sign;// 签名，详见签名算法

    public String getMch_appid() {
        return mch_appid;
    }

    public void setMch_appid(String mch_appid) {
        this.mch_appid = mch_appid;
    }

    public String getMchid() {
        return mchid;
    }

    public void setMchid(String mchid) {
        this.mchid = mchid;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getPartner_trade_no() {
        return partner_trade_no;
    }

    public void setPartner_trade_no(String partner_trade_no) {
        this.partner_trade_no = partner_trade_no;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getCheck_name() {
        return check_name;
    }

    public void setCheck_name(String check_name) {
        this.check_name = check_name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSpbill_create_ip() {
        return spbill_create_ip;
    }

    public void setSpbill_create_ip(String spbill_create_ip) {
        this.spbill_create_ip = spbill_create_ip;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "WxPayWxBean [mch_appid=" + mch_appid + ", mchid=" + mchid
                + ", nonce_str=" + nonce_str + ", partner_trade_no="
                + partner_trade_no + ", openid=" + openid + ", check_name="
                + check_name + ", amount=" + amount + ", desc=" + desc
                + ", spbill_create_ip=" + spbill_create_ip + ", sign=" + sign
                + "]";
    }

}
