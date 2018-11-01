package com.chinapalms.kwobox.service;

import com.chinapalms.kwobox.dao.impl.OrderCallbackCustomizationDAOImpl;
import com.chinapalms.kwobox.javabean.OrderCallbackCustomization;

public class OrderCallbackCustomizationService extends
        OrderCallbackCustomizationDAOImpl {

    @Override
    public boolean addOrderCallbackCustomization(
            OrderCallbackCustomization orderCallbackCustomization) {
        return super.addOrderCallbackCustomization(orderCallbackCustomization);
    }

    @Override
    public boolean updateOrderCallbackCustomization(
            OrderCallbackCustomization orderCallbackCustomization) {
        return super
                .updateOrderCallbackCustomization(orderCallbackCustomization);
    }

    @Override
    public OrderCallbackCustomization findOrderCallbackCustomizationByCustomerIdAndBoxId(
            int customerId, String boxId) {
        return super.findOrderCallbackCustomizationByCustomerIdAndBoxId(
                customerId, boxId);
    }

}
