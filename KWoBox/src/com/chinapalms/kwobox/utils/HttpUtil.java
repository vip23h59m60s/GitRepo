package com.chinapalms.kwobox.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HttpUtil {

    static Log log = LogFactory.getLog(HttpUtil.class);

    public static String sendPost(String url, Map<String, Object> paramMap) {
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod(url);
        post.addRequestHeader("Content-Type",
                "application/x-www-form-urlencoded;charset=utf-8");
        NameValuePair[] nameValuePairs = new NameValuePair[paramMap.size()];
        Iterator<Entry<String, Object>> iter = paramMap.entrySet().iterator();
        JSONArray jsonArray = new JSONArray();
        while (iter.hasNext()) {
            Entry<String, Object> entry = iter.next();
            String key = entry.getKey();
            String value = String.valueOf(entry.getValue());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("key", key);
            jsonObject.put("value", value);
            jsonArray.add(jsonObject);
        }
        for (int i = 0; i < jsonArray.size(); i++) {
            String key = jsonArray.getJSONObject(i).getString("key");
            String value = jsonArray.getJSONObject(i).getString("value");
            nameValuePairs[i] = new NameValuePair(key, value);
        }
        post.setRequestBody(nameValuePairs);

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
        String result = ResponseStatus.FAIL;
        try {
            result = new String(post.getResponseBodyAsString()
                    .getBytes("utf-8"));
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        post.releaseConnection();
        post = null;
        System.out.println("result111111111111=" + result);
        return result;
    }

    public static String sendCallbackResultMsg(String urlString,
            JSONObject jsonObject) {
        String resp = ResponseStatus.FAIL;
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString); // url地址
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();

            OutputStream os = connection.getOutputStream();
            os.write(jsonObject.toString().getBytes("UTF-8"));
            os.flush();
            os.close();
            log.info("return Status=" + connection.getResponseCode());
            reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String lines;
            StringBuffer sbf = new StringBuffer();
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sbf.append(lines);
            }
            resp = sbf.toString();
            log.info("sendCallbackResultMsgReturn=" + resp);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    reader = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                if (null != connection) {
                    // 显式关闭inputStream，达到释放资源目的
                    InputStream isInputStream = connection.getInputStream();
                    isInputStream.close();
                    isInputStream = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (null != connection) {
                    connection.disconnect();
                    connection = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resp;
    }

}
