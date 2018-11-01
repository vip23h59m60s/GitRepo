package com.custom.kwobox.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONObject;

import com.chinapalms.kwobox.javabean.Boxes;
import com.chinapalms.kwobox.javabean.Customer;
import com.chinapalms.kwobox.javabean.DynamicPasswordCallback;
import com.chinapalms.kwobox.javabean.FaceDetectCallback;
import com.chinapalms.kwobox.service.BoxesService;
import com.chinapalms.kwobox.service.CustomerService;
import com.chinapalms.kwobox.service.DynamicPasswordCallbackService;
import com.chinapalms.kwobox.service.FaceDetectCallbackService;
import com.chinapalms.kwobox.utils.HttpUtil;
import com.chinapalms.kwobox.utils.ResponseStatus;

public class CustomFaceDetectService {

    Log log = LogFactory.getLog(CustomFaceDetectService.class);

    // 获取人脸识别商户回调Url
    public String getCustomerFaceDetectCallbackUrl(String boxId) {
        BoxesService boxesService = new BoxesService();
        Boxes boxes = boxesService.findBoxesByBoxId(boxId);
        if (boxes != null) {
            // 根据商户ID查询商户合作模式
            CustomerService customerService = new CustomerService();
            Customer customer = customerService.findCustomerByCustomerId(boxes
                    .getCustomerId());
            // 判断是否是小e或Yoho服务器对接模式
            if (customer != null) {
                FaceDetectCallbackService faceDetectCallbackService = new FaceDetectCallbackService();
                FaceDetectCallback faceDetectCallback = faceDetectCallbackService
                        .findFaceDetectCallbackByCustomerId(boxes
                                .getCustomerId());
                int cooperationMode = customer.getCooperationMode();
                // 判断是否是小e服务器堆积或Yoho代理模式
                if (cooperationMode == ResponseStatus.COOPERATIONMODE_THIRD_INTERFACE
                        || cooperationMode == ResponseStatus.COOPERATIONMODE_AGENT) {
                    // 如果是小e或Yoho模式将url和urlType成Json风格返回
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("urlType", cooperationMode);
                    if (faceDetectCallback != null) {
                        // 当数据库中回调Url不为空
                        jsonObject.put("url",
                                faceDetectCallback.getFaceDetectCallbackUrl());
                    } else {
                        // 当数据库中回调Url为空
                        jsonObject
                                .put("url",
                                        ResponseStatus.COOPERATIONMODE_THIRD_NULL_CALLBACK_URL);
                    }
                    return jsonObject.toString();
                } else {
                    // 如果不是小e或Yoho模式，直接返回FAIL
                    return ResponseStatus.FAIL;
                }
            }
            // 如果不是小e服务器对接模式，直接返回FAIL
        } else {
            return ResponseStatus.FAIL;
        }
        return ResponseStatus.FAIL;
    }

    /**
     * 将人脸识别信息回调给商户服务器进行校验
     * 
     * @param dynamicPasswordCallbackUrlObject
     * @param boxId
     * @param dynamicPassword
     * @return
     */
    public String notifyCustomerServerToCheckFaceDetect(
            String faceDetectCallbackUrlObject, String boxId, String phoneNumber) {
        CustomResponseService customResponseService = new CustomResponseService();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("boxId", boxId);
        jsonObject.put("phoneNumber", phoneNumber);
        String responseToCustom = customResponseService
                .getReturnSuccessAndResultSuccessResponse(jsonObject.toString());
        String faceDetectCallbackUrl = JSONObject.fromObject(
                faceDetectCallbackUrlObject).getString("url");
        String checkResult = HttpUtil.sendCallbackResultMsg(
                faceDetectCallbackUrl, JSONObject.fromObject(responseToCustom));
        log.info("notifyCustomerServerToCheckFaceDetect checkResult="
                + checkResult);
        return checkResult;
    }

    /**
     * 通知第三方服务器（Yoho）获取人脸识别照片路径
     * 
     * @param faceDetectCallbackUrlObject
     * @param phoneNumber
     * @return
     */
    public String notifyCustomerServerToGetFaceDetectImage(
            String faceDetectCallbackUrlObject, String phoneNumber) {
        CustomResponseService customResponseService = new CustomResponseService();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("phoneNumber", phoneNumber);
        String responseToCustom = customResponseService
                .getReturnSuccessAndResultSuccessResponse(jsonObject.toString());
        String faceDetectCallbackUrl = JSONObject.fromObject(
                faceDetectCallbackUrlObject).getString("url");
        String checkResult = HttpUtil.sendCallbackResultMsg(
                faceDetectCallbackUrl, JSONObject.fromObject(responseToCustom));
        log.info("notifyCustomerServerToGetFaceDetectImage checkResult="
                + checkResult);
        return checkResult;
    }

}
