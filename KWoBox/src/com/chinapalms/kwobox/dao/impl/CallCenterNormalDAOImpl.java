package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.CallCenterNormalDAO;
import com.chinapalms.kwobox.javabean.Boxes;
import com.chinapalms.kwobox.javabean.CallCenterNormal;
import com.chinapalms.kwobox.utils.JDBCUtil;
import com.chinapalms.kwobox.utils.ResponseStatus;

public class CallCenterNormalDAOImpl implements CallCenterNormalDAO {

    Log log = LogFactory.getLog(CallCenterNormalDAOImpl.class);

    @Override
    public CallCenterNormal findCallCenterNormalByUserNameAndPassword(
            String userName, String password) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_callcenter_normal WHERE userName = ? AND password = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        CallCenterNormal callCenterNormal = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, userName);
            ps.setString(2, password);
            rs = ps.executeQuery();
            if (rs.next()) {
                callCenterNormal = new CallCenterNormal();
                callCenterNormal.setCallCenterId(rs.getInt("callCenterId"));
                callCenterNormal.setIdentityCardNumber(rs
                        .getString("identityCardNumber"));
                callCenterNormal.setName(rs.getString("name"));
                callCenterNormal.setPhoneNumber(rs.getString("phoneNumber"));
                callCenterNormal.setUserName(rs.getString("userName"));
                callCenterNormal.setPassword(rs.getString("password"));
                callCenterNormal.setEmployeeNumber(rs
                        .getString("employeeNumber"));
                callCenterNormal.setEmail(rs.getString("email"));
                callCenterNormal.setSuperAdminId(rs.getInt("superAdminId"));
                callCenterNormal.setWorkState(rs.getInt("workState"));
                callCenterNormal.setRegisterTime(rs
                        .getTimestamp("registerTime"));
            }
            return callCenterNormal;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public List<CallCenterNormal> findCallCenterNormalsByWorkState(int workState) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_callcenter_normal WHERE workState = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<CallCenterNormal> callCenterNormalsList = new ArrayList<CallCenterNormal>();
        CallCenterNormal callCenterNormal = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, workState);
            rs = ps.executeQuery();
            while (rs.next()) {
                callCenterNormal = new CallCenterNormal();
                callCenterNormal = new CallCenterNormal();
                callCenterNormal.setCallCenterId(rs.getInt("callCenterId"));
                callCenterNormal.setIdentityCardNumber(rs
                        .getString("identityCardNumber"));
                callCenterNormal.setName(rs.getString("name"));
                callCenterNormal.setPhoneNumber(rs.getString("phoneNumber"));
                callCenterNormal.setUserName(rs.getString("userName"));
                callCenterNormal.setPassword(rs.getString("password"));
                callCenterNormal.setEmployeeNumber(rs
                        .getString("employeeNumber"));
                callCenterNormal.setEmail(rs.getString("email"));
                callCenterNormal.setSuperAdminId(rs.getInt("superAdminId"));
                callCenterNormal.setWorkState(rs.getInt("workState"));
                callCenterNormal.setRegisterTime(rs
                        .getTimestamp("registerTime"));
                callCenterNormalsList.add(callCenterNormal);
            }
            return callCenterNormalsList;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    /**
     * 异常视频邮件发送人workState=2
     * 
     * @return
     */
    public List<CallCenterNormal> findEmailReceiver() {
        return findCallCenterNormalsByWorkState(2);
    }

    /**
     * 异常视频邮件抄送人workState=1
     * 
     * @return
     */
    public List<CallCenterNormal> findEmailCopyReceiver() {
        return findCallCenterNormalsByWorkState(1);
    }

}
