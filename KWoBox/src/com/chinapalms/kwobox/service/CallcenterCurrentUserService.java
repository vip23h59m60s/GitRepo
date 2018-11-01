package com.chinapalms.kwobox.service;

import com.chinapalms.kwobox.dao.impl.CallcenterCurrentUserDAOImpl;
import com.chinapalms.kwobox.javabean.CallcenterCurrentUser;

public class CallcenterCurrentUserService extends CallcenterCurrentUserDAOImpl {

    @Override
    public boolean addCallCenterCurrentUser(
            CallcenterCurrentUser callcenterCurrentUser) {
        return super.addCallCenterCurrentUser(callcenterCurrentUser);
    }

    @Override
    public CallcenterCurrentUser findCallCenterCurrentUserByBoxId(String boxId) {
        return super.findCallCenterCurrentUserByBoxId(boxId);
    }

    @Override
    public boolean updateCallCenterCurrentUser(
            CallcenterCurrentUser callcenterCurrentUser) {
        return super.updateCallCenterCurrentUser(callcenterCurrentUser);
    }

}
