package com.custom.kwobox.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.javabean.Customer;
import com.chinapalms.kwobox.javabean.DoorClosedCallback;
import com.chinapalms.kwobox.javabean.DoorClosedCallbackCustomization;
import com.chinapalms.kwobox.javabean.DoorOpenedCallback;
import com.chinapalms.kwobox.javabean.DoorOpenedCallbackCustomization;
import com.chinapalms.kwobox.javabean.OrderCallback;
import com.chinapalms.kwobox.javabean.OrderCallbackCustomization;
import com.chinapalms.kwobox.javabean.ReplenishmentCallback;
import com.chinapalms.kwobox.javabean.ReplenishmentCallbackCustomization;
import com.chinapalms.kwobox.service.CustomerService;
import com.chinapalms.kwobox.service.DoorClosedCallbackCustomizationService;
import com.chinapalms.kwobox.service.DoorClosedCallbackService;
import com.chinapalms.kwobox.service.DoorOpenedCallbackCustomizationService;
import com.chinapalms.kwobox.service.DoorOpenedCallbackService;
import com.chinapalms.kwobox.service.OrderCallbackCustomizationService;
import com.chinapalms.kwobox.service.OrderCallbackService;
import com.chinapalms.kwobox.service.ReplenishmentCallbackCustomizationService;
import com.chinapalms.kwobox.service.ReplenishmentCallbackService;
import com.chinapalms.kwobox.utils.ResponseStatus;

public class CustomUpdateCallbackUrlService {

    Log log = LogFactory.getLog(CustomUpdateCallbackUrlService.class);

    /**
     * 更新商户通用相关回调Url
     * 
     * @param mchId
     *            :服务商分配给上面的ID
     * @param urlType
     *            :回到的Url类型：普通消费者开门相关回调||理货员开门相关回调
     * @param doorOpenedCallbackUrl
     *            :门已开回调
     * @param doorClosedCallbackUrl
     *            :门已关回调
     * @param orderCallbackUrl
     *            :订单或理货单回调
     * @return
     */
    public String updateCustomCallbackUrl(String mchId, String urlType,
            String doorOpenedCallbackUrl, String doorClosedCallbackUrl,
            String orderCallbackUrl) {
        log.info("CustomUpdateCallbackUrlService->" + "mchId=" + mchId
                + ",urlType=" + urlType + ",doorOpenedCallbackUrl="
                + doorOpenedCallbackUrl + ",doorClosedCallbackUrl="
                + doorClosedCallbackUrl + ",orderCallbackUrl="
                + orderCallbackUrl);

        try {
            CustomerService customerService = new CustomerService();
            Customer customer = customerService.findCustomerByMchId(mchId);
            if (customer != null) {
                int customerId = customer.getCustomerId();
                if (urlType.equals(ResponseStatus.PERMISSION_NORMAL_USER)) {
                    if (doorOpenedCallbackUrl != null
                            && doorOpenedCallbackUrl.trim().length() > 0) {
                        DoorOpenedCallbackService doorOpenedCallbackService = new DoorOpenedCallbackService();
                        DoorOpenedCallback doorOpenedCallback = new DoorOpenedCallback();
                        doorOpenedCallback.setCustomerId(customerId);
                        doorOpenedCallback
                                .setDoorOpenedCallbackUrl(doorOpenedCallbackUrl
                                        .trim());
                        doorOpenedCallbackService
                                .updateDoorOpenedCallback(doorOpenedCallback);
                    }
                    if (doorClosedCallbackUrl != null
                            && doorClosedCallbackUrl.trim().length() > 0) {
                        DoorClosedCallbackService doorClosedCallbackService = new DoorClosedCallbackService();
                        DoorClosedCallback doorClosedCallback = new DoorClosedCallback();
                        doorClosedCallback.setCustomerId(customerId);
                        doorClosedCallback
                                .setDoorClosedCallbackUrl(doorClosedCallbackUrl
                                        .trim());
                        doorClosedCallbackService
                                .updateDoorClosedCallback(doorClosedCallback);
                    }
                    if (orderCallbackUrl != null
                            && orderCallbackUrl.trim().length() > 0) {
                        OrderCallbackService orderCallbackService = new OrderCallbackService();
                        OrderCallback orderCallback = new OrderCallback();
                        orderCallback.setCustomerId(customerId);
                        orderCallback.setOrderCallbackUrl(orderCallbackUrl
                                .trim());
                        orderCallbackService.updateOrderCallback(orderCallback);
                    }
                } else {
                    if (doorOpenedCallbackUrl != null
                            && doorOpenedCallbackUrl.trim().length() > 0) {
                        DoorOpenedCallbackService doorOpenedCallbackService = new DoorOpenedCallbackService();
                        DoorOpenedCallback doorOpenedCallback = new DoorOpenedCallback();
                        doorOpenedCallback.setCustomerId(customerId);
                        doorOpenedCallback
                                .setDoorOpenedCallbackUrl(doorOpenedCallbackUrl
                                        .trim());
                        doorOpenedCallbackService
                                .updateDoorOpenedCallback(doorOpenedCallback);
                    }
                    if (doorClosedCallbackUrl != null
                            && doorClosedCallbackUrl.trim().length() > 0) {
                        DoorClosedCallbackService doorClosedCallbackService = new DoorClosedCallbackService();
                        DoorClosedCallback doorClosedCallback = new DoorClosedCallback();
                        doorClosedCallback.setCustomerId(customerId);
                        doorClosedCallback
                                .setDoorClosedCallbackUrl(doorClosedCallbackUrl
                                        .trim());
                        doorClosedCallbackService
                                .updateDoorClosedCallback(doorClosedCallback);
                    }
                    if (orderCallbackUrl != null
                            && orderCallbackUrl.trim().length() > 0) {
                        ReplenishmentCallbackService replenishmentCallbackService = new ReplenishmentCallbackService();
                        ReplenishmentCallback replenishmentCallback = new ReplenishmentCallback();
                        replenishmentCallback.setCustomerId(customerId);
                        replenishmentCallback
                                .setReplenishmentCallbackUrl(orderCallbackUrl
                                        .trim());
                        replenishmentCallbackService
                                .updateReplenishmentCallback(replenishmentCallback);
                    }
                }
            }
            return ResponseStatus.SUCCESS;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        return ResponseStatus.FAIL;
    }

    /**
     * 更新商户柜子相关客制化回调Url
     * 
     * @param boxId
     *            :售货机ID
     * @param mchId
     *            :服务商分配给上面的ID
     * @param urlType
     *            :回到的Url类型：普通消费者开门相关回调||理货员开门相关回调
     * @param doorOpenedCallbackUrl
     *            :门已开回调
     * @param doorClosedCallbackUrl
     *            :门已关回调
     * @param orderCallbackUrl
     *            :订单或理货单回调
     * @return
     */
    public String updateBoxCustomizationCallbackUrl(String mchId, String boxId,
            String urlType, String doorOpenedCallbackUrl,
            String doorClosedCallbackUrl, String orderCallbackUrl) {
        log.info("updateBoxCustomizationCallbackUrl->" + "mchId=" + mchId
                + ",boxId=" + boxId + ",urlType=" + urlType
                + ",doorOpenedCallbackUrl=" + doorOpenedCallbackUrl
                + ",doorClosedCallbackUrl=" + doorClosedCallbackUrl
                + ",orderCallbackUrl=" + orderCallbackUrl);
        // 当查询到数据库中有数据时进行更新操作，否则插入数据
        try {
            CustomerService customerService = new CustomerService();
            Customer customer = customerService.findCustomerByMchId(mchId);
            if (customer != null && boxId != null) {
                int customerId = customer.getCustomerId();
                // 普通消费者时更新或新增门已开，门已关，订单回调Url
                if (urlType.equals(ResponseStatus.PERMISSION_NORMAL_USER)) {
                    if (doorOpenedCallbackUrl != null
                            && doorOpenedCallbackUrl.trim().length() > 0) {
                        DoorOpenedCallbackCustomizationService doorOpenedCallbackCustomizationService = new DoorOpenedCallbackCustomizationService();
                        DoorOpenedCallbackCustomization doorOpenedCallbackCustomization = new DoorOpenedCallbackCustomization();
                        doorOpenedCallbackCustomization
                                .setCustomerId(customerId);
                        doorOpenedCallbackCustomization.setBoxId(boxId);
                        doorOpenedCallbackCustomization
                                .setDoorOpenedCallbackUrl(doorOpenedCallbackUrl
                                        .trim());

                        DoorOpenedCallbackCustomization doorOpenedCallbackCustomizationQuery = doorOpenedCallbackCustomizationService
                                .findDoorOpenedCallbackCustomizationByCustomerIdAndBoxId(
                                        customerId, boxId);
                        if (doorOpenedCallbackCustomizationQuery != null) {
                            doorOpenedCallbackCustomizationService
                                    .updateDoorOpenedCallbackCustomization(doorOpenedCallbackCustomization);
                        } else {
                            doorOpenedCallbackCustomizationService
                                    .addDoorOpenedCallbackCustomization(doorOpenedCallbackCustomization);
                        }
                    }
                    if (doorClosedCallbackUrl != null
                            && doorClosedCallbackUrl.trim().length() > 0) {
                        DoorClosedCallbackCustomizationService doorClosedCallbackCustomizationService = new DoorClosedCallbackCustomizationService();
                        DoorClosedCallbackCustomization doorClosedCallbackCustomization = new DoorClosedCallbackCustomization();
                        doorClosedCallbackCustomization
                                .setCustomerId(customerId);
                        doorClosedCallbackCustomization.setBoxId(boxId);
                        doorClosedCallbackCustomization
                                .setDoorClosedCallbackUrl(doorClosedCallbackUrl
                                        .trim());

                        DoorClosedCallbackCustomization doorClosedCallbackCustomizationQuery = doorClosedCallbackCustomizationService
                                .findDoorClosedCallbackCustomizationByCustomerIdAndBoxId(
                                        customerId, boxId);
                        if (doorClosedCallbackCustomizationQuery != null) {
                            doorClosedCallbackCustomizationService
                                    .updateDoorClosedCallbackCustomization(doorClosedCallbackCustomization);
                        } else {
                            doorClosedCallbackCustomizationService
                                    .addDoorClosedCallbackCustomization(doorClosedCallbackCustomization);
                        }
                    }
                    if (orderCallbackUrl != null
                            && orderCallbackUrl.trim().length() > 0) {
                        OrderCallbackCustomizationService orderCallbackCustomizationService = new OrderCallbackCustomizationService();
                        OrderCallbackCustomization orderCallbackCustomization = new OrderCallbackCustomization();
                        orderCallbackCustomization.setCustomerId(customerId);
                        orderCallbackCustomization.setBoxId(boxId);
                        orderCallbackCustomization
                                .setOrderCallbackUrl(orderCallbackUrl.trim());

                        OrderCallbackCustomization orderCallbackCustomizationQuery = orderCallbackCustomizationService
                                .findOrderCallbackCustomizationByCustomerIdAndBoxId(
                                        customerId, boxId);
                        if (orderCallbackCustomizationQuery != null) {
                            orderCallbackCustomizationService
                                    .updateOrderCallbackCustomization(orderCallbackCustomization);
                        } else {
                            orderCallbackCustomizationService
                                    .addOrderCallbackCustomization(orderCallbackCustomization);
                        }
                    }
                } else {
                    // 理货员时更新或新增门已开，门已关，理货单回调Url
                    if (doorOpenedCallbackUrl != null
                            && doorOpenedCallbackUrl.trim().length() > 0) {
                        DoorOpenedCallbackCustomizationService doorOpenedCallbackCustomizationService = new DoorOpenedCallbackCustomizationService();
                        DoorOpenedCallbackCustomization doorOpenedCallbackCustomization = new DoorOpenedCallbackCustomization();
                        doorOpenedCallbackCustomization
                                .setCustomerId(customerId);
                        doorOpenedCallbackCustomization.setBoxId(boxId);
                        doorOpenedCallbackCustomization
                                .setDoorOpenedCallbackUrl(doorOpenedCallbackUrl
                                        .trim());

                        DoorOpenedCallbackCustomization doorOpenedCallbackCustomizationQuery = doorOpenedCallbackCustomizationService
                                .findDoorOpenedCallbackCustomizationByCustomerIdAndBoxId(
                                        customerId, boxId);
                        if (doorOpenedCallbackCustomizationQuery != null) {
                            doorOpenedCallbackCustomizationService
                                    .updateDoorOpenedCallbackCustomization(doorOpenedCallbackCustomization);
                        } else {
                            doorOpenedCallbackCustomizationService
                                    .addDoorOpenedCallbackCustomization(doorOpenedCallbackCustomization);
                        }
                    }
                    if (doorClosedCallbackUrl != null
                            && doorClosedCallbackUrl.trim().length() > 0) {
                        DoorClosedCallbackCustomizationService doorClosedCallbackCustomizationService = new DoorClosedCallbackCustomizationService();
                        DoorClosedCallbackCustomization doorClosedCallbackCustomization = new DoorClosedCallbackCustomization();
                        doorClosedCallbackCustomization
                                .setCustomerId(customerId);
                        doorClosedCallbackCustomization.setBoxId(boxId);
                        doorClosedCallbackCustomization
                                .setDoorClosedCallbackUrl(doorClosedCallbackUrl
                                        .trim());

                        DoorClosedCallbackCustomization doorClosedCallbackCustomizationQuery = doorClosedCallbackCustomizationService
                                .findDoorClosedCallbackCustomizationByCustomerIdAndBoxId(
                                        customerId, boxId);
                        if (doorClosedCallbackCustomizationQuery != null) {
                            doorClosedCallbackCustomizationService
                                    .updateDoorClosedCallbackCustomization(doorClosedCallbackCustomization);
                        } else {
                            doorClosedCallbackCustomizationService
                                    .addDoorClosedCallbackCustomization(doorClosedCallbackCustomization);
                        }
                    }
                    if (orderCallbackUrl != null
                            && orderCallbackUrl.trim().length() > 0) {
                        ReplenishmentCallbackCustomizationService replenishmentCallbackCustomizationService = new ReplenishmentCallbackCustomizationService();
                        ReplenishmentCallbackCustomization replenishmentCallbackCustomization = new ReplenishmentCallbackCustomization();
                        replenishmentCallbackCustomization
                                .setCustomerId(customerId);
                        replenishmentCallbackCustomization.setBoxId(boxId);
                        replenishmentCallbackCustomization
                                .setReplenishmentCallbackUrl(orderCallbackUrl
                                        .trim());

                        ReplenishmentCallbackCustomization replenishmentCallbackCustomizationQuery = replenishmentCallbackCustomizationService
                                .findReplenishmentCallbackCustomizationByCustomerIdAndBoxId(
                                        customerId, boxId);
                        if (replenishmentCallbackCustomizationQuery != null) {
                            replenishmentCallbackCustomizationService
                                    .updateReplenishmentCallbackCustomization(replenishmentCallbackCustomization);
                        } else {
                            replenishmentCallbackCustomizationService
                                    .addReplenishmentCallbackCustomization(replenishmentCallbackCustomization);
                        }
                    }
                }
            }
            return ResponseStatus.SUCCESS;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        return ResponseStatus.FAIL;
    }

}
