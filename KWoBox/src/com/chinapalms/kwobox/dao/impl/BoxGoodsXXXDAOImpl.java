package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.BoxGoodsXXXDAO;
import com.chinapalms.kwobox.javabean.BoxGoodsXXX;
import com.chinapalms.kwobox.utils.JDBCUtil;
import com.chinapalms.kwobox.utils.ResponseStatus;

public class BoxGoodsXXXDAOImpl implements BoxGoodsXXXDAO {

    Log log = LogFactory.getLog(BoxGoodsXXXDAOImpl.class);

    @Override
    public boolean addBoxGoodsXXX(String boxId, int boxType,
            BoxGoodsXXX boxGoodsXXX) {
        // 通过boxId获取当前售货机的类型：1高频方案，2超高频方案，3称重方案
        if (boxType == ResponseStatus.BOX_TYPE_WEIGHT) {
            boolean addFlag = false;
            Connection connection = JDBCUtil.getDBConnection();
            String sql = "INSERT INTO t_box_goods_xxx(boxId, barCodeId, goodsName, cardgoRoadId, weight, stockNumber, brandCompany, salesMode, goodsPrice, goodsDiscount, favourable, makeDate, expiryTime, goodsPicture, deliveryManagerId, deliveryTime) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = null;
            try {
                ps = connection.prepareStatement(sql);
                ps.setString(1, boxGoodsXXX.getBoxId());
                ps.setString(2, boxGoodsXXX.getBarCodeId());
                ps.setString(3, boxGoodsXXX.getGoodsName());
                ps.setInt(4, boxGoodsXXX.getCardgoRoadId());
                ps.setInt(5, boxGoodsXXX.getWeight());
                ps.setInt(6, boxGoodsXXX.getStockNumber());
                ps.setString(7, boxGoodsXXX.getBrandCompany());
                ps.setInt(8, boxGoodsXXX.getSalesMode());
                ps.setBigDecimal(9, boxGoodsXXX.getGoodsPrice());
                ps.setBigDecimal(10, boxGoodsXXX.getGoodsDiscount());
                ps.setBigDecimal(11, boxGoodsXXX.getFavourable());
                ps.setTimestamp(12, new Timestamp(boxGoodsXXX.getMakeDate()
                        .getTime()));
                ps.setFloat(13, boxGoodsXXX.getExpiryTime());
                ps.setString(14, boxGoodsXXX.getGoodsPicture());
                ps.setInt(15, boxGoodsXXX.getDeliveryManagerId());
                ps.setTimestamp(16, new Timestamp(boxGoodsXXX.getDeliveryTime()
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
        } else {
            boolean addFlag = false;
            Connection connection = JDBCUtil.getDBConnection();
            String sql = "INSERT INTO t_box_goods_xxx(boxId, goodsRFID, barCodeId, goodsName, brandCompany, goodsPrice, goodsDiscount, favourable, makeDate, expiryTime, goodsPicture, deliveryManagerId, deliveryTime) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = null;
            try {
                ps = connection.prepareStatement(sql);
                ps.setString(1, boxGoodsXXX.getBoxId());
                ps.setString(2, boxGoodsXXX.getGoodsRFID());
                ps.setString(3, boxGoodsXXX.getBarCodeId());
                ps.setString(4, boxGoodsXXX.getGoodsName());
                ps.setString(5, boxGoodsXXX.getBrandCompany());
                ps.setBigDecimal(6, boxGoodsXXX.getGoodsPrice());
                ps.setBigDecimal(7, boxGoodsXXX.getGoodsDiscount());
                ps.setBigDecimal(8, boxGoodsXXX.getFavourable());
                ps.setTimestamp(9, new Timestamp(boxGoodsXXX.getMakeDate()
                        .getTime()));
                ps.setFloat(10, boxGoodsXXX.getExpiryTime());
                ps.setString(11, boxGoodsXXX.getGoodsPicture());
                ps.setInt(12, boxGoodsXXX.getDeliveryManagerId());
                ps.setTimestamp(13, new Timestamp(boxGoodsXXX.getDeliveryTime()
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

    @Override
    public BoxGoodsXXX findBoxGoodsXXXByGoodsDistinguishId(String boxId,
            String goodsDistinguishId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_box_goods_xxx WHERE boxId = ? AND goodsRFID = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        BoxGoodsXXX boxGoodsXXX = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, boxId);
            ps.setString(2, goodsDistinguishId);
            rs = ps.executeQuery();
            if (rs.next()) {
                boxGoodsXXX = new BoxGoodsXXX();
                boxGoodsXXX.setBoxId(rs.getString("boxId"));
                boxGoodsXXX.setGoodsRFID(rs.getString("goodsRFID"));
                boxGoodsXXX.setBarCodeId(rs.getString("barCodeId"));
                boxGoodsXXX.setGoodsName(rs.getString("goodsName"));
                boxGoodsXXX.setBrandCompany(rs.getString("brandCompany"));
                boxGoodsXXX.setGoodsPrice(rs.getBigDecimal("goodsPrice"));
                boxGoodsXXX.setGoodsDiscount(rs.getBigDecimal("goodsDiscount"));
                boxGoodsXXX.setFavourable(rs.getBigDecimal("favourable"));
                boxGoodsXXX.setMakeDate(rs.getTimestamp("makeDate"));
                boxGoodsXXX.setExpiryTime(rs.getInt("expiryTime"));
                boxGoodsXXX.setGoodsPicture(rs.getString("goodsPicture"));
                boxGoodsXXX
                        .setDeliveryManagerId(rs.getInt("deliveryManagerId"));
                boxGoodsXXX.setDeliveryTime(rs.getTimestamp("deliveryTime"));
            }
            return boxGoodsXXX;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public boolean deleteBoxGoodsXXXByGoodsDistinguishId(String boxId,
            String goodsDistinguishId) {
        boolean deleteFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "DELETE FROM t_box_goods_xxx WHERE boxId = ? AND goodsRFID = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, boxId);
            ps.setString(2, goodsDistinguishId);
            int delete = ps.executeUpdate();
            if (delete > 0) {
                deleteFlag = true;
            }
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
    public List<BoxGoodsXXX> findAllBoxGoodsXXX(String boxId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_box_goods_xxx WHERE boxId = ? ORDER BY cardgoRoadId ASC";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<BoxGoodsXXX> list = new ArrayList<BoxGoodsXXX>();
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, boxId);
            rs = ps.executeQuery();
            while (rs.next()) {
                BoxGoodsXXX boxGoodsXXX = new BoxGoodsXXX();
                boxGoodsXXX.setBoxId(rs.getString("boxId"));
                boxGoodsXXX.setGoodsRFID(rs.getString("goodsRFID"));
                boxGoodsXXX.setBarCodeId(rs.getString("barCodeId"));
                boxGoodsXXX.setGoodsName(rs.getString("goodsName"));
                boxGoodsXXX.setCardgoRoadId(rs.getInt("cardgoRoadId"));
                boxGoodsXXX.setWeight(rs.getInt("weight"));
                boxGoodsXXX.setStockNumber(rs.getInt("stockNumber"));
                boxGoodsXXX.setBrandCompany(rs.getString("brandCompany"));
                boxGoodsXXX.setSalesMode(rs.getInt("salesMode"));
                boxGoodsXXX.setGoodsPrice(rs.getBigDecimal("goodsPrice"));
                boxGoodsXXX.setGoodsDiscount(rs.getBigDecimal("goodsDiscount"));
                boxGoodsXXX.setFavourable(rs.getBigDecimal("favourable"));
                boxGoodsXXX.setMakeDate(rs.getTimestamp("makeDate"));
                boxGoodsXXX.setExpiryTime(rs.getInt("expiryTime"));
                boxGoodsXXX.setGoodsPicture(rs.getString("goodsPicture"));
                boxGoodsXXX
                        .setDeliveryManagerId(rs.getInt("deliveryManagerId"));
                boxGoodsXXX.setDeliveryTime(rs.getTimestamp("deliveryTime"));
                list.add(boxGoodsXXX);
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
    public boolean updateBoxGoodsByBarCodeIdAndCardgoRoadIdAndStockNumber(
            String boxId, String barCodeId, int cardgoRoadId, int stockNumber) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_box_goods_xxx SET stockNumber = ? WHERE boxId = ? AND barCodeId = ? AND cardgoRoadId = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, stockNumber);
            ps.setString(2, boxId);
            ps.setString(3, barCodeId);
            ps.setInt(4, cardgoRoadId);
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
    public boolean updateBoxGoodsByBarCodeIdAndCardgoRoadIdAndNumberOfChange(
            String boxId, String barCodeId, int cardgoRoadId, int changeNumber) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_box_goods_xxx SET stockNumber = stockNumber - ? WHERE boxId = ? AND barCodeId = ? AND cardgoRoadId = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, changeNumber);
            ps.setString(2, boxId);
            ps.setString(3, barCodeId);
            ps.setInt(4, cardgoRoadId);
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
    public boolean emptyBoxGoodsByBoxId(String boxId, int boxType) {
        // 如果是RFID方案就直接删除相对boxId售货柜的所有记录，如果是称重方案就将每条记录每个货道对应的商品置为0，保留售货柜结构即可
        if (boxType == ResponseStatus.BOX_TYPE_WEIGHT) {
            boolean updateFlag = false;
            Connection connection = JDBCUtil.getDBConnection();
            String sql = "UPDATE t_box_goods_xxx SET stockNumber = ? WHERE boxId = ?";
            PreparedStatement ps = null;
            try {
                ps = connection.prepareStatement(sql);
                ps.setInt(1, 0);
                ps.setString(2, boxId);
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
        } else {
            boolean updateFlag = false;
            Connection connection = JDBCUtil.getDBConnection();
            String sql = "DELETE FROM t_box_goods_xxx WHERE boxId = ?";
            PreparedStatement ps = null;
            try {
                ps = connection.prepareStatement(sql);
                ps.setString(1, boxId);
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

    @Override
    public BoxGoodsXXX findBoxGoodsXXXByBarCodeIdAndCardgoRoadId(String boxId,
            String barCodeId, int CardgoRoadId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_box_goods_xxx WHERE boxId = ? AND barCodeId = ? AND cardgoRoadId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        BoxGoodsXXX boxGoodsXXX = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, boxId);
            ps.setString(2, barCodeId);
            ps.setInt(3, CardgoRoadId);
            rs = ps.executeQuery();
            if (rs.next()) {
                boxGoodsXXX = new BoxGoodsXXX();
                boxGoodsXXX.setBoxId(rs.getString("boxId"));
                boxGoodsXXX.setBarCodeId(rs.getString("barCodeId"));
                boxGoodsXXX.setGoodsName(rs.getString("goodsName"));
                boxGoodsXXX.setCardgoRoadId(rs.getInt("cardgoRoadId"));
                boxGoodsXXX.setWeight(rs.getInt("weight"));
                boxGoodsXXX.setStockNumber(rs.getInt("stockNumber"));
                boxGoodsXXX.setBrandCompany(rs.getString("brandCompany"));
                boxGoodsXXX.setSalesMode(rs.getInt("salesMode"));
                boxGoodsXXX.setGoodsPrice(rs.getBigDecimal("goodsPrice"));
                boxGoodsXXX.setGoodsDiscount(rs.getBigDecimal("goodsDiscount"));
                boxGoodsXXX.setFavourable(rs.getBigDecimal("favourable"));
                boxGoodsXXX.setMakeDate(rs.getTimestamp("makeDate"));
                boxGoodsXXX.setExpiryTime(rs.getInt("expiryTime"));
                boxGoodsXXX.setGoodsPicture(rs.getString("goodsPicture"));
                boxGoodsXXX
                        .setDeliveryManagerId(rs.getInt("deliveryManagerId"));
                boxGoodsXXX.setDeliveryTime(rs.getTimestamp("deliveryTime"));
            }
            return boxGoodsXXX;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public boolean updateBoxGoods(String boxId, BoxGoodsXXX boxGoodsXXX) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_box_goods_xxx SET barCodeId = ?, goodsName = ?, brandCompany = ?, goodsPrice = ?, goodsDiscount = ?, favourable = ?, makeDate = ?, expiryTime = ?, goodsPicture = ?, deliveryManagerId = ?, deliveryTime = ? WHERE boxId = ? AND goodsRFID = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, boxGoodsXXX.getBarCodeId());
            ps.setString(2, boxGoodsXXX.getGoodsName());
            ps.setString(3, boxGoodsXXX.getBrandCompany());
            ps.setBigDecimal(4, boxGoodsXXX.getGoodsPrice());
            ps.setBigDecimal(5, boxGoodsXXX.getGoodsDiscount());
            ps.setBigDecimal(6, boxGoodsXXX.getFavourable());
            ps.setTimestamp(7, new Timestamp(boxGoodsXXX.getMakeDate()
                    .getTime()));
            ps.setFloat(8, boxGoodsXXX.getExpiryTime());
            ps.setString(9, boxGoodsXXX.getGoodsPicture());
            ps.setInt(10, boxGoodsXXX.getDeliveryManagerId());
            ps.setTimestamp(11, new Timestamp(boxGoodsXXX.getDeliveryTime()
                    .getTime()));
            ps.setString(12, boxGoodsXXX.getBoxId());
            ps.setString(13, boxGoodsXXX.getGoodsRFID());
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
    public List<BoxGoodsXXX> findBoxGoodsByBarCodeId(String boxId,
            String barCodeId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_box_goods_xxx WHERE boxId = ? AND barCodeId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<BoxGoodsXXX> boxGoodsXXXList = new ArrayList<BoxGoodsXXX>();
        BoxGoodsXXX boxGoodsXXX = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, boxId);
            ps.setString(2, barCodeId);
            rs = ps.executeQuery();
            while (rs.next()) {
                boxGoodsXXX = new BoxGoodsXXX();
                boxGoodsXXX.setBoxId(rs.getString("boxId"));
                boxGoodsXXX.setBarCodeId(rs.getString("barCodeId"));
                boxGoodsXXX.setGoodsName(rs.getString("goodsName"));
                boxGoodsXXX.setCardgoRoadId(rs.getInt("cardgoRoadId"));
                boxGoodsXXX.setWeight(rs.getInt("weight"));
                boxGoodsXXX.setStockNumber(rs.getInt("stockNumber"));
                boxGoodsXXX.setBrandCompany(rs.getString("brandCompany"));
                boxGoodsXXX.setSalesMode(rs.getInt("salesMode"));
                boxGoodsXXX.setGoodsPrice(rs.getBigDecimal("goodsPrice"));
                boxGoodsXXX.setGoodsDiscount(rs.getBigDecimal("goodsDiscount"));
                boxGoodsXXX.setFavourable(rs.getBigDecimal("favourable"));
                boxGoodsXXX.setMakeDate(rs.getTimestamp("makeDate"));
                boxGoodsXXX.setExpiryTime(rs.getInt("expiryTime"));
                boxGoodsXXX.setGoodsPicture(rs.getString("goodsPicture"));
                boxGoodsXXX
                        .setDeliveryManagerId(rs.getInt("deliveryManagerId"));
                boxGoodsXXX.setDeliveryTime(rs.getTimestamp("deliveryTime"));
                boxGoodsXXXList.add(boxGoodsXXX);
            }
            return boxGoodsXXXList;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public boolean deleteAllBoxGoodsXXX(String boxId) {
        boolean deleteFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "DELETE FROM t_box_goods_xxx WHERE boxId = ?";
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
    public boolean updateBoxGoodsXXXByCardgoRoadId(String boxId,
            BoxGoodsXXX boxGoodsXXX) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_box_goods_xxx SET barCodeId = ?, goodsName = ?, weight = ?, stockNumber = ?, brandCompany = ?, salesMode = ?, goodsPrice = ?, goodsDiscount = ?, favourable = ?, makeDate = ?, expiryTime = ?, goodsPicture = ?, deliveryManagerId = ?, deliveryTime = ? WHERE boxId = ? AND cardgoRoadId = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, boxGoodsXXX.getBarCodeId());
            ps.setString(2, boxGoodsXXX.getGoodsName());
            ps.setInt(3, boxGoodsXXX.getWeight());
            ps.setInt(4, boxGoodsXXX.getStockNumber());
            ps.setString(5, boxGoodsXXX.getBrandCompany());
            ps.setInt(6, boxGoodsXXX.getSalesMode());
            ps.setBigDecimal(7, boxGoodsXXX.getGoodsPrice());
            ps.setBigDecimal(8, boxGoodsXXX.getGoodsDiscount());
            ps.setBigDecimal(9, boxGoodsXXX.getFavourable());
            ps.setTimestamp(10, new Timestamp(boxGoodsXXX.getMakeDate()
                    .getTime()));
            ps.setFloat(11, boxGoodsXXX.getExpiryTime());
            ps.setString(12, boxGoodsXXX.getGoodsPicture());
            ps.setInt(13, boxGoodsXXX.getDeliveryManagerId());
            ps.setTimestamp(14, new Timestamp(boxGoodsXXX.getDeliveryTime()
                    .getTime()));
            ps.setString(15, boxGoodsXXX.getBoxId());
            ps.setInt(16, boxGoodsXXX.getCardgoRoadId());
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
    public boolean updateBoxGoodsXXXPriceByCardgoRoadId(String boxId,
            BoxGoodsXXX boxGoodsXXX) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_box_goods_xxx SET goodsPrice = ?, goodsDiscount = ?, favourable = ? WHERE boxId = ? AND cardgoRoadId = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setBigDecimal(1, boxGoodsXXX.getGoodsPrice());
            ps.setBigDecimal(2, boxGoodsXXX.getGoodsDiscount());
            ps.setBigDecimal(3, boxGoodsXXX.getFavourable());
            ps.setString(4, boxGoodsXXX.getBoxId());
            ps.setInt(5, boxGoodsXXX.getCardgoRoadId());
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
