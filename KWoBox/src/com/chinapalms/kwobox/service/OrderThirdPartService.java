package com.chinapalms.kwobox.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONObject;

import com.chinapalms.kwobox.dao.impl.OrderThirdPartDAOImpl;
import com.chinapalms.kwobox.javabean.Order;
import com.chinapalms.kwobox.javabean.OrderDetails;
import com.chinapalms.kwobox.javabean.OrderDetailsModify;
import com.chinapalms.kwobox.javabean.OrderDetailsThirdPart;
import com.chinapalms.kwobox.javabean.OrderDetailsThirdPartModify;
import com.chinapalms.kwobox.javabean.OrderModify;
import com.chinapalms.kwobox.javabean.OrderThirdPart;
import com.chinapalms.kwobox.javabean.OrderThirdPartModify;

public class OrderThirdPartService extends OrderThirdPartDAOImpl {

    Log log = LogFactory.getLog(OrderThirdPartService.class);

    @Override
    public boolean addOrderThirdPart(OrderThirdPart order) {
        return super.addOrderThirdPart(order);
    }

    @Override
    public OrderThirdPart findOrderThirdPartByOrderId(String orderId) {
        return super.findOrderThirdPartByOrderId(orderId);
    }

    @Override
    public boolean updateThirdPartOrder(OrderThirdPart orderThirdPart) {
        return super.updateThirdPartOrder(orderThirdPart);
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
    public boolean addOldOrderThirdPartToOrderThirdPartModify(
            String oldOrderId, String resolverOwner, String newOrderId,
            Date newOrderDate) {
        try {
            OrderThirdPart oldOrder = findOrderThirdPartByOrderId(oldOrderId);
            OrderThirdPartModifyService orderModifyService = new OrderThirdPartModifyService();
            OrderThirdPartModify orderModify = new OrderThirdPartModify();
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
            if (orderModifyService.addOrderThirdPartModify(orderModify)) {
                OrderDetailsThirdPartModifyService orderDetailsModifyService = new OrderDetailsThirdPartModifyService();
                OrderDetailsThirdPartService orderDetailsService = new OrderDetailsThirdPartService();
                List<OrderDetailsThirdPart> orderDetailsThirdPartList = orderDetailsService
                        .findOrderDetailsThirdPartByOrderId(oldOrder
                                .getOrderId());
                // 判断是否为空账单，暂时用payState=3表示,当不为空账单时插入账单详情，否则账单详情不做处理
                if (oldOrder.getPayState() != 3) {
                    for (int i = 0; i < orderDetailsThirdPartList.size(); i++) {
                        OrderDetailsThirdPart orderDetailsThirdPart = orderDetailsThirdPartList
                                .get(i);
                        OrderDetailsThirdPartModify orderDetailsModify = (OrderDetailsThirdPartModify) JSONObject
                                .toBean(JSONObject
                                        .fromObject(orderDetailsThirdPart),
                                        OrderDetailsThirdPartModify.class);
                        orderDetailsModifyService
                                .addOrderDetailsThirdPartModify(orderDetailsModify);
                    }
                }
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        return true;
    }

}
