package com.chinapalms.kwobox.dao;

import java.util.List;

import com.chinapalms.kwobox.javabean.Customer;

public interface CustomerDAO {

    public List<Customer> findAllCustomers();

    public Customer findCustomerByMchId(String mchId);

    public Customer findCustomerByCustomerId(int customerId);

}
