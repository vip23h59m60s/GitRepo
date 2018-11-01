package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.CustomerDAO;
import com.chinapalms.kwobox.javabean.BoxStatus;
import com.chinapalms.kwobox.javabean.Customer;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class CustomerDAOImpl implements CustomerDAO {

    Log log = LogFactory.getLog(CustomerDAOImpl.class);

    @Override
    public List<Customer> findAllCustomers() {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_customer ORDER by cooperateDatetime DESC";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Customer> customersList = new ArrayList<Customer>();
        Customer customer = null;
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                customer = new Customer();
                customer.setCustomerId(rs.getInt("customerId"));
                customer.setMchId(rs.getString("mchId"));
                customer.setCompanyName(rs.getString("companyName"));
                customer.setShortName(rs.getString("shortName"));
                customer.setOwner(rs.getString("owner"));
                customer.setIdentityCardNumber(rs
                        .getString("identityCardNumber"));
                customer.setRegisterAddress(rs.getString("registerAddress"));
                customer.setRegisterMoney(rs.getBigDecimal("registerMoney"));
                customer.setSocialCreditCode(rs.getString("socialCreditCode"));
                customer.setOperateCity(rs.getString("operateCity"));
                customer.setTaxInfo(rs.getString("taxInfo"));
                customer.setWorkAddress(rs.getString("workAddress"));
                customer.setAccountNumber(rs.getString("accountNumber"));
                customer.setLicencePicture(rs.getString("licencePicture"));
                customer.setCooperationMode(rs.getInt("cooperationMode"));
                customer.setBussinessManagerId(rs.getInt("bussinessManagerId"));
                customer.setCooperateDatetime(rs
                        .getTimestamp("cooperateDatetime"));
                customersList.add(customer);
            }
            return customersList;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public Customer findCustomerByMchId(String mchId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_customer WHERE mchId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        Customer customer = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, mchId);
            rs = ps.executeQuery();
            if (rs.next()) {
                customer = new Customer();
                customer.setCustomerId(rs.getInt("customerId"));
                customer.setMchId(rs.getString("mchId"));
                customer.setCompanyName(rs.getString("companyName"));
                customer.setShortName(rs.getString("shortName"));
                customer.setOwner(rs.getString("owner"));
                customer.setIdentityCardNumber(rs
                        .getString("identityCardNumber"));
                customer.setRegisterAddress(rs.getString("registerAddress"));
                customer.setRegisterMoney(rs.getBigDecimal("registerMoney"));
                customer.setSocialCreditCode(rs.getString("socialCreditCode"));
                customer.setOperateCity(rs.getString("operateCity"));
                customer.setTaxInfo(rs.getString("taxInfo"));
                customer.setWorkAddress(rs.getString("workAddress"));
                customer.setAccountNumber(rs.getString("accountNumber"));
                customer.setLicencePicture(rs.getString("licencePicture"));
                customer.setCooperationMode(rs.getInt("cooperationMode"));
                customer.setBussinessManagerId(rs.getInt("bussinessManagerId"));
                customer.setCooperateDatetime(rs
                        .getTimestamp("cooperateDatetime"));
            }
            return customer;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public Customer findCustomerByCustomerId(int customerId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_customer WHERE customerId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        Customer customer = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, customerId);
            rs = ps.executeQuery();
            if (rs.next()) {
                customer = new Customer();
                customer.setCustomerId(rs.getInt("customerId"));
                customer.setMchId(rs.getString("mchId"));
                customer.setCompanyName(rs.getString("companyName"));
                customer.setShortName(rs.getString("shortName"));
                customer.setOwner(rs.getString("owner"));
                customer.setIdentityCardNumber(rs
                        .getString("identityCardNumber"));
                customer.setRegisterAddress(rs.getString("registerAddress"));
                customer.setRegisterMoney(rs.getBigDecimal("registerMoney"));
                customer.setSocialCreditCode(rs.getString("socialCreditCode"));
                customer.setOperateCity(rs.getString("operateCity"));
                customer.setTaxInfo(rs.getString("taxInfo"));
                customer.setWorkAddress(rs.getString("workAddress"));
                customer.setAccountNumber(rs.getString("accountNumber"));
                customer.setLicencePicture(rs.getString("licencePicture"));
                customer.setCooperationMode(rs.getInt("cooperationMode"));
                customer.setBussinessManagerId(rs.getInt("bussinessManagerId"));
                customer.setCooperateDatetime(rs
                        .getTimestamp("cooperateDatetime"));
            }
            return customer;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

}
