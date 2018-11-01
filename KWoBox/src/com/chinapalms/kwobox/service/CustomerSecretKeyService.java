package com.chinapalms.kwobox.service;

import com.chinapalms.kwobox.dao.impl.CustomerSecretKeyDAOImpl;
import com.chinapalms.kwobox.javabean.CustomerSecretKey;

public class CustomerSecretKeyService extends CustomerSecretKeyDAOImpl {

    @Override
    public CustomerSecretKey findCustomerSecretKeyByCustomerId(int customerId) {
        return super.findCustomerSecretKeyByCustomerId(customerId);
    }

}
