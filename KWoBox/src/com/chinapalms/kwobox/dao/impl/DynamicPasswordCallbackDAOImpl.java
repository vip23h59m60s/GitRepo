package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.DynamicPasswordCallbackDAO;
import com.chinapalms.kwobox.javabean.DynamicPasswordCallback;
import com.chinapalms.kwobox.javabean.OrderCallback;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class DynamicPasswordCallbackDAOImpl implements
        DynamicPasswordCallbackDAO {

    Log log = LogFactory.getLog(DynamicPasswordCallbackDAOImpl.class);

    @Override
    public DynamicPasswordCallback findDynamicPasswordCallbackByCustomerId(
            int customerId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_dynamicpassword_callback WHERE customerId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        DynamicPasswordCallback dynamicPasswordCallback = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, customerId);
            rs = ps.executeQuery();
            if (rs.next()) {
                dynamicPasswordCallback = new DynamicPasswordCallback();
                dynamicPasswordCallback.setCustomerId(rs.getInt("customerId"));
                dynamicPasswordCallback.setDynamicPasswordCallbackUrl(rs
                        .getString("dynamicPasswordCallbackUrl"));
            }
            return dynamicPasswordCallback;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

}
