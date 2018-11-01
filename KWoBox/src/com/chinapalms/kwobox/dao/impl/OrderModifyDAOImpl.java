package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.OrderModifyDAO;
import com.chinapalms.kwobox.javabean.OrderModify;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class OrderModifyDAOImpl implements OrderModifyDAO {

    Log log = LogFactory.getLog(OrderModifyDAOImpl.class);

    @Override
    public boolean addOrderModify(OrderModify order) {
        boolean addFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "INSERT INTO t_orders_modify(orderId, newOrderId, phoneNumber, vipLevel, boxId, goodsTotalNumber, payTotal, totalFavourable, actualPayTotal, payType, thirdPayOrderId, outTradeNo, transactionId, payState, payDate, buyDate, orderUpdateOwner, orderUpdateDate) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, order.getOrderId());
            ps.setString(2, order.getNewOrderId());
            ps.setString(3, order.getPhoneNumber());
            ps.setInt(4, order.getVipLevel());
            ps.setString(5, order.getBoxId());
            ps.setInt(6, order.getGoodsTotalNumber());
            ps.setBigDecimal(7, order.getPayTotal());
            ps.setBigDecimal(8, order.getTotalFavourable());
            ps.setBigDecimal(9, order.getActualPayTotal());
            ps.setString(10, order.getPayType());
            ps.setString(11, order.getThirdPayOrderId());
            ps.setString(12, order.getOutTradeNo());
            ps.setString(13, order.getTransactionId());
            ps.setInt(14, order.getPayState());
            ps.setTimestamp(15, new Timestamp(order.getPayDate().getTime()));
            ps.setTimestamp(16, new Timestamp(order.getBuyDate().getTime()));
            ps.setString(17, order.getOrderUpdateOwner());
            ps.setTimestamp(18, new Timestamp(order.getOrderUpdateDate()
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

}
