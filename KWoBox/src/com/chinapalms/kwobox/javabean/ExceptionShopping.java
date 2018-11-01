package com.chinapalms.kwobox.javabean;

import java.util.Date;

public class ExceptionShopping {

    private int id;
    private String exceptionId;// 异常购物信息ID
    private String exceptionShoppingInfo;// 异常购物内容
    private String boxId;// 发生异常购物售货机ID
    private String phoneNumber;// 购物异常者手机号码
    private int state;// 异常状态；0：待处理；1：处理中；2：已处理
    private Date handleTime;// 异常处理时间
    private Date exceptionTime;// 异常发生时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExceptionId() {
        return exceptionId;
    }

    public void setExceptionId(String exceptionId) {
        this.exceptionId = exceptionId;
    }

    public String getExceptionShoppingInfo() {
        return exceptionShoppingInfo;
    }

    public void setExceptionShoppingInfo(String exceptionShoppingInfo) {
        this.exceptionShoppingInfo = exceptionShoppingInfo;
    }

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }

    public Date getExceptionTime() {
        return exceptionTime;
    }

    public void setExceptionTime(Date exceptionTime) {
        this.exceptionTime = exceptionTime;
    }

    @Override
    public String toString() {
        return "ExceptionShopping [id=" + id + ", exceptionId=" + exceptionId
                + ", exceptionShoppingInfo=" + exceptionShoppingInfo
                + ", boxId=" + boxId + ", phoneNumber=" + phoneNumber
                + ", state=" + state + ", handleTime=" + handleTime
                + ", exceptionTime=" + exceptionTime + "]";
    }

}
