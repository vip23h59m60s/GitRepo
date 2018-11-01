package com.chinapalms.kwobox.service;

import com.chinapalms.kwobox.dao.impl.DoorOpenedCallbackCustomizationDAOImpl;
import com.chinapalms.kwobox.javabean.DoorOpenedCallbackCustomization;

public class DoorOpenedCallbackCustomizationService extends
        DoorOpenedCallbackCustomizationDAOImpl {

    @Override
    public boolean addDoorOpenedCallbackCustomization(
            DoorOpenedCallbackCustomization doorOpenedCallbackCustomization) {
        return super
                .addDoorOpenedCallbackCustomization(doorOpenedCallbackCustomization);
    }

    @Override
    public boolean updateDoorOpenedCallbackCustomization(
            DoorOpenedCallbackCustomization doorOpenedCallbackCustomization) {
        return super
                .updateDoorOpenedCallbackCustomization(doorOpenedCallbackCustomization);
    }

    @Override
    public DoorOpenedCallbackCustomization findDoorOpenedCallbackCustomizationByCustomerIdAndBoxId(
            int customerId, String boxId) {
        return super.findDoorOpenedCallbackCustomizationByCustomerIdAndBoxId(
                customerId, boxId);
    }

}
