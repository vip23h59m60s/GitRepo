package com.chinapalms.kwobox.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.chinapalms.kwobox.dao.impl.ReplenishGoodsDAOImpl;
import com.chinapalms.kwobox.javabean.Boxes;
import com.chinapalms.kwobox.javabean.CurrentUser;
import com.chinapalms.kwobox.javabean.ReplenishGoods;
import com.chinapalms.kwobox.javabean.ReplenishGoodsDetails;
import com.chinapalms.kwobox.utils.ResponseStatus;

public class ReplenishGoodsService extends ReplenishGoodsDAOImpl {

    Log log = LogFactory.getLog(ReplenishGoodsService.class);

    @Override
    public boolean addReplenishGoods(
            com.chinapalms.kwobox.javabean.ReplenishGoods replenishGoods) {
        return super.addReplenishGoods(replenishGoods);
    }

    @Override
    public List<ReplenishGoods> findReplenishGoodsByBoxDeliveryId(
            int boxDeliveryId) {
        return super.findReplenishGoodsByBoxDeliveryId(boxDeliveryId);
    }

    @Override
    public int findReplenishGoodsCountByBoxDeliveryId(int boxDeliveryId) {
        return super.findReplenishGoodsCountByBoxDeliveryId(boxDeliveryId);
    }

    @Override
    public List<ReplenishGoods> findReplenishGoodsByBoxDeliveryIdAndPageNumber(
            int boxDeliveryId, int pageNumber) {
        return super.findReplenishGoodsByBoxDeliveryIdAndPageNumber(
                boxDeliveryId, pageNumber);
    }

    @Override
    public List<ReplenishGoods> findReplenishGoodsByBoxId(String boxId) {
        return super.findReplenishGoodsByBoxId(boxId);
    }

    @Override
    public List<ReplenishGoods> findReplenishGoodsByBoxDeliveryIdAndBoxIdAndPageNumber(
            int boxDeliveryId, String boxId, int pageNumber) {
        return super.findReplenishGoodsByBoxDeliveryIdAndBoxIdAndPageNumber(
                boxDeliveryId, boxId, pageNumber);
    }

    /**
     * 生成无理货记录
     * 
     * @param boxGoodsJsonObject
     * @param replenishDate
     * @param boxId
     * @return
     */
    public boolean makeNoUpdateGoodsReplenishOrde(String replenishId,
            Date replenishDate, String boxId) {
        ReplenishGoods replenishGoods = new ReplenishGoods();
        CurrentUserService currentUserService = new CurrentUserService();
        CurrentUser currentUser = currentUserService
                .findCurrentUserByBoxId(boxId);
        if (currentUser == null) {
            return false;
        }
        replenishGoods.setReplenishmentId(replenishId);
        Date replenishDateTime = replenishDate;
        replenishGoods.setReplenishmentTime(replenishDateTime);
        replenishGoods.setBoxId(boxId);
        if (currentUser != null) {
            replenishGoods.setBoxDeliveryId(currentUser.getCustomerWorkerId());
        }
        int totalAddNumber = 0;
        int totalRemovedNumber = 0;
        replenishGoods.setReplenishmentGoodsNumber(totalAddNumber);
        replenishGoods.setTakeOffGoodsNumber(totalRemovedNumber);
        replenishGoods.setReplenishType(0);
        if (addReplenishGoods(replenishGoods)) {
            log.info("ReplenishGoodsService->add no update goods replenish success");
            return true;
        } else {
            return false;
        }
    }

    public boolean makeReplenishOrder(JSONObject boxGoodsJsonObject,
            Date replenishDate, String boxId) {
        ReplenishGoods replenishGoods = new ReplenishGoods();
        CurrentUserService currentUserService = new CurrentUserService();
        CurrentUser currentUser = currentUserService
                .findCurrentUserByBoxId(boxId);
        if (currentUser == null) {
            return false;
        }

        JSONArray boxGoodsJsonArrayFromJsonObject = boxGoodsJsonObject
                .getJSONArray("BoxGoods");
        JSONObject updateGoodsJsonObject = boxGoodsJsonArrayFromJsonObject
                .getJSONObject(0).getJSONObject("UpdatedGoods");
        String replenishId = updateGoodsJsonObject.getString("updateId");
        replenishGoods.setReplenishmentId(replenishId);
        Date replenishDateTime = replenishDate;
        replenishGoods.setReplenishmentTime(replenishDateTime);
        replenishGoods.setBoxId(boxId);
        if (currentUser != null) {
            // 暂时用phoneNumber后4位作为boxDeliveryId
            replenishGoods.setBoxDeliveryId(currentUser.getCustomerWorkerId());
        }

        String updateGoodsFlag = updateGoodsJsonObject
                .getString("updateGoodsFlag");
        if (updateGoodsFlag.equals("added")) {
            int totalAddNumber = Integer.valueOf(updateGoodsJsonObject
                    .getString("totalAddedNumber"));
            replenishGoods.setReplenishmentGoodsNumber(totalAddNumber);
            replenishGoods.setTakeOffGoodsNumber(0);
        } else if (updateGoodsFlag.equals("removed")) {
            int totalRemovedNumber = Integer.valueOf(updateGoodsJsonObject
                    .getString("totalRemovedNumber"));
            replenishGoods.setReplenishmentGoodsNumber(0);
            replenishGoods.setTakeOffGoodsNumber(totalRemovedNumber);
        } else if (updateGoodsFlag.equals("addedAndRemoved")) {
            int totalAddNumber = Integer.valueOf(updateGoodsJsonObject
                    .getString("totalAddedNumber"));
            int totalRemovedNumber = Integer.valueOf(updateGoodsJsonObject
                    .getString("totalRemovedNumber"));
            replenishGoods.setReplenishmentGoodsNumber(totalAddNumber);
            replenishGoods.setTakeOffGoodsNumber(totalRemovedNumber);
        }
        replenishGoods.setReplenishType(1);
        log.info("ReplenishGoodsService->replenishGoods=" + replenishGoods);
        if (addReplenishGoods(replenishGoods)) {
            log.info("ReplenishGoodsService->add replenishGoods success");
            ReplenishGoodsDetailsService replenishGoodsDetailsService = new ReplenishGoodsDetailsService();
            replenishGoodsDetailsService.addReplenishGoodsDetailss(
                    boxGoodsJsonObject, updateGoodsFlag);
        }
        return true;
    }

    public String makeReplenishId(Date replenishDate) {
        Random random = new Random();
        String result = "";
        for (int i = 0; i < 6; i++) {
            result += random.nextInt(10);
        }
        return new SimpleDateFormat("yyyyMMddHHmmss").format(replenishDate)
                + result;
    }

    // 分页查询逻辑
    // 理货记录分为三种情况：1.只有上货，2.只有下架；3.同时有上货和下架
    public String doFindMyReplenishGoodsOrder(String boxDeliveryId,
            int pageNumber) {
        BoxesService boxesService = new BoxesService();
        // 暂时用phoneNumber后4位作为boxDeliveryId
        int count = findReplenishGoodsCountByBoxDeliveryId(Integer
                .valueOf(boxDeliveryId));
        int totalPages = 0;
        // 计算总页数
        if (count % ReplenishGoods.PAGE_SIZE == 0) {
            // 对总页数赋值
            totalPages = count / ReplenishGoods.PAGE_SIZE;
        } else {
            // 对总页数赋值
            totalPages = count / ReplenishGoods.PAGE_SIZE + 1;
        }

        // 暂时用phoneNumber后4位作为boxDeliveryId
        List<ReplenishGoods> replenishGoodsList = findReplenishGoodsByBoxDeliveryIdAndPageNumber(
                Integer.valueOf(boxDeliveryId), pageNumber);
        ReplenishGoodsDetailsService replenishGoodsDetailsService = new ReplenishGoodsDetailsService();
        if (pageNumber == 1 && replenishGoodsList.size() <= 0) {
            return ResponseStatus.NO_ORDERS;
        } else if (pageNumber <= totalPages && replenishGoodsList.size() > 0) {
            JSONObject ordersJsonObject = new JSONObject();
            JSONArray ordersJsonArray = new JSONArray();
            JSONObject orderJsonObject = new JSONObject();
            for (int i = 0; i < replenishGoodsList.size(); i++) {
                ReplenishGoods replenishGoods = replenishGoodsList.get(i);
                String replenishGoodsFlag = null;
                if (replenishGoods.getReplenishmentGoodsNumber() > 0
                        && replenishGoods.getTakeOffGoodsNumber() > 0) {
                    replenishGoodsFlag = "addedAndRemoved";
                } else if (replenishGoods.getReplenishmentGoodsNumber() > 0) {
                    replenishGoodsFlag = "added";
                } else if (replenishGoods.getTakeOffGoodsNumber() > 0) {
                    replenishGoodsFlag = "removed";
                }
                List<ReplenishGoodsDetails> replenishGoodsDetailsList = replenishGoodsDetailsService
                        .findReplenishGoodsDetailsByReplenishId(replenishGoods
                                .getReplenishmentId());
                JSONObject orderPropertiesJsonObject = new JSONObject();
                JSONArray addedGoodsJsonArray = new JSONArray();
                JSONArray removedGoodsJsonArray = new JSONArray();

                for (int j = 0; j < replenishGoodsDetailsList.size(); j++) {
                    ReplenishGoodsDetails replenishGoodsDetails = replenishGoodsDetailsList
                            .get(j);
                    JSONObject goodsJsonObject = new JSONObject();
                    goodsJsonObject.put("goodsName",
                            replenishGoodsDetails.getGoodsName());
                    goodsJsonObject.put("categoryGoodsNumber",
                            replenishGoodsDetails.getGoodsNumber());
                    int replenishmentState = replenishGoodsDetails
                            .getReplenishmentState();
                    if (replenishmentState == 0) {
                        addedGoodsJsonArray.add(goodsJsonObject);
                    } else if (replenishmentState == 1) {
                        removedGoodsJsonArray.add(goodsJsonObject);
                    }
                }

                orderPropertiesJsonObject.put("replenishGoodsOrderId",
                        replenishGoods.getReplenishmentId());
                orderPropertiesJsonObject.put("boxId",
                        replenishGoods.getBoxId());
                Boxes boxes = boxesService.findBoxesByBoxId(replenishGoods
                        .getBoxId());
                if (boxes != null && boxes.getBoxName() != null) {
                    orderPropertiesJsonObject
                            .put("boxName", boxes.getBoxName());
                } else {
                    orderPropertiesJsonObject.put("boxName", "unknow");
                }
                orderPropertiesJsonObject.put("boxDeliveryId",
                        replenishGoods.getBoxDeliveryId());
                orderPropertiesJsonObject.put("replenishmentGoodsNumber",
                        replenishGoods.getReplenishmentGoodsNumber());
                orderPropertiesJsonObject.put("takeOffGoodsNumber",
                        replenishGoods.getTakeOffGoodsNumber());
                SimpleDateFormat df = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                orderPropertiesJsonObject.put("replenishmentDate",
                        df.format(replenishGoods.getReplenishmentTime()));

                if (replenishGoodsFlag.equals("addedAndRemoved")) {
                    orderPropertiesJsonObject.put("replenishGoodsFlag",
                            "addedAndRemoved");
                    orderPropertiesJsonObject.put("AddedGoods",
                            addedGoodsJsonArray);
                    orderPropertiesJsonObject.put("RemovedGoods",
                            removedGoodsJsonArray);
                } else if (replenishGoodsFlag.equals("added")) {
                    orderPropertiesJsonObject
                            .put("replenishGoodsFlag", "added");
                    orderPropertiesJsonObject.put("AddedGoods",
                            addedGoodsJsonArray);
                } else if (replenishGoodsFlag.equals("removed")) {
                    orderPropertiesJsonObject.put("replenishGoodsFlag",
                            "removed");
                    orderPropertiesJsonObject.put("RemovedGoods",
                            removedGoodsJsonArray);
                }
                orderJsonObject
                        .put("ReplenishOrder", orderPropertiesJsonObject);

                ordersJsonArray.add(orderJsonObject);
            }
            ordersJsonObject.put("ReplenishOrders", ordersJsonArray);
            return ordersJsonObject.toString();
        } else {
            return ResponseStatus.LAST_PAGE;
        }
    }

    // 分页查询逻辑(通过boxDeliveryId和boxId联合查询)
    // 理货记录分为三种情况：1.只有上货，2.只有下架；3.同时有上货和下架
    public String doFindMyReplenishGoodsOrder(String boxDeliveryId,
            String boxId, int pageNumber) {
        BoxesService boxesService = new BoxesService();
        // 暂时用phoneNumber后4位作为boxDeliveryId
        int count = findReplenishGoodsCountByBoxDeliveryId(Integer
                .valueOf(boxDeliveryId));
        int totalPages = 0;
        // 计算总页数
        if (count % ReplenishGoods.PAGE_SIZE == 0) {
            // 对总页数赋值
            totalPages = count / ReplenishGoods.PAGE_SIZE;
        } else {
            // 对总页数赋值
            totalPages = count / ReplenishGoods.PAGE_SIZE + 1;
        }

        // 暂时用phoneNumber后4位作为boxDeliveryId
        List<ReplenishGoods> replenishGoodsList = findReplenishGoodsByBoxDeliveryIdAndBoxIdAndPageNumber(
                Integer.valueOf(boxDeliveryId), boxId, pageNumber);
        ReplenishGoodsDetailsService replenishGoodsDetailsService = new ReplenishGoodsDetailsService();
        if (pageNumber == 1 && replenishGoodsList.size() <= 0) {
            return ResponseStatus.NO_ORDERS;
        } else if (pageNumber <= totalPages && replenishGoodsList.size() > 0) {
            JSONObject ordersJsonObject = new JSONObject();
            JSONArray ordersJsonArray = new JSONArray();
            JSONObject orderJsonObject = new JSONObject();
            for (int i = 0; i < replenishGoodsList.size(); i++) {
                ReplenishGoods replenishGoods = replenishGoodsList.get(i);
                String replenishGoodsFlag = null;
                if (replenishGoods.getReplenishmentGoodsNumber() > 0
                        && replenishGoods.getTakeOffGoodsNumber() > 0) {
                    replenishGoodsFlag = "addedAndRemoved";
                } else if (replenishGoods.getReplenishmentGoodsNumber() > 0) {
                    replenishGoodsFlag = "added";
                } else if (replenishGoods.getTakeOffGoodsNumber() > 0) {
                    replenishGoodsFlag = "removed";
                }
                List<ReplenishGoodsDetails> replenishGoodsDetailsList = replenishGoodsDetailsService
                        .findReplenishGoodsDetailsByReplenishId(replenishGoods
                                .getReplenishmentId());
                JSONObject orderPropertiesJsonObject = new JSONObject();
                JSONArray addedGoodsJsonArray = new JSONArray();
                JSONArray removedGoodsJsonArray = new JSONArray();

                for (int j = 0; j < replenishGoodsDetailsList.size(); j++) {
                    ReplenishGoodsDetails replenishGoodsDetails = replenishGoodsDetailsList
                            .get(j);
                    JSONObject goodsJsonObject = new JSONObject();
                    goodsJsonObject.put("goodsName",
                            replenishGoodsDetails.getGoodsName());
                    goodsJsonObject.put("categoryGoodsNumber",
                            replenishGoodsDetails.getGoodsNumber());
                    int replenishmentState = replenishGoodsDetails
                            .getReplenishmentState();
                    if (replenishmentState == 0) {
                        addedGoodsJsonArray.add(goodsJsonObject);
                    } else if (replenishmentState == 1) {
                        removedGoodsJsonArray.add(goodsJsonObject);
                    }
                }

                orderPropertiesJsonObject.put("replenishGoodsOrderId",
                        replenishGoods.getReplenishmentId());
                orderPropertiesJsonObject.put("boxId",
                        replenishGoods.getBoxId());
                Boxes boxes = boxesService.findBoxesByBoxId(replenishGoods
                        .getBoxId());
                if (boxes != null && boxes.getBoxName() != null) {
                    orderPropertiesJsonObject
                            .put("boxName", boxes.getBoxName());
                } else {
                    orderPropertiesJsonObject.put("boxName", "unknow");
                }
                orderPropertiesJsonObject.put("boxDeliveryId",
                        replenishGoods.getBoxDeliveryId());
                orderPropertiesJsonObject.put("replenishmentGoodsNumber",
                        replenishGoods.getReplenishmentGoodsNumber());
                orderPropertiesJsonObject.put("takeOffGoodsNumber",
                        replenishGoods.getTakeOffGoodsNumber());
                SimpleDateFormat df = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                orderPropertiesJsonObject.put("replenishmentDate",
                        df.format(replenishGoods.getReplenishmentTime()));

                if (replenishGoodsFlag.equals("addedAndRemoved")) {
                    orderPropertiesJsonObject.put("replenishGoodsFlag",
                            "addedAndRemoved");
                    orderPropertiesJsonObject.put("AddedGoods",
                            addedGoodsJsonArray);
                    orderPropertiesJsonObject.put("RemovedGoods",
                            removedGoodsJsonArray);
                } else if (replenishGoodsFlag.equals("added")) {
                    orderPropertiesJsonObject
                            .put("replenishGoodsFlag", "added");
                    orderPropertiesJsonObject.put("AddedGoods",
                            addedGoodsJsonArray);
                } else if (replenishGoodsFlag.equals("removed")) {
                    orderPropertiesJsonObject.put("replenishGoodsFlag",
                            "removed");
                    orderPropertiesJsonObject.put("RemovedGoods",
                            removedGoodsJsonArray);
                }
                orderJsonObject
                        .put("ReplenishOrder", orderPropertiesJsonObject);

                ordersJsonArray.add(orderJsonObject);
            }
            ordersJsonObject.put("ReplenishOrders", ordersJsonArray);
            return ordersJsonObject.toString();
        } else {
            return ResponseStatus.LAST_PAGE;
        }
    }

}
