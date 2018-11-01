package com.custom.kwobox.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.chinapalms.kwobox.javabean.CustomRequestInfo;
import com.chinapalms.kwobox.service.CustomRequestInfoService;
import com.chinapalms.kwobox.utils.ResponseStatus;

/**
 * 服务器回调客户服务器签名构造工具类
 * 
 * @author wangyi
 * 
 */

/**
 * 门已开客户签名相关参数构造
 * 
 * @author wangyi
 * 
 */
public class CustomCallbackSignatureParamsStructureUtil {

    public static Map<String, Object> getDoorOpenedCallbackSignatureParamsMap(
            String boxIdPara, String userType, String userIdPara, String mchId) {
        Map<String, Object> signParamMap = null;
        // 小e微店
        if (mchId.equals(CustomConfig.XIAOE_MCHID)
                || mchId.equals(CustomConfig.XIAOE_TEST_MCHID)) {
            // 加入商户开门请求时的requestSerial
            CustomRequestInfoService customRequestInfoService = new CustomRequestInfoService();
            CustomRequestInfo customRequestInfo = customRequestInfoService
                    .findCustomRequestInfoByBoxId(boxIdPara);
            // 商户开门请求时的requestSerial
            String requestSerial = customRequestInfo.getOpenDoorRequestSerial();
            // 合作伙伴发起请求的时间，格式：yyyyMMddHHmmss
            String reqtime = new SimpleDateFormat("yyyyMMddHHmmss")
                    .format(new Date());
            // 1-XML,2-JSON
            String returntype = "2";
            // 接口版本号
            String version = "1.0";
            // 商户ID标识
            String appid = "wisdcat";
            // 请求系统编号
            String systemid = "wisdcat101";
            // 请求终端类型
            String terminaltype = "wisdcat";

            // 当前购物售货机ID
            String boxId = boxIdPara;
            // 当前购物者电话号码或理货员ID
            String userId = userIdPara;

            // 签名
            signParamMap = new LinkedHashMap<String, Object>();
            signParamMap.put("requestSerial", requestSerial);
            signParamMap.put("reqtime", reqtime);
            signParamMap.put("returntype", returntype);
            signParamMap.put("version", version);
            signParamMap.put("appid", appid);
            signParamMap.put("systemid", systemid);
            signParamMap.put("terminaltype", terminaltype);
            signParamMap.put("boxId", boxId);
            if (userType.equals(ResponseStatus.PERMISSION_NORMAL_USER)) {
                signParamMap.put("phoneNumber", userId);
            } else if (userType.equals(ResponseStatus.PERMISSION_MANAGER)) {
                signParamMap.put("workerId", userId);
            }
            String sign = CustomRequestSignatureUtil
                    .getSignWithoutKey(signParamMap);
            signParamMap.put("sign", sign);
        }
        return signParamMap;
    }

    /**
     * 门已关客户签名相关参数构造
     * 
     * @param mchId
     * @return
     */
    public static Map<String, Object> getDoorClosedCallbackSignatureParamsMap(
            String boxIdPara, String userType, String userIdPara, String mchId) {
        Map<String, Object> signParamMap = null;
        // 小e微店
        if (mchId.equals(CustomConfig.XIAOE_MCHID)
                || mchId.equals(CustomConfig.XIAOE_TEST_MCHID)) {
            // 加入商户开门请求时的requestSerial
            CustomRequestInfoService customRequestInfoService = new CustomRequestInfoService();
            CustomRequestInfo customRequestInfo = customRequestInfoService
                    .findCustomRequestInfoByBoxId(boxIdPara);
            // 商户开门请求时的requestSerial
            String requestSerial = customRequestInfo.getOpenDoorRequestSerial();
            // 合作伙伴发起请求的时间，格式：yyyyMMddHHmmss
            String reqtime = new SimpleDateFormat("yyyyMMddHHmmss")
                    .format(new Date());
            // 1-XML,2-JSON
            String returntype = "2";
            // 接口版本号
            String version = "1.0";
            // 商户ID标识
            String appid = "wisdcat";
            // 请求系统编号
            String systemid = "wisdcat101";
            // 请求终端类型
            String terminaltype = "wisdcat";

            // 当前购物售货机ID
            String boxId = boxIdPara;
            // 当前购物者电话号码
            String userId = userIdPara;

            // 签名
            signParamMap = new LinkedHashMap<String, Object>();
            signParamMap.put("requestSerial", requestSerial);
            signParamMap.put("reqtime", reqtime);
            signParamMap.put("returntype", returntype);
            signParamMap.put("version", version);
            signParamMap.put("appid", appid);
            signParamMap.put("systemid", systemid);
            signParamMap.put("terminaltype", terminaltype);
            signParamMap.put("boxId", boxId);
            if (userType.equals(ResponseStatus.PERMISSION_NORMAL_USER)) {
                signParamMap.put("phoneNumber", userId);
            } else if (userType.equals(ResponseStatus.PERMISSION_MANAGER)) {
                signParamMap.put("workerId", userId);
            }
            String sign = CustomRequestSignatureUtil
                    .getSignWithoutKey(signParamMap);
            signParamMap.put("sign", sign);
        }
        return signParamMap;
    }

    /**
     * 回调商户订单相关客户签名相关参数构造
     * 
     * @param mchId
     * @return
     */
    public static Map<String, Object> getOrderCallbackSignatureParamsMap(
            String boxIdPara, String phoneNumberPara,
            String openDoorRequestSerialNumber, String mchId) {
        Map<String, Object> signParamMap = null;
        // 小e微店
        if (mchId.equals(CustomConfig.XIAOE_MCHID)
                || mchId.equals(CustomConfig.XIAOE_TEST_MCHID)) {
            // 加入商户开门请求时的requestSerial
            String requestSerial = null;
            if (openDoorRequestSerialNumber == null) {
                CustomRequestInfoService customRequestInfoService = new CustomRequestInfoService();
                CustomRequestInfo customRequestInfo = customRequestInfoService
                        .findCustomRequestInfoByBoxId(boxIdPara);
                // 商户开门请求时的requestSerial
                requestSerial = customRequestInfo.getOpenDoorRequestSerial();
            } else {
                requestSerial = openDoorRequestSerialNumber;
            }
            // 合作伙伴发起请求的时间，格式：yyyyMMddHHmmss
            String reqtime = new SimpleDateFormat("yyyyMMddHHmmss")
                    .format(new Date());
            // 1-XML,2-JSON
            String returntype = "2";
            // 接口版本号
            String version = "1.0";
            // 商户ID标识
            String appid = "wisdcat";
            // 请求系统编号
            String systemid = "wisdcat101";
            // 请求终端类型
            String terminaltype = "wisdcat";

            // 当前购物售货机ID
            String boxId = boxIdPara;
            // 当前购物者电话号码
            String phoneNumber = phoneNumberPara;

            // 签名
            signParamMap = new LinkedHashMap<String, Object>();
            signParamMap.put("requestSerial", requestSerial);
            signParamMap.put("reqtime", reqtime);
            signParamMap.put("returntype", returntype);
            signParamMap.put("version", version);
            signParamMap.put("appid", appid);
            signParamMap.put("systemid", systemid);
            signParamMap.put("terminaltype", terminaltype);
            signParamMap.put("boxId", boxId);
            signParamMap.put("phoneNumber", phoneNumber);
            String sign = CustomRequestSignatureUtil
                    .getSignWithoutKey(signParamMap);
            signParamMap.put("sign", sign);
        }
        return signParamMap;
    }

    /**
     * 回调商户补货单相关签名参数构造
     * 
     * @param boxIdPara
     * @param phoneNumberPara
     * @param openDoorRequestSerialNumber
     * @param mchId
     * @return
     */
    public static Map<String, Object> getReplenishOrderCallbackSignatureParamsMap(
            String boxIdPara, String workerIdPara, String mchId) {
        Map<String, Object> signParamMap = null;
        // 小e微店
        if (mchId.equals(CustomConfig.XIAOE_MCHID)
                || mchId.equals(CustomConfig.XIAOE_TEST_MCHID)) {
            // 加入商户开门请求时的requestSerial
            CustomRequestInfoService customRequestInfoService = new CustomRequestInfoService();
            CustomRequestInfo customRequestInfo = customRequestInfoService
                    .findCustomRequestInfoByBoxId(boxIdPara);
            // 商户开门请求时的requestSerial
            String requestSerial = customRequestInfo.getOpenDoorRequestSerial();
            // 合作伙伴发起请求的时间，格式：yyyyMMddHHmmss
            String reqtime = new SimpleDateFormat("yyyyMMddHHmmss")
                    .format(new Date());
            // 1-XML,2-JSON
            String returntype = "2";
            // 接口版本号
            String version = "1.0";
            // 商户ID标识
            String appid = "wisdcat";
            // 请求系统编号
            String systemid = "wisdcat101";
            // 请求终端类型
            String terminaltype = "wisdcat";

            // 当前购物售货机ID
            String boxId = boxIdPara;
            // 当前购物者电话号码
            String workerId = workerIdPara;

            // 签名
            signParamMap = new LinkedHashMap<String, Object>();
            signParamMap.put("requestSerial", requestSerial);
            signParamMap.put("reqtime", reqtime);
            signParamMap.put("returntype", returntype);
            signParamMap.put("version", version);
            signParamMap.put("appid", appid);
            signParamMap.put("systemid", systemid);
            signParamMap.put("terminaltype", terminaltype);
            signParamMap.put("boxId", boxId);
            signParamMap.put("workerId", workerId);
            String sign = CustomRequestSignatureUtil
                    .getSignWithoutKey(signParamMap);
            signParamMap.put("sign", sign);
        }
        return signParamMap;
    }

}
