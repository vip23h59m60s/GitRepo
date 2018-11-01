package com.chinapalms.kwobox.service;

import com.chinapalms.kwobox.dao.impl.ReplenishmentCallbackDAOImpl;
import com.chinapalms.kwobox.javabean.ReplenishmentCallback;

public class ReplenishmentCallbackService extends ReplenishmentCallbackDAOImpl {

    @Override
    public ReplenishmentCallback findReplenishmentCallbackByCustomerId(
            int customerId) {
        return super.findReplenishmentCallbackByCustomerId(customerId);
    }

    @Override
    public boolean updateReplenishmentCallback(
            ReplenishmentCallback replenishmentCallback) {
        return super.updateReplenishmentCallback(replenishmentCallback);
    }

}
