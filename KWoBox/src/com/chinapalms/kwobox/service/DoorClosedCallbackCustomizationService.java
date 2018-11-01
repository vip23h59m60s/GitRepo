package com.chinapalms.kwobox.service;

import com.chinapalms.kwobox.dao.impl.DoorClosedCallbackCustomizationDAOImpl;
import com.chinapalms.kwobox.javabean.DoorClosedCallbackCustomization;

public class DoorClosedCallbackCustomizationService extends
        DoorClosedCallbackCustomizationDAOImpl {

    @Override
    public boolean addDoorClosedCallbackCustomization(
            DoorClosedCallbackCustomization doorClosedCallbackCustomization) {
        return super
                .addDoorClosedCallbackCustomization(doorClosedCallbackCustomization);
    }

    @Override
    public boolean updateDoorClosedCallbackCustomization(
            DoorClosedCallbackCustomization doorClosedCallbackCustomization) {
        return super
                .updateDoorClosedCallbackCustomization(doorClosedCallbackCustomization);
    }

    @Override
    public DoorClosedCallbackCustomization findDoorClosedCallbackCustomizationByCustomerIdAndBoxId(
            int customerId, String boxId) {
        return super.findDoorClosedCallbackCustomizationByCustomerIdAndBoxId(
                customerId, boxId);
    }

}
