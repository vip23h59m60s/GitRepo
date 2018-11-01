package com.chinapalms.kwobox.service;

import com.chinapalms.kwobox.dao.impl.OrderCallbackDAOImpl;
import com.chinapalms.kwobox.javabean.OrderCallback;

public class OrderCallbackService extends OrderCallbackDAOImpl {

    @Override
    public OrderCallback findOrderCallbackByCustomerId(int customerId) {
        return super.findOrderCallbackByCustomerId(customerId);
    }

    @Override
    public boolean updateOrderCallback(OrderCallback orderCallback) {
        return super.updateOrderCallback(orderCallback);
    }

}
