package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.SignInRecordDAO;
import com.chinapalms.kwobox.javabean.Order;
import com.chinapalms.kwobox.javabean.SignInRecord;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class SignInRecordDAOImpl implements SignInRecordDAO {

    Log log = LogFactory.getLog(SignInDAOImpl.class);

    @Override
    public boolean addSignInRecord(SignInRecord signInRecord) {
        boolean addFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "INSERT INTO t_sign_in_record(phoneNumber, latitude, longitude, address, scores, signInDateTime) VALUES(?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, signInRecord.getPhoneNumber());
            ps.setString(2, signInRecord.getLatitude());
            ps.setString(3, signInRecord.getLongitude());
            ps.setString(4, signInRecord.getAddress());
            ps.setInt(5, signInRecord.getScores());
            ps.setTimestamp(6, new Timestamp(signInRecord.getSignInDateTime()
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
    public int findTotalSignInPoints(String phoneNumber) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT SUM(scores) AS totalSignInPoints FROM t_sign_in_record WHERE phoneNumber = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        int totalSignInPoints = 0;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, phoneNumber);
            rs = ps.executeQuery();
            if (rs.next()) {
                totalSignInPoints = rs.getInt("totalSignInPoints");
            }
            return totalSignInPoints;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

}
