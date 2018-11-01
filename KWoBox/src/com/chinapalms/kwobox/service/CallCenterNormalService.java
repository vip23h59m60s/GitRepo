package com.chinapalms.kwobox.service;

import java.util.List;

import com.chinapalms.kwobox.dao.impl.CallCenterNormalDAOImpl;
import com.chinapalms.kwobox.javabean.CallCenterNormal;
import com.chinapalms.kwobox.utils.MD5;
import com.chinapalms.kwobox.utils.ResponseStatus;

public class CallCenterNormalService extends CallCenterNormalDAOImpl {

    @Override
    public CallCenterNormal findCallCenterNormalByUserNameAndPassword(
            String userName, String password) {
        return super.findCallCenterNormalByUserNameAndPassword(userName,
                password);
    }

    @Override
    public List<CallCenterNormal> findCallCenterNormalsByWorkState(int workState) {
        return super.findCallCenterNormalsByWorkState(workState);
    }

    public String doMonitorUserLogin(String userName, String password) {
        String md5Password = MD5.MD5Encode(password);
        CallCenterNormal callCenterNormal = findCallCenterNormalByUserNameAndPassword(
                userName, md5Password);
        if (callCenterNormal != null) {
            return ResponseStatus.SUCCESS;
        } else {
            return ResponseStatus.FAIL;
        }
    }

}
