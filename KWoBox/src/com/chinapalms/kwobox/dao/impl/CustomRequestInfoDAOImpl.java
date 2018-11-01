package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.CustomRequestInfoDAO;
import com.chinapalms.kwobox.javabean.CurrentUser;
import com.chinapalms.kwobox.javabean.CustomRequestInfo;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class CustomRequestInfoDAOImpl implements CustomRequestInfoDAO {

    Log log = LogFactory.getLog(CustomRequestInfoDAOImpl.class);

    @Override
    public boolean addCustomRequestInfo(CustomRequestInfo customRequestInfo) {
        boolean addFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "INSERT INTO t_custom_request_info(boxId, openDoorRequestSerial) VALUES(?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, customRequestInfo.getBoxId());
            ps.setString(2, customRequestInfo.getOpenDoorRequestSerial());
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
    public boolean updateCustomRequestInfo(CustomRequestInfo customRequestInfo) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_custom_request_info SET boxId = ?, openDoorRequestSerial = ? WHERE boxId = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, customRequestInfo.getBoxId());
            ps.setString(2, customRequestInfo.getOpenDoorRequestSerial());
            ps.setString(3, customRequestInfo.getBoxId());
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
    public CustomRequestInfo findCustomRequestInfoByBoxId(String boxId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_custom_request_info WHERE boxId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        CustomRequestInfo customRequestInfo = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, boxId);
            rs = ps.executeQuery();
            if (rs.next()) {
                customRequestInfo = new CustomRequestInfo();
                customRequestInfo.setBoxId(rs.getString("boxId"));
                customRequestInfo.setOpenDoorRequestSerial(rs
                        .getString("openDoorRequestSerial"));
            }
            return customRequestInfo;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

}
