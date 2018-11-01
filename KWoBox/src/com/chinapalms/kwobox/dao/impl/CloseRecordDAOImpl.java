package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.CloseRecordDAO;
import com.chinapalms.kwobox.javabean.CloseRecord;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class CloseRecordDAOImpl implements CloseRecordDAO {

    Log log = LogFactory.getLog(CloseRecordDAOImpl.class);

    @Override
    public boolean addCloseRecord(CloseRecord closeRecord) {
        boolean addFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "INSERT INTO t_box_close_record(boxId, phoneNumber, customerWorkerId, closePicture, closeTime) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, closeRecord.getBoxId());
            ps.setString(2, closeRecord.getPhoneNumber());
            ps.setInt(3, closeRecord.getCustomerWorkerId());
            ps.setString(4, closeRecord.getClosePicture());
            ps.setTimestamp(5, new Timestamp(closeRecord.getCloseTime()
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
