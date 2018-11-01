package com.chinapalms.kwobox.pay.wxpay.papay;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.chinapalms.kwobox.javabean.Order;
import com.chinapalms.kwobox.pay.wxpay.Configure;
import com.chinapalms.kwobox.pay.wxpay.HttpRequest;
import com.chinapalms.kwobox.service.TokenService;
import com.chinapalms.kwobox.service.UserService;
import com.chinapalms.kwobox.utils.ResponseStatus;
import com.chinapalms.kwobox.utils.Util;

public class WxPapayService {

    Log log = LogFactory.getLog(WxPapayService.class);

    // 申请签约
    public String doGetEntrustRequestUrl() {

        String requestUrl = ResponseStatus.FAIL;
        try {
            // 微信支付分配的公众账号id
            String appid = WxPaypayConfig.getAppId();
            // 分别生成20位签约协议号和请求序列号
            // 商户侧的签约协议号，由商户生成
            String contract_code = WxPapayRandomStringGenerator
                    .makeSystemCurrentTimeMillisStyleRandomString(18);
            // 商户请求签约时的序列号，商户侧须唯一。序列号主要用于排序，不作为查询条件
            String request_serial = WxPapayRandomStringGenerator
                    .makeSystemCurrentTimeMillisStyleRandomString(18);
            // 微信支付分配的商户号
            String mch_id = WxPaypayConfig.getMch_id();
            // 免密委托代扣协议模板id
            String plan_id = WxPaypayConfig.getPlan_id();
            // 签约用户的名称，用于页面展示
            String contract_display_account = WxPaypayConfig
                    .getContract_display_account();
            // 用于接收签约成功消息的回调通知地址，以http或https开头
            String notify_url = WxPaypayConfig.getEntrust_notify_url();
            // 系统当前时间，10位
            String timestamp = String
                    .valueOf(System.currentTimeMillis() / 1000);
            // 微信支付规定固定值1.0
            String version = "1.0";

            // 这些参数按照签名规则必须按照参数名称的ASCII码排序
            Map<String, Object> signMap = new HashMap<String, Object>();
            signMap.put("appid", appid);
            signMap.put("contract_code", contract_code);
            signMap.put("contract_display_account", contract_display_account);
            signMap.put("mch_id", mch_id);
            signMap.put("notify_url", notify_url);
            signMap.put("plan_id", plan_id);
            signMap.put("request_serial", request_serial);
            signMap.put("timestamp", timestamp);
            signMap.put("version", version);

            String sign = WxPaypaySignature.getSign(signMap);

            requestUrl = "https://api.mch.weixin.qq.com/papay/entrustweb?"
                    + "appid=" + appid + "&contract_code=" + contract_code
                    + "&contract_display_account=" + contract_display_account
                    + "&mch_id=" + mch_id + "&notify_url="
                    + URLEncoder.encode(notify_url, "utf-8") + "&plan_id="
                    + plan_id + "&request_serial=" + request_serial
                    + "&timestamp=" + timestamp + "&version=" + version
                    + "&sign=" + sign;
            // 将requestUrl返回微信客户端去请求签约
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        return requestUrl;
    }

    // 供微信小程序免密支付调用
    public String doGetEntrustBeanInfo() {
        // 微信支付分配的公众账号id
        String appid = WxPaypayConfig.getAppId();
        // 分别生成20位签约协议号和请求序列号
        // 商户侧的签约协议号，由商户生成
        String contract_code = WxPapayRandomStringGenerator
                .makeSystemCurrentTimeMillisStyleRandomString(18);
        // 商户请求签约时的序列号，商户侧须唯一。序列号主要用于排序，不作为查询条件
        String request_serial = WxPapayRandomStringGenerator
                .makeSystemCurrentTimeMillisStyleRandomString(18);
        // 微信支付分配的商户号
        String mch_id = WxPaypayConfig.getMch_id();
        // 免密委托代扣协议模板id
        String plan_id = WxPaypayConfig.getPlan_id();
        // 签约用户的名称，用于页面展示
        String contract_display_account = WxPaypayConfig
                .getContract_display_account();
        // 用于接收签约成功消息的回调通知地址，以http或https开头
        String notify_url = WxPaypayConfig.getEntrust_notify_url();
        // 系统当前时间，10位
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        // 微信支付规定固定值1.0
        String version = "1.0";

        // 这些参数按照签名规则必须按照参数名称的ASCII码排序
        Map<String, Object> signMap = new HashMap<String, Object>();
        signMap.put("appid", appid);
        signMap.put("contract_code", contract_code);
        signMap.put("contract_display_account", contract_display_account);
        signMap.put("mch_id", mch_id);
        signMap.put("notify_url", notify_url);
        signMap.put("plan_id", plan_id);
        signMap.put("request_serial", request_serial);
        signMap.put("timestamp", timestamp);
        // signMap.put("version", version);

        String sign = WxPaypaySignature.getSign(signMap);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("appid", appid);
        jsonObject.put("contract_code", contract_code);
        jsonObject.put("contract_display_account", contract_display_account);
        jsonObject.put("mch_id", mch_id);
        try {
            jsonObject
                    .put("notify_url", URLEncoder.encode(notify_url, "UTF-8"));
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        jsonObject.put("plan_id", plan_id);
        jsonObject.put("request_serial", request_serial);
        jsonObject.put("timestamp", timestamp);
        // jsonObject.put("version", version);
        jsonObject.put("sign", sign);

        return jsonObject.toString();
    }

    // 查询签约关系appId+plan_id+openId
    public String doQuerySignContract(String code, String phoneNumber) {
        try {
            // 获取openId
            String openid = doWXPayGetOpenId(code);
            if (openid == null) {
                return ResponseStatus.FAIL;
            }
            // 保存用户phoneNumber对应的openId
            if (phoneNumber != null) {
                UserService userService = new UserService();
                userService.updateOpendIdByPhoneNumber(phoneNumber, openid);
            }
            // 微信支付分配的公众账号id
            String appid = WxPaypayConfig.getAppId();
            // 微信支付分配的商户号
            String mch_id = WxPaypayConfig.getMch_id();
            // 免密委托代扣协议模板id
            String plan_id = WxPaypayConfig.getPlan_id();
            // 微信支付规定固定值1.0
            String version = "1.0";

            WxPapayQuerySignContractBean wxPapayQuerySignContractBean = new WxPapayQuerySignContractBean();
            wxPapayQuerySignContractBean.setAppid(appid);
            wxPapayQuerySignContractBean.setMch_id(mch_id);
            wxPapayQuerySignContractBean.setPlan_id(plan_id);
            wxPapayQuerySignContractBean.setOpenid(openid);
            wxPapayQuerySignContractBean.setVersion(version);

            String sign = WxPaypaySignature
                    .getSign(wxPapayQuerySignContractBean);
            wxPapayQuerySignContractBean.setSign(sign);
            String result = HttpRequest.sendPost(
                    "https://api.mch.weixin.qq.com/papay/querycontract",
                    wxPapayQuerySignContractBean);
            log.info("WxPapayService->doQuerySignContract result=" + result);

            if (result != null) {
                Document doc = DocumentHelper.parseText(result.toString());
                Element root = doc.getRootElement();
                Node returnCode = root.selectSingleNode("return_code");
                Node resultCode = root.selectSingleNode("result_code");
                if (returnCode != null
                        && returnCode.getText().equals("SUCCESS")
                        && resultCode != null
                        && resultCode.getText().equals("SUCCESS")) {
                    Node contractState = root
                            .selectSingleNode("contract_state");
                    Node contractId = root.selectSingleNode("contract_id");
                    if (contractState != null && contractId != null
                            && contractState.getText().equals("0")) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("contractId", contractId.getText());
                        return jsonObject.toString();
                    } else {
                        return ResponseStatus.FAIL;
                    }
                } else {
                    return ResponseStatus.FAIL;
                }
            } else {
                return ResponseStatus.FAIL;
            }
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        return ResponseStatus.FAIL;
    }

    // 小程序获取openid
    private String doWXPayGetOpenId(String code) throws Exception {
        HttpGet httpGet = new HttpGet(
                "https://api.weixin.qq.com/sns/jscode2session?appid="
                        + Configure.getAppID() + "&secret="
                        + Configure.getSecret() + "&js_code=" + code
                        + "&grant_type=authorization_code");
        // 设置请求器的配置
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpResponse res = httpClient.execute(httpGet);
        HttpEntity entity = res.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");
        JSONObject jsonObject = JSONObject.fromObject(result);
        return jsonObject.getString("openid");
    }

    // 委托代扣支付
    public boolean doPaypay(String orderId, String contractId, int totalFee) {
        boolean payFlag = false;
        // 公众账号id
        String appid = WxPaypayConfig.getAppId();
        // 商户号
        String mch_id = WxPaypayConfig.getMch_id();
        // 随机字符串
        String nonce_str = WxPapayRandomStringGenerator
                .makeSystemCurrentTimeMillisStyleRandomString(18);
        // 商品描述
        String body = WxPaypayConfig.getContract_display_account();
        // 商户订单号
        String out_trade_no = WxPapayRandomStringGenerator
                .makeDateStyleRandomString(20);
        // 总金额
        int total_fee = totalFee;
        // 终端IP
        String spbill_create_ip = ResponseStatus.SERVER_IP;
        // 回调通知url
        String notify_url = WxPaypayConfig.getPay_success_notify_url() + "?"
                + "orderId=" + orderId;
        // 交易类型
        String trade_type = "JSAPI";
        // 委托代扣协议id
        String contract_id = contractId;

        WxPapayApplyBean wxPapayApplyBean = new WxPapayApplyBean();
        wxPapayApplyBean.setAppid(appid);
        wxPapayApplyBean.setMch_id(mch_id);
        wxPapayApplyBean.setNonce_str(nonce_str);
        wxPapayApplyBean.setBody(body);
        wxPapayApplyBean.setOut_trade_no(out_trade_no);
        wxPapayApplyBean.setTotal_fee(total_fee);
        wxPapayApplyBean.setSpbill_create_ip(spbill_create_ip);
        wxPapayApplyBean.setNotify_url(notify_url);
        wxPapayApplyBean.setTrade_type(trade_type);
        wxPapayApplyBean.setContract_id(contract_id);

        // 签名
        String sign = null;
        try {
            sign = WxPaypaySignature.getSign(wxPapayApplyBean);
            wxPapayApplyBean.setSign(sign);

            String result = HttpRequest.sendPost(
                    "https://api.mch.weixin.qq.com/pay/pappayapply",
                    wxPapayApplyBean);
            Document doc = DocumentHelper.parseText(result);
            Element root = doc.getRootElement();
            Node resultCode = root.selectSingleNode("result_code");
            if (resultCode != null && resultCode.getText().equals("SUCCESS")) {
                payFlag = true;
            } else {
                payFlag = false;
            }
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        return payFlag;
    }

    // 申请退款
    public boolean doRefund(String transactionId, int totalFee, int refundFee) {
        boolean refundFlag = false;
        // 公众账号id
        String appid = WxPaypayConfig.getAppId();
        // 商户号
        String mch_id = WxPaypayConfig.getMch_id();
        // 随机字符串
        String nonce_str = WxPapayRandomStringGenerator
                .makeSystemCurrentTimeMillisStyleRandomString(18);
        // 微信生成的订单号，在支付通知中有返回
        String transaction_id = transactionId;
        // 商户退款单号
        String out_refund_no = WxPapayRandomStringGenerator
                .makeDateStyleRandomString(20);
        // 退款总金额，订单总金额，单位为分，只能为整数，详见支付金额
        int total_fee = totalFee;
        // 退款总金额，订单总金额，单位为分，只能为整数，详见支付金额
        int refund_fee = refundFee;
        // 若商户传入，会在下发给用户的退款消息中体现退款原因
        String refund_desc = WxPaypayConfig.getRefund_desc();

        WxPapayRefundBean wxPapayRefundBean = new WxPapayRefundBean();
        wxPapayRefundBean.setAppid(appid);
        wxPapayRefundBean.setMch_id(mch_id);
        wxPapayRefundBean.setNonce_str(nonce_str);
        wxPapayRefundBean.setTransaction_id(transaction_id);
        wxPapayRefundBean.setOut_refund_no(out_refund_no);
        wxPapayRefundBean.setTotal_fee(total_fee);
        wxPapayRefundBean.setRefund_fee(refund_fee);
        wxPapayRefundBean.setRefund_desc(refund_desc);

        // 签名
        String sign = null;
        try {
            sign = WxPaypaySignature.getSign(wxPapayRefundBean);
            wxPapayRefundBean.setSign(sign);

            String result = WxPaypaySSLHttpRequest.sendPost(
                    "https://api.mch.weixin.qq.com/secapi/pay/refund",
                    wxPapayRefundBean, getCertFilePath(),
                    WxPaypayConfig.getMch_id());
            Document doc = DocumentHelper.parseText(result);
            Element root = doc.getRootElement();
            Node resultCode = root.selectSingleNode("result_code");
            if (resultCode != null && resultCode.getText().equals("SUCCESS")) {
                refundFlag = true;
            } else {
                refundFlag = false;
            }
            log.info("doRefund:result=" + result);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        return refundFlag;
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

    // 想用户微信客户端发送购买成功消息
    public void sendTemplateMsgToWxUser(Order order, String userOpenId,
            String templateMsgId, String miniProgramPage, String prepayId) {
        TokenService tokenService = new TokenService();
        String accessToken = tokenService.doGetAccessToken(
                WxPaypayConfig.getAppId(), WxPaypayConfig.getSecret(),
                ResponseStatus.CUSTOM_CATEGORY_WX);

        JSONObject templateMsgJsonObject = new JSONObject();
        templateMsgJsonObject.put("touser", userOpenId);
        templateMsgJsonObject.put("template_id",
                WxPaypayConfig.getTemplateMsgId());
        templateMsgJsonObject.put("page", WxPaypayConfig.getMiniProgramPage());
        templateMsgJsonObject.put("form_id", prepayId);

        JSONArray dataBeanJsonArray = new JSONArray();

        JSONObject orderIdJsonObject = new JSONObject();
        JSONObject orderIdBeanJsonObject = new JSONObject();
        orderIdBeanJsonObject.put("value", "1234567890");
        orderIdBeanJsonObject.put("color", "#0000ff");
        orderIdJsonObject.put("keyword1", orderIdBeanJsonObject);

        JSONObject goodsNameJsonObject = new JSONObject();
        JSONObject goodsNameBeanJsonObject = new JSONObject();
        goodsNameBeanJsonObject.put("value", "智购猫商品");
        goodsNameBeanJsonObject.put("color", "#0000ff");
        goodsNameJsonObject.put("keyword2", goodsNameBeanJsonObject);

        JSONObject goodsTotalNumberJsonObject = new JSONObject();
        JSONObject goodsTotalNumberBeanJsonObject = new JSONObject();
        goodsTotalNumberBeanJsonObject.put("value", "N");
        goodsTotalNumberBeanJsonObject.put("color", "#0000ff");
        goodsTotalNumberJsonObject.put("keyword3",
                goodsTotalNumberBeanJsonObject);

        JSONObject goodsBoxNameJsonObject = new JSONObject();
        JSONObject goodsBoxNameBeanJsonObject = new JSONObject();
        goodsBoxNameBeanJsonObject.put("value", "ZGM SHOP");
        goodsBoxNameBeanJsonObject.put("color", "#0000ff");
        goodsBoxNameJsonObject.put("keyword4", goodsBoxNameBeanJsonObject);

        JSONObject orderDateTimeJsonObject = new JSONObject();
        JSONObject orderDateTimeBeanJsonObject = new JSONObject();
        orderDateTimeBeanJsonObject.put("value", "2018-03-21 17:13:20");
        orderDateTimeBeanJsonObject.put("color", "#0000ff");
        orderDateTimeJsonObject.put("keyword5", orderDateTimeBeanJsonObject);

        dataBeanJsonArray.add(orderIdJsonObject);
        dataBeanJsonArray.add(goodsNameJsonObject);
        dataBeanJsonArray.add(goodsTotalNumberJsonObject);
        dataBeanJsonArray.add(goodsBoxNameJsonObject);
        dataBeanJsonArray.add(orderDateTimeJsonObject);

        templateMsgJsonObject.put("data", dataBeanJsonArray);

        log.info("WxPapayService:->sendTemplateMsgToWxUser:templateMsgJsonObject="
                + templateMsgJsonObject);

    }

}
