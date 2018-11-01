package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.RollingAdCustomerDAO;
import com.chinapalms.kwobox.javabean.RollingAdBox;
import com.chinapalms.kwobox.javabean.RollingAdCustomer;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class RollingAdCustomerDAOImpl implements RollingAdCustomerDAO {

    Log log = LogFactory.getLog(RollingAdCustomerDAOImpl.class);

    @Override
    public RollingAdCustomer findRollingAdCustomerByCustomerId(int customerId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_rolling_ad_customer WHERE customerId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        RollingAdCustomer rollingAdCustomer = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, customerId);
            rs = ps.executeQuery();
            if (rs.next()) {
                rollingAdCustomer = new RollingAdCustomer();
                rollingAdCustomer.setCustomerAdId(rs.getInt("customerAdId"));
                rollingAdCustomer.setCustomerAdName(rs
                        .getString("customerAdName"));
                rollingAdCustomer.setCustomerAdUrl(rs
                        .getString("customerAdUrl"));
                rollingAdCustomer.setCustomerId(rs.getInt("customerId"));
                rollingAdCustomer.setStartTime(rs.getTimestamp("startTime"));
                rollingAdCustomer.setEndTime(rs.getTimestamp("endTime"));
                rollingAdCustomer.setAdState(rs.getInt("adState"));
            }
            return rollingAdCustomer;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

}
