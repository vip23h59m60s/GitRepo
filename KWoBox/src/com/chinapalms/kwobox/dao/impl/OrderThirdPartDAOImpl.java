package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.OrderThirdPartDAO;
import com.chinapalms.kwobox.javabean.Order;
import com.chinapalms.kwobox.javabean.OrderThirdPart;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class OrderThirdPartDAOImpl implements OrderThirdPartDAO {

    Log log = LogFactory.getLog(OrderDAOImpl.class);

    @Override
    public boolean addOrderThirdPart(OrderThirdPart order) {
        boolean addFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "INSERT INTO t_orders_thirdpart(orderId, phoneNumber, vipLevel, boxId, goodsTotalNumber, payTotal, totalFavourable, actualPayTotal, payType, thirdPayOrderId, outTradeNo, transactionId, payState, payDate, buyDate) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, order.getOrderId());
            ps.setString(2, order.getPhoneNumber());
            ps.setInt(3, order.getVipLevel());
            ps.setString(4, order.getBoxId());
            ps.setInt(5, order.getGoodsTotalNumber());
            ps.setBigDecimal(6, order.getPayTotal());
            ps.setBigDecimal(7, order.getTotalFavourable());
            ps.setBigDecimal(8, order.getActualPayTotal());
            ps.setString(9, order.getPayType());
            ps.setString(10, order.getThirdPayOrderId());
            ps.setString(11, order.getOutTradeNo());
            ps.setString(12, order.getTransactionId());
            ps.setInt(13, order.getPayState());
            ps.setTimestamp(14, new Timestamp(order.getPayDate().getTime()));
            ps.setTimestamp(15, new Timestamp(order.getBuyDate().getTime()));
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
    public OrderThirdPart findOrderThirdPartByOrderId(String orderId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_orders_thirdpart WHERE orderId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        OrderThirdPart order = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, orderId);
            rs = ps.executeQuery();
            if (rs.next()) {
                order = new OrderThirdPart();
                order.setOrderId(rs.getString("orderId"));
                order.setPhoneNumber(rs.getString("phoneNumber"));
                order.setVipLevel(rs.getInt("vipLevel"));
                order.setBoxId(rs.getString("boxId"));
                order.setGoodsTotalNumber(rs.getInt("goodsTotalNumber"));
                order.setPayTotal(rs.getBigDecimal("payTotal"));
                order.setTotalFavourable(rs.getBigDecimal("totalFavourable"));
                order.setActualPayTotal(rs.getBigDecimal("actualPayTotal"));
                order.setPayType(rs.getString("payType"));
                order.setThirdPayOrderId(rs.getString("thirdPayOrderId"));
                order.setOutTradeNo(rs.getString("outTradeNo"));
                order.setTransactionId(rs.getString("transactionId"));
                order.setPayState(rs.getInt("payState"));
                order.setPayDate(rs.getTimestamp("payDate"));
                order.setBuyDate(rs.getTimestamp("buyDate"));
            }
            return order;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public boolean updateThirdPartOrder(OrderThirdPart orderThirdPart) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_orders_thirdpart SET payState = ? WHERE orderId = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, orderThirdPart.getPayState());
            ps.setString(2, orderThirdPart.getOrderId());
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
