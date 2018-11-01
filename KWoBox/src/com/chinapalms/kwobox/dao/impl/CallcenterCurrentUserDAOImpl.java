package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.CallcenterCurrentUserDAO;
import com.chinapalms.kwobox.javabean.CallcenterCurrentUser;
import com.chinapalms.kwobox.javabean.IdentifyCode;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class CallcenterCurrentUserDAOImpl implements CallcenterCurrentUserDAO {

    Log log = LogFactory.getLog(CallcenterCurrentUserDAOImpl.class);

    @Override
    public boolean addCallCenterCurrentUser(
            CallcenterCurrentUser callcenterCurrentUser) {
        boolean addFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "INSERT INTO t_callcenter_current_user(boxId, callCenterId) VALUES(?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, callcenterCurrentUser.getBoxId());
            ps.setInt(2, callcenterCurrentUser.getCallCenterId());
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
    public CallcenterCurrentUser findCallCenterCurrentUserByBoxId(String boxId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_callcenter_current_user WHERE boxId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        CallcenterCurrentUser callcenterCurrentUser = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, boxId);
            rs = ps.executeQuery();
            if (rs.next()) {
                callcenterCurrentUser = new CallcenterCurrentUser();
                callcenterCurrentUser.setBoxId(rs.getString("boxId"));
                callcenterCurrentUser
                        .setCallCenterId(rs.getInt("callCenterId"));
                ;
            }
            return callcenterCurrentUser;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public boolean updateCallCenterCurrentUser(
            CallcenterCurrentUser callcenterCurrentUser) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_callcenter_current_user SET boxId = ?, callCenterId = ? WHERE boxId = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, callcenterCurrentUser.getBoxId());
            ps.setInt(2, callcenterCurrentUser.getCallCenterId());
            ps.setString(3, callcenterCurrentUser.getBoxId());
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
