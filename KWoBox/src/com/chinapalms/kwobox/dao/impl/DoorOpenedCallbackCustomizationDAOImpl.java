package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.DoorOpenedCallbackCustomizationDAO;
import com.chinapalms.kwobox.javabean.DoorClosedCallback;
import com.chinapalms.kwobox.javabean.DoorOpenedCallbackCustomization;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class DoorOpenedCallbackCustomizationDAOImpl implements
        DoorOpenedCallbackCustomizationDAO {

    Log log = LogFactory.getLog(DoorOpenedCallbackCustomization.class);

    @Override
    public boolean addDoorOpenedCallbackCustomization(
            DoorOpenedCallbackCustomization doorOpenedCallbackCustomization) {
        boolean addFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "INSERT INTO t_door_opened_callback_customization(customerId, boxId, doorOpenedCallbackUrl) VALUES(?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, doorOpenedCallbackCustomization.getCustomerId());
            ps.setString(2, doorOpenedCallbackCustomization.getBoxId());
            ps.setString(3,
                    doorOpenedCallbackCustomization.getDoorOpenedCallbackUrl());
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
    public boolean updateDoorOpenedCallbackCustomization(
            DoorOpenedCallbackCustomization doorOpenedCallbackCustomization) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_door_opened_callback_customization SET doorOpenedCallbackUrl = ? WHERE customerId = ? AND boxId = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1,
                    doorOpenedCallbackCustomization.getDoorOpenedCallbackUrl());
            ps.setInt(2, doorOpenedCallbackCustomization.getCustomerId());
            ps.setString(3, doorOpenedCallbackCustomization.getBoxId());
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
    public DoorOpenedCallbackCustomization findDoorOpenedCallbackCustomizationByCustomerIdAndBoxId(
            int customerId, String boxId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_door_opened_callback_customization WHERE customerId = ? AND boxId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        DoorOpenedCallbackCustomization doorOpenedCallbackCustomization = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, customerId);
            ps.setString(2, boxId);
            rs = ps.executeQuery();
            if (rs.next()) {
                doorOpenedCallbackCustomization = new DoorOpenedCallbackCustomization();
                doorOpenedCallbackCustomization.setCustomerId(rs
                        .getInt("customerId"));
                doorOpenedCallbackCustomization.setBoxId(rs.getString("boxId"));
                doorOpenedCallbackCustomization.setDoorOpenedCallbackUrl(rs
                        .getString("doorOpenedCallbackUrl"));
            }
            return doorOpenedCallbackCustomization;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

}
