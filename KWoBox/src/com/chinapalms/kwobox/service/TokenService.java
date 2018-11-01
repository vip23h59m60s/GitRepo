package com.chinapalms.kwobox.service;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONObject;

import com.chinapalms.kwobox.dao.impl.TokenDAOImpl;
import com.chinapalms.kwobox.javabean.Token;
import com.chinapalms.kwobox.utils.ResponseStatus;
import com.chinapalms.kwobox.wxjssdk.WxJsSDKConfigure;
import com.chinapalms.kwobox.wxjssdk.WxJsSDKService;

public class TokenService extends TokenDAOImpl {

    Log log = LogFactory.getLog(TokenService.class);

    @Override
    public boolean addToken(Token token) {
        return super.addToken(token);
    }

    @Override
    public boolean updateToken(Token token) {
        return super.updateToken(token);
    }

    @Override
    public Token findTokenByTokenType(String tokenType) {
        return super.findTokenByTokenType(tokenType);
    }

    public String doGetWxJsSDKConfig(String customURL, String tokenType) {
        String wxJsSDKConfigResult = null;
        TokenService tokenService = new TokenService();
        Token token = tokenService.findTokenByTokenType(tokenType);
        if (token != null) {
            Date dateTimeQuery = token.getTokenDateTime();
            Date dateTimeCurrent = new Date();
            if (isTimeOut(dateTimeQuery, dateTimeCurrent)) {
                // Token已过期，重新向微信服务器获取
                wxJsSDKConfigResult = WxJsSDKService.getWxJsSDKConfig(
                        customURL, tokenType);
            } else {
                // Token未过期，直接从本地数据库中读取
                wxJsSDKConfigResult = getWxJsSDKConfigFromDatabase(token,
                        customURL);
            }
        } else {
            wxJsSDKConfigResult = WxJsSDKService.getWxJsSDKConfig(customURL,
                    tokenType);
        }
        return wxJsSDKConfigResult;
    }

    private String getWxJsSDKConfigFromDatabase(Token token, String customURL) {
        String noncestr = UUID.randomUUID().toString().replace("-", "")
                .substring(0, 16);// 随机字符串
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);// 时间戳
        String configStr = "jsapi_ticket=" + token.getRequestTicket()
                + "&noncestr=" + noncestr + "&timestamp=" + timestamp + "&url="
                + customURL;

        // 6、将字符串进行sha1加密
        String signature = WxJsSDKService.SHA1(configStr);
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
            return wxJsConfigJsonObject.toString();
        } else {
            return ResponseStatus.FAIL;
        }
    }

    // 动态获取Token
    public String doGetAccessToken(String appId, String secret, String tokenType) {
        String wxToken = null;
        Token token = findTokenByTokenType(tokenType);
        if (token != null) {
            Date dateTimeQuery = token.getTokenDateTime();
            Date dateTimeCurrent = new Date();
            if (isTimeOut(dateTimeQuery, dateTimeCurrent)) {
                // Token已过期，重新向微信服务器获取
                wxToken = getAccessTokenFrom3RDServer(appId, secret, tokenType);
            } else {
                // Token未过期，直接从本地数据库中读取
                wxToken = token.getRequestToken();
            }
        } else {
            // 还未获取过Token
            wxToken = getAccessTokenFrom3RDServer(appId, secret, tokenType);
        }
        return wxToken;
    }

    // 微信小程序获取Token
    private String getAccessTokenFrom3RDServer(String appId, String secret,
            String tokenType) {
        String access_token = null;
        String grant_type = "client_credential";// 获取access_token填写client_credential
        // 这个url链接地址和参数皆不能变
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type="
                + grant_type + "&appid=" + appId + "&secret=" + secret;

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
            // 保存Token到本地数据库
            Token token = findTokenByTokenType(tokenType);

            if (token != null) {
                token.setRequestToken(access_token);
                token.setRequestTicket("jsapi_ticket");
                token.setTokenDateTime(new Date());
                token.setTokenType(tokenType);
                if (updateToken(token)) {
                    return access_token.toString();
                } else {
                    return ResponseStatus.FAIL;
                }
            } else {
                token = new Token();
                token.setRequestToken(access_token);
                token.setRequestTicket("jsapi_ticket");
                token.setTokenDateTime(new Date());
                token.setTokenType(tokenType);
                if (addToken(token)) {
                    return access_token.toString();
                } else {
                    return ResponseStatus.FAIL;
                }
            }
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        return access_token;
    }

    // 从本地数据库获取Token
    private String getAccessTokenFromLocalDatabase(String tokenType) {
        Token token = findTokenByTokenType(tokenType);
        if (token != null) {
            return token.getRequestToken();
        } else {
            return ResponseStatus.FAIL;
        }
    }

    // 微信规定的Token过期时间为7200s，在此已7000s为一次本地更新周期
    private boolean isTimeOut(Date queryTime, Date currentTime) {
        boolean isTimeOut = false;
        long timeMinute = (currentTime.getTime() - queryTime.getTime()) / 1000;
        if (timeMinute > 7000) {
            isTimeOut = true;
        }
        return isTimeOut;
    }
}
