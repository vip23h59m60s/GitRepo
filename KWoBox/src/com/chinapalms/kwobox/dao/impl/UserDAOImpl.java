package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.UserDAO;
import com.chinapalms.kwobox.javabean.User;
import com.chinapalms.kwobox.utils.JDBCUtil;
import com.chinapalms.kwobox.utils.ResponseStatus;

public class UserDAOImpl implements UserDAO {

    Log log = LogFactory.getLog(UserDAOImpl.class);

    @Override
    public boolean addUser(User user) {
        boolean addFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "INSERT INTO t_user(phoneNumber, vipLevel, userSource, identityCardNumber, name, sex, birthday, address, wxId, wxOpenId, alipayId, wxCredit, alipayCredit, personalCredit, faceFunction, faceFunctionPassword, faceFunctionPicture, userPoints, userUnReadOrderNumber, registerTime) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            if (user.getPhoneNumber() != null) {
                ps.setString(1, user.getPhoneNumber());
            } else {
                ps.setString(1, "00000000000");
            }
            // if (user.getVipLevel() == 0) {
            ps.setInt(2, 1);
            // } else {
            // ps.setInt(2, user.getVipLevel());
            // }
            if (user.getUserSource() != null) {
                ps.setString(3, user.getUserSource());
            } else {
                ps.setString(3, "Internet");
            }
            if (user.getIdentityCardNumber() != null) {
                ps.setString(4, user.getIdentityCardNumber());
            } else {
                ps.setString(4, "000000000000000000");
            }
            if (user.getName() != null) {
                ps.setString(5, user.getName());
            } else {
                ps.setString(5, "Anonymous");
            }
            ps.setInt(6, user.getSex());
            if (user.getBirthday() != null) {
                ps.setTimestamp(7, new Timestamp(user.getBirthday().getTime()));
            } else {
                ps.setTimestamp(7, new Timestamp(new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss").parse("1970-1-1 00:00:00")
                        .getTime()));
            }
            if (user.getAddress() != null) {
                ps.setString(8, user.getAddress());
            } else {
                ps.setString(8, "Earth");
            }
            if (user.getWxId() != null) {
                ps.setString(9, user.getWxId());
            } else {
                ps.setString(9, ResponseStatus.NO_CONTRACT_ID);
            }
            if (user.getWxOpenId() != null) {
                ps.setString(10, user.getWxOpenId());
            } else {
                ps.setString(10, "noopenid");
            }
            if (user.getAlipayId() != null) {
                ps.setString(11, user.getAlipayId());
            } else {
                ps.setString(11, ResponseStatus.NO_CONTRACT_ID);
            }
            ps.setInt(12, user.getWxCredit());
            ps.setInt(13, user.getAlipayCredit());
            if (user.getPersonalCredit() == 0) {
                ps.setInt(14, ResponseStatus.PERSONAL_CREDIT_BASE_VALUE);
            } else {
                ps.setInt(14, user.getPersonalCredit());
            }
            ps.setInt(15, user.getFaceFunction());
            if (user.getFaceFunctionPassword() != null) {
                ps.setString(16, user.getFaceFunctionPassword());
            } else {
                ps.setString(16, "000000");
            }
            if (user.getFaceFunctionPicture() != null) {
                ps.setString(17, user.getFaceFunctionPicture());
            } else {
                ps.setString(17, "./images/000.png");
            }
            ps.setInt(18, user.getUserPoints());
            ps.setInt(19, user.getUserUnReadOrderNumber());
            if (user.getRegisterTime() != null) {
                ps.setTimestamp(20, new Timestamp(user.getRegisterTime()
                        .getTime()));
            } else {
                ps.setTimestamp(20, new Timestamp(new Date().getTime()));
            }
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
    public boolean deleteUser(User user) {
        boolean deleteFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "DELETE FROM t_user WHERE phoneNumber = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, user.getPhoneNumber());
            int delete = ps.executeUpdate();
            if (delete > 0) {
                deleteFlag = true;
            }
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps);
        }
        return deleteFlag;
    }

    @Override
    public User queryUserByPhoneNumber(String phoneNumber) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_user WHERE phoneNumber = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, phoneNumber);
            rs = ps.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setPhoneNumber(rs.getString("phoneNumber"));
                user.setVipLevel(rs.getInt("vipLevel"));
                user.setUserSource(rs.getString("userSource"));
                user.setIdentityCardNumber(rs.getString("identityCardNumber"));
                user.setName(rs.getString("name"));
                user.setSex(rs.getInt("sex"));
                user.setBirthday(rs.getTimestamp("birthday"));
                user.setAddress(rs.getString("address"));
                user.setWxId(rs.getString("wxId"));
                user.setWxOpenId(rs.getString("wxOpenId"));
                user.setAlipayId(rs.getString("alipayId"));
                user.setWxCredit(rs.getInt("wxCredit"));
                user.setAlipayCredit(rs.getInt("alipayCredit"));
                user.setPersonalCredit(rs.getInt("personalCredit"));
                user.setFaceFunction(rs.getInt("faceFunction"));
                user.setFaceFunctionPassword(rs
                        .getString("faceFunctionPassword"));
                user.setFaceFunctionPicture(rs.getString("faceFunctionPicture"));
                user.setUserPoints(rs.getInt("userPoints"));
                user.setUserUnReadOrderNumber(rs
                        .getInt("userUnReadOrderNumber"));
                user.setRegisterTime(rs.getTimestamp("registerTime"));
            }
            return user;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public boolean updateConstractIdByPhoneNumberAndCustomType(
            String phoneNumber, String customType, String constractId) {
        boolean updateFlag = false;
        if (customType.equals(ResponseStatus.CUSTOM_CATEGORY_WX)) {
            Connection connection = JDBCUtil.getDBConnection();
            String sql = "UPDATE t_user SET wxId = ? WHERE phoneNumber = ?";
            PreparedStatement ps = null;
            try {
                ps = connection.prepareStatement(sql);
                ps.setString(1, constractId);
                ps.setString(2, phoneNumber);
                ps.executeUpdate();
                updateFlag = true;
            } catch (Exception e) {
                log.error("try->catch exception:", e);
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            } finally {
                JDBCUtil.closeDBConnection(connection, ps);
            }
        } else if (customType.equals(ResponseStatus.CUSTOM_CATEGORY_ALIPAY)) {
            Connection connection = JDBCUtil.getDBConnection();
            String sql = "UPDATE t_user SET alipayId = ? WHERE phoneNumber = ?";
            PreparedStatement ps = null;
            try {
                ps = connection.prepareStatement(sql);
                ps.setString(1, constractId);
                ps.setString(2, phoneNumber);
                ps.executeUpdate();
                updateFlag = true;
            } catch (Exception e) {
                log.error("try->catch exception:", e);
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            } finally {
                JDBCUtil.closeDBConnection(connection, ps);
            }
        }
        return updateFlag;
    }

    @Override
    public boolean clearContractId(String contractId, String customType) {
        boolean updateFlag = false;
        if (customType.equals(ResponseStatus.CUSTOM_CATEGORY_WX)) {
            Connection connection = JDBCUtil.getDBConnection();
            String sql = "UPDATE t_user SET wxId = ? WHERE wxId = ?";
            PreparedStatement ps = null;
            try {
                ps = connection.prepareStatement(sql);
                ps.setString(1, ResponseStatus.NO_CONTRACT_ID);
                ps.setString(2, contractId);
                ps.executeUpdate();
                updateFlag = true;
            } catch (Exception e) {
                log.error("try->catch exception:", e);
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            } finally {
                JDBCUtil.closeDBConnection(connection, ps);
            }
        } else if (customType.equals(ResponseStatus.CUSTOM_CATEGORY_ALIPAY)) {
            Connection connection = JDBCUtil.getDBConnection();
            String sql = "UPDATE t_user SET alipayId = ? WHERE alipayId = ?";
            PreparedStatement ps = null;
            try {
                ps = connection.prepareStatement(sql);
                ps.setString(1, ResponseStatus.NO_CONTRACT_ID);
                ps.setString(2, contractId);
                ps.executeUpdate();
                updateFlag = true;
            } catch (Exception e) {
                log.error("try->catch exception:", e);
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            } finally {
                JDBCUtil.closeDBConnection(connection, ps);
            }
        }
        return updateFlag;
    }

    @Override
    public boolean updateFaceFuntion(User user) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_user SET faceFunction = ?, faceFunctionPassword = ?, faceFunctionPicture = ? WHERE phoneNumber = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, user.getFaceFunction());
            ps.setString(2, user.getFaceFunctionPassword());
            ps.setString(3, user.getFaceFunctionPicture());
            ps.setString(4, user.getPhoneNumber());
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
    public boolean updatePersonalCreditByPhoneNumber(String phoneNumber,
            int creditChangeValue) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_user SET personalCredit = personalCredit + ? WHERE phoneNumber = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, creditChangeValue);
            ps.setString(2, phoneNumber);
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
    public boolean updateUserPointsByPhoneNumber(String phoneNumber,
            int pointsChangeValue) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_user SET userPoints = userPoints + ? WHERE phoneNumber = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, pointsChangeValue);
            ps.setString(2, phoneNumber);
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
    public boolean updateUserUnReadOrderNumber(String phoneNumber,
            int changeValue) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = null;
        if (changeValue == 0) {
            sql = "UPDATE t_user SET userUnReadOrderNumber = ? WHERE phoneNumber = ?";
        } else {
            sql = "UPDATE t_user SET userUnReadOrderNumber = userUnReadOrderNumber + ? WHERE phoneNumber = ?";
        }
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, changeValue);
            ps.setString(2, phoneNumber);
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
    public boolean updateOpendIdByPhoneNumber(String phoneNumber, String openId) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_user SET wxOpenId = ? WHERE phoneNumber = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, openId);
            ps.setString(2, phoneNumber);
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

}
