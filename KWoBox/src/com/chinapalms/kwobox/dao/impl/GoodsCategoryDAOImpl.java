package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.GoodsCategoryDAO;
import com.chinapalms.kwobox.javabean.GoodsCategory;
import com.chinapalms.kwobox.utils.JDBCUtil;

public class GoodsCategoryDAOImpl implements GoodsCategoryDAO {

    Log log = LogFactory.getLog(GoodsCategoryDAOImpl.class);

    @Override
    public GoodsCategory findGoodsCategoryByBarCodeId(String barCodeId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_goods_category WHERE barCodeId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        GoodsCategory goodsCategory = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, barCodeId);
            rs = ps.executeQuery();
            if (rs.next()) {
                goodsCategory = new GoodsCategory();
                goodsCategory.setBarCodeId(rs.getString("barCodeId"));
                goodsCategory.setGoodsName(rs.getString("goodsName"));
                goodsCategory.setBrandCompany(rs.getString("brandCompany"));
                goodsCategory.setNationalStandard(rs
                        .getString("nationalStandard"));
                goodsCategory.setSalesMode(rs.getInt("salesMode"));
                goodsCategory.setGoodsSpec(rs.getInt("goodsSpec"));
                goodsCategory.setWeight(rs.getInt("weight"));
                goodsCategory.setWeightError(rs.getInt("weightError"));
                goodsCategory.setGoodsUnit(rs.getInt("goodsUnit"));
                goodsCategory.setPackageType(rs.getString("packageType"));
                goodsCategory.setLength(rs.getInt("length"));
                goodsCategory.setWidth(rs.getInt("width"));
                goodsCategory.setHeight(rs.getInt("height"));
                goodsCategory.setTradeMark(rs.getString("tradeMark"));
                goodsCategory.setStorageCondition(rs
                        .getString("storageCondition"));
                goodsCategory.setExpiryTime(rs.getInt("expiryTime"));
                goodsCategory.setGoodsPicture(rs.getString("goodsPicture"));
                goodsCategory.setGoodsPurchasePrice(rs
                        .getBigDecimal("goodsPurchasePrice"));
                goodsCategory.setRecommendedRetailPrice(rs
                        .getBigDecimal("recommendedRetailPrice"));
                goodsCategory.setGoodsManager(rs.getInt("goodsManager"));
                goodsCategory.setStorageTime(rs.getTimestamp("storageTime"));
            }
            return goodsCategory;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public List<GoodsCategory> findAllGoodsCategory() {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_goods_category";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<GoodsCategory> goodsCategoryList = new ArrayList<GoodsCategory>();
        GoodsCategory goodsCategory = null;
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                goodsCategory = new GoodsCategory();
                goodsCategory.setBarCodeId(rs.getString("barCodeId"));
                goodsCategory.setGoodsName(rs.getString("goodsName"));
                goodsCategory.setBrandCompany(rs.getString("brandCompany"));
                goodsCategory.setNationalStandard(rs
                        .getString("nationalStandard"));
                goodsCategory.setSalesMode(rs.getInt("salesMode"));
                goodsCategory.setGoodsSpec(rs.getInt("goodsSpec"));
                goodsCategory.setWeight(rs.getInt("weight"));
                goodsCategory.setWeightError(rs.getInt("weightError"));
                goodsCategory.setGoodsUnit(rs.getInt("goodsUnit"));
                goodsCategory.setPackageType(rs.getString("packageType"));
                goodsCategory.setLength(rs.getInt("length"));
                goodsCategory.setWidth(rs.getInt("width"));
                goodsCategory.setHeight(rs.getInt("height"));
                goodsCategory.setTradeMark(rs.getString("tradeMark"));
                goodsCategory.setStorageCondition(rs
                        .getString("storageCondition"));
                goodsCategory.setExpiryTime(rs.getInt("expiryTime"));
                goodsCategory.setGoodsPicture(rs.getString("goodsPicture"));
                goodsCategory.setGoodsPurchasePrice(rs
                        .getBigDecimal("goodsPurchasePrice"));
                goodsCategory.setRecommendedRetailPrice(rs
                        .getBigDecimal("recommendedRetailPrice"));
                goodsCategory.setGoodsManager(rs.getInt("goodsManager"));
                goodsCategory.setStorageTime(rs.getTimestamp("storageTime"));
                goodsCategoryList.add(goodsCategory);
            }
            return goodsCategoryList;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

}
