package com.chinapalms.kwobox.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.chinapalms.kwobox.dao.impl.OrderDAOImpl;
import com.chinapalms.kwobox.javabean.Boxes;
import com.chinapalms.kwobox.javabean.CurrentUser;
import com.chinapalms.kwobox.javabean.Order;
import com.chinapalms.kwobox.javabean.OrderDetails;
import com.chinapalms.kwobox.javabean.OrderDetailsModify;
import com.chinapalms.kwobox.javabean.OrderDetailsThirdPart;
import com.chinapalms.kwobox.javabean.OrderModify;
import com.chinapalms.kwobox.javabean.OrderThirdPart;
import com.chinapalms.kwobox.pay.wxpay.papay.WxPapayService;
import com.chinapalms.kwobox.utils.ResponseStatus;
import com.custom.kwobox.service.CustomWebSocketService;
import com.sun.org.apache.bcel.internal.generic.RETURN;

public class OrderService extends OrderDAOImpl {

    Log log = LogFactory.getLog(OrderService.class);

    @Override
    public boolean addOrder(Order order) {
        return super.addOrder(order);
    }

    @Override
    public Order findOrderByOrderId(String orderId) {
        return super.findOrderByOrderId(orderId);
    }

    @Override
    public List<Order> findOrdersByPhoneNumber(String phoneNumber) {
        return super.findOrdersByPhoneNumber(phoneNumber);
    }

    @Override
    public List<Order> findOrdersByPhoneNumberAndPayState(String phoneNumber,
            int payState) {
        return super.findOrdersByPhoneNumberAndPayState(phoneNumber, payState);
    }

    @Override
    public boolean updateOrder(Order order) {
        return super.updateOrder(order);
    }

    @Override
    public List<Order> findOrdersByPhoneNumberAndPayStestAndPageNumber(
            String phoneNumber, int payState, int pageNumber) {
        return super.findOrdersByPhoneNumberAndPayStestAndPageNumber(
                phoneNumber, payState, pageNumber);
    }

    @Override
    public int findOrderCountByPhoneNumberAndPayState(String phoneNumber,
            int payState) {
        return super.findOrderCountByPhoneNumberAndPayState(phoneNumber,
                payState);
    }

    @Override
    public List<Order> findTopNOrders(int topNOrderNumbers) {
        return super.findTopNOrders(topNOrderNumbers);
    }

    @Override
    public int findCurrentDayOrderCount() {
        return super.findCurrentDayOrderCount();
    }

    @Override
    public int findTotalOrderCount() {
        return super.findTotalOrderCount();
    }

    @Override
    public int findOrderCountByPhoneNumber(String phoneNumber) {
        return super.findOrderCountByPhoneNumber(phoneNumber);
    }

    @Override
    public Order findAllActualPayTotalAndAllTotalFavourableByPhoneNumber(
            String phoneNumber) {
        return super
                .findAllActualPayTotalAndAllTotalFavourableByPhoneNumber(phoneNumber);
    }

    @Override
    public boolean updateOrderInfo(Order order) {
        return super.updateOrderInfo(order);
    }

    // 订单编号由日期+6位随机数构成，共20位
    public String makeOrderId(Date orderDate) {
        Random random = new Random();
        String result = "";
        for (int i = 0; i < 6; i++) {
            result += random.nextInt(10);
        }
        return new SimpleDateFormat("yyyyMMddHHmmss").format(orderDate)
                + result;
    }

    /**
     * 生成无购物订单，并插入数据库
     */
    public boolean makeNoGoodsSaledOrder(String orderId, Date orderDate,
            String boxId) {
        CurrentUserService currentUserService = new CurrentUserService();
        CurrentUser currentUser = currentUserService
                .findCurrentUserByBoxId(boxId);
        Order order = new Order();
        order.setOrderId(orderId);
        if (currentUser != null) {
            order.setPhoneNumber(currentUser.getPhoneNumber());
            order.setVipLevel(currentUser.getVipLevel());
        }
        order.setBoxId(boxId);

        order.setGoodsTotalNumber(0);
        order.setPayTotal(BigDecimal.valueOf(Double.valueOf("0")));
        order.setTotalFavourable(BigDecimal.valueOf(Double.valueOf("0")));
        order.setActualPayTotal(BigDecimal.valueOf(Double.valueOf("0")));
        order.setPayType(ResponseStatus.CUSTOM_CATEGORY_WX);
        order.setThirdPayOrderId("WeChat0123456789");
        order.setOutTradeNo("0");
        order.setTransactionId("0");
        // payState=3暂时表示空账单无需支付
        order.setPayState(3);
        order.setPayDate(orderDate);
        order.setBuyDate(orderDate);
        // ===========对接商户服务器逻辑===========================================================================
        // 通过boxId查找商户ID，再根据商户ID查找回调Url，返回商户服务器，与商户服务器对接,如果无回调Url，则默认走智购猫平台逻辑
        CustomWebSocketService customWebSocketService = new CustomWebSocketService();
        String orderCallbackUrl = customWebSocketService
                .getCustomerOrderCallbackUrl(boxId);
        // 如果没有拿到对应商户的回调Url，则默认走智狗猫流程，否则回调商户服务器,若走商户服务器则不做扣除订单处理，由商户自行扣费
        if (orderCallbackUrl.equals(ResponseStatus.FAIL)) {
            addOrder(order);
        } else {
            // 回调商户服务器接口
            // 如果订单回调第三方接口，则插入平台数据库时将订单状态设置为已支付状态，避免需要在智购猫中进行重复支付
            OrderThirdPartService orderThirdPartService = new OrderThirdPartService();
            OrderThirdPart orderThirdPart = (OrderThirdPart) JSONObject.toBean(
                    JSONObject.fromObject(order), OrderThirdPart.class);
            orderThirdPartService.addOrderThirdPart(orderThirdPart);
        }
        return true;
    }

    /**
     * 更新订单时有中生无，生成空订单
     */
    public boolean makeUpdateNoGoodsSaledOrder(String oldOrderId,
            Order oldOrder, String orderId, Date orderDate, String boxId,
            String resolverOwner, CurrentUser currentUser) {
        Order order = new Order();
        order.setOrderId(orderId);
        if (currentUser != null) {
            order.setPhoneNumber(currentUser.getPhoneNumber());
            order.setVipLevel(currentUser.getVipLevel());
        }
        order.setBoxId(boxId);

        order.setGoodsTotalNumber(0);
        order.setPayTotal(BigDecimal.valueOf(Double.valueOf("0")));
        order.setTotalFavourable(BigDecimal.valueOf(Double.valueOf("0")));
        order.setActualPayTotal(BigDecimal.valueOf(Double.valueOf("0")));
        order.setPayType(ResponseStatus.CUSTOM_CATEGORY_WX);
        order.setThirdPayOrderId("WeChat0123456789");
        order.setOutTradeNo("0");
        order.setTransactionId("0");
        // payState=3暂时表示空账单无需支付
        order.setPayState(3);
        order.setPayDate(orderDate);
        order.setBuyDate(orderDate);
        // ===========对接商户服务器逻辑===========================================================================
        // 通过boxId查找商户ID，再根据商户ID查找回调Url，返回商户服务器，与商户服务器对接,如果无回调Url，则默认走智购猫平台逻辑
        CustomWebSocketService customWebSocketService = new CustomWebSocketService();
        String orderCallbackUrl = customWebSocketService
                .getCustomerOrderCallbackUrl(boxId);
        // 如果没有拿到对应商户的回调Url，则默认走智狗猫流程，否则回调商户服务器,若走商户服务器则不做扣除订单处理，由商户自行扣费
        if (orderCallbackUrl.equals(ResponseStatus.FAIL)) {
            // 退款成功&&保存无购物订单&&更新原来账单状态&&并把原来订单移至t_order_modify表中
            // 调用退款流程进行退款操作
            // 判断订单是否已支付并且用户非vip，如果订单已支付并且不是vip则执行退款，否则不再做图款操作
            boolean refundFlag = false;
            if (oldOrder.getPayState() == 3/* 暂时表示无购物订单逻辑 ,如果是空账单直接认为退款成功 */) {
                refundFlag = true;
            } else if (oldOrder.getPayState() == 1
                    && currentUser.getVipLevel() != 0) {
                WxPapayService wxPapayService = new WxPapayService();
                refundFlag = wxPapayService.doRefund(
                        oldOrder.getTransactionId(),
                        oldOrder.getActualPayTotal()
                                .multiply(BigDecimal.valueOf(100d)).intValue(),
                        oldOrder.getActualPayTotal()
                                .multiply(BigDecimal.valueOf(100d)).intValue());
            }
            // 重新生成订单并且把原始订单存入到t_order_modify中,并且把原始订单的payState设置为2（已退款）
            Order updatePayStateOrder = new Order();
            updatePayStateOrder.setOrderId(oldOrder.getOrderId());
            updatePayStateOrder.setPayState(2);
            if (refundFlag
                    && addOrder(order)
                    && updateOrder(updatePayStateOrder)
                    && addOldOrderToOrderModify(oldOrder, resolverOwner,
                            orderId, orderDate)) {
            }
            return true;
        } else {
            // 回调商户服务器接口
            // 如果订单回调第三方接口，则插入平台数据库时将订单状态设置为已支付状态，避免需要在智购猫中进行重复支付
            OrderThirdPartService orderThirdPartService = new OrderThirdPartService();
            OrderThirdPart orderThirdPart = (OrderThirdPart) JSONObject.toBean(
                    JSONObject.fromObject(order), OrderThirdPart.class);

            // 重新生成订单并且把原始订单存入到t_order_thirdpart_modify中,并且把原始订单的payState设置为2（已退款）
            OrderThirdPart updatePayStateOrder = new OrderThirdPart();
            updatePayStateOrder.setOrderId(oldOrderId);
            updatePayStateOrder.setPayState(2);
            if (orderThirdPartService.addOrderThirdPart(orderThirdPart)
                    && orderThirdPartService
                            .updateThirdPartOrder(updatePayStateOrder)
                    && orderThirdPartService
                            .addOldOrderThirdPartToOrderThirdPartModify(
                                    oldOrderId, resolverOwner, orderId,
                                    orderDate)) {
            }
        }
        return true;
    }

    /**
     * 生成订单
     * 
     * @param orderPropertiesJsonObject
     * @param orderDate
     * @param boxId
     * @param currentUser
     * @param addPersonCreditOrPointsRecord
     * @return
     * @throws Exception
     */
    public boolean makeOrder(JSONObject orderPropertiesJsonObject,
            Date orderDate, String boxId, CurrentUser currentUser,
            boolean addPersonCreditOrPointsRecord) throws Exception {
        Order order = new Order();
        JSONObject orderJsonObjectFromJsonObject = orderPropertiesJsonObject
                .getJSONObject("Order");
        String orderId = orderJsonObjectFromJsonObject.getString("orderId");
        order.setOrderId(orderId);

        if (currentUser != null) {
            order.setPhoneNumber(currentUser.getPhoneNumber());
            order.setVipLevel(currentUser.getVipLevel());
        }

        order.setBoxId(boxId);

        BigDecimal payTotal = BigDecimal.valueOf(Double
                .valueOf(orderJsonObjectFromJsonObject.getString("payTotal")));
        BigDecimal totalDiscount = BigDecimal.valueOf(Double
                .valueOf(orderJsonObjectFromJsonObject
                        .getString("totalFavourable")));
        BigDecimal actualPayTotal = BigDecimal.valueOf(Double
                .valueOf(orderJsonObjectFromJsonObject
                        .getString("actualPayTotal")));
        order.setPayTotal(payTotal);
        order.setTotalFavourable(totalDiscount);
        order.setActualPayTotal(actualPayTotal);
        order.setPayType(ResponseStatus.CUSTOM_CATEGORY_WX);
        order.setThirdPayOrderId("WeChat0123456789");
        order.setOutTradeNo("0");
        order.setTransactionId("0");
        order.setPayState(0);
        order.setPayDate(orderDate);

        order.setBuyDate(orderDate);

        int goodsTotalNumber = Integer.valueOf(orderJsonObjectFromJsonObject
                .getString("goodsTotalNumber"));
        order.setGoodsTotalNumber(goodsTotalNumber);

        log.info("OrderService:->make order ="
                + "orderJsonObjectFromJsonObject="
                + orderJsonObjectFromJsonObject.toString());
        // ===========对接商户服务器逻辑===========================================================================
        // 通过boxId查找商户ID，再根据商户ID查找回调Url，返回商户服务器，与商户服务器对接,如果无回调Url，则默认走智购猫平台逻辑
        CustomWebSocketService customWebSocketService = new CustomWebSocketService();
        String orderCallbackUrl = customWebSocketService
                .getCustomerOrderCallbackUrl(boxId);
        // 如果没有拿到对应商户的回调Url，则默认走智狗猫流程，否则回调商户服务器,若走商户服务器则不做扣除订单处理，由商户自行扣费
        if (orderCallbackUrl.equals(ResponseStatus.FAIL)) {
            if (addOrder(order) && addOrderDetailss(orderPropertiesJsonObject)) {
                // 更新用户积分
                UserService userService = new UserService();
                int pointsChangeValue = (int) (Double
                        .valueOf(orderJsonObjectFromJsonObject
                                .getString("actualPayTotal")).doubleValue());
                // 如果有小数，积分向前进一位
                if (pointsChangeValue < Double.valueOf(
                        orderJsonObjectFromJsonObject
                                .getString("actualPayTotal")).doubleValue()) {
                    pointsChangeValue = pointsChangeValue + 1;

                }
                // 当允许增加积分和智信分时.......
                if (addPersonCreditOrPointsRecord && pointsChangeValue > 0) {
                    userService.doUpdateUserPoints(
                            currentUser.getPhoneNumber(),
                            ResponseStatus.POINTS_ADD_REASON_NORMAL_SHOPPING,
                            pointsChangeValue);
                }
                // 更新用户未读订单
                userService.doupdateUserUnReadOrderNumber(
                        currentUser.getPhoneNumber(), 1);

                // 调用支付宝接口或者微信进行免密支付
                OrderService orderService = new OrderService();
                if (currentUser.getCustomType().equals(
                        ResponseStatus.CUSTOM_CATEGORY_ALIPAY)) {
                    if (currentUser.getVipLevel() == ResponseStatus.VIP_LEVEL_SVIP) {
                        order.setThirdPayOrderId("SVIPPayId");
                    } else {
                        doAlipay(currentUser.getAgreementNO(), order
                                .getActualPayTotal().toString());
                        order.setThirdPayOrderId("Alipay0123456789");
                    }
                    order.setPayType(ResponseStatus.CUSTOM_CATEGORY_ALIPAY);
                    order.setPayState(1);
                    orderService.updateOrder(order);
                    return true;
                } else if (currentUser.getCustomType().equals(
                        ResponseStatus.CUSTOM_CATEGORY_WX)) {
                    order.setPayType(ResponseStatus.CUSTOM_CATEGORY_WX);
                    if (currentUser.getVipLevel() == ResponseStatus.VIP_LEVEL_SVIP) {
                        order.setThirdPayOrderId("SVIPPayId");
                        order.setPayState(1);
                        orderService.updateOrder(order);
                        return true;
                    } else {
                        order.setThirdPayOrderId("WeChat0123456789");
                        WxPapayService wxPapayService = new WxPapayService();
                        int actualPay = (int) ((BigDecimal.valueOf(Double
                                .valueOf(order.getActualPayTotal().toString())))
                                .multiply(BigDecimal.valueOf(Double
                                        .valueOf(100)))).setScale(2,
                                BigDecimal.ROUND_DOWN).doubleValue();
                        // actualPay = 1;
                        if (wxPapayService.doPaypay(order.getOrderId(),
                                currentUser.getAgreementNO(), actualPay)) {
                            order.setPayState(1);
                            orderService.updateOrder(order);
                            return true;
                        } else {
                            order.setPayState(0);
                            orderService.updateOrder(order);
                            return false;
                        }
                    }
                }
            } else {
                return false;
            }
        } else {
            // 回调商户服务器接口
            // 如果订单回调第三方接口，则插入平台数据库时将订单状态设置为已支付状态，避免需要在智购猫中进行重复支付
            order.setPayState(1);
            OrderThirdPartService orderThirdPartService = new OrderThirdPartService();
            OrderDetailsThirdPartService orderDetailsThirdPartService = new OrderDetailsThirdPartService();
            OrderThirdPart orderThirdPart = (OrderThirdPart) JSONObject.toBean(
                    JSONObject.fromObject(order), OrderThirdPart.class);
            if (orderThirdPartService.addOrderThirdPart(orderThirdPart)
                    && orderDetailsThirdPartService
                            .addOrderDetailssThirdPart(orderPropertiesJsonObject)) {
                return true;
            }
        }
        // ============对接商户服务器逻辑==========================================================================
        return false;
    }

    /**
     * 生成更新订单、退款、更新订单状态逻辑
     * 
     * @param orderPropertiesJsonObject
     * @param orderDate
     * @param boxId
     * @param currentUser
     * @param addPersonCreditOrPointsRecord
     * @return
     * @throws Exception
     */
    public boolean makeUpdateOrder(String oldOrderId, Order oldOrder,
            String resolverOwner, JSONObject orderPropertiesJsonObject,
            Date orderDate, String boxId, CurrentUser currentUser)
            throws Exception {
        OrderService orderService = new OrderService();
        Order order = new Order();
        JSONObject orderJsonObjectFromJsonObject = orderPropertiesJsonObject
                .getJSONObject("Order");
        String orderId = orderJsonObjectFromJsonObject.getString("orderId");
        order.setOrderId(orderId);

        if (currentUser != null) {
            order.setPhoneNumber(currentUser.getPhoneNumber());
            order.setVipLevel(currentUser.getVipLevel());
        }

        order.setBoxId(boxId);

        BigDecimal payTotal = BigDecimal.valueOf(Double
                .valueOf(orderJsonObjectFromJsonObject.getString("payTotal")));
        BigDecimal totalDiscount = BigDecimal.valueOf(Double
                .valueOf(orderJsonObjectFromJsonObject
                        .getString("totalFavourable")));
        BigDecimal actualPayTotal = BigDecimal.valueOf(Double
                .valueOf(orderJsonObjectFromJsonObject
                        .getString("actualPayTotal")));
        order.setPayTotal(payTotal);
        order.setTotalFavourable(totalDiscount);
        order.setActualPayTotal(actualPayTotal);
        order.setPayType(ResponseStatus.CUSTOM_CATEGORY_WX);
        order.setThirdPayOrderId("WeChat0123456789");
        order.setOutTradeNo("0");
        order.setTransactionId("0");
        order.setPayState(0);
        order.setPayDate(orderDate);

        order.setBuyDate(orderDate);

        int goodsTotalNumber = Integer.valueOf(orderJsonObjectFromJsonObject
                .getString("goodsTotalNumber"));
        order.setGoodsTotalNumber(goodsTotalNumber);

        log.info("makeUpdateOrder:->make makeUpdateOrder ="
                + "orderJsonObjectFromJsonObject="
                + orderJsonObjectFromJsonObject.toString());
        // ===========对接商户服务器逻辑===========================================================================
        // 通过boxId查找商户ID，再根据商户ID查找回调Url，返回商户服务器，与商户服务器对接,如果无回调Url，则默认走智购猫平台逻辑
        CustomWebSocketService customWebSocketService = new CustomWebSocketService();
        String orderCallbackUrl = customWebSocketService
                .getCustomerOrderCallbackUrl(boxId);
        // 如果没有拿到对应商户的回调Url，则默认走智狗猫流程，否则回调商户服务器,若走商户服务器则不做扣除订单处理，由商户自行扣费
        if (orderCallbackUrl.equals(ResponseStatus.FAIL)) {
            // 调用退款流程进行退款操作
            // 判断订单是否已支付并且用户非vip，如果订单已支付并且不是vip则执行退款，否则不再做图款操作
            boolean refundFlag = false;
            if (oldOrder.getPayState() == 3/* 暂时表示无购物订单逻辑 ,如果是空账单直接认为退款成功 */) {
                refundFlag = true;
            } else if (oldOrder.getPayState() == 1
                    && currentUser.getVipLevel() != 0) {
                WxPapayService wxPapayService = new WxPapayService();
                refundFlag = wxPapayService.doRefund(
                        oldOrder.getTransactionId(),
                        oldOrder.getActualPayTotal()
                                .multiply(BigDecimal.valueOf(100d)).intValue(),
                        oldOrder.getActualPayTotal()
                                .multiply(BigDecimal.valueOf(100d)).intValue());
            }
            // 重新生成订单并且把原始订单存入到t_order_modify中,并且把原始订单的payState设置为2（已退款）
            Order updatePayStateOrder = new Order();
            updatePayStateOrder.setOrderId(oldOrder.getOrderId());
            updatePayStateOrder.setPayState(2);
            if (refundFlag
                    && addOrder(order)
                    && orderService.updateOrder(updatePayStateOrder)
                    && addOrderDetailss(orderPropertiesJsonObject)
                    && addOldOrderToOrderModify(oldOrder, resolverOwner,
                            orderId, orderDate)) {
                UserService userService = new UserService();
                // 更新用户未读订单
                userService.doupdateUserUnReadOrderNumber(
                        currentUser.getPhoneNumber(), 1);
                // 调用支付宝接口或者微信进行免密支付
                if (currentUser.getCustomType().equals(
                        ResponseStatus.CUSTOM_CATEGORY_ALIPAY)) {
                    if (currentUser.getVipLevel() == ResponseStatus.VIP_LEVEL_SVIP) {
                        order.setThirdPayOrderId("SVIPPayId");
                    } else {
                        doAlipay(currentUser.getAgreementNO(), order
                                .getActualPayTotal().toString());
                        order.setThirdPayOrderId("Alipay0123456789");
                    }
                    order.setPayType(ResponseStatus.CUSTOM_CATEGORY_ALIPAY);
                    order.setPayState(1);
                    orderService.updateOrder(order);
                    return true;
                } else if (currentUser.getCustomType().equals(
                        ResponseStatus.CUSTOM_CATEGORY_WX)) {
                    order.setPayType(ResponseStatus.CUSTOM_CATEGORY_WX);
                    if (currentUser.getVipLevel() == ResponseStatus.VIP_LEVEL_SVIP) {
                        order.setThirdPayOrderId("SVIPPayId");
                        order.setPayState(1);
                        orderService.updateOrder(order);
                        return true;
                    } else {
                        order.setThirdPayOrderId("WeChat0123456789");
                        WxPapayService wxPapayService = new WxPapayService();
                        int actualPay = (int) ((BigDecimal.valueOf(Double
                                .valueOf(order.getActualPayTotal().toString())))
                                .multiply(BigDecimal.valueOf(Double
                                        .valueOf(100)))).setScale(2,
                                BigDecimal.ROUND_DOWN).doubleValue();
                        // actualPay = 1;
                        if (wxPapayService.doPaypay(order.getOrderId(),
                                currentUser.getAgreementNO(), actualPay)) {
                            order.setPayState(1);
                            orderService.updateOrder(order);
                            return true;
                        } else {
                            order.setPayState(0);
                            orderService.updateOrder(order);
                            return false;
                        }
                    }
                }
            } else {
                return false;
            }
        } else {
            // 回调商户服务器接口
            // 如果订单回调第三方接口，则插入平台数据库时将订单状态设置为已支付状态，避免需要在智购猫中进行重复支付
            order.setPayState(1);
            OrderThirdPartService orderThirdPartService = new OrderThirdPartService();
            OrderDetailsThirdPartService orderDetailsThirdPartService = new OrderDetailsThirdPartService();
            OrderThirdPart orderThirdPart = (OrderThirdPart) JSONObject.toBean(
                    JSONObject.fromObject(order), OrderThirdPart.class);

            // 重新生成订单并且把原始订单存入到t_order_thirdpart_modify中,并且把原始订单的payState设置为2（已退款）
            OrderThirdPart updatePayStateOrder = new OrderThirdPart();
            updatePayStateOrder.setOrderId(oldOrderId);
            updatePayStateOrder.setPayState(2);
            if (orderThirdPartService.addOrderThirdPart(orderThirdPart)
                    && orderThirdPartService
                            .updateThirdPartOrder(updatePayStateOrder)
                    && orderDetailsThirdPartService
                            .addOrderDetailssThirdPart(orderPropertiesJsonObject)
                    && orderThirdPartService
                            .addOldOrderThirdPartToOrderThirdPartModify(
                                    oldOrderId, resolverOwner, orderId,
                                    orderDate)) {
                return true;
            }
        }
        // ============对接商户服务器逻辑==========================================================================
        return false;
    }

    private boolean addOrderDetailss(JSONObject orderPropertiesJsonObject) {
        OrderDetailsService orderDetailsService = new OrderDetailsService();
        if (orderDetailsService.addOrderDetailss(orderPropertiesJsonObject)) {
            return true;
        } else {
            return false;
        }
    }

    // 非分页查询订单，分页查询逻辑见如下
    public String doFindMyOrders(String phoneNumber, String orderType) {
        // 初始化用户未读订单，设置为0
        UserService userService = new UserService();
        userService.doupdateUserUnReadOrderNumber(phoneNumber, 0);
        List<Order> ordersList = findOrdersByPhoneNumberAndPayState(
                phoneNumber, orderType.equals("payedOrder") ? 1 : 0);
        BoxesService boxesService = new BoxesService();
        OrderDetailsService orderDetailsService = new OrderDetailsService();
        BigDecimal categoryGoodsWeight = new BigDecimal(0);
        if (ordersList.size() > 0) {
            JSONObject ordersJsonObject = new JSONObject();
            JSONArray ordersJsonArray = new JSONArray();
            JSONObject orderJsonObject = new JSONObject();
            for (int i = 0; i < ordersList.size(); i++) {
                Order order = ordersList.get(i);
                List<OrderDetails> orderDetailsList = orderDetailsService
                        .findOrderDetailsByOrderId(order.getOrderId());
                JSONObject orderPropertiesJsonObject = new JSONObject();
                JSONArray goodsJsonArray = new JSONArray();

                for (int j = 0; j < orderDetailsList.size(); j++) {
                    OrderDetails orderDetails = orderDetailsList.get(j);
                    JSONObject goodsJsonObject = new JSONObject();
                    goodsJsonObject.put("goodsName",
                            orderDetails.getGoodsName());
                    goodsJsonObject.put("salesMode",
                            orderDetails.getSalesMode());
                    goodsJsonObject.put("goodsPrice", orderDetails
                            .getGoodsPrice().setScale(2).toString());
                    goodsJsonObject.put("goodsFavorablePrice", orderDetails
                            .getGoodsFavorablePrice().setScale(2).toString());
                    goodsJsonObject.put("goodsActualPrice", orderDetails
                            .getGoodsActualPrice().setScale(2).toString());
                    goodsJsonObject.put("categoryGoodsNumber",
                            orderDetails.getCategoryGoodsNumber());
                    // 将类别商品重量由g转化为kg,并保留3位有效数字
                    categoryGoodsWeight = BigDecimal.valueOf(
                            Double.valueOf(Double.valueOf(orderDetails
                                    .getCategoryGoodsWeight()) / 1000))
                            .setScale(3, BigDecimal.ROUND_UP);
                    goodsJsonObject.put("categoryGoodsWeight",
                            categoryGoodsWeight);
                    goodsJsonObject.put("categoryGoodsPrice", orderDetails
                            .getCategoryGoodsPrice().setScale(2).toString());
                    goodsJsonObject
                            .put("categoryFavorablePrice", orderDetails
                                    .getCategoryFavorablePrice().setScale(2)
                                    .toString());
                    goodsJsonObject.put("categoryActualPrice", orderDetails
                            .getActualCategoryPrice().setScale(2).toString());
                    goodsJsonArray.add(goodsJsonObject);
                }

                orderPropertiesJsonObject.put("orderId", order.getOrderId());
                orderPropertiesJsonObject.put("vipLevel", order.getVipLevel());
                SimpleDateFormat df = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                orderPropertiesJsonObject.put("orderDate",
                        df.format(order.getBuyDate()));

                Boxes boxes = boxesService.findBoxesByBoxId(order.getBoxId());
                if (boxes != null && boxes.getBoxName() != null) {
                    orderPropertiesJsonObject
                            .put("boxName", boxes.getBoxName());
                } else {
                    orderPropertiesJsonObject.put("boxName", "unknow");
                }
                orderPropertiesJsonObject.put("goodsTotalNumber",
                        order.getGoodsTotalNumber());
                orderPropertiesJsonObject.put("payTotal", order.getPayTotal()
                        .setScale(2).toString());
                orderPropertiesJsonObject.put("totalFavourable", order
                        .getTotalFavourable().setScale(2).toString());
                orderPropertiesJsonObject.put("actualPayTotal", order
                        .getActualPayTotal().setScale(2).toString());
                orderPropertiesJsonObject.put("payState", order.getPayState());

                orderPropertiesJsonObject.put("Goods", goodsJsonArray);

                orderJsonObject.put("Order", orderPropertiesJsonObject);

                ordersJsonArray.add(orderJsonObject);
            }
            ordersJsonObject.put("Orders", ordersJsonArray);
            return ordersJsonObject.toString();
        } else {
            return ResponseStatus.NO_ORDERS;
        }
    }

    // 分页查询逻辑
    public String doFindMyOrders(String phoneNumber, String orderType,
            int pageNumber) {
        // 初始化用户未读订单，设置为0
        UserService userService = new UserService();
        userService.doupdateUserUnReadOrderNumber(phoneNumber, 0);
        BoxesService boxesService = new BoxesService();
        int payState = orderType.equals("payedOrder") ? 1 : 0;
        int count = findOrderCountByPhoneNumberAndPayState(phoneNumber,
                payState);
        int totalPages = 0;
        // 计算总页数
        if (count % Order.PAGE_SIZE == 0) {
            // 对总页数赋值
            totalPages = count / Order.PAGE_SIZE;
        } else {
            // 对总页数赋值
            totalPages = count / Order.PAGE_SIZE + 1;
        }

        List<Order> ordersList = findOrdersByPhoneNumberAndPayStestAndPageNumber(
                phoneNumber, payState, pageNumber);
        OrderDetailsService orderDetailsService = new OrderDetailsService();
        BigDecimal categoryGoodsWeight = new BigDecimal(0);
        if (pageNumber == 1 && ordersList.size() <= 0) {
            return ResponseStatus.NO_ORDERS;
        } else if (pageNumber <= totalPages && ordersList.size() > 0) {
            JSONObject ordersJsonObject = new JSONObject();
            JSONArray ordersJsonArray = new JSONArray();
            JSONObject orderJsonObject = new JSONObject();
            for (int i = 0; i < ordersList.size(); i++) {
                Order order = ordersList.get(i);
                List<OrderDetails> orderDetailsList = orderDetailsService
                        .findOrderDetailsByOrderId(order.getOrderId());
                JSONObject orderPropertiesJsonObject = new JSONObject();
                JSONArray goodsJsonArray = new JSONArray();

                for (int j = 0; j < orderDetailsList.size(); j++) {
                    OrderDetails orderDetails = orderDetailsList.get(j);
                    JSONObject goodsJsonObject = new JSONObject();
                    goodsJsonObject.put("goodsName",
                            orderDetails.getGoodsName());
                    goodsJsonObject.put("salesMode",
                            orderDetails.getSalesMode());
                    goodsJsonObject.put("goodsPrice", orderDetails
                            .getGoodsPrice().setScale(2).toString());
                    goodsJsonObject.put("goodsFavorablePrice", orderDetails
                            .getGoodsFavorablePrice().setScale(2).toString());
                    goodsJsonObject.put("goodsActualPrice", orderDetails
                            .getGoodsActualPrice().setScale(2).toString());
                    goodsJsonObject.put("categoryGoodsNumber",
                            orderDetails.getCategoryGoodsNumber());
                    // 将类别商品重量由g转化为kg,并保留3位有效数字
                    categoryGoodsWeight = BigDecimal.valueOf(
                            Double.valueOf(Double.valueOf(orderDetails
                                    .getCategoryGoodsWeight()) / 1000))
                            .setScale(3, BigDecimal.ROUND_UP);
                    goodsJsonObject.put("categoryGoodsWeight",
                            categoryGoodsWeight);
                    goodsJsonObject.put("categoryGoodsPrice", orderDetails
                            .getCategoryGoodsPrice().setScale(2).toString());
                    goodsJsonObject
                            .put("categoryFavorablePrice", orderDetails
                                    .getCategoryFavorablePrice().setScale(2)
                                    .toString());
                    goodsJsonObject.put("categoryActualPrice", orderDetails
                            .getActualCategoryPrice().setScale(2).toString());
                    goodsJsonArray.add(goodsJsonObject);
                }

                orderPropertiesJsonObject.put("orderId", order.getOrderId());
                orderPropertiesJsonObject.put("vipLevel", order.getVipLevel());
                SimpleDateFormat df = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                orderPropertiesJsonObject.put("orderDate",
                        df.format(order.getBuyDate()));

                Boxes boxes = boxesService.findBoxesByBoxId(order.getBoxId());
                if (boxes != null && boxes.getBoxName() != null) {
                    orderPropertiesJsonObject
                            .put("boxName", boxes.getBoxName());
                } else {
                    orderPropertiesJsonObject.put("boxName", "unknow");
                }
                orderPropertiesJsonObject.put("goodsTotalNumber",
                        order.getGoodsTotalNumber());
                orderPropertiesJsonObject.put("payTotal", order.getPayTotal()
                        .setScale(2).toString());
                orderPropertiesJsonObject.put("totalFavourable", order
                        .getTotalFavourable().setScale(2).toString());
                orderPropertiesJsonObject.put("actualPayTotal", order
                        .getActualPayTotal().setScale(2).toString());
                orderPropertiesJsonObject.put("payState", order.getPayState());

                orderPropertiesJsonObject.put("Goods", goodsJsonArray);

                orderJsonObject.put("Order", orderPropertiesJsonObject);

                ordersJsonArray.add(orderJsonObject);
            }
            ordersJsonObject.put("Orders", ordersJsonArray);
            return ordersJsonObject.toString();
        } else {
            return ResponseStatus.LAST_PAGE;
        }
    }

    // 动态获取近n条菜单分页查询逻辑,用作动态展示最近订单
    public String doFindDynamicOrders() {
        BoxesService boxesService = new BoxesService();

        List<Order> ordersList = findTopNOrders(Order.DYNAMIC_ORDER_PAGE_SIZE);
        OrderDetailsService orderDetailsService = new OrderDetailsService();
        BigDecimal categoryGoodsWeight = new BigDecimal(0);
        if (ordersList.size() <= 0) {
            return ResponseStatus.NO_ORDERS;
        } else {
            JSONObject ordersJsonObject = new JSONObject();
            JSONArray ordersJsonArray = new JSONArray();
            JSONObject orderJsonObject = new JSONObject();
            for (int i = 0; i < ordersList.size(); i++) {
                Order order = ordersList.get(i);
                List<OrderDetails> orderDetailsList = orderDetailsService
                        .findOrderDetailsByOrderId(order.getOrderId());
                JSONObject orderPropertiesJsonObject = new JSONObject();
                JSONArray goodsJsonArray = new JSONArray();

                for (int j = 0; j < orderDetailsList.size(); j++) {
                    OrderDetails orderDetails = orderDetailsList.get(j);
                    JSONObject goodsJsonObject = new JSONObject();
                    goodsJsonObject.put("goodsName",
                            orderDetails.getGoodsName());
                    goodsJsonObject.put("salesMode",
                            orderDetails.getSalesMode());
                    goodsJsonObject.put("goodsPrice", orderDetails
                            .getGoodsPrice().setScale(2).toString());
                    goodsJsonObject.put("goodsFavorablePrice", orderDetails
                            .getGoodsFavorablePrice().setScale(2).toString());
                    goodsJsonObject.put("goodsActualPrice", orderDetails
                            .getGoodsActualPrice().setScale(2).toString());
                    goodsJsonObject.put("categoryGoodsNumber",
                            orderDetails.getCategoryGoodsNumber());
                    // 将类别商品重量由g转化为kg,并保留3位有效数字
                    categoryGoodsWeight = BigDecimal.valueOf(
                            Double.valueOf(Double.valueOf(orderDetails
                                    .getCategoryGoodsWeight()) / 1000))
                            .setScale(3, BigDecimal.ROUND_UP);
                    goodsJsonObject.put("categoryGoodsWeight",
                            categoryGoodsWeight);
                    goodsJsonObject.put("categoryGoodsPrice", orderDetails
                            .getCategoryGoodsPrice().setScale(2).toString());
                    goodsJsonObject
                            .put("categoryFavorablePrice", orderDetails
                                    .getCategoryFavorablePrice().setScale(2)
                                    .toString());
                    goodsJsonObject.put("categoryActualPrice", orderDetails
                            .getActualCategoryPrice().setScale(2).toString());
                    goodsJsonArray.add(goodsJsonObject);
                }

                orderPropertiesJsonObject.put("orderId", order.getOrderId());
                orderPropertiesJsonObject.put("vipLevel", order.getVipLevel());
                orderPropertiesJsonObject.put("phoneNumber",
                        order.getPhoneNumber());
                SimpleDateFormat df = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                orderPropertiesJsonObject.put("orderDate",
                        df.format(order.getBuyDate()));

                Boxes boxes = boxesService.findBoxesByBoxId(order.getBoxId());
                if (boxes != null && boxes.getBoxName() != null) {
                    orderPropertiesJsonObject
                            .put("boxName", boxes.getBoxName());
                } else {
                    orderPropertiesJsonObject.put("boxName", "unknow");
                }
                orderPropertiesJsonObject.put("goodsTotalNumber",
                        order.getGoodsTotalNumber());
                orderPropertiesJsonObject.put("payTotal", order.getPayTotal()
                        .setScale(2).toString());
                orderPropertiesJsonObject.put("totalFavourable", order
                        .getTotalFavourable().setScale(2).toString());
                orderPropertiesJsonObject.put("actualPayTotal", order
                        .getActualPayTotal().setScale(2).toString());
                orderPropertiesJsonObject.put("payType", order.getPayType());
                orderPropertiesJsonObject.put("payState", order.getPayState());

                orderPropertiesJsonObject.put("Goods", goodsJsonArray);

                orderJsonObject.put("Order", orderPropertiesJsonObject);

                ordersJsonArray.add(orderJsonObject);
            }
            ordersJsonObject.put("Orders", ordersJsonArray);
            ordersJsonObject.put("currentDayOrderCount",
                    findCurrentDayOrderCount());
            ordersJsonObject.put("totalOrderCount", findTotalOrderCount());
            return ordersJsonObject.toString();
        }
    }

    public String doFindAllMyOrders(String phoneNumber) {
        // 初始化用户未读订单，设置为0
        UserService userService = new UserService();
        userService.doupdateUserUnReadOrderNumber(phoneNumber, 0);
        BoxesService boxesService = new BoxesService();
        List<Order> ordersList = findOrdersByPhoneNumber(phoneNumber);
        OrderDetailsService orderDetailsService = new OrderDetailsService();
        BigDecimal categoryGoodsWeight = new BigDecimal(0);
        if (ordersList.size() <= 0) {
            return ResponseStatus.NO_ORDERS;
        } else {
            JSONObject ordersJsonObject = new JSONObject();
            JSONArray ordersJsonArray = new JSONArray();
            JSONObject orderJsonObject = new JSONObject();
            for (int i = 0; i < ordersList.size(); i++) {
                Order order = ordersList.get(i);
                List<OrderDetails> orderDetailsList = orderDetailsService
                        .findOrderDetailsByOrderId(order.getOrderId());
                JSONObject orderPropertiesJsonObject = new JSONObject();
                JSONArray goodsJsonArray = new JSONArray();

                for (int j = 0; j < orderDetailsList.size(); j++) {
                    OrderDetails orderDetails = orderDetailsList.get(j);
                    JSONObject goodsJsonObject = new JSONObject();
                    goodsJsonObject.put("goodsName",
                            orderDetails.getGoodsName());
                    goodsJsonObject.put("salesMode",
                            orderDetails.getSalesMode());
                    goodsJsonObject.put("goodsPrice", orderDetails
                            .getGoodsPrice().setScale(2).toString());
                    goodsJsonObject.put("goodsFavorablePrice", orderDetails
                            .getGoodsFavorablePrice().setScale(2).toString());
                    goodsJsonObject.put("goodsActualPrice", orderDetails
                            .getGoodsActualPrice().setScale(2).toString());
                    goodsJsonObject.put("categoryGoodsNumber",
                            orderDetails.getCategoryGoodsNumber());
                    // 将类别商品重量由g转化为kg,并保留3位有效数字
                    categoryGoodsWeight = BigDecimal.valueOf(
                            Double.valueOf(Double.valueOf(orderDetails
                                    .getCategoryGoodsWeight()) / 1000))
                            .setScale(3, BigDecimal.ROUND_UP);
                    goodsJsonObject.put("categoryGoodsWeight",
                            categoryGoodsWeight);
                    goodsJsonObject.put("categoryGoodsPrice", orderDetails
                            .getCategoryGoodsPrice().setScale(2).toString());
                    goodsJsonObject
                            .put("categoryFavorablePrice", orderDetails
                                    .getCategoryFavorablePrice().setScale(2)
                                    .toString());
                    goodsJsonObject.put("categoryActualPrice", orderDetails
                            .getActualCategoryPrice().setScale(2).toString());
                    goodsJsonArray.add(goodsJsonObject);
                }

                orderPropertiesJsonObject.put("orderId", order.getOrderId());
                SimpleDateFormat df = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                orderPropertiesJsonObject.put("orderDate",
                        df.format(order.getBuyDate()));
                Boxes boxes = boxesService.findBoxesByBoxId(order.getBoxId());
                if (boxes != null && boxes.getBoxName() != null) {
                    orderPropertiesJsonObject
                            .put("boxName", boxes.getBoxName());
                } else {
                    orderPropertiesJsonObject.put("boxName", "unknow");
                }
                orderPropertiesJsonObject.put("goodsTotalNumber",
                        order.getGoodsTotalNumber());
                orderPropertiesJsonObject.put("payTotal", order.getPayTotal()
                        .setScale(2).toString());
                orderPropertiesJsonObject.put("totalFavourable", order
                        .getTotalFavourable().setScale(2).toString());
                orderPropertiesJsonObject.put("actualPayTotal", order
                        .getActualPayTotal().setScale(2).toString());
                orderPropertiesJsonObject.put("payState", order.getPayState());

                orderPropertiesJsonObject.put("Goods", goodsJsonArray);

                orderJsonObject.put("Order", orderPropertiesJsonObject);

                ordersJsonArray.add(orderJsonObject);
            }
            ordersJsonObject.put("Orders", ordersJsonArray);
            return ordersJsonObject.toString();
        }
    }

    public String doWXPaySuccess(Order order) {
        if (updateOrder(order)) {
            return ResponseStatus.SUCCESS;
        } else {
            return ResponseStatus.FAIL;
        }
    }

    // 返回失败表示有未支付订单，返回成功表示没有未支付订单
    public String doCheckNoPayOrder(String phoneNumber) {
        List<Order> noPayedOrdersList = findOrdersByPhoneNumberAndPayState(
                phoneNumber, 0);
        if (noPayedOrdersList.size() > 0) {
            return ResponseStatus.FAIL;
        } else {
            return ResponseStatus.SUCCESS;
        }
    }

    public String doAlipay(String agreementNO, String actualPayTotal)
            throws Exception {
        HttpGet httpGet = new HttpGet(
                "http://zigoomall.com:81/alipay/createandpay.php?agreement_no="
                        + agreementNO + "&total_fee=" + actualPayTotal);
        // 设置请求器的配置
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpResponse res = httpClient.execute(httpGet);
        HttpEntity entity = res.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");
        return result;
    }

    // 查询在智购猫总消费和总优惠
    public String doFindOrderTotalDescription(String phoneNumber) {
        Order order = findAllActualPayTotalAndAllTotalFavourableByPhoneNumber(phoneNumber);
        if (order != null) {
            JSONObject orderjsonObject = new JSONObject();
            orderjsonObject.put("allActualPayTotal", order
                    .getAllActualPayTotal().setScale(2, BigDecimal.ROUND_DOWN)
                    .toString());
            orderjsonObject.put("allTotalFavourable", order
                    .getAllTotalFavourable().setScale(2, BigDecimal.ROUND_UP)
                    .toString());
            JSONObject totalOrderJsonObject = new JSONObject();
            totalOrderJsonObject.put("OrderTotalDescription", orderjsonObject);
            return totalOrderJsonObject.toString();
        } else {
            return ResponseStatus.FAIL;
        }
    }

    // 把原始要修改的订单保存到t_order_modify中
    // private String orderId;// 订单号
    // private String newOrderId;// 订单修改后的新的订单号
    // private String phoneNumber;// 手机号码
    // private int vipLevel;// 消费者VIP等级：0：SVIP；1：普通消费者
    // private String boxId;// 售货机ID
    // private int goodsTotalNumber;// 订单商品总数量
    // private BigDecimal payTotal;// 本该支付总额
    // private BigDecimal totalFavourable;// 优惠总金额
    // private BigDecimal actualPayTotal;// 实际支付
    // private String payType;// 支付方式（微信或支付宝）
    // private String thirdPayOrderId;// 三方支付（微信或支付宝）平台订单号(暂时不用)
    // private String outTradeNo;// 商户申请支付商户侧交易账号
    // private String transactionId;// 微信支付微信侧交易ID
    // private int payState;// 是否已支付
    // private Date payDate;// 订单支付时间
    // private Date buyDate;// 订单时间
    // private String orderUpdateOwner;// 订单更新操作者
    // private Date orderUpdateDate;// 订单更新时间
    public boolean addOldOrderToOrderModify(Order oldOrder,
            String resolverOwner, String newOrderId, Date newOrderDate) {
        OrderModifyService orderModifyService = new OrderModifyService();
        OrderModify orderModify = new OrderModify();
        orderModify.setOrderId(oldOrder.getOrderId());
        orderModify.setNewOrderId(newOrderId);
        orderModify.setPhoneNumber(oldOrder.getPhoneNumber());
        orderModify.setVipLevel(oldOrder.getVipLevel());
        orderModify.setBoxId(oldOrder.getBoxId());
        orderModify.setGoodsTotalNumber(oldOrder.getGoodsTotalNumber());
        orderModify.setPayTotal(oldOrder.getPayTotal());
        orderModify.setTotalFavourable(oldOrder.getTotalFavourable());
        orderModify.setActualPayTotal(oldOrder.getActualPayTotal());
        orderModify.setPayType(oldOrder.getPayType());
        orderModify.setThirdPayOrderId(oldOrder.getThirdPayOrderId());
        orderModify.setOutTradeNo(oldOrder.getOutTradeNo());
        orderModify.setTransactionId(oldOrder.getTransactionId());
        orderModify.setPayState(oldOrder.getPayState());
        orderModify.setPayDate(oldOrder.getPayDate());
        orderModify.setBuyDate(oldOrder.getBuyDate());
        orderModify.setOrderUpdateOwner(resolverOwner);
        orderModify.setOrderUpdateDate(newOrderDate);
        if (orderModifyService.addOrderModify(orderModify)) {
            OrderDetailsModifyService orderDetailsModifyService = new OrderDetailsModifyService();
            OrderDetailsService orderDetailsService = new OrderDetailsService();
            List<OrderDetails> orderDetailsList = orderDetailsService
                    .findOrderDetailsByOrderId(oldOrder.getOrderId());
            // 判断是否为空账单，暂时用payState=3表示,当不为空账单时插入账单详情，否则账单详情不做处理
            if (oldOrder.getPayState() != 3) {
                for (int i = 0; i < orderDetailsList.size(); i++) {
                    OrderDetails orderDetails = orderDetailsList.get(i);
                    OrderDetailsModify orderDetailsModify = (OrderDetailsModify) JSONObject
                            .toBean(JSONObject.fromObject(orderDetails),
                                    OrderDetailsModify.class);
                    orderDetailsModifyService
                            .addOrderDetailsModify(orderDetailsModify);
                }
            }
            return true;
        } else {
            return false;
        }
    }

}
