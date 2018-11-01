package com.chinapalms.kwobox.javabean;

import java.util.Date;

public class CustomerWorkerPermission {

    private int id;
    private int customerWorkerId;// 商户工作人员ID
    private int boxManagePermission;// 售货机管理权限
    private int boxDeliveryPermission;// 售货机送货权限
    private int maintainerPermission;// 售货机维修权限
    private int dataManagerPermission;// 数据管理权限
    private int customerAdminId;// 商户系统管理员ID
    private int goodsManagerPermission;// 商品管理权限
    private Date updateDateTime;// 更新授权时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerWorkerId() {
        return customerWorkerId;
    }

    public void setCustomerWorkerId(int customerWorkerId) {
        this.customerWorkerId = customerWorkerId;
    }

    public int getBoxManagePermission() {
        return boxManagePermission;
    }

    public void setBoxManagePermission(int boxManagePermission) {
        this.boxManagePermission = boxManagePermission;
    }

    public int getBoxDeliveryPermission() {
        return boxDeliveryPermission;
    }

    public void setBoxDeliveryPermission(int boxDeliveryPermission) {
        this.boxDeliveryPermission = boxDeliveryPermission;
    }

    public int getMaintainerPermission() {
        return maintainerPermission;
    }

    public void setMaintainerPermission(int maintainerPermission) {
        this.maintainerPermission = maintainerPermission;
    }

    public int getDataManagerPermission() {
        return dataManagerPermission;
    }

    public void setDataManagerPermission(int dataManagerPermission) {
        this.dataManagerPermission = dataManagerPermission;
    }

    public int getCustomerAdminId() {
        return customerAdminId;
    }

    public void setCustomerAdminId(int customerAdminId) {
        this.customerAdminId = customerAdminId;
    }

    public int getGoodsManagerPermission() {
        return goodsManagerPermission;
    }

    public void setGoodsManagerPermission(int goodsManagerPermission) {
        this.goodsManagerPermission = goodsManagerPermission;
    }

    public Date getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(Date updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    @Override
    public String toString() {
        return "CustomerWorkerPermission [id=" + id + ", customerWorkerId="
                + customerWorkerId + ", boxManagePermission="
                + boxManagePermission + ", boxDeliveryPermission="
                + boxDeliveryPermission + ", maintainerPermission="
                + maintainerPermission + ", dataManagerPermission="
                + dataManagerPermission + ", customerAdminId="
                + customerAdminId + ", goodsManagerPermission="
                + goodsManagerPermission + ", updateDateTime=" + updateDateTime
                + "]";
    }

}
