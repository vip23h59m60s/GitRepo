package com.chinapalms.kwobox.pay.wxpay.paywx;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.awt.windows.WPageDialog;

import com.chinapalms.kwobox.pay.wxpay.papay.WxPapayRandomStringGenerator;
import com.chinapalms.kwobox.utils.ResponseStatus;
import com.chinapalms.kwobox.utils.Util;

public class WxPayWxService {

    Log log = LogFactory.getLog(WxPayWxService.class);

    // 查询转账状态
    public String doQueryPayWx(String partnerTradeNo) {
        String result = ResponseStatus.FAIL;
        try {
            // 商户号的appid
            String appid = WxPayWxConfig.getAppId();
            // 微信支付分配的商户号
            String mch_id = WxPayWxConfig.getMch_id();
            // 商户调用企业付款API时使用的商户订单号
            String partner_trade_no = partnerTradeNo;
            // 随机字符串，不长于32位
            String nonce_str = WxPayWxRandomStringGenerator
                    .makeSystemCurrentTimeMillisStyleRandomString(32);

            WxPayWxQueryPayWxBean wxPayWxQueryPayWxBean = new WxPayWxQueryPayWxBean();
            wxPayWxQueryPayWxBean.setAppid(appid);
            wxPayWxQueryPayWxBean.setMch_id(mch_id);
            wxPayWxQueryPayWxBean.setPartner_trade_no(partner_trade_no);
            wxPayWxQueryPayWxBean.setNonce_str(nonce_str);

            // 签名
            String sign = WxPayWxSignature.getSign(wxPayWxQueryPayWxBean);
            wxPayWxQueryPayWxBean.setSign(sign);

            result = SSLHttpRequest
                    .sendPost(
                            "https://api.mch.weixin.qq.com/mmpaymkttransfers/gettransferinfo",
                            wxPayWxQueryPayWxBean, getCertFilePath(),
                            WxPayWxConfig.getMch_id());
            System.out.println("result=" + result);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        return result;
    }

    // 商户微信支付钱款到个人微信
    public String doWxPayWx(String openId, int amount) {
        String result = ResponseStatus.FAIL;
        // 申请商户号的appid或商户号绑定的appid
        String mch_appid = WxPayWxConfig.getAppId();
        // 微信支付商户ID
        String mch_id = WxPayWxConfig.getMch_id();
        // 随机字符串
        String nonce_str = WxPayWxRandomStringGenerator
                .makeSystemCurrentTimeMillisStyleRandomString(32);
        // 商户订单号，需保持唯一性(只能是字母或者数字，不能包含有符号)
        String partner_trade_no = WxPapayRandomStringGenerator
                .makeDateStyleRandomString(32);
        // NO_CHECK：不校验真实姓名 FORCE_CHECK：强校验真实姓名
        String check_name = "NO_CHECK";
        // 企业付款操作说明信息。必填。
        String desc = WxPayWxConfig.getDesc();
        // 该IP同在商户平台设置的IP白名单中的IP没有关联，该IP可传用户端或者服务端的IP。
        String spbill_create_ip = ResponseStatus.SERVER_IP;
        // 签名，详见签名算法

        WxPayWxBean wxPayWxBean = new WxPayWxBean();
        wxPayWxBean.setMch_appid(mch_appid);
        wxPayWxBean.setMchid(mch_id);
        wxPayWxBean.setNonce_str(nonce_str);
        wxPayWxBean.setPartner_trade_no(partner_trade_no);
        wxPayWxBean.setOpenid(openId);
        wxPayWxBean.setCheck_name(check_name);
        wxPayWxBean.setAmount(amount);
        wxPayWxBean.setDesc(desc);
        wxPayWxBean.setSpbill_create_ip(spbill_create_ip);

        // 签名
        String sign = null;
        try {
            sign = WxPayWxSignature.getSign(wxPayWxBean);
            wxPayWxBean.setSign(sign);

            result = SSLHttpRequest
                    .sendPost(
                            "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers",
                            wxPayWxBean, getCertFilePath(),
                            WxPayWxConfig.getMch_id());
            System.out.println("result=" + result);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取支付p12 认证文件
     * 
     * @return
     */
    private String getCertFilePath() {
        String certPath = "";
        String thisPackagePath = this.getClass()
                .getResource("apiclient_cert.p12").toString();
        int m = thisPackagePath.indexOf("/");
        if (Util.isLinux()) {// 去掉路径钱的file字串
            certPath = thisPackagePath.substring(m);
        } else {
            certPath = thisPackagePath.substring(m + 1);
        }
        return certPath;
    }

}
