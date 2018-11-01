package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.DoorClosedCallbackCustomizationDAO;
import com.chinapalms.kwobox.javabean.DoorClosedCallbackCustomization;
import com.chinapalms.kwobox.javabean.DoorOpenedCallbackCustomization;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class DoorClosedCallbackCustomizationDAOImpl implements
        DoorClosedCallbackCustomizationDAO {

    Log log = LogFactory.getLog(DoorClosedCallbackCustomizationDAOImpl.class);

    @Override
    public boolean addDoorClosedCallbackCustomization(
            DoorClosedCallbackCustomization doorClosedCallbackCustomization) {
        boolean addFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "INSERT INTO t_door_closed_callback_customization(customerId, boxId, doorClosedCallbackUrl) VALUES(?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, doorClosedCallbackCustomization.getCustomerId());
            ps.setString(2, doorClosedCallbackCustomization.getBoxId());
            ps.setString(3,
                    doorClosedCallbackCustomization.getDoorClosedCallbackUrl());
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
    public boolean updateDoorClosedCallbackCustomization(
            DoorClosedCallbackCustomization doorClosedCallbackCustomization) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_door_closed_callback_customization SET doorClosedCallbackUrl = ? WHERE customerId = ? AND boxId = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1,
                    doorClosedCallbackCustomization.getDoorClosedCallbackUrl());
            ps.setInt(2, doorClosedCallbackCustomization.getCustomerId());
            ps.setString(3, doorClosedCallbackCustomization.getBoxId());
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
    public DoorClosedCallbackCustomization findDoorClosedCallbackCustomizationByCustomerIdAndBoxId(
            int customerId, String boxId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_door_closed_callback_customization WHERE customerId = ? AND boxId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        DoorClosedCallbackCustomization doorClosedCallbackCustomization = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, customerId);
            ps.setString(2, boxId);
            rs = ps.executeQuery();
            if (rs.next()) {
                doorClosedCallbackCustomization = new DoorClosedCallbackCustomization();
                doorClosedCallbackCustomization.setCustomerId(rs
                        .getInt("customerId"));
                doorClosedCallbackCustomization.setBoxId(rs.getString("boxId"));
                doorClosedCallbackCustomization.setDoorClosedCallbackUrl(rs
                        .getString("doorClosedCallbackUrl"));
            }
            return doorClosedCallbackCustomization;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

}
