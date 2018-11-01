package com.chinapalms.kwobox.wxjssdk;

public class WxJsSDKConfigure {
    private static String key = "lnkpx825vhjznsvb83xp0i627cpey0xj";

    // 微信小程序AppID
    private static String appID = "wx54b6bc5da4f33b7f";
    // 商户号
    private static String mch_id = "1496730392";
    //
    private static String secret = "379244a2d0772fb7211362b46b7bb0c9";

    private static String goodsOwner = "Zigoomall";

    public static String getSecret() {
        return secret;
    }

    public static void setSecret(String secret) {
        WxJsSDKConfigure.secret = secret;
    }

    public static String getKey() {
        return key;
    }

    public static void setKey(String key) {
        WxJsSDKConfigure.key = key;
    }

    public static String getAppID() {
        return appID;
    }

    public static void setAppID(String appID) {
        WxJsSDKConfigure.appID = appID;
    }

    public static String getMch_id() {
        return mch_id;
    }

    public static void setMch_id(String mch_id) {
        WxJsSDKConfigure.mch_id = mch_id;
    }

    public static String getGoodsOwner() {
        return goodsOwner;
    }

    public static void setGoodsOwner(String goodsOwner) {
        WxJsSDKConfigure.goodsOwner = goodsOwner;
    }

}
