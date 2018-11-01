package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.BoxStructureDAO;
import com.chinapalms.kwobox.javabean.BoxStructure;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class BoxStructureDAOImpl implements BoxStructureDAO {

    Log log = LogFactory.getLog(BoxStructureDAOImpl.class);

    @Override
    public BoxStructure findBoxStructureByStructureId(int structureId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_box_structure WHERE structureId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        BoxStructure boxStructure = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, structureId);
            rs = ps.executeQuery();
            if (rs.next()) {
                boxStructure = new BoxStructure();
                boxStructure.setStructureId(rs.getInt("structureId"));
                boxStructure.setStructureName(rs.getString("structureName"));
                boxStructure.setCalibrationWeight(rs
                        .getInt("calibrationWeight"));
                boxStructure.setWeightError(rs.getInt("weightError"));
                boxStructure.setSteadyStateRange(rs.getInt("steadyStateRange"));
                boxStructure.setMinK(rs.getInt("minK"));
                boxStructure.setMaxK(rs.getInt("maxK"));
                boxStructure.setTotalSensor(rs.getInt("totalSensor"));
                boxStructure.setTotalCardgoroad(rs.getInt("totalCardgoroad"));
                boxStructure.setTotalLayer(rs.getInt("totalLayer"));
                boxStructure.setSensor1(rs.getInt("sensor1"));
                boxStructure.setSensor2(rs.getInt("sensor2"));
                boxStructure.setSensor3(rs.getInt("sensor3"));
                boxStructure.setSensor4(rs.getInt("sensor4"));
                boxStructure.setSensor5(rs.getInt("sensor5"));
                boxStructure.setSensor6(rs.getInt("sensor6"));
                boxStructure.setSensor7(rs.getInt("sensor7"));
                boxStructure.setSensor8(rs.getInt("sensor8"));
                boxStructure.setSensor9(rs.getInt("sensor9"));
                boxStructure.setSensor10(rs.getInt("sensor10"));
                boxStructure.setSensor11(rs.getInt("sensor11"));
                boxStructure.setSensor12(rs.getInt("sensor12"));
                boxStructure.setSensor13(rs.getInt("sensor13"));
                boxStructure.setSensor14(rs.getInt("sensor14"));
                boxStructure.setSensor15(rs.getInt("sensor15"));
                boxStructure.setSensor16(rs.getInt("sensor16"));
                boxStructure.setSensor17(rs.getInt("sensor17"));
                boxStructure.setSensor18(rs.getInt("sensor18"));
                boxStructure.setSensor19(rs.getInt("sensor19"));
                boxStructure.setSensor20(rs.getInt("sensor20"));
                boxStructure.setSensor21(rs.getInt("sensor21"));
                boxStructure.setSensor22(rs.getInt("sensor22"));
                boxStructure.setSensor23(rs.getInt("sensor23"));
                boxStructure.setSensor24(rs.getInt("sensor24"));
                boxStructure.setSensor25(rs.getInt("sensor25"));
                boxStructure.setSensor26(rs.getInt("sensor26"));
                boxStructure.setSensor27(rs.getInt("sensor27"));
                boxStructure.setSensor28(rs.getInt("sensor28"));
                boxStructure.setSensor29(rs.getInt("sensor29"));
                boxStructure.setSensor30(rs.getInt("sensor30"));
                boxStructure.setSensor31(rs.getInt("sensor31"));
                boxStructure.setSensor32(rs.getInt("sensor32"));
                boxStructure.setSensor33(rs.getInt("sensor33"));
                boxStructure.setSensor34(rs.getInt("sensor34"));
                boxStructure.setSensor35(rs.getInt("sensor35"));
                boxStructure.setSensor36(rs.getInt("sensor36"));
                boxStructure.setSensor37(rs.getInt("sensor37"));
                boxStructure.setSensor38(rs.getInt("sensor38"));
                boxStructure.setSensor39(rs.getInt("sensor39"));
                boxStructure.setSensor40(rs.getInt("sensor40"));
                boxStructure.setSensor41(rs.getInt("sensor41"));
                boxStructure.setSensor42(rs.getInt("sensor42"));
                boxStructure.setSensor43(rs.getInt("sensor43"));
                boxStructure.setSensor44(rs.getInt("sensor44"));
                boxStructure.setSensor45(rs.getInt("sensor45"));
                boxStructure.setSensor46(rs.getInt("sensor46"));
                boxStructure.setSensor47(rs.getInt("sensor47"));
                boxStructure.setSensor48(rs.getInt("sensor48"));
                boxStructure.setSensor49(rs.getInt("sensor49"));
                boxStructure.setSensor50(rs.getInt("sensor50"));
                boxStructure.setSensor51(rs.getInt("sensor51"));
                boxStructure.setSensor52(rs.getInt("sensor52"));
                boxStructure.setSensor53(rs.getInt("sensor53"));
                boxStructure.setSensor54(rs.getInt("sensor54"));
                boxStructure.setSensor55(rs.getInt("sensor55"));
                boxStructure.setSensor56(rs.getInt("sensor56"));
                boxStructure.setSensor57(rs.getInt("sensor57"));
                boxStructure.setSensor58(rs.getInt("sensor58"));
                boxStructure.setSensor59(rs.getInt("sensor59"));
                boxStructure.setSensor60(rs.getInt("sensor60"));
                boxStructure.setSensor61(rs.getInt("sensor61"));
                boxStructure.setSensor62(rs.getInt("sensor62"));
                boxStructure.setSensor63(rs.getInt("sensor63"));
                boxStructure.setSensor64(rs.getInt("sensor64"));
                boxStructure.setSensor65(rs.getInt("sensor65"));
            }
            return boxStructure;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public BoxStructure findBoxStructureByStructureName(String structureName) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_box_structure WHERE structureName = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        BoxStructure boxStructure = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, structureName);
            rs = ps.executeQuery();
            if (rs.next()) {
                boxStructure = new BoxStructure();
                boxStructure.setStructureId(rs.getInt("structureId"));
                boxStructure.setStructureName(rs.getString("structureName"));
                boxStructure.setCalibrationWeight(rs
                        .getInt("calibrationWeight"));
                boxStructure.setWeightError(rs.getInt("weightError"));
                boxStructure.setSteadyStateRange(rs.getInt("steadyStateRange"));
                boxStructure.setMinK(rs.getInt("minK"));
                boxStructure.setMaxK(rs.getInt("maxK"));
                boxStructure.setTotalSensor(rs.getInt("totalSensor"));
                boxStructure.setTotalCardgoroad(rs.getInt("totalCardgoroad"));
                boxStructure.setTotalLayer(rs.getInt("totalLayer"));
                boxStructure.setSensor1(rs.getInt("sensor1"));
                boxStructure.setSensor2(rs.getInt("sensor2"));
                boxStructure.setSensor3(rs.getInt("sensor3"));
                boxStructure.setSensor4(rs.getInt("sensor4"));
                boxStructure.setSensor5(rs.getInt("sensor5"));
                boxStructure.setSensor6(rs.getInt("sensor6"));
                boxStructure.setSensor7(rs.getInt("sensor7"));
                boxStructure.setSensor8(rs.getInt("sensor8"));
                boxStructure.setSensor9(rs.getInt("sensor9"));
                boxStructure.setSensor10(rs.getInt("sensor10"));
                boxStructure.setSensor11(rs.getInt("sensor11"));
                boxStructure.setSensor12(rs.getInt("sensor12"));
                boxStructure.setSensor13(rs.getInt("sensor13"));
                boxStructure.setSensor14(rs.getInt("sensor14"));
                boxStructure.setSensor15(rs.getInt("sensor15"));
                boxStructure.setSensor16(rs.getInt("sensor16"));
                boxStructure.setSensor17(rs.getInt("sensor17"));
                boxStructure.setSensor18(rs.getInt("sensor18"));
                boxStructure.setSensor19(rs.getInt("sensor19"));
                boxStructure.setSensor20(rs.getInt("sensor20"));
                boxStructure.setSensor21(rs.getInt("sensor21"));
                boxStructure.setSensor22(rs.getInt("sensor22"));
                boxStructure.setSensor23(rs.getInt("sensor23"));
                boxStructure.setSensor24(rs.getInt("sensor24"));
                boxStructure.setSensor25(rs.getInt("sensor25"));
                boxStructure.setSensor26(rs.getInt("sensor26"));
                boxStructure.setSensor27(rs.getInt("sensor27"));
                boxStructure.setSensor28(rs.getInt("sensor28"));
                boxStructure.setSensor29(rs.getInt("sensor29"));
                boxStructure.setSensor30(rs.getInt("sensor30"));
                boxStructure.setSensor31(rs.getInt("sensor31"));
                boxStructure.setSensor32(rs.getInt("sensor32"));
                boxStructure.setSensor33(rs.getInt("sensor33"));
                boxStructure.setSensor34(rs.getInt("sensor34"));
                boxStructure.setSensor35(rs.getInt("sensor35"));
                boxStructure.setSensor36(rs.getInt("sensor36"));
                boxStructure.setSensor37(rs.getInt("sensor37"));
                boxStructure.setSensor38(rs.getInt("sensor38"));
                boxStructure.setSensor39(rs.getInt("sensor39"));
                boxStructure.setSensor40(rs.getInt("sensor40"));
                boxStructure.setSensor41(rs.getInt("sensor41"));
                boxStructure.setSensor42(rs.getInt("sensor42"));
                boxStructure.setSensor43(rs.getInt("sensor43"));
                boxStructure.setSensor44(rs.getInt("sensor44"));
                boxStructure.setSensor45(rs.getInt("sensor45"));
                boxStructure.setSensor46(rs.getInt("sensor46"));
                boxStructure.setSensor47(rs.getInt("sensor47"));
                boxStructure.setSensor48(rs.getInt("sensor48"));
                boxStructure.setSensor49(rs.getInt("sensor49"));
                boxStructure.setSensor50(rs.getInt("sensor50"));
                boxStructure.setSensor51(rs.getInt("sensor51"));
                boxStructure.setSensor52(rs.getInt("sensor52"));
                boxStructure.setSensor53(rs.getInt("sensor53"));
                boxStructure.setSensor54(rs.getInt("sensor54"));
                boxStructure.setSensor55(rs.getInt("sensor55"));
                boxStructure.setSensor56(rs.getInt("sensor56"));
                boxStructure.setSensor57(rs.getInt("sensor57"));
                boxStructure.setSensor58(rs.getInt("sensor58"));
                boxStructure.setSensor59(rs.getInt("sensor59"));
                boxStructure.setSensor60(rs.getInt("sensor60"));
                boxStructure.setSensor61(rs.getInt("sensor61"));
                boxStructure.setSensor62(rs.getInt("sensor62"));
                boxStructure.setSensor63(rs.getInt("sensor63"));
                boxStructure.setSensor64(rs.getInt("sensor64"));
                boxStructure.setSensor65(rs.getInt("sensor65"));
            }
            return boxStructure;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

}
