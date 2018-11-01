package com.chinapalms.kwobox.service;

import java.util.Date;
import java.util.Random;

import com.chinapalms.kwobox.dao.impl.IdentifyCodeDAOImpl;
import com.chinapalms.kwobox.javabean.IdentifyCode;
import com.chinapalms.kwobox.javabean.User;
import com.chinapalms.kwobox.utils.IdentifyCodeUtil;
import com.chinapalms.kwobox.utils.ResponseStatus;

public class IdentifyCodeService extends IdentifyCodeDAOImpl {

    @Override
    public boolean addIdentifyCode(IdentifyCode idendifyCode) {
        return super.addIdentifyCode(idendifyCode);
    }

    @Override
    public boolean deleteIdentifyCodeByPhoneNumber(String phoneNumber) {
        return super.deleteIdentifyCodeByPhoneNumber(phoneNumber);
    }

    @Override
    public boolean updateIdentifyCode(IdentifyCode idendifyCode) {
        return super.updateIdentifyCode(idendifyCode);
    }

    @Override
    public IdentifyCode findIdentifyCodeByPhoneNumber(String phoneNumber) {
        return super.findIdentifyCodeByPhoneNumber(phoneNumber);
    }

    @Override
    public IdentifyCode findIdentifyCodeByPhoneNumberAndIdentifyCode(
            String phoneNumber, String identifyCode) {
        return super.findIdentifyCodeByPhoneNumberAndIdentifyCode(phoneNumber,
                identifyCode);
    }

    /**
     * 获取验证码
     * 
     * @param identifyCode
     * @return
     */
    public String doGetIdentifyCode(IdentifyCode identifyCode) {
        String phoneNumber = identifyCode.getPhoneNumber();
        String identifyCodeString = makeIdentifyCode();
        IdentifyCode idendifyCodeAdd = new IdentifyCode();
        idendifyCodeAdd.setPhoneNumber(phoneNumber);
        idendifyCodeAdd.setIdentifyCode(identifyCodeString);
        if (findIdentifyCodeByPhoneNumber(phoneNumber) != null) {
            if (updateIdentifyCode(idendifyCodeAdd)) {
                // boolean sendFlag = IdentifyCodeUtil.sendIdentifyCode(
                // phoneNumber, identifyCodeString);
                boolean sendFlag = IdentifyCodeUtil
                        .sendTencentQQIdentifyCodeSms(phoneNumber,
                                identifyCodeString,
                                ResponseStatus.IDENTIFY_CODE_TIMEOUT_MINUTES);
                if (sendFlag) {
                    return ResponseStatus.SUCCESS;
                } else {
                    deleteIdentifyCodeByPhoneNumber(phoneNumber);
                    return ResponseStatus.FAIL;
                }
            } else {
                return ResponseStatus.FAIL;
            }
        } else {
            if (addIdentifyCode(idendifyCodeAdd)) {
                // boolean sendFlag = IdentifyCodeUtil.sendIdentifyCode(
                // phoneNumber, identifyCodeString);
                boolean sendFlag = IdentifyCodeUtil
                        .sendTencentQQIdentifyCodeSms(phoneNumber,
                                identifyCodeString,
                                ResponseStatus.IDENTIFY_CODE_TIMEOUT_MINUTES);
                if (sendFlag) {
                    return ResponseStatus.SUCCESS;
                } else {
                    deleteIdentifyCodeByPhoneNumber(phoneNumber);
                    return ResponseStatus.FAIL;
                }
            } else {
                return ResponseStatus.FAIL;
            }
        }
    }

    /**
     * 检查验证码有效性(1、正确性；2、是否超时:有效时间5分钟)，并且在验证成功后从数据库删除验证码
     * 
     * @param identifyCodeObject
     * @return
     */
    public String doCheckIdentifyCode(IdentifyCode identifyCodeObject,
            String customType, String contractId) {
        String phoneNumber = identifyCodeObject.getPhoneNumber();
        String identifyCode = identifyCodeObject.getIdentifyCode();
        IdentifyCode identifyCodeQuery = findIdentifyCodeByPhoneNumberAndIdentifyCode(
                phoneNumber, identifyCode);
        if (identifyCodeQuery != null) {
            Date dateTimeQuery = identifyCodeQuery.getTime();
            Date dateTimeCurrent = new Date();
            if (isTimeOut(dateTimeQuery, dateTimeCurrent)) {
                return ResponseStatus.ISTIMEOUT;
            } else {
                User user = new User();
                user.setPhoneNumber(phoneNumber);
                user.setFaceFunction(0);
                if (customType.equals(ResponseStatus.CUSTOM_CATEGORY_WX)) {
                    user.setWxId(contractId);
                }
                user.setRegisterTime(new Date());
                UserService userService = new UserService();
                if (deleteIdentifyCodeByPhoneNumber(phoneNumber)) {
                    User userQuery = userService
                            .queryUserByPhoneNumber(phoneNumber);
                    if (customType.equals(ResponseStatus.CUSTOM_CATEGORY_WX)) {
                        if ((userQuery != null && userQuery.getWxId().equals(
                                contractId))
                                || (userQuery != null
                                        && (!(userQuery.getWxId()
                                                .equals(contractId))) && userService
                                            .updateConstractIdByPhoneNumberAndCustomType(
                                                    phoneNumber, customType,
                                                    contractId))
                                || (userQuery == null && userService
                                        .addUser(user))) {
                            return ResponseStatus.SUCCESS;
                        } else {
                            return ResponseStatus.FAIL;
                        }
                    } else if (customType
                            .equals(ResponseStatus.CUSTOM_CATEGORY_ALIPAY)) {
                        if ((userQuery != null && userQuery.getAlipayId()
                                .equals(contractId))
                                || (userQuery != null
                                        && (!(userQuery.getAlipayId()
                                                .equals(contractId))) && userService
                                            .updateConstractIdByPhoneNumberAndCustomType(
                                                    phoneNumber, customType,
                                                    contractId))
                                || (userQuery == null && userService
                                        .addUser(user))) {
                            return ResponseStatus.SUCCESS;
                        } else {
                            return ResponseStatus.FAIL;
                        }
                    }
                    return ResponseStatus.FAIL;
                } else {
                    return ResponseStatus.FAIL;
                }
            }
        } else {
            return ResponseStatus.FAIL;
        }
    }

    private boolean isTimeOut(Date queryTime, Date currentTime) {
        boolean isTimeOut = false;
        long timeMinute = (currentTime.getTime() - queryTime.getTime()) / 1000 / 60;
        if (timeMinute >= ResponseStatus.IDENTIFY_CODE_TIMEOUT_MINUTES) {
            isTimeOut = true;
        }
        return isTimeOut;
    }

    private String makeIdentifyCode() {
        Random random = new Random();
        String result = "";
        for (int i = 0; i < 4; i++) {
            result += random.nextInt(10);
        }
        return result;
    }
}
