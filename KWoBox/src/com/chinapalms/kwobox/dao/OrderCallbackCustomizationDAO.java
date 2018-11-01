package com.chinapalms.kwobox.dao;

import com.chinapalms.kwobox.javabean.OrderCallbackCustomization;

public interface OrderCallbackCustomizationDAO {

    public boolean addOrderCallbackCustomization(
            OrderCallbackCustomization orderCallbackCustomization);

    public boolean updateOrderCallbackCustomization(
            OrderCallbackCustomization orderCallbackCustomization);

    public OrderCallbackCustomization findOrderCallbackCustomizationByCustomerIdAndBoxId(
            int customerId, String boxId);

}
