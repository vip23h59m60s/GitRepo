package com.chinapalms.kwobox.service;

import com.chinapalms.kwobox.dao.impl.DynamicPasswordCallbackDAOImpl;
import com.chinapalms.kwobox.javabean.DynamicPasswordCallback;

public class DynamicPasswordCallbackService extends
        DynamicPasswordCallbackDAOImpl {

    @Override
    public DynamicPasswordCallback findDynamicPasswordCallbackByCustomerId(
            int customerId) {
        return super.findDynamicPasswordCallbackByCustomerId(customerId);
    }

}
