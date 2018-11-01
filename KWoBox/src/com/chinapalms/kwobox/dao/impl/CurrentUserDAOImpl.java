package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.CurrentUserDAO;
import com.chinapalms.kwobox.javabean.CurrentUser;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class CurrentUserDAOImpl implements CurrentUserDAO {

    Log log = LogFactory.getLog(CurrentUserDAOImpl.class);

    @Override
    public boolean addCurrentUser(CurrentUser currentUser) {
        boolean addFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "INSERT INTO t_current_user(phoneNumber, customerWorkerId, boxId, customType, userType, vipLevel, agreementNO) VALUES(?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, currentUser.getPhoneNumber());
            ps.setInt(2, currentUser.getCustomerWorkerId());
            ps.setString(3, currentUser.getBoxId());
            ps.setString(4, currentUser.getCustomType());
            ps.setString(5, currentUser.getUserType());
            ps.setInt(6, currentUser.getVipLevel());
            ps.setString(7, currentUser.getAgreementNO());
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
    public boolean deleteCurrentUser(CurrentUser currentUser) {
        boolean deleteFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "DELETE FROM t_current_user WHERE boxId = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, currentUser.getBoxId());
            int delete = ps.executeUpdate();
            if (delete > 0) {
                deleteFlag = true;
            }
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps);
        }
        return deleteFlag;
    }

    @Override
    public CurrentUser findCurrentUserByBoxId(String boxId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_current_user WHERE boxId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        CurrentUser currentUser = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, boxId);
            rs = ps.executeQuery();
            if (rs.next()) {
                currentUser = new CurrentUser();
                currentUser.setPhoneNumber(rs.getString("phoneNumber"));
                currentUser.setCustomerWorkerId(rs.getInt("customerWorkerId"));
                currentUser.setBoxId(rs.getString("boxId"));
                currentUser.setCustomType(rs.getString("customType"));
                currentUser.setUserType(rs.getString("userType"));
                currentUser.setVipLevel(rs.getInt("vipLevel"));
                currentUser.setAgreementNO(rs.getString("agreementNO"));
            }
            return currentUser;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public boolean updateCurrentUser(CurrentUser currentUser) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_current_user SET phoneNumber = ?, customerWorkerId = ?, customType = ?, userType = ?, vipLevel = ?, agreementNO = ? WHERE boxId = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, currentUser.getPhoneNumber());
            ps.setInt(2, currentUser.getCustomerWorkerId());
            ps.setString(3, currentUser.getCustomType());
            ps.setString(4, currentUser.getUserType());
            ps.setInt(5, currentUser.getVipLevel());
            ps.setString(6, currentUser.getAgreementNO());
            ps.setString(7, currentUser.getBoxId());
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
