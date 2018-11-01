package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.OrderCallbackDAO;
import com.chinapalms.kwobox.javabean.DoorOpenedCallback;
import com.chinapalms.kwobox.javabean.OrderCallback;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class OrderCallbackDAOImpl implements OrderCallbackDAO {

    Log log = LogFactory.getLog(OrderCallbackDAOImpl.class);

    @Override
    public OrderCallback findOrderCallbackByCustomerId(int customerId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_order_callback WHERE customerId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        OrderCallback orderCallback = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, customerId);
            rs = ps.executeQuery();
            if (rs.next()) {
                orderCallback = new OrderCallback();
                orderCallback.setCustomerId(rs.getInt("customerId"));
                orderCallback.setOrderCallbackUrl(rs
                        .getString("orderCallbackUrl"));
            }
            return orderCallback;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public boolean updateOrderCallback(OrderCallback orderCallback) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_order_callback SET orderCallbackUrl = ? WHERE customerId = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, orderCallback.getOrderCallbackUrl());
            ps.setInt(2, orderCallback.getCustomerId());
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
