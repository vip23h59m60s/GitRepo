package com.chinapalms.kwobox.pay.wxpay.papay;

public class WxPapayApplyBean {

    // 公众账号id
    private String appid;
    // 商户号
    private String mch_id;
    // 随机字符串
    private String nonce_str;
    // 签名
    private String sign;
    // 商品描述
    private String body;
    // 商户订单号
    private String out_trade_no;
    // 总金额
    private int total_fee;
    // 终端IP
    private String spbill_create_ip;
    // 回调通知url
    private String notify_url;
    // 交易类型
    private String trade_type;
    // 委托代扣协议id
    private String contract_id;

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

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public int getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(int total_fee) {
        this.total_fee = total_fee;
    }

    public String getSpbill_create_ip() {
        return spbill_create_ip;
    }

    public void setSpbill_create_ip(String spbill_create_ip) {
        this.spbill_create_ip = spbill_create_ip;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getContract_id() {
        return contract_id;
    }

    public void setContract_id(String contract_id) {
        this.contract_id = contract_id;
    }

    @Override
    public String toString() {
        return "WxPapayApplyBean [appid=" + appid + ", mch_id=" + mch_id
                + ", nonce_str=" + nonce_str + ", sign=" + sign + ", body="
                + body + ", out_trade_no=" + out_trade_no + ", total_fee="
                + total_fee + ", spbill_create_ip=" + spbill_create_ip
                + ", notify_url=" + notify_url + ", trade_type=" + trade_type
                + ", contract_id=" + contract_id + "]";
    }
}
