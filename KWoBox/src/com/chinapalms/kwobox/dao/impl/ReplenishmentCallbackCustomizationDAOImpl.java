package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.ReplenishmentCallbackCustomizationDAO;
import com.chinapalms.kwobox.javabean.OrderCallbackCustomization;
import com.chinapalms.kwobox.javabean.ReplenishmentCallbackCustomization;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class ReplenishmentCallbackCustomizationDAOImpl implements
        ReplenishmentCallbackCustomizationDAO {

    Log log = LogFactory
            .getLog(ReplenishmentCallbackCustomizationDAOImpl.class);

    @Override
    public boolean addReplenishmentCallbackCustomization(
            ReplenishmentCallbackCustomization replenishmentCallbackCustomization) {
        boolean addFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "INSERT INTO t_replenishment_callback_customization(customerId, boxId, replenishmentCallbackUrl) VALUES(?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, replenishmentCallbackCustomization.getCustomerId());
            ps.setString(2, replenishmentCallbackCustomization.getBoxId());
            ps.setString(3, replenishmentCallbackCustomization
                    .getReplenishmentCallbackUrl());
            int add = ps.executeUpdate();
            if (add > 0) {
                addFlag = true;
            }
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps);
        }
        return addFlag;
    }

    @Override
    public boolean updateReplenishmentCallbackCustomization(
            ReplenishmentCallbackCustomization replenishmentCallbackCustomization) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_replenishment_callback_customization SET replenishmentCallbackUrl = ? WHERE customerId = ? AND boxId = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, replenishmentCallbackCustomization
                    .getReplenishmentCallbackUrl());
            ps.setInt(2, replenishmentCallbackCustomization.getCustomerId());
            ps.setString(3, replenishmentCallbackCustomization.getBoxId());
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

    @Override
    public ReplenishmentCallbackCustomization findReplenishmentCallbackCustomizationByCustomerIdAndBoxId(
            int customerId, String boxId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_replenishment_callback_customization WHERE customerId = ? AND boxId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        ReplenishmentCallbackCustomization replenishmentCallbackCustomization = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, customerId);
            ps.setString(2, boxId);
            rs = ps.executeQuery();
            if (rs.next()) {
                replenishmentCallbackCustomization = new ReplenishmentCallbackCustomization();
                replenishmentCallbackCustomization.setCustomerId(rs
                        .getInt("customerId"));
                replenishmentCallbackCustomization.setBoxId(rs
                        .getString("boxId"));
                replenishmentCallbackCustomization
                        .setReplenishmentCallbackUrl(rs
                                .getString("replenishmentCallbackUrl"));
            }
            return replenishmentCallbackCustomization;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

}
