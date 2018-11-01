package com.chinapalms.kwobox.dao;

import java.util.List;

import com.chinapalms.kwobox.javabean.OrderDetailsThirdPart;

public interface OrderDetailsThirdPartDAO {

    public boolean addOrderDetailsThirdPart(
            OrderDetailsThirdPart orderDetailsThirdPart);

    public List<OrderDetailsThirdPart> findOrderDetailsThirdPartByOrderId(
            String orderId);

}
