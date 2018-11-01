package com.chinapalms.kwobox.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.websocket.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.chinapalms.kwobox.dao.impl.BoxesDAOImpl;
import com.chinapalms.kwobox.javabean.BoxBody;
import com.chinapalms.kwobox.javabean.BoxGoodsXXX;
import com.chinapalms.kwobox.javabean.BoxGoodsXXXNew;
import com.chinapalms.kwobox.javabean.BoxManager;
import com.chinapalms.kwobox.javabean.BoxStatus;
import com.chinapalms.kwobox.javabean.BoxStructure;
import com.chinapalms.kwobox.javabean.Boxes;
import com.chinapalms.kwobox.javabean.CurrentUser;
import com.chinapalms.kwobox.javabean.Customer;
import com.chinapalms.kwobox.javabean.CustomerWorker;
import com.chinapalms.kwobox.javabean.GoodsCategory;
import com.chinapalms.kwobox.javabean.ICS;
import com.chinapalms.kwobox.javabean.QRCodeContentCustomer;
import com.chinapalms.kwobox.javabean.ReplenishGoods;
import com.chinapalms.kwobox.javabean.SerializeBoxBody;
import com.chinapalms.kwobox.javabean.User;
import com.chinapalms.kwobox.servelet.SessionMapFactory;
import com.chinapalms.kwobox.tencentlocation.TencentLocationService;
import com.chinapalms.kwobox.utils.ResponseStatus;
import com.sun.org.apache.bcel.internal.generic.NEW;

public class BoxesService extends BoxesDAOImpl {

    Log log = LogFactory.getLog(BoxesService.class);

    @Override
    public Boxes findBoxesByBoxId(String boxId) {
        return super.findBoxesByBoxId(boxId);
    }

    @Override
    public Boxes findBoxesByIcsId(int icsId) {
        return super.findBoxesByIcsId(icsId);
    }

    @Override
    public List<Boxes> findNearByBoxes(String fromLat, String fromLng,
            double nearByRange, int pageNumber) {
        return super.findNearByBoxes(fromLat, fromLng, nearByRange, pageNumber);
    }

    @Override
    public int findNearByBoxesCount(String fromLat, String fromLng,
            double nearByRange) {
        return super.findNearByBoxesCount(fromLat, fromLng, nearByRange);
    }

    @Override
    public boolean addBoxes(Boxes boxes) {
        return super.addBoxes(boxes);
    }

    @Override
    public boolean updateBoxRegisterBoxesInfo(Boxes boxes) {
        return super.updateBoxRegisterBoxesInfo(boxes);
    }

    @Override
    public boolean updateLocationLatitudeAndLongitudeByBoxId(String boxId,
            String latitude, String longitude) {
        return super.updateLocationLatitudeAndLongitudeByBoxId(boxId, latitude,
                longitude);
    }

    @Override
    public boolean updateICSId(int icsId) {
        return super.updateICSId(icsId);
    }

    @Override
    public boolean updateSerializeBoxBodyId(int serializeBoxBodyId) {
        return super.updateSerializeBoxBodyId(serializeBoxBodyId);
    }

    @Override
    public List<Boxes> findBoxesByBoxManagerIdAndPageNumber(int boxManagerId,
            int pageNumber) {
        return super.findBoxesByBoxManagerIdAndPageNumber(boxManagerId,
                pageNumber);
    }

    @Override
    public int findBoxesCountByBoxManagerId(int boxManagerId) {
        return super.findBoxesCountByBoxManagerId(boxManagerId);
    }

    @Override
    public Boxes findBoxesBySerializeBoxBodyId(int serializeBoxBodyId) {
        return super.findBoxesBySerializeBoxBodyId(serializeBoxBodyId);
    }

    @Override
    public Boxes findBoxesByBoxIdAndCustomerId(String boxId, int customerId) {
        return super.findBoxesByBoxIdAndCustomerId(boxId, customerId);
    }

    @Override
    public List<Boxes> findBoxesByCustomerId(int customerId) {
        return super.findBoxesByCustomerId(customerId);
    }

    /**
     * 验证当前柜门状态 1、占用中，即服务中；2、空闲中，可用；3、校验失败
     * 
     * @param box
     * @return
     */
    public String doOpenDoorIdentify(Boxes box, String phoneNumber) {
        String boxId = box.getBoxId();

        Boxes queryBox = findBoxesByBoxId(boxId);
        if (phoneNumber != null) {
            UserService userService = new UserService();
            User user = userService.queryUserByPhoneNumber(phoneNumber);
            if (user != null) {
                if (user.getPersonalCredit() < ResponseStatus.PERSONAL_CREDIT_FORBID_SHOPPING_VALUE) {
                    return ResponseStatus.PERSONAL_CREDIT_TOO_LOW;
                } else {
                    if (queryBox != null) {
                        BoxStatusService boxStatusService = new BoxStatusService();
                        BoxStatus boxStatus = boxStatusService
                                .findBoxStatusByBoxId(boxId);
                        // 判断对应柜子的二维码是否有商户定制的内容，如果有商户定制的内容则禁止智购猫扫开，只运行商户自己小程序扫开，否则走智狗猫默认流程
                        QRCodeContentCustomerService qrCodeContentCustomerService = new QRCodeContentCustomerService();
                        QRCodeContentCustomer qrCodeContentCustomer = qrCodeContentCustomerService
                                .findQRCodeContentCustomerByCustomerId(queryBox
                                        .getCustomerId());
                        if (qrCodeContentCustomer != null) {
                            return ResponseStatus.FAIL;
                        }
                        if (boxStatus != null) {
                            int state = boxStatus.getState();
                            if (state == ResponseStatus.BOX_STATUS_BUSY) {
                                return ResponseStatus.BOX_BUSY;
                            } else if (state == ResponseStatus.BOX_STATUS_BROKEN_DOWN) {
                                // 临时修改
                                return ResponseStatus.BOX_BROKEN_DOWN;
                                // return ResponseStatus.BOX_BUSY;
                            } else {
                                return ResponseStatus.SUCCESS;
                            }
                        } else {
                            return ResponseStatus.SUCCESS;
                        }
                    } else {
                        return ResponseStatus.FAIL;
                    }
                }
            }
        } else {
            if (queryBox != null) {
                BoxStatusService boxStatusService = new BoxStatusService();
                BoxStatus boxStatus = boxStatusService
                        .findBoxStatusByBoxId(boxId);
                if (boxStatus != null) {
                    int state = boxStatus.getState();
                    if (state == ResponseStatus.BOX_STATUS_BUSY) {
                        return ResponseStatus.BOX_BUSY;
                    } else if (state == ResponseStatus.BOX_STATUS_BROKEN_DOWN) {
                        // 临时修改
                        return ResponseStatus.BOX_BROKEN_DOWN;
                        // return ResponseStatus.BOX_BUSY;
                    } else {
                        return ResponseStatus.SUCCESS;
                    }
                } else {
                    return ResponseStatus.SUCCESS;
                }
            } else {
                return ResponseStatus.FAIL;
            }
        }
        return ResponseStatus.FAIL;
    }

    /**
     * 商户补货员扫码开门验证
     * 
     * @param boxId
     * @param customerWorkerId
     * @return
     */
    public String doManagerOpenDoorIdentify(String boxId,
            String customerWorkerId, String sortOrAddGoods) {

        Boxes queryBox = findBoxesByBoxId(boxId);
        if (queryBox != null) {

            if (sortOrAddGoods.equals(ResponseStatus.BOX_MANAGER_SORT_GOODS)) {
                BoxBodyService boxBodyService = new BoxBodyService();
                BoxBody boxBody = boxBodyService
                        .findBoxBodyByBoxBodyId(queryBox.getBoxBodyId());
                if (boxBody != null) {
                    // 只有称重柜子才需要理货，RFID类型柜子只需要补货
                    if (boxBody.getBoxType() != ResponseStatus.BOX_TYPE_WEIGHT) {
                        return ResponseStatus.BOX_NO_NEED_SORT_GOODS;
                    }
                }
            }
            CustomerWorkerService customerWorkerService = new CustomerWorkerService();
            CustomerWorker customerWorker = customerWorkerService
                    .findCustomerWorkerByCustomerWorkerId(Integer
                            .valueOf(customerWorkerId));
            BoxManagerService boxManagerService = new BoxManagerService();
            BoxManager boxManager = boxManagerService
                    .findBoxManagerByBoxIdAndDeliveryId(boxId,
                            Integer.valueOf(customerWorkerId));
            // 判断是否是具有智购猫超级员工权限或者商户具有该柜子理货权限的普通员工
            if ((customerWorker != null && customerWorker.getSuperPermission() == ResponseStatus.WORKER_PERMISSION_SUPER)
                    || (customerWorker != null
                            && customerWorker.getCustomerId() == queryBox
                                    .getCustomerId() && boxManager != null)) {
                BoxStatusService boxStatusService = new BoxStatusService();
                BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
                BoxStatus boxStatus = boxStatusService
                        .findBoxStatusByBoxId(boxId);
                if (boxStatus != null) {
                    int state = boxStatus.getState();
                    if (state == ResponseStatus.BOX_STATUS_BUSY) {
                        return ResponseStatus.BOX_BUSY;
                        // 判断后台货柜货道商品已发生变化
                    } else if (boxGoodsXXXService.checkBoxGoodsChanged(boxId)/*
                                                                              * state
                                                                              * ==
                                                                              * 3
                                                                              */) {
                        return ResponseStatus.BOX_UPDATED;
                    } else if (state == ResponseStatus.BOX_STATUS_BROKEN_DOWN) {
                        // 临时修改
                        // return ResponseStatus.BOX_BROKEN_DOWN;
                        return ResponseStatus.SUCCESS;
                    } else {
                        return ResponseStatus.SUCCESS;
                    }
                } else {
                    // 判断后台货柜货道商品已发生变化
                    if (boxGoodsXXXService.checkBoxGoodsChanged(boxId)) {
                        return ResponseStatus.BOX_UPDATED;
                    } else {
                        return ResponseStatus.SUCCESS;
                    }
                }
            } else {
                return ResponseStatus.BOX_OPEN_NO_PERMISSION;
            }
        } else {
            return ResponseStatus.FAIL;
        }
    }

    /**
     * 通知Android端开门
     * 
     * @param customCategory
     * @param boxId
     * @throws Exception
     */
    public void notifyAndroidOpenDoor(String requestSerial,
            String customCategory, String boxId, String userPermission)
            throws Exception {
        Session currentSession = SessionMapFactory.getInstance()
                .getCurrentSession(customCategory, boxId);
        try {
            if (currentSession != null) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("response", ResponseStatus.OPEN_DOOR);
                jsonObject.put("openUser", userPermission);
                // 当当前用户为普通消费者时将当前消费者的phoneNumber传递给工控机
                if (userPermission
                        .equals(ResponseStatus.PERMISSION_NORMAL_USER)) {
                    CurrentUserService currentUserService = new CurrentUserService();
                    CurrentUser currentUser = currentUserService
                            .findCurrentUserByBoxId(boxId);
                    jsonObject.put("phoneNumber", currentUser.getPhoneNumber());
                    jsonObject.put("requestSerialNumber", requestSerial);
                }
                SessionMapFactory.getInstance().sendMessage(currentSession,
                        jsonObject.toString());
            }
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    /**
     * 通知Android端开门，针对补货员，区分补货和理货
     * 
     * @param customCategory
     * @param boxId
     * @throws Exception
     */
    public void notifyAndroidOpenDoorForManager(String customCategory,
            String boxId, String userPermission, String action)
            throws Exception {
        Session currentSession = SessionMapFactory.getInstance()
                .getCurrentSession(customCategory, boxId);
        if (currentSession != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("response", ResponseStatus.OPEN_DOOR);
            jsonObject.put("openUser", userPermission);
            jsonObject.put("action", action);
            SessionMapFactory.getInstance().sendMessage(currentSession,
                    jsonObject.toString());
        }
    }

    /**
     * 通知微信端门已开
     * 
     * @param customCategory
     * @param boxId
     * @throws Exception
     */
    public void notifyWXDoorOpened(String customCategory, String boxId)
            throws Exception {
        Session currentSession = SessionMapFactory.getInstance()
                .getCurrentSession(customCategory, boxId);
        BoxStatusService boxStatusService = new BoxStatusService();
        if (boxStatusService.setOpenDoorState(boxId, true).equals(
                ResponseStatus.SUCCESS)) {
            if (currentSession != null) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("response", ResponseStatus.DOOR_OPENED);
                SessionMapFactory.getInstance().sendMessage(currentSession,
                        jsonObject.toString());
            }
        }
    }

    /**
     * 通知微信端门已关闭
     * 
     * @param customCategory
     * @param boxId
     * @throws Exception
     */
    public void notifyWXDoorClosed(String customCategory, String boxId)
            throws Exception {
        Session currentSession = SessionMapFactory.getInstance()
                .getCurrentSession(customCategory, boxId);
        if (currentSession != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("response", ResponseStatus.DOOR_CLOSED);
            SessionMapFactory.getInstance().sendMessage(currentSession,
                    jsonObject.toString());
        }
    }

    /**
     * 通知客户端柜子有人为异常行为
     * 
     * @param customCategory
     * @param boxId
     */
    public void notifyWXBoxException(String customCategory, String boxId,
            int deductPersonCredit) throws Exception {
        Session currentSession = SessionMapFactory.getInstance()
                .getCurrentSession(customCategory, boxId);
        if (currentSession != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("response", ResponseStatus.BOX_EXCEPTION);
            jsonObject.put("deductPersonCredit", deductPersonCredit);
            SessionMapFactory.getInstance().sendMessage(currentSession,
                    jsonObject.toString());
        }
    }

    // 分页查询指定理货员的柜子逻辑
    public String doFindMyManageBoxes(String boxDeliveryId, int pageNumber) {
        log.info("BoxesService->doFindMyManageBoxes:boxDeliveryId="
                + boxDeliveryId + ",pageNumber=" + pageNumber);
        int count = findBoxesCountByBoxManagerId(Integer.valueOf(boxDeliveryId));
        int totalPages = 0;
        // 计算总页数
        if (count % Boxes.PAGE_SIZE == 0) {
            // 对总页数赋值
            totalPages = count / Boxes.PAGE_SIZE;
        } else {
            // 对总页数赋值
            totalPages = count / Boxes.PAGE_SIZE + 1;
        }

        // 截取phoneNumber的后4位临时作为boxDeliveryId
        List<Boxes> boxesList = findBoxesByBoxManagerIdAndPageNumber(
                Integer.valueOf(boxDeliveryId), pageNumber);
        log.info("BoxesService->boxesList.size=" + boxesList.size());

        if (pageNumber == 1 && boxesList.size() <= 0) {
            return ResponseStatus.NO_BOXES;
        } else if (pageNumber <= totalPages && boxesList.size() > 0) {
            // 过滤重复boxes
            List<String> boxIdList = new ArrayList<String>();
            for (int i = 0; i < boxesList.size(); i++) {
                boxIdList.add(boxesList.get(i).getBoxId());
            }
            Set<String> uniqueBoxIdSet = new HashSet<>(boxIdList);
            List<Boxes> uniqueBoxesList = new ArrayList<Boxes>();
            for (String uniqueBoxId : uniqueBoxIdSet) {
                for (int i = 0; i < boxesList.size(); i++) {
                    if (uniqueBoxId.equals(boxesList.get(i).getBoxId())) {
                        uniqueBoxesList.add(boxesList.get(i));
                        break;
                    }
                }
            }
            JSONObject boxesJsonObject = new JSONObject();
            JSONArray boxesJsonArray = new JSONArray();
            // 同时查询柜子状态
            BoxStatusService boxStatusService = new BoxStatusService();
            // 检查是否需要补货
            BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
            // 查询当前是在购物还是在上货
            CurrentUserService currentUserService = new CurrentUserService();
            for (int i = 0; i < boxesList.size(); i++) {
                log.info("BoxesService->boxesList=" + boxesList.get(i));
                Boxes boxes = boxesList.get(i);
                JSONObject boxBeanJsonObject = JSONObject.fromObject(boxes);
                BoxStatus boxStatusBean = boxStatusService
                        .findBoxStatusByBoxId(boxes.getBoxId());
                int boxState = 0;
                if (boxStatusBean != null) {
                    boxState = boxStatusBean.getState();
                }
                boxBeanJsonObject.put("boxState", boxState);
                if (boxState == 0) {
                    boxBeanJsonObject.put("boxStateDescription",
                            BoxStatus.BOX_STATUS_DESCRIPTION_NORMAL);
                } else if (boxState == 1) {
                    CurrentUser currentUser = currentUserService
                            .findCurrentUserByBoxId(boxes.getBoxId());
                    if (currentUser != null) {
                        if (currentUser.getUserType().equals(
                                ResponseStatus.PERMISSION_NORMAL_USER)) {
                            boxBeanJsonObject
                                    .put("boxStateDescription",
                                            BoxStatus.BOX_STATUS_DESCRIPTION_SERVING_WITH_NORMAL_USER);
                        } else {
                            boxBeanJsonObject
                                    .put("boxStateDescription",
                                            BoxStatus.BOX_STATUS_DESCRIPTION_SERVING_WITH_MANAGER);
                        }
                    } else {
                        boxBeanJsonObject.put("boxStateDescription",
                                BoxStatus.BOX_STATUS_DESCRIPTION_SERVING);
                    }
                } else {
                    boxBeanJsonObject.put("boxStateDescription",
                            BoxStatus.BOX_STATUS_DESCRIPTION_BREAKDOWN);
                }

                if (boxGoodsXXXService.checkBoxSuggestReplenishmentState(boxes
                        .getBoxId())) {
                    boxBeanJsonObject.put("suggestReplenishmentState", "1");
                    boxBeanJsonObject
                            .put("suggestReplenishmentDescription",
                                    BoxGoodsXXX.BOX_SUGGEST_REPLENISHMENT_YES_DESCRIPTION);

                } else {
                    boxBeanJsonObject.put("suggestReplenishmentState", "0");
                    boxBeanJsonObject.put("suggestReplenishmentDescription",
                            BoxGoodsXXX.SUGGEST_REPLENISHMENT_NO_DESCRIPTION);
                }
                String boxAddress = boxes.getBoxAddress();
                boxBeanJsonObject.put("boxAddress", boxAddress);

                boxBeanJsonObject.put(
                        "boxEnvironmentPicture",
                        ResponseStatus.SERVER_URL + "/"
                                + boxes.getEnvironmentPicture());
                JSONObject boxJsonObject = new JSONObject();
                boxJsonObject.put("Box", boxBeanJsonObject);
                boxesJsonArray.add(boxJsonObject);
            }
            boxesJsonObject.put("Boxes", boxesJsonArray);
            log.info("BoxesService:->My Boxes=" + boxesJsonObject.toString());
            return boxesJsonObject.toString();
        } else {
            return ResponseStatus.LAST_PAGE;
        }
    }

    public String doGetBoxDetailsSummary(String boxId) {
        // 查询柜子模式：高频，超高频，称重
        BoxBodyService boxBodyService = new BoxBodyService();
        // 同时查询柜子状态
        BoxStatusService boxStatusService = new BoxStatusService();
        // 检查是否需要补货
        BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
        // 最近一次补货时间
        ReplenishGoodsService replenishGoodsService = new ReplenishGoodsService();
        // 查询当前是在购物还是在上货
        CurrentUserService currentUserService = new CurrentUserService();
        Boxes boxes = findBoxesByBoxId(boxId);
        JSONObject boxBeanJsonObject = JSONObject.fromObject(boxes);
        BoxStatus boxStatusBean = boxStatusService.findBoxStatusByBoxId(boxId);
        int boxType = boxBodyService.findBoxBodyByBoxBodyId(
                boxes.getBoxBodyId()).getBoxType();
        if (boxType == ResponseStatus.BOX_TYPE_WEIGHT) {
            boxBeanJsonObject.put("boxType",
                    ResponseStatus.BOX_TYPE_WEIGHT_DESCRIPTION);
        } else if (boxType == ResponseStatus.BOX_TYPE_HIGH_FREQUENCY_RFID) {
            boxBeanJsonObject.put("boxType",
                    ResponseStatus.BOX_TYPE_HIGH_FREQUENCY_RFID_DESCRIPTION);
        } else if (boxType == ResponseStatus.BOX_TYPE_SUPER_HIGH_FREQUENCY_RFID) {
            boxBeanJsonObject
                    .put("boxType",
                            ResponseStatus.BOX_TYPE_SUPER_HIGH_FREQUENCY_RFID_DESCRIPTION);
        }
        int boxState = 0;
        if (boxStatusBean != null) {
            boxState = boxStatusBean.getState();
        }
        boxBeanJsonObject.put("boxState", boxState);
        if (boxState == 0) {
            boxBeanJsonObject.put("boxStateDescription",
                    BoxStatus.BOX_STATUS_DESCRIPTION_NORMAL);
        } else if (boxState == 1) {
            CurrentUser currentUser = currentUserService
                    .findCurrentUserByBoxId(boxId);
            if (currentUser != null) {
                if (currentUser.getUserType().equals(
                        ResponseStatus.PERMISSION_NORMAL_USER)) {
                    boxBeanJsonObject
                            .put("boxStateDescription",
                                    BoxStatus.BOX_STATUS_DESCRIPTION_SERVING_WITH_NORMAL_USER);
                } else {
                    boxBeanJsonObject
                            .put("boxStateDescription",
                                    BoxStatus.BOX_STATUS_DESCRIPTION_SERVING_WITH_MANAGER);
                }
            } else {
                boxBeanJsonObject.put("boxStateDescription",
                        BoxStatus.BOX_STATUS_DESCRIPTION_SERVING);
            }
        } else {
            boxBeanJsonObject.put("boxStateDescription",
                    BoxStatus.BOX_STATUS_DESCRIPTION_BREAKDOWN);
        }

        if (boxGoodsXXXService.checkBoxSuggestReplenishmentState(boxId)) {
            boxBeanJsonObject.put("suggestReplenishmentState", "1");
            boxBeanJsonObject
                    .put("suggestReplenishmentDescription",
                            BoxGoodsXXX.BOXDETAILS_SUGGEST_REPLENISHMENT_YES_DESCRIPTION);
        } else {
            boxBeanJsonObject.put("suggestReplenishmentState", "0");
            boxBeanJsonObject.put("suggestReplenishmentDescription",
                    BoxGoodsXXX.SUGGEST_REPLENISHMENT_NO_DESCRIPTION);
        }

        List<ReplenishGoods> replenishGoodsList = replenishGoodsService
                .findReplenishGoodsByBoxId(boxId);
        if (replenishGoodsList.size() >= 1) {
            Date lastReplenishmentTime = replenishGoodsList.get(0)
                    .getReplenishmentTime();
            boxBeanJsonObject.put("lastReplenishmentTime",
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                            .format(lastReplenishmentTime));
        } else {
            boxBeanJsonObject.put("lastReplenishmentTime",
                    BoxGoodsXXX.NO_REPLENISHMENT_RECORD);
        }

        boxBeanJsonObject
                .put("boxEnvironmentPicture", ResponseStatus.SERVER_URL + "/"
                        + boxes.getEnvironmentPicture());
        JSONObject boxJsonObject = new JSONObject();
        boxJsonObject.put("Box", boxBeanJsonObject);
        return boxJsonObject.toString();
    }

    public String doGetNearbyBoxDetailsSummary(String boxId) {
        // 同时查询柜子状态
        BoxStatusService boxStatusService = new BoxStatusService();
        Boxes boxes = findBoxesByBoxId(boxId);
        JSONObject boxBeanJsonObject = JSONObject.fromObject(boxes);
        BoxStatus boxStatusBean = boxStatusService.findBoxStatusByBoxId(boxId);
        int boxState = 0;
        if (boxStatusBean != null) {
            boxState = boxStatusBean.getState();
        }
        boxBeanJsonObject.put("boxState", boxState);
        if (boxState == 0 || boxState == 1) {
            boxBeanJsonObject.put("boxStateDescription",
                    BoxStatus.BOX_STATUS_DESCRIPTION_NORMAL);
        } else {
            boxBeanJsonObject.put("boxStateDescription",
                    BoxStatus.BOX_NEARBY_STATUS_DESCRIPTION_BREAKDOWN);
        }

        boxBeanJsonObject
                .put("boxEnvironmentPicture", ResponseStatus.SERVER_URL + "/"
                        + boxes.getEnvironmentPicture());
        JSONObject boxJsonObject = new JSONObject();
        boxJsonObject.put("Box", boxBeanJsonObject);
        return boxJsonObject.toString();
    }

    public String doInitBoxConfig(String imeiNumber) {
        ICSService icsService = new ICSService();
        ICS ics = icsService.findICSByIMEINumber(imeiNumber);
        try {
            if (ics != null) {
                log.info("Boxes:doInitBoxConfig icsId=" + ics.getIcsId()
                        + ",ics=" + ics.toString() + ", imeiNumber="
                        + imeiNumber);
                Boxes boxes = findBoxesByIcsId(ics.getIcsId());
                log.info("Boxes:doInitBoxConfig boxes=" + boxes.toString());
                if (boxes != null) {
                    // 如果售货机已撤柜，则对应工控机作初始化操作时直接返回fail，以便工控机跳出条形码配置界面
                    if (boxes.getBoxState() == ResponseStatus.BOX_OPERATION_STATE_CLOSED) {
                        return ResponseStatus.FAIL;
                    }
                    BoxBodyService boxBodyService = new BoxBodyService();
                    BoxBody boxBody = boxBodyService
                            .findBoxBodyByBoxBodyId(boxes.getBoxBodyId());
                    JSONObject boxInitConfigBeanjsonObject = new JSONObject();
                    boxInitConfigBeanjsonObject.put("boxId", boxes.getBoxId());

                    // 售货机当前状态
                    BoxStatusService boxStatusService = new BoxStatusService();
                    BoxStatus boxStatus = boxStatusService
                            .findBoxStatusByBoxId(boxes.getBoxId());
                    int boxState = ResponseStatus.BOX_STATUS_NORMAL;
                    if (boxStatus != null) {
                        boxState = boxStatus.getState();
                    }
                    boxInitConfigBeanjsonObject.put("boxState", boxState);
                    CurrentUserService currentUserService = new CurrentUserService();
                    CurrentUser currentUser = currentUserService
                            .findCurrentUserByBoxId(boxes.getBoxId());
                    // 向工控机报当前售货机是一般用户还是管理员
                    String userType = ResponseStatus.PERMISSION_NORMAL_USER;
                    if (currentUser != null) {
                        userType = currentUser.getUserType();
                    }
                    boxInitConfigBeanjsonObject.put("userType", userType);

                    boxInitConfigBeanjsonObject.put("boxName",
                            boxes.getBoxName());
                    boxInitConfigBeanjsonObject.put("customerId",
                            boxes.getCustomerId());
                    QRCodeContentCustomerService qrCodeContentCustomerService = new QRCodeContentCustomerService();
                    QRCodeContentCustomer qrCodeContentCustomer = qrCodeContentCustomerService
                            .findQRCodeContentCustomerByCustomerId(boxes
                                    .getCustomerId());
                    // 如果商户有自定义二维码内容，则用商户自定义的，否则用智购猫平台默认的二维码内容
                    if (qrCodeContentCustomer != null) {
                        boxInitConfigBeanjsonObject.put("qRCodeContent",
                                qrCodeContentCustomer.getqRCodeContentPrefix()
                                        + boxes.getBoxId());
                    } else {
                        boxInitConfigBeanjsonObject.put(
                                "qRCodeContent",
                                ResponseStatus.QRCODE_CONTENT_PREFIX
                                        + boxes.getBoxId());
                    }

                    // 是否支持门店人脸识别免密功能(目前暂时定义为当box的shopId=0
                    // 或者是第三方对接(Yoho)或小e均不支持)
                    int shopFaceDetectSupport = 0;
                    CustomerService customerService = new CustomerService();
                    Customer customer = customerService
                            .findCustomerByCustomerId(boxes.getCustomerId());
                    if (boxes.getShopId() != 0) {
                        if (customer != null) {
                            int cooperationMode = customer.getCooperationMode();
                            if (cooperationMode != ResponseStatus.COOPERATIONMODE_SELF) {
                                shopFaceDetectSupport = 0;
                            } else {
                                // boxShop!=0 并且非第三方合作模式
                                shopFaceDetectSupport = 1;
                            }
                        } else {
                            shopFaceDetectSupport = 0;
                        }
                    } else {
                        shopFaceDetectSupport = 0;
                    }
                    boxInitConfigBeanjsonObject.put("shopFaceDetectSupport",
                            shopFaceDetectSupport);

                    SerializeBoxBodyService serializeBoxBodyService = new SerializeBoxBodyService();
                    SerializeBoxBody serializeBoxBody = serializeBoxBodyService
                            .findSerializeBoxBodyBySerializeBoxBodyId(boxes
                                    .getSerializeBoxBodyId());

                    BoxStructureService boxStructureService = new BoxStructureService();
                    BoxStructure boxStructure = boxStructureService
                            .findBoxStructureByStructureId(serializeBoxBody
                                    .getStructureId());

                    boxInitConfigBeanjsonObject.put("structureName",
                            boxStructure.getStructureName());
                    boxInitConfigBeanjsonObject.put("calibrationWeight",
                            boxStructure.getCalibrationWeight());
                    boxInitConfigBeanjsonObject.put("weightError",
                            boxStructure.getWeightError());
                    boxInitConfigBeanjsonObject.put("steadyStateRange",
                            boxStructure.getSteadyStateRange());
                    boxInitConfigBeanjsonObject.put("minK",
                            boxStructure.getMinK());
                    boxInitConfigBeanjsonObject.put("maxK",
                            boxStructure.getMaxK());
                    boxInitConfigBeanjsonObject.put("totalSensor",
                            boxStructure.getTotalSensor());
                    boxInitConfigBeanjsonObject.put("totalCardgoroad",
                            boxStructure.getTotalCardgoroad());
                    List<Integer> sensorList = boxStructureService
                            .getBoxAllSensorList(boxStructure.getStructureId(),
                                    boxStructure.getTotalSensor());
                    boxInitConfigBeanjsonObject
                            .put("sensorCardgoroadStructure",
                                    boxStructureService
                                            .getSensorAndCardgoRoadStructure(sensorList));
                    boxInitConfigBeanjsonObject.put("sensorStructure",
                            boxStructureService.getLayerSensorStructureInfo(
                                    sensorList, boxStructure.getStructureId()));

                    boxInitConfigBeanjsonObject.put("webSocketIpAndPort",
                            ResponseStatus.WEBSOCKET_SERVER_IP_AND_PORT);
                    boxInitConfigBeanjsonObject.put("boxType",
                            boxBody.getBoxType());
                    // 如果是小e对接模式，不支持人脸识别功能
                    if (customer != null
                            && customer.getCooperationMode() == ResponseStatus.COOPERATIONMODE_THIRD_INTERFACE) {
                        boxInitConfigBeanjsonObject.put("faceFunction", 0);
                    } else {
                        boxInitConfigBeanjsonObject.put("faceFunction",
                                boxBody.getFaceFunction());
                    }
                    boxInitConfigBeanjsonObject.put("currentTimeMillis",
                            System.currentTimeMillis());
                    JSONObject boxInitConfigJsonObject = new JSONObject();
                    boxInitConfigJsonObject.put("boxInitConfigInfo",
                            boxInitConfigBeanjsonObject);
                    return boxInitConfigJsonObject.toString();
                }
            } else {
                log.error("Boxes:doInitBoxConfig ics=null!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                return ResponseStatus.FAIL;
            }
        } catch (Exception e) {
            log.error("doInitBoxConfig fail!!!!!!!!!!!!!");
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        return ResponseStatus.FAIL;
    }

    // 分页查询附近柜子逻辑
    public String doFindNearbyBoxes(String fromLat, String fromLng,
            double nearByRange, int pageNumber) {
        BoxesService boxesService = new BoxesService();
        int count = boxesService.findNearByBoxesCount(fromLat, fromLng,
                nearByRange);
        int totalPages = 0;
        // 计算总页数
        if (count % Boxes.PAGE_SIZE == 0) {
            // 对总页数赋值
            totalPages = count / Boxes.PAGE_SIZE;
        } else {
            // 对总页数赋值
            totalPages = count / Boxes.PAGE_SIZE + 1;
        }

        List<Boxes> nearByBoxesList = boxesService.findNearByBoxes(fromLat,
                fromLng, nearByRange, pageNumber);
        log.info("BoxesService->nearByBoxesList.size=" + nearByBoxesList.size());

        if (pageNumber == 1 && nearByBoxesList.size() <= 0) {
            return ResponseStatus.NO_BOXES;
        } else if (pageNumber <= totalPages && nearByBoxesList.size() > 0) {
            JSONObject boxesJsonObject = new JSONObject();
            JSONArray boxesJsonArray = new JSONArray();
            // 同时查询柜子状态
            BoxStatusService boxStatusService = new BoxStatusService();
            for (int i = 0; i < nearByBoxesList.size(); i++) {
                log.info("BoxesService->nearByBoxesList="
                        + nearByBoxesList.get(i));
                Boxes boxes = nearByBoxesList.get(i);
                JSONObject boxBeanJsonObject = JSONObject.fromObject(boxes);
                BoxStatus boxStatusBean = boxStatusService
                        .findBoxStatusByBoxId(boxes.getBoxId());
                int boxState = 0;
                if (boxStatusBean != null) {
                    boxState = boxStatusBean.getState();
                }
                boxBeanJsonObject.put("boxState", boxState);
                if (boxState == 0 || boxState == 1) {
                    boxBeanJsonObject.put("boxStateDescription",
                            BoxStatus.BOX_STATUS_DESCRIPTION_NORMAL);
                } else {
                    boxBeanJsonObject.put("boxStateDescription",
                            BoxStatus.BOX_NEARBY_STATUS_DESCRIPTION_BREAKDOWN);
                }

                String boxAddress = boxes.getBoxAddress();
                boxBeanJsonObject.put("boxAddress", boxAddress);

                double distance = boxes.getDistance();
                // 小于1km按m显示，大于1km按km显示
                if (distance < 1000) {
                    boxBeanJsonObject.put("distance", (int) distance);
                    boxBeanJsonObject.put("distanceUnit", "m");
                } else {
                    boxBeanJsonObject.put("distance", String
                            .valueOf(new BigDecimal(distance / 1000d).setScale(
                                    1, BigDecimal.ROUND_DOWN).doubleValue()));
                    boxBeanJsonObject.put("distanceUnit", "km");
                }

                boxBeanJsonObject.put(
                        "boxEnvironmentPicture",
                        ResponseStatus.SERVER_URL + "/"
                                + boxes.getEnvironmentPicture());
                JSONObject boxJsonObject = new JSONObject();
                boxJsonObject.put("Box", boxBeanJsonObject);
                boxesJsonArray.add(boxJsonObject);
            }
            boxesJsonObject.put("Boxes", boxesJsonArray);
            log.info("BoxesService:->My Boxes=" + boxesJsonObject.toString());
            return boxesJsonObject.toString();
        } else {
            return ResponseStatus.LAST_PAGE;
        }
    }

    // 查询附近柜子，以地图风格展示,（按照微信小程序Map组件api封装数据）
    public String doFindNearbyBoxesOnMap(String fromLat, String fromLng,
            double nearByRange, int pageNumber) {
        BoxesService boxesService = new BoxesService();
        int count = boxesService.findNearByBoxesCount(fromLat, fromLng,
                nearByRange);
        int totalPages = 0;
        // 计算总页数
        if (count % Boxes.PAGE_SIZE == 0) {
            // 对总页数赋值
            totalPages = count / Boxes.PAGE_SIZE;
        } else {
            // 对总页数赋值
            totalPages = count / Boxes.PAGE_SIZE + 1;
        }

        List<Boxes> nearByBoxesList = boxesService.findNearByBoxes(fromLat,
                fromLng, nearByRange, pageNumber);
        log.info("BoxesService->nearByBoxesList.size=" + nearByBoxesList.size());

        if (pageNumber == 1 && nearByBoxesList.size() <= 0) {
            return ResponseStatus.NO_BOXES;
        } else if (pageNumber <= totalPages && nearByBoxesList.size() > 0) {
            JSONObject boxesJsonObject = new JSONObject();
            JSONArray boxesJsonArray = new JSONArray();
            // 地图marks
            JSONArray marksJsonArray = new JSONArray();
            // 自动缩放地图包含所有位置点includePoints
            JSONArray includePointsJsonArray = new JSONArray();
            // 同时查询柜子状态
            BoxStatusService boxStatusService = new BoxStatusService();
            // 获取nearByBoxesList中距离最短的那个
            List<Double> distanceList = new ArrayList<Double>();
            for (int i = 0; i < nearByBoxesList.size(); i++) {
                distanceList.add(nearByBoxesList.get(i).getDistance());
            }
            int nearestBoxIndex = distanceList.indexOf(Collections
                    .min(distanceList));
            for (int i = 0; i < nearByBoxesList.size(); i++) {
                log.info("BoxesService->nearByBoxesList="
                        + nearByBoxesList.get(i) + ",nearestBoxIndex="
                        + nearestBoxIndex);
                Boxes boxes = nearByBoxesList.get(i);
                JSONObject boxBeanJsonObject = JSONObject.fromObject(boxes);
                BoxStatus boxStatusBean = boxStatusService
                        .findBoxStatusByBoxId(boxes.getBoxId());
                int boxState = 0;
                if (boxStatusBean != null) {
                    boxState = boxStatusBean.getState();
                }
                boxBeanJsonObject.put("boxState", boxState);
                if (boxState == 0 || boxState == 1) {
                    boxBeanJsonObject.put("boxStateDescription",
                            BoxStatus.BOX_STATUS_DESCRIPTION_NORMAL);
                } else {
                    boxBeanJsonObject.put("boxStateDescription",
                            BoxStatus.BOX_NEARBY_STATUS_DESCRIPTION_BREAKDOWN);
                }

                String boxAddress = boxes.getBoxAddress();
                boxBeanJsonObject.put("boxAddress", boxAddress);

                double distance = boxes.getDistance();
                // 小于1km按m显示，大于1km按km显示
                if (distance < 1000) {
                    boxBeanJsonObject.put("distance", (int) distance);
                    boxBeanJsonObject.put("distanceUnit", "m");
                } else {
                    boxBeanJsonObject.put("distance", String
                            .valueOf(new BigDecimal(distance / 1000d).setScale(
                                    1, BigDecimal.ROUND_DOWN).doubleValue()));
                    boxBeanJsonObject.put("distanceUnit", "km");
                }

                boxBeanJsonObject.put(
                        "boxEnvironmentPicture",
                        ResponseStatus.SERVER_URL + "/"
                                + boxes.getEnvironmentPicture());
                JSONObject boxJsonObject = new JSONObject();
                boxJsonObject.put("Box", boxBeanJsonObject);
                boxesJsonArray.add(boxJsonObject);

                // 封装Map marker API相关属性
                JSONObject markBeanJsonObject = new JSONObject();
                markBeanJsonObject.put("iconPath",
                        "../../images/ic_map_markers.png");
                markBeanJsonObject.put("id", i);
                markBeanJsonObject.put("latitude",
                        Float.valueOf(boxes.getLatitude()));
                markBeanJsonObject.put("longitude",
                        Float.valueOf(boxes.getLongitude()));
                markBeanJsonObject.put("width", 40);
                markBeanJsonObject.put("height", 40);
                JSONObject calloutJsonObject = new JSONObject();
                if (nearestBoxIndex == i) {
                    calloutJsonObject.put("content", boxes.getBoxName() + "\n"
                            + ResponseStatus.NEAREST_BOX_ON_MAP_TIP);
                    calloutJsonObject.put("color", "#ff7f00");
                } else {
                    calloutJsonObject.put("content", boxes.getBoxName());
                }
                calloutJsonObject.put("textAlign", "center");
                calloutJsonObject.put("display", "ALWAYS");
                markBeanJsonObject.put("callout", calloutJsonObject);

                marksJsonArray.add(markBeanJsonObject);

                // 封装自动缩放地图包含所有位置点includePoints API信息
                JSONObject includePointsBeanJsonObject = new JSONObject();
                includePointsBeanJsonObject.put("latitude",
                        Float.valueOf(boxes.getLatitude()));
                includePointsBeanJsonObject.put("longitude",
                        Float.valueOf(boxes.getLongitude()));
                includePointsJsonArray.add(includePointsBeanJsonObject);
            }
            boxesJsonObject.put("Boxes", boxesJsonArray);
            boxesJsonObject.put("markers", marksJsonArray);
            boxesJsonObject.put("includePoints", includePointsJsonArray);
            log.info("BoxesService:->My Boxes=" + boxesJsonObject.toString());
            return boxesJsonObject.toString();
        } else {
            return ResponseStatus.LAST_PAGE;
        }
    }

    public String doDeviceRegister(String icsIMEI, String boxStructureName,
            String boxBodySnNumber, String workerId) {
        BoxesService boxesService = new BoxesService();
        Boxes boxes = new Boxes();
        ICSService icsService = new ICSService();
        ICS queryICS = icsService.findICSByIMEINumber(icsIMEI);
        SerializeBoxBodyService serializeBoxBodyService = new SerializeBoxBodyService();
        BoxStructureService boxStructureService = new BoxStructureService();
        if (queryICS != null) {
            boxes.setIcsId(queryICS.getIcsId());

            SerializeBoxBody serializeBoxBody = new SerializeBoxBody();
            BoxStructure boxStructure = boxStructureService
                    .findBoxStructureByStructureName(boxStructureName);
            serializeBoxBody.setStructureId(boxStructure.getStructureId());
            serializeBoxBody.setBoxBodySnNumber(boxBodySnNumber);
            serializeBoxBody.setBoxBodyId(1);
            serializeBoxBody.setWorkerId(Integer.valueOf(workerId));
            serializeBoxBody.setUpdateTime(new Date());
            SerializeBoxBody querySerializeBoxBody = serializeBoxBodyService
                    .findSerializeBoxBodyByBoxBodySnNumber(boxBodySnNumber);
            if (querySerializeBoxBody != null) {
                if (serializeBoxBodyService
                        .updateSerializeBoxBody(serializeBoxBody)) {
                    SerializeBoxBody serializeBoxBodyFindByBoxBodySnNumber = serializeBoxBodyService
                            .findSerializeBoxBodyByBoxBodySnNumber(boxBodySnNumber);
                    boxes.setBoxBodyId(serializeBoxBodyFindByBoxBodySnNumber
                            .getBoxBodyId());
                    boxes.setSerializeBoxBodyId(serializeBoxBodyFindByBoxBodySnNumber
                            .getSerializeBoxBodyId());
                    // 生成自定义的BoxID
                    String boxId = boxStructureName
                            + "-"
                            + boxBodySnNumber
                            + "-"
                            + new SimpleDateFormat("yyyyMMddHHmmss")
                                    .format(new Date());
                    boxes.setBoxId(boxId);
                    boxes.setBoxState(ResponseStatus.BOX_OPERATION_STATE_NORMAL);
                    boxes.setPlaceDateTime(new Date());
                    // 如果已经存在一个相同工控机的box，将原来box的工控机释放，工控机ID设置为0
                    Boxes queryBoxesByIcsId = findBoxesByIcsId(queryICS
                            .getIcsId());
                    if (queryBoxesByIcsId != null) {
                        updateICSId(queryICS.getIcsId());
                    }
                    // 如果已经存在一个相同序列号的柜体，将原来box的柜体释放，柜体Id设置为0
                    Boxes queryBoxesSerializeBoxBody = findBoxesBySerializeBoxBodyId(serializeBoxBodyFindByBoxBodySnNumber
                            .getSerializeBoxBodyId());
                    if (queryBoxesSerializeBoxBody != null) {
                        updateSerializeBoxBodyId(serializeBoxBodyFindByBoxBodySnNumber
                                .getSerializeBoxBodyId());
                    }
                    if (boxesService.addBoxes(boxes)) {
                        // 为售货货柜默认商品
                        if (setDefaultGoodsForBox(boxId,
                                ResponseStatus.DEFAULT_BOX_GOODS_BARCODEID)) {
                            return ResponseStatus.SUCCESS;
                        } else {
                            return ResponseStatus.FAIL;
                        }
                    } else {
                        return ResponseStatus.FAIL;
                    }
                } else {
                    return ResponseStatus.FAIL;
                }
            } else {
                if (serializeBoxBodyService
                        .addSerializeBoxBody(serializeBoxBody)) {
                    SerializeBoxBody serializeBoxBodyFindByBoxBodySnNumber = serializeBoxBodyService
                            .findSerializeBoxBodyByBoxBodySnNumber(boxBodySnNumber);
                    boxes.setBoxBodyId(serializeBoxBodyFindByBoxBodySnNumber
                            .getBoxBodyId());
                    boxes.setSerializeBoxBodyId(serializeBoxBodyFindByBoxBodySnNumber
                            .getSerializeBoxBodyId());
                    // 生成自定义的BoxID
                    String boxId = boxStructureName
                            + "-"
                            + boxBodySnNumber
                            + "-"
                            + new SimpleDateFormat("yyyyMMddHHmmss")
                                    .format(new Date());
                    boxes.setBoxId(boxId);
                    boxes.setBoxState(ResponseStatus.BOX_OPERATION_STATE_NORMAL);
                    boxes.setPlaceDateTime(new Date());
                    // 如果已经存在一个相同工控机的box，将原来box的工控机释放，工控机ID设置为0
                    Boxes queryBoxesByIcsId = findBoxesByIcsId(queryICS
                            .getIcsId());
                    if (queryBoxesByIcsId != null) {
                        updateICSId(queryICS.getIcsId());
                    }
                    // 如果已经存在一个相同序列号的柜体，将原来box的柜体释放，柜体Id设置为0
                    Boxes queryBoxesSerializeBoxBody = findBoxesBySerializeBoxBodyId(serializeBoxBodyFindByBoxBodySnNumber
                            .getSerializeBoxBodyId());
                    if (queryBoxesSerializeBoxBody != null) {
                        updateSerializeBoxBodyId(serializeBoxBodyFindByBoxBodySnNumber
                                .getSerializeBoxBodyId());
                    }
                    if (boxesService.addBoxes(boxes)) {
                        // 为售货货柜默认商品
                        if (setDefaultGoodsForBox(boxId,
                                ResponseStatus.DEFAULT_BOX_GOODS_BARCODEID)) {
                            return ResponseStatus.SUCCESS;
                        } else {
                            return ResponseStatus.FAIL;
                        }
                    } else {
                        return ResponseStatus.FAIL;
                    }
                } else {
                    return ResponseStatus.FAIL;
                }
            }
        } else {
            ICS ics = new ICS();
            ics.setImeiNumber(icsIMEI);
            ics.setReleaseTime(new Date());
            if (icsService.addICS(ics)) {
                ICS icsNewAdd = icsService.findICSByIMEINumber(ics
                        .getImeiNumber());
                boxes.setIcsId(icsNewAdd.getIcsId());

                SerializeBoxBody serializeBoxBody = new SerializeBoxBody();
                BoxStructure boxStructure = boxStructureService
                        .findBoxStructureByStructureName(boxStructureName);
                serializeBoxBody
                        .setStructureId((boxStructure.getStructureId()));
                serializeBoxBody.setBoxBodySnNumber(boxBodySnNumber);
                serializeBoxBody.setBoxBodyId(1);
                serializeBoxBody.setWorkerId(Integer.valueOf(workerId));
                serializeBoxBody.setUpdateTime(new Date());
                SerializeBoxBody querySerializeBoxBody = serializeBoxBodyService
                        .findSerializeBoxBodyByBoxBodySnNumber(boxBodySnNumber);
                if (querySerializeBoxBody != null) {
                    if (serializeBoxBodyService
                            .updateSerializeBoxBody(serializeBoxBody)) {
                        SerializeBoxBody serializeBoxBodyFindByBoxBodySnNumber = serializeBoxBodyService
                                .findSerializeBoxBodyByBoxBodySnNumber(boxBodySnNumber);
                        boxes.setBoxBodyId(serializeBoxBodyFindByBoxBodySnNumber
                                .getBoxBodyId());
                        boxes.setSerializeBoxBodyId(serializeBoxBodyFindByBoxBodySnNumber
                                .getSerializeBoxBodyId());
                        // 生成自定义的BoxID
                        String boxId = boxStructureName
                                + "-"
                                + boxBodySnNumber
                                + "-"
                                + new SimpleDateFormat("yyyyMMddHHmmss")
                                        .format(new Date());
                        boxes.setBoxId(boxId);
                        boxes.setBoxState(ResponseStatus.BOX_OPERATION_STATE_NORMAL);
                        boxes.setPlaceDateTime(new Date());
                        // 如果已经存在一个相同工控机的box，将原来box的工控机释放，工控机ID设置为0
                        Boxes queryBoxesByIcsId = findBoxesByIcsId(ics
                                .getIcsId());
                        if (queryBoxesByIcsId != null) {
                            updateICSId(ics.getIcsId());
                        }
                        // 如果已经存在一个相同序列号的柜体，将原来box的柜体释放，柜体Id设置为0
                        Boxes queryBoxesSerializeBoxBody = findBoxesBySerializeBoxBodyId(serializeBoxBodyFindByBoxBodySnNumber
                                .getSerializeBoxBodyId());
                        if (queryBoxesSerializeBoxBody != null) {
                            updateSerializeBoxBodyId(serializeBoxBodyFindByBoxBodySnNumber
                                    .getSerializeBoxBodyId());
                        }
                        if (boxesService.addBoxes(boxes)) {
                            // 为售货货柜默认商品
                            if (setDefaultGoodsForBox(boxId,
                                    ResponseStatus.DEFAULT_BOX_GOODS_BARCODEID)) {
                                return ResponseStatus.SUCCESS;
                            } else {
                                return ResponseStatus.FAIL;
                            }
                        } else {
                            return ResponseStatus.FAIL;
                        }
                    } else {
                        return ResponseStatus.FAIL;
                    }
                } else {
                    if (serializeBoxBodyService
                            .addSerializeBoxBody(serializeBoxBody)) {
                        SerializeBoxBody serializeBoxBodyFindByBoxBodySnNumber = serializeBoxBodyService
                                .findSerializeBoxBodyByBoxBodySnNumber(boxBodySnNumber);
                        boxes.setBoxBodyId(serializeBoxBodyFindByBoxBodySnNumber
                                .getBoxBodyId());
                        boxes.setSerializeBoxBodyId(serializeBoxBodyFindByBoxBodySnNumber
                                .getSerializeBoxBodyId());
                        // 生成自定义的BoxID
                        String boxId = boxStructureName
                                + "-"
                                + boxBodySnNumber
                                + "-"
                                + new SimpleDateFormat("yyyyMMddHHmmss")
                                        .format(new Date());
                        boxes.setBoxId(boxId);
                        boxes.setBoxState(ResponseStatus.BOX_OPERATION_STATE_NORMAL);
                        boxes.setPlaceDateTime(new Date());
                        // 如果已经存在一个相同工控机的box，将原来box的工控机释放，工控机ID设置为0
                        Boxes queryBoxesByIcsId = findBoxesByIcsId(ics
                                .getIcsId());
                        if (queryBoxesByIcsId != null) {
                            updateICSId(ics.getIcsId());
                        }
                        // 如果已经存在一个相同序列号的柜体，将原来box的柜体释放，柜体Id设置为0
                        Boxes queryBoxesSerializeBoxBody = findBoxesBySerializeBoxBodyId(serializeBoxBodyFindByBoxBodySnNumber
                                .getSerializeBoxBodyId());
                        if (queryBoxesSerializeBoxBody != null) {
                            updateSerializeBoxBodyId(serializeBoxBodyFindByBoxBodySnNumber
                                    .getSerializeBoxBodyId());
                        }
                        if (boxesService.addBoxes(boxes)) {
                            // 为售货货柜默认商品
                            if (setDefaultGoodsForBox(boxId,
                                    ResponseStatus.DEFAULT_BOX_GOODS_BARCODEID)) {
                                return ResponseStatus.SUCCESS;
                            } else {
                                return ResponseStatus.FAIL;
                            }
                        } else {
                            return ResponseStatus.FAIL;
                        }
                    } else {
                        return ResponseStatus.FAIL;
                    }
                }
            } else {
                return ResponseStatus.FAIL;
            }
        }
    }

    public boolean setDefaultGoodsForBox(String boxId, String barCodeId) {
        try {
            GoodsCategoryService goodsCategoryService = new GoodsCategoryService();
            GoodsCategory goodsCategory = goodsCategoryService
                    .findGoodsCategoryByBarCodeId(barCodeId);
            Boxes boxes = findBoxesByBoxId(boxId);
            SerializeBoxBodyService serializeBoxBodyService = new SerializeBoxBodyService();
            // 根据SerializeBoxBodyId查出售货机structureId
            SerializeBoxBody serializeBoxBody = serializeBoxBodyService
                    .findSerializeBoxBodyBySerializeBoxBodyId(boxes
                            .getSerializeBoxBodyId());
            BoxStructureService boxStructureService = new BoxStructureService();
            // 获取货道总数
            BoxStructure boxStructure = boxStructureService
                    .findBoxStructureByStructureId(serializeBoxBody
                            .getStructureId());
            BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
            BoxGoodsXXXNewService boxGoodsXXXNewService = new BoxGoodsXXXNewService();
            if (goodsCategory != null) {
                // 删除boXGoodsXXX中所有商品为初始化插入做准备
                if (boxGoodsXXXService.deleteAllBoxGoodsXXX(boxId)) {
                    // boxGoodsXXX默认商品
                    for (int i = 0; i < boxStructure.getTotalCardgoroad(); i++) {
                        BoxGoodsXXX boxGoodsXXX = new BoxGoodsXXX();
                        boxGoodsXXX.setBoxId(boxId);
                        boxGoodsXXX.setBarCodeId(barCodeId);
                        boxGoodsXXX.setGoodsName(goodsCategory.getGoodsName());
                        boxGoodsXXX.setCardgoRoadId(i);
                        boxGoodsXXX.setWeight(goodsCategory.getWeight());
                        boxGoodsXXX.setStockNumber(0);
                        boxGoodsXXX.setBrandCompany(goodsCategory
                                .getBrandCompany());
                        boxGoodsXXX.setSalesMode(goodsCategory.getSalesMode());
                        boxGoodsXXX.setGoodsPrice(goodsCategory
                                .getRecommendedRetailPrice());
                        boxGoodsXXX.setGoodsDiscount(new BigDecimal(1));
                        boxGoodsXXX.setFavourable(new BigDecimal(0));
                        boxGoodsXXX.setMakeDate(new Date());
                        boxGoodsXXX
                                .setExpiryTime(goodsCategory.getExpiryTime());
                        boxGoodsXXX.setGoodsPicture(goodsCategory
                                .getGoodsPicture());
                        boxGoodsXXX.setDeliveryManagerId(0);
                        boxGoodsXXX.setDeliveryTime(new Date());
                        boxGoodsXXXService.addBoxGoodsXXX(boxId,
                                ResponseStatus.BOX_TYPE_WEIGHT, boxGoodsXXX);
                    }
                }

                // 删除boXGoodsXXXNew中所有商品为初始化插入做准备
                if (boxGoodsXXXNewService.deleteAllBoxGoodsXXXNew(boxId)) {
                    // boxGoodsXXXNew默认商品
                    for (int i = 0; i < boxStructure.getTotalCardgoroad(); i++) {
                        BoxGoodsXXXNew boxGoodsXXXNew = new BoxGoodsXXXNew();
                        boxGoodsXXXNew.setBoxId(boxId);
                        boxGoodsXXXNew.setBarCodeId(barCodeId);
                        boxGoodsXXXNew.setGoodsName(goodsCategory
                                .getGoodsName());
                        boxGoodsXXXNew.setCardgoRoadId(i);
                        boxGoodsXXXNew.setWeight(goodsCategory.getWeight());
                        boxGoodsXXXNew.setStockNumber(0);
                        boxGoodsXXXNew.setBrandCompany(goodsCategory
                                .getBrandCompany());
                        boxGoodsXXXNew.setSalesMode(goodsCategory
                                .getSalesMode());
                        boxGoodsXXXNew.setGoodsPrice(goodsCategory
                                .getRecommendedRetailPrice());
                        boxGoodsXXXNew.setGoodsDiscount(new BigDecimal(1));
                        boxGoodsXXXNew.setFavourable(new BigDecimal(0));
                        boxGoodsXXXNew.setMakeDate(new Date());
                        boxGoodsXXXNew.setExpiryTime(goodsCategory
                                .getExpiryTime());
                        boxGoodsXXXNew.setGoodsPicture(goodsCategory
                                .getGoodsPicture());
                        boxGoodsXXXNew.setDeliveryManagerId(0);
                        boxGoodsXXXNew.setDeliveryTime(new Date());
                        boxGoodsXXXNewService.addBoxGoodsXXX(boxId,
                                ResponseStatus.BOX_TYPE_WEIGHT, boxGoodsXXXNew);
                    }
                }
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String doBoxRegisterBoxIdentify(String boxId) {
        Boxes boxes = findBoxesByBoxId(boxId);
        if (boxes != null) {
            return ResponseStatus.SUCCESS;
        } else {
            return ResponseStatus.FAIL;
        }
    }

    /**
     * 更新box表中售货机经纬度信息
     */
    public boolean updateLocationLatitudeAndLongitude(String boxId,
            String latitude, String longitude) {
        // 将百度经纬度转化为腾讯经纬度
        TencentLocationService tencentLocationService = new TencentLocationService();
        String latlngString = tencentLocationService.translateLatAndLng(
                latitude, longitude);
        if (!latlngString.equals(ResponseStatus.FAIL)) {
            String qqLat = JSONObject.fromObject(latlngString).getString("lat");
            String qqLng = JSONObject.fromObject(latlngString).getString("lng");
            if (updateLocationLatitudeAndLongitudeByBoxId(boxId, qqLat, qqLng)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public String doGetBoxName(String boxId) {
        Boxes boxes = findBoxesByBoxId(boxId);
        if (boxes != null) {
            return boxes.getBoxName();
        } else {
            return ResponseStatus.FAIL;
        }
    }

    /**
     * 获取平台默认广告信息
     * 
     * @return
     */
    public String doGetPlatformAdInfo() {
        RollingAdPlatformService rollingAdPlatformService = new RollingAdPlatformService();
        if (!rollingAdPlatformService.getRollingAdPlatform().equals(
                ResponseStatus.FAIL)) {
            JSONArray rollingAdUrljsonArray = JSONArray
                    .fromObject(rollingAdPlatformService.getRollingAdPlatform());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rollingAdUrls", rollingAdUrljsonArray);
            return jsonObject.toString();
        } else {
            return ResponseStatus.FAIL;
        }
    }

    public String doGetCustomerInfo(String boxId) {
        // 1.获取轮播广告图片URL
        // 1-1.优先获取box级别的广告，再获取商户级别的广告，再获取智购猫平台级别的广告
        // 2.获取boxName
        BoxesService boxesService = new BoxesService();
        Boxes boxes = boxesService.findBoxesByBoxId(boxId);
        RollingAdCustomerService rollingAdCustomerService = new RollingAdCustomerService();
        RollingAdPlatformService rollingAdPlatformService = new RollingAdPlatformService();
        JSONObject jsonObject = new JSONObject();
        JSONArray rollingAdUrljsonArray = null;
        // 获取轮播广告URL
        RollingAdBoxService rollingAdBoxService = new RollingAdBoxService();
        if (boxes != null) {
            if (!rollingAdBoxService.getRollingAdBox(boxId).equals(
                    ResponseStatus.FAIL)) {
                rollingAdUrljsonArray = JSONArray
                        .fromObject(rollingAdBoxService.getRollingAdBox(boxId));
            } else if (!rollingAdCustomerService.getRollingAdCustomer(
                    boxes.getCustomerId()).endsWith(ResponseStatus.FAIL)) {
                rollingAdUrljsonArray = JSONArray
                        .fromObject(rollingAdCustomerService
                                .getRollingAdCustomer(boxes.getCustomerId()));
            } else if (!rollingAdPlatformService.getRollingAdPlatform().equals(
                    ResponseStatus.FAIL)) {
                rollingAdUrljsonArray = JSONArray
                        .fromObject(rollingAdPlatformService
                                .getRollingAdPlatform());
            }
            // 获取boxName
            jsonObject.put("boxName", boxes.getBoxName());
            // 获取商户简称
            CustomerService customerService = new CustomerService();
            Customer customer = customerService.findCustomerByCustomerId(boxes
                    .getCustomerId());
            jsonObject.put("customerShortName", customer.getShortName());
            if (rollingAdUrljsonArray != null) {
                jsonObject.put("rollingAdUrls", rollingAdUrljsonArray);
            } else {
                jsonObject.put("rollingAdUrls", ResponseStatus.FAIL);
            }
            return jsonObject.toString();
        } else {
            return ResponseStatus.FAIL;
        }
    }

    public String doShutDownIdentifyWithBoxId(String boxId) {
        Boxes boxes = findBoxesByBoxId(boxId);
        if (boxes != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("boxId", boxId);
            return jsonObject.toString();
        } else {
            return ResponseStatus.FAIL;
        }
    }

    public String doShutDownIdentifyWithBoxBodySnNumber(String boxBodySnNumber) {
        SerializeBoxBodyService serializeBoxBodyService = new SerializeBoxBodyService();
        SerializeBoxBody serializeBoxBody = serializeBoxBodyService
                .findSerializeBoxBodyByBoxBodySnNumber(boxBodySnNumber);
        if (serializeBoxBody != null) {
            Boxes boxes = findBoxesBySerializeBoxBodyId(serializeBoxBody
                    .getSerializeBoxBodyId());
            if (boxes != null) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("boxId", boxes.getBoxId());
                return jsonObject.toString();
            } else {
                return ResponseStatus.FAIL;
            }
        } else {
            return ResponseStatus.FAIL;
        }
    }

    public String doFindCustomerBoxes(String customerId) {
        List<Boxes> boxesList = findBoxesByCustomerId(Integer
                .valueOf(customerId));
        if (boxesList.size() > 0) {
            JSONObject jsonObject = new JSONObject();
            JSONArray boxJsonArray = new JSONArray();
            for (int i = 0; i < boxesList.size(); i++) {
                JSONObject boxjsonObject = JSONObject.fromObject(boxesList
                        .get(i));
                boxJsonArray.add(boxjsonObject);
            }
            jsonObject.put("Boxes", boxJsonArray);
            return jsonObject.toString();
        } else {
            return ResponseStatus.FAIL;
        }
    }

    public String doGetCustomerShoppingInfoBoxes(String customerId) {
        ShoppingInfoService shoppingInfoService = new ShoppingInfoService();
        List<Boxes> boxesList = findBoxesByCustomerId(Integer
                .valueOf(customerId));
        JSONArray boxJsonArray = new JSONArray();
        for (int i = 0; i < boxesList.size(); i++) {
            Boxes boxes = boxesList.get(i);
            JSONObject boxjsonObject = JSONObject.fromObject(boxes);
            // 查出当前售货机未处理异常条数
            int unFinishedExceptionCount = shoppingInfoService
                    .findShoppingInfosCountByBoxIdAndState(boxes.getBoxId(),
                            ResponseStatus.SHOPPING_INFO_STATE_CLOSED);
            boxjsonObject.put("unFinishedExceptionNumber",
                    unFinishedExceptionCount);
            boxJsonArray.add(boxjsonObject);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Boxes", boxJsonArray);
        return jsonObject.toString();
    }

}
