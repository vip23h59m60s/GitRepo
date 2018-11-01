package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.SenderEmailDAO;
import com.chinapalms.kwobox.javabean.BoxShopes;
import com.chinapalms.kwobox.javabean.SenderEmail;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class SenderEmailDAOImpl implements SenderEmailDAO {

    Log log = LogFactory.getLog(SenderEmailDAOImpl.class);

    @Override
    public List<SenderEmail> findSenderEmails() {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_sender_email";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<SenderEmail> senderEmailsList = new ArrayList<SenderEmail>();
        SenderEmail senderEmail = null;
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                senderEmail = new SenderEmail();
                senderEmail.setEmailAccount(rs.getString("emailAccount"));
                senderEmail.setEmailPassword(rs.getString("emailPassword"));
                senderEmailsList.add(senderEmail);
            }
            return senderEmailsList;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

}
