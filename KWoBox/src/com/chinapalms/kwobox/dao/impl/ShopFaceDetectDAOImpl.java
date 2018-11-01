package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.ShopFaceDetectDAO;
import com.chinapalms.kwobox.javabean.BoxShopes;
import com.chinapalms.kwobox.javabean.ShopFaceDetect;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class ShopFaceDetectDAOImpl implements ShopFaceDetectDAO {

    Log log = LogFactory.getLog(ShopFaceDetectDAOImpl.class);

    @Override
    public ShopFaceDetect findShopFaceDetectByShopIdAndPhoneNumber(int shopId,
            String phoneNumber) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_shop_facedetect WHERE shopId = ? AND phoneNumber = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        ShopFaceDetect shopFaceDetect = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, shopId);
            ps.setString(2, phoneNumber);
            rs = ps.executeQuery();
            if (rs.next()) {
                shopFaceDetect = new ShopFaceDetect();
                shopFaceDetect.setShopId(rs.getInt("shopId"));
                shopFaceDetect.setPhoneNumber(rs.getString("phoneNumber"));
                shopFaceDetect.setState(rs.getInt("state"));
                shopFaceDetect.setLastFaceDetectShoppingTime(rs
                        .getTimestamp("lastFaceDetectShoppingTime"));
            }
            return shopFaceDetect;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public boolean addShopFaceDetect(ShopFaceDetect shopFaceDetect) {
        boolean addFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "INSERT INTO t_shop_facedetect(shopId, phoneNumber, state, lastFaceDetectShoppingTime) VALUES(?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, shopFaceDetect.getShopId());
            ps.setString(2, shopFaceDetect.getPhoneNumber());
            ps.setInt(3, shopFaceDetect.getState());
            ps.setTimestamp(4, new Timestamp(shopFaceDetect
                    .getLastFaceDetectShoppingTime().getTime()));
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
    public boolean updateShopFaceDetectState(ShopFaceDetect shopFaceDetect) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_shop_facedetect SET state = ? WHERE phoneNumber = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, shopFaceDetect.getState());
            ps.setString(2, shopFaceDetect.getPhoneNumber());
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
    public boolean updateLastShopFaceDetectShoppingTime(
            ShopFaceDetect shopFaceDetect) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_shop_facedetect SET lastFaceDetectShoppingTime = ? WHERE phoneNumber = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setTimestamp(1, new Timestamp(shopFaceDetect
                    .getLastFaceDetectShoppingTime().getTime()));
            ps.setString(2, shopFaceDetect.getPhoneNumber());
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
