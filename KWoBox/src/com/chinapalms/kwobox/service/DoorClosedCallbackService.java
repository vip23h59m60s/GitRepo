package com.chinapalms.kwobox.service;

import com.chinapalms.kwobox.dao.impl.DoorClosedCallbackDAOImpl;
import com.chinapalms.kwobox.javabean.DoorClosedCallback;

public class DoorClosedCallbackService extends DoorClosedCallbackDAOImpl {

    @Override
    public DoorClosedCallback findDoorClosedCallbackByCustomerId(int customerId) {
        return super.findDoorClosedCallbackByCustomerId(customerId);
    }

    @Override
    public boolean updateDoorClosedCallback(DoorClosedCallback dorCallback) {
        return super.updateDoorClosedCallback(dorCallback);
    }

}
