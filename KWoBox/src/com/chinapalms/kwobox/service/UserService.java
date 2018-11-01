package com.chinapalms.kwobox.service;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONObject;

import com.chinapalms.kwobox.dao.impl.UserDAOImpl;
import com.chinapalms.kwobox.javabean.PersonalCreditRecord;
import com.chinapalms.kwobox.javabean.PointsRecord;
import com.chinapalms.kwobox.javabean.SignIn;
import com.chinapalms.kwobox.javabean.User;
import com.chinapalms.kwobox.utils.ResponseStatus;
import com.chinapalms.kwobox.wxjssdk.AES;

//import com.chinapalms.kwobox.wxjssdk.AES;

public class UserService extends UserDAOImpl {

    Log log = LogFactory.getLog(UserService.class);

    @Override
    public boolean updateFaceFuntion(User user) {
        return super.updateFaceFuntion(user);
    }

    @Override
    public boolean addUser(User user) {
        return super.addUser(user);
    }

    @Override
    public boolean deleteUser(User user) {
        return super.deleteUser(user);
    }

    @Override
    public User queryUserByPhoneNumber(String phoneNumber) {
        return super.queryUserByPhoneNumber(phoneNumber);
    }

    @Override
    public boolean updateConstractIdByPhoneNumberAndCustomType(
            String phoneNumber, String customType, String constractId) {
        return super.updateConstractIdByPhoneNumberAndCustomType(phoneNumber,
                customType, constractId);
    }

    @Override
    public boolean clearContractId(String contractId, String customType) {
        return super.clearContractId(contractId, customType);
    }

    @Override
    public boolean updatePersonalCreditByPhoneNumber(String phoneNumber,
            int creditChangeValue) {
        return super.updatePersonalCreditByPhoneNumber(phoneNumber,
                creditChangeValue);
    }

    @Override
    public boolean updateUserPointsByPhoneNumber(String phoneNumber,
            int pointsChangeValue) {
        return super.updateUserPointsByPhoneNumber(phoneNumber,
                pointsChangeValue);
    }

    @Override
    public boolean updateUserUnReadOrderNumber(String phoneNumber,
            int changeValue) {
        return super.updateUserUnReadOrderNumber(phoneNumber, changeValue);
    }
    
    @Override
    public boolean updateOpendIdByPhoneNumber(String phoneNumber, String openId) {
        return super.updateOpendIdByPhoneNumber(phoneNumber, openId);
    }

    public String doUpdateContractId(String phoneNumber, String customType,
            String contractId) {
        if (updateConstractIdByPhoneNumberAndCustomType(phoneNumber,
                customType, contractId)) {
            return ResponseStatus.SUCCESS;
        } else {
            return ResponseStatus.FAIL;
        }
    }

    public String doCheckFaceDetectFunction(String phoneNumber) {
        User user = queryUserByPhoneNumber(phoneNumber);
        if (user != null && user.getFaceFunction() == 1) {
            return ResponseStatus.SUCCESS;
        }
        return ResponseStatus.FAIL;
    }

    public boolean doUpdatePersonalCredit(String phoneNumber, String boxId,
            int changeReason, int changeValue) {
        if (updatePersonalCreditByPhoneNumber(phoneNumber, changeValue)) {
            PersonalCreditRecordService personalCreditRecordService = new PersonalCreditRecordService();
            PersonalCreditRecord personalCreditRecord = new PersonalCreditRecord();
            personalCreditRecord.setPhoneNumber(phoneNumber);
            personalCreditRecord.setBoxId(boxId);
            personalCreditRecord.setChangeReason(changeReason);
            personalCreditRecord.setChangeValue(changeValue);
            personalCreditRecord.setChangeTime(new Date());
            if (personalCreditRecordService
                    .addPersonalCreditRecord(personalCreditRecord)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean doUpdateUserPoints(String phoneNumber, int changeReason,
            int changeValue) {
        if (updateUserPointsByPhoneNumber(phoneNumber, changeValue)) {
            PointsRecordService pointsRecordService = new PointsRecordService();
            PointsRecord pointsRecord = new PointsRecord();
            pointsRecord.setPhoneNumber(phoneNumber);
            pointsRecord.setChangeReason(changeReason);
            pointsRecord.setPointsChange(changeValue);
            pointsRecord.setChangeTime(new Date());
            if (pointsRecordService.addPointsRecordCreditRecord(pointsRecord)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public String doGetUserInfo(String phoneNumber) {
        User user = queryUserByPhoneNumber(phoneNumber);
        if (user != null) {
            JSONObject userBeanJsonObject = JSONObject.fromObject(user);
            PersonalCreditRecordService personalCreditRecordService = new PersonalCreditRecordService();
            List<PersonalCreditRecord> personalCreditRecordsList = personalCreditRecordService
                    .findPersonalCreditRecordsByPhoneNumber(phoneNumber);
            if (personalCreditRecordsList.size() > 0) {
                userBeanJsonObject.put("creditLastUpdateTime",
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                .format(personalCreditRecordsList.get(0)
                                        .getChangeTime()));
            } else {
                userBeanJsonObject.put("creditLastUpdateTime",
                        ResponseStatus.PERSONAL_CREDIT_NO_CREDIT_UPDATE_RECORD);
            }
            // 查询个人订单总数
            OrderService orderService = new OrderService();
            int orderCount = orderService
                    .findOrderCountByPhoneNumber(phoneNumber);
            userBeanJsonObject.put("orderCountNumber", orderCount);
            // 签到状态
            SignInService signInService = new SignInService();
            SignIn signIn = signInService.findSignInByPhoneNumber(phoneNumber);
            if (signIn != null) {
                if (signInService.isTodaySignedIn(signIn.getSignInDateTime())) {
                    userBeanJsonObject.put("signInState", 1);
                } else {
                    userBeanJsonObject.put("signInState", 0);
                }
            } else {
                userBeanJsonObject.put("signInState", 0);
            }
            JSONObject userJsonObject = new JSONObject();
            userJsonObject.put("User", userBeanJsonObject);
            return userJsonObject.toString();
        } else {
            return ResponseStatus.FAIL;
        }
    }

    public boolean doupdateUserUnReadOrderNumber(String phoneNumber,
            int changeValue) {
        return updateUserUnReadOrderNumber(phoneNumber, changeValue);
    }

    public String doGetPhoneNumberFromWX(String encryptedData, String iv,
            String sessionKey) {
        try {
            byte[] resultByte = AES.decrypt(Base64.decodeBase64(encryptedData),
                    Base64.decodeBase64(sessionKey), Base64.decodeBase64(iv));
            if (null != resultByte && resultByte.length > 0) {
                String userInfo = new String(resultByte, "UTF-8");
                log.info("doGetPhoneNumberFromWX:userInfo=" + userInfo);
                return userInfo;
            } else {
                return ResponseStatus.FAIL;
            }
        } catch (Exception e) {
            log.error("doGetPhoneNumberFromWX exception->=" + e.getMessage());
            e.printStackTrace();
        }
        return ResponseStatus.FAIL;
    }

    public String doQuickLogin(String phoneNumber, String customType,
            String contractId) {
        User user = new User();
        user.setPhoneNumber(phoneNumber);
        user.setFaceFunction(0);
        if (customType.equals(ResponseStatus.CUSTOM_CATEGORY_WX)) {
            user.setWxId(contractId);
        }
        user.setRegisterTime(new Date());
        UserService userService = new UserService();
        User userQuery = userService.queryUserByPhoneNumber(phoneNumber);
        if (customType.equals(ResponseStatus.CUSTOM_CATEGORY_WX)) {
            if ((userQuery != null && userQuery.getWxId().equals(contractId))
                    || (userQuery != null
                            && (!(userQuery.getWxId().equals(contractId))) && userService
                                .updateConstractIdByPhoneNumberAndCustomType(
                                        phoneNumber, customType, contractId))
                    || (userQuery == null && userService.addUser(user))) {
                return ResponseStatus.SUCCESS;
            } else {
                return ResponseStatus.FAIL;
            }
        } else if (customType.equals(ResponseStatus.CUSTOM_CATEGORY_ALIPAY)) {
            if ((userQuery != null && userQuery.getAlipayId()
                    .equals(contractId))
                    || (userQuery != null
                            && (!(userQuery.getAlipayId().equals(contractId))) && userService
                                .updateConstractIdByPhoneNumberAndCustomType(
                                        phoneNumber, customType, contractId))
                    || (userQuery == null && userService.addUser(user))) {
                return ResponseStatus.SUCCESS;
            } else {
                return ResponseStatus.FAIL;
            }
        }
        return ResponseStatus.FAIL;
    }

}
