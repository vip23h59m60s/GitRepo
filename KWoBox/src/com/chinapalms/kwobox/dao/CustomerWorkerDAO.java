package com.chinapalms.kwobox.dao;

import com.chinapalms.kwobox.javabean.CustomerWorker;

public interface CustomerWorkerDAO {

    public CustomerWorker findCustomerWorkerByUserNameAndPassWord(
            String userName, String password);

    public CustomerWorker findCustomerWorkerByCustomerWorkerId(
            int customerWorkerId);

}
