package com.chinapalms.kwobox.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.impl.SignInDAOImpl;
import com.chinapalms.kwobox.javabean.SignIn;
import com.chinapalms.kwobox.javabean.SignInRecord;
import com.chinapalms.kwobox.tencentlocation.TencentLocationService;
import com.chinapalms.kwobox.utils.ResponseStatus;

public class SignInService extends SignInDAOImpl {

    Log log = LogFactory.getLog(OrderService.class);

    @Override
    public boolean addSignIn(SignIn signIn) {
        return super.addSignIn(signIn);
    }

    @Override
    public SignIn findSignInByPhoneNumber(String phoneNumber) {
        return super.findSignInByPhoneNumber(phoneNumber);
    }

    @Override
    public boolean resetSignInContiuneTimes(String phoneNumber) {
        return super.resetSignInContiuneTimes(phoneNumber);
    }

    @Override
    public boolean updateSignIn(SignIn signIn) {
        return super.updateSignIn(signIn);
    }

    public String doGetSignInInfo(String phoneNumber) {
        SignIn signIn = findSignInByPhoneNumber(phoneNumber);
        JSONObject signInInfoJsonObject = new JSONObject();
        JSONObject signInBeanJsonObject = new JSONObject();
        SignInRecordService signInRecordService = new SignInRecordService();
        int totalSignInPoints = signInRecordService
                .findTotalSignInPoints(phoneNumber);
        if (signIn != null) {
            signInBeanJsonObject.put("isTodaySignedIn",
                    isTodaySignedIn(signIn.getSignInDateTime()) ? 1 : 0);
            signInBeanJsonObject.put("daysFromLastSignedInDate",
                    daysFromLastSignedInDate(signIn.getSignInDateTime()));
            signInBeanJsonObject
                    .put("contiuneTimes", signIn.getContiuneTimes());
            signInBeanJsonObject.put("totalSignInPoints", totalSignInPoints);
            signInInfoJsonObject.put("SignInInfo", signInBeanJsonObject);
        } else {
            signInBeanJsonObject.put("isTodaySignedIn", 0);
            // 因为从来没有签到记录，该值默认一个至少大于1的值
            signInBeanJsonObject.put("daysFromLastSignedInDate", 2);
            signInBeanJsonObject.put("contiuneTimes", 0);
            signInBeanJsonObject.put("totalSignInPoints", totalSignInPoints);
            signInInfoJsonObject.put("SignInInfo", signInBeanJsonObject);
        }
        return signInInfoJsonObject.toString();
    }

    // 今天是否已经签到
    public boolean isTodaySignedIn(Date lastSignedInDate) {
        try {
            Date currentDate = new Date();
            SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
            return fmt.format(lastSignedInDate).equals(fmt.format(currentDate));
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        return false;
    }

    // 今天距离最近签到日期的天数
    private long daysFromLastSignedInDate(Date lastSignedInDate) {
        try {
            String formatLastSignedInDateIgnoreHourMinSecString = new SimpleDateFormat(
                    "yyyy-MM-dd").format(lastSignedInDate);
            Date formatLastSignedInDateIgnoreHourMinSec = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss")
                    .parse(formatLastSignedInDateIgnoreHourMinSecString
                            + " 00:00:00");
            long ld1 = formatLastSignedInDateIgnoreHourMinSec.getTime();
            long ld2 = new Date().getTime();
            long days = (long) ((ld2 - ld1) / 86400000);
            return days;
        } catch (ParseException e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        return 0L;
    }

    // 重置签到信息
    public String doResetSignIn(String phoneNumber) {
        if (resetSignInContiuneTimes(phoneNumber)) {
            return ResponseStatus.SUCCESS;
        } else {
            return ResponseStatus.FAIL;
        }
    }

    public String doSignIn(String phoneNumber, String willGetScroes,
            String signInLatitude, String signInLongitude) {
        TencentLocationService tencentLocationService = new TencentLocationService();
        // 将经纬度转化为address信息
        String address = tencentLocationService.latLngToAddress(signInLatitude,
                signInLongitude);
        if (address.equals(ResponseStatus.FAIL)) {
            address = "unknown";
        }
        UserService userService = new UserService();
        SignInService signInService = new SignInService();
        SignIn signInQuery = signInService.findSignInByPhoneNumber(phoneNumber);
        SignIn signIn = new SignIn();
        signIn.setPhoneNumber(phoneNumber);
        signIn.setLatitude(signInLatitude);
        signIn.setLongitude(signInLongitude);
        signIn.setAddress(address);
        signIn.setContiuneTimes(1);
        Date date = new Date();
        signIn.setSignInDateTime(date);

        // 签到记录相关信息
        SignInRecordService signInRecordService = new SignInRecordService();
        SignInRecord signInRecord = new SignInRecord();
        signInRecord.setPhoneNumber(phoneNumber);
        signInRecord.setLatitude(signInLatitude);
        signInRecord.setLongitude(signInLongitude);
        signInRecord.setAddress(address);
        signInRecord.setScores(Integer.valueOf(willGetScroes));
        signInRecord.setSignInDateTime(date);
        // 添加每日签到信息时，同时添加签到记录、更新用户积分和用户积分记录
        if (signInQuery == null) {
            if (signInService.addSignIn(signIn)
                    && signInRecordService.addSignInRecord(signInRecord)
                    && userService.doUpdateUserPoints(phoneNumber,
                            ResponseStatus.POINTS_ADD_REASON_FROM_SIGN_IN,
                            Integer.valueOf(willGetScroes))) {
                return doGetSignInInfo(phoneNumber);
            } else {
                return ResponseStatus.FAIL;
            }
        } else {
            if (isTodaySignedIn(signInQuery.getSignInDateTime())) {
                return ResponseStatus.FAIL;
            }
            if (signInService.updateSignIn(signIn)
                    && signInRecordService.addSignInRecord(signInRecord)
                    && userService.doUpdateUserPoints(phoneNumber,
                            ResponseStatus.POINTS_ADD_REASON_FROM_SIGN_IN,
                            Integer.valueOf(willGetScroes))) {
                return doGetSignInInfo(phoneNumber);
            } else {
                return ResponseStatus.FAIL;
            }
        }
    }

}
