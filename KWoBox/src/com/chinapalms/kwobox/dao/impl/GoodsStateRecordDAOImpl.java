package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.GoodsStateRecordDAO;
import com.chinapalms.kwobox.javabean.GoodsStateRecord;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class GoodsStateRecordDAOImpl implements GoodsStateRecordDAO {

    Log log = LogFactory.getLog(GoodsStateRecordDAOImpl.class);

    @Override
    public boolean addGoodsStateRecord(GoodsStateRecord goodsStateRecord) {
        boolean addFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "INSERT INTO t_goods_state_record(goodsRFID, barCodeId, goodsName, goodsPrice, goodsDiscount, favourable, manufacturer, goodsChannel, makeDate, expiryTime, goodsPicture, state, customerId, workerId, updateDateTime) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, goodsStateRecord.getGoodsRFID());
            ps.setString(2, goodsStateRecord.getBarCodeId());
            ps.setString(3, goodsStateRecord.getGoodsName());
            ps.setBigDecimal(4, goodsStateRecord.getGoodsPrice());
            ps.setBigDecimal(5, goodsStateRecord.getGoodsDiscount());
            ps.setBigDecimal(6, goodsStateRecord.getFavourable());
            ps.setString(7, goodsStateRecord.getManufacturer());
            ps.setString(8, goodsStateRecord.getGoodsChannel());
            ps.setTimestamp(9, new Timestamp(goodsStateRecord.getMakeDate()
                    .getTime()));
            ps.setFloat(10, goodsStateRecord.getExpiryTime());
            ps.setString(11, goodsStateRecord.getGoodsPicture());
            ps.setInt(12, goodsStateRecord.getState());
            ps.setInt(13, goodsStateRecord.getCustomerId());
            ps.setInt(14, goodsStateRecord.getWorkerId());
            ps.setTimestamp(15, new Timestamp(goodsStateRecord
                    .getUpdateDateTime().getTime()));
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
    public GoodsStateRecord findGoodsStateRecordByDistinguishId(
            String distinguishId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_goods_state_record WHERE goodsRFID = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        GoodsStateRecord goodsStateRecord = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, distinguishId);
            rs = ps.executeQuery();
            if (rs.next()) {
                goodsStateRecord = new GoodsStateRecord();
                goodsStateRecord.setGoodsRFID(rs.getString("goodsRFID"));
                goodsStateRecord.setBarCodeId(rs.getString("barCodeId"));
                goodsStateRecord.setGoodsName(rs.getString("goodsName"));
                goodsStateRecord.setGoodsPrice(rs.getBigDecimal("goodsPrice"));
                goodsStateRecord.setGoodsDiscount(rs
                        .getBigDecimal("goodsDiscount"));
                goodsStateRecord.setFavourable(rs.getBigDecimal("favourable"));
                goodsStateRecord.setManufacturer(rs.getString("manufacturer"));
                goodsStateRecord.setGoodsChannel(rs.getString("goodsChannel"));
                goodsStateRecord.setMakeDate(rs.getTimestamp("makeDate"));
                goodsStateRecord.setExpiryTime(rs.getInt("expiryTime"));
                goodsStateRecord.setGoodsPicture(rs.getString("goodsPicture"));
                goodsStateRecord.setState(rs.getInt("state"));
                goodsStateRecord.setCustomerId(rs.getInt("customerId"));
                goodsStateRecord.setWorkerId(rs.getInt("workerId"));
                goodsStateRecord.setUpdateDateTime(rs
                        .getTimestamp("updateDateTime"));
            }
            return goodsStateRecord;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public boolean updateGoodsStateRecordByDistinguishId(String distinguishId,
            int goodsState) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_goods_state_record SET state = ? AND updateDateTime = ? WHERE goodsRFID = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, goodsState);
            ps.setTimestamp(2, new Timestamp(new Date().getTime()));
            ps.setString(3, distinguishId);
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
    public boolean updateGoodsStateRecord(GoodsStateRecord goodsStateRecord) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_goods_state_record SET barCodeId = ?, goodsName = ?, goodsPrice = ?, goodsDiscount = ?, favourable = ?, manufacturer = ?, goodsChannel = ?, makeDate = ?, expiryTime = ?, goodsPicture = ?, state = ?, customerId = ?, workerId = ?, updateDateTime = ? WHERE goodsRFID = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, goodsStateRecord.getBarCodeId());
            ps.setString(2, goodsStateRecord.getGoodsName());
            ps.setBigDecimal(3, goodsStateRecord.getGoodsPrice());
            ps.setBigDecimal(4, goodsStateRecord.getGoodsDiscount());
            ps.setBigDecimal(5, goodsStateRecord.getFavourable());
            ps.setString(6, goodsStateRecord.getManufacturer());
            ps.setString(7, goodsStateRecord.getGoodsChannel());
            ps.setTimestamp(8, new Timestamp(goodsStateRecord.getMakeDate()
                    .getTime()));
            ps.setFloat(9, goodsStateRecord.getExpiryTime());
            ps.setString(10, goodsStateRecord.getGoodsPicture());
            ps.setInt(11, goodsStateRecord.getState());
            ps.setInt(12, goodsStateRecord.getCustomerId());
            ps.setInt(13, goodsStateRecord.getWorkerId());
            ps.setTimestamp(14, new Timestamp(goodsStateRecord
                    .getUpdateDateTime().getTime()));
            ps.setString(15, goodsStateRecord.getGoodsRFID());
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
