package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.WeightSensorStatusDAO;
import com.chinapalms.kwobox.javabean.WeightSensorStatus;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class WeightSensorStatusDAOImpl implements WeightSensorStatusDAO {

    Log log = LogFactory.getLog(WeightSensorStatusDAOImpl.class);

    @Override
    public boolean addWeightSensorStatus(
            WeightSensorStatus weightSensorStatus) {
        boolean addFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "INSERT INTO t_weight_sensor_status(boxId, state, reportTime) VALUES(?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, weightSensorStatus.getBoxId());
            ps.setInt(2, weightSensorStatus.getState());
            ps.setTimestamp(3, new Timestamp(weightSensorStatus.getReportTime()
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
