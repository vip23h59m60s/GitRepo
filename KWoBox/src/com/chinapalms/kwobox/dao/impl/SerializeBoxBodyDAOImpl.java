package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import com.chinapalms.kwobox.dao.SerializeBoxBodyDAO;
import com.chinapalms.kwobox.javabean.SerializeBoxBody;
import com.chinapalms.kwobox.utils.JDBCUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SerializeBoxBodyDAOImpl implements SerializeBoxBodyDAO {

    Log log = LogFactory.getLog(SerializeBoxBodyDAOImpl.class);

    @Override
    public boolean addSerializeBoxBody(SerializeBoxBody serializeBoxBody) {
        boolean addFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "INSERT INTO t_serialize_boxbody(boxBodySnNumber, boxBodyId, structureId, workerId, updateTime) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, serializeBoxBody.getBoxBodySnNumber());
            ps.setInt(2, serializeBoxBody.getBoxBodyId());
            ps.setInt(3, serializeBoxBody.getStructureId());
            ps.setInt(4, serializeBoxBody.getWorkerId());
            ps.setTimestamp(5, new Timestamp(serializeBoxBody.getUpdateTime()
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

    @Override
    public SerializeBoxBody findSerializeBoxBodyByBoxBodySnNumber(
            String boxBodySnNumber) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_serialize_boxbody WHERE boxBodySnNumber = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        SerializeBoxBody serializeBoxBody = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, boxBodySnNumber);
            rs = ps.executeQuery();
            if (rs.next()) {
                serializeBoxBody = new SerializeBoxBody();
                serializeBoxBody.setSerializeBoxBodyId(rs
                        .getInt("serializeBoxBodyId"));
                serializeBoxBody.setBoxBodySnNumber(rs
                        .getString("boxBodySnNumber"));
                serializeBoxBody.setBoxBodyId(rs.getInt("boxBodyId"));
                serializeBoxBody.setStructureId(rs.getInt("structureId"));
                serializeBoxBody.setWorkerId(rs.getInt("workerId"));
                serializeBoxBody.setUpdateTime(rs.getTimestamp("updateTime"));
            }
            return serializeBoxBody;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public boolean updateSerializeBoxBody(SerializeBoxBody serializeBoxBody) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_serialize_boxbody SET structureId = ?, workerId = ?, updateTime = ? WHERE boxBodySnNumber = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, serializeBoxBody.getStructureId());
            ps.setInt(2, serializeBoxBody.getWorkerId());
            ps.setTimestamp(3, new Timestamp(serializeBoxBody.getUpdateTime()
                    .getTime()));
            ps.setString(4, serializeBoxBody.getBoxBodySnNumber());
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
    public SerializeBoxBody findSerializeBoxBodyBySerializeBoxBodyId(
            int serializeBoxBodyId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_serialize_boxbody WHERE serializeBoxBodyId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        SerializeBoxBody serializeBoxBody = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, serializeBoxBodyId);
            rs = ps.executeQuery();
            if (rs.next()) {
                serializeBoxBody = new SerializeBoxBody();
                serializeBoxBody.setSerializeBoxBodyId(rs
                        .getInt("serializeBoxBodyId"));
                serializeBoxBody.setBoxBodySnNumber(rs
                        .getString("boxBodySnNumber"));
                serializeBoxBody.setBoxBodyId(rs.getInt("boxBodyId"));
                serializeBoxBody.setStructureId(rs.getInt("structureId"));
                serializeBoxBody.setWorkerId(rs.getInt("workerId"));
                serializeBoxBody.setUpdateTime(rs.getTimestamp("updateTime"));
            }
            return serializeBoxBody;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

}
