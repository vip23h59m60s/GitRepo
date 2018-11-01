package com.chinapalms.kwobox.service;

import com.chinapalms.kwobox.dao.impl.QRCodeContentCustomerDAOImpl;

public class QRCodeContentCustomerService extends QRCodeContentCustomerDAOImpl {

    @Override
    public com.chinapalms.kwobox.javabean.QRCodeContentCustomer findQRCodeContentCustomerByCustomerId(
            int customerId) {
        return super.findQRCodeContentCustomerByCustomerId(customerId);
    }

}
