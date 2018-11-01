package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.DoorClosedCallbackDAO;
import com.chinapalms.kwobox.javabean.DoorClosedCallback;
import com.chinapalms.kwobox.javabean.DoorOpenedCallback;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class DoorClosedCallbackDAOImpl implements DoorClosedCallbackDAO {

    Log log = LogFactory.getLog(DoorClosedCallbackDAOImpl.class);

    @Override
    public DoorClosedCallback findDoorClosedCallbackByCustomerId(int customerId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_door_closed_callback WHERE customerId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        DoorClosedCallback doorClosedCallback = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, customerId);
            rs = ps.executeQuery();
            if (rs.next()) {
                doorClosedCallback = new DoorClosedCallback();
                doorClosedCallback.setCustomerId(rs.getInt("customerId"));
                doorClosedCallback.setDoorClosedCallbackUrl(rs
                        .getString("doorClosedCallbackUrl"));
            }
            return doorClosedCallback;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public boolean updateDoorClosedCallback(DoorClosedCallback dorCallback) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_door_closed_callback SET doorClosedCallbackUrl = ? WHERE customerId = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, dorCallback.getDoorClosedCallbackUrl());
            ps.setInt(2, dorCallback.getCustomerId());
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
