package com.chinapalms.kwobox.dao;

import com.chinapalms.kwobox.javabean.DoorClosedCallback;

public interface DoorClosedCallbackDAO {

    public DoorClosedCallback findDoorClosedCallbackByCustomerId(int customerId);

    public boolean updateDoorClosedCallback(DoorClosedCallback dorCallback);

}
