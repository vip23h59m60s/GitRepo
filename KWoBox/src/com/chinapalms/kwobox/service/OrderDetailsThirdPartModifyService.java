package com.chinapalms.kwobox.service;

import com.chinapalms.kwobox.dao.impl.OrderDetailsThirdPartModifyDAOImpl;
import com.chinapalms.kwobox.javabean.OrderDetailsThirdPartModify;

public class OrderDetailsThirdPartModifyService extends
        OrderDetailsThirdPartModifyDAOImpl {

    @Override
    public boolean addOrderDetailsThirdPartModify(
            OrderDetailsThirdPartModify orderDetailsThirdPartModify) {
        return super
                .addOrderDetailsThirdPartModify(orderDetailsThirdPartModify);
    }

}
