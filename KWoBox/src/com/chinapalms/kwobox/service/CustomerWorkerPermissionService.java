package com.chinapalms.kwobox.service;

import com.chinapalms.kwobox.dao.impl.CustomerWorkerPermissionDAOImpl;
import com.chinapalms.kwobox.javabean.CustomerWorkerPermission;

public class CustomerWorkerPermissionService extends
        CustomerWorkerPermissionDAOImpl {

    @Override
    public CustomerWorkerPermission findCustomerWorkerPermissionByCustomerWorkerId(
            int customerWorkerId) {
        return super
                .findCustomerWorkerPermissionByCustomerWorkerId(customerWorkerId);
    }

}
