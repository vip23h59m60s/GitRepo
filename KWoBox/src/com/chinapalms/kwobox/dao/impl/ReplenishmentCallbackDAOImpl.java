package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.ReplenishmentCallbackDAO;
import com.chinapalms.kwobox.javabean.ReplenishmentCallback;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class ReplenishmentCallbackDAOImpl implements ReplenishmentCallbackDAO {

    Log log = LogFactory.getLog(ReplenishmentCallbackDAOImpl.class);

    @Override
    public ReplenishmentCallback findReplenishmentCallbackByCustomerId(
            int customerId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_replenishment_callback WHERE customerId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        ReplenishmentCallback replenishmentCallback = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, customerId);
            rs = ps.executeQuery();
            if (rs.next()) {
                replenishmentCallback = new ReplenishmentCallback();
                replenishmentCallback.setCustomerId(rs.getInt("customerId"));
                replenishmentCallback.setReplenishmentCallbackUrl(rs
                        .getString("replenishmentCallbackUrl"));
            }
            return replenishmentCallback;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public boolean updateReplenishmentCallback(
            ReplenishmentCallback replenishmentCallback) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_replenishment_callback SET replenishmentCallbackUrl = ? WHERE customerId = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, replenishmentCallback.getReplenishmentCallbackUrl());
            ps.setInt(2, replenishmentCallback.getCustomerId());
            ps.executeUpdate();
            updateFlag = true;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps);
        }
        return updateFlag;
    }

}
