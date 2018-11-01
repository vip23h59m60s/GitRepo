package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.QRCodeContentCustomerDAO;
import com.chinapalms.kwobox.javabean.QRCodeContentCustomer;
import com.chinapalms.kwobox.javabean.Token;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class QRCodeContentCustomerDAOImpl implements QRCodeContentCustomerDAO {

    Log log = LogFactory.getLog(QRCodeContentCustomerDAOImpl.class);

    @Override
    public QRCodeContentCustomer findQRCodeContentCustomerByCustomerId(
            int customerId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_qrcode_content_customer WHERE customerId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        QRCodeContentCustomer qrCodeContentCustomer = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, customerId);
            rs = ps.executeQuery();
            if (rs.next()) {
                qrCodeContentCustomer = new QRCodeContentCustomer();
                qrCodeContentCustomer.setCustomerId(rs.getInt("customerId"));
                qrCodeContentCustomer.setqRCodeContentPrefix(rs
                        .getString("qRCodeContentPrefix"));
            }
            return qrCodeContentCustomer;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

}
