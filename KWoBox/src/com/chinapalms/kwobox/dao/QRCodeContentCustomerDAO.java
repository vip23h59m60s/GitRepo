package com.chinapalms.kwobox.dao;

import com.chinapalms.kwobox.javabean.QRCodeContentCustomer;

public interface QRCodeContentCustomerDAO {

    public QRCodeContentCustomer findQRCodeContentCustomerByCustomerId(
            int customerId);
}
