package com.chinapalms.kwobox.pay.wxpay.paybank;

import java.security.PublicKey;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.pay.wxpay.paywx.SSLHttpRequest;
import com.chinapalms.kwobox.pay.wxpay.paywx.WxPayWxConfig;
import com.chinapalms.kwobox.utils.ResponseStatus;
import com.chinapalms.kwobox.utils.Util;

public class WxPayBankService {

    Log log = LogFactory.getLog(WxPayBankService.class);

    public String getRASPubKey() {
        String result = ResponseStatus.FAIL;
        // 微信支付商户ID
        String mch_id = WxPayBankConfig.getMch_id();
        // 随机字符串
        String nonce_str = WxPayBankRandomStringGenerator
                .makeSystemCurrentTimeMillisStyleRandomString(32);
        // 签名类型
        String sign_type = "MD5";

        WxPayBankRSAPubKeyBean wxPayBankRSAPubKeyBean = new WxPayBankRSAPubKeyBean();
        wxPayBankRSAPubKeyBean.setMch_id(mch_id);
        wxPayBankRSAPubKeyBean.setNonce_str(nonce_str);
        wxPayBankRSAPubKeyBean.setSign_type(sign_type);

        // 签名
        String sign = null;
        try {
            sign = WxPayBankSignature.getSign(wxPayBankRSAPubKeyBean);
            wxPayBankRSAPubKeyBean.setSign(sign);

            result = SSLHttpRequest.sendPost(
                    "https://fraud.mch.weixin.qq.com/risk/getpublickey",
                    wxPayBankRSAPubKeyBean, getCertFilePath(),
                    WxPayWxConfig.getMch_id());
            System.out.println("result=" + result);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        return result;
    }

    public String doQueryPayBank(String partnerTradeNo) {
        String result = ResponseStatus.FAIL;
        try {
            // 微信支付分配的商户号
            String mch_id = WxPayBankConfig.getMch_id();
            // 商户订单号，需保持唯一（只允许数字[0~9]或字母[A~Z]和[a~z]最短8位，最长32位）
            String partner_trade_no = partnerTradeNo;
            // 随机字符串，长度小于32位
            String nonce_str = WxPayBankRandomStringGenerator
                    .makeSystemCurrentTimeMillisStyleRandomString(32);

            WxPayBankQueryPayBankBean wxPayBankQueryPayBankBean = new WxPayBankQueryPayBankBean();
            wxPayBankQueryPayBankBean.setMch_id(mch_id);
            wxPayBankQueryPayBankBean.setPartner_trade_no(partner_trade_no);
            wxPayBankQueryPayBankBean.setNonce_str(nonce_str);

            String sign = WxPayBankSignature.getSign(wxPayBankQueryPayBankBean);
            wxPayBankQueryPayBankBean.setSign(sign);

            result = SSLHttpRequest.sendPost(
                    "https://api.mch.weixin.qq.com/mmpaysptrans/query_bank",
                    wxPayBankQueryPayBankBean, getCertFilePath(),
                    WxPayWxConfig.getMch_id());
            System.out.println("result=" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String doWxPayBank(String encBankNoOrigin, String encTrueNameOrigin,
            String bankCode, int amountOrigin) {
        String result = ResponseStatus.FAIL;
        try {
            // 微信支付分配的商户号
            String mch_id = WxPayBankConfig.getMch_id();
            // 商户订单号，需保持唯一（只允许数字[0~9]或字母[A~Z]和[a~z]，最短8位，最长32位）
            String partner_trade_no = WxPayBankRandomStringGenerator
                    .makeDateStyleRandomString(32);
            // 随机字符串，不长于32位
            String nonce_str = WxPayBankRandomStringGenerator
                    .makeSystemCurrentTimeMillisStyleRandomString(32);
            // 收款方银行卡号（采用标准RSA算法，公钥由微信侧提供）,详见获取RSA加密公钥API
            // 定义自己公钥的路径
            String keyfile = "D:\\wangyi/tools/zhigoumao_pksc8_public.pem";
            // RSA工具类提供了，根据加载PKCS8密钥文件的方法
            PublicKey pub = RSAUtil.getPubKey(keyfile, "RSA");
            // rsa是微信付款到银行卡要求我们填充的字符串
            // eg：Java的填充方式要选 " RSA/ECB/OAEPWITHSHA-1ANDMGF1PADDING"
            String rsa = "RSA/ECB/OAEPWITHSHA-1ANDMGF1PADDING";
            // 进行加密,对银行账号进行base加密
            byte[] encryptBankNo = RSAUtil.encrypt(
                    encBankNoOrigin.getBytes("UTF-8"), pub, 2048, 11, rsa);
            String enc_bank_no = Base64.encode(encryptBankNo);
            System.out.println("enc_bank_no=" + enc_bank_no);
            // 收款方用户名（采用标准RSA算法，公钥由微信侧提供）详见获取RSA加密公钥API
            // 进行加密,对收款人银行卡账号姓名进行base加密
            byte[] encryptEncTrueName = RSAUtil.encrypt(
                    encTrueNameOrigin.getBytes("UTF-8"), pub, 2048, 11, rsa);
            String enc_true_name = Base64.encode(encryptEncTrueName);
            System.out.println("enc_true_name=" + enc_true_name);
            // 银行卡所在开户行编号,详见银行编号列表
            String bank_code = bankCode;
            // 付款金额：RMB分（支付总额，不含手续费） 注：大于0的整数
            int amount = amountOrigin;
            // 企业付款到银行卡付款说明,即订单备注（UTF8编码，允许100个字符以内
            String desc = new String(WxPayBankConfig.getDesc()
                    .getBytes("UTF-8"));

            WxPayBankBean wxPayBankBean = new WxPayBankBean();
            wxPayBankBean.setMch_id(mch_id);
            wxPayBankBean.setPartner_trade_no(partner_trade_no);
            wxPayBankBean.setNonce_str(nonce_str);
            wxPayBankBean.setEnc_bank_no(enc_bank_no);
            wxPayBankBean.setEnc_true_name(enc_true_name);
            wxPayBankBean.setBank_code(bank_code);
            wxPayBankBean.setAmount(amount);
            wxPayBankBean.setDesc(desc);

            // 通过MD5签名算法计算得出的签名值，详见MD5签名生成算法
            String sign = WxPayBankSignature.getSign(wxPayBankBean);
            wxPayBankBean.setSign(sign);

            result = SSLHttpRequest
                    .sendPost(
                            "https://api.mch.weixin.qq.com/mmpaysptrans/pay_bank",
                            wxPayBankBean, getCertFilePath(),
                            WxPayWxConfig.getMch_id());
            System.out.println("result=" + result);
            return null;
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
