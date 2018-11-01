package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.IdentifyCodeDAO;
import com.chinapalms.kwobox.javabean.IdentifyCode;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class IdentifyCodeDAOImpl implements IdentifyCodeDAO {

    Log log = LogFactory.getLog(IdentifyCodeDAOImpl.class);

    @Override
    public boolean addIdentifyCode(IdentifyCode idendifyCode) {
        boolean addFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "INSERT INTO t_identify_code(phoneNumber, identifyCode, time) VALUES(?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, idendifyCode.getPhoneNumber());
            ps.setString(2, idendifyCode.getIdentifyCode());
            ps.setTimestamp(3, new Timestamp(new Date().getTime()));
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
    public boolean deleteIdentifyCodeByPhoneNumber(String phoneNumber) {
        boolean deleteFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "DELETE FROM t_identify_code WHERE phoneNumber = ?";
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

    @Override
    public boolean updateIdentifyCode(IdentifyCode idendifyCode) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_identify_code SET identifyCode = ?, time = ? WHERE phoneNumber = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, idendifyCode.getIdentifyCode());
            ps.setTimestamp(2, new Timestamp(new Date().getTime()));
            ps.setString(3, idendifyCode.getPhoneNumber());
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
    public IdentifyCode findIdentifyCodeByPhoneNumber(String phoneNumber) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_identify_code WHERE phoneNumber = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        IdentifyCode identifyCode = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, phoneNumber);
            rs = ps.executeQuery();
            if (rs.next()) {
                identifyCode = new IdentifyCode();
                identifyCode.setPhoneNumber(rs.getString("phoneNumber"));
                identifyCode.setIdentifyCode(rs.getString("identifyCode"));
                identifyCode.setTime(rs.getTimestamp("time"));
            }
            return identifyCode;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public IdentifyCode findIdentifyCodeByPhoneNumberAndIdentifyCode(
            String phoneNumber, String identifyCode) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_identify_code WHERE phoneNumber = ? AND identifyCode = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        IdentifyCode identifyCodeQuery = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, phoneNumber);
            ps.setString(2, identifyCode);
            rs = ps.executeQuery();
            if (rs.next()) {
                identifyCodeQuery = new IdentifyCode();
                identifyCodeQuery.setPhoneNumber(rs.getString("phoneNumber"));
                identifyCodeQuery.setIdentifyCode(rs.getString("identifyCode"));
                identifyCodeQuery.setTime(rs.getTimestamp("time"));
            }
            return identifyCodeQuery;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

}
