package com.custom.kwobox.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.impl.BoxesDAOImpl;
import com.chinapalms.kwobox.javabean.BoxGoodsXXX;
import com.chinapalms.kwobox.javabean.BoxManager;
import com.chinapalms.kwobox.javabean.BoxStatus;
import com.chinapalms.kwobox.javabean.Boxes;
import com.chinapalms.kwobox.javabean.CurrentUser;
import com.chinapalms.kwobox.javabean.CustomRequestInfo;
import com.chinapalms.kwobox.javabean.Customer;
import com.chinapalms.kwobox.javabean.CustomerWorker;
import com.chinapalms.kwobox.javabean.SerializeBoxBody;
import com.chinapalms.kwobox.javabean.ShoppingInfo;
import com.chinapalms.kwobox.service.BoxGoodsXXXService;
import com.chinapalms.kwobox.service.BoxManagerService;
import com.chinapalms.kwobox.service.BoxStatusService;
import com.chinapalms.kwobox.service.BoxStructureService;
import com.chinapalms.kwobox.service.BoxesService;
import com.chinapalms.kwobox.service.CustomRequestInfoService;
import com.chinapalms.kwobox.service.CustomerService;
import com.chinapalms.kwobox.service.CustomerWorkerService;
import com.chinapalms.kwobox.service.SerializeBoxBodyService;
import com.chinapalms.kwobox.service.ShoppingInfoService;
import com.chinapalms.kwobox.utils.ResponseStatus;
import com.custom.kwobox.utils.CustomConfig;
import com.custom.kwobox.utils.CustomResponseStatus;

public class CustomBoxesService extends BoxesDAOImpl {

    Log log = LogFactory.getLog(CustomBoxesService.class);

    @Override
    public Boxes findBoxesByBoxId(String boxId) {
        return super.findBoxesByBoxId(boxId);
    }

    @Override
    public Boxes findBoxesByIcsId(int icsId) {
        return super.findBoxesByIcsId(icsId);
    }

    @Override
    public List<Boxes> findNearByBoxes(String fromLat, String fromLng,
            double nearByRange, int pageNumber) {
        return super.findNearByBoxes(fromLat, fromLng, nearByRange, pageNumber);
    }

    @Override
    public int findNearByBoxesCount(String fromLat, String fromLng,
            double nearByRange) {
        return super.findNearByBoxesCount(fromLat, fromLng, nearByRange);
    }

    @Override
    public boolean addBoxes(Boxes boxes) {
        return super.addBoxes(boxes);
    }

    @Override
    public boolean updateBoxRegisterBoxesInfo(Boxes boxes) {
        return super.updateBoxRegisterBoxesInfo(boxes);
    }

    @Override
    public boolean updateLocationLatitudeAndLongitudeByBoxId(String boxId,
            String latitude, String longitude) {
        return super.updateLocationLatitudeAndLongitudeByBoxId(boxId, latitude,
                longitude);
    }

    @Override
    public boolean updateICSId(int icsId) {
        return super.updateICSId(icsId);
    }

    @Override
    public boolean updateSerializeBoxBodyId(int serializeBoxBodyId) {
        return super.updateSerializeBoxBodyId(serializeBoxBodyId);
    }

    @Override
    public List<Boxes> findBoxesByBoxManagerIdAndPageNumber(int boxManagerId,
            int pageNumber) {
        return super.findBoxesByBoxManagerIdAndPageNumber(boxManagerId,
                pageNumber);
    }

    @Override
    public int findBoxesCountByBoxManagerId(int boxManagerId) {
        return super.findBoxesCountByBoxManagerId(boxManagerId);
    }

    @Override
    public Boxes findBoxesBySerializeBoxBodyId(int serializeBoxBodyId) {
        return super.findBoxesBySerializeBoxBodyId(serializeBoxBodyId);
    }

    @Override
    public Boxes findBoxesByBoxIdAndCustomerId(String boxId, int customerId) {
        return super.findBoxesByBoxIdAndCustomerId(boxId, customerId);
    }

    @Override
    public List<Boxes> findNearByBoxesByCustomerId(int customerId,
            String fromLat, String fromLng, double nearByRange, int pageNumber) {
        return super.findNearByBoxesByCustomerId(customerId, fromLat, fromLng,
                nearByRange, pageNumber);
    }

    @Override
    public int findNearByBoxesCountByCustomerId(int customerId, String fromLat,
            String fromLng, double nearByRange) {
        return super.findNearByBoxesCountByCustomerId(customerId, fromLat,
                fromLng, nearByRange);
    }

    /**
     * 普通消费者执行开门指令
     * 
     * @param boxId
     * @param phoneNumber
     * @param userType
     * @return
     */
    public String doOpenDoor(String boxId, String mchId, String phoneNumber,
            String userType, String requestSerial, String doorOpenedNotifyUrl,
            String doorClosedNotifyUrl, String orderNotifyUrl) {
        String openDoorResult = ResponseStatus.FAIL;
        String resultMsg = CustomResponseStatus.RESULT_MSG_UNKNOW;
        int errorCode = CustomResponseStatus.ERR_CODE_UNKNOW;
        String errorCodeDes = CustomResponseStatus.ERR_CODE_DES_UNKNOW;
        try {
            String openDoorIdentifyResult = openDoorIdentify(boxId, mchId);
            if (openDoorIdentifyResult.equals(ResponseStatus.SUCCESS)) {
                // 检查该用户在该商户是否还有未处理异常订单，如果有，则不允许开门
                // 暂时只对小e商户做次判断
                if ((mchId.equals(CustomConfig.XIAOE_MCHID) || mchId
                        .equals(CustomConfig.XIAOE_TEST_MCHID))
                        && checkExceptionShopping(boxId, phoneNumber).equals(
                                ResponseStatus.FAIL)) {
                    openDoorResult = ResponseStatus.FAIL;
                    resultMsg = CustomResponseStatus.RESULT_MSG_OPEN_DOOR_REQUEST_FAIL;
                    errorCode = CustomResponseStatus.ERR_CODE_ONGOING_ORDER;
                    errorCodeDes = CustomResponseStatus.ERR_CODE_DES_ONGOING_ORDER;
                } else {
                    // 售货机开门状态校验通过，继续进行登记当前用户操作
                    CustomCurrentUserService customCurrentUserService = new CustomCurrentUserService();
                    CurrentUser currentUser = new CurrentUser();
                    currentUser.setPhoneNumber(phoneNumber);
                    currentUser.setCustomerWorkerId(0);
                    currentUser.setBoxId(boxId);
                    currentUser
                            .setCustomType(ResponseStatus.CUSTOM_CATEGORY_WX);
                    currentUser
                            .setUserType(userType.equals("0") ? ResponseStatus.PERMISSION_NORMAL_USER
                                    : ResponseStatus.PERMISSION_MANAGER);
                    currentUser.setVipLevel(1);
                    currentUser.setAgreementNO("0");
                    if (customCurrentUserService.registerCurrentUser(
                            currentUser).equals(ResponseStatus.SUCCESS)) {
                        // 登记商户请求的requestSerial，以便回调商户信息时携带该信息
                        CustomRequestInfoService cuRequestInfoService = new CustomRequestInfoService();
                        CustomRequestInfo customRequestInfo = new CustomRequestInfo();
                        customRequestInfo.setBoxId(boxId);
                        customRequestInfo
                                .setOpenDoorRequestSerial(requestSerial);
                        if (cuRequestInfoService.registerCustomRequestInfo(
                                customRequestInfo).equals(
                                ResponseStatus.SUCCESS)) {
                            // 通知开门
                            BoxesService boxesService = new BoxesService();
                            boxesService.notifyAndroidOpenDoor(requestSerial,
                                    ResponseStatus.CUSTOM_CATEGORY_ANDROID,
                                    boxId,
                                    ResponseStatus.PERMISSION_NORMAL_USER);
                            openDoorResult = ResponseStatus.SUCCESS;
                            resultMsg = CustomResponseStatus.RESULT_MSG_OPEN_DOOR_REQUEST_SUCCESS;
                            // 开门请求校验成功时检测是否要更新商户回调Url
                            // 目前只针对小e线下测试柜做此处理
                            if (mchId.equals(CustomConfig.XIAOE_TEST_MCHID)) {
                                try {
                                    CustomUpdateCallbackUrlService customUpdateCallbackUrlService = new CustomUpdateCallbackUrlService();
                                    customUpdateCallbackUrlService
                                            .updateBoxCustomizationCallbackUrl(
                                                    mchId,
                                                    boxId,
                                                    ResponseStatus.PERMISSION_NORMAL_USER,
                                                    doorOpenedNotifyUrl,
                                                    doorClosedNotifyUrl,
                                                    orderNotifyUrl);
                                } catch (Exception e) {
                                    log.error("try->catch exception:", e);
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            // 登记请求开门请求信息失败
                            openDoorResult = ResponseStatus.FAIL;
                            resultMsg = CustomResponseStatus.RESULT_MSG_OPEN_DOOR_REQUEST_FAIL;
                            errorCode = CustomResponseStatus.ERR_CODE_REGISTER_REQUEST_INFO_FAIL;
                            errorCodeDes = CustomResponseStatus.ERR_CODE_DES_REGISTER_REQUEST_INFO_FAIL;
                        }
                    } else {
                        // 登记当前用户失败
                        openDoorResult = ResponseStatus.FAIL;
                        resultMsg = CustomResponseStatus.RESULT_MSG_OPEN_DOOR_REQUEST_FAIL;
                        errorCode = CustomResponseStatus.ERR_CODE_REGISTER_USER_FAIL;
                        errorCodeDes = CustomResponseStatus.ERR_CODE_DES_REGISTER_USER_FAIL;
                    }
                }
            } else {
                // 售货机状态校验失败，未满足开门条件
                openDoorResult = ResponseStatus.FAIL;
                resultMsg = CustomResponseStatus.RESULT_MSG_OPEN_DOOR_REQUEST_FAIL;
                if (openDoorIdentifyResult
                        .equals(ResponseStatus.CUSTOMER_BOX_NOT_FOUND)) {
                    errorCode = CustomResponseStatus.ERR_CODE_CUSTOMER_BOX_NOT_FOUND;
                    errorCodeDes = CustomResponseStatus.ERR_CODE_DES_CUSTOMER_BOX_NOT_FOUND;
                } else if (openDoorIdentifyResult
                        .equals(ResponseStatus.BOX_BUSY)) {
                    errorCode = CustomResponseStatus.ERR_CODE_BOX_BUSY;
                    errorCodeDes = CustomResponseStatus.ERR_CODE_DES_BOX_BUSY;
                } else if (openDoorIdentifyResult
                        .equals(ResponseStatus.BOX_BROKEN_DOWN)) {
                    errorCode = CustomResponseStatus.ERR_CODE_BOX_BROKEN_DOWN;
                    errorCodeDes = CustomResponseStatus.ERR_CODE_DES_BOX_BROKEN_DOWN;
                } else {
                    errorCode = CustomResponseStatus.ERR_CODE_UNKNOW;
                    errorCodeDes = CustomResponseStatus.ERR_CODE_DES_UNKNOW;
                }
            }
        } catch (Exception e) {
            openDoorResult = ResponseStatus.FAIL;
            resultMsg = CustomResponseStatus.RESULT_MSG_OPEN_DOOR_REQUEST_FAIL;
            errorCode = CustomResponseStatus.ERR_CODE_UNKNOW;
            errorCodeDes = CustomResponseStatus.ERR_CODE_DES_UNKNOW;
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        // 将结果封装传递个Sevlet处理
        JSONObject jsonObject = new JSONObject();
        if (openDoorResult.equals(ResponseStatus.SUCCESS)) {
            jsonObject.put("actionResult", openDoorResult);
            jsonObject.put("resultMsg", resultMsg);
        } else {
            jsonObject.put("actionResult", openDoorResult);
            jsonObject.put("resultMsg", resultMsg);
            jsonObject.put("errCode", errorCode);
            jsonObject.put("errCodeDes", errorCodeDes);
        }

        JSONObject openDoorResultJsonObject = JSONObject.fromObject(jsonObject);
        String actionResult = openDoorResultJsonObject
                .getString("actionResult");
        String openDoorResultMsg = openDoorResultJsonObject
                .getString("resultMsg");

        CustomResponseService customResponseService = new CustomResponseService();
        String responseToCustom = null;
        if (actionResult.equals(ResponseStatus.SUCCESS)) {
            responseToCustom = customResponseService
                    .getReturnSuccessAndResultSuccessResponse(openDoorResultMsg);
        } else {
            int openDoorResultErrorCode = openDoorResultJsonObject
                    .getInt("errCode");
            String openDoorResultErrorCodeDes = openDoorResultJsonObject
                    .getString("errCodeDes");
            responseToCustom = customResponseService
                    .getReturnSuccessButResultFailResponse(openDoorResultMsg,
                            openDoorResultErrorCode, openDoorResultErrorCodeDes);
        }
        return responseToCustom.toString();
    }

    /**
     * 理货员执行开门指令
     * 
     * @param boxId
     * @param phoneNumber
     * @param userType
     * @return
     */
    public String doManagerOpenDoor(String boxId, String mchId,
            String customerWorkerId, String userType, String requestSerial,
            String doorOpenedNotifyUrl, String doorClosedNotifyUrl,
            String orderNotifyUrl) {
        String openDoorResult = ResponseStatus.FAIL;
        String resultMsg = CustomResponseStatus.RESULT_MSG_UNKNOW;
        int errorCode = CustomResponseStatus.ERR_CODE_UNKNOW;
        String errorCodeDes = CustomResponseStatus.ERR_CODE_DES_UNKNOW;
        try {
            String openDoorIdentifyResult = managerOpenDoorIdentify(boxId,
                    mchId, customerWorkerId);
            if (openDoorIdentifyResult.equals(ResponseStatus.SUCCESS)) {
                // 售货机开门状态校验通过，继续进行登记当前用户操作
                CustomCurrentUserService customCurrentUserService = new CustomCurrentUserService();
                CurrentUser currentUser = new CurrentUser();
                currentUser.setPhoneNumber("0");
                currentUser.setCustomerWorkerId(Integer
                        .valueOf(customerWorkerId));
                currentUser.setBoxId(boxId);
                currentUser.setCustomType(ResponseStatus.CUSTOM_CATEGORY_WX);
                currentUser.setUserType(ResponseStatus.PERMISSION_MANAGER);
                currentUser.setVipLevel(1);
                currentUser.setAgreementNO("0");
                if (customCurrentUserService.registerCurrentUser(currentUser)
                        .equals(ResponseStatus.SUCCESS)) {
                    // 登记商户请求的requestSerial，以便回调商户信息时携带该信息
                    CustomRequestInfoService cuRequestInfoService = new CustomRequestInfoService();
                    CustomRequestInfo customRequestInfo = new CustomRequestInfo();
                    customRequestInfo.setBoxId(boxId);
                    customRequestInfo.setOpenDoorRequestSerial(requestSerial);
                    if (cuRequestInfoService.registerCustomRequestInfo(
                            customRequestInfo).equals(ResponseStatus.SUCCESS)) {
                        // 通知开门
                        BoxesService boxesService = new BoxesService();
                        boxesService.notifyAndroidOpenDoorForManager(
                                ResponseStatus.CUSTOM_CATEGORY_ANDROID, boxId,
                                ResponseStatus.PERMISSION_MANAGER,
                                ResponseStatus.BOX_MANAGER_SORT_GOODS);
                        openDoorResult = ResponseStatus.SUCCESS;
                        resultMsg = CustomResponseStatus.RESULT_MSG_OPEN_DOOR_REQUEST_SUCCESS;
                        // 开门请求校验成功时检测是否要更新商户回调Url
                        // 目前只针对小e线下测试柜做此处理
                        if (mchId.equals(CustomConfig.XIAOE_TEST_MCHID)) {
                            try {
                                CustomUpdateCallbackUrlService customUpdateCallbackUrlService = new CustomUpdateCallbackUrlService();
                                customUpdateCallbackUrlService
                                        .updateBoxCustomizationCallbackUrl(
                                                mchId,
                                                boxId,
                                                ResponseStatus.PERMISSION_MANAGER,
                                                doorOpenedNotifyUrl,
                                                doorClosedNotifyUrl,
                                                orderNotifyUrl);
                            } catch (Exception e) {
                                log.error("try->catch exception:", e);
                                e.printStackTrace();
                            }
                        }
                    } else {
                        // 登记请求开门请求信息失败
                        openDoorResult = ResponseStatus.FAIL;
                        resultMsg = CustomResponseStatus.RESULT_MSG_OPEN_DOOR_REQUEST_FAIL;
                        errorCode = CustomResponseStatus.ERR_CODE_REGISTER_REQUEST_INFO_FAIL;
                        errorCodeDes = CustomResponseStatus.ERR_CODE_DES_REGISTER_REQUEST_INFO_FAIL;
                    }
                } else {
                    // 登记当前用户失败
                    openDoorResult = ResponseStatus.FAIL;
                    resultMsg = CustomResponseStatus.RESULT_MSG_OPEN_DOOR_REQUEST_FAIL;
                    errorCode = CustomResponseStatus.ERR_CODE_REGISTER_USER_FAIL;
                    errorCodeDes = CustomResponseStatus.ERR_CODE_DES_REGISTER_USER_FAIL;
                }
            } else {
                // 售货机状态校验失败，未满足开门条件
                openDoorResult = ResponseStatus.FAIL;
                resultMsg = CustomResponseStatus.RESULT_MSG_OPEN_DOOR_REQUEST_FAIL;
                if (openDoorIdentifyResult
                        .equals(ResponseStatus.CUSTOMER_BOX_NOT_FOUND)) {
                    errorCode = CustomResponseStatus.ERR_CODE_CUSTOMER_BOX_NOT_FOUND;
                    errorCodeDes = CustomResponseStatus.ERR_CODE_DES_CUSTOMER_BOX_NOT_FOUND;
                } else if (openDoorIdentifyResult
                        .equals(ResponseStatus.BOX_OPEN_NO_PERMISSION)) {
                    errorCode = CustomResponseStatus.ERR_CODE_OPEN_NO_PERMISSION;
                    errorCodeDes = CustomResponseStatus.ERR_CODE_DES_OPEN_NO_PERMISSION;
                } else if (openDoorIdentifyResult
                        .equals(ResponseStatus.BOX_BUSY)) {
                    errorCode = CustomResponseStatus.ERR_CODE_BOX_BUSY;
                    errorCodeDes = CustomResponseStatus.ERR_CODE_DES_BOX_BUSY;
                } else {
                    errorCode = CustomResponseStatus.ERR_CODE_UNKNOW;
                    errorCodeDes = CustomResponseStatus.ERR_CODE_DES_UNKNOW;
                }
            }
        } catch (Exception e) {
            openDoorResult = ResponseStatus.FAIL;
            resultMsg = CustomResponseStatus.RESULT_MSG_OPEN_DOOR_REQUEST_FAIL;
            errorCode = CustomResponseStatus.ERR_CODE_UNKNOW;
            errorCodeDes = CustomResponseStatus.ERR_CODE_DES_UNKNOW;
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        // 将结果封装传递个Sevlet处理
        JSONObject jsonObject = new JSONObject();
        if (openDoorResult.equals(ResponseStatus.SUCCESS)) {
            jsonObject.put("actionResult", openDoorResult);
            jsonObject.put("resultMsg", resultMsg);
        } else {
            jsonObject.put("actionResult", openDoorResult);
            jsonObject.put("resultMsg", resultMsg);
            jsonObject.put("errCode", errorCode);
            jsonObject.put("errCodeDes", errorCodeDes);
        }

        JSONObject openDoorResultJsonObject = JSONObject.fromObject(jsonObject);
        String actionResult = openDoorResultJsonObject
                .getString("actionResult");
        String openDoorResultMsg = openDoorResultJsonObject
                .getString("resultMsg");

        CustomResponseService customResponseService = new CustomResponseService();
        String responseToCustom = null;
        if (actionResult.equals(ResponseStatus.SUCCESS)) {
            responseToCustom = customResponseService
                    .getReturnSuccessAndResultSuccessResponse(openDoorResultMsg);
        } else {
            int openDoorResultErrorCode = openDoorResultJsonObject
                    .getInt("errCode");
            String openDoorResultErrorCodeDes = openDoorResultJsonObject
                    .getString("errCodeDes");
            responseToCustom = customResponseService
                    .getReturnSuccessButResultFailResponse(openDoorResultMsg,
                            openDoorResultErrorCode, openDoorResultErrorCodeDes);
        }
        return responseToCustom.toString();
    }

    /**
     * 验证当前柜门状态0:正常运营中, 1、占用中，即服务中；2、维护中；3、校验失败
     * 
     * @param boxId
     *            售货机ID
     * @param mchId
     *            商户ID
     * @return
     */
    public String openDoorIdentify(String boxId, String mchId) {
        // 根据mchId查找出商户的customerId
        CustomerService customerService = new CustomerService();
        Customer customer = customerService.findCustomerByMchId(mchId);
        Boxes queryBox = findBoxesByBoxIdAndCustomerId(boxId,
                customer.getCustomerId());
        if (queryBox != null) {
            BoxStatusService boxStatusService = new BoxStatusService();
            BoxStatus boxStatus = boxStatusService.findBoxStatusByBoxId(boxId);
            if (boxStatus != null) {
                int state = boxStatus.getState();
                if (state == ResponseStatus.BOX_STATUS_BUSY) {
                    return ResponseStatus.BOX_BUSY;
                } else if (state == ResponseStatus.BOX_STATUS_BROKEN_DOWN) {
                    return ResponseStatus.BOX_BROKEN_DOWN;
                } else {
                    return ResponseStatus.SUCCESS;
                }
            } else {
                return ResponseStatus.SUCCESS;
            }
        } else {
            return ResponseStatus.CUSTOMER_BOX_NOT_FOUND;
        }
    }

    /**
     * 商户补货员扫码开门验证
     * 
     * @param boxId
     * @param customerWorkerId
     * @return
     */
    public String managerOpenDoorIdentify(String boxId, String mchId,
            String customerWorkerId) {
        // 根据mchId查找出商户的customerId
        // 判断该售货机是否属于该商户
        CustomerService customerService = new CustomerService();
        Customer customer = customerService.findCustomerByMchId(mchId);
        Boxes queryBox = findBoxesByBoxIdAndCustomerId(boxId,
                customer.getCustomerId());
        if (queryBox != null) {
            // 判断该customerWorkerId是否具有该售货机的理货权限
            CustomerWorkerService customerWorkerService = new CustomerWorkerService();
            CustomerWorker customerWorker = customerWorkerService
                    .findCustomerWorkerByCustomerWorkerId(Integer
                            .valueOf(customerWorkerId));
            BoxManagerService boxManagerService = new BoxManagerService();
            BoxManager boxManager = boxManagerService
                    .findBoxManagerByBoxIdAndDeliveryId(boxId,
                            Integer.valueOf(customerWorkerId));
            // 判断是否是具有该柜子理货权限
            if (customerWorker != null
                    && customerWorker.getCustomerId() == queryBox
                            .getCustomerId() && boxManager != null) {
                BoxStatusService boxStatusService = new BoxStatusService();
                BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
                BoxStatus boxStatus = boxStatusService
                        .findBoxStatusByBoxId(boxId);
                if (boxStatus != null) {
                    int state = boxStatus.getState();
                    if (state == ResponseStatus.BOX_STATUS_BUSY) {
                        return ResponseStatus.BOX_BUSY;
                        // 判断后台货柜货道商品已发生变化
                        /*
                         * } else if
                         * (boxGoodsXXXService.checkBoxGoodsChanged(boxId)) {
                         * return ResponseStatus.BOX_UPDATED;
                         */
                    } else if (state == ResponseStatus.BOX_STATUS_BROKEN_DOWN) {
                        // 临时修改
                        // return ResponseStatus.BOX_BROKEN_DOWN;
                        return ResponseStatus.SUCCESS;
                    } else {
                        return ResponseStatus.SUCCESS;
                    }
                } else {
                    // 判断后台货柜货道商品已发生变化
                    /*
                     * if (boxGoodsXXXService.checkBoxGoodsChanged(boxId)) {
                     * return ResponseStatus.BOX_UPDATED; } else { return
                     * ResponseStatus.SUCCESS; }
                     */
                    // 暂不判断货道商品是否已发生变化
                    return ResponseStatus.SUCCESS;
                }
            } else {
                return ResponseStatus.BOX_OPEN_NO_PERMISSION;
            }
        } else {
            return ResponseStatus.CUSTOMER_BOX_NOT_FOUND;
        }
    }

    // 判断针对同一消费者是否还有未处理异常订单，如果有未处理异常订单，则不允许该用户继续打开售货机对应商户的柜子
    public String checkExceptionShopping(String boxId, String phoneNumber) {
        ShoppingInfoService shoppingInfoService = new ShoppingInfoService();
        BoxesService boxesService = new BoxesService();
        Boxes boxes = boxesService.findBoxesByBoxId(boxId);

        List<ShoppingInfo> shoppingInfosList = shoppingInfoService
                .findShoppingInfosByCustomerIdAndPhoneNumberAndState(
                        boxes.getCustomerId(), phoneNumber,
                        ResponseStatus.SHOPPING_INFO_STATE_CLOSED);
        if (shoppingInfosList != null && shoppingInfosList.size() > 0) {
            // 还有异常视频单未处理
            return ResponseStatus.FAIL;
        } else {
            return ResponseStatus.SUCCESS;
        }
    }

    // 补货时获取当前柜子货道商品结构
    public String doGetBoxCardgoRoadGoods(String boxId, String mchId) {
        String getBoxGoodsResult = ResponseStatus.FAIL;
        String resultMsg = CustomResponseStatus.RESULT_MSG_UNKNOW;
        int errorCode = CustomResponseStatus.ERR_CODE_UNKNOW;
        String errorCodeDes = CustomResponseStatus.ERR_CODE_DES_UNKNOW;
        try {
            String identifyResult = doGetCurrentBoxGoodsIdentify(boxId, mchId);
            if (identifyResult.equals(ResponseStatus.SUCCESS)) {
                BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
                List<BoxGoodsXXX> boxGoodsXXXsCurrentList = boxGoodsXXXService
                        .findAllBoxGoodsXXX(boxId);
                JSONObject currentAndNewBoxGoodsBeanObject = new JSONObject();
                JSONArray boxGoodsXXXCurrentJsonArray = new JSONArray();
                // 当前售货柜商品结构
                for (int i = 0; i < boxGoodsXXXsCurrentList.size(); i++) {
                    BoxGoodsXXX boxGoodsXXXCurrent = boxGoodsXXXsCurrentList
                            .get(i);
                    JSONObject boxGoodsXXXBeanJsonObject = JSONObject
                            .fromObject(boxGoodsXXXCurrent);
                    boxGoodsXXXBeanJsonObject
                            .put("goodsPicture",
                                    boxGoodsXXXCurrent.getGoodsPicture() != null ? (boxGoodsXXXCurrent
                                            .getGoodsPicture().split(",")[0] != null ? ResponseStatus.SERVER_APPCHA_URL
                                            + boxGoodsXXXCurrent
                                                    .getGoodsPicture().split(
                                                            ",")[0]
                                            : "pictureNotFound")
                                            : "pictureNotFound");
                    boxGoodsXXXBeanJsonObject.put("goodsPrice",
                            boxGoodsXXXCurrent.getGoodsPrice().setScale(2)
                                    .toString());
                    boxGoodsXXXBeanJsonObject.put("favourable",
                            boxGoodsXXXCurrent.getFavourable().setScale(2)
                                    .toString());
                    BigDecimal goodsActualPrice = (boxGoodsXXXCurrent
                            .getGoodsPrice())
                            .multiply(boxGoodsXXXCurrent.getGoodsDiscount())
                            .subtract(boxGoodsXXXCurrent.getFavourable())
                            .setScale(2, BigDecimal.ROUND_UP);
                    boxGoodsXXXBeanJsonObject.put("goodsActualPrice",
                            goodsActualPrice.toString());
                    JSONObject boxGoodsXXXObjectJsonObject = new JSONObject();
                    boxGoodsXXXObjectJsonObject.put("BoxGoods",
                            boxGoodsXXXBeanJsonObject);
                    boxGoodsXXXCurrentJsonArray
                            .add(boxGoodsXXXObjectJsonObject);
                }

                currentAndNewBoxGoodsBeanObject.put("BoxCardgoRoadGoods",
                        boxGoodsXXXCurrentJsonArray);

                // 获取售货机层数和每层货道数信息
                BoxesService boxesService = new BoxesService();
                SerializeBoxBodyService serializeBoxBodyService = new SerializeBoxBodyService();
                BoxStructureService boxStructureService = new BoxStructureService();
                Boxes boxes = boxesService.findBoxesByBoxId(boxId);
                SerializeBoxBody serializeBoxBody = serializeBoxBodyService
                        .findSerializeBoxBodyBySerializeBoxBodyId(boxes
                                .getSerializeBoxBodyId());
                currentAndNewBoxGoodsBeanObject
                        .put("LayerAndCardgoRoadInfo",
                                boxStructureService
                                        .getCardgoRoadAndLayerStructureInfo(serializeBoxBody
                                                .getStructureId()));
                getBoxGoodsResult = ResponseStatus.SUCCESS;
                resultMsg = currentAndNewBoxGoodsBeanObject.toString();
            } else {
                // 售货机状态校验失败，未满足开门条件
                getBoxGoodsResult = ResponseStatus.FAIL;
                resultMsg = CustomResponseStatus.RESULT_MSG_GET_BOX_CARDGOROAD_BOX_GOODS_FAIL;
                if (identifyResult
                        .equals(ResponseStatus.CUSTOMER_BOX_NOT_FOUND)) {
                    errorCode = CustomResponseStatus.ERR_CODE_CUSTOMER_BOX_NOT_FOUND;
                    errorCodeDes = CustomResponseStatus.ERR_CODE_DES_CUSTOMER_BOX_NOT_FOUND;
                } else {
                    errorCode = CustomResponseStatus.ERR_CODE_UNKNOW;
                    errorCodeDes = CustomResponseStatus.ERR_CODE_DES_UNKNOW;
                }
            }

        } catch (Exception e) {
            getBoxGoodsResult = ResponseStatus.FAIL;
            resultMsg = CustomResponseStatus.RESULT_MSG_OPEN_DOOR_REQUEST_FAIL;
            errorCode = CustomResponseStatus.ERR_CODE_UNKNOW;
            errorCodeDes = CustomResponseStatus.ERR_CODE_DES_UNKNOW;
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }

        JSONObject jsonObject = new JSONObject();
        if (getBoxGoodsResult.equals(ResponseStatus.SUCCESS)) {
            jsonObject.put("actionResult", getBoxGoodsResult);
            jsonObject.put("resultMsg", resultMsg);
        } else {
            jsonObject.put("actionResult", getBoxGoodsResult);
            jsonObject.put("resultMsg", resultMsg);
            jsonObject.put("errCode", errorCode);
            jsonObject.put("errCodeDes", errorCodeDes);
        }

        JSONObject getBoxGoodsResultJsonObject = JSONObject
                .fromObject(jsonObject);
        String actionResult = getBoxGoodsResultJsonObject
                .getString("actionResult");
        String getBoxGoodsResultMsg = getBoxGoodsResultJsonObject
                .getString("resultMsg");

        CustomResponseService customResponseService = new CustomResponseService();
        String responseToCustom = null;
        if (actionResult.equals(ResponseStatus.SUCCESS)) {
            responseToCustom = customResponseService
                    .getReturnSuccessAndResultSuccessResponse(getBoxGoodsResultMsg);
        } else {
            int openDoorResultErrorCode = getBoxGoodsResultJsonObject
                    .getInt("errCode");
            String openDoorResultErrorCodeDes = getBoxGoodsResultJsonObject
                    .getString("errCodeDes");
            responseToCustom = customResponseService
                    .getReturnSuccessButResultFailResponse(
                            getBoxGoodsResultMsg, openDoorResultErrorCode,
                            openDoorResultErrorCodeDes);
        }
        return responseToCustom.toString();
    }

    private String doGetCurrentBoxGoodsIdentify(String boxId, String mchId) {
        // 根据mchId查找出商户的customerId
        // 判断该售货机是否属于该商户
        CustomerService customerService = new CustomerService();
        Customer customer = customerService.findCustomerByMchId(mchId);
        Boxes queryBox = findBoxesByBoxIdAndCustomerId(boxId,
                customer.getCustomerId());
        if (queryBox != null) {
            return ResponseStatus.SUCCESS;
        } else {
            return ResponseStatus.CUSTOMER_BOX_NOT_FOUND;
        }
    }

    /**
     * 分页查询附近柜子逻辑
     * 
     * @param fromLat
     * @param fromLng
     * @param nearByRange
     * @param pageNumber
     * @return
     */
    public String doFindNearbyBoxes(String mchId, String fromLat,
            String fromLng, double nearByRange, int pageNumber) {
        CustomerService customerService = new CustomerService();
        Customer customer = customerService.findCustomerByMchId(mchId);
        int count = findNearByBoxesCountByCustomerId(customer.getCustomerId(),
                fromLat, fromLng, nearByRange);
        int totalPages = 0;
        // 计算总页数
        if (count % Boxes.PAGE_SIZE == 0) {
            // 对总页数赋值
            totalPages = count / Boxes.PAGE_SIZE;
        } else {
            // 对总页数赋值
            totalPages = count / Boxes.PAGE_SIZE + 1;
        }

        List<Boxes> nearByBoxesList = findNearByBoxesByCustomerId(
                customer.getCustomerId(), fromLat, fromLng, nearByRange,
                pageNumber);
        log.info("BoxesService->nearByBoxesList.size=" + nearByBoxesList.size());

        if (pageNumber == 1 && nearByBoxesList.size() <= 0) {
            return ResponseStatus.NO_BOXES;
        } else if (pageNumber <= totalPages && nearByBoxesList.size() > 0) {
            JSONObject boxesJsonObject = new JSONObject();
            JSONArray boxesJsonArray = new JSONArray();
            // 同时查询柜子状态
            BoxStatusService boxStatusService = new BoxStatusService();
            for (int i = 0; i < nearByBoxesList.size(); i++) {
                log.info("BoxesService->nearByBoxesList="
                        + nearByBoxesList.get(i));
                Boxes boxes = nearByBoxesList.get(i);
                JSONObject boxBeanJsonObject = JSONObject.fromObject(boxes);
                BoxStatus boxStatusBean = boxStatusService
                        .findBoxStatusByBoxId(boxes.getBoxId());
                int boxState = 0;
                if (boxStatusBean != null) {
                    boxState = boxStatusBean.getState();
                }
                boxBeanJsonObject.put("boxState", boxState);
                if (boxState == 0 || boxState == 1) {
                    boxBeanJsonObject.put("boxStateDescription",
                            BoxStatus.BOX_STATUS_DESCRIPTION_NORMAL);
                } else {
                    boxBeanJsonObject.put("boxStateDescription",
                            BoxStatus.BOX_NEARBY_STATUS_DESCRIPTION_BREAKDOWN);
                }

                String boxAddress = boxes.getBoxAddress();
                boxBeanJsonObject.put("boxAddress", boxAddress);

                double distance = boxes.getDistance();
                // 小于1km按m显示，大于1km按km显示
                if (distance < 1000) {
                    boxBeanJsonObject.put("distance", (int) distance);
                    boxBeanJsonObject.put("distanceUnit", "m");
                } else {
                    boxBeanJsonObject.put("distance", String
                            .valueOf(new BigDecimal(distance / 1000d).setScale(
                                    1, BigDecimal.ROUND_DOWN).doubleValue()));
                    boxBeanJsonObject.put("distanceUnit", "km");
                }

                boxBeanJsonObject.put(
                        "boxEnvironmentPicture",
                        ResponseStatus.SERVER_URL + "/"
                                + boxes.getEnvironmentPicture());
                JSONObject boxJsonObject = new JSONObject();
                boxJsonObject.put("Box", boxBeanJsonObject);
                boxesJsonArray.add(boxJsonObject);
            }
            boxesJsonObject.put("Boxes", boxesJsonArray);
            log.info("BoxesService:->My Boxes=" + boxesJsonObject.toString());
            return boxesJsonObject.toString();
        } else {
            return ResponseStatus.LAST_PAGE;
        }
    }

    /**
     * 分页查询附近柜子在地图上显示逻辑
     * 
     * @param fromLat
     * @param fromLng
     * @param nearByRange
     * @param pageNumber
     * @return
     */
    // 查询附近柜子，以地图风格展示,（按照微信小程序Map组件api封装数据）
    public String doFindNearbyBoxesOnMap(String mchId, String fromLat,
            String fromLng, double nearByRange, int pageNumber) {
        CustomerService customerService = new CustomerService();
        Customer customer = customerService.findCustomerByMchId(mchId);
        int count = findNearByBoxesCountByCustomerId(customer.getCustomerId(),
                fromLat, fromLng, nearByRange);
        int totalPages = 0;
        // 计算总页数
        if (count % Boxes.PAGE_SIZE == 0) {
            // 对总页数赋值
            totalPages = count / Boxes.PAGE_SIZE;
        } else {
            // 对总页数赋值
            totalPages = count / Boxes.PAGE_SIZE + 1;
        }

        List<Boxes> nearByBoxesList = findNearByBoxesByCustomerId(
                customer.getCustomerId(), fromLat, fromLng, nearByRange,
                pageNumber);
        log.info("BoxesService->nearByBoxesList.size=" + nearByBoxesList.size());

        if (pageNumber == 1 && nearByBoxesList.size() <= 0) {
            return ResponseStatus.NO_BOXES;
        } else if (pageNumber <= totalPages && nearByBoxesList.size() > 0) {
            JSONObject boxesJsonObject = new JSONObject();
            JSONArray boxesJsonArray = new JSONArray();
            // 地图marks
            JSONArray marksJsonArray = new JSONArray();
            // 自动缩放地图包含所有位置点includePoints
            JSONArray includePointsJsonArray = new JSONArray();
            // 同时查询柜子状态
            BoxStatusService boxStatusService = new BoxStatusService();
            // 获取nearByBoxesList中距离最短的那个
            List<Double> distanceList = new ArrayList<Double>();
            for (int i = 0; i < nearByBoxesList.size(); i++) {
                distanceList.add(nearByBoxesList.get(i).getDistance());
            }
            int nearestBoxIndex = distanceList.indexOf(Collections
                    .min(distanceList));
            for (int i = 0; i < nearByBoxesList.size(); i++) {
                log.info("BoxesService->nearByBoxesList="
                        + nearByBoxesList.get(i) + ",nearestBoxIndex="
                        + nearestBoxIndex);
                Boxes boxes = nearByBoxesList.get(i);
                JSONObject boxBeanJsonObject = JSONObject.fromObject(boxes);
                BoxStatus boxStatusBean = boxStatusService
                        .findBoxStatusByBoxId(boxes.getBoxId());
                int boxState = 0;
                if (boxStatusBean != null) {
                    boxState = boxStatusBean.getState();
                }
                boxBeanJsonObject.put("boxState", boxState);
                if (boxState == 0 || boxState == 1) {
                    boxBeanJsonObject.put("boxStateDescription",
                            BoxStatus.BOX_STATUS_DESCRIPTION_NORMAL);
                } else {
                    boxBeanJsonObject.put("boxStateDescription",
                            BoxStatus.BOX_NEARBY_STATUS_DESCRIPTION_BREAKDOWN);
                }

                String boxAddress = boxes.getBoxAddress();
                boxBeanJsonObject.put("boxAddress", boxAddress);

                double distance = boxes.getDistance();
                // 小于1km按m显示，大于1km按km显示
                if (distance < 1000) {
                    boxBeanJsonObject.put("distance", (int) distance);
                    boxBeanJsonObject.put("distanceUnit", "m");
                } else {
                    boxBeanJsonObject.put("distance", String
                            .valueOf(new BigDecimal(distance / 1000d).setScale(
                                    1, BigDecimal.ROUND_DOWN).doubleValue()));
                    boxBeanJsonObject.put("distanceUnit", "km");
                }

                boxBeanJsonObject.put(
                        "boxEnvironmentPicture",
                        ResponseStatus.SERVER_URL + "/"
                                + boxes.getEnvironmentPicture());
                JSONObject boxJsonObject = new JSONObject();
                boxJsonObject.put("Box", boxBeanJsonObject);
                boxesJsonArray.add(boxJsonObject);

                // 封装Map marker API相关属性
                JSONObject markBeanJsonObject = new JSONObject();
                markBeanJsonObject.put("iconPath",
                        "../../images/ic_map_markers.png");
                markBeanJsonObject.put("id", i);
                markBeanJsonObject.put("latitude",
                        Float.valueOf(boxes.getLatitude()));
                markBeanJsonObject.put("longitude",
                        Float.valueOf(boxes.getLongitude()));
                markBeanJsonObject.put("width", 40);
                markBeanJsonObject.put("height", 40);
                JSONObject calloutJsonObject = new JSONObject();
                if (nearestBoxIndex == i) {
                    calloutJsonObject.put("content", boxes.getBoxName() + "\n"
                            + ResponseStatus.NEAREST_BOX_ON_MAP_TIP);
                    calloutJsonObject.put("color", "#ff7f00");
                } else {
                    calloutJsonObject.put("content", boxes.getBoxName());
                }
                calloutJsonObject.put("textAlign", "center");
                calloutJsonObject.put("display", "ALWAYS");
                markBeanJsonObject.put("callout", calloutJsonObject);

                marksJsonArray.add(markBeanJsonObject);

                // 封装自动缩放地图包含所有位置点includePoints API信息
                JSONObject includePointsBeanJsonObject = new JSONObject();
                includePointsBeanJsonObject.put("latitude",
                        Float.valueOf(boxes.getLatitude()));
                includePointsBeanJsonObject.put("longitude",
                        Float.valueOf(boxes.getLongitude()));
                includePointsJsonArray.add(includePointsBeanJsonObject);
            }
            boxesJsonObject.put("Boxes", boxesJsonArray);
            boxesJsonObject.put("markers", marksJsonArray);
            boxesJsonObject.put("includePoints", includePointsJsonArray);
            log.info("CustomBoxesService:->My Boxes="
                    + boxesJsonObject.toString());
            return boxesJsonObject.toString();
        } else {
            return ResponseStatus.LAST_PAGE;
        }
    }

}
