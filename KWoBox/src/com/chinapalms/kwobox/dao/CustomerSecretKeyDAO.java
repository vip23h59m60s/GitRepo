package com.chinapalms.kwobox.dao;

import com.chinapalms.kwobox.javabean.CustomerSecretKey;

public interface CustomerSecretKeyDAO {

    public CustomerSecretKey findCustomerSecretKeyByCustomerId(int customerId);

}
