package com.chinapalms.kwobox.dao;

import java.util.List;

import com.chinapalms.kwobox.javabean.OrderDetails;

public interface OrderDetailsDAO {

    public boolean addOrderDetails(OrderDetails orderDetails);

    public List<OrderDetails> findOrderDetailsByOrderId(String orderId);

}
