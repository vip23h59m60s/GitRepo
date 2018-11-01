package com.custom.kwobox.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONObject;

import com.chinapalms.kwobox.javabean.Boxes;
import com.chinapalms.kwobox.javabean.Customer;
import com.chinapalms.kwobox.javabean.DynamicPasswordCallback;
import com.chinapalms.kwobox.service.BoxesService;
import com.chinapalms.kwobox.service.CustomerService;
import com.chinapalms.kwobox.service.DynamicPasswordCallbackService;
import com.chinapalms.kwobox.utils.HttpUtil;
import com.chinapalms.kwobox.utils.ResponseStatus;

public class CustomDynamicPasswordService {

    Log log = LogFactory.getLog(CustomDynamicPasswordService.class);

    // 获取动态密码商户回调Url
    public String getCustomerDynamicPasswordCallbackUrl(String boxId) {
        BoxesService boxesService = new BoxesService();
        Boxes boxes = boxesService.findBoxesByBoxId(boxId);
        if (boxes != null) {
            // 根据商户ID查询商户合作模式
            CustomerService customerService = new CustomerService();
            Customer customer = customerService.findCustomerByCustomerId(boxes
                    .getCustomerId());
            // 判断是否是小e或Yoho服务器对接模式
            if (customer != null) {
                DynamicPasswordCallbackService dynamicPasswordCallbackService = new DynamicPasswordCallbackService();
                DynamicPasswordCallback dynamicPasswordCallback = dynamicPasswordCallbackService
                        .findDynamicPasswordCallbackByCustomerId(boxes
                                .getCustomerId());
                int cooperationMode = customer.getCooperationMode();
                if (dynamicPasswordCallback != null) {
                    // 判断是否是小e服务器堆积或Yoho代理模式
                    if (cooperationMode == ResponseStatus.COOPERATIONMODE_THIRD_INTERFACE
                            || cooperationMode == ResponseStatus.COOPERATIONMODE_AGENT) {
                        // 如果是小e或Yoho模式将url和urlType成Json风格返回
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("urlType", cooperationMode);
                        jsonObject.put("url", dynamicPasswordCallback
                                .getDynamicPasswordCallbackUrl());
                        return jsonObject.toString();
                    } else {
                        // 如果不是小e或Yoho模式，直接返回FAIL
                        return ResponseStatus.FAIL;
                    }
                } else {
                    // 如果不是小e或Yoho模式，直接返回FAIL
                    return ResponseStatus.FAIL;
                }
                // 如果不是小e服务器对接模式，直接返回FAIL
            } else {
                return ResponseStatus.FAIL;
            }
        } else {
            return ResponseStatus.FAIL;
        }
    }

    /**
     * 将动态密码信息回调给商户服务器进行校验
     * 
     * @param dynamicPasswordCallbackUrlObject
     * @param boxId
     * @param dynamicPassword
     * @return
     */
    public String notifyCustomerServerToCheckDynamicPassword(
            String dynamicPasswordCallbackUrlObject, String boxId,
            String dynamicPassword) {
        CustomResponseService customResponseService = new CustomResponseService();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("boxId", boxId);
        jsonObject.put("dynamicPassword", dynamicPassword);
        String responseToCustom = customResponseService
                .getReturnSuccessAndResultSuccessResponse(jsonObject.toString());
        String dynamicPasswordCallbackUrl = JSONObject.fromObject(
                dynamicPasswordCallbackUrlObject).getString("url");
        String checkResult = HttpUtil.sendCallbackResultMsg(
                dynamicPasswordCallbackUrl,
                JSONObject.fromObject(responseToCustom));
        log.info("notifyCustomerServerToCheckDynamicPassword checkResult="
                + checkResult);
        return checkResult;
    }

}
