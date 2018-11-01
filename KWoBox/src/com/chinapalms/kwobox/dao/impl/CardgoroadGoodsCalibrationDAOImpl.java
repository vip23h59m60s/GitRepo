package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.CardgoroadGoodsCalibrationDAO;
import com.chinapalms.kwobox.javabean.CardgoroadGoodsCalibration;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class CardgoroadGoodsCalibrationDAOImpl implements
        CardgoroadGoodsCalibrationDAO {

    Log log = LogFactory.getLog(CardgoroadGoodsCalibrationDAOImpl.class);

    @Override
    public boolean addCardgoroadGoodsCalibration(
            CardgoroadGoodsCalibration cardgoroadGoodsCalibration) {
        boolean addFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "INSERT INTO t_cardgoroad_goods_calibration(calibrationRecordId, replenishmentId, boxId, cardgoRoadNumber, barCodeId, goodsName, beforeCalibrationStockNumber, calibrationType, updateGoodsNumber, afterCalibrationStockNumber, boxDeliveryId, calibrationTime) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, cardgoroadGoodsCalibration.getCalibrationRecordId());
            ps.setString(2, cardgoroadGoodsCalibration.getReplenishmentId());
            ps.setString(3, cardgoroadGoodsCalibration.getBoxId());
            ps.setInt(4, cardgoroadGoodsCalibration.getCardgoRoadNumber());
            ps.setString(5, cardgoroadGoodsCalibration.getBarCodeId());
            ps.setString(6, cardgoroadGoodsCalibration.getGoodsName());
            ps.setInt(7, cardgoroadGoodsCalibration
                    .getBeforeCalibrationStockNumber());
            ps.setInt(8, cardgoroadGoodsCalibration.getCalibrationType());
            ps.setInt(9, cardgoroadGoodsCalibration.getUpdateGoodsNumber());
            ps.setInt(10,
                    cardgoroadGoodsCalibration.getAfterCalibrationStockNumber());
            ps.setInt(11, cardgoroadGoodsCalibration.getBoxDeliveryId());
            ps.setTimestamp(12, new Timestamp(cardgoroadGoodsCalibration
                    .getCalibrationTime().getTime()));
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
