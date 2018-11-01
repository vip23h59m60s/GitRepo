package com.chinapalms.kwobox.service;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.chinapalms.kwobox.dao.impl.CustomerDAOImpl;
import com.chinapalms.kwobox.javabean.Customer;
import com.chinapalms.kwobox.utils.ResponseStatus;

public class CustomerService extends CustomerDAOImpl {

    @Override
    public List<Customer> findAllCustomers() {
        return super.findAllCustomers();
    }

    @Override
    public Customer findCustomerByMchId(String mchId) {
        return super.findCustomerByMchId(mchId);
    }

    @Override
    public Customer findCustomerByCustomerId(int customerId) {
        return super.findCustomerByCustomerId(customerId);
    }

    public String doFindAllCustomers() {
        List<Customer> customersList = findAllCustomers();
        JSONArray customerJsonArray = JSONArray.fromObject(customersList);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Customers", customerJsonArray);
        return jsonObject.toString();
    }

    public String doFindAllShoppingInfoCustomers() {
        List<Customer> customersList = findAllCustomers();
        JSONArray customerJsonArray = new JSONArray();
        for (int i = 0; i < customersList.size(); i++) {
            Customer customer = customersList.get(i);
            JSONObject customerJsonObject = JSONObject.fromObject(customer);
            // 查找对应商户的未处理异常的柜子数
            ShoppingInfoService shoppingInfoService = new ShoppingInfoService();
            int unFinishedOrderBoxesCount = shoppingInfoService
                    .findShoppingInfosBoxesCountByCustomerIdAndState(
                            customer.getCustomerId(),
                            ResponseStatus.SHOPPING_INFO_STATE_CLOSED);
            customerJsonObject.put("unFinishedExceptionBoxesNumber",
                    unFinishedOrderBoxesCount);
            customerJsonArray.add(customerJsonObject);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Customers", customerJsonArray);
        return jsonObject.toString();
    }

}
