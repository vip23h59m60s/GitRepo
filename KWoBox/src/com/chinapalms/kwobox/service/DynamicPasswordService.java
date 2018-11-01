package com.chinapalms.kwobox.service;

import java.util.Date;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONObject;

import com.chinapalms.kwobox.dao.impl.DynamicPasswordDAOImpl;
import com.chinapalms.kwobox.javabean.BoxStatus;
import com.chinapalms.kwobox.javabean.Boxes;
import com.chinapalms.kwobox.javabean.CurrentUser;
import com.chinapalms.kwobox.javabean.Customer;
import com.chinapalms.kwobox.javabean.DoorOpenedCallback;
import com.chinapalms.kwobox.javabean.DynamicPassword;
import com.chinapalms.kwobox.javabean.User;
import com.chinapalms.kwobox.utils.ResponseStatus;
import com.custom.kwobox.service.CustomCurrentUserService;
import com.custom.kwobox.service.CustomDynamicPasswordService;
import com.custom.kwobox.service.CustomResponseService;

public class DynamicPasswordService extends DynamicPasswordDAOImpl {

    Log log = LogFactory.getLog(DynamicPasswordService.class);

    @Override
    public boolean addDynamicPassword(DynamicPassword dynamicPassword) {
        return super.addDynamicPassword(dynamicPassword);
    }

    @Override
    public boolean updateDynamicPassword(DynamicPassword dynamicPassword) {
        return super.updateDynamicPassword(dynamicPassword);
    }

    @Override
    public DynamicPassword findDynamicPasswordByPhoneNumber(String phoneNumber) {
        return super.findDynamicPasswordByPhoneNumber(phoneNumber);
    }

    @Override
    public DynamicPassword findDynamicPasswordByDynamicPassword(
            String dynamicPassword) {
        return super.findDynamicPasswordByDynamicPassword(dynamicPassword);
    }

    @Override
    public boolean deleteDynamicPasswordByPhoneNumber(String phoneNumber) {
        return super.deleteDynamicPasswordByPhoneNumber(phoneNumber);
    }

    public String doGetDynamicPassword(String phoneNumber) {
        String dynamicPasswordString = getRandomDynamicPasswordByLength(8);
        DynamicPassword dynamicPassword = new DynamicPassword();
        dynamicPassword.setPhoneNumber(phoneNumber);
        dynamicPassword.setDynamicPassword(dynamicPasswordString);
        Date currentDate = new Date();
        dynamicPassword.setPasswordTime(currentDate);
        DynamicPassword dynamicPasswordQuery = findDynamicPasswordByPhoneNumber(phoneNumber);
        if (dynamicPasswordQuery == null) {
            if (addDynamicPassword(dynamicPassword)) {
                return dynamicPasswordString;
            } else {
                return ResponseStatus.FAIL;
            }
        } else {
            // 如果密码已经过期，则更新，否则直接返回原密码
            if (isTimeOut(dynamicPasswordQuery.getPasswordTime(), currentDate)) {
                if (updateDynamicPassword(dynamicPassword)) {
                    return dynamicPasswordString;
                } else {
                    return ResponseStatus.FAIL;
                }
            } else {
                return dynamicPasswordQuery.getDynamicPassword();
            }
        }
    }

    // 获取指定位数0-9 8位独一无二的随机密码
    private String getRandomDynamicPasswordByLength(int length) {
        String base = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        String password = sb.toString();
        DynamicPassword dynamicPassword = findDynamicPasswordByDynamicPassword(password);
        if (dynamicPassword == null) {
            return sb.toString();
        } else {
            return getRandomDynamicPasswordByLength(8);
        }
    }

    public String doCheckDynamicPasswordState(String dynamicPassword) {
        DynamicPassword dynamicPasswordQuery = findDynamicPasswordByDynamicPassword(dynamicPassword);
        Date currentDate = new Date();
        if (dynamicPasswordQuery != null) {
            if (isTimeOut(dynamicPasswordQuery.getPasswordTime(), currentDate)) {
                return ResponseStatus.HAD_TIMOUT;
            } else {
                return String
                        .valueOf(ResponseStatus.DYNAMICPASSWORD_TIMEOUT_MINUTES
                                - (int) ((currentDate.getTime() - dynamicPasswordQuery
                                        .getPasswordTime().getTime()) / 1000 / 60));
            }
        } else {
            return ResponseStatus.HAD_TIMOUT;
        }
    }

    private boolean isTimeOut(Date queryTime, Date currentTime) {
        boolean isTimeOut = false;
        long timeMinute = (currentTime.getTime() - queryTime.getTime()) / 1000 / 60;
        if (timeMinute >= ResponseStatus.DYNAMICPASSWORD_TIMEOUT_MINUTES) {
            isTimeOut = true;
        }
        return isTimeOut;
    }

    public String doCheckDynamicPassword(String boxId, String dynamicPassword) {

        // 针对Yoho和小e模式的柜子临时做处理，不让开门
        // BoxesService boxesServiceTemp = new BoxesService();
        // Boxes boxesTemp = boxesServiceTemp.findBoxesByBoxId(boxId);
        // if (boxesTemp != null) {
        // // 根据商户ID查询商户合作模式
        // CustomerService customerService = new CustomerService();
        // Customer customer = customerService
        // .findCustomerByCustomerId(boxesTemp.getCustomerId());
        // // 判断是否是小e服务器对接模式
        // if (customer != null) {
        // DoorOpenedCallbackService doorOpenedCallbackService = new
        // DoorOpenedCallbackService();
        // DoorOpenedCallback doorOpenedCallback = doorOpenedCallbackService
        // .findDoorOpenedCallbackByCustomerId(boxesTemp
        // .getCustomerId());
        // int cooperationMode = customer.getCooperationMode();
        // if (doorOpenedCallback != null) {
        // // 判断是否是小e服务器堆积或Yoho代理模式
        // if (cooperationMode == ResponseStatus.COOPERATIONMODE_THIRD_INTERFACE
        // || cooperationMode == ResponseStatus.COOPERATIONMODE_AGENT) {
        // return ResponseStatus.FAIL;
        // }
        // }
        // }
        // }
        // 针对Yoho和小e模式的柜子临时做处理，不让开门

        // ============对接商户服务器逻辑==========================================================================
        // 判断是否是第三方对接模式(Yoho模式),如果是Yoho模式，将异常信息回调至商户服务器订单回调页面，以便于商户服务器更新个人征信分和通知商户微信客户端
        // 通过boxId查找商户ID，再根据商户ID查找回调Url，返回商户服务器，与商户服务器对接,如果无回调Url，则默认走智购猫平台逻辑
        CustomDynamicPasswordService customDynamicPasswordService = new CustomDynamicPasswordService();
        String dynamicPasswordCallbackUrlObject = customDynamicPasswordService
                .getCustomerDynamicPasswordCallbackUrl(boxId);
        if (dynamicPasswordCallbackUrlObject.equals(ResponseStatus.FAIL)) {
            // 走智购猫默认流程
            DynamicPassword dynamicPasswordQuery = findDynamicPasswordByDynamicPassword(dynamicPassword);
            Date currentDate = new Date();
            if (dynamicPasswordQuery != null) {
                if (isTimeOut(dynamicPasswordQuery.getPasswordTime(),
                        currentDate)) {
                    return ResponseStatus.HAD_TIMOUT;
                } else {
                    String contractId = findPapayContractIdByPhoneNumber(dynamicPasswordQuery
                            .getPhoneNumber());
                    // (暂时忽略是否开通免密)
                    if (!contractId.equals(ResponseStatus.NO_OPEN_PAPAY)) {
                        log.info("doCheckDynamicPassword ->>start doRegisterCurrentUser...");
                        CurrentUserService currentUserService = new CurrentUserService();
                        CurrentUser currentUser = new CurrentUser();
                        currentUser.setPhoneNumber(dynamicPasswordQuery
                                .getPhoneNumber());
                        currentUser.setCustomerWorkerId(0);
                        currentUser.setBoxId(boxId);
                        currentUser
                                .setCustomType(ResponseStatus.CUSTOM_CATEGORY_WX);
                        currentUser
                                .setUserType(ResponseStatus.PERMISSION_NORMAL_USER);
                        currentUser.setAgreementNO(contractId);
                        // 检查是否有未支付订单
                        OrderService orderService = new OrderService();
                        if (!orderService.doCheckNoPayOrder(
                                dynamicPasswordQuery.getPhoneNumber()).equals(
                                ResponseStatus.FAIL)) {
                            // 验证柜子状态
                            BoxesService boxesService = new BoxesService();
                            Boxes boxes = new Boxes();
                            boxes.setBoxId(boxId);
                            String identifyResult = boxesService
                                    .doOpenDoorIdentify(boxes,
                                            dynamicPasswordQuery
                                                    .getPhoneNumber());

                            if (identifyResult
                                    .equals(ResponseStatus.PERSONAL_CREDIT_TOO_LOW)) {
                                // 个人征信过低，暂时用BOX_BROKEN_DOWN 代替 return
                                return ResponseStatus.PERSONAL_CREDIT_TOO_LOW;
                            } else if (identifyResult
                                    .equals(ResponseStatus.BOX_BUSY)) {
                                return ResponseStatus.BOX_BUSY;
                            } else if (identifyResult
                                    .equals(ResponseStatus.BOX_BROKEN_DOWN)) {
                                return ResponseStatus.BOX_BROKEN_DOWN;
                            } else {
                                // 注册当前柜子使用用户
                                if (currentUserService.doRegisterCurrentUser(
                                        currentUser).equals(
                                        ResponseStatus.SUCCESS)) {
                                    log.info("doCheckDynamicPassword ->>doRegisterCurrentUser success...");
                                    // 验证通过，通知主控开门
                                    log.info("doCheckDynamicPassword->notify Android to open door!!!");
                                    notifyAndroidOpenDoor(
                                            ResponseStatus.OPEN_DOOR_REQUEST_SERIALNUMBER_NORMAL,
                                            ResponseStatus.CUSTOM_CATEGORY_ANDROID,
                                            boxId,
                                            ResponseStatus.PERMISSION_NORMAL_USER);
                                    // 通知开门以后删除数据库中动态密码（只能用一次）
                                    deleteDynamicPasswordByPhoneNumber(dynamicPasswordQuery
                                            .getPhoneNumber());
                                    return ResponseStatus.SUCCESS;
                                } else {
                                    return ResponseStatus.FAIL;
                                }
                            }
                        } else {
                            log.info("doCheckDynamicPassword->NO_PAYED_ORDER");
                            return ResponseStatus.NO_PAYED_ORDER;
                        }
                    } else {
                        // 未开通免密委托代扣
                        log.info("doCheckDynamicPassword->NO_OPEN_PAPAY");
                        return ResponseStatus.NO_OPEN_PAPAY;
                    }
                }
            } else {
                return ResponseStatus.DYNAMICPASSWORD_ERROR;
            }
        } else {
            // 走回调商户服务器流程
            int urlType = JSONObject.fromObject(
                    dynamicPasswordCallbackUrlObject).getInt("urlType");
            // 如果是Yoho代理模式,则把订单信息回调给代理开发服务器
            if (urlType == ResponseStatus.COOPERATIONMODE_AGENT) {
                String checkResult = customDynamicPasswordService
                        .notifyCustomerServerToCheckDynamicPassword(
                                dynamicPasswordCallbackUrlObject, boxId,
                                dynamicPassword);
                JSONObject checkResultJsonObject = JSONObject
                        .fromObject(checkResult);
                String checkResultResponse = checkResultJsonObject
                        .getString("response");
                log.info("checkResultResponse=" + checkResultResponse);
                if (checkResultResponse.equals(ResponseStatus.SUCCESS)) {
                    String phoneNumberFromCustom = checkResultJsonObject
                            .getString("phoneNumber");
                    String boxIdFromCustom = checkResultJsonObject
                            .getString("boxId");
                    // 检查柜子状态
                    BoxStatusService boxStatusService = new BoxStatusService();
                    BoxStatus boxStatus = boxStatusService
                            .findBoxStatusByBoxId(boxIdFromCustom);
                    if (boxStatus != null) {
                        int state = boxStatus.getState();
                        if (state == ResponseStatus.BOX_STATUS_BUSY) {
                            return ResponseStatus.BOX_BUSY;
                        } else if (state == ResponseStatus.BOX_STATUS_BROKEN_DOWN) {
                            return ResponseStatus.BOX_BROKEN_DOWN;
                        } else {
                            // 注册当前用户
                            log.info("doCheckDynamicPasswordFromCustomServer ->>start doRegisterCurrentUser...");
                            CustomCurrentUserService customCurrentUserService = new CustomCurrentUserService();
                            CurrentUser currentUser = new CurrentUser();
                            currentUser.setPhoneNumber(phoneNumberFromCustom);
                            currentUser.setCustomerWorkerId(0);
                            currentUser.setBoxId(boxIdFromCustom);
                            currentUser
                                    .setCustomType(ResponseStatus.CUSTOM_CATEGORY_WX);
                            currentUser
                                    .setUserType(ResponseStatus.PERMISSION_NORMAL_USER);
                            currentUser.setVipLevel(1);
                            currentUser.setAgreementNO("0");
                            if (customCurrentUserService.registerCurrentUser(
                                    currentUser).equals(ResponseStatus.SUCCESS)) {
                                // 返回工控机结果，工控机开门
                                log.info("doCheckDynamicPassword->notify Android to open door!!!");
                                notifyAndroidOpenDoor(
                                        ResponseStatus.OPEN_DOOR_REQUEST_SERIALNUMBER_NORMAL,
                                        ResponseStatus.CUSTOM_CATEGORY_ANDROID,
                                        boxId,
                                        ResponseStatus.PERMISSION_NORMAL_USER);
                                return ResponseStatus.SUCCESS;
                            } else {
                                return ResponseStatus.FAIL;
                            }
                        }
                    } else {
                        return ResponseStatus.SUCCESS;
                    }
                } else {
                    // 从商户服务器返回的错误验证状态结果返回给工控机进行处理
                    return checkResultResponse;
                }
            } else {
                return ResponseStatus.DYNAMICPASSWORD_ERROR;
            }
        }
    }

    private String findPapayContractIdByPhoneNumber(String phoneNumber) {
        UserService userService = new UserService();
        User user = userService.queryUserByPhoneNumber(phoneNumber);
        if (user != null
                && !user.getWxId().equals(ResponseStatus.NO_CONTRACT_ID)) {
            return user.getWxId();
        } else {
            return ResponseStatus.NO_OPEN_PAPAY;
        }
    }

    /**
     * 通知Android设备去开门
     * 
     * @param customCategory
     * @param boxId
     */
    private void notifyAndroidOpenDoor(String requestSerialNumber,
            String customCategory, String boxId, String userPermission) {
        BoxesService boxService = new BoxesService();
        try {
            boxService.notifyAndroidOpenDoor(requestSerialNumber,
                    customCategory, boxId, userPermission);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

}
