package com.custom.kwobox.service;

import com.custom.kwobox.utils.CustomResponseStatus;

import net.sf.json.JSONObject;

public class CustomResponseService {

    // 商户服务器返回码
    // return_code:SUCCESS
    // return_msg:xxx
    // result_code:xxxx
    // result_msg:xxxxxx
    // 请求验证成功时返回格式
    public String getReturnSuccessAndResultSuccessResponse(
            String resultSuccessMsg) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("return_code", CustomResponseStatus.RETURN_CODE_SUCCESS);
        jsonObject.put("return_msg", CustomResponseStatus.RETURN_MSG_SUCCESS);
        jsonObject.put("result_code", CustomResponseStatus.RESULT_CODE_SUCCESS);
        jsonObject.put("result_msg", resultSuccessMsg);
        return jsonObject.toString();
    }

    // 请求验证失败时返回格式
    public String getReturnSuccessButResultFailResponse(String resultFailMsg,
            int errCode, String errCodeDes) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("return_code", CustomResponseStatus.RETURN_CODE_SUCCESS);
        jsonObject.put("return_msg", CustomResponseStatus.RETURN_MSG_SUCCESS);
        jsonObject.put("result_code", CustomResponseStatus.RESULT_CODE_FAIL);
        jsonObject.put("result_msg", resultFailMsg);
        jsonObject.put("err_code", errCode);
        jsonObject.put("err_code_des", errCodeDes);
        return jsonObject.toString();
    }

    // 回调商户服务器，并向商户服务器发送信息
    public String getCallbackCustomerServerResponse(String callbackResultMsg) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("return_code", CustomResponseStatus.RETURN_CODE_SUCCESS);
        jsonObject.put("return_msg", CustomResponseStatus.RETURN_MSG_SUCCESS);
        jsonObject.put("result_code", CustomResponseStatus.RESULT_CODE_SUCCESS);
        jsonObject.put("result_msg", callbackResultMsg);
        return jsonObject.toString();
    }

}
