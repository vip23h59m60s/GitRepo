package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.OrderDetailsModifyDAO;
import com.chinapalms.kwobox.dao.OrderDetailsThirdPartModifyDAO;
import com.chinapalms.kwobox.javabean.OrderDetailsModify;
import com.chinapalms.kwobox.javabean.OrderDetailsThirdPartModify;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class OrderDetailsThirdPartModifyDAOImpl implements
        OrderDetailsThirdPartModifyDAO {

    Log log = LogFactory.getLog(OrderModifyDAOImpl.class);

    @Override
    public boolean addOrderDetailsThirdPartModify(
            OrderDetailsThirdPartModify orderDetails) {
        boolean addFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "INSERT INTO t_order_details_thirdpart_modify(orderId, barCodeId, goodsName, salesMode, goodsPrice, goodsFavorablePrice, goodsActualPrice, categoryGoodsNumber, categoryGoodsWeight, categoryGoodsPrice, categoryfavorablePrice, actualCategoryPrice) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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

}
