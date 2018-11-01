package com.chinapalms.kwobox.dao;

import com.chinapalms.kwobox.javabean.OrderCallback;

public interface OrderCallbackDAO {

    public OrderCallback findOrderCallbackByCustomerId(int customerId);

    public boolean updateOrderCallback(OrderCallback orderCallback);

}
