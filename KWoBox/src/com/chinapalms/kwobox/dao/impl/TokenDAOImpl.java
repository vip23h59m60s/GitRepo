package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.TokenDAO;
import com.chinapalms.kwobox.javabean.Token;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class TokenDAOImpl implements TokenDAO {

    Log log = LogFactory.getLog(TokenDAOImpl.class);

    @Override
    public boolean addToken(Token token) {
        boolean addFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "INSERT INTO t_token(requestToken, requestTicket, tokenType, tokenDateTime) VALUES(?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, token.getRequestToken());
            ps.setString(2, token.getRequestTicket());
            ps.setString(3, token.getTokenType());
            ps.setTimestamp(4, new Timestamp(new Date().getTime()));
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
    public boolean updateToken(Token token) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_token SET requestToken = ?, requestTicket = ?, tokenDateTime = ? WHERE tokenType = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, token.getRequestToken());
            ps.setString(2, token.getRequestTicket());
            ps.setTimestamp(3, new Timestamp(new Date().getTime()));
            ps.setString(4, token.getTokenType());
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
    public Token findTokenByTokenType(String tokenType) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_token WHERE tokenType = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        Token token = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, tokenType);
            rs = ps.executeQuery();
            if (rs.next()) {
                token = new Token();
                token.setRequestToken(rs.getString("requestToken"));
                token.setRequestTicket(rs.getString("requestTicket"));
                token.setTokenDateTime(rs.getTimestamp("tokenDateTime"));
                token.setTokenType(rs.getString("tokenType"));
            }
            return token;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

}
