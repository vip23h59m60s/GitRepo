package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.RollingAdPlatformDAO;
import com.chinapalms.kwobox.javabean.RollingAdBox;
import com.chinapalms.kwobox.javabean.RollingAdPlatform;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class RollingAdPlatformImpl implements RollingAdPlatformDAO {

    Log log = LogFactory.getLog(RollingAdPlatformImpl.class);

    @Override
    public RollingAdPlatform findRollingAdPlatform() {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_rolling_ad_platform";
        PreparedStatement ps = null;
        ResultSet rs = null;
        RollingAdPlatform rollingAdPlatform = null;
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                rollingAdPlatform = new RollingAdPlatform();
                rollingAdPlatform.setPlatformAdId(rs.getInt("platformAdId"));
                rollingAdPlatform.setPlatformAdName(rs
                        .getString("platformAdName"));
                rollingAdPlatform.setPlatformAdUrl(rs
                        .getString("platformAdUrl"));
                rollingAdPlatform.setStartTime(rs.getTimestamp("startTime"));
                rollingAdPlatform.setEndTime(rs.getTimestamp("endTime"));
                rollingAdPlatform.setAdState(rs.getInt("adState"));
            }
            return rollingAdPlatform;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

}
