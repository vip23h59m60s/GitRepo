package com.chinapalms.kwobox.tencentlocation;

import java.math.BigDecimal;
import java.net.URLEncoder;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.chinapalms.kwobox.pay.wxpay.papay.WxPapayService;
import com.chinapalms.kwobox.utils.ResponseStatus;

public class TencentLocationService {

    Log log = LogFactory.getLog(WxPapayService.class);

    // 逆地址解析(坐标位置描述)(坐标转地址)
    public String latLngToAddress(String lat, String lng) {
        String address = ResponseStatus.FAIL;
        try {
            HttpGet httpGet = new HttpGet(
                    "https://apis.map.qq.com/ws/geocoder/v1/?location="
                            + URLEncoder.encode(lat + "," + lng, "utf-8")
                            + "&key="
                            + URLEncoder
                                    .encode(TencentLocationConfig.LOCATION_KEY,
                                            "utf-8") + "&get_poi="
                            + URLEncoder.encode("1", "utf-8"));
            // 设置请求器的配置
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpResponse res = httpClient.execute(httpGet);
            HttpEntity entity = res.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            JSONObject jsonObject = JSONObject.fromObject(result);

            // 获取到状态信息并且状态值为0表示返回成功，否则返回失败
            if (jsonObject.containsKey("status")
                    && jsonObject.getString("status").equals("0")) {
                int poi_count = Integer.valueOf(jsonObject.getJSONObject(
                        "result").getString("poi_count"));
                if (poi_count > 0) {
                    JSONObject firstPoisJsonObject = jsonObject
                            .getJSONObject("result").getJSONArray("pois")
                            .getJSONObject(0);
                    address = firstPoisJsonObject.getString("address")
                            + firstPoisJsonObject.getString("title");
                } else {
                    address = jsonObject.getJSONObject("result").getString(
                            "address");
                }
            } else {
                address = ResponseStatus.FAIL;
            }
            log.info("TencentLocationService->latLngToAddress:address="
                    + address);
        } catch (Exception e) {
            address = ResponseStatus.FAIL;
            log.error("TencentLocationService->latLngToAddress=error:address="
                    + address);
            e.printStackTrace();
        }
        return address;
    }

    // 根据两点指点的坐标计算两点之间的距离（计算结果距离+单位）
    public String calculateDistance(String fromLat, String fromLng,
            String toLat, String toLng) {
        String distanceString = ResponseStatus.FAIL;
        try {
            HttpGet httpGet = new HttpGet(
                    "https://apis.map.qq.com/ws/distance/v1/?mode="
                            + URLEncoder.encode("walking", "utf-8")
                            + "&from="
                            + URLEncoder.encode(fromLat + "," + fromLng,
                                    "utf-8")
                            + "&to="
                            + URLEncoder.encode(toLat + "," + toLng, "utf-8")
                            + "&key="
                            + URLEncoder
                                    .encode(TencentLocationConfig.LOCATION_KEY,
                                            "utf-8"));
            // 设置请求器的配置
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpResponse res = httpClient.execute(httpGet);
            HttpEntity entity = res.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            JSONObject jsonObject = JSONObject.fromObject(result);
            // 获取到状态信息并且状态值为0表示返回成功，否则返回失败
            if (jsonObject.containsKey("status")
                    && jsonObject.getString("status").equals("0")) {
                distanceString = jsonObject.getJSONObject("result")
                        .getJSONArray("elements").getJSONObject(0)
                        .getString("distance");
                // 小于1km按m显示，大于1km按km显示
                double distance = Double.valueOf(distanceString);
                JSONObject distanceJsonObject = new JSONObject();
                if (distance < 1000) {
                    distanceJsonObject.put("distance",
                            (int) Double.valueOf(distanceString).doubleValue());
                    distanceJsonObject.put("unit", "m");
                } else {
                    distanceJsonObject.put("distance", String
                            .valueOf(new BigDecimal(distance / 1000d).setScale(
                                    1, BigDecimal.ROUND_DOWN).doubleValue()));
                    distanceJsonObject.put("unit", "km");
                }
                distanceString = distanceJsonObject.toString();
            } else {
                distanceString = ResponseStatus.FAIL;
            }
            log.info("TencentLocationService->calculateDistance:distanceString="
                    + distanceString);
        } catch (Exception e) {
            distanceString = ResponseStatus.FAIL;
            log.error("TencentLocationService->calculateDistance=error:distanceString="
                    + distanceString);
            e.printStackTrace();
        }
        return distanceString;
    }

    // 把其它地图(百度地图：type=3)运营商的经纬度转化为腾讯的经纬度
    public String translateLatAndLng(String otherLat, String otherLng) {
        String latAndLngTranslated = ResponseStatus.FAIL;
        try {
            HttpGet httpGet = new HttpGet(
                    "https://apis.map.qq.com/ws/coord/v1/translate?locations="
                            + URLEncoder.encode(otherLat + "," + otherLng,
                                    "utf-8")
                            + "&type="
                            + URLEncoder.encode("3", "utf-8")
                            + "&key="
                            + URLEncoder
                                    .encode(TencentLocationConfig.LOCATION_KEY,
                                            "utf-8"));
            // 设置请求器的配置
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpResponse res = httpClient.execute(httpGet);
            HttpEntity entity = res.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            JSONObject jsonObject = JSONObject.fromObject(result);
            // 获取到状态信息并且状态值为0表示返回成功，否则返回失败
            if (jsonObject.containsKey("status")
                    && jsonObject.getString("status").equals("0")) {
                latAndLngTranslated = jsonObject.getJSONArray("locations")
                        .getJSONObject(0).toString();
            } else {
                latAndLngTranslated = ResponseStatus.FAIL;
            }
            log.info("TencentLocationService->latLngToAddress:address="
                    + latAndLngTranslated);
        } catch (Exception e) {
            latAndLngTranslated = ResponseStatus.FAIL;
            log.error("TencentLocationService->latLngToAddress=error:address="
                    + latAndLngTranslated);
            e.printStackTrace();
        }
        return latAndLngTranslated;
    }
}
