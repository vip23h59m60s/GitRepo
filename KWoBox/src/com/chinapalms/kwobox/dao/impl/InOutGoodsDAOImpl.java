package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.javabean.InOutGoods;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class InOutGoodsDAOImpl implements
        com.chinapalms.kwobox.dao.InOutGoodsDAO {

    Log log = LogFactory.getLog(InOutGoodsDAOImpl.class);

    @Override
    public boolean addInOutGoods(InOutGoods inOutGoods) {
        boolean addFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "INSERT INTO t_inout_goods(goodsRFID, barCodeId, goodsName, goodsPrice, goodsDiscount, favourable, manufacturer, goodsChannel, makeDate, expiryTime, goodsPicture, customerId, workerId, inOutTime) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, inOutGoods.getGoodsRFID());
            ps.setString(2, inOutGoods.getBarCodeId());
            ps.setString(3, inOutGoods.getGoodsName());
            ps.setBigDecimal(4, inOutGoods.getGoodsPrice());
            ps.setBigDecimal(5, inOutGoods.getGoodsDiscount());
            ps.setBigDecimal(6, inOutGoods.getFavourable());
            ps.setString(7, inOutGoods.getManufacturer());
            ps.setString(8, inOutGoods.getGoodsChannel());
            ps.setTimestamp(9,
                    new Timestamp(inOutGoods.getMakeDate().getTime()));
            ps.setFloat(10, inOutGoods.getExpiryTime());
            ps.setString(11, inOutGoods.getGoodsPicture());
            ps.setInt(12, inOutGoods.getCustomerId());
            ps.setInt(13, inOutGoods.getWorkerId());
            ps.setTimestamp(14, new Timestamp(inOutGoods.getInOutTime()
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

    @Override
    public InOutGoods findInOutGoodsByGoodsRFID(String goodsRFID) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_inout_goods WHERE goodsRFID = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        InOutGoods inOutGoods = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, goodsRFID);
            rs = ps.executeQuery();
            if (rs.next()) {
                inOutGoods = new InOutGoods();
                inOutGoods.setGoodsRFID(rs.getString("goodsRFID"));
                inOutGoods.setBarCodeId(rs.getString("barCodeId"));
                inOutGoods.setGoodsName(rs.getString("goodsName"));
                inOutGoods.setGoodsPrice(rs.getBigDecimal("goodsPrice"));
                inOutGoods.setGoodsDiscount(rs.getBigDecimal("goodsDiscount"));
                inOutGoods.setFavourable(rs.getBigDecimal("favourable"));
                inOutGoods.setManufacturer(rs.getString("manufacturer"));
                inOutGoods.setGoodsChannel(rs.getString("goodsChannel"));
                inOutGoods.setMakeDate(rs.getTimestamp("makeDate"));
                inOutGoods.setExpiryTime(rs.getInt("expiryTime"));
                inOutGoods.setGoodsPicture(rs.getString("goodsPicture"));
                inOutGoods.setCustomerId(rs.getInt("customerId"));
                inOutGoods.setWorkerId(rs.getInt("workerId"));
                inOutGoods.setInOutTime(rs.getTimestamp("inOutTime"));
            }
            return inOutGoods;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public boolean deleteInOutGoodsByGoodsRFID(String goodsRFID) {
        boolean deleteFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "DELETE FROM t_inout_goods WHERE goodsRFID = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, goodsRFID);
            ps.executeUpdate();
            deleteFlag = true;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps);
        }
        return deleteFlag;
    }

    @Override
    public boolean updateInOutGoods(InOutGoods inOutGoods) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_inout_goods SET barCodeId = ?, goodsName = ?, goodsPrice = ?, goodsDiscount = ?, favourable = ?, manufacturer = ?, goodsChannel = ?, makeDate = ?, expiryTime = ?, goodsPicture = ?, customerId = ?, workerId = ?, inOutTime = ? where goodsRFID = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, inOutGoods.getBarCodeId());
            ps.setString(2, inOutGoods.getGoodsName());
            ps.setBigDecimal(3, inOutGoods.getGoodsPrice());
            ps.setBigDecimal(4, inOutGoods.getGoodsDiscount());
            ps.setBigDecimal(5, inOutGoods.getFavourable());
            ps.setString(6, inOutGoods.getManufacturer());
            ps.setString(7, inOutGoods.getGoodsChannel());
            ps.setTimestamp(8,
                    new Timestamp(inOutGoods.getMakeDate().getTime()));
            ps.setFloat(9, inOutGoods.getExpiryTime());
            ps.setString(10, inOutGoods.getGoodsPicture());
            ps.setInt(11, inOutGoods.getCustomerId());
            ps.setInt(12, inOutGoods.getWorkerId());
            ps.setTimestamp(13, new Timestamp(inOutGoods.getInOutTime()
                    .getTime()));
            ps.setString(14, inOutGoods.getGoodsRFID());
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
