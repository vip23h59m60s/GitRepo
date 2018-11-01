package com.chinapalms.kwobox.pay.wxpay;

import java.math.BigDecimal;
import java.net.InetAddress;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.chinapalms.kwobox.utils.ResponseStatus;
import com.thoughtworks.xstream.XStream;

public class WXPayService {

    Log log = LogFactory.getLog(WXPayService.class);

    public String doWXPayGetOpenId(String code) throws Exception {
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
        return result;
    }

    public String doWXPayXiaDan(String payOrderId, String openId,
            String payTotal) {
        try {
            OrderInfo order = new OrderInfo();
            order.setAppid(Configure.getAppID());
            order.setMch_id(Configure.getMch_id());
            order.setNonce_str(RandomStringGenerator
                    .getRandomStringByLength(32));
            order.setBody(Configure.getGoodsOwner());
            order.setOut_trade_no(RandomStringGenerator
                    .makeDateStyleRandomString(20));
            log.info("WXPayService->payTotal=" + payTotal);
            order.setTotal_fee((int) ((BigDecimal.valueOf(Double
                    .valueOf(payTotal))).multiply(BigDecimal.valueOf(Double
                    .valueOf(100)))).setScale(2, BigDecimal.ROUND_DOWN)
                    .doubleValue());
            // order.setTotal_fee(1);
            order.setSpbill_create_ip(ResponseStatus.SERVER_IP);
            if (payOrderId != null) {
                order.setNotify_url(Configure.getNotify_url() + "?orderId="
                        + payOrderId);
            } else {
                order.setNotify_url(Configure.getNotify_url());
            }
            order.setTrade_type("JSAPI");
            order.setOpenid(openId);
            order.setSign_type("MD5");
            // 生成签名
            String sign = Signature.getSign(order);
            order.setSign(sign);

            String result = HttpRequest.sendPost(
                    "https://api.mch.weixin.qq.com/pay/unifiedorder", order);
            log.info("doWXPayXiaDan:result=" + result);
            XStream xStream = new XStream();
            xStream.alias("xml", OrderReturnInfo.class);

            OrderReturnInfo returnInfo = (OrderReturnInfo) xStream
                    .fromXML(result);
            return returnInfo.getPrepay_id();
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        return "";
    }

    public String doWXPaySign(String prePayId) {
        try {
            SignInfo signInfo = new SignInfo();
            signInfo.setAppId(Configure.getAppID());
            long time = System.currentTimeMillis() / 1000;
            signInfo.setTimeStamp(String.valueOf(time));
            signInfo.setNonceStr(RandomStringGenerator
                    .getRandomStringByLength(32));
            signInfo.setRepay_id("prepay_id=" + prePayId);
            signInfo.setSignType("MD5");
            // 生成签名
            String sign = Signature.getSign(signInfo);

            JSONObject json = new JSONObject();
            json.put("timeStamp", signInfo.getTimeStamp());
            json.put("nonceStr", signInfo.getNonceStr());
            json.put("package", signInfo.getRepay_id());
            json.put("signType", signInfo.getSignType());
            json.put("paySign", sign);
            return json.toString();
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        return "";
    }
}
