package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.BoxStockSyncStatusDAO;
import com.chinapalms.kwobox.javabean.BoxStatus;
import com.chinapalms.kwobox.javabean.BoxStockSyncStatus;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class BoxStockSyncStatusDAOImpl implements BoxStockSyncStatusDAO {

    Log log = LogFactory.getLog(BoxStockSyncStatusDAOImpl.class);

    @Override
    public boolean addBoxStockSyncStatus(BoxStockSyncStatus boxStockSyncStatus) {
        boolean addFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "INSERT INTO t_box_stock_sync_status(boxId, state) VALUES(?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, boxStockSyncStatus.getBoxId());
            ps.setInt(2, boxStockSyncStatus.getState());
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
    public boolean updateBoxStockSyncStatus(
            BoxStockSyncStatus boxStockSyncStatus) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_box_stock_sync_status SET state = ? WHERE boxId = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, boxStockSyncStatus.getState());
            ps.setString(2, boxStockSyncStatus.getBoxId());
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
    public BoxStockSyncStatus findBoxStockSyncStatusByBoxId(String boxId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_box_stock_sync_status WHERE boxId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        BoxStockSyncStatus boxStockSyncStatus = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, boxId);
            rs = ps.executeQuery();
            if (rs.next()) {
                boxStockSyncStatus = new BoxStockSyncStatus();
                boxStockSyncStatus.setBoxId(rs.getString("boxId"));
                boxStockSyncStatus.setState(rs.getInt("state"));
            }
            return boxStockSyncStatus;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

}
