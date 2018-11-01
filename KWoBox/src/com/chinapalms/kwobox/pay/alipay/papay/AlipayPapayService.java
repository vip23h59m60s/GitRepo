package com.chinapalms.kwobox.pay.alipay.papay;

import java.net.URLEncoder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class AlipayPapayService {

    Log log = LogFactory.getLog(AlipayPapayService.class);

    // public void getUserInfo() {
    // try {
    // HttpGet httpGet = new HttpGet(
    // "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=2017100909216887&scope=auth_userinfo&redirect_uri="
    // + URLEncoder
    // .encode(AlipayPaypayConfig.GET_USERINFO_REDIRECT_URI,
    // "utf-8"));
    //
    // // 设置请求器的配置
    // CloseableHttpClient httpClient = HttpClients.createDefault();
    // HttpResponse res = httpClient.execute(httpGet);
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }

    // 获取请求签约URL
    public String dogetAlipayEntrustURL() {

        String service = "alipay.dut.customer.agreement.page.sign";
        String partner = AlipayPaypayConfig.getPartner();
        String sign_type = "MD5";
        String _input_charset = "utf-8";
        String notify_url = AlipayPaypayConfig.getEntrust_notify_url();
        String product_code = "GENERAL_WITHHOLDING_P";
        // 请求参数时access_info需要Encode
        String access_info = "{\"channel\":\"ALIPAYAPP\"}";

        AlipayPaypayEntrustBean alipayPaypayEntrustBean = new AlipayPaypayEntrustBean();
        alipayPaypayEntrustBean.setService(service);
        alipayPaypayEntrustBean.setPartner(partner);
        // sign_type不需要参与签名
        // alipayPaypayEntrustBean.setSign_type(sign_type);
        alipayPaypayEntrustBean.set_input_charset(_input_charset);
        alipayPaypayEntrustBean.setNotify_url(notify_url);
        alipayPaypayEntrustBean.setProduct_code(product_code);
        alipayPaypayEntrustBean.setAccess_info(access_info);

        try {
            String sign = AlipayPaypaySignature
                    .getSign(alipayPaypayEntrustBean);
            alipayPaypayEntrustBean.setSign(sign);

            String url = "https://mapi.alipay.com/gateway.do?service="
                    + service + "&partner=" + partner + "&_input_charset="
                    + _input_charset + "&notify_url="
                    + URLEncoder.encode(notify_url, "UTF-8") + "&product_code="
                    + product_code + "&access_info="
                    + URLEncoder.encode(access_info, "UTF-8") + "&sign_type="
                    + sign_type + "&sign=" + sign;
            return url;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        return null;
    }
}
