package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.BoxStatusDAO;
import com.chinapalms.kwobox.javabean.BoxStatus;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class BoxStatusDAOImpl implements BoxStatusDAO {

    Log log = LogFactory.getLog(BoxStatusDAOImpl.class);

    @Override
    public boolean updateBoxStatus(BoxStatus boxStatus) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_box_status SET state = ?, reportTime = ? WHERE boxId = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, boxStatus.getState());
            ps.setTimestamp(2, new Timestamp(boxStatus.getReportTime()
                    .getTime()));
            ps.setString(3, boxStatus.getBoxId());
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
    public BoxStatus findBoxStatusByBoxId(String boxId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_box_status WHERE boxId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        BoxStatus boxStatus = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, boxId);
            rs = ps.executeQuery();
            if (rs.next()) {
                boxStatus = new BoxStatus();
                boxStatus.setBoxId(rs.getString("boxId"));
                boxStatus.setState(rs.getInt("state"));
                boxStatus.setReportTime(rs.getTimestamp("reportTime"));
            }
            return boxStatus;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public boolean addBoxStatus(BoxStatus boxStatus) {
        boolean addFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "INSERT INTO t_box_status(boxId, state, reportTime) VALUES(?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, boxStatus.getBoxId());
            ps.setInt(2, boxStatus.getState());
            ps.setTimestamp(3, new Timestamp(boxStatus.getReportTime()
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

}
