package com.chinapalms.kwobox.dao;

import com.chinapalms.kwobox.javabean.DoorClosedCallbackCustomization;

public interface DoorClosedCallbackCustomizationDAO {

    public boolean addDoorClosedCallbackCustomization(
            DoorClosedCallbackCustomization doorClosedCallbackCustomization);

    public boolean updateDoorClosedCallbackCustomization(
            DoorClosedCallbackCustomization doorClosedCallbackCustomization);

    public DoorClosedCallbackCustomization findDoorClosedCallbackCustomizationByCustomerIdAndBoxId(
            int customerId, String boxId);

}
