package com.chinapalms.kwobox.dao;

import com.chinapalms.kwobox.javabean.DoorOpenedCallbackCustomization;

public interface DoorOpenedCallbackCustomizationDAO {

    public boolean addDoorOpenedCallbackCustomization(
            DoorOpenedCallbackCustomization doorOpenedCallbackCustomization);

    public boolean updateDoorOpenedCallbackCustomization(
            DoorOpenedCallbackCustomization doorOpenedCallbackCustomization);

    public DoorOpenedCallbackCustomization findDoorOpenedCallbackCustomizationByCustomerIdAndBoxId(
            int customerId, String boxId);

}
