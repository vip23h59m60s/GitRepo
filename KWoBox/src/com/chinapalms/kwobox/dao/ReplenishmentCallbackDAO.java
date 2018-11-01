package com.chinapalms.kwobox.dao;

import com.chinapalms.kwobox.javabean.ReplenishmentCallback;

public interface ReplenishmentCallbackDAO {

    public ReplenishmentCallback findReplenishmentCallbackByCustomerId(
            int customerId);

    public boolean updateReplenishmentCallback(
            ReplenishmentCallback replenishmentCallback);

}
