package com.chinapalms.kwobox.service;

import com.chinapalms.kwobox.dao.impl.ReplenishmentCallbackCustomizationDAOImpl;
import com.chinapalms.kwobox.javabean.ReplenishmentCallbackCustomization;

public class ReplenishmentCallbackCustomizationService extends
        ReplenishmentCallbackCustomizationDAOImpl {

    @Override
    public boolean addReplenishmentCallbackCustomization(
            ReplenishmentCallbackCustomization replenishmentCallbackCustomization) {
        return super
                .addReplenishmentCallbackCustomization(replenishmentCallbackCustomization);
    }

    @Override
    public boolean updateReplenishmentCallbackCustomization(
            ReplenishmentCallbackCustomization replenishmentCallbackCustomization) {
        return super
                .updateReplenishmentCallbackCustomization(replenishmentCallbackCustomization);
    }

    @Override
    public ReplenishmentCallbackCustomization findReplenishmentCallbackCustomizationByCustomerIdAndBoxId(
            int customerId, String boxId) {
        return super
                .findReplenishmentCallbackCustomizationByCustomerIdAndBoxId(
                        customerId, boxId);
    }

}
