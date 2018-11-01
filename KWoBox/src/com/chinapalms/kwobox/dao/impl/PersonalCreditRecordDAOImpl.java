package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.PersonalCreditRecordDAO;
import com.chinapalms.kwobox.javabean.Boxes;
import com.chinapalms.kwobox.javabean.Order;
import com.chinapalms.kwobox.javabean.PersonalCreditRecord;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class PersonalCreditRecordDAOImpl implements PersonalCreditRecordDAO {

    Log log = LogFactory.getLog(PersonalCreditRecordDAOImpl.class);

    @Override
    public boolean addPersonalCreditRecord(
            PersonalCreditRecord personalCreditRecord) {
        boolean addFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "INSERT INTO t_personal_credit_record(phoneNumber, boxId, changeReason, changeValue, changeTime) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, personalCreditRecord.getPhoneNumber());
            ps.setString(2, personalCreditRecord.getBoxId());
            ps.setInt(3, personalCreditRecord.getChangeReason());
            ps.setInt(4, personalCreditRecord.getChangeValue());
            ps.setTimestamp(5, new Timestamp(personalCreditRecord
                    .getChangeTime().getTime()));
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
    public List<PersonalCreditRecord> findPersonalCreditRecordsByPhoneNumber(
            String phoneNumber) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_personal_credit_record WHERE phoneNumber = ? ORDER BY changeTime DESC";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<PersonalCreditRecord> personalCreditRecordsList = new ArrayList<PersonalCreditRecord>();
        PersonalCreditRecord personalCreditRecord = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, phoneNumber);
            rs = ps.executeQuery();
            while (rs.next()) {
                personalCreditRecord = new PersonalCreditRecord();
                personalCreditRecord
                        .setPhoneNumber(rs.getString("phoneNumber"));
                personalCreditRecord.setBoxId(rs.getString("boxId"));
                personalCreditRecord.setChangeReason(rs.getInt("changeReason"));
                personalCreditRecord.setChangeValue(rs.getInt("changeValue"));
                personalCreditRecord.setChangeTime(rs
                        .getTimestamp("changeTime"));
                personalCreditRecordsList.add(personalCreditRecord);
            }
            return personalCreditRecordsList;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public int findPersionalCreditRecordsCountByPhoneNumber(String phoneNumber) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT COUNT(*) AS creditRecordCount FROM t_personal_credit_record WHERE phoneNumber = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        int orderCount = 0;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, phoneNumber);
            rs = ps.executeQuery();
            if (rs.next()) {
                orderCount = rs.getInt("creditRecordCount");
            }
            return orderCount;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public List<PersonalCreditRecord> findCreditRecordsByPhoneNumberAndPageNumber(
            String phoneNumber, int pageNumber) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_personal_credit_record WHERE phoneNumber = ? ORDER BY changeTime DESC LIMIT ? , ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<PersonalCreditRecord> personalCreditRecordsList = new ArrayList<PersonalCreditRecord>();
        PersonalCreditRecord personalCreditRecord = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, phoneNumber);
            ps.setInt(2, (pageNumber - 1) * PersonalCreditRecord.PAGE_SIZE);
            ps.setInt(3, PersonalCreditRecord.PAGE_SIZE);
            rs = ps.executeQuery();
            while (rs.next()) {
                personalCreditRecord = new PersonalCreditRecord();
                personalCreditRecord
                        .setPhoneNumber(rs.getString("phoneNumber"));
                personalCreditRecord.setBoxId(rs.getString("boxId"));
                personalCreditRecord.setChangeReason(rs.getInt("changeReason"));
                personalCreditRecord.setChangeValue(rs.getInt("changeValue"));
                personalCreditRecord.setChangeTime(rs
                        .getTimestamp("changeTime"));
                personalCreditRecordsList.add(personalCreditRecord);
            }
            return personalCreditRecordsList;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

}
