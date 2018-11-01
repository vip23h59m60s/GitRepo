package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.ReplenishGoodsDAO;
import com.chinapalms.kwobox.javabean.ReplenishGoods;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class ReplenishGoodsDAOImpl implements ReplenishGoodsDAO {

    Log log = LogFactory.getLog(ReplenishGoodsDAOImpl.class);

    @Override
    public boolean addReplenishGoods(ReplenishGoods replenishGoods) {
        boolean addFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "INSERT INTO t_replenishment(replenishmentId, boxId, boxDeliveryId, replenishmentGoodsNumber, takeOffGoodsNumber, replenishType, replenishmentTime) VALUES(?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, replenishGoods.getReplenishmentId());
            ps.setString(2, replenishGoods.getBoxId());
            ps.setInt(3, replenishGoods.getBoxDeliveryId());
            ps.setInt(4, replenishGoods.getReplenishmentGoodsNumber());
            ps.setInt(5, replenishGoods.getTakeOffGoodsNumber());
            ps.setInt(6, replenishGoods.getReplenishType());
            ps.setTimestamp(7, new Timestamp(replenishGoods
                    .getReplenishmentTime().getTime()));
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
    public List<ReplenishGoods> findReplenishGoodsByBoxDeliveryId(
            int boxDeliveryId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_replenishment WHERE boxDeliveryId = ? AND replenishType = ? ORDER by replenishmentTime DESC";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ReplenishGoods> replenishGoodsList = new ArrayList<ReplenishGoods>();
        ReplenishGoods replenishGoods = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, boxDeliveryId);
            ps.setInt(2, 1);
            rs = ps.executeQuery();
            while (rs.next()) {
                replenishGoods = new ReplenishGoods();
                replenishGoods.setReplenishmentId(rs
                        .getString("replenishmentId"));
                replenishGoods.setBoxId(rs.getString("boxId"));
                replenishGoods.setBoxDeliveryId(rs.getInt("boxDeliveryId"));
                replenishGoods.setReplenishmentGoodsNumber(rs
                        .getInt("replenishmentGoodsNumber"));
                replenishGoods.setTakeOffGoodsNumber(rs
                        .getInt("takeOffGoodsNumber"));
                replenishGoods.setReplenishType(rs.getInt("replenishType"));
                replenishGoods.setReplenishmentTime(rs
                        .getTimestamp("replenishmentTime"));
                replenishGoodsList.add(replenishGoods);
            }
            return replenishGoodsList;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public int findReplenishGoodsCountByBoxDeliveryId(int boxDeliveryId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT COUNT(*) AS replenishGoodsCount FROM t_replenishment WHERE boxDeliveryId = ? AND replenishType = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        int replenishGoodsCount = 0;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, boxDeliveryId);
            ps.setInt(2, 1);
            rs = ps.executeQuery();
            if (rs.next()) {
                replenishGoodsCount = rs.getInt("replenishGoodsCount");
            }
            return replenishGoodsCount;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public List<ReplenishGoods> findReplenishGoodsByBoxDeliveryIdAndPageNumber(
            int boxDeliveryId, int pageNumber) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_replenishment WHERE boxDeliveryId = ? AND replenishType = ? ORDER by replenishmentTime DESC LIMIT ? , ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ReplenishGoods> replenishGoodsList = new ArrayList<ReplenishGoods>();
        ReplenishGoods replenishGoods = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, boxDeliveryId);
            ps.setInt(2, 1);
            ps.setInt(3, (pageNumber - 1) * ReplenishGoods.PAGE_SIZE);
            ps.setInt(4, ReplenishGoods.PAGE_SIZE);
            rs = ps.executeQuery();
            while (rs.next()) {
                replenishGoods = new ReplenishGoods();
                replenishGoods.setReplenishmentId(rs
                        .getString("replenishmentId"));
                replenishGoods.setBoxId(rs.getString("boxId"));
                replenishGoods.setBoxDeliveryId(rs.getInt("boxDeliveryId"));
                replenishGoods.setReplenishmentGoodsNumber(rs
                        .getInt("replenishmentGoodsNumber"));
                replenishGoods.setTakeOffGoodsNumber(rs
                        .getInt("takeOffGoodsNumber"));
                replenishGoods.setReplenishType(rs.getInt("replenishType"));
                replenishGoods.setReplenishmentTime(rs
                        .getTimestamp("replenishmentTime"));
                replenishGoodsList.add(replenishGoods);
            }
            return replenishGoodsList;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public List<ReplenishGoods> findReplenishGoodsByBoxDeliveryIdAndBoxIdAndPageNumber(
            int boxDeliveryId, String boxId, int pageNumber) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_replenishment WHERE boxDeliveryId = ? AND boxId = ? AND replenishType = ? ORDER by replenishmentTime DESC LIMIT ? , ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ReplenishGoods> replenishGoodsList = new ArrayList<ReplenishGoods>();
        ReplenishGoods replenishGoods = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, boxDeliveryId);
            ps.setString(2, boxId);
            ps.setInt(3, 1);
            ps.setInt(4, (pageNumber - 1) * ReplenishGoods.PAGE_SIZE);
            ps.setInt(5, ReplenishGoods.PAGE_SIZE);
            rs = ps.executeQuery();
            while (rs.next()) {
                replenishGoods = new ReplenishGoods();
                replenishGoods.setReplenishmentId(rs
                        .getString("replenishmentId"));
                replenishGoods.setBoxId(rs.getString("boxId"));
                replenishGoods.setBoxDeliveryId(rs.getInt("boxDeliveryId"));
                replenishGoods.setReplenishmentGoodsNumber(rs
                        .getInt("replenishmentGoodsNumber"));
                replenishGoods.setTakeOffGoodsNumber(rs
                        .getInt("takeOffGoodsNumber"));
                replenishGoods.setReplenishType(rs.getInt("replenishType"));
                replenishGoods.setReplenishmentTime(rs
                        .getTimestamp("replenishmentTime"));
                replenishGoodsList.add(replenishGoods);
            }
            return replenishGoodsList;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public List<ReplenishGoods> findReplenishGoodsByBoxId(String boxId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_replenishment WHERE boxId = ? AND replenishType = ? ORDER by replenishmentTime DESC";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ReplenishGoods> replenishGoodsList = new ArrayList<ReplenishGoods>();
        ReplenishGoods replenishGoods = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, boxId);
            ps.setInt(2, 1);
            rs = ps.executeQuery();
            while (rs.next()) {
                replenishGoods = new ReplenishGoods();
                replenishGoods.setReplenishmentId(rs
                        .getString("replenishmentId"));
                replenishGoods.setBoxId(rs.getString("boxId"));
                replenishGoods.setBoxDeliveryId(rs.getInt("boxDeliveryId"));
                replenishGoods.setReplenishmentGoodsNumber(rs
                        .getInt("replenishmentGoodsNumber"));
                replenishGoods.setTakeOffGoodsNumber(rs
                        .getInt("takeOffGoodsNumber"));
                replenishGoods.setReplenishType(rs.getInt("replenishType"));
                replenishGoods.setReplenishmentTime(rs
                        .getTimestamp("replenishmentTime"));
                replenishGoodsList.add(replenishGoods);
            }
            return replenishGoodsList;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

}
