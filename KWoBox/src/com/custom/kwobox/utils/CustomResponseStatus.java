package com.custom.kwobox.utils;

public class CustomResponseStatus {

    // 商户服务器返回码
    // return_code:SUCCESS
    // return_msg:xxx
    // result_code:xxxx
    // result_msg:xxxxxx
    // err_code:xxxx
    // err_code_des:xxx
    public final static int RETURN_CODE_SUCCESS = 666;
    public final static int RETURN_CODE_FAIL = 444;
    public final static String RETURN_MSG_SUCCESS = "SUCCESS";
    public final static String RETURN_MSG_FAIL = "FAIL";

    public final static int RESULT_CODE_SUCCESS = 666;
    public final static int RESULT_CODE_FAIL = 444;
    public final static String RESULT_MSG_SUCCESS = "SUCCESS";
    public final static String RESULT_MSG_FAIL = "FAIL";

    // result_msg
    public final static String RESULT_MSG_UNKNOW = "未知错误";
    // 商户服务器签名校验result_msg
    public final static String RESULT_MSG_REQUEST_SIGN_CHECK_SUCCESS = "签名校验成功";
    public final static String RESULT_MSG_REQUEST_SIGN_CHECK_FAIL = "签名校验失败";
    // 开门请求校验成功result_msg
    public final static String RESULT_MSG_OPEN_DOOR_REQUEST_SUCCESS = "开门请求校验成功";
    // 开门请求校验失败result_msg
    public final static String RESULT_MSG_OPEN_DOOR_REQUEST_FAIL = "开门请求校验失败";

    // 开门成功回调 result_msg
    public final static String RESULT_MSG_DOOR_OPENED_SUCCESS = "门已开";

    // err_code && err_code_des
    public final static int ERR_CODE_UNKNOW = -1;
    public final static String ERR_CODE_DES_UNKNOW = "未知错误";
    // checkSign检验请求签名
    public final static int ERR_CODE_REQUEST_SIGN_FAIL = 0;
    public final static String ERR_CODE_DES_REQUEST_SIGN_FAIL = "签名校验失败";
    // 服务中openDoor
    public final static int ERR_CODE_BOX_BUSY = 1;
    public final static String ERR_CODE_DES_BOX_BUSY = "设备服务中";
    public final static int ERR_CODE_BOX_BROKEN_DOWN = 2;
    public final static String ERR_CODE_DES_BOX_BROKEN_DOWN = "设备维护中";
    public final static int ERR_CODE_CUSTOMER_BOX_NOT_FOUND = 4;
    public final static String ERR_CODE_DES_CUSTOMER_BOX_NOT_FOUND = "售货机与商户所属不符";
    public final static int ERR_CODE_ONGOING_ORDER = 5;
    public final static String ERR_CODE_DES_ONGOING_ORDER = "您还有订单正在结算中，请稍后...";
    public final static int ERR_CODE_REGISTER_USER_FAIL = 10;
    public final static String ERR_CODE_DES_REGISTER_USER_FAIL = "登记用户失败";
    public final static int ERR_CODE_REGISTER_REQUEST_INFO_FAIL = 11;
    public final static String ERR_CODE_DES_REGISTER_REQUEST_INFO_FAIL = "登记开门请求信息失败";

    // 理货员扫码开门相关接口状态
    public final static int ERR_CODE_OPEN_NO_PERMISSION = 20;
    public final static String ERR_CODE_DES_OPEN_NO_PERMISSION = "您未获取管理该售货机权限,请与商家确认";

    // 商户获取售货机信息失败result_msg
    public final static String RESULT_MSG_GET_BOX_CARDGOROAD_BOX_GOODS_FAIL = "开门请求校验失败";
    public final static String RESULT_MSG_GET_BOX_CARDGOROAD_BOX_GOODS_SUCCESS = "获取售货机商品信息成功";

}
