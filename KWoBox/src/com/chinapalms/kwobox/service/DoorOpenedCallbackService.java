package com.chinapalms.kwobox.service;

import com.chinapalms.kwobox.dao.impl.DoorOpenedCallbackDAOImpl;
import com.chinapalms.kwobox.javabean.DoorOpenedCallback;

public class DoorOpenedCallbackService extends DoorOpenedCallbackDAOImpl {

    @Override
    public DoorOpenedCallback findDoorOpenedCallbackByCustomerId(int customerId) {
        return super.findDoorOpenedCallbackByCustomerId(customerId);
    }

    @Override
    public boolean updateDoorOpenedCallback(
            DoorOpenedCallback doorOpenedCallback) {
        return super.updateDoorOpenedCallback(doorOpenedCallback);
    }

}
