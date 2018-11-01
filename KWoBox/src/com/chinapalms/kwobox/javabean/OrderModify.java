package com.chinapalms.kwobox.javabean;

import java.math.BigDecimal;
import java.util.Date;

public class OrderModify {

    private String orderId;// 订单号
    private String newOrderId;// 订单修改后的新的订单号
    private String phoneNumber;// 手机号码
    private int vipLevel;// 消费者VIP等级：0：SVIP；1：普通消费者
    private String boxId;// 售货机ID
    private int goodsTotalNumber;// 订单商品总数量
    private BigDecimal payTotal;// 本该支付总额
    private BigDecimal totalFavourable;// 优惠总金额
    private BigDecimal actualPayTotal;// 实际支付
    private String payType;// 支付方式（微信或支付宝）
    private String thirdPayOrderId;// 三方支付（微信或支付宝）平台订单号(暂时不用)
    private String outTradeNo;// 商户申请支付商户侧交易账号
    private String transactionId;// 微信支付微信侧交易ID
    private int payState;// 是否已支付
    private Date payDate;// 订单支付时间
    private Date buyDate;// 订单时间
    private String orderUpdateOwner;// 订单更新操作者
    private Date orderUpdateDate;// 订单更新时间

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getNewOrderId() {
        return newOrderId;
    }

    public void setNewOrderId(String newOrderId) {
        this.newOrderId = newOrderId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    public int getGoodsTotalNumber() {
        return goodsTotalNumber;
    }

    public void setGoodsTotalNumber(int goodsTotalNumber) {
        this.goodsTotalNumber = goodsTotalNumber;
    }

    public BigDecimal getPayTotal() {
        return payTotal;
    }

    public void setPayTotal(BigDecimal payTotal) {
        this.payTotal = payTotal;
    }

    public BigDecimal getTotalFavourable() {
        return totalFavourable;
    }

    public void setTotalFavourable(BigDecimal totalFavourable) {
        this.totalFavourable = totalFavourable;
    }

    public BigDecimal getActualPayTotal() {
        return actualPayTotal;
    }

    public void setActualPayTotal(BigDecimal actualPayTotal) {
        this.actualPayTotal = actualPayTotal;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getThirdPayOrderId() {
        return thirdPayOrderId;
    }

    public void setThirdPayOrderId(String thirdPayOrderId) {
        this.thirdPayOrderId = thirdPayOrderId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public int getPayState() {
        return payState;
    }

    public void setPayState(int payState) {
        this.payState = payState;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public Date getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }

    public String getOrderUpdateOwner() {
        return orderUpdateOwner;
    }

    public void setOrderUpdateOwner(String orderUpdateOwner) {
        this.orderUpdateOwner = orderUpdateOwner;
    }

    public Date getOrderUpdateDate() {
        return orderUpdateDate;
    }

    public void setOrderUpdateDate(Date orderUpdateDate) {
        this.orderUpdateDate = orderUpdateDate;
    }

    @Override
    public String toString() {
        return "OrderModify [orderId=" + orderId + ", newOrderId=" + newOrderId
                + ", phoneNumber=" + phoneNumber + ", vipLevel=" + vipLevel
                + ", boxId=" + boxId + ", goodsTotalNumber=" + goodsTotalNumber
                + ", payTotal=" + payTotal + ", totalFavourable="
                + totalFavourable + ", actualPayTotal=" + actualPayTotal
                + ", payType=" + payType + ", thirdPayOrderId="
                + thirdPayOrderId + ", outTradeNo=" + outTradeNo
                + ", transactionId=" + transactionId + ", payState=" + payState
                + ", payDate=" + payDate + ", buyDate=" + buyDate
                + ", orderUpdateOwner=" + orderUpdateOwner
                + ", orderUpdateDate=" + orderUpdateDate + "]";
    }

}
