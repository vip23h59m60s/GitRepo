package com.chinapalms.kwobox.pay.alipay.papay;

public class AlipayPaypayEntrustBean {

    private String service;
    private String partner;
    private String sign;
    private String _input_charset;
    private String notify_url;
    private String product_code;
    private String access_info;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String get_input_charset() {
        return _input_charset;
    }

    public void set_input_charset(String _input_charset) {
        this._input_charset = _input_charset;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getAccess_info() {
        return access_info;
    }

    public void setAccess_info(String access_info) {
        this.access_info = access_info;
    }

    @Override
    public String toString() {
        return "AlipayPaypayEntrustBean [service=" + service + ", partner="
                + partner + ", sign=" + sign + ", _input_charset="
                + _input_charset + ", notify_url=" + notify_url
                + ", product_code=" + product_code + ", access_info="
                + access_info + "]";
    }

}
