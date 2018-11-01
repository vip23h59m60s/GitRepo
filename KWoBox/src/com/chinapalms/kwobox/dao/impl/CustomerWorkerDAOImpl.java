package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.CustomerWorkerDAO;
import com.chinapalms.kwobox.javabean.CustomerWorker;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class CustomerWorkerDAOImpl implements CustomerWorkerDAO {

    Log log = LogFactory.getLog(CustomerWorkerDAOImpl.class);

    @Override
    public CustomerWorker findCustomerWorkerByUserNameAndPassWord(
            String userName, String password) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_customer_worker WHERE userName = ? AND password = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        CustomerWorker customerWorker = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, userName);
            ps.setString(2, password);
            rs = ps.executeQuery();
            if (rs.next()) {
                customerWorker = new CustomerWorker();
                customerWorker.setCustomerWorkerId(rs
                        .getInt("customerWorkerId"));
                customerWorker.setIdentityCardNumber(rs
                        .getString("identityCardNumber"));
                customerWorker.setName(rs.getString("name"));
                customerWorker.setPhoneNumber(rs.getString("phoneNumber"));
                customerWorker.setUserName(rs.getString("userName"));
                customerWorker.setPassword(rs.getString("password"));
                customerWorker.setCustomerId(rs.getInt("customerId"));
                customerWorker.setCustomerAdminId(rs.getInt("customerAdminId"));
                customerWorker.setSuperPermission(rs.getInt("superPermission"));
                customerWorker.setUpdateDateTime(rs
                        .getTimestamp("updateDateTime"));
            }
            return customerWorker;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public CustomerWorker findCustomerWorkerByCustomerWorkerId(
            int customerWorkerId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_customer_worker WHERE customerWorkerId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        CustomerWorker customerWorker = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, customerWorkerId);
            rs = ps.executeQuery();
            if (rs.next()) {
                customerWorker = new CustomerWorker();
                customerWorker.setCustomerWorkerId(rs
                        .getInt("customerWorkerId"));
                customerWorker.setIdentityCardNumber(rs
                        .getString("identityCardNumber"));
                customerWorker.setName(rs.getString("name"));
                customerWorker.setPhoneNumber(rs.getString("phoneNumber"));
                customerWorker.setUserName(rs.getString("userName"));
                customerWorker.setPassword(rs.getString("password"));
                customerWorker.setCustomerId(rs.getInt("customerId"));
                customerWorker.setCustomerAdminId(rs.getInt("customerAdminId"));
                customerWorker.setSuperPermission(rs.getInt("superPermission"));
                customerWorker.setUpdateDateTime(rs
                        .getTimestamp("updateDateTime"));
            }
            return customerWorker;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

}
