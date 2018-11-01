package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.CustomerSecretKeyDAO;
import com.chinapalms.kwobox.javabean.CustomerSecretKey;
import com.chinapalms.kwobox.javabean.RollingAdBox;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class CustomerSecretKeyDAOImpl implements CustomerSecretKeyDAO {

    Log log = LogFactory.getLog(CustomerSecretKeyDAOImpl.class);

    @Override
    public CustomerSecretKey findCustomerSecretKeyByCustomerId(int customerId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_customer_secretkey WHERE customerId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        CustomerSecretKey customerSecretKey = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, customerId);
            rs = ps.executeQuery();
            if (rs.next()) {
                customerSecretKey = new CustomerSecretKey();
                customerSecretKey.setCustomerId(rs.getInt("customerId"));
                customerSecretKey.setSecretKey(rs.getString("secretKey"));
            }
            return customerSecretKey;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

}
