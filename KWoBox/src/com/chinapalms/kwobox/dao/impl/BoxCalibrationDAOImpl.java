package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.BoxCalibrationDAO;
import com.chinapalms.kwobox.javabean.BoxCalibration;
import com.chinapalms.kwobox.javabean.ICS;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class BoxCalibrationDAOImpl implements BoxCalibrationDAO {

    Log log = LogFactory.getLog(BoxCalibrationDAOImpl.class);

    @Override
    public boolean addBoxCalibration(BoxCalibration boxCalibration) {
        boolean addFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "INSERT INTO t_box_calibration(boxId, fileUrl, calibrateTime) VALUES(?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, boxCalibration.getBoxId());
            ps.setString(2, boxCalibration.getFileUrl());
            ps.setTimestamp(3, new Timestamp(boxCalibration.getCalibrateTime()
                    .getTime()));
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
    public boolean updateBoxCalibration(BoxCalibration boxCalibration) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_box_calibration SET fileUrl = ?, calibrateTime = ? WHERE boxId = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, boxCalibration.getFileUrl());
            ps.setTimestamp(2, new Timestamp(boxCalibration.getCalibrateTime()
                    .getTime()));
            ps.setString(3, boxCalibration.getBoxId());
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
    public BoxCalibration findBoxCalibrationByBoxId(String boxId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_box_calibration WHERE boxId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        BoxCalibration boxCalibration = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, boxId);
            rs = ps.executeQuery();
            if (rs.next()) {
                boxCalibration = new BoxCalibration();
                boxCalibration.setBoxId(rs.getString("boxId"));
                boxCalibration.setFileUrl(rs.getString("fileUrl"));
                boxCalibration.setCalibrateTime(rs
                        .getTimestamp("calibrateTime"));
            }
            return boxCalibration;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

}
