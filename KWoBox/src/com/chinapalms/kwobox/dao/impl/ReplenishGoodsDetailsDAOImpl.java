package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.ReplenishGoodsDetailsDAO;
import com.chinapalms.kwobox.javabean.ReplenishGoodsDetails;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class ReplenishGoodsDetailsDAOImpl implements ReplenishGoodsDetailsDAO {

    Log log = LogFactory.getLog(ReplenishGoodsDetailsDAOImpl.class);

    @Override
    public boolean addReplenishGoodsDetails(
            ReplenishGoodsDetails replenishGoodsDetails) {
        boolean addFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "INSERT INTO t_replenishment_details(replenishmentId, barCodeId, goodsName, goodsNumber, replenishmentState) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, replenishGoodsDetails.getReplenishmentId());
            ps.setString(2, replenishGoodsDetails.getBarCodeId());
            ps.setString(3, replenishGoodsDetails.getGoodsName());
            ps.setInt(4, replenishGoodsDetails.getGoodsNumber());
            ps.setInt(5, replenishGoodsDetails.getReplenishmentState());
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
    public List<ReplenishGoodsDetails> findReplenishGoodsDetailsByReplenishId(
            String replenishId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_replenishment_details WHERE replenishmentId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ReplenishGoodsDetails> replenishGoodsDetailsList = new ArrayList<ReplenishGoodsDetails>();
        ReplenishGoodsDetails replenishGoodsDetails = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, replenishId);
            rs = ps.executeQuery();
            while (rs.next()) {
                replenishGoodsDetails = new ReplenishGoodsDetails();
                replenishGoodsDetails.setReplenishmentId(rs
                        .getString("replenishmentId"));
                replenishGoodsDetails.setBarCodeId(rs.getString("barCodeId"));
                replenishGoodsDetails.setGoodsName(rs.getString("goodsName"));
                replenishGoodsDetails.setGoodsNumber(rs.getInt("goodsNumber"));
                replenishGoodsDetails.setReplenishmentState(rs
                        .getInt("replenishmentState"));
                replenishGoodsDetailsList.add(replenishGoodsDetails);
            }
            return replenishGoodsDetailsList;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

}
