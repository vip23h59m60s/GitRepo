package com.chinapalms.kwobox.dao;

import com.chinapalms.kwobox.javabean.DynamicPasswordCallback;

public interface DynamicPasswordCallbackDAO {

    public DynamicPasswordCallback findDynamicPasswordCallbackByCustomerId(
            int customerId);

}
