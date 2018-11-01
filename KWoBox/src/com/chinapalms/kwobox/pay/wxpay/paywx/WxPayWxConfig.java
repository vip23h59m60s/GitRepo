package com.chinapalms.kwobox.pay.wxpay.paywx;

public class WxPayWxConfig {

    // zigoomall
    // 商户后台管理生成的支付相关API安全密钥
    // private static String key = "y54ye3gv2dcx24o6hycf58117b7c0r21";
    // zhigoumao
    // 商户后台管理生成的支付相关API安全密钥
    private static String key = "lnkpx825vhjznsvb83xp0i627cpey0xj";

    // 小程序ID(zigoomall)
    // private static String appId = "wx025f17836cef3782";
    // 小程序ID(zhigoumao)
    private static String appId = "wx54b6bc5da4f33b7f";
    // 商户号(zigoomall)
    // private static String mch_id = "1489283872";
    // 商户号(zhigoumao)
    private static String mch_id = "1496730392";

    // 小程序密钥密钥(zigoomall)
    // private static String secret = "64fb73c0198db861b75444fab37cf0e9";
    // 小程序密钥密钥(zhigoumao)
    private static String secret = "379244a2d0772fb7211362b46b7bb0c9";

    // 微信支付协议模板id(zigoomall)
    // private static String plan_id = "93604";
    // 微信支付协议模板id(zhigoumao)
    private static String plan_id = "115612";

    // 用户账户展示名称
    private static String contract_display_account = "智购猫便捷支付";

    // 用于接收签约成功消息的回调通知地址，以http或https开头(zigoomall)
    // private static String entrust_notify_url =
    // "https://www.zigoomall.com/KWoBox/wxPapayEntrustReturn.jsp";
    // 用于接收签约成功消息的回调通知地址，以http或https开头(zhigoumao)
    private static String entrust_notify_url = "https://www.zigoomo.com/KWoBox/wxPapayEntrustReturn.jsp";

    // 用于接收支付成功消息的回调通知地址，以http或https开头(zigoomall)
    // private static String pay_success_notify_url =
    // "https://www.zigoomall.com/KWoBox/wxPapayPaySuccesstReturn.jsp";
    // 用于接收支付成功消息的回调通知地址，以http或https开头(zhigoumao)
    private static String pay_success_notify_url = "https://www.zigoomo.com/KWoBox/wxPapayPaySuccesstReturn.jsp";

    // 微信小程序推送模板消息ID
    private static String templateMsgId = "LH_aDI3ZMnIFhssI7wK1Gxv9apT4-5fwa0DhXJ-hKfw";

    // 点击微信小程序模板消息面板后跳转页面
    private static String miniProgramPage = "myorder";

    // 企业付款操作说明信息。必填。
    private static String desc = "智购猫分账";

    public static String getDesc() {
        return desc;
    }

    public static void setDesc(String desc) {
        WxPayWxConfig.desc = desc;
    }

    public static String getKey() {
        return key;
    }

    public static void setKey(String key) {
        WxPayWxConfig.key = key;
    }

    public static String getAppId() {
        return appId;
    }

    public static void setAppId(String appId) {
        WxPayWxConfig.appId = appId;
    }

    public static String getMch_id() {
        return mch_id;
    }

    public static void setMch_id(String mch_id) {
        WxPayWxConfig.mch_id = mch_id;
    }

    public static String getSecret() {
        return secret;
    }

    public static void setSecret(String secret) {
        WxPayWxConfig.secret = secret;
    }

    public static String getPlan_id() {
        return plan_id;
    }

    public static void setPlan_id(String plan_id) {
        WxPayWxConfig.plan_id = plan_id;
    }

    public static String getContract_display_account() {
        return contract_display_account;
    }

    public static void setContract_display_account(
            String contract_display_account) {
        WxPayWxConfig.contract_display_account = contract_display_account;
    }

    public static String getEntrust_notify_url() {
        return entrust_notify_url;
    }

    public static void setEntrust_notify_url(String entrust_notify_url) {
        WxPayWxConfig.entrust_notify_url = entrust_notify_url;
    }

    public static String getPay_success_notify_url() {
        return pay_success_notify_url;
    }

    public static void setPay_success_notify_url(String pay_success_notify_url) {
        WxPayWxConfig.pay_success_notify_url = pay_success_notify_url;
    }

    public static String getTemplateMsgId() {
        return templateMsgId;
    }

    public static void setTemplateMsgId(String templateMsgId) {
        WxPayWxConfig.templateMsgId = templateMsgId;
    }

    public static String getMiniProgramPage() {
        return miniProgramPage;
    }

    public static void setMiniProgramPage(String miniProgramPage) {
        WxPayWxConfig.miniProgramPage = miniProgramPage;
    }

}
