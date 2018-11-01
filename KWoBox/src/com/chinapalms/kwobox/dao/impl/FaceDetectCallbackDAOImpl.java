package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.FaceDetectCallbackDAO;
import com.chinapalms.kwobox.javabean.FaceDetectCallback;
import com.chinapalms.kwobox.javabean.OrderCallback;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class FaceDetectCallbackDAOImpl implements FaceDetectCallbackDAO {

    Log log = LogFactory.getLog(FaceDetectCallbackDAOImpl.class);

    @Override
    public FaceDetectCallback findFaceDetectCallbackByCustomerId(int customerId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_facedetect_callback WHERE customerId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        FaceDetectCallback faceDetectCallback = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, customerId);
            rs = ps.executeQuery();
            if (rs.next()) {
                faceDetectCallback = new FaceDetectCallback();
                faceDetectCallback.setCustomerId(rs.getInt("customerId"));
                faceDetectCallback.setFaceDetectCallbackUrl(rs
                        .getString("faceDetectCallbackUrl"));
            }
            return faceDetectCallback;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

}
