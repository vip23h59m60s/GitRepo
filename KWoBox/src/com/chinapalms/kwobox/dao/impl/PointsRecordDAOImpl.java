package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.PointsRecordDAO;
import com.chinapalms.kwobox.javabean.PersonalCreditRecord;
import com.chinapalms.kwobox.javabean.PointsRecord;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class PointsRecordDAOImpl implements PointsRecordDAO {

    Log log = LogFactory.getLog(PointsRecordDAOImpl.class);

    @Override
    public boolean addPointsRecordCreditRecord(PointsRecord pointsRecord) {
        boolean addFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "INSERT INTO t_points_record(phoneNumber, pointsChange, changeReason, changeDatetime) VALUES(?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, pointsRecord.getPhoneNumber());
            ps.setInt(2, pointsRecord.getPointsChange());
            ps.setInt(3, pointsRecord.getChangeReason());
            ps.setTimestamp(4, new Timestamp(pointsRecord.getChangeTime()
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
    public List<PointsRecord> findPointsRecordsByPhoneNumber(String phoneNumber) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_points_record WHERE phoneNumber = ? ORDER BY changeTime DESC";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<PointsRecord> pointsRecordsList = new ArrayList<PointsRecord>();
        PointsRecord pointsRecord = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, phoneNumber);
            rs = ps.executeQuery();
            while (rs.next()) {
                pointsRecord = new PointsRecord();
                pointsRecord.setPhoneNumber(rs.getString("phoneNumber"));
                pointsRecord.setPointsChange(rs.getInt("pointsChange"));
                pointsRecord.setChangeReason(rs.getInt("changeReason"));
                pointsRecord.setChangeTime(rs.getTimestamp("changeDatetime"));
                pointsRecordsList.add(pointsRecord);
            }
            return pointsRecordsList;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public int findPointsRecordsCountByPhoneNumber(String phoneNumber) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT COUNT(*) AS pointRecordCount FROM t_points_record WHERE phoneNumber = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        int orderCount = 0;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, phoneNumber);
            rs = ps.executeQuery();
            if (rs.next()) {
                orderCount = rs.getInt("pointRecordCount");
            }
            return orderCount;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public List<PointsRecord> findPointsRecordsByPhoneNumberAndPageNumber(
            String phoneNumber, int pageNumber) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_points_record WHERE phoneNumber = ? ORDER BY changeDatetime DESC LIMIT ? , ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<PointsRecord> pointsRecordsList = new ArrayList<PointsRecord>();
        PointsRecord pointsRecord = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, phoneNumber);
            ps.setInt(2, (pageNumber - 1) * PointsRecord.PAGE_SIZE);
            ps.setInt(3, PointsRecord.PAGE_SIZE);
            rs = ps.executeQuery();
            while (rs.next()) {
                pointsRecord = new PointsRecord();
                pointsRecord = new PointsRecord();
                pointsRecord.setPhoneNumber(rs.getString("phoneNumber"));
                pointsRecord.setPointsChange(rs.getInt("pointsChange"));
                pointsRecord.setChangeReason(rs.getInt("changeReason"));
                pointsRecord.setChangeTime(rs.getTimestamp("changeDatetime"));
                pointsRecordsList.add(pointsRecord);
            }
            return pointsRecordsList;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

}
