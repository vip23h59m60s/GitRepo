package com.chinapalms.kwobox.dao;

import java.util.List;

import com.chinapalms.kwobox.javabean.CallCenterNormal;

public interface CallCenterNormalDAO {

    public CallCenterNormal findCallCenterNormalByUserNameAndPassword(
            String userName, String password);

    public List<CallCenterNormal> findCallCenterNormalsByWorkState(int workState);

}
