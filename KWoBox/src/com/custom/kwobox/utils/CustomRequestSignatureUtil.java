package com.custom.kwobox.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import junit.framework.Test;

import com.chinapalms.kwobox.javabean.Customer;
import com.chinapalms.kwobox.javabean.CustomerSecretKey;
import com.chinapalms.kwobox.service.CustomerSecretKeyService;
import com.chinapalms.kwobox.service.CustomerService;
import com.chinapalms.kwobox.utils.MD5;
import com.chinapalms.kwobox.utils.ResponseStatus;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 签名
 * 
 * @author zuoliangzhu
 * 
 */
public class CustomRequestSignatureUtil {

    private static Log log = LogFactory
            .getLog(CustomRequestSignatureUtil.class);

    /**
     * 签名算法
     * 
     * @param o
     *            要参与签名的数据对象
     * @return 签名
     * @throws IllegalAccessException
     */
    public static String getSign(Object o, String customerSecretKey)
            throws IllegalAccessException {
        ArrayList<String> list = new ArrayList<String>();
        Class<?> cls = o.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            if (f.get(o) != null && f.get(o) != "") {
                String name = f.getName();
                XStreamAlias anno = f.getAnnotation(XStreamAlias.class);
                if (anno != null)
                    name = anno.value();
                list.add(name + "=" + f.get(o) + "&");
            }
        }
        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result += "key=" + customerSecretKey;
        result = MD5.MD5Encode(result).toUpperCase();
        return result;
    }

    /**
     * 构造签名参数（含秘钥key模式）
     * 
     * @param map
     * @param customerSecretKey
     * @return
     */
    public static String getSign(Map<String, Object> map,
            String customerSecretKey) {
        ArrayList<String> list = new ArrayList<String>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() != "") {
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result += "key=" + customerSecretKey;
        result = MD5.MD5Encode(result).toUpperCase();
        return result;
    }

    /**
     * 构造签名参数（不包含秘钥key模式）
     * 
     * @param map
     * @param customerSecretKey
     * @return
     */
    public static String getSignWithoutKey(Map<String, Object> map) {
        ArrayList<String> list = new ArrayList<String>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() != "") {
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        // 不加key的情况下最后一个"&"不必添加
        if (result != null) {
            result = result.substring(0, result.lastIndexOf("&"));
        }
        // result += "key=" + customerSecretKey;
        // result = MD5.MD5Encode(result).toUpperCase();
        // MD5签名以后不转化为大写
        result = MD5.MD5Encode(result);
        return result;
    }

    /**
     * 检查签名是否正确
     * 
     * @param signParaMap
     * @return
     */
    public static String checkSign(String mchId, String nonceStr,
            String requestSerial, String timeStamp, String signFromCustom) {
        log.info("checkSign:" + "mchId=" + mchId + ",nonceStr=" + nonceStr
                + ",requestSerial=" + requestSerial + ",timeStamp=" + timeStamp
                + ",signFromCustom=" + signFromCustom);
        String checkSignResult = ResponseStatus.FAIL;
        String resultMsg = CustomResponseStatus.RESULT_MSG_UNKNOW;
        int errorCode = CustomResponseStatus.ERR_CODE_UNKNOW;
        String errorCodeDes = CustomResponseStatus.ERR_CODE_DES_UNKNOW;
        if (mchId != null && nonceStr != null && requestSerial != null
                && timeStamp != null && signFromCustom != null) {
            Map<String, Object> signParaMap = new HashMap<String, Object>();
            signParaMap.put("mchId", mchId);
            signParaMap.put("nonceStr", nonceStr);
            signParaMap.put("requestSerial", requestSerial);
            signParaMap.put("timeStamp", timeStamp);
            // 根据商户ID查找商户秘钥
            CustomerService customerService = new CustomerService();
            Customer customer = customerService.findCustomerByMchId(mchId);
            if (customer != null) {
                CustomerSecretKeyService customerSecretKeyService = new CustomerSecretKeyService();
                CustomerSecretKey customerSecretKey = customerSecretKeyService
                        .findCustomerSecretKeyByCustomerId(customer
                                .getCustomerId());
                if (customerSecretKey != null) {
                    String signLocale = getSign(signParaMap,
                            customerSecretKey.getSecretKey());
                    log.info("checkSign:" + "signFromCustom=" + signFromCustom
                            + ",signLocale=" + signLocale);
                    if (signFromCustom.equals(signLocale)) {
                        checkSignResult = ResponseStatus.SUCCESS;
                        resultMsg = CustomResponseStatus.RESULT_MSG_REQUEST_SIGN_CHECK_SUCCESS;
                    } else {
                        checkSignResult = ResponseStatus.FAIL;
                        resultMsg = CustomResponseStatus.RESULT_MSG_REQUEST_SIGN_CHECK_FAIL;
                        errorCode = CustomResponseStatus.ERR_CODE_REQUEST_SIGN_FAIL;
                        errorCodeDes = CustomResponseStatus.ERR_CODE_DES_REQUEST_SIGN_FAIL;
                    }
                } else {
                    checkSignResult = ResponseStatus.FAIL;
                    resultMsg = CustomResponseStatus.RESULT_MSG_REQUEST_SIGN_CHECK_FAIL;
                    errorCode = CustomResponseStatus.ERR_CODE_REQUEST_SIGN_FAIL;
                    errorCodeDes = CustomResponseStatus.ERR_CODE_DES_REQUEST_SIGN_FAIL;
                }
            } else {
                checkSignResult = ResponseStatus.FAIL;
                resultMsg = CustomResponseStatus.RESULT_MSG_REQUEST_SIGN_CHECK_FAIL;
                errorCode = CustomResponseStatus.ERR_CODE_REQUEST_SIGN_FAIL;
                errorCodeDes = CustomResponseStatus.ERR_CODE_DES_REQUEST_SIGN_FAIL;
            }
        } else {
            checkSignResult = ResponseStatus.FAIL;
            resultMsg = CustomResponseStatus.RESULT_MSG_REQUEST_SIGN_CHECK_FAIL;
            errorCode = CustomResponseStatus.ERR_CODE_REQUEST_SIGN_FAIL;
            errorCodeDes = CustomResponseStatus.ERR_CODE_DES_REQUEST_SIGN_FAIL;
        }

        // 校验完成后，将校验结果封装起来传给Sevlet处理
        JSONObject jsonObject = new JSONObject();
        if (checkSignResult.equals(ResponseStatus.SUCCESS)) {
            jsonObject.put("actionResult", checkSignResult);
            jsonObject.put("resultMsg", resultMsg);
        } else {
            jsonObject.put("actionResult", checkSignResult);
            jsonObject.put("resultMsg", resultMsg);
            jsonObject.put("errCode", errorCode);
            jsonObject.put("errCodeDes", errorCodeDes);
        }
        return jsonObject.toString();
    }

}
