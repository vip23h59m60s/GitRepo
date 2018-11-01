package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.OrderDAO;
import com.chinapalms.kwobox.javabean.Order;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class OrderDAOImpl implements OrderDAO {

    Log log = LogFactory.getLog(OrderDAOImpl.class);

    @Override
    public boolean addOrder(Order order) {
        boolean addFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "INSERT INTO t_orders(orderId, phoneNumber, vipLevel, boxId, goodsTotalNumber, payTotal, totalFavourable, actualPayTotal, payType, thirdPayOrderId, outTradeNo, transactionId, payState, payDate, buyDate) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
    public Order findOrderByOrderId(String orderId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_orders WHERE orderId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        Order order = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, orderId);
            rs = ps.executeQuery();
            if (rs.next()) {
                order = new Order();
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
    public List<Order> findOrdersByPhoneNumber(String phoneNumber) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_orders WHERE phoneNumber = ? ORDER BY buyDate DESC";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Order> ordersList = new ArrayList<Order>();
        Order order = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, phoneNumber);
            rs = ps.executeQuery();
            while (rs.next()) {
                order = new Order();
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
                ordersList.add(order);
            }
            return ordersList;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public List<Order> findOrdersByPhoneNumberAndPayState(String phoneNumber,
            int payState) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_orders WHERE phoneNumber = ? AND payState = ? ORDER BY buyDate DESC";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Order> ordersList = new ArrayList<Order>();
        Order order = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, phoneNumber);
            ps.setInt(2, payState);
            rs = ps.executeQuery();
            while (rs.next()) {
                order = new Order();
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
                ordersList.add(order);
            }
            return ordersList;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public boolean updateOrder(Order order) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_orders SET payState = ? WHERE orderId = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, order.getPayState());
            ps.setString(2, order.getOrderId());
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
    public List<Order> findOrdersByPhoneNumberAndPayStestAndPageNumber(
            String phoneNumber, int payState, int pageNumber) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_orders WHERE phoneNumber = ? AND payState = ? ORDER BY buyDate DESC LIMIT ? , ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Order> ordersList = new ArrayList<Order>();
        Order order = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, phoneNumber);
            ps.setInt(2, payState);
            ps.setInt(3, (pageNumber - 1) * Order.PAGE_SIZE);
            ps.setInt(4, Order.PAGE_SIZE);
            rs = ps.executeQuery();
            while (rs.next()) {
                order = new Order();
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
                ordersList.add(order);
            }
            return ordersList;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public int findOrderCountByPhoneNumberAndPayState(String phoneNumber,
            int payState) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT COUNT(*) AS orderCount FROM t_orders WHERE phoneNumber = ? AND payState = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        int orderCount = 0;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, phoneNumber);
            ps.setInt(2, payState);
            rs = ps.executeQuery();
            if (rs.next()) {
                orderCount = rs.getInt("orderCount");
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
    public List<Order> findTopNOrders(int topNOrderNumbers) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_orders ORDER BY buyDate DESC LIMIT ? , ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Order> ordersList = new ArrayList<Order>();
        Order order = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, 0);
            ps.setInt(2, topNOrderNumbers);
            rs = ps.executeQuery();
            while (rs.next()) {
                order = new Order();
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
                ordersList.add(order);
            }
            return ordersList;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public int findCurrentDayOrderCount() {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT COUNT(*) AS orderCount FROM t_orders WHERE DateDiff(dd, buyDate, getdate()) = 0";
        PreparedStatement ps = null;
        ResultSet rs = null;
        int orderCount = 0;
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                orderCount = rs.getInt("orderCount");
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
    public int findTotalOrderCount() {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT COUNT(*) AS orderCount FROM t_orders";
        PreparedStatement ps = null;
        ResultSet rs = null;
        int orderCount = 0;
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                orderCount = rs.getInt("orderCount");
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
    public int findOrderCountByPhoneNumber(String phoneNumber) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT COUNT(*) AS orderCount FROM t_orders WHERE phoneNumber = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        int orderCount = 0;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, phoneNumber);
            rs = ps.executeQuery();
            if (rs.next()) {
                orderCount = rs.getInt("orderCount");
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
    public Order findAllActualPayTotalAndAllTotalFavourableByPhoneNumber(
            String phoneNumber) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT SUM(actualPayTotal) AS allActualPayTotal, SUM(totalFavourable) AS allTotalFavourable FROM t_orders WHERE phoneNumber = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        Order order = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, phoneNumber);
            rs = ps.executeQuery();
            if (rs.next()) {
                order = new Order();
                order.setAllActualPayTotal(rs
                        .getBigDecimal("allActualPayTotal"));
                order.setAllTotalFavourable(rs
                        .getBigDecimal("allTotalFavourable"));
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
    public boolean updateOrderInfo(Order order) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = null;
        if (order.getPayDate() != null) {
            sql = "UPDATE t_orders SET payState = ?, outTradeNo = ?, transactionId = ?, payDate = ? WHERE orderId = ?";
            PreparedStatement ps = null;
            try {
                ps = connection.prepareStatement(sql);
                ps.setInt(1, order.getPayState());
                ps.setString(2, order.getOutTradeNo());
                ps.setString(3, order.getTransactionId());
                ps.setTimestamp(4, new Timestamp(order.getPayDate().getTime()));
                ps.setString(5, order.getOrderId());
                ps.executeUpdate();
                updateFlag = true;
            } catch (Exception e) {
                log.error("try->catch exception:", e);
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            } finally {
                JDBCUtil.closeDBConnection(connection, ps);
            }
        } else {
            sql = "UPDATE t_orders SET payState = ?, outTradeNo = ?, transactionId = ? WHERE orderId = ?";
            PreparedStatement ps = null;
            try {
                ps = connection.prepareStatement(sql);
                ps.setInt(1, order.getPayState());
                ps.setString(2, order.getOutTradeNo());
                ps.setString(3, order.getTransactionId());
                ps.setString(4, order.getOrderId());
                ps.executeUpdate();
                updateFlag = true;
            } catch (Exception e) {
                log.error("try->catch exception:", e);
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            } finally {
                JDBCUtil.closeDBConnection(connection, ps);
            }
        }
        return updateFlag;
    }

}
