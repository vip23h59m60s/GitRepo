package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.GoodsActualWeightDAO;
import com.chinapalms.kwobox.javabean.GoodsActualWeight;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class GoodsActualWeightDAOImpl implements GoodsActualWeightDAO {

    Log log = LogFactory.getLog(GoodsActualWeightDAOImpl.class);

    @Override
    public boolean addGoodsActualWeight(GoodsActualWeight goodsActualWeight) {
        boolean addFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "INSERT INTO t_goods_actual_weight(boxId, barCodeId, goodsWeight, updateDateTime) VALUES(?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, goodsActualWeight.getBoxId());
            ps.setString(2, goodsActualWeight.getBarCodeId());
            ps.setBigDecimal(3, goodsActualWeight.getGoodsWeight());
            ps.setTimestamp(4, new Timestamp(goodsActualWeight
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

}
