package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.DoorOpenedCallbackDAO;
import com.chinapalms.kwobox.javabean.CustomerSecretKey;
import com.chinapalms.kwobox.javabean.DoorOpenedCallback;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class DoorOpenedCallbackDAOImpl implements DoorOpenedCallbackDAO {

    Log log = LogFactory.getLog(DoorOpenedCallbackDAOImpl.class);

    @Override
    public DoorOpenedCallback findDoorOpenedCallbackByCustomerId(int customerId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_door_opened_callback WHERE customerId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        DoorOpenedCallback doorOpenedCallback = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, customerId);
            rs = ps.executeQuery();
            if (rs.next()) {
                doorOpenedCallback = new DoorOpenedCallback();
                doorOpenedCallback.setCustomerId(rs.getInt("customerId"));
                doorOpenedCallback.setDoorOpenedCallbackUrl(rs
                        .getString("doorOpenedCallbackUrl"));
            }
            return doorOpenedCallback;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public boolean updateDoorOpenedCallback(
            DoorOpenedCallback doorOpenedCallback) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_door_opened_callback SET doorOpenedCallbackUrl = ? WHERE customerId = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, doorOpenedCallback.getDoorOpenedCallbackUrl());
            ps.setInt(2, doorOpenedCallback.getCustomerId());
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
