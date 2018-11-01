package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.CustomerWorkerPermissionDAO;
import com.chinapalms.kwobox.javabean.CustomerWorkerPermission;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class CustomerWorkerPermissionDAOImpl implements
        CustomerWorkerPermissionDAO {

    Log log = LogFactory.getLog(CustomerWorkerPermissionDAOImpl.class);

    @Override
    public CustomerWorkerPermission findCustomerWorkerPermissionByCustomerWorkerId(
            int customerWorkerId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_customer_worker_permission WHERE customerWorkerId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        CustomerWorkerPermission customerWorkerPermission = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, customerWorkerId);
            rs = ps.executeQuery();
            if (rs.next()) {
                customerWorkerPermission = new CustomerWorkerPermission();
                customerWorkerPermission.setCustomerWorkerId(rs
                        .getInt("customerWorkerId"));
                customerWorkerPermission.setBoxManagePermission(rs
                        .getInt("boxManagePermission"));
                customerWorkerPermission.setBoxDeliveryPermission(rs
                        .getInt("boxDeliveryPermission"));
                customerWorkerPermission.setMaintainerPermission(rs
                        .getInt("maintainerPermission"));
                customerWorkerPermission.setDataManagerPermission(rs
                        .getInt("dataManagerPermission"));
                customerWorkerPermission.setCustomerAdminId(rs
                        .getInt("customerAdminId"));
                customerWorkerPermission.setGoodsManagerPermission(rs
                        .getInt("goodsManagerPermission"));
                customerWorkerPermission.setUpdateDateTime(rs
                        .getTimestamp("updateDateTime"));
            }
            return customerWorkerPermission;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

}
