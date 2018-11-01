package com.chinapalms.kwobox.service;

import net.sf.json.JSONObject;

import com.chinapalms.kwobox.dao.impl.CustomerWorkerDAOImpl;
import com.chinapalms.kwobox.javabean.CustomerWorker;
import com.chinapalms.kwobox.javabean.CustomerWorkerPermission;
import com.chinapalms.kwobox.utils.MD5;
import com.chinapalms.kwobox.utils.ResponseStatus;

public class CustomerWorkerService extends CustomerWorkerDAOImpl {

    @Override
    public CustomerWorker findCustomerWorkerByUserNameAndPassWord(
            String userName, String password) {
        return super
                .findCustomerWorkerByUserNameAndPassWord(userName, password);
    }

    @Override
    public CustomerWorker findCustomerWorkerByCustomerWorkerId(
            int customerWorkerId) {
        return super.findCustomerWorkerByCustomerWorkerId(customerWorkerId);
    }

    public String doCheckManagerLoginIdentify(String userName, String password) {
        // 先检查worker是否存在，在检查是否有补货等权限；
        CustomerWorker customerWorker = findCustomerWorkerByUserNameAndPassWord(
                userName, MD5.MD5Encode(password));
        JSONObject customerWorkerJsonObject = new JSONObject();
        if (customerWorker != null) {
            CustomerWorkerPermissionService customerWorkerPermissionService = new CustomerWorkerPermissionService();
            CustomerWorkerPermission customerWorkerPermission = customerWorkerPermissionService
                    .findCustomerWorkerPermissionByCustomerWorkerId(customerWorker
                            .getCustomerWorkerId());
            // 如果是商户员工并且该员工具有理货权限或者该员工账号为智购猫系统超级管理员
            if (customerWorkerPermission != null
                    && customerWorkerPermission.getBoxDeliveryPermission() == 1) {
                customerWorkerJsonObject.put("managePermissionFlag",
                        ResponseStatus.SUCCESS);
                customerWorkerJsonObject.put("CustomerWorkerInfo",
                        JSONObject.fromObject(customerWorker));
            } else if (customerWorker.getSuperPermission() == ResponseStatus.WORKER_PERMISSION_SUPER) {
                customerWorkerJsonObject.put("managePermissionFlag",
                        ResponseStatus.SUCCESS);
                customerWorkerJsonObject.put("CustomerWorkerInfo",
                        JSONObject.fromObject(customerWorker));
            } else {
                customerWorkerJsonObject.put("managePermissionFlag",
                        ResponseStatus.FAIL);
            }
        } else {
            customerWorkerJsonObject.put("managePermissionFlag",
                    ResponseStatus.FAIL);
        }
        return customerWorkerJsonObject.toString();
    }

}
