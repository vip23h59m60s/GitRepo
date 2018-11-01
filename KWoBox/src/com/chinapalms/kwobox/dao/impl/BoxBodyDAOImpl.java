package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.BoxBodyDAO;
import com.chinapalms.kwobox.javabean.BoxBody;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class BoxBodyDAOImpl implements BoxBodyDAO {

    Log log = LogFactory.getLog(BoxBodyDAOImpl.class);

    @Override
    public BoxBody findBoxBodyByBoxBodyId(int boxBodyId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_box_body WHERE boxBodyId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        BoxBody boxBody = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, boxBodyId);
            rs = ps.executeQuery();
            if (rs.next()) {
                boxBody = new BoxBody();
                boxBody.setBoxBodyId(rs.getInt("boxBodyId"));
                boxBody.setBoxBodyName(rs.getString("boxBodyName"));
                boxBody.setBoxType(rs.getInt("boxType"));
                boxBody.setModel(rs.getString("model"));
                boxBody.setRefrigerationMode(rs.getInt("refrigerationMode"));
                boxBody.setBoxModel(rs.getString("boxModel"));
                boxBody.setBoxMaker(rs.getString("boxMaker"));
                boxBody.setCardgoRoadNumber(rs.getInt("cardgoRoadNumber"));
                boxBody.setFaceFunction(rs.getInt("faceFunction"));
                boxBody.setScreenModel(rs.getString("screenModel"));
                boxBody.setMonitorMaker(rs.getString("monitorMaker"));
                boxBody.setOutsideViewPicture(rs
                        .getString("outsideViewPicture"));
                boxBody.setCardgoRoadPicture(rs.getString("cardgoRoadPicture"));
                boxBody.setBoxManagerId(rs.getString("boxManagerId"));
                boxBody.setReleaseDateTime(rs.getTimestamp("releaseDateTime"));
            }
            return boxBody;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public BoxBody findBoxBodyByBoxBodyModel(String boxBodyModel) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_box_body WHERE model = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        BoxBody boxBody = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, boxBodyModel);
            rs = ps.executeQuery();
            if (rs.next()) {
                boxBody = new BoxBody();
                boxBody.setBoxBodyId(rs.getInt("boxBodyId"));
                boxBody.setBoxBodyName(rs.getString("boxBodyName"));
                boxBody.setBoxType(rs.getInt("boxType"));
                boxBody.setModel(rs.getString("model"));
                boxBody.setRefrigerationMode(rs.getInt("refrigerationMode"));
                boxBody.setBoxModel(rs.getString("boxModel"));
                boxBody.setBoxMaker(rs.getString("boxMaker"));
                boxBody.setCardgoRoadNumber(rs.getInt("cardgoRoadNumber"));
                boxBody.setFaceFunction(rs.getInt("faceFunction"));
                boxBody.setScreenModel(rs.getString("screenModel"));
                boxBody.setMonitorMaker(rs.getString("monitorMaker"));
                boxBody.setOutsideViewPicture(rs
                        .getString("outsideViewPicture"));
                boxBody.setCardgoRoadPicture(rs.getString("cardgoRoadPicture"));
                boxBody.setBoxManagerId(rs.getString("boxManagerId"));
                boxBody.setReleaseDateTime(rs.getTimestamp("releaseDateTime"));
            }
            return boxBody;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

}
