package com.chinapalms.kwobox.dao;

import com.chinapalms.kwobox.javabean.ReplenishmentCallbackCustomization;

public interface ReplenishmentCallbackCustomizationDAO {

    public boolean addReplenishmentCallbackCustomization(
            ReplenishmentCallbackCustomization replenishmentCallbackCustomization);

    public boolean updateReplenishmentCallbackCustomization(
            ReplenishmentCallbackCustomization replenishmentCallbackCustomization);

    public ReplenishmentCallbackCustomization findReplenishmentCallbackCustomizationByCustomerIdAndBoxId(
            int customerId, String boxId);

}
