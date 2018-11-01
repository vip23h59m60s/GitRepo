package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.BoxShopesDAO;
import com.chinapalms.kwobox.javabean.BoxShopes;
import com.chinapalms.kwobox.javabean.Boxes;
import com.chinapalms.kwobox.utils.JDBCUtil;
import com.chinapalms.kwobox.utils.ResponseStatus;

public class BoxShopesDAOImpl implements BoxShopesDAO {

    Log log = LogFactory.getLog(BoxShopesDAOImpl.class);

    @Override
    public BoxShopes findBoxShopByShopId(int shopId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_box_shops WHERE shopId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        BoxShopes boxShop = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, shopId);
            rs = ps.executeQuery();
            if (rs.next()) {
                boxShop = new BoxShopes();
                boxShop.setShopId(rs.getInt("shopId"));
                boxShop.setShopName(rs.getString("shopName"));
                boxShop.setCarrieroperator(rs.getString("carrieroperator"));
                boxShop.setEnvironmentPicture(rs.getString("equipmentDealer"));
                boxShop.setSiteDealer(rs.getString("siteDealer"));
                boxShop.setAddress(rs.getString("address"));
                boxShop.setEnvironmentPicture(rs
                        .getString("environmentPicture"));
                boxShop.setShopType(rs.getInt("shopType"));
                boxShop.setSiteType(rs.getInt("siteType"));
                boxShop.setOpenTime(rs.getTimestamp("openTime"));
                boxShop.setCloseTime(rs.getTimestamp("closeTime"));
                boxShop.setCustomerId(rs.getInt("customerId"));
                boxShop.setShopCreateTime(rs.getTimestamp("shopCreateTime"));
            }
            return boxShop;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public List<BoxShopes> findBoxShopesByCustomerId(int customerId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_box_shops WHERE customerId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<BoxShopes> boxeShopesList = new ArrayList<BoxShopes>();
        BoxShopes boxShop = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, customerId);
            rs = ps.executeQuery();
            while (rs.next()) {
                boxShop = new BoxShopes();
                boxShop.setShopId(rs.getInt("shopId"));
                boxShop.setShopName(rs.getString("shopName"));
                boxShop.setCarrieroperator(rs.getString("carrieroperator"));
                boxShop.setEnvironmentPicture(rs.getString("equipmentDealer"));
                boxShop.setSiteDealer(rs.getString("siteDealer"));
                boxShop.setAddress(rs.getString("address"));
                boxShop.setEnvironmentPicture(rs
                        .getString("environmentPicture"));
                boxShop.setShopType(rs.getInt("shopType"));
                boxShop.setSiteType(rs.getInt("siteType"));
                boxShop.setOpenTime(rs.getTimestamp("openTime"));
                boxShop.setCloseTime(rs.getTimestamp("closeTime"));
                boxShop.setCustomerId(rs.getInt("customerId"));
                boxShop.setShopCreateTime(rs.getTimestamp("shopCreateTime"));
                boxeShopesList.add(boxShop);
            }
            return boxeShopesList;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

}
