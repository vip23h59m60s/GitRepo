package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.OrderDetailsThirdPartDAO;
import com.chinapalms.kwobox.javabean.OrderDetails;
import com.chinapalms.kwobox.javabean.OrderDetailsThirdPart;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class OrderDetailsThirdPartDAOImpl implements OrderDetailsThirdPartDAO {

    Log log = LogFactory.getLog(OrderDetailsDAOImpl.class);

    @Override
    public boolean addOrderDetailsThirdPart(OrderDetailsThirdPart orderDetails) {
        boolean addFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "INSERT INTO t_order_details_thirdpart(orderId, barCodeId, goodsName, salesMode, goodsPrice, goodsFavorablePrice, goodsActualPrice, categoryGoodsNumber, categoryGoodsWeight, categoryGoodsPrice, categoryfavorablePrice, actualCategoryPrice) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, orderDetails.getOrderId());
            ps.setString(2, orderDetails.getBarCodeId());
            ps.setString(3, orderDetails.getGoodsName());
            ps.setInt(4, orderDetails.getSalesMode());
            ps.setBigDecimal(5, orderDetails.getGoodsPrice());
            ps.setBigDecimal(6, orderDetails.getGoodsFavorablePrice());
            ps.setBigDecimal(7, orderDetails.getGoodsActualPrice());
            ps.setInt(8, orderDetails.getCategoryGoodsNumber());
            ps.setInt(9, orderDetails.getCategoryGoodsWeight());
            ps.setBigDecimal(10, orderDetails.getCategoryGoodsPrice());
            ps.setBigDecimal(11, orderDetails.getCategoryFavorablePrice());
            ps.setBigDecimal(12, orderDetails.getActualCategoryPrice());
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
    public List<OrderDetailsThirdPart> findOrderDetailsThirdPartByOrderId(
            String orderId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_order_details_thirdpart WHERE orderId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<OrderDetailsThirdPart> orderDetailsList = new ArrayList<OrderDetailsThirdPart>();
        OrderDetailsThirdPart orderDetails = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, orderId);
            rs = ps.executeQuery();
            while (rs.next()) {
                orderDetails = new OrderDetailsThirdPart();
                orderDetails.setOrderId(rs.getString("orderId"));
                orderDetails.setBarCodeId(rs.getString("barCodeId"));
                orderDetails.setGoodsName(rs.getString("goodsName"));
                orderDetails.setSalesMode(rs.getInt("salesMode"));
                orderDetails.setGoodsPrice(rs.getBigDecimal("goodsPrice"));
                orderDetails.setGoodsFavorablePrice(rs
                        .getBigDecimal("goodsFavorablePrice"));
                orderDetails.setGoodsActualPrice(rs
                        .getBigDecimal("goodsActualPrice"));
                orderDetails.setCategoryGoodsNumber(rs
                        .getInt("categoryGoodsNumber"));
                orderDetails.setCategoryGoodsWeight(rs
                        .getInt("categoryGoodsWeight"));
                orderDetails.setCategoryGoodsPrice(rs
                        .getBigDecimal("categoryGoodsPrice"));
                orderDetails.setCategoryFavorablePrice(rs
                        .getBigDecimal("categoryfavorablePrice"));
                orderDetails.setActualCategoryPrice(rs
                        .getBigDecimal("actualCategoryPrice"));
                orderDetailsList.add(orderDetails);
            }
            return orderDetailsList;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

}
