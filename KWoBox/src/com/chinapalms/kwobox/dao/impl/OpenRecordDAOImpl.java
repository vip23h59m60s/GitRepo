package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.OpenRecordDAO;
import com.chinapalms.kwobox.javabean.OpenRecord;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class OpenRecordDAOImpl implements OpenRecordDAO {

    Log log = LogFactory.getLog(OpenRecordDAOImpl.class);

    @Override
    public boolean addOpenRecord(OpenRecord openRecord) {
        boolean addFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "INSERT INTO t_box_open_record(boxId, phoneNumber, customerWorkerId, openWay, openPicture, openTime) VALUES(?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, openRecord.getBoxId());
            ps.setString(2, openRecord.getPhoneNumber());
            ps.setInt(3, openRecord.getCustomerWorkerId());
            ps.setString(4, openRecord.getOpenWay());
            ps.setString(5, openRecord.getOpenPicture());
            ps.setTimestamp(6,
                    new Timestamp(openRecord.getOpenTime().getTime()));
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
