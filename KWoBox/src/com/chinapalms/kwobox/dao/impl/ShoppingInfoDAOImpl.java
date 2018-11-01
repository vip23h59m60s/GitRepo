package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.ShoppingInfoDAO;
import com.chinapalms.kwobox.javabean.PersonalCreditRecord;
import com.chinapalms.kwobox.javabean.ShoppingInfo;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class ShoppingInfoDAOImpl implements ShoppingInfoDAO {

    Log log = LogFactory.getLog(ShoppingInfo.class);

    @Override
    public boolean addShoppingInfo(ShoppingInfo shoppingInfo) {
        boolean addFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "INSERT INTO t_shopping_info(shoppingInfoId, orderId, customerId, boxId, phoneNumber, requestSerialNumber, shoppingInfo, videoUrlInfo, referScore, resolveUser, state, openTime, closeTime) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, shoppingInfo.getShoppingInfoId());
            ps.setString(2, shoppingInfo.getOrderId());
            ps.setInt(3, shoppingInfo.getCustomerId());
            ps.setString(4, shoppingInfo.getBoxId());
            ps.setString(5, shoppingInfo.getPhoneNumber());
            ps.setString(6, shoppingInfo.getRequestSerialNumber());
            ps.setString(7, shoppingInfo.getShoppingInfo());
            ps.setString(8, shoppingInfo.getVideoUrlInfo());
            ps.setInt(9, shoppingInfo.getReferScore());
            ps.setString(10, shoppingInfo.getResolveUser());
            ps.setInt(11, shoppingInfo.getState());
            ps.setTimestamp(12, new Timestamp(shoppingInfo.getOpenTime()
                    .getTime()));
            ps.setTimestamp(13, new Timestamp(shoppingInfo.getCloseTime()
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
    public ShoppingInfo findShoppingInfoByShoppingInfoId(String shoppingInfoId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_shopping_info WHERE shoppingInfoId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        ShoppingInfo shoppingInfo = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, shoppingInfoId);
            rs = ps.executeQuery();
            if (rs.next()) {
                shoppingInfo = new ShoppingInfo();
                shoppingInfo.setShoppingInfoId(rs.getString("shoppingInfoId"));
                shoppingInfo.setOrderId(rs.getString("orderId"));
                shoppingInfo.setCustomerId(rs.getInt("customerId"));
                shoppingInfo.setBoxId(rs.getString("boxId"));
                shoppingInfo.setPhoneNumber(rs.getString("phoneNumber"));
                shoppingInfo.setRequestSerialNumber(rs
                        .getString("requestSerialNumber"));
                shoppingInfo.setShoppingInfo(rs.getString("shoppingInfo"));
                shoppingInfo.setVideoUrlInfo(rs.getString("videoUrlInfo"));
                shoppingInfo.setReferScore(rs.getInt("referScore"));
                shoppingInfo.setResolveUser(rs.getString("resolveUser"));
                shoppingInfo.setState(rs.getInt("state"));
                shoppingInfo.setOpenTime(rs.getTimestamp("openTime"));
                shoppingInfo.setCloseTime(rs.getTimestamp("closeTime"));
            }
            return shoppingInfo;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public int findShoppInfoCountByBoxIdAndState(String boxId, int state,
            String startTime, String endTime) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT COUNT(*) AS shoppingInfoCount FROM t_shopping_info WHERE boxId = ? AND state = ? AND openTime >= ? AND openTime <= ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        int orderCount = 0;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, boxId);
            ps.setInt(2, state);
            ps.setString(3, startTime);
            ps.setString(4, endTime);
            rs = ps.executeQuery();
            if (rs.next()) {
                orderCount = rs.getInt("shoppingInfoCount");
            }
            return orderCount;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public List<ShoppingInfo> findShoppingInfosByBoxIdAndStateAndPageNumber(
            String boxId, int state, String startTime, String endTime,
            int pageNumber, int pageSize) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_shopping_info WHERE boxId = ? AND state = ? AND openTime >= ? AND openTime <= ? ORDER BY openTime ASC LIMIT ? , ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ShoppingInfo> shoppingInfosList = new ArrayList<ShoppingInfo>();
        ShoppingInfo shoppingInfo = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, boxId);
            ps.setInt(2, state);
            ps.setString(3, startTime);
            ps.setString(4, endTime);
            ps.setInt(5, (pageNumber - 1) * pageSize);
            ps.setInt(6, pageSize);
            rs = ps.executeQuery();
            while (rs.next()) {
                shoppingInfo = new ShoppingInfo();
                shoppingInfo.setShoppingInfoId(rs.getString("shoppingInfoId"));
                shoppingInfo.setOrderId(rs.getString("orderId"));
                shoppingInfo.setCustomerId(rs.getInt("customerId"));
                shoppingInfo.setBoxId(rs.getString("boxId"));
                shoppingInfo.setPhoneNumber(rs.getString("phoneNumber"));
                shoppingInfo.setRequestSerialNumber(rs
                        .getString("requestSerialNumber"));
                shoppingInfo.setShoppingInfo(rs.getString("shoppingInfo"));
                shoppingInfo.setVideoUrlInfo(rs.getString("videoUrlInfo"));
                shoppingInfo.setReferScore(rs.getInt("referScore"));
                shoppingInfo.setResolveUser(rs.getString("resolveUser"));
                shoppingInfo.setState(rs.getInt("state"));
                shoppingInfo.setOpenTime(rs.getTimestamp("openTime"));
                shoppingInfo.setCloseTime(rs.getTimestamp("closeTime"));
                shoppingInfosList.add(shoppingInfo);
            }
            return shoppingInfosList;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public ShoppingInfo findShoppingInfoByOrderId(String orderId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_shopping_info WHERE orderId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        ShoppingInfo shoppingInfo = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, orderId);
            rs = ps.executeQuery();
            if (rs.next()) {
                shoppingInfo = new ShoppingInfo();
                shoppingInfo.setShoppingInfoId(rs.getString("shoppingInfoId"));
                shoppingInfo.setOrderId(rs.getString("orderId"));
                shoppingInfo.setCustomerId(rs.getInt("customerId"));
                shoppingInfo.setBoxId(rs.getString("boxId"));
                shoppingInfo.setPhoneNumber(rs.getString("phoneNumber"));
                shoppingInfo.setRequestSerialNumber(rs
                        .getString("requestSerialNumber"));
                shoppingInfo.setShoppingInfo(rs.getString("shoppingInfo"));
                shoppingInfo.setVideoUrlInfo(rs.getString("videoUrlInfo"));
                shoppingInfo.setReferScore(rs.getInt("referScore"));
                shoppingInfo.setResolveUser(rs.getString("resolveUser"));
                shoppingInfo.setState(rs.getInt("state"));
                shoppingInfo.setOpenTime(rs.getTimestamp("openTime"));
                shoppingInfo.setCloseTime(rs.getTimestamp("closeTime"));
            }
            return shoppingInfo;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public boolean updateShoppingInfoState(ShoppingInfo shoppingInfo) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_shopping_info SET resolveUser = ?, state = ?, closeTime = ? WHERE shoppingInfoId = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, shoppingInfo.getResolveUser());
            ps.setInt(2, shoppingInfo.getState());
            ps.setTimestamp(3, new Timestamp(shoppingInfo.getCloseTime()
                    .getTime()));
            ps.setString(4, shoppingInfo.getShoppingInfoId());
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
    public boolean updateShoppingInfoVideosUrl(ShoppingInfo shoppingInfo) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_shopping_info SET videoUrlInfo = ? WHERE shoppingInfoId = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, shoppingInfo.getVideoUrlInfo());
            ps.setString(2, shoppingInfo.getShoppingInfoId());
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
    public boolean updateShoppingInfoOrderId(ShoppingInfo shoppingInfo) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_shopping_info SET orderId = ? WHERE shoppingInfoId = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, shoppingInfo.getOrderId());
            ps.setString(2, shoppingInfo.getShoppingInfoId());
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
    public List<ShoppingInfo> findShoppInfoByBoxIdAndState(String boxId,
            int state) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_shopping_info WHERE boxId = ? AND state = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ShoppingInfo> shoppingInfosList = new ArrayList<ShoppingInfo>();
        ShoppingInfo shoppingInfo = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, boxId);
            ps.setInt(2, state);
            rs = ps.executeQuery();
            while (rs.next()) {
                shoppingInfo = new ShoppingInfo();
                shoppingInfo.setShoppingInfoId(rs.getString("shoppingInfoId"));
                shoppingInfo.setOrderId(rs.getString("orderId"));
                shoppingInfo.setCustomerId(rs.getInt("customerId"));
                shoppingInfo.setBoxId(rs.getString("boxId"));
                shoppingInfo.setPhoneNumber(rs.getString("phoneNumber"));
                shoppingInfo.setRequestSerialNumber(rs
                        .getString("requestSerialNumber"));
                shoppingInfo.setShoppingInfo(rs.getString("shoppingInfo"));
                shoppingInfo.setVideoUrlInfo(rs.getString("videoUrlInfo"));
                shoppingInfo.setReferScore(rs.getInt("referScore"));
                shoppingInfo.setResolveUser(rs.getString("resolveUser"));
                shoppingInfo.setState(rs.getInt("state"));
                shoppingInfo.setOpenTime(rs.getTimestamp("openTime"));
                shoppingInfo.setCloseTime(rs.getTimestamp("closeTime"));
                shoppingInfosList.add(shoppingInfo);
            }
            return shoppingInfosList;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public List<ShoppingInfo> findShoppingInfosByCustomerIdAndPhoneNumberAndState(
            int customerId, String phoneNumber, int state) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_shopping_info WHERE customerId = ? AND phoneNumber = ? AND state <> ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ShoppingInfo> shoppingInfosList = new ArrayList<ShoppingInfo>();
        ShoppingInfo shoppingInfo = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, customerId);
            ps.setString(2, phoneNumber);
            ps.setInt(3, state);
            rs = ps.executeQuery();
            while (rs.next()) {
                shoppingInfo = new ShoppingInfo();
                shoppingInfo.setShoppingInfoId(rs.getString("shoppingInfoId"));
                shoppingInfo.setOrderId(rs.getString("orderId"));
                shoppingInfo.setCustomerId(rs.getInt("customerId"));
                shoppingInfo.setBoxId(rs.getString("boxId"));
                shoppingInfo.setPhoneNumber(rs.getString("phoneNumber"));
                shoppingInfo.setRequestSerialNumber(rs
                        .getString("requestSerialNumber"));
                shoppingInfo.setShoppingInfo(rs.getString("shoppingInfo"));
                shoppingInfo.setVideoUrlInfo(rs.getString("videoUrlInfo"));
                shoppingInfo.setReferScore(rs.getInt("referScore"));
                shoppingInfo.setResolveUser(rs.getString("resolveUser"));
                shoppingInfo.setState(rs.getInt("state"));
                shoppingInfo.setOpenTime(rs.getTimestamp("openTime"));
                shoppingInfo.setCloseTime(rs.getTimestamp("closeTime"));
                shoppingInfosList.add(shoppingInfo);
            }
            return shoppingInfosList;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public int findShoppingInfosBoxesCountByCustomerIdAndState(int customerId,
            int state) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT COUNT(DISTINCT(boxId)) AS boxCount FROM t_shopping_info WHERE customerId = ? AND state <> ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        int boxCount = 0;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, customerId);
            ps.setInt(2, state);
            rs = ps.executeQuery();
            if (rs.next()) {
                boxCount = rs.getInt("boxCount");
            }
            return boxCount;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public int findShoppingInfosCountByBoxIdAndState(String boxId, int state) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT COUNT(*) AS shoppingInfoCount FROM t_shopping_info WHERE boxId = ? AND state <> ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        int shoppingInfoCount = 0;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, boxId);
            ps.setInt(2, state);
            rs = ps.executeQuery();
            if (rs.next()) {
                shoppingInfoCount = rs.getInt("shoppingInfoCount");
            }
            return shoppingInfoCount;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

}
