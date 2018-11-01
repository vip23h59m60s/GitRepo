package com.chinapalms.kwobox.service;

import com.chinapalms.kwobox.dao.impl.OrderDetailsModifyDAOImpl;

public class OrderDetailsModifyService extends OrderDetailsModifyDAOImpl {

    @Override
    public boolean addOrderDetailsModify(
            com.chinapalms.kwobox.javabean.OrderDetailsModify orderDetails) {
        return super.addOrderDetailsModify(orderDetails);
    }

}
