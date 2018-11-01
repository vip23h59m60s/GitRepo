package com.chinapalms.kwobox.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.BoxesBoxManagerDAO;
import com.chinapalms.kwobox.dao.BoxesDAO;
import com.chinapalms.kwobox.javabean.BoxManager;
import com.chinapalms.kwobox.javabean.Boxes;
import com.chinapalms.kwobox.utils.JDBCUtil;
import com.chinapalms.kwobox.utils.ResponseStatus;

public class BoxesDAOImpl implements BoxesDAO, BoxesBoxManagerDAO {

    Log log = LogFactory.getLog(BoxesDAOImpl.class);

    @Override
    public Boxes findBoxesByBoxId(String boxId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_boxes WHERE boxId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        Boxes boxes = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, boxId);
            rs = ps.executeQuery();
            if (rs.next()) {
                boxes = new Boxes();
                boxes.setBoxId(rs.getString("boxId"));
                boxes.setBoxName(rs.getString("boxName"));
                boxes.setBoxBodyId(rs.getInt("boxBodyId"));
                boxes.setSerializeBoxBodyId(rs.getInt("serializeBoxBodyId"));
                boxes.setCustomerId(rs.getInt("customerId"));
                boxes.setIcsId(rs.getInt("icsId"));
                boxes.setBoxAddress(rs.getString("boxAddress"));
                boxes.setLatitude(rs.getString("latitude"));
                boxes.setLongitude(rs.getString("longitude"));
                boxes.setLocationClassification(rs
                        .getInt("locationClassification"));
                boxes.setNetworkType(rs.getInt("networkType"));
                boxes.setPlaceEnvironment(rs.getInt("placeEnvironment"));
                boxes.setIsOpen(rs.getInt("isOpen"));
                boxes.setEnvironmentPicture(rs.getString("environmentPicture"));
                boxes.setBoxManagerId(rs.getInt("boxManagerId"));
                boxes.setBoxState(rs.getInt("boxState"));
                boxes.setShopId(rs.getInt("shopId"));
                boxes.setPlaceDateTime(rs.getTimestamp("placeDateTime"));
            }
            return boxes;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public Boxes findBoxesByIcsId(int icsId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_boxes WHERE icsId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        Boxes boxes = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, icsId);
            rs = ps.executeQuery();
            if (rs.next()) {
                boxes = new Boxes();
                boxes.setBoxId(rs.getString("boxId"));
                boxes.setBoxName(rs.getString("boxName"));
                boxes.setBoxBodyId(rs.getInt("boxBodyId"));
                boxes.setSerializeBoxBodyId(rs.getInt("serializeBoxBodyId"));
                boxes.setCustomerId(rs.getInt("customerId"));
                boxes.setIcsId(rs.getInt("icsId"));
                boxes.setBoxAddress(rs.getString("boxAddress"));
                boxes.setLatitude(rs.getString("latitude"));
                boxes.setLongitude(rs.getString("longitude"));
                boxes.setLocationClassification(rs
                        .getInt("locationClassification"));
                boxes.setNetworkType(rs.getInt("networkType"));
                boxes.setPlaceEnvironment(rs.getInt("placeEnvironment"));
                boxes.setIsOpen(rs.getInt("isOpen"));
                boxes.setEnvironmentPicture(rs.getString("environmentPicture"));
                boxes.setBoxManagerId(rs.getInt("boxManagerId"));
                boxes.setBoxState(rs.getInt("boxState"));
                boxes.setShopId(rs.getInt("shopId"));
                boxes.setPlaceDateTime(rs.getTimestamp("placeDateTime"));
            }
            return boxes;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public List<Boxes> findNearByBoxes(String fromLat, String fromLng,
            double nearByRange, int pageNumber) {
        Connection connection = JDBCUtil.getDBConnection();
        log.info("findNearByBoxes:formLat=" + fromLat + ",fromLng=" + fromLng
                + ",nearByRange=" + nearByRange + ",pageNumber=" + pageNumber);
        // 计算当前位置和数据库位置两个点之前的距离
        // order by 和 limit共用会出现奇葩问题，暂时去掉order by
        // String sql =
        // "SELECT * FROM (SELECT *,(ROUND(6378138 * 2 * ASIN(SQRT(POW(SIN(((latitude * PI()) / 180 - (? * PI()) / 180) / 2), 2) + COS((? * PI()) / 180) * COS((latitude * PI()) / 180) * POW(SIN(((longitude * PI()) / 180 - (? * PI()) / 180) / 2), 2))))) AS distance FROM `t_boxes`) t WHERE t.distance <= ? ORDER BY distance ASC LIMIT ? , ?";
        String sql = "SELECT * FROM (SELECT *,(ROUND(6378138 * 2 * ASIN(SQRT(POW(SIN(((latitude * PI()) / 180 - (? * PI()) / 180) / 2), 2) + COS((? * PI()) / 180) * COS((latitude * PI()) / 180) * POW(SIN(((longitude * PI()) / 180 - (? * PI()) / 180) / 2), 2))))) AS distance FROM `t_boxes`) t WHERE t.distance <= ? AND t.boxState = ? LIMIT ? , ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Boxes> boxesList = new ArrayList<Boxes>();
        Boxes boxes = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, fromLat);
            ps.setString(2, fromLat);
            ps.setString(3, fromLng);
            ps.setDouble(4, nearByRange);
            ps.setInt(5, ResponseStatus.BOX_STATE_FINISHED_MERCHANT_DEPLOYMENT);
            ps.setInt(6, (pageNumber - 1) * Boxes.NEARBY_BOXES_PAGE_SIZE);
            ps.setInt(7, Boxes.NEARBY_BOXES_PAGE_SIZE);
            rs = ps.executeQuery();
            while (rs.next()) {
                boxes = new Boxes();
                boxes.setBoxId(rs.getString("boxId"));
                boxes.setBoxName(rs.getString("boxName"));
                boxes.setBoxBodyId(rs.getInt("boxBodyId"));
                boxes.setSerializeBoxBodyId(rs.getInt("serializeBoxBodyId"));
                boxes.setCustomerId(rs.getInt("customerId"));
                boxes.setIcsId(rs.getInt("icsId"));
                boxes.setBoxAddress(rs.getString("boxAddress"));
                boxes.setLatitude(rs.getString("latitude"));
                boxes.setLongitude(rs.getString("longitude"));
                boxes.setDistance(rs.getDouble("distance"));
                boxes.setLocationClassification(rs
                        .getInt("locationClassification"));
                boxes.setNetworkType(rs.getInt("networkType"));
                boxes.setPlaceEnvironment(rs.getInt("placeEnvironment"));
                boxes.setIsOpen(rs.getInt("isOpen"));
                boxes.setEnvironmentPicture(rs.getString("environmentPicture"));
                boxes.setBoxManagerId(rs.getInt("boxManagerId"));
                boxes.setBoxState(rs.getInt("boxState"));
                boxes.setShopId(rs.getInt("shopId"));
                boxes.setPlaceDateTime(rs.getTimestamp("placeDateTime"));
                boxesList.add(boxes);
            }
            return boxesList;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public int findNearByBoxesCount(String fromLat, String fromLng,
            double nearByRange) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT COUNT(*) nearByBoxesCount FROM (SELECT *,(ROUND(6378138 * 2 * ASIN(SQRT(POW(SIN(((latitude * PI()) / 180 - (? * PI()) / 180) / 2), 2) + COS((? * PI()) / 180) * COS((latitude * PI()) / 180) * POW(SIN(((longitude * PI()) / 180 - (? * PI()) / 180) / 2), 2))))) AS distance FROM `t_boxes`) t WHERE t.distance <= ? AND t.boxState = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        int boxManagerCount = 0;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, fromLat);
            ps.setString(2, fromLat);
            ps.setString(3, fromLng);
            ps.setDouble(4, nearByRange);
            ps.setInt(5, ResponseStatus.BOX_STATE_FINISHED_MERCHANT_DEPLOYMENT);
            rs = ps.executeQuery();
            if (rs.next()) {
                boxManagerCount = rs.getInt("nearByBoxesCount");
            }
            return boxManagerCount;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public boolean addBoxes(Boxes boxes) {
        boolean addFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "INSERT INTO t_boxes(boxId, boxBodyId, serializeBoxBodyId, icsId, boxState, placeDateTime) VALUES(?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, boxes.getBoxId());
            ps.setInt(2, boxes.getBoxBodyId());
            ps.setInt(3, boxes.getSerializeBoxBodyId());
            ps.setInt(4, boxes.getIcsId());
            ps.setInt(5, boxes.getBoxState());
            ps.setTimestamp(6,
                    new Timestamp(boxes.getPlaceDateTime().getTime()));
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
    public boolean updateBoxRegisterBoxesInfo(Boxes boxes) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_boxes SET boxName = ?, customerId = ?, boxAddress = ?, environmentPicture = ?, boxState = ?, shopId = ?, placeDateTime = ? WHERE boxId = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, boxes.getBoxName());
            ps.setInt(2, boxes.getCustomerId());
            ps.setString(3, boxes.getBoxAddress());
            ps.setString(4, boxes.getEnvironmentPicture());
            ps.setInt(5, boxes.getBoxState());
            ps.setInt(6, boxes.getShopId());
            ps.setTimestamp(7,
                    new Timestamp(boxes.getPlaceDateTime().getTime()));
            ps.setString(8, boxes.getBoxId());
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
    public boolean updateLocationLatitudeAndLongitudeByBoxId(String boxId,
            String latitude, String longitude) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_boxes SET latitude = ?, longitude = ? WHERE boxId = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, latitude);
            ps.setString(2, longitude);
            ps.setString(3, boxId);
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
    public boolean updateICSId(int icsId) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_boxes SET icsId = ? WHERE icsId = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, 0);
            ps.setInt(2, icsId);
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
    public List<Boxes> findBoxesByBoxManagerIdAndPageNumber(int boxManagerId,
            int pageNumber) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT b.* FROM t_box_manager a, t_boxes b WHERE a.boxId=b.boxId AND a.boxDeliveryId = ? AND b.boxState = ? LIMIT ? , ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Boxes> boxesList = new ArrayList<Boxes>();
        Boxes boxes = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, boxManagerId);
            ps.setInt(2, ResponseStatus.BOX_STATE_FINISHED_MERCHANT_DEPLOYMENT);
            ps.setInt(3, (pageNumber - 1) * Boxes.PAGE_SIZE);
            ps.setInt(4, Boxes.PAGE_SIZE);
            rs = ps.executeQuery();
            while (rs.next()) {
                boxes = new Boxes();
                boxes.setBoxId(rs.getString("boxId"));
                boxes.setBoxName(rs.getString("boxName"));
                boxes.setBoxBodyId(rs.getInt("boxBodyId"));
                boxes.setSerializeBoxBodyId(rs.getInt("serializeBoxBodyId"));
                boxes.setCustomerId(rs.getInt("customerId"));
                boxes.setIcsId(rs.getInt("icsId"));
                boxes.setBoxAddress(rs.getString("boxAddress"));
                boxes.setLatitude(rs.getString("latitude"));
                boxes.setLongitude(rs.getString("longitude"));
                boxes.setLocationClassification(rs
                        .getInt("locationClassification"));
                boxes.setNetworkType(rs.getInt("networkType"));
                boxes.setPlaceEnvironment(rs.getInt("placeEnvironment"));
                boxes.setIsOpen(rs.getInt("isOpen"));
                boxes.setEnvironmentPicture(rs.getString("environmentPicture"));
                boxes.setBoxManagerId(rs.getInt("boxManagerId"));
                boxes.setBoxState(rs.getInt("boxState"));
                boxes.setShopId(rs.getInt("shopId"));
                boxes.setPlaceDateTime(rs.getTimestamp("placeDateTime"));
                boxesList.add(boxes);
            }
            return boxesList;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public int findBoxesCountByBoxManagerId(int boxManagerId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT COUNT(*) AS boxesCount FROM t_box_manager a, t_boxes b WHERE a.boxId=b.boxId AND a.boxDeliveryId = ? and b.boxState = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        int boxesCount = 0;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, boxManagerId);
            ps.setInt(2, ResponseStatus.BOX_STATE_FINISHED_MERCHANT_DEPLOYMENT);
            rs = ps.executeQuery();
            if (rs.next()) {
                boxesCount = rs.getInt("boxesCount");
            }
            return boxesCount;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public Boxes findBoxesBySerializeBoxBodyId(int serializeBoxBodyId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_boxes WHERE serializeBoxBodyId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        Boxes boxes = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, serializeBoxBodyId);
            rs = ps.executeQuery();
            if (rs.next()) {
                boxes = new Boxes();
                boxes.setBoxId(rs.getString("boxId"));
                boxes.setBoxName(rs.getString("boxName"));
                boxes.setBoxBodyId(rs.getInt("boxBodyId"));
                boxes.setSerializeBoxBodyId(rs.getInt("serializeBoxBodyId"));
                boxes.setCustomerId(rs.getInt("customerId"));
                boxes.setIcsId(rs.getInt("icsId"));
                boxes.setBoxAddress(rs.getString("boxAddress"));
                boxes.setLatitude(rs.getString("latitude"));
                boxes.setLongitude(rs.getString("longitude"));
                boxes.setLocationClassification(rs
                        .getInt("locationClassification"));
                boxes.setNetworkType(rs.getInt("networkType"));
                boxes.setPlaceEnvironment(rs.getInt("placeEnvironment"));
                boxes.setIsOpen(rs.getInt("isOpen"));
                boxes.setEnvironmentPicture(rs.getString("environmentPicture"));
                boxes.setBoxManagerId(rs.getInt("boxManagerId"));
                boxes.setBoxState(rs.getInt("boxState"));
                boxes.setShopId(rs.getInt("shopId"));
                boxes.setPlaceDateTime(rs.getTimestamp("placeDateTime"));
            }
            return boxes;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public boolean updateSerializeBoxBodyId(int serializeBoxBodyId) {
        boolean updateFlag = false;
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "UPDATE t_boxes SET serializeBoxBodyId = ? WHERE serializeBoxBodyId = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, 0);
            ps.setInt(2, serializeBoxBodyId);
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
    public Boxes findBoxesByBoxIdAndCustomerId(String boxId, int customerId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_boxes WHERE boxId = ? AND customerId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        Boxes boxes = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, boxId);
            ps.setInt(2, customerId);
            rs = ps.executeQuery();
            if (rs.next()) {
                boxes = new Boxes();
                boxes.setBoxId(rs.getString("boxId"));
                boxes.setBoxName(rs.getString("boxName"));
                boxes.setBoxBodyId(rs.getInt("boxBodyId"));
                boxes.setSerializeBoxBodyId(rs.getInt("serializeBoxBodyId"));
                boxes.setCustomerId(rs.getInt("customerId"));
                boxes.setIcsId(rs.getInt("icsId"));
                boxes.setBoxAddress(rs.getString("boxAddress"));
                boxes.setLatitude(rs.getString("latitude"));
                boxes.setLongitude(rs.getString("longitude"));
                boxes.setLocationClassification(rs
                        .getInt("locationClassification"));
                boxes.setNetworkType(rs.getInt("networkType"));
                boxes.setPlaceEnvironment(rs.getInt("placeEnvironment"));
                boxes.setIsOpen(rs.getInt("isOpen"));
                boxes.setEnvironmentPicture(rs.getString("environmentPicture"));
                boxes.setBoxManagerId(rs.getInt("boxManagerId"));
                boxes.setBoxState(rs.getInt("boxState"));
                boxes.setShopId(rs.getInt("shopId"));
                boxes.setPlaceDateTime(rs.getTimestamp("placeDateTime"));
            }
            return boxes;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    /**
     * 针对第三方对接商户查询附近门店
     */
    @Override
    public List<Boxes> findNearByBoxesByCustomerId(int customerId,
            String fromLat, String fromLng, double nearByRange, int pageNumber) {
        Connection connection = JDBCUtil.getDBConnection();
        log.info("findNearByBoxes:formLat=" + fromLat + ",fromLng=" + fromLng
                + ",nearByRange=" + nearByRange + ",pageNumber=" + pageNumber);
        // 计算当前位置和数据库位置两个点之前的距离
        // order by 和 limit共用会出现奇葩问题，暂时去掉order by
        // String sql =
        // "SELECT * FROM (SELECT *,(ROUND(6378138 * 2 * ASIN(SQRT(POW(SIN(((latitude * PI()) / 180 - (? * PI()) / 180) / 2), 2) + COS((? * PI()) / 180) * COS((latitude * PI()) / 180) * POW(SIN(((longitude * PI()) / 180 - (? * PI()) / 180) / 2), 2))))) AS distance FROM `t_boxes`) t WHERE t.distance <= ? ORDER BY distance ASC LIMIT ? , ?";
        String sql = "SELECT * FROM (SELECT *,(ROUND(6378138 * 2 * ASIN(SQRT(POW(SIN(((latitude * PI()) / 180 - (? * PI()) / 180) / 2), 2) + COS((? * PI()) / 180) * COS((latitude * PI()) / 180) * POW(SIN(((longitude * PI()) / 180 - (? * PI()) / 180) / 2), 2))))) AS distance FROM `t_boxes`) t WHERE t.distance <= ? AND t.boxState = ? AND t.customerId = ? LIMIT ? , ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Boxes> boxesList = new ArrayList<Boxes>();
        Boxes boxes = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, fromLat);
            ps.setString(2, fromLat);
            ps.setString(3, fromLng);
            ps.setDouble(4, nearByRange);
            ps.setInt(5, ResponseStatus.BOX_STATE_FINISHED_MERCHANT_DEPLOYMENT);
            ps.setInt(6, customerId);
            ps.setInt(7, (pageNumber - 1) * Boxes.NEARBY_BOXES_PAGE_SIZE);
            ps.setInt(8, Boxes.NEARBY_BOXES_PAGE_SIZE);
            rs = ps.executeQuery();
            while (rs.next()) {
                boxes = new Boxes();
                boxes.setBoxId(rs.getString("boxId"));
                boxes.setBoxName(rs.getString("boxName"));
                boxes.setBoxBodyId(rs.getInt("boxBodyId"));
                boxes.setSerializeBoxBodyId(rs.getInt("serializeBoxBodyId"));
                boxes.setCustomerId(rs.getInt("customerId"));
                boxes.setIcsId(rs.getInt("icsId"));
                boxes.setBoxAddress(rs.getString("boxAddress"));
                boxes.setLatitude(rs.getString("latitude"));
                boxes.setLongitude(rs.getString("longitude"));
                boxes.setDistance(rs.getDouble("distance"));
                boxes.setLocationClassification(rs
                        .getInt("locationClassification"));
                boxes.setNetworkType(rs.getInt("networkType"));
                boxes.setPlaceEnvironment(rs.getInt("placeEnvironment"));
                boxes.setIsOpen(rs.getInt("isOpen"));
                boxes.setEnvironmentPicture(rs.getString("environmentPicture"));
                boxes.setBoxManagerId(rs.getInt("boxManagerId"));
                boxes.setBoxState(rs.getInt("boxState"));
                boxes.setShopId(rs.getInt("shopId"));
                boxes.setPlaceDateTime(rs.getTimestamp("placeDateTime"));
                boxesList.add(boxes);
            }
            return boxesList;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    /**
     * 针对第三方对接商户查询附近门店
     */
    @Override
    public int findNearByBoxesCountByCustomerId(int customerId, String fromLat,
            String fromLng, double nearByRange) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT COUNT(*) nearByBoxesCount FROM (SELECT *,(ROUND(6378138 * 2 * ASIN(SQRT(POW(SIN(((latitude * PI()) / 180 - (? * PI()) / 180) / 2), 2) + COS((? * PI()) / 180) * COS((latitude * PI()) / 180) * POW(SIN(((longitude * PI()) / 180 - (? * PI()) / 180) / 2), 2))))) AS distance FROM `t_boxes`) t WHERE t.distance <= ? AND t.boxState = ? AND t.customerId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        int boxManagerCount = 0;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, fromLat);
            ps.setString(2, fromLat);
            ps.setString(3, fromLng);
            ps.setDouble(4, nearByRange);
            ps.setInt(5, ResponseStatus.BOX_STATE_FINISHED_MERCHANT_DEPLOYMENT);
            ps.setInt(6, customerId);
            rs = ps.executeQuery();
            if (rs.next()) {
                boxManagerCount = rs.getInt("nearByBoxesCount");
            }
            return boxManagerCount;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

    @Override
    public List<Boxes> findBoxesByCustomerId(int customerId) {
        Connection connection = JDBCUtil.getDBConnection();
        String sql = "SELECT * FROM t_boxes WHERE customerId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Boxes> boxesList = new ArrayList<Boxes>();
        Boxes boxes = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, customerId);
            rs = ps.executeQuery();
            while (rs.next()) {
                boxes = new Boxes();
                boxes.setBoxId(rs.getString("boxId"));
                boxes.setBoxName(rs.getString("boxName"));
                boxes.setBoxBodyId(rs.getInt("boxBodyId"));
                boxes.setSerializeBoxBodyId(rs.getInt("serializeBoxBodyId"));
                boxes.setCustomerId(rs.getInt("customerId"));
                boxes.setIcsId(rs.getInt("icsId"));
                boxes.setBoxAddress(rs.getString("boxAddress"));
                boxes.setLatitude(rs.getString("latitude"));
                boxes.setLongitude(rs.getString("longitude"));
                boxes.setLocationClassification(rs
                        .getInt("locationClassification"));
                boxes.setNetworkType(rs.getInt("networkType"));
                boxes.setPlaceEnvironment(rs.getInt("placeEnvironment"));
                boxes.setIsOpen(rs.getInt("isOpen"));
                boxes.setEnvironmentPicture(rs.getString("environmentPicture"));
                boxes.setBoxManagerId(rs.getInt("boxManagerId"));
                boxes.setBoxState(rs.getInt("boxState"));
                boxes.setShopId(rs.getInt("shopId"));
                boxes.setPlaceDateTime(rs.getTimestamp("placeDateTime"));
                boxesList.add(boxes);
            }
            return boxesList;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JDBCUtil.closeDBConnection(connection, ps, rs);
        }
    }

}
