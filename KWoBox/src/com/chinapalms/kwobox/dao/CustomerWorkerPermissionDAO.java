package com.chinapalms.kwobox.dao;

import com.chinapalms.kwobox.javabean.CustomerWorkerPermission;

public interface CustomerWorkerPermissionDAO {

    public CustomerWorkerPermission findCustomerWorkerPermissionByCustomerWorkerId(
            int customerWorkerId);
}
