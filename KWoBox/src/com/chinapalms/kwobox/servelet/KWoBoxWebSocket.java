package com.chinapalms.kwobox.servelet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.chinapalms.kwobox.devicemonitor.DeviceMonitorService;
import com.chinapalms.kwobox.javabean.BoxGoodsXXX;
import com.chinapalms.kwobox.javabean.BoxStatus;
import com.chinapalms.kwobox.javabean.Boxes;
import com.chinapalms.kwobox.javabean.CardgoroadGoodsCalibration;
import com.chinapalms.kwobox.javabean.CurrentUser;
import com.chinapalms.kwobox.javabean.ExceptionShopping;
import com.chinapalms.kwobox.service.BoxGoodsXXXService;
import com.chinapalms.kwobox.service.BoxStatusService;
import com.chinapalms.kwobox.service.BoxesService;
import com.chinapalms.kwobox.service.CardgoroadGoodsCalibrationService;
import com.chinapalms.kwobox.service.CloseRecordService;
import com.chinapalms.kwobox.service.CurrentUserService;
import com.chinapalms.kwobox.service.ExceptionShoppingService;
import com.chinapalms.kwobox.service.OpenRecordService;
import com.chinapalms.kwobox.service.UserService;
import com.chinapalms.kwobox.service.WeightSensorStatusService;
import com.chinapalms.kwobox.utils.JsonParseUtil;
import com.chinapalms.kwobox.utils.ResponseStatus;
import com.custom.kwobox.service.CustomWebSocketService;

/**
 * 该注解用来指定一个URI，客户端可以通过这个URI来连接到WebSocket。
 * 并传递customCategory和boxId参数类似Servlet的注解mapping。无需在web.xml中配置。
 * 
 * @author yi.wang customCatory: 客户端类型 boxId: 客户端ID:微信客户端用'WeChat'表示,
 *         支付宝客户端用'Alipay'表示,Android 客户端用 'Android'表示
 */
@ServerEndpoint("/Websocket/{customCategory}/{boxId}")
public class KWoBoxWebSocket {

    Log log = LogFactory.getLog(KWoBoxWebSocket.class);

    /**
     * 连接建立成功调用的方法 连接建立时保存session到HashMap中
     * 
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam("customCategory") String customCategory,
            @PathParam("boxId") String boxId, Session session) {
        try {
            String mapKey = customCategory + "_" + boxId;
            SessionMapFactory.getInstance().addSession(mapKey, session);
            log.info("webSocket onOpen: customCategory=" + customCategory
                    + ", boxId=" + boxId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接关闭调用的方法 连接关闭时将当前Session从HashMap中移除
     */
    @OnClose
    public void onClose(@PathParam("customCategory") String customCategory,
            @PathParam("boxId") String boxId, Session session) {
        try {
            String mapKey = customCategory + "_" + boxId;
            log.info("webSocket onClose: customCategory=" + customCategory
                    + ", boxId=" + boxId);
            SessionMapFactory.getInstance().removeSession(mapKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 收到客户端消息后调用的方法
     * 
     * @param message
     *            客户端发送过来的消息
     * @param session
     *            可选的参数
     */
    @OnMessage
    public void onMessage(@PathParam("customCategory") String customCategory,
            @PathParam("boxId") String boxId, String message, Session session) {
        try {
            log.info("webSocket onMessage: customCategory=" + customCategory
                    + ", boxId=" + boxId + ", message=" + message);
            if (customCategory != null) {
                // 处理微信到Android的逻辑
                if (customCategory.equals(ResponseStatus.CUSTOM_CATEGORY_WX)
                        || customCategory
                                .equals(ResponseStatus.CUSTOM_CATEGORY_ALIPAY)) {
                    if (message != null) {
                        if (message.equals("userOpen")) {
                            log.info("webSocket onMessage: customCategory="
                                    + customCategory + ", boxId=" + boxId
                                    + ", notifyAndroidOpenDoor for user");
                            notifyAndroidOpenDoor(
                                    ResponseStatus.OPEN_DOOR_REQUEST_SERIALNUMBER_NORMAL,
                                    ResponseStatus.CUSTOM_CATEGORY_ANDROID,
                                    boxId,
                                    ResponseStatus.PERMISSION_NORMAL_USER);
                            // 理货员理货逻辑
                        } else if (message.equals("managerSortGoodsOpen")) {
                            log.info("webSocket onMessage: customCategory="
                                    + customCategory
                                    + ", boxId="
                                    + boxId
                                    + ", notifyAndroidOpenDoor for manager to sortGoods");
                            notifyAndroidOpenDoorForManager(
                                    ResponseStatus.CUSTOM_CATEGORY_ANDROID,
                                    boxId, ResponseStatus.PERMISSION_MANAGER,
                                    ResponseStatus.BOX_MANAGER_SORT_GOODS);
                            // 理货员补货逻辑
                        } else if (message.equals("managerAddGoodsOpen")) {
                            log.info("webSocket onMessage: customCategory="
                                    + customCategory
                                    + ", boxId="
                                    + boxId
                                    + ", notifyAndroidOpenDoor for manager to addGoods");
                            notifyAndroidOpenDoorForManager(
                                    ResponseStatus.CUSTOM_CATEGORY_ANDROID,
                                    boxId, ResponseStatus.PERMISSION_MANAGER,
                                    ResponseStatus.BOX_MANAGER_ADD_GOODS);
                            // 通知主控售货机货道商品信息已更新，使主控同步售货机信息
                        } else if (message.equals("updateCurrentBoxGoods")) {
                            log.info("webSocket onMessage: customCategory="
                                    + customCategory + ", boxId=" + boxId
                                    + ", notifyAndroidUpdateCurrentBoxGoods");
                            notifyAndroidUpdateCurrentBoxGoods(
                                    ResponseStatus.CUSTOM_CATEGORY_ANDROID,
                                    boxId);
                        }
                    }
                    // 处理Android到微信的逻辑
                } else if (customCategory
                        .equals(ResponseStatus.CUSTOM_CATEGORY_ANDROID)) {
                    String whichCustomToSendMsg = null;
                    String customType = null;
                    CurrentUserService currentUserService = new CurrentUserService();
                    CurrentUser currentUser = currentUserService
                            .findCurrentUserByBoxId(boxId);
                    if (currentUser != null) {
                        customType = currentUser.getCustomType();
                    }
                    if (customType != null) {
                        if (customType
                                .equals(ResponseStatus.CUSTOM_CATEGORY_WX)) {
                            whichCustomToSendMsg = ResponseStatus.CUSTOM_CATEGORY_WX;
                        } else {
                            whichCustomToSendMsg = ResponseStatus.CUSTOM_CATEGORY_ALIPAY;
                        }
                    }
                    if (message != null) {
                        JSONObject jsonObject = JSONObject.fromObject(message);
                        // RFID方案，传递RFID值作为GoodsId
                        if (jsonObject.containsKey("SaledGoodsIds")) {
                            if (jsonObject.containsValue("noGoodsId")) {
                                notifyWXNoGoodsSaled(null,
                                        whichCustomToSendMsg, boxId, null, null);
                                log.info("webSocket onMessage RFID notifyWXNoGoodsSaled: customCategory="
                                        + customCategory
                                        + ", boxId="
                                        + boxId
                                        + ", customType=" + customType);
                            } else {
                                List<JSONObject> goodsIdsJsonList = JsonParseUtil
                                        .parseJsonObjectToObjectList(
                                                jsonObject, "SaledGoodsIds");
                                notifyWXWhatGoodsSaled(
                                        null,
                                        whichCustomToSendMsg,
                                        boxId,
                                        null,
                                        null,
                                        ResponseStatus.BOX_TYPE_HIGH_FREQUENCY_RFID,
                                        goodsIdsJsonList, true);
                                log.info("webSocket onMessage RFID notifyWXWhatGoodsSaled: customCategory="
                                        + customCategory
                                        + ", boxId="
                                        + boxId
                                        + ", customType="
                                        + customType
                                        + ", goodsIdsJsonList.size="
                                        + goodsIdsJsonList.size());
                            }
                            // 称重方案：传递barCodeId:商品条形码ID;cardgoRoadId:称重售货柜货到号;goodsNumber:每个货道上商品更新数量作为商品更改信息
                        } else if (jsonObject.containsKey("SaledGoodsObjects")) {
                            if (jsonObject.containsValue("noGoodsObject")) {
                                notifyWXNoGoodsSaled(null,
                                        whichCustomToSendMsg, boxId, null, null);
                                log.info("webSocket onMessage WEIGHT notifyWXNoGoodsSaled: customCategory="
                                        + customCategory
                                        + ", boxId="
                                        + boxId
                                        + ", customType=" + customType);
                            } else {
                                List<JSONObject> goodsBarCodeIdsJsonList = JsonParseUtil
                                        .parseJsonObjectToObjectList(
                                                jsonObject, "SaledGoodsObjects");
                                notifyWXWhatGoodsSaled(null,
                                        whichCustomToSendMsg, boxId, null,
                                        null, ResponseStatus.BOX_TYPE_WEIGHT,
                                        goodsBarCodeIdsJsonList, true);
                                log.info("webSocket onMessage WEIGHT notifyWXWhatGoodsSaled: customCategory="
                                        + customCategory
                                        + ", boxId="
                                        + boxId
                                        + ", customType="
                                        + customType
                                        + ", goodsIdsJsonList.size="
                                        + goodsBarCodeIdsJsonList.size());
                            }
                            // RFID方案上货逻辑：商品RFID作为更新商品的标识
                        } else if (jsonObject.containsKey("UpdatedGoodsIds")) {
                            JSONObject addedGoodsJsonObject = jsonObject
                                    .getJSONArray("UpdatedGoodsIds")
                                    .getJSONObject(0);
                            JSONObject removedGoodsJsonObject = jsonObject
                                    .getJSONArray("UpdatedGoodsIds")
                                    .getJSONObject(1);
                            JSONObject allGoodsJsonObject = jsonObject
                                    .getJSONArray("UpdatedGoodsIds")
                                    .getJSONObject(2);
                            log.info("webSocket onMessage RFID UpdatedGoodsIds: customCategory="
                                    + customCategory
                                    + ", boxId="
                                    + boxId
                                    + ", customType="
                                    + customType
                                    + ", addedGoodsJsonObject="
                                    + addedGoodsJsonObject.toString()
                                    + ", removedGoodsJsonObject="
                                    + removedGoodsJsonObject.toString()
                                    + ", allGoodsJsonObject="
                                    + allGoodsJsonObject.toString());
                            if (addedGoodsJsonObject.containsValue("noGoodsId")
                                    && removedGoodsJsonObject
                                            .containsValue("noGoodsId")) {
                                // 无商品上货
                                notifyWXNoGoodsUpdated(
                                        whichCustomToSendMsg,
                                        boxId,
                                        ResponseStatus.BOX_TYPE_HIGH_FREQUENCY_RFID,
                                        allGoodsJsonObject);
                            } else if (!(addedGoodsJsonObject
                                    .containsValue("noGoodsId"))
                                    && removedGoodsJsonObject
                                            .containsValue("noGoodsId")) {
                                // 有商品上货，无商品拿出
                                notifyWXWhatGoodsUpdated(
                                        whichCustomToSendMsg,
                                        boxId,
                                        ResponseStatus.BOX_TYPE_HIGH_FREQUENCY_RFID,
                                        addedGoodsJsonObject, true,
                                        removedGoodsJsonObject, false,
                                        allGoodsJsonObject);

                            } else if (addedGoodsJsonObject
                                    .containsValue("noGoodsId")
                                    && !(removedGoodsJsonObject
                                            .containsValue("noGoodsId"))) {
                                // 无商品上货，有商品拿出
                                notifyWXWhatGoodsUpdated(
                                        whichCustomToSendMsg,
                                        boxId,
                                        ResponseStatus.BOX_TYPE_HIGH_FREQUENCY_RFID,
                                        addedGoodsJsonObject, false,
                                        removedGoodsJsonObject, true,
                                        allGoodsJsonObject);
                            } else if (!(addedGoodsJsonObject
                                    .containsValue("noGoodsId"))
                                    && !(removedGoodsJsonObject
                                            .containsValue("noGoodsId"))) {
                                // 既有商品上货，又有商品拿出
                                notifyWXWhatGoodsUpdated(
                                        whichCustomToSendMsg,
                                        boxId,
                                        ResponseStatus.BOX_TYPE_HIGH_FREQUENCY_RFID,
                                        addedGoodsJsonObject, true,
                                        removedGoodsJsonObject, true,
                                        allGoodsJsonObject);
                            }
                            // 称重方案上货逻辑：商品barCodeId，货单号和货道号对应的更新商品数量作为更新商品的标识
                        } else if (jsonObject
                                .containsKey("UpdatedGoodsObjects")) {
                            JSONObject addedGoodsJsonObject = jsonObject
                                    .getJSONArray("UpdatedGoodsObjects")
                                    .getJSONObject(0);
                            JSONObject removedGoodsJsonObject = jsonObject
                                    .getJSONArray("UpdatedGoodsObjects")
                                    .getJSONObject(1);
                            JSONObject allGoodsJsonObject = jsonObject
                                    .getJSONArray("UpdatedGoodsObjects")
                                    .getJSONObject(2);
                            log.info("webSocket onMessage WEIGHT UpdatedGoodsObjects: customCategory="
                                    + customCategory
                                    + ", boxId="
                                    + boxId
                                    + ", customType="
                                    + customType
                                    + ", addedGoodsJsonObject="
                                    + addedGoodsJsonObject.toString()
                                    + ", removedGoodsJsonObject="
                                    + removedGoodsJsonObject.toString()
                                    + ", allGoodsJsonObject="
                                    + allGoodsJsonObject.toString());

                            // Android主控对上下架商品不做处理，由服务器通过allGoodsJsonObject和数据库库存商品做对比，提取出商品上下架信息
                            JSONArray addedAndRemoveGoodsJsonArray = getAddedAndRemovedGoodsJsonObject(
                                    boxId, allGoodsJsonObject);

                            if (addedAndRemoveGoodsJsonArray.size() > 0) {
                                // 将上下架JsonObject重新赋值
                                addedGoodsJsonObject = addedAndRemoveGoodsJsonArray
                                        .getJSONObject(0);
                                removedGoodsJsonObject = addedAndRemoveGoodsJsonArray
                                        .getJSONObject(1);
                            }

                            if (addedGoodsJsonObject
                                    .containsValue("noGoodsObject")
                                    && removedGoodsJsonObject
                                            .containsValue("noGoodsObject")) {
                                // 无商品上货
                                notifyWXNoGoodsUpdated(whichCustomToSendMsg,
                                        boxId, ResponseStatus.BOX_TYPE_WEIGHT,
                                        allGoodsJsonObject);
                            } else if (!(addedGoodsJsonObject
                                    .containsValue("noGoodsObject"))
                                    && removedGoodsJsonObject
                                            .containsValue("noGoodsObject")) {
                                // 有商品上货，无商品拿出
                                notifyWXWhatGoodsUpdated(whichCustomToSendMsg,
                                        boxId, ResponseStatus.BOX_TYPE_WEIGHT,
                                        addedGoodsJsonObject, true,
                                        removedGoodsJsonObject, false,
                                        allGoodsJsonObject);

                            } else if (addedGoodsJsonObject
                                    .containsValue("noGoodsObject")
                                    && !(removedGoodsJsonObject
                                            .containsValue("noGoodsObject"))) {
                                // 无商品上货，有商品拿出
                                notifyWXWhatGoodsUpdated(whichCustomToSendMsg,
                                        boxId, ResponseStatus.BOX_TYPE_WEIGHT,
                                        addedGoodsJsonObject, false,
                                        removedGoodsJsonObject, true,
                                        allGoodsJsonObject);
                            } else if (!(addedGoodsJsonObject
                                    .containsValue("noGoodsObject"))
                                    && !(removedGoodsJsonObject
                                            .containsValue("noGoodsObject"))) {
                                // 既有商品上货，又有商品拿出
                                notifyWXWhatGoodsUpdated(whichCustomToSendMsg,
                                        boxId, ResponseStatus.BOX_TYPE_WEIGHT,
                                        addedGoodsJsonObject, true,
                                        removedGoodsJsonObject, true,
                                        allGoodsJsonObject);
                            }
                            // Android主控更新完售货机商品信息后反馈结果
                        } else if (jsonObject
                                .containsKey("UpdateCurrentBoxGoods")) {
                            String androidUpdateResult = jsonObject
                                    .getString("UpdateCurrentBoxGoods");
                            log.info("webSocket onMessage : received the UpdateCurrentBoxGoods from Android: customCategory="
                                    + customCategory
                                    + ", boxId="
                                    + boxId
                                    + ", customType="
                                    + customType
                                    + ", androidUpdateResult="
                                    + androidUpdateResult);
                            // 就收到Android更新结果以后，开始更新本地数据库，并把更新结果反馈给客户端
                            notifyWXUpdateCurrentBoxGoodsResult(
                                    ResponseStatus.CUSTOM_CATEGORY_WX, boxId,
                                    androidUpdateResult);
                            // Android主控更新完售货机货道商品价格以后反馈结果，便于同步更新数据库
                        } else if (jsonObject
                                .containsKey("UpdateCurrentBoxGoodsPrice")) {
                            String androidUpdateResult = jsonObject
                                    .getString("UpdateCurrentBoxGoodsPrice");
                            log.info("webSocket onMessage : received the UpdateCurrentBoxGoodsPrice from Android: customCategory="
                                    + customCategory
                                    + ", boxId="
                                    + boxId
                                    + ", customType="
                                    + customType
                                    + ", androidUpdateResult="
                                    + androidUpdateResult);
                            // 收到Android更新货道商品价格结果以后，开始更新本地数据库
                            doUpdateCurrentBoxGoodsPrice(boxId,
                                    androidUpdateResult);
                            // Android主控更新完货道商品库存以后反馈结果
                        } else if (jsonObject
                                .containsKey("UpdateCardgoRoadGoodsStockNumberInfo")) {
                            String androidUpdateResult = jsonObject
                                    .getString("UpdateCardgoRoadGoodsStockNumberInfo");
                            log.info("webSocket onMessage : received the UpdateCardgoRoadGoodsStockNumberInfo from Android: customCategory="
                                    + customCategory
                                    + ", boxId="
                                    + boxId
                                    + ", customType="
                                    + customType
                                    + ", androidUpdateResult="
                                    + androidUpdateResult);
                            // 就收到Android更新结果以后，开始更新本地数据库，并把更新结果反馈给客户端
                            notifyWXUpdateCardgoRoadGoodsStockNumberResult(
                                    ResponseStatus.CUSTOM_CATEGORY_WX, boxId,
                                    androidUpdateResult);
                        } else if (jsonObject.containsKey("request")) {
                            String request = jsonObject.getString("request");
                            log.info("webSocket onMessage : received the door state from Android: customCategory="
                                    + customCategory
                                    + ", boxId="
                                    + boxId
                                    + ", customType="
                                    + customType
                                    + ", doorState=" + request);
                            if (request.equals("opened")) {
                                // 如果当前用户是一般用户（非理货员）
                                if (currentUser.getUserType().equals(
                                        ResponseStatus.PERMISSION_NORMAL_USER)) {
                                    // 通过boxId查找商户ID，再根据商户ID查找回调Url，返回商户服务器，与商户服务器对接,如果无回调Url，则默认走智购猫平台逻辑
                                    CustomWebSocketService customWebSocketService = new CustomWebSocketService();
                                    String doorOpenedCallbackUrlObject = customWebSocketService
                                            .getCustomerDoorOpenedCallbackUrl(boxId);
                                    log.info("boxId=" + boxId + "userType="
                                            + "normal user"
                                            + ",doorOpenedCallbackUrlObject="
                                            + doorOpenedCallbackUrlObject);
                                    // 如果没有拿到对应商户的回调Url，则默认走智狗猫流程，否则回调商户服务器
                                    if (doorOpenedCallbackUrlObject
                                            .equals(ResponseStatus.FAIL)) {
                                        notifyWXDoorOpened(
                                                whichCustomToSendMsg, boxId);
                                    } else {
                                        // 走商户回调流程,通知商户服务器门已开
                                        customWebSocketService
                                                .notifyCustomServerDoorOpened(
                                                        doorOpenedCallbackUrlObject,
                                                        boxId,
                                                        currentUser
                                                                .getPhoneNumber());
                                    }
                                } else {
                                    // 通过boxId查找商户ID，再根据商户ID查找回调Url，返回商户服务器，与商户服务器对接,如果无回调Url，则默认走智购猫平台逻辑
                                    CustomWebSocketService customWebSocketService = new CustomWebSocketService();
                                    String doorOpenedCallbackUrlObject = customWebSocketService
                                            .getCustomerDoorOpenedCallbackUrl(boxId);
                                    log.info("boxId=" + boxId + "userType="
                                            + "manager"
                                            + ",doorOpenedCallbackUrlObject="
                                            + doorOpenedCallbackUrlObject);
                                    // 如果没有拿到对应商户的回调Url，则默认走智狗猫流程，否则回调商户服务器
                                    if (doorOpenedCallbackUrlObject
                                            .equals(ResponseStatus.FAIL)) {
                                        // 如果当前用户是理货员，继续走智购云流程
                                        notifyWXDoorOpened(
                                                whichCustomToSendMsg, boxId);
                                    } else {
                                        // 为了商户能兼容使用智购云理货小程序进行理货操作，暂时保留智购云小程序通知逻辑流程
                                        notifyWXDoorOpened(
                                                whichCustomToSendMsg, boxId);
                                        // 走商户回调流程,通知商户服务器门已开
                                        try {
                                            customWebSocketService
                                                    .notifyCustomServerDoorOpenedForManager(
                                                            doorOpenedCallbackUrlObject,
                                                            boxId,
                                                            currentUser
                                                                    .getCustomerWorkerId());
                                        } catch (Exception e) {
                                            log.error("try->catch exception:",
                                                    e);
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                // 设置开门记录，并把开门记录数据插入到开门记录表中
                                registerOpenDoorRecord(boxId);
                            } else if (request.equals("closed")) {
                                // 如果当前用户是一般用户（非理货员）
                                if (currentUser.getUserType().equals(
                                        ResponseStatus.PERMISSION_NORMAL_USER)) {
                                    // 通过boxId查找商户ID，再根据商户ID查找回调Url，返回商户服务器，与商户服务器对接,如果无回调Url，则默认走智购猫平台逻辑
                                    CustomWebSocketService customWebSocketService = new CustomWebSocketService();
                                    String doorClosedCallbackUrlObject = customWebSocketService
                                            .getCustomerDoorClosedCallbackUrl(boxId);
                                    log.info("boxId=" + boxId + "userType="
                                            + "normal user"
                                            + ",doorClosedCallbackUrlObject="
                                            + doorClosedCallbackUrlObject);
                                    // 如果没有拿到对应商户的回调Url，则默认走智狗猫流程，否则回调商户服务器
                                    if (doorClosedCallbackUrlObject
                                            .equals(ResponseStatus.FAIL)) {
                                        notifyWXDoorClosed(
                                                whichCustomToSendMsg, boxId);
                                    } else {
                                        // 走商户回调流程,通知商户服务器门已开
                                        customWebSocketService
                                                .notifyCustomServerDoorClosed(
                                                        doorClosedCallbackUrlObject,
                                                        boxId,
                                                        currentUser
                                                                .getPhoneNumber());
                                    }
                                } else {
                                    // 通过boxId查找商户ID，再根据商户ID查找回调Url，返回商户服务器，与商户服务器对接,如果无回调Url，则默认走智购猫平台逻辑
                                    CustomWebSocketService customWebSocketService = new CustomWebSocketService();
                                    String doorClosedCallbackUrlObject = customWebSocketService
                                            .getCustomerDoorClosedCallbackUrl(boxId);
                                    log.info("boxId=" + boxId + "userType="
                                            + "manager"
                                            + ",doorClosedCallbackUrlObject="
                                            + doorClosedCallbackUrlObject);
                                    // 如果没有拿到对应商户的回调Url，则默认走智狗猫流程，否则回调商户服务器
                                    if (doorClosedCallbackUrlObject
                                            .equals(ResponseStatus.FAIL)) {
                                        notifyWXDoorClosed(
                                                whichCustomToSendMsg, boxId);
                                    } else {
                                        // 为了商户能兼容使用智购云理货小程序进行理货操作，暂时保留智购云小程序通知逻辑流程
                                        notifyWXDoorClosed(
                                                whichCustomToSendMsg, boxId);
                                        // 走商户回调流程,通知商户服务器门已开
                                        try {
                                            customWebSocketService
                                                    .notifyCustomServerDoorClosedForManager(
                                                            doorClosedCallbackUrlObject,
                                                            boxId,
                                                            currentUser
                                                                    .getCustomerWorkerId());
                                        } catch (Exception e) {
                                            log.error("try->catch exception:",
                                                    e);
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                // 设置关门记录，并把关门记录数据插入到关门记录表中
                                registerCloseDoorRecord(boxId);
                            } else if (request.equals("boxBusy")) {
                                // 当前售货机还有异常视频未处理完毕时，此时不允许理货员开门,并通知给理货员
                                notifyWXBoxBusy(whichCustomToSendMsg, boxId);
                            } else if (request.equals("resetBoxState")) {
                                resetBoxState(boxId);
                                // 处理异常购物行为动作导致柜体异常
                            } else if (request.equals("boxException")) {
                                notifyWXBoxException(whichCustomToSendMsg,
                                        boxId, 10);
                                // 异常购物行为更新个人征信
                                updatePersonalCredit(
                                        currentUser.getPhoneNumber(),
                                        boxId,
                                        ResponseStatus.PERSONAL_CREDIT_DEDUCT,
                                        ResponseStatus.PERSONAL_CREDIT_DEDUCT_VALUE);
                                // 发送异常购物行为短信给消费者
                                // 发送异常购物行为短信给消费者
                                // IdentifyCodeUtil
                                // .sendTencentQQPersonalCreditSms(
                                // currentUser.getPhoneNumber(),
                                // Math.abs(ResponseStatus.PERSONAL_CREDIT_DEDUCT_VALUE));
                                // 工控机电池未充电（断电报警）,通过短信方式通知管理员
                            } else if (request.equals("notCharging")) {
                                notifyManagerDeviceNotCharging(
                                        whichCustomToSendMsg, boxId);
                                // 工控机上报层架是否断开和恢复状态
                            } else if (request.equals("weightSensorStatus")) {
                                int state = jsonObject.getInt("state");
                                // 保存工控机上报层架是否断开和恢复状态
                                saveWeightSensorStatus(boxId, state);
                                // 工控机上报售货机经纬度信息
                            } else if (request.equals("boxLocation")) {
                                String latitude = jsonObject
                                        .getString("latitude");
                                String longitude = jsonObject
                                        .getString("longitude");
                                // 更新box表中售货机经纬度信息
                                updateLocationLatitudeAndLongitude(boxId,
                                        latitude, longitude);
                                // 工控机处理完异常购物行为以后返回所处理异常购物视频的状态，同步更新数据库为该异常视频已处理状态
                            } else if (request
                                    .equals("setExceptionShoppingState")) {
                                String exceptionId = jsonObject
                                        .getString("exceptionId");
                                updateExceptionShoppingState(
                                        exceptionId,
                                        ResponseStatus.EXCEPTION_HANDLE_STATE_HANDLED);
                            } else if (request.equals("powerOff")) {
                                // 接收到公共机已接收到智购云关机指令，并开始执行关机动作反馈，把设备状态设置为维护中,并通知微信小程序已执行关机指令
                                notifyWXDeviceShutdownSuccess(
                                        ResponseStatus.CUSTOM_CATEGORY_WX,
                                        boxId);
                            } else if (request.equals("heart")) {
                                // 服务器与工控机的心跳以及对心跳的反馈
                                long timeStamp = jsonObject
                                        .getLong("timeStamp");
                                sendPongHeartToAndroid(boxId, timeStamp);
                            }
                        }
                    }
                    // 接收异常购物人工处理客户端信息逻辑
                } else if (customCategory
                        .equals(ResponseStatus.CUSTOM_CATEGORY_EXCEPTION)) {
                    if (message != null) {
                        JSONObject jsonObject = JSONObject.fromObject(message);
                        if (jsonObject.containsKey("shopException")) {
                            log.info("KWoBoxWebSocket->start sendExceptionShoppingInfoBackToAndroid!!!!");
                            String exceptionBoxId = jsonObject.getJSONObject(
                                    "shopException").getString("boxId");
                            String exceptionType = jsonObject.getJSONObject(
                                    "shopException").getString("ExceptionType");
                            String exceptionId = null;
                            try {
                                exceptionId = jsonObject.getJSONObject(
                                        "shopException").getString(
                                        "exceptionId");
                            } catch (Exception e) {
                                log.error("try->catch exception:", e);
                                e.printStackTrace();
                            }
                            JSONArray shoppingArray = jsonObject.getJSONObject(
                                    "shopException").getJSONArray("Shopping");
                            int personalScores = jsonObject.getJSONObject(
                                    "shopException").getInt("score");
                            CurrentUserService currentUserService = new CurrentUserService();
                            CurrentUser currentUser = currentUserService
                                    .findCurrentUserByBoxId(exceptionBoxId);
                            if (personalScores != 0) {
                                // ============对接商户服务器逻辑==========================================================================
                                // 判断是否是第三方对接模式(Yoho模式),如果是Yoho模式，将异常信息回调至商户服务器订单回调页面，以便于商户服务器更新个人征信分和通知商户微信客户端
                                // 通过boxId查找商户ID，再根据商户ID查找回调Url，返回商户服务器，与商户服务器对接,如果无回调Url，则默认走智购猫平台逻辑
                                CustomWebSocketService customWebSocketService = new CustomWebSocketService();
                                String orderCallbackUrlObject = customWebSocketService
                                        .getCustomerOrderCallbackUrl(exceptionBoxId);
                                if (orderCallbackUrlObject
                                        .equals(ResponseStatus.FAIL)) {
                                    // 异常购物行为更新个人征信
                                    updatePersonalCredit(
                                            currentUser.getPhoneNumber(),
                                            exceptionBoxId,
                                            ResponseStatus.PERSONAL_CREDIT_DEDUCT,
                                            -personalScores);
                                    // 发送异常信息到微信客户端
                                    notifyWXBoxException(
                                            ResponseStatus.CUSTOM_CATEGORY_WX,
                                            exceptionBoxId, personalScores);
                                } else {
                                    int urlType = JSONObject.fromObject(
                                            orderCallbackUrlObject).getInt(
                                            "urlType");
                                    // 如果是Yoho代理模式,则把订单信息回调给代理开发服务器
                                    if (urlType == ResponseStatus.COOPERATIONMODE_AGENT) {
                                        customWebSocketService
                                                .notifyCustomerServerExceptionShoppingInfo(
                                                        orderCallbackUrlObject,
                                                        exceptionBoxId,
                                                        currentUser
                                                                .getPhoneNumber(),
                                                        personalScores);
                                    }
                                }
                                // ============对接商户服务器逻辑==========================================================================
                            }
                            // 如果是强制生成订单，就直接拦截并生成订单，否则返回给Android主控进行处理
                            if (exceptionType.equals("force")) {
                                // 生成并发送订单
                                JSONObject saledGoodsObjects = new JSONObject();
                                JSONArray exceptionGoodsJsonArray = new JSONArray();
                                List<Integer> cardgoRoadList = new ArrayList<Integer>();
                                if (shoppingArray.size() > 0) {
                                    for (int i = 0; i < shoppingArray.size(); i++) {
                                        cardgoRoadList.add(shoppingArray
                                                .getJSONObject(i).getInt(
                                                        "position"));
                                    }
                                    Set<Integer> cardgoRoadSet = new HashSet<Integer>(
                                            cardgoRoadList);
                                    for (int uniqueCardgoRoadId : cardgoRoadSet) {
                                        JSONObject exceptionShoppingJsonObject = new JSONObject();
                                        float cardgoRoadGoodsTotalWeight = 0.0f;
                                        for (int i = 0; i < shoppingArray
                                                .size(); i++) {
                                            JSONObject shoppingJsonObject = shoppingArray
                                                    .getJSONObject(i);
                                            if (shoppingJsonObject
                                                    .getInt("position") == uniqueCardgoRoadId) {
                                                exceptionShoppingJsonObject
                                                        .put("barCodeId",
                                                                shoppingJsonObject
                                                                        .getString("barcode"));
                                                exceptionShoppingJsonObject
                                                        .put("cardgoRoadId",
                                                                uniqueCardgoRoadId);
                                                exceptionShoppingJsonObject
                                                        .put("goodsNumber",
                                                                Collections
                                                                        .frequency(
                                                                                cardgoRoadList,
                                                                                uniqueCardgoRoadId));
                                                cardgoRoadGoodsTotalWeight = cardgoRoadGoodsTotalWeight
                                                        + shoppingJsonObject
                                                                .getInt("weight")
                                                        / 1000.0f;
                                            }
                                        }
                                        exceptionShoppingJsonObject.put(
                                                "cardgoRoadGoodsTotalWeight",
                                                cardgoRoadGoodsTotalWeight);
                                        exceptionGoodsJsonArray
                                                .add(exceptionShoppingJsonObject);
                                    }
                                    saledGoodsObjects.put("SaledGoodsObjects",
                                            exceptionGoodsJsonArray);
                                    List<JSONObject> goodsBarCodeIdsJsonList = JsonParseUtil
                                            .parseJsonObjectToObjectList(
                                                    saledGoodsObjects,
                                                    "SaledGoodsObjects");
                                    notifyWXWhatGoodsSaled(null,
                                            ResponseStatus.CUSTOM_CATEGORY_WX,
                                            exceptionBoxId, null, null,
                                            ResponseStatus.BOX_TYPE_WEIGHT,
                                            goodsBarCodeIdsJsonList, false);
                                } else {
                                    notifyWXNoGoodsSaled(null,
                                            ResponseStatus.CUSTOM_CATEGORY_WX,
                                            exceptionBoxId, null, null);
                                }
                                // 先将售货机置为维护中
                                BoxStatusService boxStatusService = new BoxStatusService();
                                BoxStatus boxStatus = new BoxStatus();
                                boxStatus.setBoxId(exceptionBoxId);
                                boxStatus
                                        .setState(ResponseStatus.BOX_STATUS_BROKEN_DOWN);
                                boxStatus.setReportTime(new Date());
                                boxStatusService.updateBoxStatus(boxStatus);
                                // 发短信给理货员，售货柜已发生异常，尽快理货
                                notifyBoxManagerToManageExceptionShoppingBox(
                                        ResponseStatus.CUSTOM_CATEGORY_ANDROID,
                                        exceptionBoxId);
                                // 并且将该异常记录状态设置为已处理状态
                                if (exceptionId != null) {
                                    updateExceptionShoppingState(
                                            exceptionId,
                                            ResponseStatus.EXCEPTION_HANDLE_STATE_HANDLED);
                                }
                            } else if (exceptionType.equals("normal")) {
                                // 发送异常处理客户端传送过来的人工处理以后的异常信息到工控机进行处理
                                sendExceptionShoppingInfoBackToAndroid(
                                        ResponseStatus.CUSTOM_CATEGORY_ANDROID,
                                        exceptionBoxId, jsonObject);
                            }
                        } else if (jsonObject.containsKey("request")) {
                            String request = jsonObject.getString("request");
                            log.info("webSocket onMessage : received the door state from ExceptionServer: to notifyManager the shopping Exception!!!!"
                                    + "request=" + request);
                            if (request
                                    .equals("notifyShoppingExceptionToManager")) {
                                String boxIdException = jsonObject
                                        .getString("boxId");
                                log.info("notifyShoppingExceptionToManager!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
                                        + "boxId=" + boxIdException);
                                CurrentUserService currentUserService = new CurrentUserService();
                                CurrentUser currentUser = currentUserService
                                        .findCurrentUserByBoxId(boxIdException);
                                // 发送异常信息到微信客户端
                                notifyWXBoxException(
                                        ResponseStatus.CUSTOM_CATEGORY_WX,
                                        boxIdException, 10);
                                // 异常购物行为更新个人征信
                                updatePersonalCredit(
                                        currentUser.getPhoneNumber(),
                                        boxIdException,
                                        ResponseStatus.PERSONAL_CREDIT_DEDUCT,
                                        ResponseStatus.PERSONAL_CREDIT_DEDUCT_PUT_OTHERTHING_VALUE);
                                // 先将售货机置为维护中
                                BoxStatusService boxStatusService = new BoxStatusService();
                                BoxStatus boxStatus = new BoxStatus();
                                boxStatus.setBoxId(boxIdException);
                                boxStatus
                                        .setState(ResponseStatus.BOX_STATUS_BROKEN_DOWN);
                                boxStatus.setReportTime(new Date());
                                boxStatusService.updateBoxStatus(boxStatus);
                                // 发短信给理货员，售货柜已发生异常，尽快理货
                                notifyBoxManagerToManageExceptionShoppingBox(
                                        ResponseStatus.CUSTOM_CATEGORY_ANDROID,
                                        boxIdException);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("websocket onMessage exception", e);
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    /**
     * 发生错误时调用
     * 
     * @param session
     * @param error
     */
    @OnError
    public void onError(@PathParam("customCategory") String customCategory,
            @PathParam("boxId") String boxId, Session session, Throwable e) {
    }

    /**
     * 通知Android设备去开门
     * 
     * @param customCategory
     * @param boxId
     */
    private void notifyAndroidOpenDoor(String openRequestSerial,
            String customCategory, String boxId, String userPermission) {
        BoxesService boxService = new BoxesService();
        try {
            boxService.notifyAndroidOpenDoor(openRequestSerial, customCategory,
                    boxId, userPermission);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    /**
     * 理货员扫码开门时区分是理货还是补货
     * 
     * @param customCategory
     * @param boxId
     * @param userPermission
     * @param action
     */
    private void notifyAndroidOpenDoorForManager(String customCategory,
            String boxId, String userPermission, String action) {
        BoxesService boxService = new BoxesService();
        try {
            boxService.notifyAndroidOpenDoorForManager(customCategory, boxId,
                    userPermission, action);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    /**
     * 通知微信客户端门已开启
     * 
     * @param customCategory
     * @param boxId
     */
    private void notifyWXDoorOpened(String customCategory, String boxId) {
        BoxesService boxService = new BoxesService();
        try {
            boxService.notifyWXDoorOpened(customCategory, boxId);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    /**
     * 通知微信客户端门已关闭
     * 
     * @param customCategory
     * @param boxId
     */
    private void notifyWXDoorClosed(String customCategory, String boxId) {
        BoxesService boxService = new BoxesService();
        try {
            boxService.notifyWXDoorClosed(customCategory, boxId);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    /**
     * 当前售货机还有异常视频未处理完毕时，此时不允许理货员开门,并通知给理货员
     * 
     * @param customCategory
     * @param boxId
     */
    private void notifyWXBoxBusy(String customCategory, String boxId) {
        BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
        try {
            boxGoodsXXXService.notifyWXBoxBusy(customCategory, boxId);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    /**
     * 通知Android主控更新售货机货道商品
     * 
     * @param customCategory
     * @param boxId
     */
    private void notifyAndroidUpdateCurrentBoxGoods(String customCategory,
            String boxId) {
        BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
        try {
            boxGoodsXXXService.notifyAndroidUpdateCurrentBoxGoods(
                    customCategory, boxId);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    /**
     * 通知客户端柜子有人为异常行为
     * 
     * @param customCategory
     * @param boxId
     */
    private void notifyWXBoxException(String customCategory, String boxId,
            int deductPersonCredit) {
        BoxesService boxService = new BoxesService();
        try {
            boxService.notifyWXBoxException(customCategory, boxId,
                    deductPersonCredit);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    /**
     * 返回人工处理后的异常购物信息给Android主控进行处理
     * 
     * @param customCategory
     * @param boxId
     * @param exceptionShoppingInfo
     */
    private void sendExceptionShoppingInfoBackToAndroid(String customCategory,
            String boxId, JSONObject exceptionShoppingInfoJsonObject) {
        try {
            Session currentSession = SessionMapFactory.getInstance()
                    .getCurrentSession(customCategory, boxId);
            SessionMapFactory.getInstance().sendMessage(currentSession,
                    exceptionShoppingInfoJsonObject.toString());
            log.info("KWoBoxWebSocket->" + "exceptionShoppingInfoJsonObject="
                    + exceptionShoppingInfoJsonObject.toString());
        } catch (Exception e) {
            log.error("KWoBoxWebSocket sendExceptionShoppingInfoBackToAndroid error:"
                    + e.toString());
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    /**
     * 重置柜子状态
     * 
     * @param boxId
     */
    private void resetBoxState(String boxId) {
        try {
            BoxStatusService boxStatusService = new BoxStatusService();
            boxStatusService.setOpenDoorState(boxId, false);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void notifyWXWhatGoodsSaled(String orderType,
            String customCategory, String boxId, String phoneNumber,
            String openDoorRequestSerialNumber, int boxType,
            List<JSONObject> goodsIdsJsonList,
            boolean addPersonCreditOrPointsRecord) {
        BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
        try {
            boxGoodsXXXService.notifyWXWhatGoodsSaled(orderType,
                    customCategory, boxId, phoneNumber,
                    openDoorRequestSerialNumber, boxType, goodsIdsJsonList,
                    addPersonCreditOrPointsRecord);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void notifyWXNoGoodsSaled(String orderType, String customCategory,
            String boxId, String phoneNumber, String openDoorRequestSerialNumber) {
        BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
        try {
            boxGoodsXXXService.notifyWXNoGoodsSaled(orderType, customCategory,
                    boxId, phoneNumber, openDoorRequestSerialNumber);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    /**
     * 通知微信客户端上货员本次上货情况
     * 
     * @param customCategory
     * @param boxId
     * @param addedGoodsFlag
     * @param addedGoodsJsonObject
     * @param removedGoodsFlag
     * @param removedGoodsJsonObject
     */
    private void notifyWXWhatGoodsUpdated(String customCategory, String boxId,
            int boxType, JSONObject addedGoodsJsonObject,
            boolean addedGoodsFlag, JSONObject removedGoodsJsonObject,
            boolean removedGoodsFlag, JSONObject allGoodsJsonObject) {
        BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
        try {
            boxGoodsXXXService.notifyWXWhatGoodsUpdated(customCategory, boxId,
                    boxType, addedGoodsJsonObject, addedGoodsFlag,
                    removedGoodsJsonObject, removedGoodsFlag,
                    allGoodsJsonObject);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    /**
     * 通知微信客户端上货人员本次无上货
     * 
     * @param customCategory
     * @param boxId
     */
    private void notifyWXNoGoodsUpdated(String customCategory, String boxId,
            int boxType, JSONObject allGoodsJsonObject) {
        BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
        try {
            boxGoodsXXXService.notifyWXNoGoodsUpdated(customCategory, boxId,
                    boxType, allGoodsJsonObject);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    /**
     * 记录开门记录
     * 
     * @param boxId
     */
    private void registerOpenDoorRecord(String boxId) {
        try {
            OpenRecordService openRecordService = new OpenRecordService();
            openRecordService.doAddOpenRecord(boxId);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void registerCloseDoorRecord(String boxId) {
        try {
            CloseRecordService closeRecordService = new CloseRecordService();
            closeRecordService.doCloseOpenRecord(boxId);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void updatePersonalCredit(String phoneNumber, String boxId,
            int changeReason, int changeValue) {
        try {
            UserService userService = new UserService();
            userService.doUpdatePersonalCredit(phoneNumber, boxId,
                    changeReason, changeValue);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    // 就收到Android更新结果以后，开始更新本地数据库，并把更新结果反馈给客户端
    private void notifyWXUpdateCurrentBoxGoodsResult(String customCategory,
            String boxId, String androidUpdateResult) {
        try {
            JSONObject jsonObject = new JSONObject();
            if (androidUpdateResult.equals(ResponseStatus.FAIL)) {
                jsonObject.put("response", ResponseStatus.FAIL);
            } else {
                BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
                String updateResult = boxGoodsXXXService
                        .doUpdateBoxCardgoRoadGoods(boxId);
                jsonObject = JSONObject.fromObject(updateResult);
            }
            Session currentSession = SessionMapFactory.getInstance()
                    .getCurrentSession(customCategory, boxId);
            SessionMapFactory.getInstance().sendMessage(currentSession,
                    jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    // 就收到Android更新结果以后，开始更新本地数据库，并把更新结果反馈给客户端
    private void notifyWXUpdateCardgoRoadGoodsStockNumberResult(
            String customCategory, String boxId, String androidUpdateResult) {
        try {
            BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
            JSONObject jsonObject = new JSONObject();
            if (androidUpdateResult.equals(ResponseStatus.FAIL)) {
                jsonObject.put("response", ResponseStatus.FAIL);
            } else {
                String cardgoRoadId = JSONObject
                        .fromObject(androidUpdateResult).getString(
                                "cardgoRoadId");
                String barCodeId = JSONObject.fromObject(androidUpdateResult)
                        .getString("barCodeId");
                String updateStockNumber = JSONObject.fromObject(
                        androidUpdateResult).getString("updateStockNumber");

                String replenishmentId = null;
                try {
                    replenishmentId = JSONObject
                            .fromObject(androidUpdateResult).getString(
                                    "currentReplenishId");
                } catch (Exception e) {
                    log.error("try->catch exception:", e);
                    e.printStackTrace();
                }

                // 如果当前用户是管理员
                CurrentUserService currentUserService = new CurrentUserService();
                CurrentUser currentUser = currentUserService
                        .findCurrentUserByBoxId(boxId);
                if (currentUser != null
                        && currentUser.getUserType().equals(
                                ResponseStatus.PERMISSION_MANAGER)) {
                    // 校准商品以后将校准记录保存到货道商品校准记录表t_cardgoroad_goods_calibration
                    BoxGoodsXXX boxGoodsXXX = boxGoodsXXXService
                            .findBoxGoodsXXXByBarCodeIdAndCardgoRoadId(boxId,
                                    barCodeId, Integer.valueOf(cardgoRoadId));
                    String goodsName = boxGoodsXXX.getGoodsName();
                    // 校准前商品数量
                    int beforeCalibrationStockNumber = boxGoodsXXX
                            .getStockNumber();
                    // 校准后商品数量
                    int afterCalibrationStockNumber = Integer
                            .valueOf(updateStockNumber);
                    // 校准类型：0:表示不增不减 1:表示减少 2：表示增加
                    int calibrationType = 0;
                    if (beforeCalibrationStockNumber
                            - afterCalibrationStockNumber > 0) {
                        calibrationType = 1;
                    } else if (beforeCalibrationStockNumber
                            - afterCalibrationStockNumber < 0) {
                        calibrationType = 2;
                    } else {
                        calibrationType = 0;
                    }
                    // 进行货道商品校准的理货员ID
                    int boxDeliveryId = currentUser.getCustomerWorkerId();
                    // 货道商品校准变更数量
                    int updateGoodsNumber = Math
                            .abs(beforeCalibrationStockNumber
                                    - afterCalibrationStockNumber);

                    CardgoroadGoodsCalibrationService cardgoroadGoodsCalibrationService = new CardgoroadGoodsCalibrationService();
                    Date calibrationDate = new Date();
                    String calibrationRecordId = cardgoroadGoodsCalibrationService
                            .makeCalibrationRecordId(calibrationDate);
                    CardgoroadGoodsCalibration cardgoroadGoodsCalibration = new CardgoroadGoodsCalibration();
                    cardgoroadGoodsCalibration
                            .setCalibrationRecordId(calibrationRecordId);
                    if (replenishmentId != null) {
                        cardgoroadGoodsCalibration
                                .setReplenishmentId(replenishmentId);
                    } else {
                        cardgoroadGoodsCalibration.setReplenishmentId("0");
                    }
                    cardgoroadGoodsCalibration.setBoxId(boxId);
                    cardgoroadGoodsCalibration.setCardgoRoadNumber(Integer
                            .valueOf(cardgoRoadId));
                    cardgoroadGoodsCalibration.setBarCodeId(barCodeId);
                    cardgoroadGoodsCalibration.setGoodsName(goodsName);
                    cardgoroadGoodsCalibration
                            .setBeforeCalibrationStockNumber(beforeCalibrationStockNumber);
                    cardgoroadGoodsCalibration
                            .setCalibrationType(calibrationType);
                    cardgoroadGoodsCalibration
                            .setUpdateGoodsNumber(updateGoodsNumber);
                    cardgoroadGoodsCalibration
                            .setAfterCalibrationStockNumber(afterCalibrationStockNumber);
                    cardgoroadGoodsCalibration.setBoxDeliveryId(boxDeliveryId);
                    cardgoroadGoodsCalibration
                            .setCalibrationTime(calibrationDate);
                    cardgoroadGoodsCalibrationService
                            .addCardgoroadGoodsCalibration(cardgoroadGoodsCalibration);
                }

                String updateResult = boxGoodsXXXService
                        .doUpdateCardgoRoadGoodsStockNumber(boxId,
                                cardgoRoadId, barCodeId, updateStockNumber);
                jsonObject = JSONObject.fromObject(updateResult);
            }
            // 获取最新的售货机商品信息
            JSONObject allBoxGoodsJsonObject = JSONObject
                    .fromObject(boxGoodsXXXService
                            .doGetNewCurrentBoxAllGoods(boxId));
            jsonObject.put("AllBoxGoods", allBoxGoodsJsonObject);
            Session currentSession = SessionMapFactory.getInstance()
                    .getCurrentSession(customCategory, boxId);
            SessionMapFactory.getInstance().sendMessage(currentSession,
                    jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    // 工控机电池未充电（断电报警）
    private void notifyManagerDeviceNotCharging(String customCategory,
            String boxId) {
        DeviceMonitorService deviceMonitorService = new DeviceMonitorService();
        try {
            deviceMonitorService.notifyManagerDeviceNotCharging(customCategory,
                    boxId);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    // 发短信给理货员，售货柜已发生异常，尽快理货
    private void notifyBoxManagerToManageExceptionShoppingBox(
            String customCategory, String boxId) {
        DeviceMonitorService deviceMonitorService = new DeviceMonitorService();
        try {
            deviceMonitorService.notifyBoxManagerToManageExceptionShoppingBox(
                    customCategory, boxId);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    /**
     * 更新box表中售货机经纬度信息
     */
    private void updateLocationLatitudeAndLongitude(String boxId,
            String latitude, String longitude) {
        BoxesService boxesService = new BoxesService();
        try {
            boxesService.updateLocationLatitudeAndLongitude(boxId, latitude,
                    longitude);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    /**
     * Android主控对上下架商品不做处理，由服务器通过allGoodsJsonObject和数据库库存商品做对比，提取出商品上下架信息
     */
    private JSONArray getAddedAndRemovedGoodsJsonObject(String boxId,
            JSONObject allGoodsJsonObject) {
        JSONArray addedAndRemoveGoodsJsonArray = new JSONArray();
        try {
            BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
            addedAndRemoveGoodsJsonArray = boxGoodsXXXService
                    .getAddedAndRemovedGoodsJsonObject(boxId,
                            allGoodsJsonObject);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        return addedAndRemoveGoodsJsonArray;
    }

    /**
     * 工控机处理完异常购物行为以后返回所处理异常购物视频的状态，同步更新数据库为该异常视频已处理状态
     * 
     * @param exceptionId
     */
    private void updateExceptionShoppingState(String exceptionId, int state) {
        try {
            ExceptionShoppingService exceptionShoppingService = new ExceptionShoppingService();
            ExceptionShopping exceptionShopping = new ExceptionShopping();
            exceptionShopping.setExceptionId(exceptionId);
            exceptionShopping.setState(state);
            exceptionShopping.setHandleTime(new Date());
            exceptionShoppingService
                    .updateExceptionShoppingState(exceptionShopping);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    /**
     * 接收到公共机已接收到智购云关机指令，并开始执行关机动作反馈，把设备状态设置为维护中,并通知微信小程序已执行关机指令
     */
    private void notifyWXDeviceShutdownSuccess(String customCategory,
            String boxId) {
        try {
            BoxStatusService boxStatusService = new BoxStatusService();
            BoxStatus boxStatus = new BoxStatus();
            boxStatus.setBoxId(boxId);
            boxStatus.setState(ResponseStatus.BOX_STATUS_BROKEN_DOWN);
            boxStatus.setReportTime(new Date());

            BoxStatus boxStatusQuery = boxStatusService
                    .findBoxStatusByBoxId(boxId);
            if (boxStatusQuery != null) {
                boxStatusService.updateBoxStatus(boxStatus);
            } else {
                boxStatusService.addBoxStatus(boxStatus);
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("response", ResponseStatus.SUCCESS);
            Session currentSession = SessionMapFactory.getInstance()
                    .getCurrentSession(customCategory, boxId);
            SessionMapFactory.getInstance().sendMessage(currentSession,
                    jsonObject.toString());
        } catch (IOException e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void sendPongHeartToAndroid(String boxId, long timestamp) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("response", "heart");
            jsonObject.put("timeStamp", timestamp);
            // 售货机当前状态
            BoxStatusService boxStatusService = new BoxStatusService();
            BoxStatus boxStatus = boxStatusService.findBoxStatusByBoxId(boxId);
            int boxState = ResponseStatus.BOX_STATUS_NORMAL;
            if (boxStatus != null) {
                boxState = boxStatus.getState();
            }
            jsonObject.put("boxState", boxState);
            CurrentUserService currentUserService = new CurrentUserService();
            CurrentUser currentUser = currentUserService
                    .findCurrentUserByBoxId(boxId);
            // 向工控机报当前售货机是一般用户还是管理员
            String userType = ResponseStatus.PERMISSION_NORMAL_USER;
            if (currentUser != null) {
                userType = currentUser.getUserType();
            }
            jsonObject.put("userType", userType);
            Session currentSession = SessionMapFactory.getInstance()
                    .getCurrentSession(ResponseStatus.CUSTOM_CATEGORY_ANDROID,
                            boxId);
            SessionMapFactory.getInstance().sendMessage(currentSession,
                    jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    /**
     * 保存工控机上报层架是否断开和恢复状态
     * 
     * @param boxId
     * @param state
     */
    private void saveWeightSensorStatus(String boxId, int state) {
        try {
            WeightSensorStatusService weightSensorStatusService = new WeightSensorStatusService();
            weightSensorStatusService.saveWeightSensorStatus(boxId, state);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    /**
     * 收到Android更新货道商品价格结果以后，开始更新本地数据库
     * 
     * @param boxId
     * @param androidUpdateResult
     */
    private void doUpdateCurrentBoxGoodsPrice(String boxId,
            String androidUpdateResult) {
        if (androidUpdateResult.equals(ResponseStatus.FAIL)) {
            log.info("doUpdateCurrentBoxGoodsPrice failed error");
        } else {
            BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
            boxGoodsXXXService.doUpdateCurrentBoxGoodsPrice(boxId);
        }
    }

}