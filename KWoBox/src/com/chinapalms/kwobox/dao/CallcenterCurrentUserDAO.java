package com.chinapalms.kwobox.dao;

import com.chinapalms.kwobox.javabean.CallcenterCurrentUser;

public interface CallcenterCurrentUserDAO {

    public boolean addCallCenterCurrentUser(
            CallcenterCurrentUser callcenterCurrentUser);

    public CallcenterCurrentUser findCallCenterCurrentUserByBoxId(String boxId);

    public boolean updateCallCenterCurrentUser(
            CallcenterCurrentUser callcenterCurrentUser);

}
