package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.GoodsAverageWeightDAO;
import com.chinapalms.kwobox.javabean.GoodsAverageWeight;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class GoodsAverageWeightDAOImpl implements GoodsAverageWeightDAO {

    Log log = LogFactory.getLog(GoodsAverageWeightDAOImpl.class);

    @Override
    public boolean addGoodsWeight(GoodsAverageWeight goodsWeight) {
        boolean addFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "INSERT INTO t_goods_average_weight(boxId, barCodeId, goodsWeight, goodsNumber, averageWeight, updateDateTime) VALUES(?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, goodsWeight.getBoxId());
            ps.setString(2, goodsWeight.getBarCodeId());
            ps.setBigDecimal(3, goodsWeight.getGoodsWeight());
            ps.setInt(4, goodsWeight.getGoodsNumber());
            ps.setBigDecimal(5, goodsWeight.getAverageWeight());
            ps.setTimestamp(6, new Timestamp(goodsWeight.getUpdateDateTime()
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
