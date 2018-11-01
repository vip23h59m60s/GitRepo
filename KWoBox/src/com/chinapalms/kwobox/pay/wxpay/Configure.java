package com.chinapalms.kwobox.pay.wxpay;

public class Configure {
    // zigoomall
    // 商户后台管理生成的支付相关API安全密钥
    // private static String key = "y54ye3gv2dcx24o6hycf58117b7c0r21";
    // zhigoumao
    // 商户后台管理生成的支付相关API安全密钥
    private static String key = "lnkpx825vhjznsvb83xp0i627cpey0xj";

    // 小程序ID(zigoomall)
    // private static String appID = "wx025f17836cef3782";
    // 小程序ID(zhigoumao)
    private static String appID = "wx54b6bc5da4f33b7f";
    // 商户号(zigoomall)
    // private static String mch_id = "1489283872";
    // 商户号(zhigoumao)
    private static String mch_id = "1496730392";
    // 小程序密钥密钥(zigoomall)
    // private static String secret = "64fb73c0198db861b75444fab37cf0e9";
    // 小程序密钥密钥(zhigoumao)
    private static String secret = "379244a2d0772fb7211362b46b7bb0c9";

    private static String goodsOwner = "智购猫商品";

    // 支付成功回调url
    private static String notify_url = "https://www.zigoomo.com/KWoBox/wxPayPaySuccesstReturn.jsp";

    public static String getKey() {
        return key;
    }

    public static void setKey(String key) {
        Configure.key = key;
    }

    public static String getAppID() {
        return appID;
    }

    public static void setAppID(String appID) {
        Configure.appID = appID;
    }

    public static String getMch_id() {
        return mch_id;
    }

    public static void setMch_id(String mch_id) {
        Configure.mch_id = mch_id;
    }

    public static String getSecret() {
        return secret;
    }

    public static void setSecret(String secret) {
        Configure.secret = secret;
    }

    public static String getGoodsOwner() {
        return goodsOwner;
    }

    public static void setGoodsOwner(String goodsOwner) {
        Configure.goodsOwner = goodsOwner;
    }

    public static String getNotify_url() {
        return notify_url;
    }

    public static void setNotify_url(String notify_url) {
        Configure.notify_url = notify_url;
    }

}
