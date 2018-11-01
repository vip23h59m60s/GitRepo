package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.util.logging.resources.logging;

import com.chinapalms.kwobox.dao.RollingAdBoxDAO;
import com.chinapalms.kwobox.javabean.RollingAdBox;
import com.chinapalms.kwobox.javabean.SerializeBoxBody;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class RollingAdBoxDAOImpl implements RollingAdBoxDAO {

    Log log = LogFactory.getLog(RollingAdBoxDAOImpl.class);

    @Override
    public RollingAdBox findRollingAdBoxByBoxId(String boxId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_rolling_ad_box WHERE boxId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        RollingAdBox rollingAdBox = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, boxId);
            rs = ps.executeQuery();
            if (rs.next()) {
                rollingAdBox = new RollingAdBox();
                rollingAdBox.setBoxAdId(rs.getInt("boxAdId"));
                rollingAdBox.setBoxAdName(rs.getString("boxAdName"));
                rollingAdBox.setBoxAdUrl(rs.getString("boxAdUrl"));
                rollingAdBox.setBoxId(rs.getString("boxId"));
                rollingAdBox.setStartTime(rs.getTimestamp("startTime"));
                rollingAdBox.setEndTime(rs.getTimestamp("endTime"));
                rollingAdBox.setAdState(rs.getInt("adState"));
            }
            return rollingAdBox;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

}
