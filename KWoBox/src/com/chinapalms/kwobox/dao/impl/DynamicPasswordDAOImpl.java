package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.DynamicPasswordDAO;
import com.chinapalms.kwobox.javabean.DynamicPassword;
import com.chinapalms.kwobox.javabean.SignIn;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class DynamicPasswordDAOImpl implements DynamicPasswordDAO {

    Log log = LogFactory.getLog(DynamicPasswordDAOImpl.class);

    @Override
    public boolean addDynamicPassword(DynamicPassword dynamicPassword) {
        boolean addFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "INSERT INTO t_dynamic_password(phoneNumber, dynamicPassword, passwordTime) VALUES(?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, dynamicPassword.getPhoneNumber());
            ps.setString(2, dynamicPassword.getDynamicPassword());
            ps.setTimestamp(3, new Timestamp(dynamicPassword.getPasswordTime()
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
    public boolean updateDynamicPassword(DynamicPassword dynamicPassword) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_dynamic_password SET dynamicPassword = ?, passwordTime = ? WHERE phoneNumber = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, dynamicPassword.getDynamicPassword());
            ps.setTimestamp(2, new Timestamp(dynamicPassword.getPasswordTime()
                    .getTime()));
            ps.setString(3, dynamicPassword.getPhoneNumber());
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
    public DynamicPassword findDynamicPasswordByPhoneNumber(String phoneNumber) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_dynamic_password WHERE phoneNumber = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        DynamicPassword dynamicPassword = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, phoneNumber);
            rs = ps.executeQuery();
            if (rs.next()) {
                dynamicPassword = new DynamicPassword();
                dynamicPassword.setPhoneNumber(rs.getString("phoneNumber"));
                dynamicPassword.setDynamicPassword(rs
                        .getString("dynamicPassword"));
                dynamicPassword
                        .setPasswordTime(rs.getTimestamp("passwordTime"));
            }
            return dynamicPassword;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public DynamicPassword findDynamicPasswordByDynamicPassword(
            String dynamicPassword) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_dynamic_password WHERE dynamicPassword = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        DynamicPassword dynamicPasswordNew = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, dynamicPassword);
            rs = ps.executeQuery();
            if (rs.next()) {
                dynamicPasswordNew = new DynamicPassword();
                dynamicPasswordNew.setPhoneNumber(rs.getString("phoneNumber"));
                dynamicPasswordNew.setDynamicPassword(rs
                        .getString("dynamicPassword"));
                dynamicPasswordNew.setPasswordTime(rs
                        .getTimestamp("passwordTime"));
            }
            return dynamicPasswordNew;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public boolean deleteDynamicPasswordByPhoneNumber(String phoneNumber) {
        boolean deleteFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "DELETE FROM t_dynamic_password WHERE phoneNumber = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, phoneNumber);
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

}
