package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.ICSDAO;
import com.chinapalms.kwobox.javabean.ICS;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class ICSDAOImpl implements ICSDAO {

    Log log = LogFactory.getLog(ICSDAOImpl.class);

    @Override
    public ICS findICSByIMEINumber(String imeiNumber) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_ics WHERE imeiNumber = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        ICS ics = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, imeiNumber);
            rs = ps.executeQuery();
            if (rs.next()) {
                ics = new ICS();
                ics.setIcsId(rs.getInt("icsId"));
                ics.setModel(rs.getString("model"));
                ics.setSimCardNumber(rs.getString("simCardNumber"));
                ics.setWifiMacAddress(rs.getString("wifiMacAddress"));
                ics.setSnNumber(rs.getString("snNumber"));
                ics.setImeiNumber(rs.getString("imeiNumber"));
                ics.setFotaVersion(rs.getString("fotaVersion"));
                ics.setLingMaoVersion(rs.getString("lingMaoVersion"));
                ics.setLingMaoCoreVersion(rs.getString("lingMaoCoreVersion"));
                ics.setLingMaoToolsVersion(rs.getString("lingMaoToolsVersion"));
                ics.setAdvVersion(rs.getString("advVersion"));
                ics.setDebugStatus(rs.getInt("debugStatus"));
                ics.setLogLevel(rs.getString("logLevel"));
                ics.setCheckSelf(rs.getInt("checkSelf"));
                ics.setDeviceManagerId(rs.getInt("deviceManagerId"));
                ics.setReleaseTime(rs.getTimestamp("releaseTime"));
            }

            return ics;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public boolean addICS(ICS ics) {
        boolean addFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "INSERT INTO t_ics(imeiNumber, releaseTime) VALUES(?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, ics.getImeiNumber());
            ps.setTimestamp(2, new Timestamp(ics.getReleaseTime().getTime()));
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
    public boolean updateICS(ICS ics) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_ics SET fotaVersion = ?, lingMaoVersion = ?, lingMaoCoreVersion = ?, lingMaoToolsVersion = ?, advVersion = ?, debugStatus = ?, logLevel = ?, checkSelf = ? WHERE icsId = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, ics.getFotaVersion());
            ps.setString(2, ics.getLingMaoVersion());
            ps.setString(3, ics.getLingMaoCoreVersion());
            ps.setString(4, ics.getLingMaoToolsVersion());
            ps.setString(5, ics.getAdvVersion());
            ps.setInt(6, ics.getDebugStatus());
            ps.setString(7, ics.getLogLevel());
            ps.setInt(8, ics.getCheckSelf());
            ps.setInt(9, ics.getIcsId());
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
