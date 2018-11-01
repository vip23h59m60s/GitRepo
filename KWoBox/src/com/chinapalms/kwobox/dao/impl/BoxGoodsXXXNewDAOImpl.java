package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.BoxGoodsXXXNewDAO;
import com.chinapalms.kwobox.javabean.BoxGoodsXXX;
import com.chinapalms.kwobox.javabean.BoxGoodsXXXNew;
import com.chinapalms.kwobox.utils.JDBCUtil;
import com.chinapalms.kwobox.utils.ResponseStatus;

public class BoxGoodsXXXNewDAOImpl implements BoxGoodsXXXNewDAO {

    Log log = LogFactory.getLog(BoxGoodsXXXNewDAOImpl.class);

    @Override
    public List<BoxGoodsXXXNew> findAllBoxGoodsXXXNew(String boxId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_box_goods_xxx_new WHERE boxId = ? ORDER BY cardgoRoadId ASC";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<BoxGoodsXXXNew> list = new ArrayList<BoxGoodsXXXNew>();
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, boxId);
            rs = ps.executeQuery();
            while (rs.next()) {
                BoxGoodsXXXNew boxGoodsXXXNew = new BoxGoodsXXXNew();
                boxGoodsXXXNew.setBoxId(rs.getString("boxId"));
                boxGoodsXXXNew.setGoodsRFID(rs.getString("goodsRFID"));
                boxGoodsXXXNew.setBarCodeId(rs.getString("barCodeId"));
                boxGoodsXXXNew.setGoodsName(rs.getString("goodsName"));
                boxGoodsXXXNew.setCardgoRoadId(rs.getInt("cardgoRoadId"));
                boxGoodsXXXNew.setWeight(rs.getInt("weight"));
                boxGoodsXXXNew.setStockNumber(rs.getInt("stockNumber"));
                boxGoodsXXXNew.setBrandCompany(rs.getString("brandCompany"));
                boxGoodsXXXNew.setSalesMode(rs.getInt("salesMode"));
                boxGoodsXXXNew.setGoodsPrice(rs.getBigDecimal("goodsPrice"));
                boxGoodsXXXNew.setGoodsDiscount(rs
                        .getBigDecimal("goodsDiscount"));
                boxGoodsXXXNew.setFavourable(rs.getBigDecimal("favourable"));
                boxGoodsXXXNew.setMakeDate(rs.getTimestamp("makeDate"));
                boxGoodsXXXNew.setExpiryTime(rs.getInt("expiryTime"));
                boxGoodsXXXNew.setGoodsPicture(rs.getString("goodsPicture"));
                boxGoodsXXXNew.setDeliveryManagerId(rs
                        .getInt("deliveryManagerId"));
                boxGoodsXXXNew.setDeliveryTime(rs.getTimestamp("deliveryTime"));
                list.add(boxGoodsXXXNew);
            }
            return list;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public boolean deleteAllBoxGoodsXXXNew(String boxId) {
        boolean deleteFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "DELETE FROM t_box_goods_xxx_new WHERE boxId = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, boxId);
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
    public boolean addBoxGoodsXXX(String boxId, int boxType,
            BoxGoodsXXXNew boxGoodsXXXNew) {
        // 通过boxId获取当前售货机的类型：1高频方案，2超高频方案，3称重方案
        if (boxType == ResponseStatus.BOX_TYPE_WEIGHT) {
            boolean addFlag = false;
            Connection connection = JDBCUtil.getDBConnection();
            String sql = "INSERT INTO t_box_goods_xxx_new(boxId, barCodeId, goodsName, cardgoRoadId, weight, stockNumber, brandCompany, salesMode, goodsPrice, goodsDiscount, favourable, makeDate, expiryTime, goodsPicture, deliveryManagerId, deliveryTime) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = null;
            try {
                ps = connection.prepareStatement(sql);
                ps.setString(1, boxGoodsXXXNew.getBoxId());
                ps.setString(2, boxGoodsXXXNew.getBarCodeId());
                ps.setString(3, boxGoodsXXXNew.getGoodsName());
                ps.setInt(4, boxGoodsXXXNew.getCardgoRoadId());
                ps.setInt(5, boxGoodsXXXNew.getWeight());
                ps.setInt(6, boxGoodsXXXNew.getStockNumber());
                ps.setString(7, boxGoodsXXXNew.getBrandCompany());
                ps.setInt(8, boxGoodsXXXNew.getSalesMode());
                ps.setBigDecimal(9, boxGoodsXXXNew.getGoodsPrice());
                ps.setBigDecimal(10, boxGoodsXXXNew.getGoodsDiscount());
                ps.setBigDecimal(11, boxGoodsXXXNew.getFavourable());
                ps.setTimestamp(12, new Timestamp(boxGoodsXXXNew.getMakeDate()
                        .getTime()));
                ps.setFloat(13, boxGoodsXXXNew.getExpiryTime());
                ps.setString(14, boxGoodsXXXNew.getGoodsPicture());
                ps.setInt(15, boxGoodsXXXNew.getDeliveryManagerId());
                ps.setTimestamp(16, new Timestamp(boxGoodsXXXNew
                        .getDeliveryTime().getTime()));
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
        } else {
            boolean addFlag = false;
            Connection connection = JDBCUtil.getDBConnection();
            String sql = "INSERT INTO t_box_goods_xxx_new(boxId, goodsRFID, barCodeId, goodsName, brandCompany, goodsPrice, goodsDiscount, favourable, makeDate, expiryTime, goodsPicture, deliveryManagerId, deliveryTime) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = null;
            try {
                ps = connection.prepareStatement(sql);
                ps.setString(1, boxGoodsXXXNew.getBoxId());
                ps.setString(2, boxGoodsXXXNew.getGoodsRFID());
                ps.setString(3, boxGoodsXXXNew.getBarCodeId());
                ps.setString(4, boxGoodsXXXNew.getGoodsName());
                ps.setString(5, boxGoodsXXXNew.getBrandCompany());
                ps.setBigDecimal(6, boxGoodsXXXNew.getGoodsPrice());
                ps.setBigDecimal(7, boxGoodsXXXNew.getGoodsDiscount());
                ps.setBigDecimal(8, boxGoodsXXXNew.getFavourable());
                ps.setTimestamp(9, new Timestamp(boxGoodsXXXNew.getMakeDate()
                        .getTime()));
                ps.setFloat(10, boxGoodsXXXNew.getExpiryTime());
                ps.setString(11, boxGoodsXXXNew.getGoodsPicture());
                ps.setInt(12, boxGoodsXXXNew.getDeliveryManagerId());
                ps.setTimestamp(13, new Timestamp(boxGoodsXXXNew
                        .getDeliveryTime().getTime()));
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

}
