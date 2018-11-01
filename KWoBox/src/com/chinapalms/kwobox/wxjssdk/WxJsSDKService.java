package com.chinapalms.kwobox.wxjssdk;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.chinapalms.kwobox.javabean.Token;
import com.chinapalms.kwobox.pay.wxpay.Configure;
import com.chinapalms.kwobox.service.TokenService;
import com.chinapalms.kwobox.utils.MD5;
import com.chinapalms.kwobox.utils.ResponseStatus;

import net.sf.json.JSONObject;

//该内容参考
//微信JS-SDK获取signature签名以及config配置
//http://1017401036.iteye.com/blog/2263358
public class WxJsSDKService {

    static Log log = LogFactory.getLog(WxJsSDKService.class);

    public static String getWxJsSDKConfig(String customURL, String tokenType) {
        // 1、获取AccessToken
        String accessToken = getAccessToken();
        String jsapi_ticket = null;
        if (accessToken != null) {
            jsapi_ticket = getTicket(accessToken);
        } else {
            return ResponseStatus.FAIL;
        }
        // 2、获取Ticket

        // 3、时间戳和随机字符串
        String noncestr = UUID.randomUUID().toString().replace("-", "")
                .substring(0, 16);// 随机字符串
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);// 时间戳

        // 4、获取url
        // String url = "https://www.zigoomall.com/H5Zigoomall/main.html";
        /*
         * 根据JSSDK上面的规则进行计算，这里比较简单，我就手动写啦 String[] ArrTmp =
         * {"jsapi_ticket","timestamp","nonce","url"}; Arrays.sort(ArrTmp);
         * StringBuffer sf = new StringBuffer(); for(int
         * i=0;i<ArrTmp.length;i++){ sf.append(ArrTmp[i]); }
         */

        // 5、将参数排序并拼接字符串
        String str = null;
        if (jsapi_ticket != null) {
            str = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + noncestr
                    + "&timestamp=" + timestamp + "&url=" + customURL;
        } else {
            return ResponseStatus.FAIL;
        }

        // 6、将字符串进行sha1加密
        String signature = SHA1(str);
        if (signature != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("appId", WxJsSDKConfigure.getAppID());
            jsonObject.put("timestamp", timestamp);
            jsonObject.put("nonceStr", noncestr);
            jsonObject.put("signature", signature);

            JSONObject wxJsConfigJsonObject = new JSONObject();
            wxJsConfigJsonObject.put("wxJsSDKConfig", jsonObject);
            System.out
                    .println("getJsConfig=" + wxJsConfigJsonObject.toString());
            // 将获取的Token和Ticket等信息保存在本地数据库,如果有就更新,如果没有就插入
            TokenService tokenService = new TokenService();
            Token token = tokenService.findTokenByTokenType(tokenType);

            if (token != null) {
                token.setRequestToken(accessToken);
                token.setRequestTicket(jsapi_ticket);
                token.setTokenDateTime(new Date());
                token.setTokenType(tokenType);
                if (tokenService.updateToken(token)) {
                    return wxJsConfigJsonObject.toString();
                } else {
                    return ResponseStatus.FAIL;
                }
            } else {
                token = new Token();
                token.setRequestToken(accessToken);
                token.setRequestTicket(jsapi_ticket);
                token.setTokenDateTime(new Date());
                token.setTokenType(tokenType);
                if (tokenService.addToken(token)) {
                    return wxJsConfigJsonObject.toString();
                } else {
                    return ResponseStatus.FAIL;
                }
            }
        } else {
            return ResponseStatus.FAIL;
        }
    }

    // 获取access_token
    private static String getAccessToken() {
        String access_token = null;
        String grant_type = "client_credential";// 获取access_token填写client_credential
        String AppId = WxJsSDKConfigure.getAppID();// 第三方用户唯一凭证
        String secret = WxJsSDKConfigure.getSecret();// 第三方用户唯一凭证密钥，即appsecret
        // 这个url链接地址和参数皆不能变
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type="
                + grant_type + "&appid=" + AppId + "&secret=" + secret;

        try {
            URL urlGet = new URL(url);
            HttpURLConnection http = (HttpURLConnection) urlGet
                    .openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
            http.connect();
            InputStream is = http.getInputStream();
            int size = is.available();
            byte[] jsonBytes = new byte[size];
            is.read(jsonBytes);
            String message = new String(jsonBytes, "UTF-8");
            JSONObject demoJson = JSONObject.fromObject(message);
            access_token = demoJson.getString("access_token");
            is.close();
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        return access_token;
    }

    // 获取jsapi_ticket
    private static String getTicket(String access_token) {
        String ticket = null;
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="
                + access_token + "&type=jsapi";// 这个url链接和参数不能变
        try {
            URL urlGet = new URL(url);
            HttpURLConnection http = (HttpURLConnection) urlGet
                    .openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
            http.connect();
            InputStream is = http.getInputStream();
            int size = is.available();
            byte[] jsonBytes = new byte[size];
            is.read(jsonBytes);
            String message = new String(jsonBytes, "UTF-8");
            JSONObject demoJson = JSONObject.fromObject(message);
            ticket = demoJson.getString("ticket");
            is.close();
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        return ticket;
    }

    // 拿到了jsapi_ticket之后就要参数名排序和拼接字符串，并加密了。以下为sha1的加密算法
    public static String SHA1(String decript) {
        String shaString = null;
        try {
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            shaString = hexString.toString();

        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        return shaString;
    }

    public String doWXGetOpenSession(String code) throws Exception {
        HttpGet httpGet = new HttpGet(
                "https://api.weixin.qq.com/sns/jscode2session?appid="
                        + WxJsSDKConfigure.getAppID() + "&secret="
                        + WxJsSDKConfigure.getSecret() + "&js_code=" + code
                        + "&grant_type=authorization_code");
        // 设置请求器的配置
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpResponse res = httpClient.execute(httpGet);
        HttpEntity entity = res.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");
        return result;
    }

}
