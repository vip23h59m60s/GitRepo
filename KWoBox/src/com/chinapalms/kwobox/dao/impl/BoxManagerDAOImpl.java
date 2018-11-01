package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.BoxManagerDAO;
import com.chinapalms.kwobox.javabean.BoxManager;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class BoxManagerDAOImpl implements BoxManagerDAO {

    Log log = LogFactory.getLog(BoxManagerDAOImpl.class);

    @Override
    public List<BoxManager> findBoxManagerByBoxDeliveryId(int boxDeliveryId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_box_manager WHERE boxDeliveryId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<BoxManager> boxManagerList = new ArrayList<BoxManager>();
        BoxManager boxManager = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, boxDeliveryId);
            rs = ps.executeQuery();
            while (rs.next()) {
                boxManager = new BoxManager();
                boxManager.setBoxId(rs.getString("boxId"));
                boxManager.setBoxManagerId(rs.getInt("boxManagerId"));
                boxManager.setBoxDeliveryId(rs.getInt("boxDeliveryId"));
                boxManager.setMaintainerId(rs.getInt("maintainerId"));
                boxManager.setDataManagerId(rs.getInt("dataManagerId"));
                boxManager.setCustomManagerId(rs.getInt("customManagerId"));
                boxManager.setUpdateDateTime(rs.getTimestamp("updateDateTime"));
                boxManagerList.add(boxManager);
            }
            return boxManagerList;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public List<BoxManager> findBoxManagerByBoxDeliveryIdAndPageNumber(
            int boxDeliveryId, int pageNumber) {
        Connection connection = JDBCUtil.getDBConnection();
        // 上面的SQL语句当updateDateTime相同时会出现诡异情况，属于LIMIT和ORDER By同时使用的坑
        // String sql =
        // "SELECT * FROM t_box_manager WHERE boxDeliveryId = ? ORDER BY updateDateTime DESC LIMIT ? , ?";
        String sql = "SELECT * FROM t_box_manager WHERE boxDeliveryId = ? LIMIT ? , ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<BoxManager> boxManagerList = new ArrayList<BoxManager>();
        BoxManager boxManager = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, boxDeliveryId);
            ps.setInt(2, (pageNumber - 1) * BoxManager.PAGE_SIZE);
            ps.setInt(3, BoxManager.PAGE_SIZE);
            rs = ps.executeQuery();
            while (rs.next()) {
                boxManager = new BoxManager();
                boxManager.setBoxId(rs.getString("boxId"));
                boxManager.setBoxManagerId(rs.getInt("boxManagerId"));
                boxManager.setBoxDeliveryId(rs.getInt("boxDeliveryId"));
                boxManager.setMaintainerId(rs.getInt("maintainerId"));
                boxManager.setDataManagerId(rs.getInt("dataManagerId"));
                boxManager.setCustomManagerId(rs.getInt("customManagerId"));
                boxManager.setUpdateDateTime(rs.getTimestamp("updateDateTime"));
                boxManagerList.add(boxManager);
            }
            return boxManagerList;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public int findBoxManagerCountByBoxDeliveryId(int boxDeliveryId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT COUNT(*) AS boxManagerCount FROM t_box_manager WHERE boxDeliveryId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        int boxManagerCount = 0;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, boxDeliveryId);
            rs = ps.executeQuery();
            if (rs.next()) {
                boxManagerCount = rs.getInt("boxManagerCount");
            }
            return boxManagerCount;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public List<BoxManager> findBoxManagersByBoxId(String boxId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_box_manager WHERE boxId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<BoxManager> boxManagerList = new ArrayList<BoxManager>();
        BoxManager boxManager = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, boxId);
            rs = ps.executeQuery();
            while (rs.next()) {
                boxManager = new BoxManager();
                boxManager.setBoxId(rs.getString("boxId"));
                boxManager.setBoxManagerId(rs.getInt("boxManagerId"));
                boxManager.setBoxDeliveryId(rs.getInt("boxDeliveryId"));
                boxManager.setMaintainerId(rs.getInt("maintainerId"));
                boxManager.setDataManagerId(rs.getInt("dataManagerId"));
                boxManager.setCustomManagerId(rs.getInt("customManagerId"));
                boxManager.setUpdateDateTime(rs.getTimestamp("updateDateTime"));
                boxManagerList.add(boxManager);
            }
            return boxManagerList;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public BoxManager findBoxManagerByBoxIdAndDeliveryId(String boxId,
            int boxDeliveryId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_box_manager WHERE boxId = ? AND boxDeliveryId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        BoxManager boxManager = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, boxId);
            ps.setInt(2, boxDeliveryId);
            rs = ps.executeQuery();
            if (rs.next()) {
                boxManager = new BoxManager();
                boxManager.setBoxId(rs.getString("boxId"));
                boxManager.setBoxManagerId(rs.getInt("boxManagerId"));
                boxManager.setBoxDeliveryId(rs.getInt("boxDeliveryId"));
                boxManager.setMaintainerId(rs.getInt("maintainerId"));
                boxManager.setDataManagerId(rs.getInt("dataManagerId"));
                boxManager.setCustomManagerId(rs.getInt("customManagerId"));
                boxManager.setUpdateDateTime(rs.getTimestamp("updateDateTime"));
            }
            return boxManager;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

}
