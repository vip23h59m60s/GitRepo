package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.BoxGoodsStockDAO;
import com.chinapalms.kwobox.javabean.BoxGoodsStock;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class BoxGoodsStockDAOImpl implements BoxGoodsStockDAO {

    Log log = LogFactory.getLog(BoxGoodsStockDAOImpl.class);

    @Override
    public boolean addBoxGoodsStock(BoxGoodsStock boxGoodsStock) {
        boolean addFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "INSERT INTO t_box_goods_stock(replenishmentId, boxId, cardgoRoadId, barCodeId, stockNumber) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, boxGoodsStock.getReplenishmentId());
            ps.setString(2, boxGoodsStock.getBoxId());
            ps.setInt(3, boxGoodsStock.getCardgoRoadId());
            ps.setString(4, boxGoodsStock.getBarCodeId());
            ps.setInt(5, boxGoodsStock.getStockNumber());
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
