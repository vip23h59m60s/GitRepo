package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.SignInDAO;
import com.chinapalms.kwobox.javabean.Boxes;
import com.chinapalms.kwobox.javabean.SignIn;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class SignInDAOImpl implements SignInDAO {

    Log log = LogFactory.getLog(SignInDAOImpl.class);

    @Override
    public boolean addSignIn(SignIn signIn) {
        boolean addFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "INSERT INTO t_sign_in(phoneNumber, latitude, longitude, address, contiuneTimes, signInDateTime) VALUES(?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, signIn.getPhoneNumber());
            ps.setString(2, signIn.getLatitude());
            ps.setString(3, signIn.getLongitude());
            ps.setString(4, signIn.getAddress());
            ps.setInt(5, signIn.getContiuneTimes());
            ps.setTimestamp(6, new Timestamp(signIn.getSignInDateTime()
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
    public SignIn findSignInByPhoneNumber(String phoneNumber) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_sign_in WHERE phoneNumber = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        SignIn signIn = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, phoneNumber);
            rs = ps.executeQuery();
            if (rs.next()) {
                signIn = new SignIn();
                signIn.setPhoneNumber(rs.getString("phoneNumber"));
                signIn.setLatitude(rs.getString("latitude"));
                signIn.setLongitude(rs.getString("longitude"));
                signIn.setAddress(rs.getString("address"));
                signIn.setContiuneTimes(rs.getInt("contiuneTimes"));
                signIn.setSignInDateTime(rs.getTimestamp("signInDateTime"));
            }
            return signIn;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public boolean resetSignInContiuneTimes(String phoneNumber) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_sign_in SET contiuneTimes = ? WHERE phoneNumber = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, 0);
            ps.setString(2, phoneNumber);
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
    public boolean updateSignIn(SignIn signIn) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_sign_in SET latitude = ?, longitude = ?, address = ?, contiuneTimes = contiuneTimes + 1, signInDateTime = ? WHERE phoneNumber = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, signIn.getLatitude());
            ps.setString(2, signIn.getLongitude());
            ps.setString(3, signIn.getAddress());
            ps.setTimestamp(4, new Timestamp(signIn.getSignInDateTime()
                    .getTime()));
            ps.setString(5, signIn.getPhoneNumber());
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
