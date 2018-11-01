package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.OrderCallbackCustomizationDAO;
import com.chinapalms.kwobox.javabean.DoorOpenedCallbackCustomization;
import com.chinapalms.kwobox.javabean.OrderCallbackCustomization;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class OrderCallbackCustomizationDAOImpl implements
        OrderCallbackCustomizationDAO {

    Log log = LogFactory.getLog(OrderCallbackCustomizationDAOImpl.class);

    @Override
    public boolean addOrderCallbackCustomization(
            OrderCallbackCustomization orderCallbackCustomization) {
        boolean addFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "INSERT INTO t_order_callback_customization(customerId, boxId, orderCallbackUrl) VALUES(?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, orderCallbackCustomization.getCustomerId());
            ps.setString(2, orderCallbackCustomization.getBoxId());
            ps.setString(3, orderCallbackCustomization.getOrderCallbackUrl());
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
    public boolean updateOrderCallbackCustomization(
            OrderCallbackCustomization orderCallbackCustomization) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_order_callback_customization SET orderCallbackUrl = ? WHERE customerId = ? AND boxId = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, orderCallbackCustomization.getOrderCallbackUrl());
            ps.setInt(2, orderCallbackCustomization.getCustomerId());
            ps.setString(3, orderCallbackCustomization.getBoxId());
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
    public OrderCallbackCustomization findOrderCallbackCustomizationByCustomerIdAndBoxId(
            int customerId, String boxId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_order_callback_customization WHERE customerId = ? AND boxId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        OrderCallbackCustomization orderCallbackCustomization = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, customerId);
            ps.setString(2, boxId);
            rs = ps.executeQuery();
            if (rs.next()) {
                orderCallbackCustomization = new OrderCallbackCustomization();
                orderCallbackCustomization.setCustomerId(rs
                        .getInt("customerId"));
                orderCallbackCustomization.setBoxId(rs.getString("boxId"));
                orderCallbackCustomization.setOrderCallbackUrl(rs
                        .getString("orderCallbackUrl"));
            }
            return orderCallbackCustomization;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

}
