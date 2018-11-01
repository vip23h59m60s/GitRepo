package com.chinapalms.kwobox.dao;

import com.chinapalms.kwobox.javabean.DoorOpenedCallback;

public interface DoorOpenedCallbackDAO {

    public DoorOpenedCallback findDoorOpenedCallbackByCustomerId(int customerId);

    public boolean updateDoorOpenedCallback(
            DoorOpenedCallback doorOpenedCallback);

}
