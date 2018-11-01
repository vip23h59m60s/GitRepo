package com.chinapalms.kwobox.pay.alipay.papay;

public class AlipayPaypayConfig {

    // 签约账号id
    private static String partner = "2088821272260578";
    // 商户的私钥
    private static String key = "cutecyoq8uk9yvhszub3lqbvs5uwmd3v";

    // 请求签约回调Url
    private static String entrust_notify_url = "https://www.zigoomall.com/KWoBox/alipayPapayEntrustReturn.jsp";
    private static String GET_USERINFO_REDIRECT_URI = "https://www.zigoomall.com/KWoBox/KWoBoxServelet";

    public static String getPartner() {
        return partner;
    }

    public static void setPartner(String partner) {
        AlipayPaypayConfig.partner = partner;
    }

    public static String getKey() {
        return key;
    }

    public static void setKey(String key) {
        AlipayPaypayConfig.key = key;
    }

    public static String getGET_USERINFO_REDIRECT_URI() {
        return GET_USERINFO_REDIRECT_URI;
    }

    public static void setGET_USERINFO_REDIRECT_URI(
            String gET_USERINFO_REDIRECT_URI) {
        GET_USERINFO_REDIRECT_URI = gET_USERINFO_REDIRECT_URI;
    }

    public static String getEntrust_notify_url() {
        return entrust_notify_url;
    }

    public static void setEntrust_notify_url(String entrust_notify_url) {
        AlipayPaypayConfig.entrust_notify_url = entrust_notify_url;
    }

}
