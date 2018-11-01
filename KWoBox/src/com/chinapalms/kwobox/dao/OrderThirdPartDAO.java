package com.chinapalms.kwobox.dao;

import com.chinapalms.kwobox.javabean.OrderThirdPart;

public interface OrderThirdPartDAO {

    public boolean addOrderThirdPart(OrderThirdPart orderThirdPart);

    public OrderThirdPart findOrderThirdPartByOrderId(String orderId);

    public boolean updateThirdPartOrder(OrderThirdPart orderThirdPart);

}
