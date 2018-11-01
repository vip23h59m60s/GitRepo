package com.chinapalms.kwobox.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;

//http://sms.webchinese.cn/  中国网建短信通
public class IdentifyCodeUtil {

    private static Log log = LogFactory.getLog(IdentifyCodeUtil.class);

    public static boolean sendIdentifyCode(String phoneNumber,
            String identifyCode) {
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod("http://utf8.api.smschinese.cn/");
        post.addRequestHeader("Content-Type",
                "application/x-www-form-urlencoded;charset=utf-8");
        NameValuePair[] data = {
                new NameValuePair("Uid", "vip23h59m60s"),
                new NameValuePair("Key", "070ddafa1c2c6496fcce"),
                new NameValuePair("smsMob", phoneNumber),
                new NameValuePair("smsText", "验证码：" + identifyCode
                        + "，10分钟内输入有效，立即登录") };
        post.setRequestBody(data);

        try {
            client.executeMethod(post);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        Header[] headers = post.getResponseHeaders();
        int statusCode = post.getStatusCode();
        for (Header h : headers) {
        }
        String result = null;
        try {
            result = new String(post.getResponseBodyAsString()
                    .getBytes("utf-8"));
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        post.releaseConnection();
        if (result != null && Integer.valueOf(result) > 0) {
            return true;
        } else {
            return false;
        }
    }

    // 腾讯短信验证码
    public static boolean sendTencentQQIdentifyCodeSms(String phoneNumber,
            String identifyCode, int timeOutMinutes) {
        try {
            SmsSingleSender smsSingleSender = new SmsSingleSender(1400066984,
                    "35859c9468c6b195b794b531ea114735");
            SmsSingleSenderResult result = smsSingleSender.send(0, "86",
                    phoneNumber, "验证码：" + identifyCode + "，请于" + timeOutMinutes
                            + "分钟内输入。如非本人操作，请忽略本短信。", "", "");
            if (result != null && result.errMsg.equals("OK")) {
                log.info("sendTencentQQIdentifyCodeSms->SUCCESS"
                        + result.errMsg);
                return true;
            } else {
                log.error("sendTencentQQIdentifyCodeSms->FAIL" + result.errMsg);
                return false;
            }
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        return false;
    }

    // 通过腾讯发送短信进行征信通知
    public static boolean sendTencentQQPersonalCreditSms(String phoneNumber,
            int pesonalCredit) {
        try {
            SmsSingleSender smsSingleSender = new SmsSingleSender(1400066984,
                    "35859c9468c6b195b794b531ea114735");
            SmsSingleSenderResult result = smsSingleSender.send(0, "86",
                    phoneNumber, "你好，" + phoneNumber.substring(0, 3) + "****"
                            + phoneNumber.substring(7)
                            + "，智购猫已检测到你有异常购物行为，扣除征信分" + pesonalCredit
                            + "分，请注意文明购物！", "", "");
            if (result != null && result.errMsg.equals("OK")) {
                log.info("sendTencentQQPersonalCreditSms->SUCCESS"
                        + result.errMsg);
                return true;
            } else {
                log.error("sendTencentQQPersonalCreditSms->FAIL"
                        + result.errMsg);
                return false;
            }
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        return false;
    }

    // 通过腾讯发送短信进行征信通知
    public static boolean sendTencentQQDeviceNotChargingSms(String phoneNumber,
            String boxName) {
        try {
            SmsSingleSender smsSingleSender = new SmsSingleSender(1400066984,
                    "35859c9468c6b195b794b531ea114735");
            SmsSingleSenderResult result = smsSingleSender.send(0, "86",
                    phoneNumber, "注意：[" + boxName + "]已处于停止充电异常状态，请尽快安排检修！",
                    "", "");
            if (result != null && result.errMsg.equals("OK")) {
                log.info("sendTencentQQDeviceNotChargingSms->SUCCESS"
                        + result.errMsg);
                return true;
            } else {
                log.error("sendTencentQQDeviceNotChargingSms->FAIL"
                        + result.errMsg);
                return false;
            }
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        return false;
    }

    // 通过腾讯发送短信进行通知售货机管理员因异常购物行为导致货柜混乱，需要重新进行理货操作
    public static boolean sendTencentQQToBoxManagerToManageExceptionShoppingBox(
            String phoneNumber, String boxName) {
        try {
            SmsSingleSender smsSingleSender = new SmsSingleSender(1400066984,
                    "35859c9468c6b195b794b531ea114735");
            SmsSingleSenderResult result = smsSingleSender.send(0, "86",
                    phoneNumber, "智购猫温馨提示：[" + boxName
                            + "]因消费者异常购物行为导致售货机无法正常运营，已将售货机设为保护状态，请尽快检查并重新理货！",
                    "", "");
            if (result != null && result.errMsg.equals("OK")) {
                log.info("sendTencentQQToBoxManagerToManageExceptionShoppingBox->SUCCESS"
                        + result.errMsg);
                return true;
            } else {
                log.error("sendTencentQQToBoxManagerToManageExceptionShoppingBox->FAIL"
                        + result.errMsg);
                return false;
            }
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        return false;
    }

    // 向指定手机号码发送短信
    public static boolean sendSMS(int templateId, String phoneNumber,
            String smsContent) {
        try {
            ArrayList<String> params = new ArrayList<>();// 数组具体的元素个数和模板中变量个数必须一致，例如事例中templateId:5678对应一个变量，参数数组中元素个数也必须是一个
            params.add("1234");
            params.add("5678");
            SmsSingleSender ssender = new SmsSingleSender(1400066984,
                    "35859c9468c6b195b794b531ea114735");
            SmsSingleSenderResult result = ssender.sendWithParam("86",
                    phoneNumber, /* templateId */98374, params, "智购猫", "", ""); // 签名参数未提供或者为空时，会使用默认签名发送短信
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
