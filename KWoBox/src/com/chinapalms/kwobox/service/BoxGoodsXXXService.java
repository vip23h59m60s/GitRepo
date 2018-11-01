package com.chinapalms.kwobox.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.websocket.Session;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.impl.BoxGoodsXXXDAOImpl;
import com.chinapalms.kwobox.javabean.BoxGoodsXXX;
import com.chinapalms.kwobox.javabean.BoxGoodsXXXNew;
import com.chinapalms.kwobox.javabean.BoxStatus;
import com.chinapalms.kwobox.javabean.BoxStockSyncStatus;
import com.chinapalms.kwobox.javabean.BoxStructure;
import com.chinapalms.kwobox.javabean.Boxes;
import com.chinapalms.kwobox.javabean.CurrentUser;
import com.chinapalms.kwobox.javabean.GoodsActualWeight;
import com.chinapalms.kwobox.javabean.GoodsAverageWeight;
import com.chinapalms.kwobox.javabean.InOutGoods;
import com.chinapalms.kwobox.javabean.Order;
import com.chinapalms.kwobox.javabean.OrderThirdPart;
import com.chinapalms.kwobox.javabean.SerializeBoxBody;
import com.chinapalms.kwobox.javabean.ShoppingInfo;
import com.chinapalms.kwobox.javabean.User;
import com.chinapalms.kwobox.javabean.WeightBoxGoodsUpdate;
import com.chinapalms.kwobox.pay.wxpay.papay.WxPapayService;
import com.chinapalms.kwobox.servelet.SessionMapFactory;
import com.chinapalms.kwobox.utils.JsonParseUtil;
import com.chinapalms.kwobox.utils.ResponseStatus;
import com.custom.kwobox.service.CustomWebSocketService;

public class BoxGoodsXXXService extends BoxGoodsXXXDAOImpl {

    Log log = LogFactory.getLog(BoxGoodsXXXService.class);

    @Override
    public boolean addBoxGoodsXXX(String boxId, int boxType,
            BoxGoodsXXX boxGoodsXXX) {
        return super.addBoxGoodsXXX(boxId, boxType, boxGoodsXXX);
    }

    @Override
    public BoxGoodsXXX findBoxGoodsXXXByGoodsDistinguishId(String boxId,
            String goodsDistinguishId) {
        return super.findBoxGoodsXXXByGoodsDistinguishId(boxId,
                goodsDistinguishId);
    }

    @Override
    public List<BoxGoodsXXX> findAllBoxGoodsXXX(String boxId) {
        return super.findAllBoxGoodsXXX(boxId);
    }

    @Override
    public boolean updateBoxGoodsByBarCodeIdAndCardgoRoadIdAndStockNumber(
            String boxId, String barCodeId, int cardgoRoadId, int stockNumber) {
        return super.updateBoxGoodsByBarCodeIdAndCardgoRoadIdAndStockNumber(
                boxId, barCodeId, cardgoRoadId, stockNumber);
    }

    @Override
    public boolean updateBoxGoodsByBarCodeIdAndCardgoRoadIdAndNumberOfChange(
            String boxId, String barCodeId, int cardgoRoadId, int changeNumber) {
        return super.updateBoxGoodsByBarCodeIdAndCardgoRoadIdAndNumberOfChange(
                boxId, barCodeId, cardgoRoadId, changeNumber);
    }

    @Override
    public boolean emptyBoxGoodsByBoxId(String boxId, int boxType) {
        return super.emptyBoxGoodsByBoxId(boxId, boxType);
    }

    @Override
    public boolean deleteBoxGoodsXXXByGoodsDistinguishId(String boxId,
            String goodsDistinguishId) {
        return super.deleteBoxGoodsXXXByGoodsDistinguishId(boxId,
                goodsDistinguishId);
    }

    @Override
    public BoxGoodsXXX findBoxGoodsXXXByBarCodeIdAndCardgoRoadId(String boxId,
            String barCodeId, int CardgoRoadId) {
        return super.findBoxGoodsXXXByBarCodeIdAndCardgoRoadId(boxId,
                barCodeId, CardgoRoadId);
    }

    @Override
    public boolean updateBoxGoods(String boxId, BoxGoodsXXX boxGoodsXXX) {
        return super.updateBoxGoods(boxId, boxGoodsXXX);
    }

    @Override
    public List<BoxGoodsXXX> findBoxGoodsByBarCodeId(String boxId,
            String barCodeId) {
        return super.findBoxGoodsByBarCodeId(boxId, barCodeId);
    }

    @Override
    public boolean deleteAllBoxGoodsXXX(String boxId) {
        return super.deleteAllBoxGoodsXXX(boxId);
    }

    @Override
    public boolean updateBoxGoodsXXXByCardgoRoadId(String boxId,
            BoxGoodsXXX boxGoodsXXX) {
        return super.updateBoxGoodsXXXByCardgoRoadId(boxId, boxGoodsXXX);
    }

    @Override
    public boolean updateBoxGoodsXXXPriceByCardgoRoadId(String boxId,
            BoxGoodsXXX boxGoodsXXX) {
        return super.updateBoxGoodsXXXPriceByCardgoRoadId(boxId, boxGoodsXXX);
    }

    /**
     * 通知微信端当前交易已出售商品
     * 
     * @param customCategory
     * @param boxId
     * @param goodsIdsJsonList
     * @throws Exception
     */
    public String notifyWXWhatGoodsSaled(String orderType,
            String customCategory, String boxId, String phoneNumber,
            String openDoorRequestSerialNumber, int boxType,
            List<JSONObject> goodsIdsJsonList,
            boolean addPersonCreditOrPointsRecord) throws Exception {
        CurrentUserService currentUserService = new CurrentUserService();
        CurrentUser currentUser = null;
        // 目前线上版本不传递phoneNumber和openDoorRequestSerialNumber,所以需要做一下判断以兼容线上版本
        if (phoneNumber == null) {
            currentUser = currentUserService.findCurrentUserByBoxId(boxId);
        } else {
            // ===========对接商户服务器逻辑===========================================================================
            // 通过boxId查找商户ID，再根据商户ID查找回调Url，返回商户服务器，与商户服务器对接,如果无回调Url，则默认走智购猫平台逻辑
            CustomWebSocketService customWebSocketService = new CustomWebSocketService();
            String orderCallbackUrlObject = customWebSocketService
                    .getCustomerOrderCallbackUrl(boxId);
            // 如果没有拿到对应商户的回调Url，则默认走智狗猫流程，否则回调商户服务器
            if (orderCallbackUrlObject.equals(ResponseStatus.FAIL)) {
                UserService userService = new UserService();
                User user = userService.queryUserByPhoneNumber(phoneNumber);
                currentUser = new CurrentUser();
                currentUser.setPhoneNumber(phoneNumber);
                currentUser.setCustomerWorkerId(0);
                currentUser.setBoxId(boxId);
                currentUser.setCustomType(ResponseStatus.CUSTOM_CATEGORY_WX);
                currentUser.setUserType(ResponseStatus.PERMISSION_NORMAL_USER);
                currentUser.setVipLevel(user.getVipLevel());
                currentUser.setAgreementNO(user.getWxId());
            } else {
                int urlType = JSONObject.fromObject(orderCallbackUrlObject)
                        .getInt("urlType");
                // 如果是服务器接口对接模式(例如小e),则把订单回调给商户服务器
                if (urlType == ResponseStatus.COOPERATIONMODE_THIRD_INTERFACE) {
                    currentUser = new CurrentUser();
                    currentUser.setPhoneNumber(phoneNumber);
                    currentUser.setCustomerWorkerId(0);
                    currentUser.setBoxId(boxId);
                    currentUser
                            .setCustomType(ResponseStatus.CUSTOM_CATEGORY_WX);
                    currentUser
                            .setUserType(ResponseStatus.PERMISSION_NORMAL_USER);
                    currentUser.setVipLevel(1);
                    currentUser.setAgreementNO("0");
                    // 如果是Yoho代理模式,则把订单信息回调给代理开发服务器
                } else if (urlType == ResponseStatus.COOPERATIONMODE_AGENT) {
                    currentUser = new CurrentUser();
                    currentUser.setPhoneNumber(phoneNumber);
                    currentUser.setCustomerWorkerId(0);
                    currentUser.setBoxId(boxId);
                    currentUser
                            .setCustomType(ResponseStatus.CUSTOM_CATEGORY_WX);
                    currentUser
                            .setUserType(ResponseStatus.PERMISSION_NORMAL_USER);
                    currentUser.setVipLevel(1);
                    currentUser.setAgreementNO("0");
                }
            }
        }
        // ===========对接商户服务器逻辑===========================================================================
        // 称重方案卖出商品逻辑
        if (boxType == ResponseStatus.BOX_TYPE_WEIGHT) {
            Session currentSession = SessionMapFactory.getInstance()
                    .getCurrentSession(customCategory, boxId);

            OrderService orderService = new OrderService();

            // 把Android端传递过来的商品变更信息（条形码ID,货到号,每个货道对应的更新商品的数量）从Json对象读取出来并保存至List中
            List<WeightBoxGoodsUpdate> weightBoxGoodsUpdateFromJsonList = new ArrayList<WeightBoxGoodsUpdate>();
            for (int i = 0; i < goodsIdsJsonList.size(); i++) {
                JSONObject weightBoxGoodsUpdateJsonObject = goodsIdsJsonList
                        .get(i);
                WeightBoxGoodsUpdate weightBoxGoodsUpdate = new WeightBoxGoodsUpdate();
                String barCodeId = weightBoxGoodsUpdateJsonObject
                        .getString("barCodeId");
                // 货道商品总重量(当为上货时，表示当前货道商品的总重量，当为正常售卖时为当前货道卖出商品总重量（目前只关注每个货道只售出单个商品的情况录入统计数据库）)
                BigDecimal saledGoodsActualWeight = BigDecimal.valueOf(Double
                        .valueOf(weightBoxGoodsUpdateJsonObject
                                .getString("cardgoRoadGoodsTotalWeight")));
                int cardgoRoadId = Integer
                        .valueOf(weightBoxGoodsUpdateJsonObject
                                .getString("cardgoRoadId"));
                int goodsNumber = Integer
                        .valueOf(weightBoxGoodsUpdateJsonObject
                                .getString("goodsNumber"));
                weightBoxGoodsUpdate.setBarCodeId(barCodeId);
                weightBoxGoodsUpdate
                        .setCardgoRoadGoodsTotalWeight(saledGoodsActualWeight);
                weightBoxGoodsUpdate.setCardgoRoadId(cardgoRoadId);
                weightBoxGoodsUpdate.setGoodsNumber(goodsNumber);
                weightBoxGoodsUpdateFromJsonList.add(weightBoxGoodsUpdate);
            }

            JSONArray goodsJsonArray = new JSONArray();

            List<BoxGoodsXXX> boxGoodsXXXList = new ArrayList<BoxGoodsXXX>();
            List<WeightBoxGoodsUpdate> filterWeightBoxGoodsUpdateFromJsonList = new ArrayList<WeightBoxGoodsUpdate>();

            // BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
            GoodsActualWeightService goodsActualWeightService = new GoodsActualWeightService();
            for (int i = 0; i < weightBoxGoodsUpdateFromJsonList.size(); i++) {
                WeightBoxGoodsUpdate weightBoxGoodsUpdate = weightBoxGoodsUpdateFromJsonList
                        .get(i);
                BoxGoodsXXX boxGoodsXXX = findBoxGoodsXXXByBarCodeIdAndCardgoRoadId(
                        boxId, weightBoxGoodsUpdate.getBarCodeId(),
                        weightBoxGoodsUpdate.getCardgoRoadId());
                // 如果盘点过来的数据未在数据库中，忽略不统计到账单中
                if (boxGoodsXXX != null) {
                    boxGoodsXXXList.add(boxGoodsXXX);
                    // 对应的将主控传递过来的有效的商品更新信息重新保存
                    filterWeightBoxGoodsUpdateFromJsonList
                            .add(weightBoxGoodsUpdateFromJsonList.get(i));

                    // 当主控传过来的商品对应货道只有一件商品的时候计入商品重量统计（统计商品实际重量，为后续积累商品重量误差），多于一件则忽略不计
                    if (weightBoxGoodsUpdate.getGoodsNumber() == 1) {
                        GoodsActualWeight goodsActualWeight = new GoodsActualWeight();
                        goodsActualWeight.setBoxId(boxId);
                        goodsActualWeight.setBarCodeId(weightBoxGoodsUpdate
                                .getBarCodeId());
                        goodsActualWeight.setGoodsWeight(weightBoxGoodsUpdate
                                .getCardgoRoadGoodsTotalWeight());
                        goodsActualWeight.setUpdateDateTime(new Date());
                        goodsActualWeightService
                                .addGoodsActualWeight(goodsActualWeight);
                    }
                }
            }

            // 如果除去未在数据库中商品以后，商品为空，通知客户端本次无购物
            if (boxGoodsXXXList.size() == 0) {
                return notifyWXNoGoodsSaled(orderType, customCategory, boxId,
                        currentUser.getPhoneNumber(),
                        openDoorRequestSerialNumber);
            }

            List<String> categoryIdList = new ArrayList<String>();
            for (int i = 0; i < boxGoodsXXXList.size(); i++) {
                categoryIdList.add(boxGoodsXXXList.get(i).getBarCodeId());
            }

            int goodsTotalNumber = 0;
            // 去除重复类别商品，并且把重复商品做数量统计，为生成订单做准备,并把订单封装成Json格式字符串传递至客户端
            Set<String> uniqueSet = new HashSet<String>(categoryIdList);
            for (String categoryId : uniqueSet) {
                JSONObject boxGoodsJsonObject = new JSONObject();
                for (int i = 0; i < boxGoodsXXXList.size(); i++) {
                    BoxGoodsXXX boxGoodsXXX = boxGoodsXXXList.get(i);
                    if (boxGoodsXXX.getBarCodeId().equals(categoryId)) {
                        boxGoodsJsonObject.put("barCodeId",
                                boxGoodsXXX.getBarCodeId());
                        String goodsName = boxGoodsXXX.getGoodsName();
                        boxGoodsJsonObject.put("goodsCategoryName", goodsName);
                        boxGoodsJsonObject.put("salesMode",
                                boxGoodsXXX.getSalesMode());
                        boxGoodsJsonObject.put("goodsPrice", boxGoodsXXX
                                .getGoodsPrice().setScale(2).toString());
                        BigDecimal goodsActualPrice = (boxGoodsXXX
                                .getGoodsPrice())
                                .multiply(boxGoodsXXX.getGoodsDiscount())
                                .subtract(boxGoodsXXX.getFavourable())
                                .setScale(2, BigDecimal.ROUND_UP);
                        boxGoodsJsonObject.put("goodsActualPrice",
                                goodsActualPrice.toString());
                        int categoryGoodsNumber = 0;
                        // 记录类别商品重量，以计算非标商品（按重量计价）类别商品价格
                        int categoryGoodsWeight = 0;
                        for (int j = 0; j < filterWeightBoxGoodsUpdateFromJsonList
                                .size(); j++) {
                            WeightBoxGoodsUpdate weightBoxGoodsUpdate = filterWeightBoxGoodsUpdateFromJsonList
                                    .get(j);
                            if (categoryId.equals(weightBoxGoodsUpdate
                                    .getBarCodeId())) {
                                categoryGoodsNumber = categoryGoodsNumber
                                        + weightBoxGoodsUpdate.getGoodsNumber();
                                categoryGoodsWeight = categoryGoodsWeight
                                        + (int) (Double
                                                .valueOf(weightBoxGoodsUpdate
                                                        .getCardgoRoadGoodsTotalWeight()
                                                        .toString())
                                                .doubleValue());
                                continue;
                            }
                        }
                        goodsTotalNumber = goodsTotalNumber
                                + categoryGoodsNumber;
                        boxGoodsJsonObject.put("categoryGoodsNumber",
                                categoryGoodsNumber);
                        boxGoodsJsonObject.put("categoryGoodsWeight",
                                categoryGoodsWeight);
                        BigDecimal categoryGoodsTotalPrice = new BigDecimal(0);
                        BigDecimal categoryGoodsTotalDiscount = new BigDecimal(
                                0);
                        BigDecimal actualCategoryPrice = new BigDecimal(0);
                        // 判断商品是否非标（售卖方式:按件数售卖还是按重量售卖）
                        if (boxGoodsXXX.getSalesMode() == ResponseStatus.SALES_MODE_NUMBER) {
                            // 标准商品，按件数计算价格
                            categoryGoodsTotalPrice = (boxGoodsXXX
                                    .getGoodsPrice()).multiply(
                                    BigDecimal.valueOf(Double
                                            .valueOf(categoryGoodsNumber)))
                                    .setScale(2, BigDecimal.ROUND_UP);
                            categoryGoodsTotalDiscount = ((BigDecimal
                                    .valueOf(1.0d).subtract(boxGoodsXXX
                                    .getGoodsDiscount())).multiply(boxGoodsXXX
                                    .getGoodsPrice()).add(boxGoodsXXX
                                    .getFavourable())).multiply(
                                    BigDecimal.valueOf(Double
                                            .valueOf(categoryGoodsNumber)))
                                    .setScale(2, BigDecimal.ROUND_DOWN);
                            actualCategoryPrice = goodsActualPrice.multiply(
                                    BigDecimal.valueOf(Double
                                            .valueOf(categoryGoodsNumber)))
                                    .setScale(2, BigDecimal.ROUND_UP);
                        } else if (boxGoodsXXX.getSalesMode() == ResponseStatus.SALES_MODE_WEIGHT) {
                            // 非标产品价格计算,单价标准为类似1.2元/kg 类别商品价格=商品单价x商品重量
                            categoryGoodsTotalPrice = (boxGoodsXXX
                                    .getGoodsPrice())
                                    .multiply(
                                            BigDecimal.valueOf(Double.valueOf(Double
                                                    .valueOf(categoryGoodsWeight) / 1000)))
                                    .setScale(2, BigDecimal.ROUND_UP);
                            categoryGoodsTotalDiscount = ((BigDecimal
                                    .valueOf(1.0d).subtract(boxGoodsXXX
                                    .getGoodsDiscount())).multiply(boxGoodsXXX
                                    .getGoodsPrice()).add(boxGoodsXXX
                                    .getFavourable()))
                                    .multiply(
                                            BigDecimal.valueOf(Double.valueOf(Double.valueOf(Double
                                                    .valueOf(categoryGoodsWeight) / 1000))))
                                    .setScale(2, BigDecimal.ROUND_DOWN);
                            actualCategoryPrice = goodsActualPrice
                                    .multiply(
                                            BigDecimal.valueOf(Double.valueOf(Double.valueOf(Double
                                                    .valueOf(categoryGoodsWeight) / 1000))))
                                    .setScale(2, BigDecimal.ROUND_UP);
                        }
                        // 四舍五入保存两位数据
                        boxGoodsJsonObject.put("categoryGoodsTotalPrice",
                                categoryGoodsTotalPrice.toString());
                        // 四舍五入保存两位数据
                        boxGoodsJsonObject.put("categoryGoodsTotalDiscount",
                                categoryGoodsTotalDiscount.toString());
                        boxGoodsJsonObject.put("actualCategoryPrice",
                                actualCategoryPrice.toString());
                        break;
                    }
                }
                goodsJsonArray.add(boxGoodsJsonObject);
            }

            JSONObject orderPropertiesJsonObject = new JSONObject();

            Date orderDate = new Date();
            String orderId = orderService.makeOrderId(orderDate);
            orderPropertiesJsonObject.put("orderId", orderId);
            orderPropertiesJsonObject
                    .put("vipLevel", currentUser.getVipLevel());
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            orderPropertiesJsonObject.put("orderDate", df.format(orderDate));

            orderPropertiesJsonObject.put("goodsTotalNumber", goodsTotalNumber);

            BigDecimal payTotal = new BigDecimal(0);
            BigDecimal totalDiscount = new BigDecimal(0);
            BigDecimal actualPayTotal = new BigDecimal(0);
            for (int i = 0; i < goodsJsonArray.size(); i++) {
                JSONObject boxGoodsJsonObject = goodsJsonArray.getJSONObject(i);
                int goodsNumber = Integer.parseInt(boxGoodsJsonObject.get(
                        "categoryGoodsNumber").toString());
                int categoryGoodsWeight = Integer.parseInt(boxGoodsJsonObject
                        .get("categoryGoodsWeight").toString());
                BigDecimal goodsPrice = BigDecimal.valueOf(Double
                        .valueOf(boxGoodsJsonObject.getString(("goodsPrice"))));

                // 标准商品按件数计算总价，非标商品按重量计算价格
                int salesMode = Integer.valueOf(boxGoodsJsonObject
                        .getString("salesMode"));
                if (salesMode == ResponseStatus.SALES_MODE_NUMBER) {
                    payTotal = payTotal.add((goodsPrice.multiply(BigDecimal
                            .valueOf(Double.valueOf(goodsNumber)))));
                } else if (salesMode == ResponseStatus.SALES_MODE_WEIGHT) {
                    payTotal = payTotal.add((goodsPrice.multiply(BigDecimal
                            .valueOf(Double.valueOf(Double
                                    .valueOf(categoryGoodsWeight) / 1000)))));
                }

                totalDiscount = totalDiscount.add(BigDecimal.valueOf(Double
                        .valueOf(boxGoodsJsonObject
                                .getString("categoryGoodsTotalDiscount"))));
            }
            // 四舍五入保存两位数据
            orderPropertiesJsonObject.put("payTotal",
                    payTotal.setScale(2, BigDecimal.ROUND_DOWN).toString());

            orderPropertiesJsonObject.put("totalFavourable", totalDiscount
                    .setScale(2, BigDecimal.ROUND_DOWN).toString());

            actualPayTotal = payTotal.subtract(totalDiscount);

            orderPropertiesJsonObject.put("actualPayTotal", actualPayTotal
                    .setScale(2, BigDecimal.ROUND_DOWN).toString());

            // 本次购物获取的积分
            int pointsChangeValue = (int) Double.valueOf(
                    actualPayTotal.setScale(2, BigDecimal.ROUND_DOWN)
                            .toString()).doubleValue();
            // 如果有小数，积分向前进一位
            if (pointsChangeValue < Double.valueOf(
                    actualPayTotal.setScale(2, BigDecimal.ROUND_DOWN)
                            .toString()).doubleValue()) {
                pointsChangeValue = pointsChangeValue + 1;
            }
            // 判读是否是异常购物行为，决定是否增加信用分和积分
            if (addPersonCreditOrPointsRecord) {
                orderPropertiesJsonObject.put("addedUserPoints",
                        pointsChangeValue);
                orderPropertiesJsonObject.put("addedPersonCredit",
                        ResponseStatus.POINTS_ADD_REASON_NORMAL_SHOPPING);
            } else {
                orderPropertiesJsonObject.put("addedUserPoints", 0);
                orderPropertiesJsonObject.put("addedPersonCredit", 0);
            }

            orderPropertiesJsonObject.put("Goods", goodsJsonArray);

            JSONObject orderJsonObject = new JSONObject();
            orderJsonObject.put("Order", orderPropertiesJsonObject);

            for (int i = 0; i < boxGoodsXXXList.size(); i++) {
                // 卖出商品以后将BoxXXX中对应商品移除
                WeightBoxGoodsUpdate weightBoxGoodsUpdate = filterWeightBoxGoodsUpdateFromJsonList
                        .get(i);
                updateBoxGoodsByBarCodeIdAndCardgoRoadIdAndNumberOfChange(
                        boxId, weightBoxGoodsUpdate.getBarCodeId(),
                        weightBoxGoodsUpdate.getCardgoRoadId(),
                        weightBoxGoodsUpdate.getGoodsNumber());
            }

            BoxStatusService boxStatusService = new BoxStatusService();

            // 把当前订单出入订单表中和订单明细表中,然后把订单信息返回客户端,并设置柜门状态
            // 订单推送完成以后删除当前货柜注册用户信息,但为了防止用户非法瞎搞货柜，会把瞎搞产品订单强制记录在上次购物用户头上，所以不对当前货柜用户做删除处理
            // CurrentUserService currentUserService = new CurrentUserService();
            if (orderService.makeOrder(orderJsonObject, orderDate, boxId,
                    currentUser, addPersonCreditOrPointsRecord)
            /* && currentUserService.deleteCurrentUserByBoxId(boxId) */) {
                // 如果当前order的orderType==null（目前线上软件无orderType参数，为了兼容线上产品做orderType==null判断）为目前线上版本（无不锁门逻辑）人工处理以后发出的order:orderType=delayOrder时不做恢复柜子状态处理
                // 仅当orderType==null(线上产品)或为正常orderType=normalOrder时做门状态恢复处理(异常状态已经在Servlet中恢复)
                if (orderType == null
                        || (orderType != null && orderType
                                .equals(ResponseStatus.ORDER_TYPE_NORMAL))) {
                    boxStatusService.setOpenDoorState(boxId, false);
                }

                orderPropertiesJsonObject.put("payState", 1);
                orderJsonObject.put("Order", orderPropertiesJsonObject);

                // ===========对接商户服务器逻辑===========================================================================
                // 通过boxId查找商户ID，再根据商户ID查找回调Url，返回商户服务器，与商户服务器对接,如果无回调Url，则默认走智购猫平台逻辑
                CustomWebSocketService customWebSocketService = new CustomWebSocketService();
                String orderCallbackUrlObject = customWebSocketService
                        .getCustomerOrderCallbackUrl(boxId);
                // 如果没有拿到对应商户的回调Url，则默认走智狗猫流程，否则回调商户服务器
                if (orderCallbackUrlObject.equals(ResponseStatus.FAIL)) {
                    // 不锁门方案，异常视频处理以后不再发送给微信小程序客户端
                    // 当前线上软件不穿点orderType参数，为了兼容线上产品在这里设置做orderType判断
                    if (orderType == null) {
                        SessionMapFactory.getInstance().sendMessage(
                                currentSession, orderJsonObject.toString());
                    } else {
                        if (orderType.equals(ResponseStatus.ORDER_TYPE_NORMAL)) {
                            SessionMapFactory.getInstance().sendMessage(
                                    currentSession, orderJsonObject.toString());
                        }
                    }
                    // 正常购物完成增加个人征信值
                    if (addPersonCreditOrPointsRecord) {
                        updatePersonalCredit(currentUser.getPhoneNumber(),
                                boxId, ResponseStatus.PERSONAL_CREDIT_ADD,
                                ResponseStatus.PERSONAL_CREDIT_ADD_VALUE);
                    }
                } else {
                    int urlType = JSONObject.fromObject(orderCallbackUrlObject)
                            .getInt("urlType");
                    // 如果是服务器接口对接模式(例如小e),则把订单回调给商户服务器
                    if (urlType == ResponseStatus.COOPERATIONMODE_THIRD_INTERFACE) {
                        customWebSocketService
                                .notifyCustomServerWhatGoodsSaled(
                                        orderCallbackUrlObject, boxId,
                                        currentUser.getPhoneNumber(),
                                        openDoorRequestSerialNumber,
                                        orderJsonObject);
                        // 如果是Yoho代理模式,则把订单信息回调给代理开发服务器
                    } else if (urlType == ResponseStatus.COOPERATIONMODE_AGENT) {
                        customWebSocketService
                                .notifyCustomerServerWhatGoodsSaled(orderType,
                                        orderCallbackUrlObject,
                                        orderJsonObject, orderDate, boxId,
                                        currentUser.getPhoneNumber(),
                                        addPersonCreditOrPointsRecord);
                    }
                }
                // ===========对接商户服务器逻辑===========================================================================

                log.info("BoxGoodsXXXService: notifyWXWhatGoodsSaled:payState=1 WEIGHT: orderJsonObject="
                        + orderJsonObject);
            } else {
                // 如果当前order的orderType==null（目前线上软件无orderType参数，为了兼容线上产品做orderType==null判断）为目前线上版本（无不锁门逻辑）人工处理以后发出的order:orderType=delayOrder时不做恢复柜子状态处理
                // 仅当orderType==null(线上产品)或为正常orderType=normalOrder时做门状态恢复处理(异常状态已经在Servlet中恢复)
                if (orderType == null
                        || (orderType != null && orderType
                                .equals(ResponseStatus.ORDER_TYPE_NORMAL))) {
                    boxStatusService.setOpenDoorState(boxId, false);
                }
                orderPropertiesJsonObject.put("payState", 0);
                orderJsonObject.put("Order", orderPropertiesJsonObject);

                // ===========对接商户服务器逻辑===========================================================================
                // 通过boxId查找商户ID，再根据商户ID查找回调Url，返回商户服务器，与商户服务器对接,如果无回调Url，则默认走智购猫平台逻辑
                CustomWebSocketService customWebSocketService = new CustomWebSocketService();
                String orderCallbackUrlObject = customWebSocketService
                        .getCustomerOrderCallbackUrl(boxId);
                // 如果没有拿到对应商户的回调Url，则默认走智狗猫流程，否则回调商户服务器
                if (orderCallbackUrlObject.equals(ResponseStatus.FAIL)) {
                    // 不锁门方案，异常视频处理以后不再发送给微信小程序客户端
                    // 当前线上软件不穿点orderType参数，为了兼容线上产品在这里设置做orderType判断
                    if (orderType == null) {
                        SessionMapFactory.getInstance().sendMessage(
                                currentSession, orderJsonObject.toString());
                    } else {
                        if (orderType.equals(ResponseStatus.ORDER_TYPE_NORMAL)) {
                            SessionMapFactory.getInstance().sendMessage(
                                    currentSession, orderJsonObject.toString());
                        }
                    }
                    // 正常购物完成增加个人征信值
                    if (addPersonCreditOrPointsRecord) {
                        updatePersonalCredit(currentUser.getPhoneNumber(),
                                boxId, ResponseStatus.PERSONAL_CREDIT_ADD,
                                ResponseStatus.PERSONAL_CREDIT_ADD_VALUE);
                    }
                } else {
                    int urlType = JSONObject.fromObject(orderCallbackUrlObject)
                            .getInt("urlType");
                    // 如果是服务器接口对接模式(例如小e),则把订单回调给商户服务器
                    if (urlType == ResponseStatus.COOPERATIONMODE_THIRD_INTERFACE) {
                        customWebSocketService
                                .notifyCustomServerWhatGoodsSaled(
                                        orderCallbackUrlObject, boxId,
                                        currentUser.getPhoneNumber(),
                                        openDoorRequestSerialNumber,
                                        orderJsonObject);
                        // 如果是Yoho代理模式,则把订单信息回调给代理开发服务器
                    } else if (urlType == ResponseStatus.COOPERATIONMODE_AGENT) {
                        customWebSocketService
                                .notifyCustomerServerWhatGoodsSaled(orderType,
                                        orderCallbackUrlObject,
                                        orderJsonObject, orderDate, boxId,
                                        currentUser.getPhoneNumber(),
                                        addPersonCreditOrPointsRecord);
                    }
                }
                // ===========对接商户服务器逻辑===========================================================================

                log.info("BoxGoodsXXXService: notifyWXWhatGoodsSaled:payState=0 WEIGHT: orderJsonObject="
                        + orderJsonObject);
            }
            // 返回orderId，以便返回工控机进行处理
            return orderId;
            // RFID方案卖出商品逻辑
        } else {
            Session currentSession = SessionMapFactory.getInstance()
                    .getCurrentSession(customCategory, boxId);

            OrderService orderService = new OrderService();

            // 把Android端传递过来的商品Id从Json对象读取出来并保存至List中
            List<String> goodsDistinguishIdFromJsonList = new ArrayList<String>();
            for (int i = 0; i < goodsIdsJsonList.size(); i++) {
                goodsDistinguishIdFromJsonList.add(goodsIdsJsonList.get(i)
                        .getString("goodsId"));
            }

            JSONArray goodsJsonArray = new JSONArray();

            List<BoxGoodsXXX> boxGoodsXXXList = new ArrayList<BoxGoodsXXX>();
            /*
             * for (int i = 0; i < goodsDistinguishIdList.size(); i++) {
             * boxGoodsXXXList.add(findBoxGoodsXXXByGoodsDistinguishId(boxId,
             * goodsDistinguishIdList.get(i))); }
             */
            BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
            GoodsStateRecordService goodsStateRecordService = new GoodsStateRecordService();
            for (int i = 0; i < goodsDistinguishIdFromJsonList.size(); i++) {
                BoxGoodsXXX boxGoodsXXX = findBoxGoodsXXXByGoodsDistinguishId(
                        boxId, goodsDistinguishIdFromJsonList.get(i));
                // 如果盘点过来的数据未在数据库中，忽略不统计到账单中
                if (boxGoodsXXX != null) {
                    boxGoodsXXXList.add(boxGoodsXXX);
                    // 卖出商品以后将BoxXXX中对应商品移除
                    // boxGoodsXXXService.deleteBoxGoodsXXXByGoodsDistinguishId(boxId,
                    // goodsDistinguishIdFromJsonList.get(i));
                    // 卖出商品以后将Goods表中商品状态置为已售出
                    // goodsStateRecordService.updateGoodsStateRecordByDistinguishId(
                    // goodsDistinguishIdFromJsonList.get(i), 2);
                }
            }

            // 如果除去未在数据库中商品以后，商品为空，通知客户端本次无购物
            if (boxGoodsXXXList.size() == 0) {
                return notifyWXNoGoodsSaled(orderType, customCategory, boxId,
                        currentUser.getPhoneNumber(),
                        openDoorRequestSerialNumber);
            }

            List<String> categoryIdList = new ArrayList<String>();
            for (int i = 0; i < boxGoodsXXXList.size(); i++) {
                categoryIdList.add(boxGoodsXXXList.get(i).getBarCodeId());
            }

            // 去除重复类别商品，并且把重复商品做数量统计，为生成订单做准备,并把订单封装成Json格式字符串传递至客户端
            Set<String> uniqueSet = new HashSet<String>(categoryIdList);
            for (String categoryId : uniqueSet) {
                JSONObject boxGoodsJsonObject = new JSONObject();
                for (int i = 0; i < boxGoodsXXXList.size(); i++) {
                    BoxGoodsXXX boxGoodsXXX = boxGoodsXXXList.get(i);
                    if (boxGoodsXXX.getBarCodeId().equals(categoryId)) {
                        boxGoodsJsonObject.put("barCodeId",
                                boxGoodsXXX.getBarCodeId());
                        String goodsName = boxGoodsXXX.getGoodsName();
                        boxGoodsJsonObject.put("goodsCategoryName", goodsName);
                        boxGoodsJsonObject.put("goodsPrice", boxGoodsXXX
                                .getGoodsPrice().setScale(2).toString());
                        BigDecimal goodsActualPrice = (boxGoodsXXX
                                .getGoodsPrice())
                                .multiply(boxGoodsXXX.getGoodsDiscount())
                                .subtract(boxGoodsXXX.getFavourable())
                                .setScale(2, BigDecimal.ROUND_UP);
                        boxGoodsJsonObject.put("goodsActualPrice",
                                goodsActualPrice.toString());
                        boxGoodsJsonObject.put("categoryGoodsNumber",
                                Collections.frequency(categoryIdList,
                                        categoryId));
                        BigDecimal categoryGoodsTotalPrice = (boxGoodsXXX
                                .getGoodsPrice())
                                .multiply(
                                        BigDecimal.valueOf(Double
                                                .valueOf(Collections.frequency(
                                                        categoryIdList,
                                                        categoryId))))
                                .setScale(2, BigDecimal.ROUND_UP);
                        // 四舍五入保存两位数据
                        boxGoodsJsonObject.put("categoryGoodsTotalPrice",
                                categoryGoodsTotalPrice.toString());
                        BigDecimal categoryGoodsTotalDiscount = ((BigDecimal
                                .valueOf(1.0d).subtract(boxGoodsXXX
                                .getGoodsDiscount())).multiply(boxGoodsXXX
                                .getGoodsPrice()).add(boxGoodsXXX
                                .getFavourable()))
                                .multiply(
                                        BigDecimal.valueOf(Double
                                                .valueOf(Collections.frequency(
                                                        categoryIdList,
                                                        categoryId))))
                                .setScale(2, BigDecimal.ROUND_DOWN);
                        // 四舍五入保存两位数据
                        boxGoodsJsonObject.put("categoryGoodsTotalDiscount",
                                categoryGoodsTotalDiscount.toString());

                        BigDecimal actualCategoryPrice = goodsActualPrice
                                .multiply(
                                        BigDecimal.valueOf(Double
                                                .valueOf(Collections.frequency(
                                                        categoryIdList,
                                                        categoryId))))
                                .setScale(2, BigDecimal.ROUND_UP);
                        boxGoodsJsonObject.put("actualCategoryPrice",
                                actualCategoryPrice.toString());
                        break;
                    }
                }
                goodsJsonArray.add(boxGoodsJsonObject);
            }

            JSONObject orderPropertiesJsonObject = new JSONObject();

            Date orderDate = new Date();
            String orderId = orderService.makeOrderId(orderDate);
            orderPropertiesJsonObject.put("orderId", orderId);

            orderPropertiesJsonObject
                    .put("vipLevel", currentUser.getVipLevel());
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            orderPropertiesJsonObject.put("orderDate", df.format(orderDate));

            orderPropertiesJsonObject.put("goodsTotalNumber",
                    boxGoodsXXXList.size());

            BigDecimal payTotal = new BigDecimal(0);
            BigDecimal totalDiscount = new BigDecimal(0);
            BigDecimal actualPayTotal = new BigDecimal(0);
            for (int i = 0; i < goodsJsonArray.size(); i++) {
                JSONObject boxGoodsJsonObject = goodsJsonArray.getJSONObject(i);
                int goodsNumber = Integer.parseInt(boxGoodsJsonObject.get(
                        "categoryGoodsNumber").toString());
                BigDecimal goodsPrice = BigDecimal.valueOf(Double
                        .valueOf(boxGoodsJsonObject.getString(("goodsPrice"))));

                payTotal = payTotal.add((goodsPrice.multiply(BigDecimal
                        .valueOf(Double.valueOf(goodsNumber)))));

                totalDiscount = totalDiscount.add(BigDecimal.valueOf(Double
                        .valueOf(boxGoodsJsonObject
                                .getString("categoryGoodsTotalDiscount"))));
            }
            // 四舍五入保存两位数据
            orderPropertiesJsonObject.put("payTotal",
                    payTotal.setScale(2, BigDecimal.ROUND_DOWN).toString());

            orderPropertiesJsonObject.put("totalFavourable", totalDiscount
                    .setScale(2, BigDecimal.ROUND_DOWN).toString());

            actualPayTotal = payTotal.subtract(totalDiscount);

            orderPropertiesJsonObject.put("actualPayTotal", actualPayTotal
                    .setScale(2, BigDecimal.ROUND_DOWN).toString());

            orderPropertiesJsonObject.put("Goods", goodsJsonArray);

            JSONObject orderJsonObject = new JSONObject();
            orderJsonObject.put("Order", orderPropertiesJsonObject);

            for (int i = 0; i < boxGoodsXXXList.size(); i++) {
                // 卖出商品以后将BoxXXX中对应商品移除
                boxGoodsXXXService.deleteBoxGoodsXXXByGoodsDistinguishId(boxId,
                        boxGoodsXXXList.get(i).getGoodsRFID());
                // 卖出商品以后将Goods表中商品状态置为已售出
                goodsStateRecordService.updateGoodsStateRecordByDistinguishId(
                        boxGoodsXXXList.get(i).getGoodsRFID(), 2);
            }

            BoxStatusService boxStatusService = new BoxStatusService();

            // 把当前订单出入订单表中和订单明细表中,然后把订单信息返回客户端,并设置柜门状态
            // 订单推送完成以后删除当前货柜注册用户信息,但为了防止用户非法瞎搞货柜，会把瞎搞产品订单强制记录在上次购物用户头上，所以不对当前货柜用户做删除处理
            // CurrentUserService currentUserService = new CurrentUserService();
            if (orderService.makeOrder(orderJsonObject, orderDate, boxId,
                    currentUser, addPersonCreditOrPointsRecord)
            /* && currentUserService.deleteCurrentUserByBoxId(boxId) */) {
                // 如果当前order的orderType==null（目前线上软件无orderType参数，为了兼容线上产品做orderType==null判断）为目前线上版本（无不锁门逻辑）人工处理以后发出的order:orderType=delayOrder时不做恢复柜子状态处理
                // 仅当orderType==null(线上产品)或为正常orderType=normalOrder时做门状态恢复处理(异常状态已经在Servlet中恢复)
                if (orderType == null
                        || (orderType != null && orderType
                                .equals(ResponseStatus.ORDER_TYPE_NORMAL))) {
                    boxStatusService.setOpenDoorState(boxId, false);
                }

                orderPropertiesJsonObject.put("payState", 1);
                orderJsonObject.put("Order", orderPropertiesJsonObject);

                // ===========对接商户服务器逻辑===========================================================================
                // 通过boxId查找商户ID，再根据商户ID查找回调Url，返回商户服务器，与商户服务器对接,如果无回调Url，则默认走智购猫平台逻辑
                CustomWebSocketService customWebSocketService = new CustomWebSocketService();
                String orderCallbackUrlObject = customWebSocketService
                        .getCustomerOrderCallbackUrl(boxId);
                // 如果没有拿到对应商户的回调Url，则默认走智狗猫流程，否则回调商户服务器
                if (orderCallbackUrlObject.equals(ResponseStatus.FAIL)) {
                    SessionMapFactory.getInstance().sendMessage(currentSession,
                            orderJsonObject.toString());
                    // 正常购物完成增加个人征信值
                    if (addPersonCreditOrPointsRecord) {
                        updatePersonalCredit(currentUser.getPhoneNumber(),
                                boxId, ResponseStatus.PERSONAL_CREDIT_ADD,
                                ResponseStatus.PERSONAL_CREDIT_ADD_VALUE);
                    }
                } else {
                    int urlType = JSONObject.fromObject(orderCallbackUrlObject)
                            .getInt("urlType");
                    // 如果是服务器接口对接模式(例如小e),则把订单回调给商户服务器
                    if (urlType == ResponseStatus.COOPERATIONMODE_THIRD_INTERFACE) {
                        customWebSocketService
                                .notifyCustomServerWhatGoodsSaled(
                                        orderCallbackUrlObject, boxId,
                                        currentUser.getPhoneNumber(),
                                        openDoorRequestSerialNumber,
                                        orderJsonObject);
                        // 如果是Yoho代理模式,则把订单信息回调给代理开发服务器
                    } else if (urlType == ResponseStatus.COOPERATIONMODE_AGENT) {
                        customWebSocketService
                                .notifyCustomerServerWhatGoodsSaled(orderType,
                                        orderCallbackUrlObject,
                                        orderJsonObject, orderDate, boxId,
                                        currentUser.getPhoneNumber(),
                                        addPersonCreditOrPointsRecord);
                    }
                }
                // ===========对接商户服务器逻辑===========================================================================

                log.info("BoxGoodsXXXService: notifyWXWhatGoodsSaled:payState=1 RFID: orderJsonObject="
                        + orderJsonObject);
            } else {
                // 如果当前order的orderType==null（目前线上软件无orderType参数，为了兼容线上产品做orderType==null判断）为目前线上版本（无不锁门逻辑）人工处理以后发出的order:orderType=delayOrder时不做恢复柜子状态处理
                // 仅当orderType==null(线上产品)或为正常orderType=normalOrder时做门状态恢复处理(异常状态已经在Servlet中恢复)
                if (orderType == null
                        || (orderType != null && orderType
                                .equals(ResponseStatus.ORDER_TYPE_NORMAL))) {
                    boxStatusService.setOpenDoorState(boxId, false);
                }
                orderPropertiesJsonObject.put("payState", 0);
                orderJsonObject.put("Order", orderPropertiesJsonObject);

                // ===========对接商户服务器逻辑===========================================================================
                // 通过boxId查找商户ID，再根据商户ID查找回调Url，返回商户服务器，与商户服务器对接,如果无回调Url，则默认走智购猫平台逻辑
                CustomWebSocketService customWebSocketService = new CustomWebSocketService();
                String orderCallbackUrlObject = customWebSocketService
                        .getCustomerOrderCallbackUrl(boxId);
                // 如果没有拿到对应商户的回调Url，则默认走智狗猫流程，否则回调商户服务器
                if (orderCallbackUrlObject.equals(ResponseStatus.FAIL)) {
                    SessionMapFactory.getInstance().sendMessage(currentSession,
                            orderJsonObject.toString());
                    // 正常购物完成增加个人征信值
                    if (addPersonCreditOrPointsRecord) {
                        updatePersonalCredit(currentUser.getPhoneNumber(),
                                boxId, ResponseStatus.PERSONAL_CREDIT_ADD,
                                ResponseStatus.PERSONAL_CREDIT_ADD_VALUE);
                    }
                } else {
                    int urlType = JSONObject.fromObject(orderCallbackUrlObject)
                            .getInt("urlType");
                    // 如果是服务器接口对接模式(例如小e),则把订单回调给商户服务器
                    if (urlType == ResponseStatus.COOPERATIONMODE_THIRD_INTERFACE) {
                        customWebSocketService
                                .notifyCustomServerWhatGoodsSaled(
                                        orderCallbackUrlObject, boxId,
                                        currentUser.getPhoneNumber(),
                                        openDoorRequestSerialNumber,
                                        orderJsonObject);
                        // 如果是Yoho代理模式,则把订单信息回调给代理开发服务器
                    } else if (urlType == ResponseStatus.COOPERATIONMODE_AGENT) {
                        customWebSocketService
                                .notifyCustomerServerWhatGoodsSaled(orderType,
                                        orderCallbackUrlObject,
                                        orderJsonObject, orderDate, boxId,
                                        currentUser.getPhoneNumber(),
                                        addPersonCreditOrPointsRecord);
                    }
                }
                // ===========对接商户服务器逻辑===========================================================================

                log.info("BoxGoodsXXXService: notifyWXWhatGoodsSaled:payState=0 RFID: orderJsonObject="
                        + orderJsonObject);
            }
            // 返回orderId，以便返回工控机进行处理
            return orderId;
        }
    }

    /**
     * 处理修复以后的订单售卖商品信息进行订单更新操作 oldOrderId 需要更新原来的订单ID
     * 
     * @param customCategory
     * @param boxId
     * @param goodsIdsJsonList
     * @throws Exception
     */
    public String notifyWXWhatUpdateGoodsSaled(String oldOrderId,
            Order oldOrder, String resolverOwner, CurrentUser currentUser,
            String customCategory, String boxId, int boxType,
            List<JSONObject> goodsIdsJsonList) throws Exception {
        String orderId = null;
        // 称重方案卖出商品逻辑
        if (boxType == ResponseStatus.BOX_TYPE_WEIGHT) {
            OrderService orderService = new OrderService();

            // 把Android端传递过来的商品变更信息（条形码ID,货到号,每个货道对应的更新商品的数量）从Json对象读取出来并保存至List中
            List<WeightBoxGoodsUpdate> weightBoxGoodsUpdateFromJsonList = new ArrayList<WeightBoxGoodsUpdate>();
            for (int i = 0; i < goodsIdsJsonList.size(); i++) {
                JSONObject weightBoxGoodsUpdateJsonObject = goodsIdsJsonList
                        .get(i);
                WeightBoxGoodsUpdate weightBoxGoodsUpdate = new WeightBoxGoodsUpdate();
                String barCodeId = weightBoxGoodsUpdateJsonObject
                        .getString("barCodeId");
                // 货道商品总重量(当为上货时，表示当前货道商品的总重量，当为正常售卖时为当前货道卖出商品总重量（目前只关注每个货道只售出单个商品的情况录入统计数据库）)
                BigDecimal saledGoodsActualWeight = BigDecimal.valueOf(Double
                        .valueOf(weightBoxGoodsUpdateJsonObject
                                .getString("cardgoRoadGoodsTotalWeight")));
                int cardgoRoadId = Integer
                        .valueOf(weightBoxGoodsUpdateJsonObject
                                .getString("cardgoRoadId"));
                int goodsNumber = Integer
                        .valueOf(weightBoxGoodsUpdateJsonObject
                                .getString("goodsNumber"));
                weightBoxGoodsUpdate.setBarCodeId(barCodeId);
                weightBoxGoodsUpdate
                        .setCardgoRoadGoodsTotalWeight(saledGoodsActualWeight);
                weightBoxGoodsUpdate.setCardgoRoadId(cardgoRoadId);
                weightBoxGoodsUpdate.setGoodsNumber(goodsNumber);
                weightBoxGoodsUpdateFromJsonList.add(weightBoxGoodsUpdate);
            }

            JSONArray goodsJsonArray = new JSONArray();

            List<BoxGoodsXXX> boxGoodsXXXList = new ArrayList<BoxGoodsXXX>();
            List<WeightBoxGoodsUpdate> filterWeightBoxGoodsUpdateFromJsonList = new ArrayList<WeightBoxGoodsUpdate>();

            // BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
            GoodsActualWeightService goodsActualWeightService = new GoodsActualWeightService();
            for (int i = 0; i < weightBoxGoodsUpdateFromJsonList.size(); i++) {
                WeightBoxGoodsUpdate weightBoxGoodsUpdate = weightBoxGoodsUpdateFromJsonList
                        .get(i);
                BoxGoodsXXX boxGoodsXXX = findBoxGoodsXXXByBarCodeIdAndCardgoRoadId(
                        boxId, weightBoxGoodsUpdate.getBarCodeId(),
                        weightBoxGoodsUpdate.getCardgoRoadId());
                // 如果盘点过来的数据未在数据库中，忽略不统计到账单中
                if (boxGoodsXXX != null) {
                    boxGoodsXXXList.add(boxGoodsXXX);
                    // 对应的将主控传递过来的有效的商品更新信息重新保存
                    filterWeightBoxGoodsUpdateFromJsonList
                            .add(weightBoxGoodsUpdateFromJsonList.get(i));

                    // 当主控传过来的商品对应货道只有一件商品的时候计入商品重量统计（统计商品实际重量，为后续积累商品重量误差），多于一件则忽略不计
                    if (weightBoxGoodsUpdate.getGoodsNumber() == 1) {
                        GoodsActualWeight goodsActualWeight = new GoodsActualWeight();
                        goodsActualWeight.setBoxId(boxId);
                        goodsActualWeight.setBarCodeId(weightBoxGoodsUpdate
                                .getBarCodeId());
                        goodsActualWeight.setGoodsWeight(weightBoxGoodsUpdate
                                .getCardgoRoadGoodsTotalWeight());
                        goodsActualWeight.setUpdateDateTime(new Date());
                        goodsActualWeightService
                                .addGoodsActualWeight(goodsActualWeight);
                    }
                }
            }

            // 如果除去未在数据库中商品以后，商品为空，通知客户端本次无购物
            if (boxGoodsXXXList.size() == 0) {
                return notifyWXUpdateNoGoodsSaled(oldOrderId, oldOrder,
                        resolverOwner, currentUser, customCategory, boxId);
            }

            List<String> categoryIdList = new ArrayList<String>();
            for (int i = 0; i < boxGoodsXXXList.size(); i++) {
                categoryIdList.add(boxGoodsXXXList.get(i).getBarCodeId());
            }

            int goodsTotalNumber = 0;
            // 去除重复类别商品，并且把重复商品做数量统计，为生成订单做准备,并把订单封装成Json格式字符串传递至客户端
            Set<String> uniqueSet = new HashSet<String>(categoryIdList);
            for (String categoryId : uniqueSet) {
                JSONObject boxGoodsJsonObject = new JSONObject();
                for (int i = 0; i < boxGoodsXXXList.size(); i++) {
                    BoxGoodsXXX boxGoodsXXX = boxGoodsXXXList.get(i);
                    if (boxGoodsXXX.getBarCodeId().equals(categoryId)) {
                        boxGoodsJsonObject.put("barCodeId",
                                boxGoodsXXX.getBarCodeId());
                        String goodsName = boxGoodsXXX.getGoodsName();
                        boxGoodsJsonObject.put("goodsCategoryName", goodsName);
                        boxGoodsJsonObject.put("salesMode",
                                boxGoodsXXX.getSalesMode());
                        boxGoodsJsonObject.put("goodsPrice", boxGoodsXXX
                                .getGoodsPrice().setScale(2).toString());
                        BigDecimal goodsActualPrice = (boxGoodsXXX
                                .getGoodsPrice())
                                .multiply(boxGoodsXXX.getGoodsDiscount())
                                .subtract(boxGoodsXXX.getFavourable())
                                .setScale(2, BigDecimal.ROUND_UP);
                        boxGoodsJsonObject.put("goodsActualPrice",
                                goodsActualPrice.toString());
                        int categoryGoodsNumber = 0;
                        // 记录类别商品重量，以计算非标商品（按重量计价）类别商品价格
                        int categoryGoodsWeight = 0;
                        for (int j = 0; j < filterWeightBoxGoodsUpdateFromJsonList
                                .size(); j++) {
                            WeightBoxGoodsUpdate weightBoxGoodsUpdate = filterWeightBoxGoodsUpdateFromJsonList
                                    .get(j);
                            if (categoryId.equals(weightBoxGoodsUpdate
                                    .getBarCodeId())) {
                                categoryGoodsNumber = categoryGoodsNumber
                                        + weightBoxGoodsUpdate.getGoodsNumber();
                                categoryGoodsWeight = categoryGoodsWeight
                                        + (int) (Double
                                                .valueOf(weightBoxGoodsUpdate
                                                        .getCardgoRoadGoodsTotalWeight()
                                                        .toString())
                                                .doubleValue());
                                continue;
                            }
                        }
                        goodsTotalNumber = goodsTotalNumber
                                + categoryGoodsNumber;
                        boxGoodsJsonObject.put("categoryGoodsNumber",
                                categoryGoodsNumber);
                        boxGoodsJsonObject.put("categoryGoodsWeight",
                                categoryGoodsWeight);
                        BigDecimal categoryGoodsTotalPrice = new BigDecimal(0);
                        BigDecimal categoryGoodsTotalDiscount = new BigDecimal(
                                0);
                        BigDecimal actualCategoryPrice = new BigDecimal(0);
                        // 判断商品是否非标（售卖方式:按件数售卖还是按重量售卖）
                        if (boxGoodsXXX.getSalesMode() == ResponseStatus.SALES_MODE_NUMBER) {
                            // 标准商品，按件数计算价格
                            categoryGoodsTotalPrice = (boxGoodsXXX
                                    .getGoodsPrice()).multiply(
                                    BigDecimal.valueOf(Double
                                            .valueOf(categoryGoodsNumber)))
                                    .setScale(2, BigDecimal.ROUND_UP);
                            categoryGoodsTotalDiscount = ((BigDecimal
                                    .valueOf(1.0d).subtract(boxGoodsXXX
                                    .getGoodsDiscount())).multiply(boxGoodsXXX
                                    .getGoodsPrice()).add(boxGoodsXXX
                                    .getFavourable())).multiply(
                                    BigDecimal.valueOf(Double
                                            .valueOf(categoryGoodsNumber)))
                                    .setScale(2, BigDecimal.ROUND_DOWN);
                            actualCategoryPrice = goodsActualPrice.multiply(
                                    BigDecimal.valueOf(Double
                                            .valueOf(categoryGoodsNumber)))
                                    .setScale(2, BigDecimal.ROUND_UP);
                        } else if (boxGoodsXXX.getSalesMode() == ResponseStatus.SALES_MODE_WEIGHT) {
                            // 非标产品价格计算,单价标准为类似1.2元/kg 类别商品价格=商品单价x商品重量
                            categoryGoodsTotalPrice = (boxGoodsXXX
                                    .getGoodsPrice())
                                    .multiply(
                                            BigDecimal.valueOf(Double.valueOf(Double
                                                    .valueOf(categoryGoodsWeight) / 1000)))
                                    .setScale(2, BigDecimal.ROUND_UP);
                            categoryGoodsTotalDiscount = ((BigDecimal
                                    .valueOf(1.0d).subtract(boxGoodsXXX
                                    .getGoodsDiscount())).multiply(boxGoodsXXX
                                    .getGoodsPrice()).add(boxGoodsXXX
                                    .getFavourable()))
                                    .multiply(
                                            BigDecimal.valueOf(Double.valueOf(Double.valueOf(Double
                                                    .valueOf(categoryGoodsWeight) / 1000))))
                                    .setScale(2, BigDecimal.ROUND_DOWN);
                            actualCategoryPrice = goodsActualPrice
                                    .multiply(
                                            BigDecimal.valueOf(Double.valueOf(Double.valueOf(Double
                                                    .valueOf(categoryGoodsWeight) / 1000))))
                                    .setScale(2, BigDecimal.ROUND_UP);
                        }
                        // 四舍五入保存两位数据
                        boxGoodsJsonObject.put("categoryGoodsTotalPrice",
                                categoryGoodsTotalPrice.toString());
                        // 四舍五入保存两位数据
                        boxGoodsJsonObject.put("categoryGoodsTotalDiscount",
                                categoryGoodsTotalDiscount.toString());
                        boxGoodsJsonObject.put("actualCategoryPrice",
                                actualCategoryPrice.toString());
                        break;
                    }
                }
                goodsJsonArray.add(boxGoodsJsonObject);
            }

            JSONObject orderPropertiesJsonObject = new JSONObject();

            Date orderDate = new Date();
            orderId = orderService.makeOrderId(orderDate);
            orderPropertiesJsonObject.put("orderId", orderId);
            orderPropertiesJsonObject
                    .put("vipLevel", currentUser.getVipLevel());
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            orderPropertiesJsonObject.put("orderDate", df.format(orderDate));

            orderPropertiesJsonObject.put("goodsTotalNumber", goodsTotalNumber);

            BigDecimal payTotal = new BigDecimal(0);
            BigDecimal totalDiscount = new BigDecimal(0);
            BigDecimal actualPayTotal = new BigDecimal(0);
            for (int i = 0; i < goodsJsonArray.size(); i++) {
                JSONObject boxGoodsJsonObject = goodsJsonArray.getJSONObject(i);
                int goodsNumber = Integer.parseInt(boxGoodsJsonObject.get(
                        "categoryGoodsNumber").toString());
                int categoryGoodsWeight = Integer.parseInt(boxGoodsJsonObject
                        .get("categoryGoodsWeight").toString());
                BigDecimal goodsPrice = BigDecimal.valueOf(Double
                        .valueOf(boxGoodsJsonObject.getString(("goodsPrice"))));

                // 标准商品按件数计算总价，非标商品按重量计算价格
                int salesMode = Integer.valueOf(boxGoodsJsonObject
                        .getString("salesMode"));
                if (salesMode == ResponseStatus.SALES_MODE_NUMBER) {
                    payTotal = payTotal.add((goodsPrice.multiply(BigDecimal
                            .valueOf(Double.valueOf(goodsNumber)))));
                } else if (salesMode == ResponseStatus.SALES_MODE_WEIGHT) {
                    payTotal = payTotal.add((goodsPrice.multiply(BigDecimal
                            .valueOf(Double.valueOf(Double
                                    .valueOf(categoryGoodsWeight) / 1000)))));
                }

                totalDiscount = totalDiscount.add(BigDecimal.valueOf(Double
                        .valueOf(boxGoodsJsonObject
                                .getString("categoryGoodsTotalDiscount"))));
            }
            // 四舍五入保存两位数据
            orderPropertiesJsonObject.put("payTotal",
                    payTotal.setScale(2, BigDecimal.ROUND_DOWN).toString());

            orderPropertiesJsonObject.put("totalFavourable", totalDiscount
                    .setScale(2, BigDecimal.ROUND_DOWN).toString());

            actualPayTotal = payTotal.subtract(totalDiscount);

            orderPropertiesJsonObject.put("actualPayTotal", actualPayTotal
                    .setScale(2, BigDecimal.ROUND_DOWN).toString());

            // 本次购物获取的积分
            int pointsChangeValue = (int) Double.valueOf(
                    actualPayTotal.setScale(2, BigDecimal.ROUND_DOWN)
                            .toString()).doubleValue();
            // 如果有小数，积分向前进一位
            if (pointsChangeValue < Double.valueOf(
                    actualPayTotal.setScale(2, BigDecimal.ROUND_DOWN)
                            .toString()).doubleValue()) {
                pointsChangeValue = pointsChangeValue + 1;
            }

            orderPropertiesJsonObject.put("Goods", goodsJsonArray);

            JSONObject orderJsonObject = new JSONObject();
            orderJsonObject.put("Order", orderPropertiesJsonObject);

            for (int i = 0; i < boxGoodsXXXList.size(); i++) {
                // 卖出商品以后将BoxXXX中对应商品移除
                WeightBoxGoodsUpdate weightBoxGoodsUpdate = filterWeightBoxGoodsUpdateFromJsonList
                        .get(i);
                updateBoxGoodsByBarCodeIdAndCardgoRoadIdAndNumberOfChange(
                        boxId, weightBoxGoodsUpdate.getBarCodeId(),
                        weightBoxGoodsUpdate.getCardgoRoadId(),
                        weightBoxGoodsUpdate.getGoodsNumber());
            }

            // 把当前订单出入订单表中和订单明细表中,然后把订单信息返回客户端,并设置柜门状态
            // 订单推送完成以后删除当前货柜注册用户信息,但为了防止用户非法瞎搞货柜，会把瞎搞产品订单强制记录在上次购物用户头上，所以不对当前货柜用户做删除处理
            // CurrentUserService currentUserService = new CurrentUserService();
            if (orderService.makeUpdateOrder(oldOrderId, oldOrder,
                    resolverOwner, orderJsonObject, orderDate, boxId,
                    currentUser)
            /* && currentUserService.deleteCurrentUserByBoxId(boxId) */) {
                orderPropertiesJsonObject.put("payState", 1);
                orderJsonObject.put("Order", orderPropertiesJsonObject);

                // ===========对接商户服务器逻辑===========================================================================
                // 通过boxId查找商户ID，再根据商户ID查找回调Url，返回商户服务器，与商户服务器对接,如果无回调Url，则默认走智购猫平台逻辑
                CustomWebSocketService customWebSocketService = new CustomWebSocketService();
                String orderCallbackUrlObject = customWebSocketService
                        .getCustomerOrderCallbackUrl(boxId);
                // 如果没有拿到对应商户的回调Url，则默认走智狗猫流程，否则回调商户服务器
                if (orderCallbackUrlObject.equals(ResponseStatus.FAIL)) {
                    // SessionMapFactory.getInstance().sendMessage(
                    // currentSession, orderJsonObject.toString());
                } else {
                    int urlType = JSONObject.fromObject(orderCallbackUrlObject)
                            .getInt("urlType");
                    // 如果是服务器接口对接模式(例如小e),则把订单回调给商户服务器
                    if (urlType == ResponseStatus.COOPERATIONMODE_THIRD_INTERFACE) {
                        customWebSocketService
                                .notifyCustomServerUpdateWhatGoodsSaled(
                                        oldOrderId, orderCallbackUrlObject,
                                        boxId, currentUser.getPhoneNumber(),
                                        orderJsonObject);
                        // 如果是Yoho代理模式,则把订单信息回调给代理开发服务器
                    } else if (urlType == ResponseStatus.COOPERATIONMODE_AGENT) {
                        customWebSocketService
                                .notifyCustomerServerUpdateWhatGoodsSaled(
                                        oldOrderId, orderCallbackUrlObject,
                                        orderJsonObject, resolverOwner,
                                        orderDate, boxId);
                    }
                }
                // ===========对接商户服务器逻辑===========================================================================

                log.info("BoxGoodsXXXService: notifyWXWhatGoodsSaled:payState=1 WEIGHT: orderJsonObject="
                        + orderJsonObject);
            } else {
                orderPropertiesJsonObject.put("payState", 0);
                orderJsonObject.put("Order", orderPropertiesJsonObject);

                // ===========对接商户服务器逻辑===========================================================================
                // 通过boxId查找商户ID，再根据商户ID查找回调Url，返回商户服务器，与商户服务器对接,如果无回调Url，则默认走智购猫平台逻辑
                CustomWebSocketService customWebSocketService = new CustomWebSocketService();
                String orderCallbackUrlObject = customWebSocketService
                        .getCustomerOrderCallbackUrl(boxId);
                // 如果没有拿到对应商户的回调Url，则默认走智狗猫流程，否则回调商户服务器
                if (orderCallbackUrlObject.equals(ResponseStatus.FAIL)) {
                } else {
                    int urlType = JSONObject.fromObject(orderCallbackUrlObject)
                            .getInt("urlType");
                    // 如果是服务器接口对接模式(例如小e),则把订单回调给商户服务器
                    if (urlType == ResponseStatus.COOPERATIONMODE_THIRD_INTERFACE) {
                        customWebSocketService
                                .notifyCustomServerUpdateWhatGoodsSaled(
                                        oldOrderId, orderCallbackUrlObject,
                                        boxId, currentUser.getPhoneNumber(),
                                        orderJsonObject);
                        // 如果是Yoho代理模式,则把订单信息回调给代理开发服务器
                    } else if (urlType == ResponseStatus.COOPERATIONMODE_AGENT) {
                        customWebSocketService
                                .notifyCustomerServerUpdateWhatGoodsSaled(
                                        oldOrderId, orderCallbackUrlObject,
                                        orderJsonObject, resolverOwner,
                                        orderDate, boxId);
                    }
                }
                // ===========对接商户服务器逻辑===========================================================================

                log.info("BoxGoodsXXXService: notifyWXWhatGoodsSaled:payState=0 WEIGHT: orderJsonObject="
                        + orderJsonObject);
            }
        }
        // 返回orderId，以便返回工控机进行处理
        return orderId;
    }

    // 无购物订单
    public String notifyWXNoGoodsSaled(String orderType, String customCategory,
            String boxId, String phoneNumber, String openDoorRequestSerialNumber)
            throws Exception {
        if (phoneNumber == null) {
            CurrentUserService currentUserService = new CurrentUserService();
            CurrentUser currentUser = currentUserService
                    .findCurrentUserByBoxId(boxId);
            phoneNumber = currentUser.getPhoneNumber();
        }
        Session currentSession = SessionMapFactory.getInstance()
                .getCurrentSession(customCategory, boxId);

        OrderService orderService = new OrderService();
        Date orderDate = new Date();
        String orderId = orderService.makeOrderId(orderDate);
        // 保存无购物订单
        try {
            orderService.makeNoGoodsSaledOrder(orderId, orderDate, boxId);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }

        BoxStatusService boxStatusService = new BoxStatusService();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", "noGoods");

        // 订单推送完成以后删除当前货柜注册用户信息,但为了防止用户非法瞎搞货柜，会把瞎搞产品订单强制记录在上次购物用户头上，所以不对当前货柜用户做删除处理
        // CurrentUserService currentUserService = new CurrentUserService();
        // 如果当前order的orderType==null（目前线上软件无orderType参数，为了兼容线上产品做orderType==null判断）为目前线上版本（无不锁门逻辑）人工处理以后发出的order:orderType=delayOrder时不做恢复柜子状态处理
        // 仅当orderType==null(线上产品)或为正常orderType=normalOrder时做门状态恢复处理(异常状态已经在Servlet中恢复)
        if (orderType == null
                || (orderType != null && orderType
                        .equals(ResponseStatus.ORDER_TYPE_NORMAL))) {
            boxStatusService.setOpenDoorState(boxId, false);
        }

        // ===========对接商户服务器逻辑===========================================================================
        // 通过boxId查找商户ID，再根据商户ID查找回调Url，返回商户服务器，与商户服务器对接,如果无回调Url，则默认走智购猫平台逻辑
        CustomWebSocketService customWebSocketService = new CustomWebSocketService();
        String orderCallbackUrlObject = customWebSocketService
                .getCustomerOrderCallbackUrl(boxId);
        // 如果没有拿到对应商户的回调Url，则默认走智狗猫流程，否则回调商户服务器
        if (orderCallbackUrlObject.equals(ResponseStatus.FAIL)) {
            // 不锁门方案，异常视频处理以后不再发送给微信小程序客户端
            // 当前线上软件不穿点orderType参数，为了兼容线上产品在这里设置做orderType判断
            if (orderType == null) {
                SessionMapFactory.getInstance().sendMessage(currentSession,
                        jsonObject.toString());
            } else {
                if (orderType.equals(ResponseStatus.ORDER_TYPE_NORMAL)) {
                    SessionMapFactory.getInstance().sendMessage(currentSession,
                            jsonObject.toString());
                }
            }
        } else {
            int urlType = JSONObject.fromObject(orderCallbackUrlObject).getInt(
                    "urlType");
            // 如果是服务器接口对接模式(例如小e),则把订单回调给商户服务器
            if (urlType == ResponseStatus.COOPERATIONMODE_THIRD_INTERFACE) {
                // CurrentUserService currentUserService = new
                // CurrentUserService();
                // CurrentUser currentUser = currentUserService
                // .findCurrentUserByBoxId(boxId);
                JSONObject noGoodsOrderJsonObject = new JSONObject();
                JSONObject noGoodsOrderBeanJsonObject = new JSONObject();
                noGoodsOrderBeanJsonObject.put("Goods", "noGoods");
                noGoodsOrderJsonObject.put("Order", noGoodsOrderBeanJsonObject);
                customWebSocketService.notifyCustomServerNoGoodsSaled(
                        orderCallbackUrlObject, orderId, boxId, phoneNumber,
                        openDoorRequestSerialNumber, noGoodsOrderJsonObject);
                // 如果是Yoho代理模式,则把订单信息回调给代理开发服务器
            } else if (urlType == ResponseStatus.COOPERATIONMODE_AGENT) {
                // CurrentUserService currentUserService = new
                // CurrentUserService();
                // CurrentUser currentUser = currentUserService
                // .findCurrentUserByBoxId(boxId);
                customWebSocketService.notifyCustomerServerNoGoodsSaled(
                        orderType, orderId, orderDate, orderCallbackUrlObject,
                        boxId, phoneNumber);
            }
        }
        // ===========对接商户服务器逻辑===========================================================================

        log.info("BoxGoodsXXXService: notifyWXNoGoodsSaled");
        // 返回orderId，以便返回工控机进行处理
        return orderId;
    }

    /**
     * 修改订单为无购物订单，此时需要修改原订单，并执行退款操作
     * 
     * @param oldOrder
     * @param resolverOwner
     * @param currentUser
     * @param customCategory
     * @param boxId
     */
    public String notifyWXUpdateNoGoodsSaled(String oldOrderId, Order oldOrder,
            String resolverOwner, CurrentUser currentUser,
            String customCategory, String boxId) {
        OrderService orderService = new OrderService();

        Date orderDate = new Date();
        String orderId = orderService.makeOrderId(orderDate);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", "noGoods");

        // ===========对接商户服务器逻辑===========================================================================
        // 通过boxId查找商户ID，再根据商户ID查找回调Url，返回商户服务器，与商户服务器对接,如果无回调Url，则默认走智购猫平台逻辑
        CustomWebSocketService customWebSocketService = new CustomWebSocketService();
        String orderCallbackUrlObject = customWebSocketService
                .getCustomerOrderCallbackUrl(boxId);
        // 如果没有拿到对应商户的回调Url，则默认走智狗猫流程，否则回调商户服务器
        if (orderCallbackUrlObject.equals(ResponseStatus.FAIL)) {
            orderService.makeUpdateNoGoodsSaledOrder(oldOrderId, oldOrder,
                    orderId, orderDate, boxId, resolverOwner, currentUser);
        } else {
            int urlType = JSONObject.fromObject(orderCallbackUrlObject).getInt(
                    "urlType");
            // 如果是服务器接口对接模式(例如小e),则把订单回调给商户服务器
            if (urlType == ResponseStatus.COOPERATIONMODE_THIRD_INTERFACE) {
                OrderThirdPartService orderThirdPartService = new OrderThirdPartService();
                OrderThirdPart orderThirdPart = orderThirdPartService
                        .findOrderThirdPartByOrderId(oldOrderId);
                String phoneNumber = orderThirdPart != null ? orderThirdPart
                        .getPhoneNumber() : "unknowPhoneNumber";
                JSONObject noGoodsOrderJsonObject = new JSONObject();
                JSONObject noGoodsOrderBeanJsonObject = new JSONObject();
                noGoodsOrderBeanJsonObject.put("Goods", "noGoods");
                noGoodsOrderJsonObject.put("Order", noGoodsOrderBeanJsonObject);
                customWebSocketService.notifyCustomServerUpdateNoGoodsSaled(
                        oldOrderId, orderId, orderCallbackUrlObject, boxId,
                        phoneNumber, noGoodsOrderJsonObject);
                // 如果是Yoho代理模式,则把订单信息回调给代理开发服务器
            } else if (urlType == ResponseStatus.COOPERATIONMODE_AGENT) {
                customWebSocketService.notifyCustomerServerUpdateNoGoodsSaled(
                        oldOrderId, orderId, orderCallbackUrlObject, orderDate,
                        boxId, resolverOwner);
            }
            OrderThirdPartService orderThirdPartService = new OrderThirdPartService();
            OrderThirdPart orderThirdPart = orderThirdPartService
                    .findOrderThirdPartByOrderId(oldOrderId);
            CurrentUser currentUserThirdPart = new CurrentUser();
            currentUserThirdPart
                    .setPhoneNumber(orderThirdPart != null ? orderThirdPart
                            .getPhoneNumber() : "unknowPhoneNumber");
            currentUserThirdPart.setVipLevel(1);
            orderService.makeUpdateNoGoodsSaledOrder(oldOrderId, oldOrder,
                    orderId, orderDate, boxId, resolverOwner,
                    currentUserThirdPart);
        }
        // ===========对接商户服务器逻辑===========================================================================

        log.info("BoxGoodsXXXService: notifyWXNoGoodsSaled");
        // 返回orderId，以便返回工控机进行处理
        return orderId;
    }

    public void notifyWXWhatGoodsUpdated(String customCategory, String boxId,
            int boxType, JSONObject addedGoodsJsonObject,
            boolean addedGoodsFlag, JSONObject removedGoodsJsonObject,
            boolean removedGoodsFlag, JSONObject allGoodsJsonObject)
            throws Exception {
        CurrentUserService currentUserService = new CurrentUserService();
        CurrentUser currentUser = currentUserService
                .findCurrentUserByBoxId(boxId);
        // 称重方案上货逻辑
        if (boxType == ResponseStatus.BOX_TYPE_WEIGHT) {
            JSONObject updateGoodsJsonObject = new JSONObject();
            ReplenishGoodsService replenishGoodsService = new ReplenishGoodsService();
            Date replenishDate = new Date();
            String replenishId = replenishGoodsService
                    .makeReplenishId(replenishDate);
            String updateDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .format(replenishDate);
            if (addedGoodsFlag && !removedGoodsFlag) {
                JSONArray addedGoodsJsonArray = getUpdatedGoodsJsonArray(
                        addedGoodsJsonObject, "AddedGoodsObjects", boxId,
                        boxType);
                if (addedGoodsJsonArray != null) {
                    int totalAddedNumber = 0;
                    for (int i = 0; i < addedGoodsJsonArray.size(); i++) {
                        JSONObject removedJsonObject = addedGoodsJsonArray
                                .getJSONObject(i);
                        totalAddedNumber = totalAddedNumber
                                + Integer.valueOf(removedJsonObject
                                        .getString("categoryGoodsNumber"));
                    }
                    JSONObject sendAddedGoodsJsonObject = new JSONObject();
                    sendAddedGoodsJsonObject.put("updateId", replenishId);
                    sendAddedGoodsJsonObject.put("updateDateTime",
                            updateDateTime);
                    sendAddedGoodsJsonObject.put("updateGoodsFlag", "added");
                    sendAddedGoodsJsonObject.put("totalAddedNumber",
                            totalAddedNumber);
                    sendAddedGoodsJsonObject.put("AddedGoods",
                            addedGoodsJsonArray);

                    updateGoodsJsonObject.put("UpdatedGoods",
                            sendAddedGoodsJsonObject);
                } else {
                    notifyWXNoGoodsUpdated(customCategory, boxId, boxType,
                            allGoodsJsonObject);
                    return;
                }
            } else if (!addedGoodsFlag && removedGoodsFlag) {
                JSONArray removedGoodsJsonArray = getUpdatedGoodsJsonArray(
                        removedGoodsJsonObject, "RemovedGoodsObjects", boxId,
                        boxType);
                if (removedGoodsJsonArray != null) {
                    int totalRemovedNumber = 0;
                    for (int i = 0; i < removedGoodsJsonArray.size(); i++) {
                        JSONObject removedJsonObject = removedGoodsJsonArray
                                .getJSONObject(i);
                        totalRemovedNumber = totalRemovedNumber
                                + Integer.valueOf(removedJsonObject
                                        .getString("categoryGoodsNumber"));
                    }
                    JSONObject sendRemovedGoodsJsonObject = new JSONObject();
                    sendRemovedGoodsJsonObject.put("updateId", replenishId);
                    sendRemovedGoodsJsonObject.put("updateDateTime",
                            updateDateTime);
                    sendRemovedGoodsJsonObject
                            .put("updateGoodsFlag", "removed");
                    sendRemovedGoodsJsonObject.put("totalRemovedNumber",
                            totalRemovedNumber);
                    sendRemovedGoodsJsonObject.put("RemovedGoods",
                            removedGoodsJsonArray);

                    updateGoodsJsonObject.put("UpdatedGoods",
                            sendRemovedGoodsJsonObject);
                } else {
                    notifyWXNoGoodsUpdated(customCategory, boxId, boxType,
                            allGoodsJsonObject);
                    return;
                }
            } else if (addedGoodsFlag && removedGoodsFlag) {
                JSONArray addedGoodsJsonArray = getUpdatedGoodsJsonArray(
                        addedGoodsJsonObject, "AddedGoodsObjects", boxId,
                        boxType);
                JSONArray removedGoodsJsonArray = getUpdatedGoodsJsonArray(
                        removedGoodsJsonObject, "RemovedGoodsObjects", boxId,
                        boxType);
                if (addedGoodsJsonArray != null
                        && removedGoodsJsonArray != null) {
                    int totalAddedNumber = 0;
                    for (int i = 0; i < addedGoodsJsonArray.size(); i++) {
                        JSONObject removedJsonObject = addedGoodsJsonArray
                                .getJSONObject(i);
                        totalAddedNumber = totalAddedNumber
                                + Integer.valueOf(removedJsonObject
                                        .getString("categoryGoodsNumber"));
                    }
                    int totalRemovedNumber = 0;
                    for (int i = 0; i < removedGoodsJsonArray.size(); i++) {
                        JSONObject removedJsonObject = removedGoodsJsonArray
                                .getJSONObject(i);
                        totalRemovedNumber = totalRemovedNumber
                                + Integer.valueOf(removedJsonObject
                                        .getString("categoryGoodsNumber"));
                    }
                    JSONObject sendAndRemovedAddedGoodsJsonObject = new JSONObject();
                    sendAndRemovedAddedGoodsJsonObject.put("updateId",
                            replenishId);
                    sendAndRemovedAddedGoodsJsonObject.put("updateDateTime",
                            updateDateTime);
                    sendAndRemovedAddedGoodsJsonObject.put("updateGoodsFlag",
                            "addedAndRemoved");
                    sendAndRemovedAddedGoodsJsonObject.put("totalAddedNumber",
                            totalAddedNumber);
                    sendAndRemovedAddedGoodsJsonObject.put(
                            "totalRemovedNumber", totalRemovedNumber);
                    sendAndRemovedAddedGoodsJsonObject.put("AddedGoods",
                            addedGoodsJsonArray);
                    sendAndRemovedAddedGoodsJsonObject.put("RemovedGoods",
                            removedGoodsJsonArray);

                    updateGoodsJsonObject.put("UpdatedGoods",
                            sendAndRemovedAddedGoodsJsonObject);
                } else if (addedGoodsJsonArray != null
                        && removedGoodsJsonArray == null) {
                    int totalAddedNumber = 0;
                    for (int i = 0; i < addedGoodsJsonArray.size(); i++) {
                        JSONObject removedJsonObject = addedGoodsJsonArray
                                .getJSONObject(i);
                        totalAddedNumber = totalAddedNumber
                                + Integer.valueOf(removedJsonObject
                                        .getString("categoryGoodsNumber"));
                    }
                    JSONObject sendAddedGoodsJsonObject = new JSONObject();
                    sendAddedGoodsJsonObject.put("updateId", replenishId);
                    sendAddedGoodsJsonObject.put("updateDateTime",
                            updateDateTime);
                    sendAddedGoodsJsonObject.put("updateGoodsFlag", "added");
                    sendAddedGoodsJsonObject.put("totalAddedNumber",
                            totalAddedNumber);
                    sendAddedGoodsJsonObject.put("AddedGoods",
                            addedGoodsJsonArray);

                    updateGoodsJsonObject.put("UpdatedGoods",
                            sendAddedGoodsJsonObject);
                } else if (addedGoodsJsonArray == null
                        && removedGoodsJsonArray != null) {
                    int totalRemovedNumber = 0;
                    for (int i = 0; i < removedGoodsJsonArray.size(); i++) {
                        JSONObject removedJsonObject = removedGoodsJsonArray
                                .getJSONObject(i);
                        totalRemovedNumber = totalRemovedNumber
                                + Integer.valueOf(removedJsonObject
                                        .getString("categoryGoodsNumber"));
                    }
                    JSONObject sendRemovedGoodsJsonObject = new JSONObject();
                    sendRemovedGoodsJsonObject.put("updateId", replenishId);
                    sendRemovedGoodsJsonObject.put("updateDateTime",
                            updateDateTime);
                    sendRemovedGoodsJsonObject
                            .put("updateGoodsFlag", "removed");
                    sendRemovedGoodsJsonObject.put("totalRemovedNumber",
                            totalRemovedNumber);
                    sendRemovedGoodsJsonObject.put("RemovedGoods",
                            removedGoodsJsonArray);

                    updateGoodsJsonObject.put("UpdatedGoods",
                            sendRemovedGoodsJsonObject);
                } else if (addedGoodsJsonArray == null
                        && removedGoodsJsonArray == null) {
                    notifyWXNoGoodsUpdated(customCategory, boxId, boxType,
                            allGoodsJsonObject);
                    return;
                }
            }

            // 解析出主控传输过来的目前柜子中所有商品信息加入到返回客户端的Json数组中
            JSONObject boxGoodsJsonObject = new JSONObject();
            if (allGoodsJsonObject.containsKey("AllGoods")) {
                if (allGoodsJsonObject.containsValue("noGoodsObject")) {
                    // 当主控发送过来的货柜中无商品时，对货柜数据库进行同步
                    BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
                    // 在删除柜子中全部商品数据之前保留当前柜子货道商品信息，以便与上货操作结束后商品数量做对比,找出每个货道上货下架情况
                    List<BoxGoodsXXX> boxGoodsXXXsCurrentList = boxGoodsXXXService
                            .findAllBoxGoodsXXX(boxId);
                    List<BoxGoodsXXX> boxGoodsXXXsNewListTemp = boxGoodsXXXService
                            .findAllBoxGoodsXXX(boxId);

                    // 当AllGoods为noGoods时，将boxGoodsXXXsCurrentList的库存设置为0作为boxGoodsXXXsNewList
                    List<BoxGoodsXXX> boxGoodsXXXsNewList = new ArrayList<BoxGoodsXXX>();
                    for (int i = 0; i < boxGoodsXXXsNewListTemp.size(); i++) {

                        BoxGoodsXXX boxGoodsXXXCurrentTemp = boxGoodsXXXsNewListTemp
                                .get(i);
                        boxGoodsXXXCurrentTemp.setStockNumber(0);
                        BoxGoodsXXX boxGoodsXXXNew = boxGoodsXXXCurrentTemp;
                        boxGoodsXXXsNewList.add(boxGoodsXXXNew);

                    }

                    boxGoodsXXXService.emptyBoxGoodsByBoxId(boxId, boxType);
                    JSONObject noGoodsAllGoodsJsonObject = new JSONObject();
                    noGoodsAllGoodsJsonObject.put("allGoodsNumber", 0);
                    noGoodsAllGoodsJsonObject.put("exceptionGoodsNumber", 0);
                    noGoodsAllGoodsJsonObject.put("AllGoods", "noGoods");
                    noGoodsAllGoodsJsonObject.put("ExceptionGoods", "noGoods");
                    JSONObject allBoxGoodJsonObject = new JSONObject();

                    // 获取售货柜货道商品及商品库存变化（每个货道商品上下货数量）
                    JSONObject boxCardgoRoadGoodsXXXsNewAndChangeJsonObject = JSONObject
                            .fromObject(doGetNewBoxGoodsAndCardgoRoadGoodsChange(
                                    boxGoodsXXXsCurrentList,
                                    boxGoodsXXXsNewList,
                                    new ArrayList<String>()));
                    // 具体到货道和上下架过程中每个货道上下架状态信息的柜体商品信息
                    noGoodsAllGoodsJsonObject.put(
                            "AllGoodsWithCardgoRoadAndGoodsNumberChange",
                            boxCardgoRoadGoodsXXXsNewAndChangeJsonObject
                                    .getJSONArray("BoxGoodssNew"));

                    // 获取售货机层数和每层货道数信息
                    BoxesService boxesService = new BoxesService();
                    SerializeBoxBodyService serializeBoxBodyService = new SerializeBoxBodyService();
                    BoxStructureService boxStructureService = new BoxStructureService();
                    Boxes boxes = boxesService.findBoxesByBoxId(boxId);
                    SerializeBoxBody serializeBoxBody = serializeBoxBodyService
                            .findSerializeBoxBodyBySerializeBoxBodyId(boxes
                                    .getSerializeBoxBodyId());
                    noGoodsAllGoodsJsonObject
                            .put("LayerAndCardgoRoadInfo",
                                    boxStructureService
                                            .getCardgoRoadAndLayerStructureInfo(serializeBoxBody
                                                    .getStructureId()));

                    allBoxGoodJsonObject.put("AllBoxGoods",
                            noGoodsAllGoodsJsonObject);

                    JSONArray boxGoodsJonsArray = new JSONArray();
                    boxGoodsJonsArray.add(updateGoodsJsonObject);
                    boxGoodsJonsArray.add(allBoxGoodJsonObject);

                    boxGoodsJsonObject.put("BoxGoods", boxGoodsJonsArray);
                } else {
                    JSONObject allBoxJsonObject = getAllGoodsJsonObject(
                            allGoodsJsonObject, "AllGoods", boxId, boxType);

                    JSONArray boxGoodsJonsArray = new JSONArray();
                    boxGoodsJonsArray.add(updateGoodsJsonObject);
                    boxGoodsJonsArray.add(allBoxJsonObject);

                    boxGoodsJsonObject.put("BoxGoods", boxGoodsJonsArray);
                }
            }

            Session currentSession = SessionMapFactory.getInstance()
                    .getCurrentSession(customCategory, boxId);

            BoxStatusService boxStatusService = new BoxStatusService();
            // CurrentUserService currentUserService = new CurrentUserService();
            if (replenishGoodsService.makeReplenishOrder(boxGoodsJsonObject,
                    replenishDate, boxId)) {
                if (boxStatusService.setOpenDoorState(boxId, false).equals(
                        ResponseStatus.SUCCESS)
                /* && currentUserService.deleteCurrentUserByBoxId(boxId) */) {
                    // ===========对接商户服务器逻辑===========================================================================
                    // 通过boxId查找商户ID，再根据商户ID查找回调Url，返回商户服务器，与商户服务器对接,如果无回调Url，则默认走智购猫平台逻辑
                    CustomWebSocketService customWebSocketService = new CustomWebSocketService();
                    String replenishmentCallbackUrlObject = customWebSocketService
                            .getCustomerReplenishmentCallbackUrl(boxId);
                    // 如果没有拿到对应商户的回调Url，则默认走智狗猫流程，否则回调商户服务器
                    if (replenishmentCallbackUrlObject
                            .equals(ResponseStatus.FAIL)) {
                        SessionMapFactory.getInstance().sendMessage(
                                currentSession, boxGoodsJsonObject.toString());
                    } else {
                        // 为了商户能兼容使用智购云理货小程序进行理货操作，暂时保留智购云小程序通知逻辑流程
                        SessionMapFactory.getInstance().sendMessage(
                                currentSession, boxGoodsJsonObject.toString());

                        try {
                            int urlType = JSONObject.fromObject(
                                    replenishmentCallbackUrlObject).getInt(
                                    "urlType");
                            // 如果是服务器接口对接模式(例如小e),则把订单回调给商户服务器
                            if (urlType == ResponseStatus.COOPERATIONMODE_THIRD_INTERFACE) {
                                customWebSocketService
                                        .notifyCustomServerWhatGoodsUpdated(
                                                replenishmentCallbackUrlObject,
                                                boxId, currentUser
                                                        .getCustomerWorkerId(),
                                                boxGoodsJsonObject);
                            }
                        } catch (Exception e) {
                            log.error("try->catch exception:", e);
                            e.printStackTrace();
                        }
                    }
                    // ===========对接商户服务器逻辑===========================================================================
                    log.info("BoxGoodsXXXService: notifyWXWhatGoodsUpdated:WEIGHT: boxGoodsJsonObject="
                            + boxGoodsJsonObject);
                }
                // 本次理货完成以后记录当前售货机库存
                BoxGoodsStockService boxGoodsStockService = new BoxGoodsStockService();
                boxGoodsStockService.saveCurrentBoxGoodsStock(replenishId,
                        boxId);
            }
            // RFID方案上货逻辑
        } else {
            JSONObject updateGoodsJsonObject = new JSONObject();
            ReplenishGoodsService replenishGoodsService = new ReplenishGoodsService();
            Date replenishDate = new Date();
            String replenishId = replenishGoodsService
                    .makeReplenishId(replenishDate);
            String updateDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .format(replenishDate);
            if (addedGoodsFlag && !removedGoodsFlag) {
                JSONArray addedGoodsJsonArray = getUpdatedGoodsJsonArray(
                        addedGoodsJsonObject, "AddedGoodsIds", boxId, boxType);
                if (addedGoodsJsonArray != null) {
                    int totalAddedNumber = 0;
                    for (int i = 0; i < addedGoodsJsonArray.size(); i++) {
                        JSONObject removedJsonObject = addedGoodsJsonArray
                                .getJSONObject(i);
                        totalAddedNumber = totalAddedNumber
                                + Integer.valueOf(removedJsonObject
                                        .getString("categoryGoodsNumber"));
                    }
                    JSONObject sendAddedGoodsJsonObject = new JSONObject();
                    sendAddedGoodsJsonObject.put("updateId", replenishId);
                    sendAddedGoodsJsonObject.put("updateDateTime",
                            updateDateTime);
                    sendAddedGoodsJsonObject.put("updateGoodsFlag", "added");
                    sendAddedGoodsJsonObject.put("totalAddedNumber",
                            totalAddedNumber);
                    sendAddedGoodsJsonObject.put("AddedGoods",
                            addedGoodsJsonArray);

                    updateGoodsJsonObject.put("UpdatedGoods",
                            sendAddedGoodsJsonObject);
                } else {
                    notifyWXNoGoodsUpdated(customCategory, boxId, boxType,
                            allGoodsJsonObject);
                    return;
                }
            } else if (!addedGoodsFlag && removedGoodsFlag) {
                JSONArray removedGoodsJsonArray = getUpdatedGoodsJsonArray(
                        removedGoodsJsonObject, "RemovedGoodsIds", boxId,
                        boxType);
                if (removedGoodsJsonArray != null) {
                    int totalRemovedNumber = 0;
                    for (int i = 0; i < removedGoodsJsonArray.size(); i++) {
                        JSONObject removedJsonObject = removedGoodsJsonArray
                                .getJSONObject(i);
                        totalRemovedNumber = totalRemovedNumber
                                + Integer.valueOf(removedJsonObject
                                        .getString("categoryGoodsNumber"));
                    }
                    JSONObject sendRemovedGoodsJsonObject = new JSONObject();
                    sendRemovedGoodsJsonObject.put("updateId", replenishId);
                    sendRemovedGoodsJsonObject.put("updateDateTime",
                            updateDateTime);
                    sendRemovedGoodsJsonObject
                            .put("updateGoodsFlag", "removed");
                    sendRemovedGoodsJsonObject.put("totalRemovedNumber",
                            totalRemovedNumber);
                    sendRemovedGoodsJsonObject.put("RemovedGoods",
                            removedGoodsJsonArray);

                    updateGoodsJsonObject.put("UpdatedGoods",
                            sendRemovedGoodsJsonObject);
                } else {
                    notifyWXNoGoodsUpdated(customCategory, boxId, boxType,
                            allGoodsJsonObject);
                    return;
                }
            } else if (addedGoodsFlag && removedGoodsFlag) {
                JSONArray addedGoodsJsonArray = getUpdatedGoodsJsonArray(
                        addedGoodsJsonObject, "AddedGoodsIds", boxId, boxType);
                JSONArray removedGoodsJsonArray = getUpdatedGoodsJsonArray(
                        removedGoodsJsonObject, "RemovedGoodsIds", boxId,
                        boxType);
                if (addedGoodsJsonArray != null
                        && removedGoodsJsonArray != null) {
                    int totalAddedNumber = 0;
                    for (int i = 0; i < addedGoodsJsonArray.size(); i++) {
                        JSONObject removedJsonObject = addedGoodsJsonArray
                                .getJSONObject(i);
                        totalAddedNumber = totalAddedNumber
                                + Integer.valueOf(removedJsonObject
                                        .getString("categoryGoodsNumber"));
                    }
                    int totalRemovedNumber = 0;
                    for (int i = 0; i < removedGoodsJsonArray.size(); i++) {
                        JSONObject removedJsonObject = removedGoodsJsonArray
                                .getJSONObject(i);
                        totalRemovedNumber = totalRemovedNumber
                                + Integer.valueOf(removedJsonObject
                                        .getString("categoryGoodsNumber"));
                    }
                    JSONObject sendAndRemovedAddedGoodsJsonObject = new JSONObject();
                    sendAndRemovedAddedGoodsJsonObject.put("updateId",
                            replenishId);
                    sendAndRemovedAddedGoodsJsonObject.put("updateDateTime",
                            updateDateTime);
                    sendAndRemovedAddedGoodsJsonObject.put("updateGoodsFlag",
                            "addedAndRemoved");
                    sendAndRemovedAddedGoodsJsonObject.put("totalAddedNumber",
                            totalAddedNumber);
                    sendAndRemovedAddedGoodsJsonObject.put(
                            "totalRemovedNumber", totalRemovedNumber);
                    sendAndRemovedAddedGoodsJsonObject.put("AddedGoods",
                            addedGoodsJsonArray);
                    sendAndRemovedAddedGoodsJsonObject.put("RemovedGoods",
                            removedGoodsJsonArray);

                    updateGoodsJsonObject.put("UpdatedGoods",
                            sendAndRemovedAddedGoodsJsonObject);
                } else if (addedGoodsJsonArray != null
                        && removedGoodsJsonArray == null) {
                    int totalAddedNumber = 0;
                    for (int i = 0; i < addedGoodsJsonArray.size(); i++) {
                        JSONObject removedJsonObject = addedGoodsJsonArray
                                .getJSONObject(i);
                        totalAddedNumber = totalAddedNumber
                                + Integer.valueOf(removedJsonObject
                                        .getString("categoryGoodsNumber"));
                    }
                    JSONObject sendAddedGoodsJsonObject = new JSONObject();
                    sendAddedGoodsJsonObject.put("updateId", replenishId);
                    sendAddedGoodsJsonObject.put("updateDateTime",
                            updateDateTime);
                    sendAddedGoodsJsonObject.put("updateGoodsFlag", "added");
                    sendAddedGoodsJsonObject.put("totalAddedNumber",
                            totalAddedNumber);
                    sendAddedGoodsJsonObject.put("AddedGoods",
                            addedGoodsJsonArray);

                    updateGoodsJsonObject.put("UpdatedGoods",
                            sendAddedGoodsJsonObject);
                } else if (addedGoodsJsonArray == null
                        && removedGoodsJsonArray != null) {
                    int totalRemovedNumber = 0;
                    for (int i = 0; i < removedGoodsJsonArray.size(); i++) {
                        JSONObject removedJsonObject = removedGoodsJsonArray
                                .getJSONObject(i);
                        totalRemovedNumber = totalRemovedNumber
                                + Integer.valueOf(removedJsonObject
                                        .getString("categoryGoodsNumber"));
                    }
                    JSONObject sendRemovedGoodsJsonObject = new JSONObject();
                    sendRemovedGoodsJsonObject.put("updateId", replenishId);
                    sendRemovedGoodsJsonObject.put("updateDateTime",
                            updateDateTime);
                    sendRemovedGoodsJsonObject
                            .put("updateGoodsFlag", "removed");
                    sendRemovedGoodsJsonObject.put("totalRemovedNumber",
                            totalRemovedNumber);
                    sendRemovedGoodsJsonObject.put("RemovedGoods",
                            removedGoodsJsonArray);

                    updateGoodsJsonObject.put("UpdatedGoods",
                            sendRemovedGoodsJsonObject);
                } else if (addedGoodsJsonArray == null
                        && removedGoodsJsonArray == null) {
                    notifyWXNoGoodsUpdated(customCategory, boxId, boxType,
                            allGoodsJsonObject);
                    return;
                }
            }

            // 解析出主控传输过来的目前柜子中所有商品信息加入到返回客户端的Json数组中
            JSONObject boxGoodsJsonObject = new JSONObject();
            if (allGoodsJsonObject.containsKey("AllGoods")) {
                if (allGoodsJsonObject.containsValue("noGoodsId")) {
                    // 当主控发送过来的货柜中无商品时，对货柜数据库进行同步
                    BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
                    boxGoodsXXXService.emptyBoxGoodsByBoxId(boxId, boxType);
                    JSONObject noGoodsAllGoodsJsonObject = new JSONObject();
                    noGoodsAllGoodsJsonObject.put("allGoodsNumber", 0);
                    noGoodsAllGoodsJsonObject.put("exceptionGoodsNumber", 0);
                    noGoodsAllGoodsJsonObject.put("AllGoods", "noGoods");
                    noGoodsAllGoodsJsonObject.put("ExceptionGoods", "noGoods");
                    JSONObject allBoxGoodJsonObject = new JSONObject();

                    allBoxGoodJsonObject.put("AllBoxGoods",
                            noGoodsAllGoodsJsonObject);

                    JSONArray boxGoodsJonsArray = new JSONArray();
                    boxGoodsJonsArray.add(updateGoodsJsonObject);
                    boxGoodsJonsArray.add(allBoxGoodJsonObject);

                    boxGoodsJsonObject.put("BoxGoods", boxGoodsJonsArray);
                } else {
                    JSONObject allBoxJsonObject = getAllGoodsJsonObject(
                            allGoodsJsonObject, "AllGoods", boxId, boxType);

                    JSONArray boxGoodsJonsArray = new JSONArray();
                    boxGoodsJonsArray.add(updateGoodsJsonObject);
                    boxGoodsJonsArray.add(allBoxJsonObject);

                    boxGoodsJsonObject.put("BoxGoods", boxGoodsJonsArray);
                }
            }

            Session currentSession = SessionMapFactory.getInstance()
                    .getCurrentSession(customCategory, boxId);

            BoxStatusService boxStatusService = new BoxStatusService();
            // CurrentUserService currentUserService = new CurrentUserService();
            if (replenishGoodsService.makeReplenishOrder(boxGoodsJsonObject,
                    replenishDate, boxId)) {
                if (boxStatusService.setOpenDoorState(boxId, false).equals(
                        ResponseStatus.SUCCESS)
                /* && currentUserService.deleteCurrentUserByBoxId(boxId) */) {
                    SessionMapFactory.getInstance().sendMessage(currentSession,
                            boxGoodsJsonObject.toString());
                    log.info("BoxGoodsXXXService: notifyWXWhatGoodsUpdated:RFID: boxGoodsJsonObject="
                            + boxGoodsJsonObject);
                }
            }
        }
    }

    public void notifyWXNoGoodsUpdated(String customCategory, String boxId,
            int boxType, JSONObject allGoodsJsonObject) throws Exception {
        CurrentUserService currentUserService = new CurrentUserService();
        CurrentUser currentUser = currentUserService
                .findCurrentUserByBoxId(boxId);
        ReplenishGoodsService replenishGoodsService = new ReplenishGoodsService();
        Date replenishDate = new Date();
        String replenishId = replenishGoodsService
                .makeReplenishId(replenishDate);
        // 称重方案无商品更新流程
        if (boxType == ResponseStatus.BOX_TYPE_WEIGHT) {
            Session currentSession = SessionMapFactory.getInstance()
                    .getCurrentSession(customCategory, boxId);

            BoxStatusService boxStatusService = new BoxStatusService();
            JSONObject updateGoodsJsonObject = new JSONObject();
            // 无上下架时也赋予ID,以便插入一个空的上下架订单
            updateGoodsJsonObject.put("updateId", replenishId);
            updateGoodsJsonObject.put("UpdatedGoods", "noGoods");

            // 解析出主控传输过来的目前柜子中所有商品信息加入到返回客户端的Json数组中
            JSONObject boxGoodsJsonObject = new JSONObject();
            if (allGoodsJsonObject.containsKey("AllGoods")) {
                if (allGoodsJsonObject.containsValue("noGoodsObject")) {
                    // 当主控发送过来的货柜中无商品时，对货柜数据库进行同步
                    BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
                    // 在删除柜子中全部商品数据之前保留当前柜子货道商品信息，以便与上货操作结束后商品数量做对比,找出每个货道上货下架情况
                    List<BoxGoodsXXX> boxGoodsXXXsCurrentList = boxGoodsXXXService
                            .findAllBoxGoodsXXX(boxId);
                    List<BoxGoodsXXX> boxGoodsXXXsNewListTemp = boxGoodsXXXService
                            .findAllBoxGoodsXXX(boxId);

                    // 当AllGoods为noGoods时，将boxGoodsXXXsCurrentList的库存设置为0作为boxGoodsXXXsNewList
                    List<BoxGoodsXXX> boxGoodsXXXsNewList = new ArrayList<BoxGoodsXXX>();
                    for (int i = 0; i < boxGoodsXXXsNewListTemp.size(); i++) {

                        BoxGoodsXXX boxGoodsXXXCurrentTemp = boxGoodsXXXsNewListTemp
                                .get(i);
                        boxGoodsXXXCurrentTemp.setStockNumber(0);
                        BoxGoodsXXX boxGoodsXXXNew = boxGoodsXXXCurrentTemp;
                        boxGoodsXXXsNewList.add(boxGoodsXXXNew);

                    }

                    boxGoodsXXXService.emptyBoxGoodsByBoxId(boxId, boxType);
                    JSONObject noGoodsAllGoodsJsonObject = new JSONObject();
                    noGoodsAllGoodsJsonObject.put("allGoodsNumber", 0);
                    noGoodsAllGoodsJsonObject.put("exceptionGoodsNumber", 0);
                    noGoodsAllGoodsJsonObject.put("AllGoods", "noGoods");
                    noGoodsAllGoodsJsonObject.put("ExceptionGoods", "noGoods");
                    JSONObject allBoxGoodJsonObject = new JSONObject();

                    // 获取售货柜货道商品及商品库存变化（每个货道商品上下货数量）
                    JSONObject boxCardgoRoadGoodsXXXsNewAndChangeJsonObject = JSONObject
                            .fromObject(doGetNewBoxGoodsAndCardgoRoadGoodsChange(
                                    boxGoodsXXXsCurrentList,
                                    boxGoodsXXXsNewList,
                                    new ArrayList<String>()));
                    // 具体到货道和上下架过程中每个货道上下架状态信息的柜体商品信息
                    noGoodsAllGoodsJsonObject.put(
                            "AllGoodsWithCardgoRoadAndGoodsNumberChange",
                            boxCardgoRoadGoodsXXXsNewAndChangeJsonObject
                                    .getJSONArray("BoxGoodssNew"));

                    // 获取售货机层数和每层货道数信息
                    BoxesService boxesService = new BoxesService();
                    SerializeBoxBodyService serializeBoxBodyService = new SerializeBoxBodyService();
                    BoxStructureService boxStructureService = new BoxStructureService();
                    Boxes boxes = boxesService.findBoxesByBoxId(boxId);
                    SerializeBoxBody serializeBoxBody = serializeBoxBodyService
                            .findSerializeBoxBodyBySerializeBoxBodyId(boxes
                                    .getSerializeBoxBodyId());
                    noGoodsAllGoodsJsonObject
                            .put("LayerAndCardgoRoadInfo",
                                    boxStructureService
                                            .getCardgoRoadAndLayerStructureInfo(serializeBoxBody
                                                    .getStructureId()));

                    allBoxGoodJsonObject.put("AllBoxGoods",
                            noGoodsAllGoodsJsonObject);

                    JSONArray boxGoodsJonsArray = new JSONArray();
                    boxGoodsJonsArray.add(updateGoodsJsonObject);
                    boxGoodsJonsArray.add(allBoxGoodJsonObject);

                    boxGoodsJsonObject.put("BoxGoods", boxGoodsJonsArray);
                } else {
                    JSONObject allBoxJsonObject = getAllGoodsJsonObject(
                            allGoodsJsonObject, "AllGoods", boxId, boxType);

                    JSONArray boxGoodsJonsArray = new JSONArray();
                    boxGoodsJonsArray.add(updateGoodsJsonObject);
                    boxGoodsJonsArray.add(allBoxJsonObject);

                    boxGoodsJsonObject.put("BoxGoods", boxGoodsJonsArray);
                }
            }

            // CurrentUserService currentUserService = new CurrentUserService();
            // 将空订单插入数据库
            if (replenishGoodsService.makeNoUpdateGoodsReplenishOrde(
                    replenishId, replenishDate, boxId)) {
                if (boxStatusService.setOpenDoorState(boxId, false).equals(
                        ResponseStatus.SUCCESS)
                /* && currentUserService.deleteCurrentUserByBoxId(boxId) */) {
                    // ===========对接商户服务器逻辑===========================================================================
                    // 通过boxId查找商户ID，再根据商户ID查找回调Url，返回商户服务器，与商户服务器对接,如果无回调Url，则默认走智购猫平台逻辑
                    CustomWebSocketService customWebSocketService = new CustomWebSocketService();
                    String replenishmentCallbackUrlObject = customWebSocketService
                            .getCustomerReplenishmentCallbackUrl(boxId);
                    // 如果没有拿到对应商户的回调Url，则默认走智狗猫流程，否则回调商户服务器
                    if (replenishmentCallbackUrlObject
                            .equals(ResponseStatus.FAIL)) {
                        SessionMapFactory.getInstance().sendMessage(
                                currentSession, boxGoodsJsonObject.toString());
                    } else {
                        // 为了商户能兼容使用智购云理货小程序进行理货操作，暂时保留智购云小程序通知逻辑流程
                        SessionMapFactory.getInstance().sendMessage(
                                currentSession, boxGoodsJsonObject.toString());

                        try {
                            int urlType = JSONObject.fromObject(
                                    replenishmentCallbackUrlObject).getInt(
                                    "urlType");
                            // 如果是服务器接口对接模式(例如小e),则把订单回调给商户服务器
                            if (urlType == ResponseStatus.COOPERATIONMODE_THIRD_INTERFACE) {
                                customWebSocketService
                                        .notifyCustomServerNoGoodsUpdated(
                                                replenishmentCallbackUrlObject,
                                                boxId, currentUser
                                                        .getCustomerWorkerId(),
                                                boxGoodsJsonObject);
                            }
                        } catch (Exception e) {
                            log.error("try->catch exception:", e);
                            e.printStackTrace();
                        }
                    }
                    log.info("BoxGoodsXXXService: notifyWXNoGoodsUpdated:WEIGHT: boxGoodsJsonObject="
                            + boxGoodsJsonObject);
                }
            }
            // 本次理货完成以后记录当前售货机库存
            BoxGoodsStockService boxGoodsStockService = new BoxGoodsStockService();
            boxGoodsStockService.saveCurrentBoxGoodsStock(replenishId, boxId);
            // RFID方案无商品更新流程
        } else {
            Session currentSession = SessionMapFactory.getInstance()
                    .getCurrentSession(customCategory, boxId);

            BoxStatusService boxStatusService = new BoxStatusService();
            JSONObject updateGoodsJsonObject = new JSONObject();
            updateGoodsJsonObject.put("UpdatedGoods", "noGoods");

            // 解析出主控传输过来的目前柜子中所有商品信息加入到返回客户端的Json数组中
            JSONObject boxGoodsJsonObject = new JSONObject();
            if (allGoodsJsonObject.containsKey("AllGoods")) {
                if (allGoodsJsonObject.containsValue("noGoodsId")) {
                    // 当主控发送过来的货柜中无商品时，对货柜数据库进行同步
                    BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
                    boxGoodsXXXService.emptyBoxGoodsByBoxId(boxId, boxType);
                    JSONObject noGoodsAllGoodsJsonObject = new JSONObject();
                    noGoodsAllGoodsJsonObject.put("allGoodsNumber", 0);
                    noGoodsAllGoodsJsonObject.put("exceptionGoodsNumber", 0);
                    noGoodsAllGoodsJsonObject.put("AllGoods", "noGoods");
                    noGoodsAllGoodsJsonObject.put("ExceptionGoods", "noGoods");
                    JSONObject allBoxGoodJsonObject = new JSONObject();

                    allBoxGoodJsonObject.put("AllBoxGoods",
                            noGoodsAllGoodsJsonObject);

                    JSONArray boxGoodsJonsArray = new JSONArray();
                    boxGoodsJonsArray.add(updateGoodsJsonObject);
                    boxGoodsJonsArray.add(allBoxGoodJsonObject);

                    boxGoodsJsonObject.put("BoxGoods", boxGoodsJonsArray);
                } else {
                    JSONObject allBoxJsonObject = getAllGoodsJsonObject(
                            allGoodsJsonObject, "AllGoods", boxId, boxType);

                    JSONArray boxGoodsJonsArray = new JSONArray();
                    boxGoodsJonsArray.add(updateGoodsJsonObject);
                    boxGoodsJonsArray.add(allBoxJsonObject);

                    boxGoodsJsonObject.put("BoxGoods", boxGoodsJonsArray);
                }
            }

            // CurrentUserService currentUserService = new CurrentUserService();
            if (boxStatusService.setOpenDoorState(boxId, false).equals(
                    ResponseStatus.SUCCESS)
            /* && currentUserService.deleteCurrentUserByBoxId(boxId) */) {
                SessionMapFactory.getInstance().sendMessage(currentSession,
                        boxGoodsJsonObject.toString());
                log.info("BoxGoodsXXXService: notifyWXNoGoodsUpdated:RFID: boxGoodsJsonObject="
                        + boxGoodsJsonObject);
            }
        }
    }

    private JSONArray getUpdatedGoodsJsonArray(JSONObject jsonObject,
            String arrayName, String boxId, int boxType) throws Exception {

        if (boxType == ResponseStatus.BOX_TYPE_WEIGHT) {
            List<JSONObject> goodsUpdatedJsonList = JsonParseUtil
                    .parseJsonObjectToObjectList(jsonObject, arrayName);
            // 把Android端传递过来的商品变更信息（条形码ID,货到号,每个货道对应的更新商品的数量）从Json对象读取出来并保存至List中
            List<WeightBoxGoodsUpdate> weightBoxGoodsUpdateFromJsonList = new ArrayList<WeightBoxGoodsUpdate>();
            for (int i = 0; i < goodsUpdatedJsonList.size(); i++) {
                JSONObject weightBoxGoodsUpdateJsonObject = goodsUpdatedJsonList
                        .get(i);
                WeightBoxGoodsUpdate weightBoxGoodsUpdate = new WeightBoxGoodsUpdate();
                String barCodeId = weightBoxGoodsUpdateJsonObject
                        .getString("barCodeId");
                int cardgoRoadId = Integer
                        .valueOf(weightBoxGoodsUpdateJsonObject
                                .getString("cardgoRoadId"));
                int goodsNumber = Integer
                        .valueOf(weightBoxGoodsUpdateJsonObject
                                .getString("goodsNumber"));
                weightBoxGoodsUpdate.setBarCodeId(barCodeId);
                weightBoxGoodsUpdate.setCardgoRoadId(cardgoRoadId);
                weightBoxGoodsUpdate.setGoodsNumber(goodsNumber);
                weightBoxGoodsUpdateFromJsonList.add(weightBoxGoodsUpdate);
            }

            JSONArray goodsJsonArray = new JSONArray();

            List<BoxGoodsXXX> boxGoodsXXXList = new ArrayList<BoxGoodsXXX>();
            List<WeightBoxGoodsUpdate> filterWeightBoxGoodsUpdateFromJsonList = new ArrayList<WeightBoxGoodsUpdate>();

            for (int i = 0; i < weightBoxGoodsUpdateFromJsonList.size(); i++) {
                WeightBoxGoodsUpdate weightBoxGoodsUpdate = weightBoxGoodsUpdateFromJsonList
                        .get(i);
                BoxGoodsXXX boxGoodsXXX = findBoxGoodsXXXByBarCodeIdAndCardgoRoadId(
                        boxId, weightBoxGoodsUpdate.getBarCodeId(),
                        weightBoxGoodsUpdate.getCardgoRoadId());
                // 如果数据未在box数据库中，忽略不统计到账单中
                if (boxGoodsXXX != null) {
                    boxGoodsXXXList.add(boxGoodsXXX);
                    filterWeightBoxGoodsUpdateFromJsonList
                            .add(weightBoxGoodsUpdate);
                }
            }

            // 如果去除不在该柜子数据库中数据以后，商品为空，则认为无商品入库
            if (boxGoodsXXXList.size() == 0) {
                return null;
            }

            List<String> categoryIdList = new ArrayList<String>();
            for (int i = 0; i < boxGoodsXXXList.size(); i++) {
                categoryIdList.add(boxGoodsXXXList.get(i).getBarCodeId());
            }

            // 去除重复类别商品，并且把重复商品做数量统计，为生成订单做准备,并把订单封装成Json格式字符串传递至客户端
            Set<String> uniqueSet = new HashSet<String>(categoryIdList);
            for (String categoryId : uniqueSet) {
                JSONObject updateGoodsJsonObject = new JSONObject();
                for (int i = 0; i < boxGoodsXXXList.size(); i++) {
                    BoxGoodsXXX boxGoodsXXX = boxGoodsXXXList.get(i);
                    if (boxGoodsXXX.getBarCodeId().equals(categoryId)) {
                        updateGoodsJsonObject.put("barCodeId",
                                boxGoodsXXX.getBarCodeId());
                        String goodsName = boxGoodsXXX.getGoodsName();
                        updateGoodsJsonObject.put("goodsCategoryName",
                                goodsName);
                        int categoryGoodsNumber = 0;
                        for (int j = 0; j < filterWeightBoxGoodsUpdateFromJsonList
                                .size(); j++) {
                            WeightBoxGoodsUpdate weightBoxGoodsUpdate = filterWeightBoxGoodsUpdateFromJsonList
                                    .get(j);
                            if (categoryId.equals(weightBoxGoodsUpdate
                                    .getBarCodeId())) {
                                categoryGoodsNumber = categoryGoodsNumber
                                        + weightBoxGoodsUpdate.getGoodsNumber();
                                continue;
                            }
                        }
                        updateGoodsJsonObject.put("categoryGoodsNumber",
                                categoryGoodsNumber);
                        break;
                    }
                }
                goodsJsonArray.add(updateGoodsJsonObject);
            }
            return goodsJsonArray;
            // RFID方案上货逻辑
        } else {
            // 上架逻辑
            if (arrayName.equals("AddedGoodsIds")) {
                List<JSONObject> goodsDistinguishIdJsonList = JsonParseUtil
                        .parseJsonObjectToObjectList(jsonObject, arrayName);
                // 把Android端传递过来的商品Id从Json对象读取出来并保存至List中
                List<String> goodsDistinguishIdFromJsonList = new ArrayList<String>();
                for (int i = 0; i < goodsDistinguishIdJsonList.size(); i++) {
                    goodsDistinguishIdFromJsonList
                            .add(goodsDistinguishIdJsonList.get(i).getString(
                                    "goodsId"));
                }

                JSONArray goodsJsonArray = new JSONArray();

                BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
                InOutGoodsService inOutGoodsService = new InOutGoodsService();
                GoodsStateRecordService goodsStateRecordService = new GoodsStateRecordService();
                Date deliveryTime = new Date();
                List<InOutGoods> goodsList = new ArrayList<InOutGoods>();
                for (int i = 0; i < goodsDistinguishIdFromJsonList.size(); i++) {
                    InOutGoods inOutGoods = inOutGoodsService
                            .findInOutGoodsByGoodsRFID(goodsDistinguishIdFromJsonList
                                    .get(i));
                    // 如果上货商品不在入库数据库中，忽略不计
                    if (inOutGoods != null) {
                        goodsList.add(inOutGoods);
                        // 将主控传输过来的全部商品信息插入该柜体数据库
                        // 同时将商品插入相对应的上货货柜数据库
                        // BoxGoodsXXX boxGoodsXXX = new BoxGoodsXXX();
                        // boxGoodsXXX.setBoxId(boxId);
                        // boxGoodsXXX.setGoodsRFID(inOutGoods.getGoodsRFID());
                        // boxGoodsXXX.setBarCodeId(inOutGoods.getBarCodeId());
                        // boxGoodsXXX.setGoodsName(inOutGoods.getGoodsName());
                        // boxGoodsXXX.setBrandCompany(inOutGoods
                        // .getManufacturer());
                        // boxGoodsXXX.setGoodsPrice(inOutGoods.getGoodsPrice());
                        // boxGoodsXXX.setGoodsDiscount(inOutGoods
                        // .getGoodsDiscount());
                        // boxGoodsXXX.setFavourable(inOutGoods.getFavourable());
                        // boxGoodsXXX.setMakeDate(inOutGoods.getMakeDate());
                        // boxGoodsXXX.setExpiryTime(inOutGoods.getExpiryTime());
                        // boxGoodsXXX.setGoodsPicture(inOutGoods
                        // .getGoodsPicture());
                        // boxGoodsXXX.setDeliveryManagerId("deliveryManagerId");
                        // boxGoodsXXX.setDeliveryTime(deliveryTime);
                        //
                        // boxGoodsXXXService.addBoxGoodsXXX(boxId, boxType,
                        // boxGoodsXXX);
                        // 此处，暂时不做删除操作，统一在获取到柜子中所有商品信息以后做删除和先清空商品数据库再插入数据操作，确保每次上货以后数据库中数据与盘点数据一致；

                        // 上货以后，删除入库出库表InOutGoods中对应商品
                        // inOutGoodsService
                        // .deleteInOutGoodsByGoodsRFID(inOutGoods
                        // .getGoodsRFID());
                        // 此处，暂时不做删除操作，统一在获取到柜子中所有商品信息以后做删除和先清空商品数据库再插入数据操作，确保每次上货以后数据库中数据与盘点数据一致；

                        // 同时将商品状态置为已上货
                        goodsStateRecordService
                                .updateGoodsStateRecordByDistinguishId(
                                        inOutGoods.getGoodsRFID(), 1);
                    }
                }

                // 如果去除不在入库商品数据库中数据以后，商品为空，则认为无商品入库
                if (goodsList.size() == 0) {
                    return null;
                }

                List<String> categoryIdList = new ArrayList<String>();
                for (int i = 0; i < goodsList.size(); i++) {
                    categoryIdList.add(goodsList.get(i).getBarCodeId());
                }

                // 去除重复类别商品，并且把重复商品做数量统计，为生成订单做准备,并把订单封装成Json格式字符串传递至客户端
                Set<String> uniqueSet = new HashSet<String>(categoryIdList);
                for (String categoryId : uniqueSet) {
                    JSONObject updateGoodsJsonObject = new JSONObject();
                    for (int i = 0; i < goodsList.size(); i++) {
                        InOutGoods inOutGoods = goodsList.get(i);
                        if (inOutGoods.getBarCodeId().equals(categoryId)) {
                            updateGoodsJsonObject.put("barCodeId",
                                    inOutGoods.getBarCodeId());
                            String goodsName = inOutGoods.getGoodsName();
                            updateGoodsJsonObject.put("goodsCategoryName",
                                    goodsName);
                            updateGoodsJsonObject.put("categoryGoodsNumber",
                                    Collections.frequency(categoryIdList,
                                            categoryId));
                            break;
                        }
                    }
                    goodsJsonArray.add(updateGoodsJsonObject);
                }
                return goodsJsonArray;
            } else {
                // Removed逻辑：即下架逻辑
                List<JSONObject> goodsDistinguishIdJsonList = JsonParseUtil
                        .parseJsonObjectToObjectList(jsonObject, arrayName);
                // 把Android端传递过来的商品Id从Json对象读取出来并保存至List中
                List<String> goodsDistinguishIdFromJsonList = new ArrayList<String>();
                for (int i = 0; i < goodsDistinguishIdJsonList.size(); i++) {
                    goodsDistinguishIdFromJsonList
                            .add(goodsDistinguishIdJsonList.get(i).getString(
                                    "goodsId"));
                }

                JSONArray goodsJsonArray = new JSONArray();

                BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
                GoodsStateRecordService goodsStateRecordService = new GoodsStateRecordService();
                List<BoxGoodsXXX> goodsList = new ArrayList<BoxGoodsXXX>();
                for (int i = 0; i < goodsDistinguishIdFromJsonList.size(); i++) {
                    BoxGoodsXXX boxGoodsXXX = boxGoodsXXXService
                            .findBoxGoodsXXXByGoodsDistinguishId(boxId,
                                    goodsDistinguishIdFromJsonList.get(i));
                    // 如果下架商品不在box数据库中，忽略不计
                    if (boxGoodsXXX != null) {
                        goodsList.add(boxGoodsXXX);
                        // 将主控传输过来的全部商品信息从box数据库中删除

                        // 下架以后，删除对应box表中对应商品
                        // boxGoodsXXXService
                        // .deleteBoxGoodsXXXByGoodsDistinguishId(boxId,
                        // boxGoodsXXX.getGoodsRFID());
                        // 此处，暂时不做删除操作，统一在获取到柜子中所有商品信息以后做删除和先清空商品数据库再插入数据操作，确保每次上货以后数据库中数据与盘点数据一致；

                        // 同时将商品状态置为已下架
                        goodsStateRecordService
                                .updateGoodsStateRecordByDistinguishId(
                                        boxGoodsXXX.getGoodsRFID(), 3);
                    }
                }

                // 如果去除不在box数据库中数据以后，商品为空，则认为无下架商品
                if (goodsList.size() == 0) {
                    return null;
                }

                List<String> categoryIdList = new ArrayList<String>();
                for (int i = 0; i < goodsList.size(); i++) {
                    categoryIdList.add(goodsList.get(i).getBarCodeId());
                }

                // 去除重复类别商品，并且把重复商品做数量统计，为生成订单做准备,并把订单封装成Json格式字符串传递至客户端
                Set<String> uniqueSet = new HashSet<String>(categoryIdList);
                for (String categoryId : uniqueSet) {
                    JSONObject updateGoodsJsonObject = new JSONObject();
                    for (int i = 0; i < goodsList.size(); i++) {
                        BoxGoodsXXX boxGoodsXXX = goodsList.get(i);
                        if (boxGoodsXXX.getBarCodeId().equals(categoryId)) {
                            updateGoodsJsonObject.put("barCodeId",
                                    boxGoodsXXX.getBarCodeId());
                            String goodsName = boxGoodsXXX.getGoodsName();
                            updateGoodsJsonObject.put("goodsCategoryName",
                                    goodsName);
                            updateGoodsJsonObject.put("categoryGoodsNumber",
                                    Collections.frequency(categoryIdList,
                                            categoryId));
                            break;
                        }
                    }
                    goodsJsonArray.add(updateGoodsJsonObject);
                }
                return goodsJsonArray;
            }
        }
    }

    private JSONObject getAllGoodsJsonObject(JSONObject jsonObject,
            String arrayName, String boxId, int boxType) throws Exception {

        // 称重方案上货逻辑
        if (boxType == ResponseStatus.BOX_TYPE_WEIGHT) {
            List<JSONObject> goodsAllJsonList = JsonParseUtil
                    .parseJsonObjectToObjectList(jsonObject, arrayName);
            // 把Android端传递过来的商品变更信息（条形码ID,货到号,每个货道对应的更新商品的数量）从Json对象读取出来并保存至List中
            List<WeightBoxGoodsUpdate> weightBoxGoodsFromJsonList = new ArrayList<WeightBoxGoodsUpdate>();
            for (int i = 0; i < goodsAllJsonList.size(); i++) {
                JSONObject weightBoxGoodsUpdateJsonObject = goodsAllJsonList
                        .get(i);
                WeightBoxGoodsUpdate weightBoxGoods = new WeightBoxGoodsUpdate();
                String barCodeId = weightBoxGoodsUpdateJsonObject
                        .getString("barCodeId");
                int cardgoRoadId = Integer
                        .valueOf(weightBoxGoodsUpdateJsonObject
                                .getString("cardgoRoadId"));
                int goodsNumber = Integer
                        .valueOf(weightBoxGoodsUpdateJsonObject
                                .getString("goodsNumber"));
                // 单个货道商品总重量
                BigDecimal cardgoRoadGoodsTotalWeight = BigDecimal
                        .valueOf(Double.valueOf(weightBoxGoodsUpdateJsonObject
                                .getString("cardgoRoadGoodsTotalWeight")));
                weightBoxGoods.setBarCodeId(barCodeId);
                weightBoxGoods.setCardgoRoadId(cardgoRoadId);
                weightBoxGoods
                        .setCardgoRoadGoodsTotalWeight(cardgoRoadGoodsTotalWeight);
                weightBoxGoods.setGoodsNumber(goodsNumber);
                weightBoxGoodsFromJsonList.add(weightBoxGoods);
            }

            JSONArray goodsJsonArray = new JSONArray();

            List<BoxGoodsXXX> boxGoodsXXXList = new ArrayList<BoxGoodsXXX>();
            BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
            List<WeightBoxGoodsUpdate> filterWeightBoxGoodsUpdateFromJsonList = new ArrayList<WeightBoxGoodsUpdate>();
            // 在删除柜子中全部商品数据之前保留当前柜子货道商品信息，以便与上货操作结束后商品数量做对比,找出每个货道上货下架情况
            List<BoxGoodsXXX> boxGoodsXXXsCurrentList = boxGoodsXXXService
                    .findAllBoxGoodsXXX(boxId);
            // 先删除该柜子中全部商品数据，为重新插入做准备
            boxGoodsXXXService.emptyBoxGoodsByBoxId(boxId, boxType);
            // 异常商品（未知商品列表）
            List<String> exceptionGoodsBarCodeIdList = new ArrayList<String>();
            for (int i = 0; i < weightBoxGoodsFromJsonList.size(); i++) {
                WeightBoxGoodsUpdate weightBoxGoodsUpdate = weightBoxGoodsFromJsonList
                        .get(i);
                BoxGoodsXXX boxGoodsXXX = findBoxGoodsXXXByBarCodeIdAndCardgoRoadId(
                        boxId, weightBoxGoodsUpdate.getBarCodeId(),
                        weightBoxGoodsUpdate.getCardgoRoadId());
                // 如果盘点过来的数据未在数据库中，忽略不统计到账单中
                if (boxGoodsXXX != null) {
                    boxGoodsXXXList.add(boxGoodsXXX);
                    // 将商品重新插入对应柜子的数据库
                    updateBoxGoodsByBarCodeIdAndCardgoRoadIdAndStockNumber(
                            boxId, weightBoxGoodsUpdate.getBarCodeId(),
                            weightBoxGoodsUpdate.getCardgoRoadId(),
                            weightBoxGoodsUpdate.getGoodsNumber());
                    filterWeightBoxGoodsUpdateFromJsonList
                            .add(weightBoxGoodsUpdate);
                } else {
                    exceptionGoodsBarCodeIdList.add(weightBoxGoodsFromJsonList
                            .get(i).getCardgoRoadId()
                            + ","
                            + weightBoxGoodsFromJsonList.get(i).getBarCodeId());
                    // 货道商品为异常unknow商品时，库存恢复到本次理货前货道原货道商品库存数量
                    updateBoxGoodsByBarCodeIdAndCardgoRoadIdAndStockNumber(
                            boxId,
                            boxGoodsXXXsCurrentList.get(
                                    weightBoxGoodsUpdate.getCardgoRoadId())
                                    .getBarCodeId(),
                            weightBoxGoodsUpdate.getCardgoRoadId(),
                            boxGoodsXXXsCurrentList.get(
                                    weightBoxGoodsUpdate.getCardgoRoadId())
                                    .getStockNumber());
                }
            }

            // 柜子中上货完成后，数据库重新更新以后，获取柜子货道商品信息，以便与本次上货操作开始前商品数量做对比，找出每个货道上货下架情况
            List<BoxGoodsXXX> boxGoodsXXXsNewList = boxGoodsXXXService
                    .findAllBoxGoodsXXX(boxId);

            // 获取售货柜货道商品及商品库存变化（每个货道商品上下货数量）
            JSONObject boxCardgoRoadGoodsXXXsNewAndChangeJsonObject = JSONObject
                    .fromObject(doGetNewBoxGoodsAndCardgoRoadGoodsChange(
                            boxGoodsXXXsCurrentList, boxGoodsXXXsNewList,
                            exceptionGoodsBarCodeIdList));

            // 如果去除不在入库商品数据库中数据以后，商品为空，则认为无商品入库
            if (boxGoodsXXXList.size() == 0) {
                // 当主控发送过来的货柜中无商品时，对货柜数据库进行同步
                // 前面已清空，不再做清空操作
                // boxGoodsXXXService.emptyBoxGoodsByBoxId(boxId, boxType);
                JSONObject allGoodsJsonObject = new JSONObject();
                allGoodsJsonObject.put("allGoodsNumber", 0);
                if (exceptionGoodsBarCodeIdList.size() == 0) {
                    allGoodsJsonObject.put("exceptionGoodsNumber", 0);
                } else {
                    allGoodsJsonObject.put("exceptionGoodsNumber",
                            exceptionGoodsBarCodeIdList.size());
                }
                allGoodsJsonObject.put("AllGoods", "noGoods");
                if (exceptionGoodsBarCodeIdList.size() == 0) {
                    allGoodsJsonObject.put("ExceptionGoods", "noGoods");
                } else {
                    JSONArray exceptionGoodsJsonArray = new JSONArray();
                    for (int i = 0; i < exceptionGoodsBarCodeIdList.size(); i++) {
                        JSONObject exceptGoodsJsonObject = new JSONObject();
                        exceptGoodsJsonObject.put("exceptionGoodsCardgoRoadId",
                                Integer.valueOf(exceptionGoodsBarCodeIdList
                                        .get(i).split(",")[0]));
                        exceptGoodsJsonObject
                                .put("exceptionGoodsId",
                                        exceptionGoodsBarCodeIdList.get(i)
                                                .split(",")[1]);
                        exceptionGoodsJsonArray.add(exceptGoodsJsonObject);
                    }
                    allGoodsJsonObject.put("ExceptionGoods",
                            exceptionGoodsJsonArray);
                }

                // 具体到货道和上下架过程中每个货道上下架状态信息的柜体商品信息
                allGoodsJsonObject.put(
                        "AllGoodsWithCardgoRoadAndGoodsNumberChange",
                        boxCardgoRoadGoodsXXXsNewAndChangeJsonObject
                                .getJSONArray("BoxGoodssNew"));

                // 获取售货机层数和每层货道数信息
                BoxesService boxesService = new BoxesService();
                SerializeBoxBodyService serializeBoxBodyService = new SerializeBoxBodyService();
                BoxStructureService boxStructureService = new BoxStructureService();
                Boxes boxes = boxesService.findBoxesByBoxId(boxId);
                SerializeBoxBody serializeBoxBody = serializeBoxBodyService
                        .findSerializeBoxBodyBySerializeBoxBodyId(boxes
                                .getSerializeBoxBodyId());
                allGoodsJsonObject
                        .put("LayerAndCardgoRoadInfo",
                                boxStructureService
                                        .getCardgoRoadAndLayerStructureInfo(serializeBoxBody
                                                .getStructureId()));

                JSONObject allBoxGoodJsonObject = new JSONObject();

                allBoxGoodJsonObject.put("AllBoxGoods", allGoodsJsonObject);
                return allBoxGoodJsonObject;
            }

            List<String> categoryIdList = new ArrayList<String>();
            for (int i = 0; i < boxGoodsXXXList.size(); i++) {
                categoryIdList.add(boxGoodsXXXList.get(i).getBarCodeId());
            }

            int goodsTotalNumber = 0;
            // 去除重复类别商品，并且把重复商品做数量统计
            Set<String> uniqueSet = new HashSet<String>(categoryIdList);
            for (String categoryId : uniqueSet) {
                JSONObject updateGoodsJsonObject = new JSONObject();
                for (int i = 0; i < boxGoodsXXXList.size(); i++) {
                    BoxGoodsXXX boxGoodsXXX = boxGoodsXXXList.get(i);
                    if (boxGoodsXXX.getBarCodeId().equals(categoryId)) {
                        updateGoodsJsonObject.put("barCodeId",
                                boxGoodsXXX.getBarCodeId());
                        String goodsName = boxGoodsXXX.getGoodsName();
                        updateGoodsJsonObject.put("goodsCategoryName",
                                goodsName);
                        int categoryGoodsNumber = 0;
                        // 同类别商品总重量
                        BigDecimal categoryGoodsTotalWeight = new BigDecimal(
                                0.0);
                        for (int j = 0; j < filterWeightBoxGoodsUpdateFromJsonList
                                .size(); j++) {
                            WeightBoxGoodsUpdate weightBoxGoodsUpdate = filterWeightBoxGoodsUpdateFromJsonList
                                    .get(j);
                            if (categoryId.equals(weightBoxGoodsUpdate
                                    .getBarCodeId())) {
                                categoryGoodsTotalWeight = categoryGoodsTotalWeight
                                        .add(weightBoxGoodsUpdate
                                                .getCardgoRoadGoodsTotalWeight())
                                        .setScale(3, BigDecimal.ROUND_UP);
                                categoryGoodsNumber = categoryGoodsNumber
                                        + weightBoxGoodsUpdate.getGoodsNumber();
                                continue;
                            }
                        }
                        goodsTotalNumber = goodsTotalNumber
                                + categoryGoodsNumber;
                        updateGoodsJsonObject.put("categoryGoodsTotalWeight",
                                categoryGoodsTotalWeight);
                        updateGoodsJsonObject.put("categoryGoodsNumber",
                                categoryGoodsNumber);
                        break;
                    }
                }
                goodsJsonArray.add(updateGoodsJsonObject);
            }

            int allGoodsNumber = goodsTotalNumber;
            JSONObject allGoodsJsonObject = new JSONObject();
            allGoodsJsonObject.put("allGoodsNumber", allGoodsNumber);

            if (exceptionGoodsBarCodeIdList.size() == 0) {
                allGoodsJsonObject.put("exceptionGoodsNumber", 0);
            } else {
                allGoodsJsonObject.put("exceptionGoodsNumber",
                        exceptionGoodsBarCodeIdList.size());
            }
            allGoodsJsonObject.put("AllGoods", goodsJsonArray);

            if (exceptionGoodsBarCodeIdList.size() == 0) {
                allGoodsJsonObject.put("ExceptionGoods", "noGoods");
                // 没有异常商品的情况下，将补货过程中统计的同类商品总重量，商品数量，和计算出来的平均值插入到数据库
                JSONArray allGoodsJsonArray = allGoodsJsonObject
                        .getJSONArray("AllGoods");
                for (int k = 0; k < allGoodsJsonArray.size(); k++) {
                    JSONObject allGoodsCategoryGoodsJsonObject = allGoodsJsonArray
                            .getJSONObject(k);
                    String barCodeId = allGoodsCategoryGoodsJsonObject
                            .getString("barCodeId");
                    BigDecimal categoryGoodsTotalWeight = BigDecimal
                            .valueOf(Double.valueOf(allGoodsCategoryGoodsJsonObject
                                    .getString("categoryGoodsTotalWeight")));
                    int categoryGoodsNumber = Integer
                            .valueOf(allGoodsCategoryGoodsJsonObject
                                    .getString("categoryGoodsNumber"));
                    BigDecimal categoryGoodsAverageWeight = categoryGoodsTotalWeight
                            .divide(BigDecimal.valueOf(Double
                                    .valueOf(categoryGoodsNumber)), 3,
                                    BigDecimal.ROUND_DOWN);
                    GoodsAverageWeightService goodsWeightService = new GoodsAverageWeightService();
                    GoodsAverageWeight goodsWeight = new GoodsAverageWeight();
                    goodsWeight.setBoxId(boxId);
                    goodsWeight.setBarCodeId(barCodeId);
                    goodsWeight.setGoodsWeight(categoryGoodsTotalWeight);
                    goodsWeight.setGoodsNumber(categoryGoodsNumber);
                    goodsWeight.setAverageWeight(categoryGoodsAverageWeight);
                    goodsWeight.setUpdateDateTime(new Date());
                    goodsWeightService.addGoodsWeight(goodsWeight);
                }
            } else {
                JSONArray exceptionGoodsJsonArray = new JSONArray();
                for (int i = 0; i < exceptionGoodsBarCodeIdList.size(); i++) {
                    JSONObject exceptGoodsJsonObject = new JSONObject();
                    exceptGoodsJsonObject.put("exceptionGoodsCardgoRoadId",
                            Integer.valueOf(exceptionGoodsBarCodeIdList.get(i)
                                    .split(",")[0]));
                    exceptGoodsJsonObject.put("exceptionGoodsId",
                            exceptionGoodsBarCodeIdList.get(i).split(",")[1]);
                    exceptionGoodsJsonArray.add(exceptGoodsJsonObject);
                }
                allGoodsJsonObject.put("ExceptionGoods",
                        exceptionGoodsJsonArray);
            }
            // 具体到货道和上下架过程中每个货道上下架状态信息的柜体商品信息
            allGoodsJsonObject.put(
                    "AllGoodsWithCardgoRoadAndGoodsNumberChange",
                    boxCardgoRoadGoodsXXXsNewAndChangeJsonObject
                            .getJSONArray("BoxGoodssNew"));

            // 获取售货机层数和每层货道数信息
            BoxesService boxesService = new BoxesService();
            SerializeBoxBodyService serializeBoxBodyService = new SerializeBoxBodyService();
            BoxStructureService boxStructureService = new BoxStructureService();
            Boxes boxes = boxesService.findBoxesByBoxId(boxId);
            SerializeBoxBody serializeBoxBody = serializeBoxBodyService
                    .findSerializeBoxBodyBySerializeBoxBodyId(boxes
                            .getSerializeBoxBodyId());
            allGoodsJsonObject
                    .put("LayerAndCardgoRoadInfo",
                            boxStructureService
                                    .getCardgoRoadAndLayerStructureInfo(serializeBoxBody
                                            .getStructureId()));

            JSONObject allBoxGoodJsonObject = new JSONObject();
            allBoxGoodJsonObject.put("AllBoxGoods", allGoodsJsonObject);
            return allBoxGoodJsonObject;
            // RFID方案 上货逻辑
        } else {
            List<JSONObject> goodsDistinguishIdJsonList = JsonParseUtil
                    .parseJsonObjectToObjectList(jsonObject, arrayName);
            // 把Android端传递过来的商品Id从Json对象读取出来并保存至List中
            List<String> goodsDistinguishIdFromJsonList = new ArrayList<String>();
            for (int i = 0; i < goodsDistinguishIdJsonList.size(); i++) {
                goodsDistinguishIdFromJsonList.add(goodsDistinguishIdJsonList
                        .get(i).getString("goodsId"));
            }

            JSONArray goodsJsonArray = new JSONArray();

            BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
            InOutGoodsService inOutGoodsService = new InOutGoodsService();
            Date deliveryTime = new Date();
            List<InOutGoods> inOutGoodsList = new ArrayList<InOutGoods>();
            // 先删除该柜子中全部商品数据，为重新插入做准备
            boxGoodsXXXService.emptyBoxGoodsByBoxId(boxId, boxType);
            // 异常商品（未入库商品列表）
            List<String> exceptionGoodsDistinguishIdList = new ArrayList<String>();
            for (int i = 0; i < goodsDistinguishIdFromJsonList.size(); i++) {
                InOutGoods inOutGoods = inOutGoodsService
                        .findInOutGoodsByGoodsRFID(goodsDistinguishIdFromJsonList
                                .get(i));
                // 如果上货商品不在InOutGoods入库数据库中，忽略不计
                if (inOutGoods != null) {
                    inOutGoodsList.add(inOutGoods);
                    // 同时将商品插入相对应的上货货柜数据库
                    BoxGoodsXXX boxGoodsXXX = new BoxGoodsXXX();
                    boxGoodsXXX.setBoxId(boxId);
                    boxGoodsXXX.setGoodsRFID(inOutGoods.getGoodsRFID());
                    boxGoodsXXX.setBarCodeId(inOutGoods.getBarCodeId());
                    boxGoodsXXX.setGoodsName(inOutGoods.getGoodsName());
                    boxGoodsXXX.setBrandCompany(inOutGoods.getManufacturer());
                    boxGoodsXXX.setGoodsPrice(inOutGoods.getGoodsPrice());
                    boxGoodsXXX.setGoodsDiscount(inOutGoods.getGoodsDiscount());
                    boxGoodsXXX.setFavourable(inOutGoods.getFavourable());
                    boxGoodsXXX.setMakeDate(inOutGoods.getMakeDate());
                    boxGoodsXXX.setExpiryTime(inOutGoods.getExpiryTime());
                    boxGoodsXXX.setGoodsPicture(inOutGoods.getGoodsPicture());
                    boxGoodsXXX.setDeliveryManagerId(12345678);
                    boxGoodsXXX.setDeliveryTime(deliveryTime);

                    boxGoodsXXXService.addBoxGoodsXXX(boxId, boxType,
                            boxGoodsXXX);

                    // 上货以后，删除入库出库表InOutGoods中对应boxGoodsXXX当前上货货柜商品(此处可能有本来已经在数据库中而却没有在入库表中的记录，直接删除这些记录操作也无关紧要)
                    // inOutGoodsService.deleteInOutGoodsByGoodsRFID(inOutGoods
                    // .getGoodsRFID());
                    // 此处暂时不做删除操作，防止下次上货时因查不到商品信息导致该商品被识别为异常商品
                } else {
                    exceptionGoodsDistinguishIdList.add("0,"
                            + goodsDistinguishIdFromJsonList.get(i));
                }
            }

            // 如果去除不在入库商品数据库中数据以后，商品为空，则认为无商品上货
            if (inOutGoodsList.size() == 0) {
                // 当主控发送过来的货柜中无商品时，对货柜数据库进行同步
                // 前面已清空，不再做清空操作
                // boxGoodsXXXService.emptyBoxGoodsByBoxId(boxId, boxType);
                JSONObject allGoodsJsonObject = new JSONObject();
                allGoodsJsonObject.put("allGoodsNumber", 0);
                if (exceptionGoodsDistinguishIdList.size() == 0) {
                    allGoodsJsonObject.put("exceptionGoodsNumber", 0);
                } else {
                    allGoodsJsonObject.put("exceptionGoodsNumber",
                            exceptionGoodsDistinguishIdList.size());
                }
                allGoodsJsonObject.put("AllGoods", "noGoods");
                if (exceptionGoodsDistinguishIdList.size() == 0) {
                    allGoodsJsonObject.put("ExceptionGoods", "noGoods");
                } else {
                    JSONArray exceptionGoodsJsonArray = new JSONArray();
                    for (int i = 0; i < exceptionGoodsDistinguishIdList.size(); i++) {
                        JSONObject exceptGoodsJsonObject = new JSONObject();
                        exceptGoodsJsonObject.put(
                                "exceptionGoodsCardgoRoadId",
                                exceptionGoodsDistinguishIdList.get(i).split(
                                        ",")[0]);
                        exceptGoodsJsonObject.put(
                                "exceptionGoodsId",
                                exceptionGoodsDistinguishIdList.get(i).split(
                                        ",")[1]);
                        exceptionGoodsJsonArray.add(exceptGoodsJsonObject);
                    }
                    allGoodsJsonObject.put("ExceptionGoods",
                            exceptionGoodsJsonArray);
                }

                JSONObject allBoxGoodJsonObject = new JSONObject();

                allBoxGoodJsonObject.put("AllBoxGoods", allGoodsJsonObject);
                return allBoxGoodJsonObject;
            }

            List<String> categoryIdList = new ArrayList<String>();
            for (int i = 0; i < inOutGoodsList.size(); i++) {
                categoryIdList.add(inOutGoodsList.get(i).getBarCodeId());
            }

            // 去除重复类别商品，并且把重复商品做数量统计
            Set<String> uniqueSet = new HashSet<String>(categoryIdList);
            for (String categoryId : uniqueSet) {
                JSONObject updateGoodsJsonObject = new JSONObject();
                for (int i = 0; i < inOutGoodsList.size(); i++) {
                    InOutGoods inOutGoods = inOutGoodsList.get(i);
                    if (inOutGoods.getBarCodeId().equals(categoryId)) {
                        updateGoodsJsonObject.put("barCodeId",
                                inOutGoods.getBarCodeId());
                        String goodsName = inOutGoods.getGoodsName();
                        updateGoodsJsonObject.put("goodsCategoryName",
                                goodsName);
                        updateGoodsJsonObject.put("categoryGoodsNumber",
                                Collections.frequency(categoryIdList,
                                        categoryId));
                        break;
                    }
                }
                goodsJsonArray.add(updateGoodsJsonObject);
            }

            int allGoodsNumber = inOutGoodsList.size();
            JSONObject allGoodsJsonObject = new JSONObject();
            allGoodsJsonObject.put("allGoodsNumber", allGoodsNumber);

            if (exceptionGoodsDistinguishIdList.size() == 0) {
                allGoodsJsonObject.put("exceptionGoodsNumber", 0);
            } else {
                allGoodsJsonObject.put("exceptionGoodsNumber",
                        exceptionGoodsDistinguishIdList.size());
            }
            allGoodsJsonObject.put("AllGoods", goodsJsonArray);

            if (exceptionGoodsDistinguishIdList.size() == 0) {
                allGoodsJsonObject.put("ExceptionGoods", "noGoods");
            } else {
                JSONArray exceptionGoodsJsonArray = new JSONArray();
                for (int i = 0; i < exceptionGoodsDistinguishIdList.size(); i++) {
                    JSONObject exceptGoodsJsonObject = new JSONObject();
                    exceptGoodsJsonObject.put("exceptionGoodsCardgoRoadId",
                            Integer.valueOf(exceptionGoodsDistinguishIdList
                                    .get(i).split(",")[0]));
                    exceptGoodsJsonObject
                            .put("exceptionGoodsId",
                                    exceptionGoodsDistinguishIdList.get(i)
                                            .split(",")[1]);
                    exceptionGoodsJsonArray.add(exceptGoodsJsonObject);
                }
                allGoodsJsonObject.put("ExceptionGoods",
                        exceptionGoodsJsonArray);
            }

            JSONObject allBoxGoodJsonObject = new JSONObject();
            allBoxGoodJsonObject.put("AllBoxGoods", allGoodsJsonObject);
            return allBoxGoodJsonObject;
        }
    }

    // 获取当前柜子中所有商品，以便展示在购物界面共用户浏览,非分页查询
    public String doGetCurrentBoxAllGoods(String boxId) {
        BoxesService boxesService = new BoxesService();
        BoxBodyService boxesBodyService = new BoxBodyService();
        Boxes boxes = boxesService.findBoxesByBoxId(boxId);
        int boxType = boxesBodyService.findBoxBodyByBoxBodyId(
                boxes.getBoxBodyId()).getBoxType();
        // 称重方案
        if (boxType == ResponseStatus.BOX_TYPE_WEIGHT) {
            List<BoxGoodsXXX> boxGoodsXXXList = findAllBoxGoodsXXX(boxId);
            JSONObject allBoxGoodJsonObject = new JSONObject();
            if (boxGoodsXXXList.size() <= 0) {
                allBoxGoodJsonObject.put("totalGoodsNumber", 0);
                allBoxGoodJsonObject.put("BoxGoods", "noGoods");
            } else {
                // 查询出当前柜子中商品的所有类别
                List<String> categoryIdList = new ArrayList<String>();
                for (int i = 0; i < boxGoodsXXXList.size(); i++) {
                    categoryIdList.add(boxGoodsXXXList.get(i).getBarCodeId());
                }

                JSONArray goodsJsonArray = new JSONArray();
                // 去除重复类别商品，并且把重复商品做数量统计
                Set<String> uniqueSet = new HashSet<String>(categoryIdList);
                int totalGoodsNumber = 0;
                for (String categoryId : uniqueSet) {
                    JSONObject categoryGoodsJsonObject = new JSONObject();
                    int categoryGoodsNumber = 0;
                    for (int i = 0; i < boxGoodsXXXList.size(); i++) {
                        BoxGoodsXXX boxGoodsXXX = boxGoodsXXXList.get(i);
                        if (boxGoodsXXX.getBarCodeId().equals(categoryId)) {
                            categoryGoodsJsonObject.put("barCodeId",
                                    boxGoodsXXX.getBarCodeId());
                            String goodsName = boxGoodsXXX.getGoodsName();
                            categoryGoodsJsonObject.put("goodsCategoryName",
                                    goodsName);
                            categoryGoodsNumber = categoryGoodsNumber
                                    + boxGoodsXXX.getStockNumber();
                            categoryGoodsJsonObject.put("categoryGoodsNumber",
                                    categoryGoodsNumber);

                            categoryGoodsJsonObject.put("goodsPrice",
                                    boxGoodsXXX.getGoodsPrice().setScale(2)
                                            .toString());
                            BigDecimal goodsActualPrice = (boxGoodsXXX
                                    .getGoodsPrice())
                                    .multiply(boxGoodsXXX.getGoodsDiscount())
                                    .subtract(boxGoodsXXX.getFavourable())
                                    .setScale(2, BigDecimal.ROUND_UP);
                            categoryGoodsJsonObject.put("goodsActualPrice",
                                    goodsActualPrice.toString());
                            categoryGoodsJsonObject
                                    .put("goodsPicture",
                                            ResponseStatus.SERVER_APPCHA_URL
                                                    + boxGoodsXXX
                                                            .getGoodsPicture() != null ? (boxGoodsXXX
                                                    .getGoodsPicture().split(
                                                            ",")[0] != null ? ResponseStatus.SERVER_APPCHA_URL
                                                    + boxGoodsXXX
                                                            .getGoodsPicture()
                                                            .split(",")[0]
                                                    : "pictureNotFound")
                                                    : "pictureNotFound");
                            continue;
                        }
                    }
                    totalGoodsNumber = totalGoodsNumber + categoryGoodsNumber;
                    JSONObject goodsJsonObject = new JSONObject();
                    goodsJsonObject.put("CategoryGoods",
                            categoryGoodsJsonObject);
                    goodsJsonArray.add(goodsJsonObject);
                }
                allBoxGoodJsonObject.put("totalGoodsNumber", totalGoodsNumber);
                allBoxGoodJsonObject.put("BoxGoods", goodsJsonArray);
                // }
            }
            return allBoxGoodJsonObject.toString();
        } else {
            // RFID方案
            List<BoxGoodsXXX> boxGoodsXXXList = findAllBoxGoodsXXX(boxId);
            JSONObject allBoxGoodJsonObject = new JSONObject();
            if (boxGoodsXXXList.size() == 0) {
                allBoxGoodJsonObject.put("totalGoodsNumber", 0);
                allBoxGoodJsonObject.put("BoxGoods", "noGoods");
            } else {
                // 查询出当前柜子中商品的所有类别
                List<String> categoryIdList = new ArrayList<String>();
                for (int i = 0; i < boxGoodsXXXList.size(); i++) {
                    categoryIdList.add(boxGoodsXXXList.get(i).getBarCodeId());
                }

                JSONArray goodsJsonArray = new JSONArray();
                // 去除重复类别商品，并且把重复商品做数量统计
                Set<String> uniqueSet = new HashSet<String>(categoryIdList);
                int totalGoodsNumber = 0;
                for (String categoryId : uniqueSet) {
                    JSONObject categoryGoodsJsonObject = new JSONObject();
                    for (int i = 0; i < boxGoodsXXXList.size(); i++) {
                        BoxGoodsXXX boxGoodsXXX = boxGoodsXXXList.get(i);
                        if (boxGoodsXXX.getBarCodeId().equals(categoryId)) {
                            categoryGoodsJsonObject.put("barCodeId",
                                    boxGoodsXXX.getBarCodeId());
                            String goodsName = boxGoodsXXX.getGoodsName();
                            categoryGoodsJsonObject.put("goodsCategoryName",
                                    goodsName);
                            categoryGoodsJsonObject.put("categoryGoodsNumber",
                                    Collections.frequency(categoryIdList,
                                            categoryId));

                            categoryGoodsJsonObject.put("goodsPrice",
                                    boxGoodsXXX.getGoodsPrice().setScale(2)
                                            .toString());
                            BigDecimal goodsActualPrice = (boxGoodsXXX
                                    .getGoodsPrice())
                                    .multiply(boxGoodsXXX.getGoodsDiscount())
                                    .subtract(boxGoodsXXX.getFavourable())
                                    .setScale(2, BigDecimal.ROUND_UP);
                            categoryGoodsJsonObject.put("goodsActualPrice",
                                    goodsActualPrice.toString());
                            categoryGoodsJsonObject
                                    .put("goodsPicture",
                                            ResponseStatus.SERVER_APPCHA_URL
                                                    + boxGoodsXXX
                                                            .getGoodsPicture() != null ? (boxGoodsXXX
                                                    .getGoodsPicture().split(
                                                            ",")[0] != null ? ResponseStatus.SERVER_APPCHA_URL
                                                    + boxGoodsXXX
                                                            .getGoodsPicture()
                                                            .split(",")[0]
                                                    : "pictureNotFound")
                                                    : "pictureNotFound");
                            break;
                        }
                    }
                    totalGoodsNumber = totalGoodsNumber
                            + Collections.frequency(categoryIdList, categoryId);
                    JSONObject goodsJsonObject = new JSONObject();
                    goodsJsonObject.put("CategoryGoods",
                            categoryGoodsJsonObject);
                    goodsJsonArray.add(goodsJsonObject);
                }
                allBoxGoodJsonObject.put("totalGoodsNumber", totalGoodsNumber);
                allBoxGoodJsonObject.put("BoxGoods", goodsJsonArray);
            }
            return allBoxGoodJsonObject.toString();
        }
    }

    // 获取附近当前柜子中所有商品，以便展示在购物界面共用户浏览,非分页查询
    public String doGetNearbyCurrentBoxAllGoods(String boxId) {
        BoxesService boxesService = new BoxesService();
        BoxBodyService boxesBodyService = new BoxBodyService();
        Boxes boxes = boxesService.findBoxesByBoxId(boxId);
        int boxType = boxesBodyService.findBoxBodyByBoxBodyId(
                boxes.getBoxBodyId()).getBoxType();
        // 称重方案
        if (boxType == ResponseStatus.BOX_TYPE_WEIGHT) {
            List<BoxGoodsXXX> boxGoodsXXXList = findAllBoxGoodsXXX(boxId);
            JSONObject allBoxGoodJsonObject = new JSONObject();
            if (boxGoodsXXXList.size() <= 0) {
                allBoxGoodJsonObject.put("totalGoodsNumber", 0);
                allBoxGoodJsonObject.put("BoxGoods", "noGoods");
            } else {
                // 查询出当前柜子中商品的所有类别
                List<String> categoryIdList = new ArrayList<String>();
                for (int i = 0; i < boxGoodsXXXList.size(); i++) {
                    categoryIdList.add(boxGoodsXXXList.get(i).getBarCodeId());
                }

                JSONArray goodsJsonArray = new JSONArray();
                // 去除重复类别商品，并且把重复商品做数量统计
                Set<String> uniqueSet = new HashSet<String>(categoryIdList);
                int totalGoodsNumber = 0;
                for (String categoryId : uniqueSet) {
                    JSONObject categoryGoodsJsonObject = new JSONObject();
                    int categoryGoodsNumber = 0;
                    for (int i = 0; i < boxGoodsXXXList.size(); i++) {
                        BoxGoodsXXX boxGoodsXXX = boxGoodsXXXList.get(i);
                        if (boxGoodsXXX.getBarCodeId().equals(categoryId)) {
                            categoryGoodsJsonObject.put("barCodeId",
                                    boxGoodsXXX.getBarCodeId());
                            String goodsName = boxGoodsXXX.getGoodsName();
                            categoryGoodsJsonObject.put("goodsCategoryName",
                                    goodsName);
                            categoryGoodsNumber = categoryGoodsNumber
                                    + boxGoodsXXX.getStockNumber();
                            categoryGoodsJsonObject.put("categoryGoodsNumber",
                                    categoryGoodsNumber);

                            categoryGoodsJsonObject.put("goodsPrice",
                                    boxGoodsXXX.getGoodsPrice().setScale(2)
                                            .toString());
                            BigDecimal goodsActualPrice = (boxGoodsXXX
                                    .getGoodsPrice())
                                    .multiply(boxGoodsXXX.getGoodsDiscount())
                                    .subtract(boxGoodsXXX.getFavourable())
                                    .setScale(2, BigDecimal.ROUND_UP);
                            categoryGoodsJsonObject.put("goodsActualPrice",
                                    goodsActualPrice.toString());
                            categoryGoodsJsonObject
                                    .put("goodsPicture",
                                            ResponseStatus.SERVER_APPCHA_URL
                                                    + boxGoodsXXX
                                                            .getGoodsPicture() != null ? (boxGoodsXXX
                                                    .getGoodsPicture().split(
                                                            ",")[0] != null ? ResponseStatus.SERVER_APPCHA_URL
                                                    + boxGoodsXXX
                                                            .getGoodsPicture()
                                                            .split(",")[0]
                                                    : "pictureNotFound")
                                                    : "pictureNotFound");
                            continue;
                        }
                    }
                    totalGoodsNumber = totalGoodsNumber + categoryGoodsNumber;
                    JSONObject goodsJsonObject = new JSONObject();
                    goodsJsonObject.put("CategoryGoods",
                            categoryGoodsJsonObject);
                    goodsJsonArray.add(goodsJsonObject);
                }
                allBoxGoodJsonObject.put("totalGoodsNumber", totalGoodsNumber);
                allBoxGoodJsonObject.put("BoxGoods", goodsJsonArray);
                // }
            }
            return allBoxGoodJsonObject.toString();
        } else {
            // RFID方案
            List<BoxGoodsXXX> boxGoodsXXXList = findAllBoxGoodsXXX(boxId);
            JSONObject allBoxGoodJsonObject = new JSONObject();
            if (boxGoodsXXXList.size() == 0) {
                allBoxGoodJsonObject.put("totalGoodsNumber", 0);
                allBoxGoodJsonObject.put("BoxGoods", "noGoods");
            } else {
                // 查询出当前柜子中商品的所有类别
                List<String> categoryIdList = new ArrayList<String>();
                for (int i = 0; i < boxGoodsXXXList.size(); i++) {
                    categoryIdList.add(boxGoodsXXXList.get(i).getBarCodeId());
                }

                JSONArray goodsJsonArray = new JSONArray();
                // 去除重复类别商品，并且把重复商品做数量统计
                Set<String> uniqueSet = new HashSet<String>(categoryIdList);
                int totalGoodsNumber = 0;
                for (String categoryId : uniqueSet) {
                    JSONObject categoryGoodsJsonObject = new JSONObject();
                    for (int i = 0; i < boxGoodsXXXList.size(); i++) {
                        BoxGoodsXXX boxGoodsXXX = boxGoodsXXXList.get(i);
                        if (boxGoodsXXX.getBarCodeId().equals(categoryId)) {
                            categoryGoodsJsonObject.put("barCodeId",
                                    boxGoodsXXX.getBarCodeId());
                            String goodsName = boxGoodsXXX.getGoodsName();
                            categoryGoodsJsonObject.put("goodsCategoryName",
                                    goodsName);
                            categoryGoodsJsonObject.put("categoryGoodsNumber",
                                    Collections.frequency(categoryIdList,
                                            categoryId));

                            categoryGoodsJsonObject.put("goodsPrice",
                                    boxGoodsXXX.getGoodsPrice().setScale(2)
                                            .toString());
                            BigDecimal goodsActualPrice = (boxGoodsXXX
                                    .getGoodsPrice())
                                    .multiply(boxGoodsXXX.getGoodsDiscount())
                                    .subtract(boxGoodsXXX.getFavourable())
                                    .setScale(2, BigDecimal.ROUND_UP);
                            categoryGoodsJsonObject.put("goodsActualPrice",
                                    goodsActualPrice.toString());
                            categoryGoodsJsonObject
                                    .put("goodsPicture",
                                            ResponseStatus.SERVER_APPCHA_URL
                                                    + boxGoodsXXX
                                                            .getGoodsPicture() != null ? (boxGoodsXXX
                                                    .getGoodsPicture().split(
                                                            ",")[0] != null ? ResponseStatus.SERVER_APPCHA_URL
                                                    + boxGoodsXXX
                                                            .getGoodsPicture()
                                                            .split(",")[0]
                                                    : "pictureNotFound")
                                                    : "pictureNotFound");
                            break;
                        }
                    }
                    totalGoodsNumber = totalGoodsNumber
                            + Collections.frequency(categoryIdList, categoryId);
                    JSONObject goodsJsonObject = new JSONObject();
                    goodsJsonObject.put("CategoryGoods",
                            categoryGoodsJsonObject);
                    goodsJsonArray.add(goodsJsonObject);
                }
                allBoxGoodJsonObject.put("totalGoodsNumber", totalGoodsNumber);
                allBoxGoodJsonObject.put("BoxGoods", goodsJsonArray);
            }
            return allBoxGoodJsonObject.toString();
        }
    }

    // 更新售货机货道商品库存后重新，获取当前柜子中所有商品，以便展示在所有商品列表中
    public String doGetNewCurrentBoxAllGoods(String boxId) {
        BoxesService boxesService = new BoxesService();
        BoxBodyService boxesBodyService = new BoxBodyService();
        Boxes boxes = boxesService.findBoxesByBoxId(boxId);
        int boxType = boxesBodyService.findBoxBodyByBoxBodyId(
                boxes.getBoxBodyId()).getBoxType();
        // 称重方案
        if (boxType == ResponseStatus.BOX_TYPE_WEIGHT) {
            List<BoxGoodsXXX> boxGoodsXXXList = findAllBoxGoodsXXX(boxId);
            JSONObject allBoxGoodJsonObject = new JSONObject();
            if (boxGoodsXXXList.size() <= 0) {
                allBoxGoodJsonObject.put("allGoodsNumber", 0);
                allBoxGoodJsonObject.put("AllGoods", "noGoods");
            } else {
                // 查询出当前柜子中商品的所有类别
                List<String> categoryIdList = new ArrayList<String>();
                for (int i = 0; i < boxGoodsXXXList.size(); i++) {
                    categoryIdList.add(boxGoodsXXXList.get(i).getBarCodeId());
                }

                JSONArray goodsJsonArray = new JSONArray();
                // 去除重复类别商品，并且把重复商品做数量统计
                Set<String> uniqueSet = new HashSet<String>(categoryIdList);
                int totalGoodsNumber = 0;
                for (String categoryId : uniqueSet) {
                    JSONObject categoryGoodsJsonObject = new JSONObject();
                    int categoryGoodsNumber = 0;
                    for (int i = 0; i < boxGoodsXXXList.size(); i++) {
                        BoxGoodsXXX boxGoodsXXX = boxGoodsXXXList.get(i);
                        if (boxGoodsXXX.getBarCodeId().equals(categoryId)) {
                            categoryGoodsJsonObject.put("barCodeId",
                                    boxGoodsXXX.getBarCodeId());
                            String goodsName = boxGoodsXXX.getGoodsName();
                            categoryGoodsJsonObject.put("goodsCategoryName",
                                    goodsName);
                            categoryGoodsNumber = categoryGoodsNumber
                                    + boxGoodsXXX.getStockNumber();
                            categoryGoodsJsonObject.put("categoryGoodsNumber",
                                    categoryGoodsNumber);

                            continue;
                        }
                    }
                    totalGoodsNumber = totalGoodsNumber + categoryGoodsNumber;
                    goodsJsonArray.add(categoryGoodsJsonObject);
                }
                allBoxGoodJsonObject.put("allGoodsNumber", totalGoodsNumber);
                allBoxGoodJsonObject.put("AllGoods", goodsJsonArray);
            }
            return allBoxGoodJsonObject.toString();
        } else {
            // RFID方案
            List<BoxGoodsXXX> boxGoodsXXXList = findAllBoxGoodsXXX(boxId);
            JSONObject allBoxGoodJsonObject = new JSONObject();
            if (boxGoodsXXXList.size() == 0) {
                allBoxGoodJsonObject.put("allGoodsNumber", 0);
                allBoxGoodJsonObject.put("AllGoods", "noGoods");
            } else {
                // 查询出当前柜子中商品的所有类别
                List<String> categoryIdList = new ArrayList<String>();
                for (int i = 0; i < boxGoodsXXXList.size(); i++) {
                    categoryIdList.add(boxGoodsXXXList.get(i).getBarCodeId());
                }

                JSONArray goodsJsonArray = new JSONArray();
                // 去除重复类别商品，并且把重复商品做数量统计
                Set<String> uniqueSet = new HashSet<String>(categoryIdList);
                int totalGoodsNumber = 0;
                for (String categoryId : uniqueSet) {
                    JSONObject categoryGoodsJsonObject = new JSONObject();
                    for (int i = 0; i < boxGoodsXXXList.size(); i++) {
                        BoxGoodsXXX boxGoodsXXX = boxGoodsXXXList.get(i);
                        if (boxGoodsXXX.getBarCodeId().equals(categoryId)) {
                            categoryGoodsJsonObject.put("barCodeId",
                                    boxGoodsXXX.getBarCodeId());
                            String goodsName = boxGoodsXXX.getGoodsName();
                            categoryGoodsJsonObject.put("goodsCategoryName",
                                    goodsName);
                            categoryGoodsJsonObject.put("categoryGoodsNumber",
                                    Collections.frequency(categoryIdList,
                                            categoryId));

                            break;
                        }
                    }
                    totalGoodsNumber = totalGoodsNumber
                            + Collections.frequency(categoryIdList, categoryId);
                    goodsJsonArray.add(categoryGoodsJsonObject);
                }
                allBoxGoodJsonObject.put("allGoodsNumber", totalGoodsNumber);
                allBoxGoodJsonObject.put("AllGoods", goodsJsonArray);
            }
            return allBoxGoodJsonObject.toString();
        }
    }

    public String doGetBoxInfo(String boxId) {
        JSONObject boxGoodssJsonObject = new JSONObject();
        JSONArray boxGoodssJsonArray = new JSONArray();

        List<BoxGoodsXXX> boxGoodsXXXList = findAllBoxGoodsXXX(boxId);
        if (boxGoodsXXXList.size() > 0) {
            for (int i = 0; i < boxGoodsXXXList.size(); i++) {
                BoxGoodsXXX boxGoodsXXX = boxGoodsXXXList.get(i);
                JSONObject boxGoodsJsonObject = JSONObject
                        .fromObject(boxGoodsXXX);
                boxGoodssJsonArray.add(boxGoodsJsonObject);
            }
            boxGoodssJsonObject.put("BoxGoodsInfo", boxGoodssJsonArray);
        } else {
            boxGoodssJsonObject.put("BoxGoodsInfo", "noBoxGoods");
        }
        return boxGoodssJsonObject.toString();
    }

    // 检查是否需要补货，默认当柜子内有某一类商品少于或者等于两件时建议补货
    // 当商品总数小于20时推荐补货
    public boolean checkBoxSuggestReplenishmentState(String boxId) {
        boolean suggestReplenishmentFlag = false;
        BoxesService boxesService = new BoxesService();
        BoxBodyService boxesBodyService = new BoxBodyService();
        Boxes boxes = boxesService.findBoxesByBoxId(boxId);
        int boxType = boxesBodyService.findBoxBodyByBoxBodyId(
                boxes.getBoxBodyId()).getBoxType();
        // 称重方案
        if (boxType == ResponseStatus.BOX_TYPE_WEIGHT) {
            List<BoxGoodsXXX> boxGoodsXXXList = findAllBoxGoodsXXX(boxId);

            // 查询出当前柜子中商品的所有类别
            List<String> categoryIdList = new ArrayList<String>();
            for (int i = 0; i < boxGoodsXXXList.size(); i++) {
                categoryIdList.add(boxGoodsXXXList.get(i).getBarCodeId());
            }
            // 去除重复类别商品，并且把重复商品做数量统计
            Set<String> uniqueSet = new HashSet<String>(categoryIdList);
            String categoryGoodsName = null;
            int goodsTotalNumber = 0;
            for (String categoryId : uniqueSet) {
                int categoryGoodsNumber = 0;
                for (int i = 0; i < boxGoodsXXXList.size(); i++) {
                    BoxGoodsXXX boxGoodsXXX = boxGoodsXXXList.get(i);
                    if (boxGoodsXXX.getBarCodeId().equals(categoryId)) {
                        categoryGoodsName = boxGoodsXXX.getGoodsName();
                        categoryGoodsNumber = categoryGoodsNumber
                                + boxGoodsXXX.getStockNumber();
                        continue;
                    }
                }
                goodsTotalNumber = goodsTotalNumber + categoryGoodsNumber;
                if (categoryGoodsNumber <= BoxGoodsXXX.CATEGORY_GOODS_BASELINE) {
                    suggestReplenishmentFlag = true;
                }
            }
            if (goodsTotalNumber <= BoxGoodsXXX.TOTAL_GOODS_BASELINE) {
                suggestReplenishmentFlag = true;
            }
            return suggestReplenishmentFlag;
        } else {
            // RFID方案
            List<BoxGoodsXXX> boxGoodsXXXList = findAllBoxGoodsXXX(boxId);

            // 查询出当前柜子中商品的所有类别
            List<String> categoryIdList = new ArrayList<String>();
            for (int i = 0; i < boxGoodsXXXList.size(); i++) {
                categoryIdList.add(boxGoodsXXXList.get(i).getBarCodeId());
            }

            // 去除重复类别商品，并且把重复商品做数量统计
            Set<String> uniqueSet = new HashSet<String>(categoryIdList);
            for (String categoryId : uniqueSet) {
                String categoryGoodsName = null;
                int categoryGoodsNumber = 0;
                for (int i = 0; i < boxGoodsXXXList.size(); i++) {
                    BoxGoodsXXX boxGoodsXXX = boxGoodsXXXList.get(i);
                    if (boxGoodsXXX.getBarCodeId().equals(categoryId)) {
                        categoryGoodsName = boxGoodsXXX.getGoodsName();
                        categoryGoodsNumber = Collections.frequency(
                                categoryIdList, categoryId);
                        break;
                    }
                }
                if (categoryGoodsNumber <= BoxGoodsXXX.CATEGORY_GOODS_BASELINE) {
                    suggestReplenishmentFlag = true;
                }
            }
            if (boxGoodsXXXList.size() <= BoxGoodsXXX.TOTAL_GOODS_BASELINE) {
                suggestReplenishmentFlag = true;
            }
            return suggestReplenishmentFlag;
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

    // 补货时获取当前柜子货道商品结构
    public String doGetCurrentBoxGoods(String boxId) {
        List<BoxGoodsXXX> boxGoodsXXXsCurrentList = findAllBoxGoodsXXX(boxId);
        JSONObject currentAndNewBoxGoodsBeanObject = new JSONObject();
        JSONArray boxGoodsXXXCurrentJsonArray = new JSONArray();
        // 当前售货柜商品结构
        for (int i = 0; i < boxGoodsXXXsCurrentList.size(); i++) {
            BoxGoodsXXX boxGoodsXXXCurrent = boxGoodsXXXsCurrentList.get(i);
            JSONObject boxGoodsXXXBeanJsonObject = JSONObject
                    .fromObject(boxGoodsXXXCurrent);
            boxGoodsXXXBeanJsonObject
                    .put("goodsPicture",
                            boxGoodsXXXCurrent.getGoodsPicture() != null ? (boxGoodsXXXCurrent
                                    .getGoodsPicture().split(",")[0] != null ? ResponseStatus.SERVER_APPCHA_URL
                                    + boxGoodsXXXCurrent.getGoodsPicture()
                                            .split(",")[0]
                                    : "pictureNotFound")
                                    : "pictureNotFound");
            boxGoodsXXXBeanJsonObject.put("goodsPrice", boxGoodsXXXCurrent
                    .getGoodsPrice().setScale(2).toString());
            boxGoodsXXXBeanJsonObject.put("favourable", boxGoodsXXXCurrent
                    .getFavourable().setScale(2).toString());
            BigDecimal goodsActualPrice = (boxGoodsXXXCurrent.getGoodsPrice())
                    .multiply(boxGoodsXXXCurrent.getGoodsDiscount())
                    .subtract(boxGoodsXXXCurrent.getFavourable())
                    .setScale(2, BigDecimal.ROUND_UP);
            boxGoodsXXXBeanJsonObject.put("goodsActualPrice",
                    goodsActualPrice.toString());
            JSONObject boxGoodsXXXObjectJsonObject = new JSONObject();
            boxGoodsXXXObjectJsonObject.put("BoxGoods",
                    boxGoodsXXXBeanJsonObject);
            boxGoodsXXXCurrentJsonArray.add(boxGoodsXXXObjectJsonObject);
        }

        currentAndNewBoxGoodsBeanObject.put("BoxGoodssCurrent",
                boxGoodsXXXCurrentJsonArray);

        // 获取售货机层数和每层货道数信息
        BoxesService boxesService = new BoxesService();
        SerializeBoxBodyService serializeBoxBodyService = new SerializeBoxBodyService();
        BoxStructureService boxStructureService = new BoxStructureService();
        Boxes boxes = boxesService.findBoxesByBoxId(boxId);
        SerializeBoxBody serializeBoxBody = serializeBoxBodyService
                .findSerializeBoxBodyBySerializeBoxBodyId(boxes
                        .getSerializeBoxBodyId());
        currentAndNewBoxGoodsBeanObject.put("LayerAndCardgoRoadInfo",
                boxStructureService
                        .getCardgoRoadAndLayerStructureInfo(serializeBoxBody
                                .getStructureId()));
        return currentAndNewBoxGoodsBeanObject.toString();
    }

    // 获取当前柜子每个货道上货下架商品状态以及异常商品列表
    public String doGetNewBoxGoodsAndCardgoRoadGoodsChange(
            List<BoxGoodsXXX> currentBoxGoodsXXXsList,
            List<BoxGoodsXXX> newBoxGoodsXXXsList,
            List<String> exceptionGoodsBarCodeIdList) {
        // 已更新售货柜商品结构
        JSONArray boxGoodsXXXNewJsonArray = new JSONArray();
        for (int i = 0; i < newBoxGoodsXXXsList.size(); i++) {
            BoxGoodsXXX boxGoodsXXXNew = newBoxGoodsXXXsList.get(i);
            JSONObject boxGoodsXXXBeanJsonObject = JSONObject
                    .fromObject(boxGoodsXXXNew);
            boxGoodsXXXBeanJsonObject
                    .put("goodsPicture",
                            boxGoodsXXXNew.getGoodsPicture() != null ? (boxGoodsXXXNew
                                    .getGoodsPicture().split(",")[0] != null ? ResponseStatus.SERVER_APPCHA_URL
                                    + boxGoodsXXXNew.getGoodsPicture().split(
                                            ",")[0]
                                    : "pictureNotFound")
                                    : "pictureNotFound");
            boxGoodsXXXBeanJsonObject.put("goodsPrice", boxGoodsXXXNew
                    .getGoodsPrice().setScale(2).toString());
            boxGoodsXXXBeanJsonObject.put("favourable", boxGoodsXXXNew
                    .getFavourable().setScale(2).toString());
            BigDecimal goodsActualPrice = (boxGoodsXXXNew.getGoodsPrice())
                    .multiply(boxGoodsXXXNew.getGoodsDiscount())
                    .subtract(boxGoodsXXXNew.getFavourable())
                    .setScale(2, BigDecimal.ROUND_UP);
            boxGoodsXXXBeanJsonObject.put("goodsActualPrice",
                    goodsActualPrice.toString());
            // 对比出两个柜子中商品发生变化的货道
            BoxGoodsXXX boxGoodsXXXCurrent = currentBoxGoodsXXXsList.get(i);
            // 如果商品库存不同，说明商品已经发生了变化cardgoRoadGoodsNumberChanged=1补货，=-1，下架，=0不变=2表示该货道有unknow商品出现
            int cardgoRoadGoodsChangeNumber = boxGoodsXXXNew.getStockNumber()
                    - boxGoodsXXXCurrent.getStockNumber();
            if (cardgoRoadGoodsChangeNumber != 0) {
                // 当货道为上下架商品不为0时，优先判判断未上下架货道是否有异常商品，然后再统计上下架商品件数
                if (cardgoRoadGoodsChangeNumber > 0) {
                    boxGoodsXXXBeanJsonObject.put(
                            "cardgoRoadGoodsNumberChanged", "1");
                    boxGoodsXXXBeanJsonObject.put(
                            "cardgoRoadGoodsChangeNumber",
                            cardgoRoadGoodsChangeNumber);
                } else {
                    boxGoodsXXXBeanJsonObject.put(
                            "cardgoRoadGoodsNumberChanged", "-1");
                    boxGoodsXXXBeanJsonObject.put(
                            "cardgoRoadGoodsChangeNumber",
                            Math.abs(cardgoRoadGoodsChangeNumber));
                }
            } else {
                boxGoodsXXXBeanJsonObject.put("cardgoRoadGoodsNumberChanged",
                        "0");
                boxGoodsXXXBeanJsonObject.put("cardgoRoadGoodsChangeNumber",
                        cardgoRoadGoodsChangeNumber);
            }

            // 当货道为上下架商品不为0时，优先判判断未上下架货道是否有异常商品，然后再统计上下架商品件数
            // 当货道为没有上下架商品货到时，判断未上下架货道是否有异常商品
            for (int j = 0; j < exceptionGoodsBarCodeIdList.size(); j++) {
                String exceptionGoodsCardgoRoadId = exceptionGoodsBarCodeIdList
                        .get(j).split(",")[0];
                if (boxGoodsXXXNew.getCardgoRoadId() == Integer
                        .valueOf(exceptionGoodsCardgoRoadId)) {
                    // 检测到和异常商品货道相同
                    boxGoodsXXXBeanJsonObject.put(
                            "cardgoRoadGoodsNumberChanged", "2");
                    boxGoodsXXXBeanJsonObject.put(
                            "cardgoRoadGoodsChangeNumber",
                            cardgoRoadGoodsChangeNumber);
                    break;
                }
            }

            JSONObject boxGoodsXXXObjectJsonObject = new JSONObject();
            boxGoodsXXXObjectJsonObject.put("BoxGoods",
                    boxGoodsXXXBeanJsonObject);
            boxGoodsXXXNewJsonArray.add(boxGoodsXXXObjectJsonObject);
        }

        JSONObject boxGoodsXXXNewJsonObject = new JSONObject();
        boxGoodsXXXNewJsonObject.put("BoxGoodssNew", boxGoodsXXXNewJsonArray);
        return boxGoodsXXXNewJsonObject.toString();
    }

    public String doGetCurrentAndNewBoxGoods(String boxId) {
        BoxGoodsXXXNewService boxGoodsXXXNewService = new BoxGoodsXXXNewService();
        List<BoxGoodsXXX> boxGoodsXXXsCurrentList = findAllBoxGoodsXXX(boxId);
        List<BoxGoodsXXXNew> boxGoodsXXXNewsList = boxGoodsXXXNewService
                .findAllBoxGoodsXXXNew(boxId);
        JSONObject currentAndNewBoxGoodsBeanObject = new JSONObject();
        JSONObject currentBoxCardgoRoadGoodsStockBeforeUpdateJsonObject = new JSONObject();
        // 用来标识发生商品变化的货道是否还有原来商品的库存，”0“表示有，“1”表示没有
        String cardgoRoadGoodsStockFlag = "0";
        // 发生商品变化的货道
        JSONArray cardgoRoadGoodsStockJsonArray = new JSONArray();
        JSONObject cardgoRoadGoodsStockJsonArrayBeanJsonObject = new JSONObject();
        JSONArray boxGoodsXXXCurrentJsonArray = new JSONArray();
        // 当前售货柜商品结构
        for (int i = 0; i < boxGoodsXXXsCurrentList.size(); i++) {
            BoxGoodsXXX boxGoodsXXXCurrent = boxGoodsXXXsCurrentList.get(i);
            JSONObject boxGoodsXXXBeanJsonObject = JSONObject
                    .fromObject(boxGoodsXXXCurrent);
            boxGoodsXXXBeanJsonObject
                    .put("goodsPicture",
                            boxGoodsXXXCurrent.getGoodsPicture() != null ? (boxGoodsXXXCurrent
                                    .getGoodsPicture().split(",")[0] != null ? ResponseStatus.SERVER_APPCHA_URL
                                    + boxGoodsXXXCurrent.getGoodsPicture()
                                            .split(",")[0]
                                    : "pictureNotFound")
                                    : "pictureNotFound");
            boxGoodsXXXBeanJsonObject.put("goodsPrice", boxGoodsXXXCurrent
                    .getGoodsPrice().setScale(2).toString());
            boxGoodsXXXBeanJsonObject.put("favourable", boxGoodsXXXCurrent
                    .getFavourable().setScale(2).toString());
            BigDecimal goodsActualPrice = (boxGoodsXXXCurrent.getGoodsPrice())
                    .multiply(boxGoodsXXXCurrent.getGoodsDiscount())
                    .subtract(boxGoodsXXXCurrent.getFavourable())
                    .setScale(2, BigDecimal.ROUND_UP);
            boxGoodsXXXBeanJsonObject.put("goodsActualPrice",
                    goodsActualPrice.toString());
            JSONObject boxGoodsXXXObjectJsonObject = new JSONObject();
            boxGoodsXXXObjectJsonObject.put("BoxGoods",
                    boxGoodsXXXBeanJsonObject);
            boxGoodsXXXCurrentJsonArray.add(boxGoodsXXXObjectJsonObject);
        }

        // 已更新售货柜商品结构
        JSONArray boxGoodsXXXNewJsonArray = new JSONArray();
        int cardgoRoadGoodsChangedNumber = 0;
        for (int j = 0; j < boxGoodsXXXNewsList.size(); j++) {
            BoxGoodsXXXNew boxGoodsXXXNew = boxGoodsXXXNewsList.get(j);
            JSONObject boxGoodsXXXBeanJsonObject = JSONObject
                    .fromObject(boxGoodsXXXNew);
            boxGoodsXXXBeanJsonObject
                    .put("goodsPicture",
                            boxGoodsXXXNew.getGoodsPicture() != null ? (boxGoodsXXXNew
                                    .getGoodsPicture().split(",")[0] != null ? ResponseStatus.SERVER_APPCHA_URL
                                    + boxGoodsXXXNew.getGoodsPicture().split(
                                            ",")[0]
                                    : "pictureNotFound")
                                    : "pictureNotFound");
            boxGoodsXXXBeanJsonObject.put("goodsPrice", boxGoodsXXXNew
                    .getGoodsPrice().setScale(2).toString());
            boxGoodsXXXBeanJsonObject.put("favourable", boxGoodsXXXNew
                    .getFavourable().setScale(2).toString());
            BigDecimal goodsActualPrice = (boxGoodsXXXNew.getGoodsPrice())
                    .multiply(boxGoodsXXXNew.getGoodsDiscount())
                    .subtract(boxGoodsXXXNew.getFavourable())
                    .setScale(2, BigDecimal.ROUND_UP);
            boxGoodsXXXBeanJsonObject.put("goodsActualPrice",
                    goodsActualPrice.toString());
            // 对比出两个柜子中商品发生变化的货道
            BoxGoodsXXX boxGoodsXXXCurrent = boxGoodsXXXsCurrentList.get(j);
            // 相同货道，但是商品barCodeId不同，说明商品已经发生了变化
            if ((boxGoodsXXXCurrent.getCardgoRoadId() == boxGoodsXXXNew
                    .getCardgoRoadId())
                    && (!boxGoodsXXXCurrent.getBarCodeId().equals(
                            boxGoodsXXXNew.getBarCodeId()))) {
                boxGoodsXXXBeanJsonObject.put("cardgoRoadGoodsChanged", "1");
                cardgoRoadGoodsChangedNumber = cardgoRoadGoodsChangedNumber + 1;
                if (boxGoodsXXXCurrent.getStockNumber() > 0) {
                    cardgoRoadGoodsStockFlag = "1";
                    cardgoRoadGoodsStockJsonArrayBeanJsonObject.put(
                            "cardgoRoad", boxGoodsXXXCurrent.getCardgoRoadId());
                    cardgoRoadGoodsStockJsonArray
                            .add(cardgoRoadGoodsStockJsonArrayBeanJsonObject);
                }
                // 货道相同，但商品价格相关不同，说明商品价格发生了变化
            } else if ((boxGoodsXXXCurrent.getCardgoRoadId() == boxGoodsXXXNew
                    .getCardgoRoadId())
                    && (!(boxGoodsXXXCurrent.getGoodsPrice().toString()
                            .equals(boxGoodsXXXNew.getGoodsPrice().toString())
                            && boxGoodsXXXCurrent
                                    .getGoodsDiscount()
                                    .toString()
                                    .equals(boxGoodsXXXNew.getGoodsDiscount()
                                            .toString()) && boxGoodsXXXCurrent
                            .getFavourable().toString()
                            .equals(boxGoodsXXXNew.getFavourable().toString())))) {
                boxGoodsXXXBeanJsonObject.put("cardgoRoadGoodsChanged", "2");
                cardgoRoadGoodsChangedNumber = cardgoRoadGoodsChangedNumber + 1;
            } else {
                boxGoodsXXXBeanJsonObject.put("cardgoRoadGoodsChanged", "0");
            }

            JSONObject boxGoodsXXXObjectJsonObject = new JSONObject();
            boxGoodsXXXObjectJsonObject.put("BoxGoods",
                    boxGoodsXXXBeanJsonObject);
            boxGoodsXXXNewJsonArray.add(boxGoodsXXXObjectJsonObject);
        }

        currentBoxCardgoRoadGoodsStockBeforeUpdateJsonObject.put(
                "cardgoRoadGoodsStockFlag", cardgoRoadGoodsStockFlag);
        currentBoxCardgoRoadGoodsStockBeforeUpdateJsonObject.put(
                "cardgoRoadGoodsStockJsonArray", cardgoRoadGoodsStockJsonArray);

        currentAndNewBoxGoodsBeanObject.put("BoxGoodssCurrent",
                boxGoodsXXXCurrentJsonArray);
        currentAndNewBoxGoodsBeanObject.put("BoxGoodssNew",
                boxGoodsXXXNewJsonArray);
        currentAndNewBoxGoodsBeanObject.put("CardgoRoadGoodsChangedNumber",
                cardgoRoadGoodsChangedNumber);
        // 商品结构变化的商品是否还有库存
        currentAndNewBoxGoodsBeanObject.put(
                "CurrentBoxCardgoRoadGoodsStockBeforeUpdate",
                currentBoxCardgoRoadGoodsStockBeforeUpdateJsonObject);

        // 获取售货机层数和每层货道数信息
        BoxesService boxesService = new BoxesService();
        SerializeBoxBodyService serializeBoxBodyService = new SerializeBoxBodyService();
        BoxStructureService boxStructureService = new BoxStructureService();
        Boxes boxes = boxesService.findBoxesByBoxId(boxId);
        SerializeBoxBody serializeBoxBody = serializeBoxBodyService
                .findSerializeBoxBodyBySerializeBoxBodyId(boxes
                        .getSerializeBoxBodyId());
        currentAndNewBoxGoodsBeanObject.put("LayerAndCardgoRoadInfo",
                boxStructureService
                        .getCardgoRoadAndLayerStructureInfo(serializeBoxBody
                                .getStructureId()));

        JSONObject currentAndNewBoxGoodsObject = new JSONObject();
        currentAndNewBoxGoodsObject.put("CurrentAndNewBoxGoodss",
                currentAndNewBoxGoodsBeanObject);
        return currentAndNewBoxGoodsObject.toString();
    }

    // 针对称重柜，更新货柜货道商品结构
    public String doUpdateBoxCardgoRoadGoods(String boxId) {
        // 若果货道商品不变则不更新原有商品库存，如果货道商品变化则包括商品库存在内全部更新
        BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
        List<BoxGoodsXXX> boxGoodsXXXsList = boxGoodsXXXService
                .findAllBoxGoodsXXX(boxId);
        BoxGoodsXXXNewService boxGoodsXXXNewService = new BoxGoodsXXXNewService();
        List<BoxGoodsXXXNew> boxGoodsXXXNewsList = boxGoodsXXXNewService
                .findAllBoxGoodsXXXNew(boxId);
        for (int i = 0; i < boxGoodsXXXNewsList.size(); i++) {
            BoxGoodsXXXNew boxGoodsXXXNew = boxGoodsXXXNewsList.get(i);
            BoxGoodsXXX boxGoodsXXX = boxGoodsXXXsList.get(i);
            if (boxGoodsXXXNew.getBarCodeId()
                    .equals(boxGoodsXXX.getBarCodeId())) {
                // 当货道商品不变时保持原来商品库存
                boxGoodsXXXNew.setStockNumber(boxGoodsXXX.getStockNumber());
            } else {
                // 当货道商品变化时新商品库存变为0
                boxGoodsXXXNew.setStockNumber(0);
            }
            JSONObject boxGoodsXXXNewJsonObject = JSONObject
                    .fromObject(boxGoodsXXXNew);
            BoxGoodsXXX boxGoodsXXXFromJSonBean = (BoxGoodsXXX) JSONObject
                    .toBean(boxGoodsXXXNewJsonObject, BoxGoodsXXX.class);
            if (!updateBoxGoodsXXXByCardgoRoadId(boxId, boxGoodsXXXFromJSonBean)) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("response", ResponseStatus.FAIL);
                return jsonObject.toString();
            }
        }
        // 向客户端返回更新后的数据
        return doGetCurrentAndNewBoxGoods(boxId);
    }

    /**
     * 通知工控机货道商品价格已更新，工控机进行商品价格同步
     */
    public void notifyAndroidUpdateCurrentBoxGoodsPrice(String boxIdsStr) {
        try {
            List<String> boxIdsList = Arrays.asList(boxIdsStr.split(","));
            for (int i = 0; i < boxIdsList.size(); i++) {
                String boxId = boxIdsList.get(i);
                Session currentSession = SessionMapFactory.getInstance()
                        .getCurrentSession(
                                ResponseStatus.CUSTOM_CATEGORY_ANDROID, boxId);
                if (currentSession != null) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("response",
                            ResponseStatus.UPDATE_CURRENT_BOXGOODS_PRICE);
                    SessionMapFactory.getInstance().sendMessage(currentSession,
                            jsonObject.toString());
                }
            }
        } catch (IOException e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    // 更新售货机数据库商品价格
    public String doUpdateCurrentBoxGoodsPrice(String boxId) {
        BoxGoodsXXXNewService boxGoodsXXXNewService = new BoxGoodsXXXNewService();
        List<BoxGoodsXXXNew> boxGoodsXXXNewsList = boxGoodsXXXNewService
                .findAllBoxGoodsXXXNew(boxId);
        // 对比当前售货机数据库和已更新价格售货机数据库，并将当前售货机数据库的商品价格更新为已更新价格的数据库中的商品价格
        for (int i = 0; i < boxGoodsXXXNewsList.size(); i++) {
            BoxGoodsXXXNew boxGoodsXXXNew = boxGoodsXXXNewsList.get(i);
            JSONObject boxGoodsXXXNewJsonObject = JSONObject
                    .fromObject(boxGoodsXXXNew);
            BoxGoodsXXX boxGoodsXXXFromJSonBean = (BoxGoodsXXX) JSONObject
                    .toBean(boxGoodsXXXNewJsonObject, BoxGoodsXXX.class);
            if (!updateBoxGoodsXXXPriceByCardgoRoadId(boxId,
                    boxGoodsXXXFromJSonBean)) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("response", ResponseStatus.FAIL);
                return jsonObject.toString();
            }
        }
        // 向客户端返回货道商品价格更新成功
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", ResponseStatus.SUCCESS);
        return jsonObject.toString();
    }

    // 检查后台商品信息是否已发生变化
    public boolean checkBoxGoodsChanged(String boxId) {
        boolean isChanged = false;
        BoxGoodsXXXNewService boxGoodsXXXNewService = new BoxGoodsXXXNewService();
        List<BoxGoodsXXX> boxGoodsXXXsCurrentList = findAllBoxGoodsXXX(boxId);
        List<BoxGoodsXXXNew> boxGoodsXXXNewsList = boxGoodsXXXNewService
                .findAllBoxGoodsXXXNew(boxId);
        if (boxGoodsXXXsCurrentList.size() != boxGoodsXXXNewsList.size()) {
            isChanged = true;
            return isChanged;
        }
        if (boxGoodsXXXNewsList.size() > 0) {
            for (int i = 0; i < boxGoodsXXXNewsList.size(); i++) {
                BoxGoodsXXXNew boxGoodsXXXNew = boxGoodsXXXNewsList.get(i);
                // 对比出两个柜子中商品发生变化的货道
                BoxGoodsXXX boxGoodsXXXCurrent = boxGoodsXXXsCurrentList.get(i);
                // 相同货道，但是商品barCodeId不同，说明商品已经发生了变化或者货道相同，价格发生变化
                if (((boxGoodsXXXCurrent.getCardgoRoadId() == boxGoodsXXXNew
                        .getCardgoRoadId()) && (!boxGoodsXXXCurrent
                        .getBarCodeId().equals(boxGoodsXXXNew.getBarCodeId())))
                        || (boxGoodsXXXCurrent.getCardgoRoadId() == boxGoodsXXXNew
                                .getCardgoRoadId())
                        && (!(boxGoodsXXXCurrent
                                .getGoodsPrice()
                                .toString()
                                .equals(boxGoodsXXXNew.getGoodsPrice()
                                        .toString())
                                && boxGoodsXXXCurrent
                                        .getGoodsDiscount()
                                        .toString()
                                        .equals(boxGoodsXXXNew
                                                .getGoodsDiscount().toString()) && boxGoodsXXXCurrent
                                .getFavourable()
                                .toString()
                                .equals(boxGoodsXXXNew.getFavourable()
                                        .toString())))) {
                    isChanged = true;
                    break;
                }
            }
        } else {
            isChanged = false;
        }
        return isChanged;
    }

    /**
     * 通知Android主控更新售货机货道商品
     * 
     * @param customCategory
     * @param boxId
     */
    public void notifyAndroidUpdateCurrentBoxGoods(String customCategory,
            String boxId) throws Exception {
        Session currentSession = SessionMapFactory.getInstance()
                .getCurrentSession(customCategory, boxId);
        if (currentSession != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("response", ResponseStatus.UPDATE_CURRENT_BOXGOODS);
            SessionMapFactory.getInstance().sendMessage(currentSession,
                    jsonObject.toString());
        }
    }

    // 更新单个货道库存，以指挥Android主控进行去皮操作
    public String doNotifyAndroidUpdateCardgoRoadGoodsStockNumber(
            String currentReplenishId, String boxId, String cardgoRoadId,
            String barCodeId, String updateStockNumber) throws Exception {
        Session currentSession = SessionMapFactory.getInstance()
                .getCurrentSession(ResponseStatus.CUSTOM_CATEGORY_ANDROID,
                        boxId);
        if (currentSession != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("response",
                    ResponseStatus.UPDATE_CARDGOROAD_GOODS_STOCKNUMBER);
            JSONObject updateBeanJsonObject = new JSONObject();
            if (currentReplenishId != null) {
                updateBeanJsonObject.put("currentReplenishId",
                        currentReplenishId);
            }
            updateBeanJsonObject.put("boxId", boxId);
            updateBeanJsonObject.put("cardgoRoadId", cardgoRoadId);
            updateBeanJsonObject.put("barCodeId", barCodeId);
            updateBeanJsonObject.put("updateStockNumber", updateStockNumber);
            jsonObject.put("UpdateCardgoRoadGoodsStockNumberInfo",
                    updateBeanJsonObject);
            SessionMapFactory.getInstance().sendMessage(currentSession,
                    jsonObject.toString());
            return ResponseStatus.SUCCESS;
        } else {
            return ResponseStatus.FAIL;
        }
    }

    // 就收到Android更新结果以后，开始更新本地数据库，并把更新结果反馈给客户端
    public String doUpdateCardgoRoadGoodsStockNumber(String boxId,
            String cardgoRoadId, String barCodeId, String updateStockNumber)
            throws Exception {
        JSONObject jsonObject = new JSONObject();
        if (updateBoxGoodsByBarCodeIdAndCardgoRoadIdAndStockNumber(boxId,
                barCodeId, Integer.valueOf(cardgoRoadId),
                Integer.valueOf(updateStockNumber))) {
            jsonObject.put("response", ResponseStatus.SUCCESS);
        } else {
            jsonObject.put("response", ResponseStatus.FAIL);
        }
        return jsonObject.toString();
    }

    /**
     * Android主控对上下架商品不做处理，由服务器通过allGoodsJsonObject和数据库库存商品做对比，提取出商品上下架信息
     */
    public JSONArray getAddedAndRemovedGoodsJsonObject(String boxId,
            JSONObject allGoodsJsonObject) {
        JSONArray addedAndRemoveGoodsJsonArray = new JSONArray();
        JSONObject addedGoodsJsonObject = new JSONObject();
        JSONObject removedGoodsJsonObject = new JSONObject();
        JSONArray addedGoodsJsonArray = new JSONArray();
        JSONArray removedGoodsJsonArray = new JSONArray();
        List<BoxGoodsXXX> currentBoxGoodsXXXList = findAllBoxGoodsXXX(boxId);

        if (allGoodsJsonObject.containsValue("noGoodsObject")) {
            // 如果主控传递过来的AllGoods为noGoodsObject，则认为当前售货机所有货道商品均下架，当前售货机库存商品全部加入到removedGoodsJsonArray,并且将
            addedGoodsJsonObject.put("AddedGoodsObjects", "noGoodsObject");
            for (int i = 0; i < currentBoxGoodsXXXList.size(); i++) {
                BoxGoodsXXX currentBoxGoodsXXX = currentBoxGoodsXXXList.get(i);
                if (currentBoxGoodsXXX.getStockNumber() != 0) {
                    JSONObject removedGoodsBeanJsonObject = new JSONObject();
                    removedGoodsBeanJsonObject.put("barCodeId",
                            currentBoxGoodsXXX.getBarCodeId());
                    removedGoodsBeanJsonObject.put("cardgoRoadId",
                            currentBoxGoodsXXX.getCardgoRoadId());
                    removedGoodsBeanJsonObject.put("goodsNumber",
                            currentBoxGoodsXXX.getStockNumber());
                    removedGoodsJsonArray.add(removedGoodsBeanJsonObject);
                }
            }
            if (removedGoodsJsonArray.size() == 0) {
                removedGoodsJsonObject.put("RemovedGoodsObjects",
                        "noGoodsObject");
            } else {
                removedGoodsJsonObject.put("RemovedGoodsObjects",
                        removedGoodsJsonArray);
            }
            addedAndRemoveGoodsJsonArray.add(addedGoodsJsonObject);
            addedAndRemoveGoodsJsonArray.add(removedGoodsJsonObject);
        } else {
            // 若果allGoodsJsonObject不=noGoodsObject，则需要和当前售货机货道商品做对比，比较出每个货道上下架商品情况
            List<JSONObject> allGoodsJsonListFromAndroid = JsonParseUtil
                    .parseJsonObjectToObjectList(allGoodsJsonObject, "AllGoods");
            // 因为Android主控对于货道商品为0的数据未封装传递，在此处做一个0填充，用来吻合数据库查出来的数据货道商品结构，方便进行上下架对比
            List<JSONObject> newAllGoodsJsonListFromAndroid = new ArrayList<JSONObject>();
            for (int i = 0; i < currentBoxGoodsXXXList.size(); i++) {
                BoxGoodsXXX currentBoxGoodsXXX = currentBoxGoodsXXXList.get(i);
                JSONObject nullStockGoodsJsonObject = new JSONObject();
                nullStockGoodsJsonObject.put("cardgoRoadId",
                        currentBoxGoodsXXX.getCardgoRoadId());
                nullStockGoodsJsonObject.put("barCodeId",
                        currentBoxGoodsXXX.getBarCodeId());
                nullStockGoodsJsonObject.put("goodsNumber", "0");
                newAllGoodsJsonListFromAndroid.add(nullStockGoodsJsonObject);
            }

            for (int j = 0; j < allGoodsJsonListFromAndroid.size(); j++) {
                JSONObject weightBoxGoodsUpdateJsonObject = allGoodsJsonListFromAndroid
                        .get(j);
                int cardgoRoadId = Integer
                        .valueOf(weightBoxGoodsUpdateJsonObject
                                .getString("cardgoRoadId"));
                newAllGoodsJsonListFromAndroid.set(cardgoRoadId,
                        weightBoxGoodsUpdateJsonObject);
            }

            for (int i = 0; i < currentBoxGoodsXXXList.size(); i++) {
                BoxGoodsXXX currentBoxGoodsXXX = currentBoxGoodsXXXList.get(i);
                for (int j = 0; j < newAllGoodsJsonListFromAndroid.size(); j++) {
                    JSONObject weightBoxGoodsUpdateJsonObject = newAllGoodsJsonListFromAndroid
                            .get(j);
                    if (currentBoxGoodsXXX.getCardgoRoadId() == Integer
                            .valueOf(weightBoxGoodsUpdateJsonObject
                                    .getString("cardgoRoadId"))
                            && currentBoxGoodsXXX.getBarCodeId().equals(
                                    weightBoxGoodsUpdateJsonObject
                                            .getString("barCodeId"))) {
                        int currentBoxCardgoRoadStockNumber = currentBoxGoodsXXX
                                .getStockNumber();
                        int fromAndroidCardgoRoadStockNumber = Integer
                                .valueOf(weightBoxGoodsUpdateJsonObject
                                        .getString("goodsNumber"));

                        if (currentBoxCardgoRoadStockNumber < fromAndroidCardgoRoadStockNumber) {
                            // 该货道有商品上货
                            JSONObject addedGoodsBeanJsonObject = new JSONObject();
                            addedGoodsBeanJsonObject.put("barCodeId",
                                    currentBoxGoodsXXX.getBarCodeId());
                            addedGoodsBeanJsonObject.put("cardgoRoadId",
                                    currentBoxGoodsXXX.getCardgoRoadId());
                            addedGoodsBeanJsonObject.put("goodsNumber",
                                    fromAndroidCardgoRoadStockNumber
                                            - currentBoxCardgoRoadStockNumber);
                            addedGoodsJsonArray.add(addedGoodsBeanJsonObject);
                        } else if (currentBoxCardgoRoadStockNumber > fromAndroidCardgoRoadStockNumber) {
                            // 该货道有商品下架
                            JSONObject removedGoodsBeanJsonObject = new JSONObject();
                            removedGoodsBeanJsonObject.put("barCodeId",
                                    currentBoxGoodsXXX.getBarCodeId());
                            removedGoodsBeanJsonObject.put("cardgoRoadId",
                                    currentBoxGoodsXXX.getCardgoRoadId());
                            removedGoodsBeanJsonObject.put("goodsNumber",
                                    currentBoxCardgoRoadStockNumber
                                            - fromAndroidCardgoRoadStockNumber);
                            removedGoodsJsonArray
                                    .add(removedGoodsBeanJsonObject);
                        }
                        break;
                    }
                }
            }

            if (addedGoodsJsonArray.size() == 0) {
                // 若果addedGoodsJsonArray.size==0，说明无商品上架
                addedGoodsJsonObject.put("AddedGoodsObjects", "noGoodsObject");
            } else {
                addedGoodsJsonObject.put("AddedGoodsObjects",
                        addedGoodsJsonArray);
            }
            if (removedGoodsJsonArray.size() == 0) {
                // 若果addedGoodsJsonArray.size==0，说明无商品下架
                removedGoodsJsonObject.put("RemovedGoodsObjects",
                        "noGoodsObject");
            } else {
                removedGoodsJsonObject.put("RemovedGoodsObjects",
                        removedGoodsJsonArray);
            }

            addedAndRemoveGoodsJsonArray.add(addedGoodsJsonObject);
            addedAndRemoveGoodsJsonArray.add(removedGoodsJsonObject);
        }
        return addedAndRemoveGoodsJsonArray;
    }

    /**
     * 接收公共机盘点完成的所售卖的商品信息，生成购物订单
     * 
     * @param boxId
     * @param saledGoodsInfo
     * @return
     */
    public String doSaledGoodsInfo(String orderType, String boxId,
            String phoneNumber, String openDoorRequestSerialNumber,
            String saledGoodsInfo) {
        String saledResult = ResponseStatus.FAIL;
        try {
            log.info("BoxGoodsXXXService->doSaledGoodsInfo: boxId=" + boxId
                    + ",saledGoodsInfo=" + saledGoodsInfo);
            JSONObject jsonObject = JSONObject.fromObject(saledGoodsInfo);
            if (jsonObject.containsKey("SaledGoodsObjects")) {
                if (jsonObject.containsValue("noGoodsObject")) {
                    saledResult = notifyWXNoGoodsSaled(orderType,
                            ResponseStatus.CUSTOM_CATEGORY_WX, boxId,
                            phoneNumber, openDoorRequestSerialNumber);
                } else {
                    List<JSONObject> goodsBarCodeIdsJsonList = JsonParseUtil
                            .parseJsonObjectToObjectList(jsonObject,
                                    "SaledGoodsObjects");
                    saledResult = notifyWXWhatGoodsSaled(orderType,
                            ResponseStatus.CUSTOM_CATEGORY_WX, boxId,
                            phoneNumber, openDoorRequestSerialNumber,
                            ResponseStatus.BOX_TYPE_WEIGHT,
                            goodsBarCodeIdsJsonList, true);
                }
            }
            return saledResult;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        return saledResult;
    }

    /**
     * 视频客户端发来修改本次购物订单的购物信息，服务器解析生成新订单
     * 
     * @param orderId
     * @param boxId
     * @param saledGoodsInfo
     * @return
     */
    public String doUpdateSaledGoodsInfo(String oldOrderId, String boxId,
            String saledGoodsInfo) {
        // 根据订单ID查询出出力本次修改订单的owner
        ShoppingInfoService shoppingInfoService = new ShoppingInfoService();
        ShoppingInfo shoppingInfo = shoppingInfoService
                .findShoppingInfoByOrderId(oldOrderId);
        String resolverOwner = shoppingInfo.getResolveUser();
        // 根据boxId获取当前boxId对应商户的合作模式
        CustomWebSocketService customWebSocketService = new CustomWebSocketService();
        int cooperationMode = customWebSocketService.getCooperationMode(boxId);
        OrderService orderService = new OrderService();
        Order oldOrder = orderService.findOrderByOrderId(oldOrderId);
        CurrentUser currentUser = new CurrentUser();
        // 如果商户智购猫自营
        if (cooperationMode == ResponseStatus.COOPERATIONMODE_SELF) {
            String phoneNumber = oldOrder.getPhoneNumber();
            UserService userService = new UserService();
            User user = userService.queryUserByPhoneNumber(phoneNumber);
            currentUser.setPhoneNumber(phoneNumber);
            currentUser.setBoxId(boxId);
            currentUser.setVipLevel(oldOrder.getVipLevel());
            currentUser.setAgreementNO(user.getWxId());
            currentUser.setCustomType(ResponseStatus.CUSTOM_CATEGORY_WX);
        }

        String saledResult = ResponseStatus.FAIL;
        try {
            log.info("BoxGoodsXXXService->doUpdateSaledGoodsInfo: boxId="
                    + boxId + ",saledGoodsInfo=" + saledGoodsInfo);
            JSONObject jsonObject = JSONObject.fromObject(saledGoodsInfo);
            if (jsonObject.containsKey("SaledGoodsObjects")) {
                if (jsonObject.containsValue("noGoodsObject")) {
                    saledResult = notifyWXUpdateNoGoodsSaled(oldOrderId,
                            oldOrder, resolverOwner, currentUser,
                            ResponseStatus.CUSTOM_CATEGORY_WX, boxId);
                } else {
                    List<JSONObject> goodsBarCodeIdsJsonList = JsonParseUtil
                            .parseJsonObjectToObjectList(jsonObject,
                                    "SaledGoodsObjects");
                    saledResult = notifyWXWhatUpdateGoodsSaled(oldOrderId,
                            oldOrder, resolverOwner, currentUser,
                            ResponseStatus.CUSTOM_CATEGORY_WX, boxId,
                            ResponseStatus.BOX_TYPE_WEIGHT,
                            goodsBarCodeIdsJsonList);
                }
            }
            return saledResult;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        return saledResult;
    }

    /**
     * 接收公控机盘点完成的商品理货信息，生成理货订单
     * 
     * @param boxId
     * @param updatedGoodsInfo
     * @return
     */
    public String doUpdatedGoodsInfo(String boxId, String updatedGoodsInfo) {
        try {
            JSONObject jsonObject = JSONObject.fromObject(updatedGoodsInfo);
            if (jsonObject.containsKey("UpdatedGoodsObjects")) {
                JSONObject addedGoodsJsonObject = jsonObject.getJSONArray(
                        "UpdatedGoodsObjects").getJSONObject(0);
                JSONObject removedGoodsJsonObject = jsonObject.getJSONArray(
                        "UpdatedGoodsObjects").getJSONObject(1);
                JSONObject allGoodsJsonObject = jsonObject.getJSONArray(
                        "UpdatedGoodsObjects").getJSONObject(2);
                log.info("BoxGoodsXXXService->doUpdatedGoodsInfo UpdatedGoodsObjects:"
                        + updatedGoodsInfo
                        + ", boxId="
                        + boxId
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

                if (addedGoodsJsonObject.containsValue("noGoodsObject")
                        && removedGoodsJsonObject
                                .containsValue("noGoodsObject")) {
                    // 无商品上货
                    notifyWXNoGoodsUpdated(ResponseStatus.CUSTOM_CATEGORY_WX,
                            boxId, ResponseStatus.BOX_TYPE_WEIGHT,
                            allGoodsJsonObject);
                } else if (!(addedGoodsJsonObject
                        .containsValue("noGoodsObject"))
                        && removedGoodsJsonObject
                                .containsValue("noGoodsObject")) {
                    // 有商品上货，无商品拿出
                    notifyWXWhatGoodsUpdated(ResponseStatus.CUSTOM_CATEGORY_WX,
                            boxId, ResponseStatus.BOX_TYPE_WEIGHT,
                            addedGoodsJsonObject, true, removedGoodsJsonObject,
                            false, allGoodsJsonObject);

                } else if (addedGoodsJsonObject.containsValue("noGoodsObject")
                        && !(removedGoodsJsonObject
                                .containsValue("noGoodsObject"))) {
                    // 无商品上货，有商品拿出
                    notifyWXWhatGoodsUpdated(ResponseStatus.CUSTOM_CATEGORY_WX,
                            boxId, ResponseStatus.BOX_TYPE_WEIGHT,
                            addedGoodsJsonObject, false,
                            removedGoodsJsonObject, true, allGoodsJsonObject);
                } else if (!(addedGoodsJsonObject
                        .containsValue("noGoodsObject"))
                        && !(removedGoodsJsonObject
                                .containsValue("noGoodsObject"))) {
                    // 既有商品上货，又有商品拿出
                    notifyWXWhatGoodsUpdated(ResponseStatus.CUSTOM_CATEGORY_WX,
                            boxId, ResponseStatus.BOX_TYPE_WEIGHT,
                            addedGoodsJsonObject, true, removedGoodsJsonObject,
                            true, allGoodsJsonObject);
                }
            }
            return ResponseStatus.SUCCESS;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        return ResponseStatus.FAIL;
    }

    public String doSyncBoxStockInfo(String boxId, String stockInfo) {
        // 首先将售货机是否已同步库存状态设置为未同步
        BoxStockSyncStatusService boxStockSyncStatusService = new BoxStockSyncStatusService();
        BoxStockSyncStatus boxStockSyncStatus = new BoxStockSyncStatus();
        boxStockSyncStatus.setBoxId(boxId);
        boxStockSyncStatus.setState(ResponseStatus.SYNC_BOX_STOCK_ONGOING);
        BoxStockSyncStatus boxStockSyncStatusQuery = boxStockSyncStatusService
                .findBoxStockSyncStatusByBoxId(boxId);
        if (boxStockSyncStatusQuery != null) {
            boxStockSyncStatusService
                    .updateBoxStockSyncStatus(boxStockSyncStatus);
        } else {
            boxStockSyncStatusService.addBoxStockSyncStatus(boxStockSyncStatus);
        }
        // 同步库存之前先对该柜子是否还有未处理异常和柜子是否处理服务中状态进行判断,如果是这两种状态则返回fail，暂时无法同步
        // 是否有未处理异常视频
        ShoppingInfoService shoppingInfoService = new ShoppingInfoService();
        List<ShoppingInfo> shoppingInfosList = shoppingInfoService
                .findShoppInfoByBoxIdAndState(boxId, 0);
        if (shoppingInfosList.size() > 0) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("response", ResponseStatus.FAIL);
            jsonObject
                    .put("errorCode",
                            ResponseStatus.SYNC_STOCKINFO_FAIL_EXIST_UNHANDLE_EXCEPTION_CODE_444);
            jsonObject
                    .put("errorMsg",
                            ResponseStatus.SYNC_STOCKINFO_FAIL_EXIST_UNHANDLE_EXCEPTION_MSG_444);
            return jsonObject.toString();
        }
        // 售货机当前状态
        BoxStatusService boxStatusService = new BoxStatusService();
        BoxStatus boxStatus = boxStatusService.findBoxStatusByBoxId(boxId);
        int boxState = ResponseStatus.BOX_STATUS_NORMAL;
        if (boxStatus != null) {
            boxState = boxStatus.getState();
        }
        if (boxState == ResponseStatus.BOX_STATUS_BUSY) {
            // 如果服务中同步失败
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("response", ResponseStatus.FAIL);
            jsonObject.put("errorCode",
                    ResponseStatus.SYNC_STOCKINFO_FAIL_BOX_BUSY_CODE_445);
            jsonObject.put("errorMsg",
                    ResponseStatus.SYNC_STOCKINFO_FAIL_BOX_BUSY_MSG_445);
            return jsonObject.toString();
        }
        // 将信息通知给主控工控机进行库存同步
        if (stockInfo != null) {
            JSONObject jsonObjectToAndroid = new JSONObject();
            jsonObjectToAndroid.put("response", "syncBoxStockInfo");
            jsonObjectToAndroid.put("stockInfo", stockInfo);
            Session session = SessionMapFactory.getInstance()
                    .getCurrentSession(ResponseStatus.CUSTOM_CATEGORY_ANDROID,
                            boxId);
            try {
                SessionMapFactory.getInstance().sendMessage(session,
                        jsonObjectToAndroid.toString());
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("response", ResponseStatus.SUCCESS);
                return jsonObject.toString();
            } catch (IOException e) {
                log.error("try->catch exception:", e);
                e.printStackTrace();
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("response", ResponseStatus.FAIL);
            jsonObject.put("errorCode",
                    ResponseStatus.SYNC_STOCKINFO_FAIL_UNKNOW_CODE_447);
            jsonObject.put("errorMsg",
                    ResponseStatus.SYNC_STOCKINFO_FAIL_UNKNOW_MSG_447);
            return jsonObject.toString();
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("response", ResponseStatus.FAIL);
            jsonObject.put("errorCode",
                    ResponseStatus.SYNC_STOCKINFO_FAIL_NULL_STOCKINFO_CODE_446);
            jsonObject.put("errorMsg",
                    ResponseStatus.SYNC_STOCKINFO_FAIL_NULL_STOCKINFO_MSG_446);
            return jsonObject.toString();
        }
    }

    public void notifyWXBoxBusy(String customCategory, String boxId) {
        Session session = SessionMapFactory.getInstance().getCurrentSession(
                customCategory, boxId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", ResponseStatus.BOX_ORDER_BUSY);
        try {
            SessionMapFactory.getInstance().sendMessage(session,
                    jsonObject.toString());
        } catch (IOException e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

}
