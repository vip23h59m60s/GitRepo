package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.ExceptionShoppingDAO;
import com.chinapalms.kwobox.javabean.Customer;
import com.chinapalms.kwobox.javabean.ExceptionShopping;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class ExceptionShoppingDAOImpl implements ExceptionShoppingDAO {

    Log log = LogFactory.getLog(ExceptionShoppingDAOImpl.class);

    @Override
    public boolean addExceptionShopping(ExceptionShopping exceptionShopping) {
        boolean addFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "INSERT INTO t_exception_shopping(exceptionId, exceptionShoppingInfo, boxId, phoneNumber, state, handleTime, exceptionTime) VALUES(?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, exceptionShopping.getExceptionId());
            ps.setString(2, exceptionShopping.getExceptionShoppingInfo());
            ps.setString(3, exceptionShopping.getBoxId());
            ps.setString(4, exceptionShopping.getPhoneNumber());
            ps.setInt(5, exceptionShopping.getState());
            ps.setTimestamp(6, new Timestamp(exceptionShopping.getHandleTime()
                    .getTime()));
            ps.setTimestamp(7, new Timestamp(exceptionShopping
                    .getExceptionTime().getTime()));
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
    public boolean updateExceptionShoppingState(
            ExceptionShopping exceptionShopping) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_exception_shopping SET state = ?, handleTime = ? WHERE exceptionId = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, exceptionShopping.getState());
            ps.setTimestamp(2, new Timestamp(exceptionShopping.getHandleTime()
                    .getTime()));
            ps.setString(3, exceptionShopping.getExceptionId());
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
    public List<ExceptionShopping> findExceptionShoppingsByState(int state) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_exception_shopping where state = ? ORDER by exceptionTime DESC";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ExceptionShopping> exceptionList = new ArrayList<ExceptionShopping>();
        ExceptionShopping exceptionShopping = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, state);
            rs = ps.executeQuery();
            while (rs.next()) {
                exceptionShopping = new ExceptionShopping();
                exceptionShopping.setExceptionId(rs.getString("exceptionId"));
                exceptionShopping.setExceptionShoppingInfo(rs
                        .getString("exceptionShoppingInfo"));
                exceptionShopping.setBoxId(rs.getString("boxId"));
                exceptionShopping.setPhoneNumber(rs.getString("phoneNumber"));
                exceptionShopping.setState(rs.getInt("state"));
                exceptionShopping.setHandleTime(rs.getTimestamp("handleTime"));
                exceptionShopping.setExceptionTime(rs
                        .getTimestamp("exceptionTime"));
                exceptionList.add(exceptionShopping);
            }
            return exceptionList;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

}
