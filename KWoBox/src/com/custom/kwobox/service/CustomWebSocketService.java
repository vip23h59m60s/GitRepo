package com.custom.kwobox.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.chinapalms.kwobox.javabean.Boxes;
import com.chinapalms.kwobox.javabean.Customer;
import com.chinapalms.kwobox.javabean.DoorClosedCallback;
import com.chinapalms.kwobox.javabean.DoorClosedCallbackCustomization;
import com.chinapalms.kwobox.javabean.DoorOpenedCallback;
import com.chinapalms.kwobox.javabean.DoorOpenedCallbackCustomization;
import com.chinapalms.kwobox.javabean.OrderCallback;
import com.chinapalms.kwobox.javabean.OrderCallbackCustomization;
import com.chinapalms.kwobox.javabean.ReplenishmentCallback;
import com.chinapalms.kwobox.javabean.ReplenishmentCallbackCustomization;
import com.chinapalms.kwobox.service.BoxStatusService;
import com.chinapalms.kwobox.service.BoxesService;
import com.chinapalms.kwobox.service.CustomerService;
import com.chinapalms.kwobox.service.DoorClosedCallbackCustomizationService;
import com.chinapalms.kwobox.service.DoorClosedCallbackService;
import com.chinapalms.kwobox.service.DoorOpenedCallbackCustomizationService;
import com.chinapalms.kwobox.service.DoorOpenedCallbackService;
import com.chinapalms.kwobox.service.OrderCallbackCustomizationService;
import com.chinapalms.kwobox.service.OrderCallbackService;
import com.chinapalms.kwobox.service.ReplenishmentCallbackCustomizationService;
import com.chinapalms.kwobox.service.ReplenishmentCallbackService;
import com.chinapalms.kwobox.utils.HttpUtil;
import com.chinapalms.kwobox.utils.ResponseStatus;
import com.custom.kwobox.utils.CustomCallbackSignatureParamsStructureUtil;
import com.custom.kwobox.utils.CustomConfig;

public class CustomWebSocketService {

    Log log = LogFactory.getLog(CustomWebSocketService.class);

    // 根据boxId获取售货机对应商户的商户合作模式
    public int getCooperationMode(String boxId) {
        int cooperationMode = -1;
        BoxesService boxesService = new BoxesService();
        Boxes boxes = boxesService.findBoxesByBoxId(boxId);
        if (boxes != null) {
            // 根据商户ID查询商户合作模式
            CustomerService customerService = new CustomerService();
            Customer customer = customerService.findCustomerByCustomerId(boxes
                    .getCustomerId());
            // 判断是否是小e服务器对接模式
            if (customer != null) {
                cooperationMode = customer.getCooperationMode();
            }
        }
        return cooperationMode;
    }

    // 获取门已开售货机的商户回调Url
    public String getCustomerDoorOpenedCallbackUrl(String boxId) {
        BoxesService boxesService = new BoxesService();
        Boxes boxes = boxesService.findBoxesByBoxId(boxId);
        if (boxes != null) {
            // 根据商户ID查询商户合作模式
            CustomerService customerService = new CustomerService();
            Customer customer = customerService.findCustomerByCustomerId(boxes
                    .getCustomerId());
            // 判断是否是小e服务器对接模式
            if (customer != null) {
                int cooperationMode = customer.getCooperationMode();
                // 先根据商户和售货机查询回调Url，再仅根据商户查询通用Url
                DoorOpenedCallbackCustomizationService doorOpenedCallbackCustomizationService = new DoorOpenedCallbackCustomizationService();
                DoorOpenedCallbackCustomization doorOpenedCallbackCustomization = doorOpenedCallbackCustomizationService
                        .findDoorOpenedCallbackCustomizationByCustomerIdAndBoxId(
                                boxes.getCustomerId(), boxId);
                if (doorOpenedCallbackCustomization != null) {
                    // 判断是否是小e服务器堆积或Yoho代理模式
                    if (cooperationMode == ResponseStatus.COOPERATIONMODE_THIRD_INTERFACE
                            || cooperationMode == ResponseStatus.COOPERATIONMODE_AGENT) {
                        // 如果是小e或Yoho模式将url和urlType成Json风格返回
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("urlType", cooperationMode);
                        jsonObject.put("url", doorOpenedCallbackCustomization
                                .getDoorOpenedCallbackUrl());
                        return jsonObject.toString();
                    } else {
                        // 如果不是小e或Yoho模式，直接返回FAIL
                        return ResponseStatus.FAIL;
                    }
                } else {
                    DoorOpenedCallbackService doorOpenedCallbackService = new DoorOpenedCallbackService();
                    DoorOpenedCallback doorOpenedCallback = doorOpenedCallbackService
                            .findDoorOpenedCallbackByCustomerId(boxes
                                    .getCustomerId());
                    if (doorOpenedCallback != null) {
                        // 判断是否是小e服务器堆积或Yoho代理模式
                        if (cooperationMode == ResponseStatus.COOPERATIONMODE_THIRD_INTERFACE
                                || cooperationMode == ResponseStatus.COOPERATIONMODE_AGENT) {
                            // 如果是小e或Yoho模式将url和urlType成Json风格返回
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("urlType", cooperationMode);
                            jsonObject.put("url", doorOpenedCallback
                                    .getDoorOpenedCallbackUrl());
                            return jsonObject.toString();
                        } else {
                            // 如果不是小e或Yoho模式，直接返回FAIL
                            return ResponseStatus.FAIL;
                        }
                    } else {
                        // 如果不是小e或Yoho模式，直接返回FAIL
                        return ResponseStatus.FAIL;
                    }
                }
                // 如果不是小e服务器对接模式，直接返回FAIL
            } else {
                return ResponseStatus.FAIL;
            }
        } else {
            return ResponseStatus.FAIL;
        }
    }

    // 获取门已关售货机的商户回调Url
    public String getCustomerDoorClosedCallbackUrl(String boxId) {
        BoxesService boxesService = new BoxesService();
        Boxes boxes = boxesService.findBoxesByBoxId(boxId);
        if (boxes != null) {
            // 根据商户ID查询商户合作模式
            CustomerService customerService = new CustomerService();
            Customer customer = customerService.findCustomerByCustomerId(boxes
                    .getCustomerId());
            if (customer != null) {
                int cooperationMode = customer.getCooperationMode();
                // 先根据商户和售货机查询回调Url，再仅根据商户查询通用Url
                DoorClosedCallbackCustomizationService doorClosedCallbackCustomizationService = new DoorClosedCallbackCustomizationService();
                DoorClosedCallbackCustomization doorClosedCallbackCustomization = doorClosedCallbackCustomizationService
                        .findDoorClosedCallbackCustomizationByCustomerIdAndBoxId(
                                boxes.getCustomerId(), boxId);
                if (doorClosedCallbackCustomization != null) {
                    // 判断是否是小e服务器堆积或Yoho代理模式
                    if (cooperationMode == ResponseStatus.COOPERATIONMODE_THIRD_INTERFACE
                            || cooperationMode == ResponseStatus.COOPERATIONMODE_AGENT) {
                        // 如果是小e或Yoho模式将url和urlType成Json风格返回
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("urlType", cooperationMode);
                        jsonObject.put("url", doorClosedCallbackCustomization
                                .getDoorClosedCallbackUrl());
                        return jsonObject.toString();
                    } else {
                        // 如果不是小e或Yoho模式，直接返回FAIL
                        return ResponseStatus.FAIL;
                    }
                } else {
                    DoorClosedCallbackService doorClosedCallbackService = new DoorClosedCallbackService();
                    DoorClosedCallback doorClosedCallback = doorClosedCallbackService
                            .findDoorClosedCallbackByCustomerId(boxes
                                    .getCustomerId());
                    if (doorClosedCallback != null) {
                        // 判断是否是小e服务器堆积或Yoho代理模式
                        if (cooperationMode == ResponseStatus.COOPERATIONMODE_THIRD_INTERFACE
                                || cooperationMode == ResponseStatus.COOPERATIONMODE_AGENT) {
                            // 如果是小e或Yoho模式将url和urlType成Json风格返回
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("urlType", cooperationMode);
                            jsonObject.put("url", doorClosedCallback
                                    .getDoorClosedCallbackUrl());
                            return jsonObject.toString();
                        } else {
                            // 如果不是小e或Yoho模式，直接返回FAIL
                            return ResponseStatus.FAIL;
                        }
                    } else {
                        // 如果不是小e或Yoho模式，直接返回FAIL
                        return ResponseStatus.FAIL;
                    }
                }
            } else {
                // 如果不是小e或Yoho模式，直接返回FAIL
                return ResponseStatus.FAIL;
            }
        } else {
            // 如果不是小e或Yoho模式，直接返回FAIL
            return ResponseStatus.FAIL;
        }
    }

    // 获取商户订单回调Url
    public String getCustomerOrderCallbackUrl(String boxId) {
        BoxesService boxesService = new BoxesService();
        Boxes boxes = boxesService.findBoxesByBoxId(boxId);
        if (boxes != null) {
            // 根据商户ID查询商户合作模式
            CustomerService customerService = new CustomerService();
            Customer customer = customerService.findCustomerByCustomerId(boxes
                    .getCustomerId());
            // 判断是否是小e服务器对接模式
            if (customer != null) {
                int cooperationMode = customer.getCooperationMode();
                // 先根据商户和售货机查询回调Url，再仅根据商户查询通用Url
                OrderCallbackCustomizationService orderCallbackCustomizationService = new OrderCallbackCustomizationService();
                OrderCallbackCustomization orderCallbackCustomization = orderCallbackCustomizationService
                        .findOrderCallbackCustomizationByCustomerIdAndBoxId(
                                boxes.getCustomerId(), boxId);
                if (orderCallbackCustomization != null) {
                    // 判断是否是小e服务器堆积或Yoho代理模式
                    if (cooperationMode == ResponseStatus.COOPERATIONMODE_THIRD_INTERFACE
                            || cooperationMode == ResponseStatus.COOPERATIONMODE_AGENT) {
                        // 如果是小e或Yoho模式将url和urlType成Json风格返回
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("urlType", cooperationMode);
                        jsonObject.put("url", orderCallbackCustomization
                                .getOrderCallbackUrl());
                        return jsonObject.toString();
                    } else {
                        // 如果不是小e或Yoho模式，直接返回FAIL
                        return ResponseStatus.FAIL;
                    }
                } else {
                    OrderCallbackService orderCallbackService = new OrderCallbackService();
                    OrderCallback orderCallback = orderCallbackService
                            .findOrderCallbackByCustomerId(boxes
                                    .getCustomerId());
                    if (orderCallback != null) {
                        // 判断是否是小e服务器堆积或Yoho代理模式
                        if (cooperationMode == ResponseStatus.COOPERATIONMODE_THIRD_INTERFACE
                                || cooperationMode == ResponseStatus.COOPERATIONMODE_AGENT) {
                            // 如果是小e或Yoho模式将url和urlType成Json风格返回
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("urlType", cooperationMode);
                            jsonObject.put("url",
                                    orderCallback.getOrderCallbackUrl());
                            return jsonObject.toString();
                        } else {
                            // 如果不是小e或Yoho模式，直接返回FAIL
                            return ResponseStatus.FAIL;
                        }
                    } else {
                        // 如果不是小e或Yoho模式，直接返回FAIL
                        return ResponseStatus.FAIL;
                    }
                }
            } else {
                // 判断是否是小e服务器对接模式
                return ResponseStatus.FAIL;
            }
        } else {
            // 如果不是小e或Yoho模式，直接返回FAIL
            return ResponseStatus.FAIL;
        }
    }

    // 获取理货单商户回调Url
    public String getCustomerReplenishmentCallbackUrl(String boxId) {
        BoxesService boxesService = new BoxesService();
        Boxes boxes = boxesService.findBoxesByBoxId(boxId);
        if (boxes != null) {
            // 根据商户ID查询商户合作模式
            CustomerService customerService = new CustomerService();
            Customer customer = customerService.findCustomerByCustomerId(boxes
                    .getCustomerId());
            // 判断是否是小e服务器对接模式
            if (customer != null) {
                int cooperationMode = customer.getCooperationMode();
                // 先根据商户和售货机查询回调Url，再仅根据商户查询通用Url
                ReplenishmentCallbackCustomizationService replenishmentCallbackCustomizationService = new ReplenishmentCallbackCustomizationService();
                ReplenishmentCallbackCustomization replenishmentCallbackCustomization = replenishmentCallbackCustomizationService
                        .findReplenishmentCallbackCustomizationByCustomerIdAndBoxId(
                                boxes.getCustomerId(), boxId);
                if (replenishmentCallbackCustomization != null) {
                    // 判断是否是小e服务器堆积或Yoho代理模式
                    if (cooperationMode == ResponseStatus.COOPERATIONMODE_THIRD_INTERFACE
                            || cooperationMode == ResponseStatus.COOPERATIONMODE_AGENT) {
                        // 如果是小e或Yoho模式将url和urlType成Json风格返回
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("urlType", cooperationMode);
                        jsonObject.put("url",
                                replenishmentCallbackCustomization
                                        .getReplenishmentCallbackUrl());
                        return jsonObject.toString();
                    } else {
                        // 如果不是小e或Yoho模式，直接返回FAIL
                        return ResponseStatus.FAIL;
                    }
                } else {
                    ReplenishmentCallbackService replenishmentCallbackService = new ReplenishmentCallbackService();
                    ReplenishmentCallback replenishmentCallback = replenishmentCallbackService
                            .findReplenishmentCallbackByCustomerId(boxes
                                    .getCustomerId());
                    if (replenishmentCallback != null) {
                        // 判断是否是小e服务器堆积或Yoho代理模式
                        if (cooperationMode == ResponseStatus.COOPERATIONMODE_THIRD_INTERFACE
                                || cooperationMode == ResponseStatus.COOPERATIONMODE_AGENT) {
                            // 如果是小e或Yoho模式将url和urlType成Json风格返回
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("urlType", cooperationMode);
                            jsonObject.put("url", replenishmentCallback
                                    .getReplenishmentCallbackUrl());
                            return jsonObject.toString();
                        } else {
                            // 如果不是小e或Yoho模式，直接返回FAIL
                            return ResponseStatus.FAIL;
                        }
                    } else {
                        // 如果不是小e或Yoho模式，直接返回FAIL
                        return ResponseStatus.FAIL;
                    }
                }
            } else {
                // 判断是否是小e服务器对接模式
                return ResponseStatus.FAIL;
            }
        } else {
            // 如果不是小e或Yoho模式，直接返回FAIL
            return ResponseStatus.FAIL;
        }
    }

    // 回调商户服务器：发送门已开回调信息(普通消费者)
    public void notifyCustomServerDoorOpened(String doorOpenedUrlObject,
            String boxId, String phoneNumber) throws Exception {
        CustomResponseService customResponseService = new CustomResponseService();
        JSONObject resultMsgJsonObject = new JSONObject();
        resultMsgJsonObject.put("boxId", boxId);
        resultMsgJsonObject.put("userType", 0);
        resultMsgJsonObject.put("phoneNumber", phoneNumber);
        String responseToCustom = customResponseService
                .getReturnSuccessAndResultSuccessResponse(resultMsgJsonObject
                        .toString());
        // 判断是否要拼接上客户签名相关字串
        BoxesService boxesService = new BoxesService();
        Boxes boxes = boxesService.findBoxesByBoxId(boxId);
        if (boxes != null) {
            // 根据商户ID查询商户合作模式
            CustomerService customerService = new CustomerService();
            Customer customer = customerService.findCustomerByCustomerId(boxes
                    .getCustomerId());
            String mchId = customer.getMchId();
            if (mchId != null) {
                // 小e微店
                if (mchId.equals(CustomConfig.XIAOE_MCHID)
                        || mchId.equals(CustomConfig.XIAOE_TEST_MCHID)) {
                    Map<String, Object> signParamMap = CustomCallbackSignatureParamsStructureUtil
                            .getDoorOpenedCallbackSignatureParamsMap(boxId,
                                    ResponseStatus.PERMISSION_NORMAL_USER,
                                    phoneNumber, mchId);
                    if (signParamMap != null) {
                        JSONObject responseToCustomJsonObject = JSONObject
                                .fromObject(responseToCustom);
                        for (Map.Entry<String, Object> entry : signParamMap
                                .entrySet()) {
                            responseToCustomJsonObject.put(entry.getKey(),
                                    entry.getValue());
                            log.info("notifyCustomServerDoorOpened:->paras for sign"
                                    + "Key: "
                                    + entry.getKey()
                                    + " Value: "
                                    + entry.getValue());
                        }
                        responseToCustom = responseToCustomJsonObject
                                .toString();
                        log.info("notifyCustomServerDoorOpened:->responseTocustom="
                                + responseToCustom);
                    }
                }
            }
        }
        String doorOpenedUrl = JSONObject.fromObject(doorOpenedUrlObject)
                .getString("url");
        BoxStatusService boxStatusService = new BoxStatusService();
        if (boxStatusService.setOpenDoorState(boxId, true).equals(
                ResponseStatus.SUCCESS)) {
            HttpUtil.sendCallbackResultMsg(doorOpenedUrl,
                    JSONObject.fromObject(responseToCustom));
        }
    }

    // 回调商户服务器：发送门已开回调信息(理货员)
    public void notifyCustomServerDoorOpenedForManager(
            String doorOpenedUrlObject, String boxId, int customerWorkerId)
            throws Exception {
        CustomResponseService customResponseService = new CustomResponseService();
        JSONObject resultMsgJsonObject = new JSONObject();
        resultMsgJsonObject.put("boxId", boxId);
        resultMsgJsonObject.put("userType", 1);
        resultMsgJsonObject.put("workerId", customerWorkerId);
        String responseToCustom = customResponseService
                .getReturnSuccessAndResultSuccessResponse(resultMsgJsonObject
                        .toString());
        // 判断是否要拼接上客户签名相关字串
        BoxesService boxesService = new BoxesService();
        Boxes boxes = boxesService.findBoxesByBoxId(boxId);
        if (boxes != null) {
            // 根据商户ID查询商户合作模式
            CustomerService customerService = new CustomerService();
            Customer customer = customerService.findCustomerByCustomerId(boxes
                    .getCustomerId());
            String mchId = customer.getMchId();
            if (mchId != null) {
                // 小e微店
                if (mchId.equals(CustomConfig.XIAOE_MCHID)
                        || mchId.equals(CustomConfig.XIAOE_TEST_MCHID)) {
                    Map<String, Object> signParamMap = CustomCallbackSignatureParamsStructureUtil
                            .getDoorOpenedCallbackSignatureParamsMap(boxId,
                                    ResponseStatus.PERMISSION_MANAGER,
                                    String.valueOf(customerWorkerId), mchId);
                    if (signParamMap != null) {
                        JSONObject responseToCustomJsonObject = JSONObject
                                .fromObject(responseToCustom);
                        for (Map.Entry<String, Object> entry : signParamMap
                                .entrySet()) {
                            responseToCustomJsonObject.put(entry.getKey(),
                                    entry.getValue());
                            log.info("notifyCustomServerDoorOpened for manager:->paras for sign"
                                    + "Key: "
                                    + entry.getKey()
                                    + " Value: "
                                    + entry.getValue());
                        }
                        responseToCustom = responseToCustomJsonObject
                                .toString();
                        log.info("notifyCustomServerDoorOpened for manager:->responseTocustom="
                                + responseToCustom);
                    }
                }
            }
        }
        String doorOpenedUrl = JSONObject.fromObject(doorOpenedUrlObject)
                .getString("url");
        BoxStatusService boxStatusService = new BoxStatusService();
        if (boxStatusService.setOpenDoorState(boxId, true).equals(
                ResponseStatus.SUCCESS)) {
            HttpUtil.sendCallbackResultMsg(doorOpenedUrl,
                    JSONObject.fromObject(responseToCustom));
        }
    }

    // 回调商户服务器：发送门已关回调信息(普通消费者)
    public void notifyCustomServerDoorClosed(String doorClosedUrlObject,
            String boxId, String phoneNumber) {
        CustomResponseService customResponseService = new CustomResponseService();
        JSONObject resultMsgJsonObject = new JSONObject();
        resultMsgJsonObject.put("boxId", boxId);
        resultMsgJsonObject.put("userType", 0);
        resultMsgJsonObject.put("phoneNumber", phoneNumber);
        String responseToCustom = customResponseService
                .getReturnSuccessAndResultSuccessResponse(resultMsgJsonObject
                        .toString());
        // 判断是否要拼接上客户签名相关字串
        BoxesService boxesService = new BoxesService();
        Boxes boxes = boxesService.findBoxesByBoxId(boxId);
        if (boxes != null) {
            // 根据商户ID查询商户合作模式
            CustomerService customerService = new CustomerService();
            Customer customer = customerService.findCustomerByCustomerId(boxes
                    .getCustomerId());
            String mchId = customer.getMchId();
            if (mchId != null) {
                // 小e微店
                if (mchId.equals(CustomConfig.XIAOE_MCHID)
                        || mchId.equals(CustomConfig.XIAOE_TEST_MCHID)) {
                    Map<String, Object> signParamMap = CustomCallbackSignatureParamsStructureUtil
                            .getDoorClosedCallbackSignatureParamsMap(boxId,
                                    ResponseStatus.PERMISSION_NORMAL_USER,
                                    phoneNumber, mchId);
                    if (signParamMap != null) {
                        JSONObject responseToCustomJsonObject = JSONObject
                                .fromObject(responseToCustom);
                        for (Map.Entry<String, Object> entry : signParamMap
                                .entrySet()) {
                            responseToCustomJsonObject.put(entry.getKey(),
                                    entry.getValue());

                            log.info("notifyCustomServerDoorClosed:->paras for sign"
                                    + "Key: "
                                    + entry.getKey()
                                    + " Value: "
                                    + entry.getValue());
                        }
                        responseToCustom = responseToCustomJsonObject
                                .toString();
                        log.info("notifyCustomServerDoorClosed:->responseTocustom="
                                + responseToCustom);
                    }
                }
            }
        }
        String doorClosedUrl = JSONObject.fromObject(doorClosedUrlObject)
                .getString("url");
        HttpUtil.sendCallbackResultMsg(doorClosedUrl,
                JSONObject.fromObject(responseToCustom));
    }

    // 回调商户服务器：发送门已关回调信息(理货员)
    public void notifyCustomServerDoorClosedForManager(
            String doorClosedUrlObject, String boxId, int customerWorkerId) {
        CustomResponseService customResponseService = new CustomResponseService();
        JSONObject resultMsgJsonObject = new JSONObject();
        resultMsgJsonObject.put("boxId", boxId);
        resultMsgJsonObject.put("userType", 1);
        resultMsgJsonObject.put("workerId", customerWorkerId);
        String responseToCustom = customResponseService
                .getReturnSuccessAndResultSuccessResponse(resultMsgJsonObject
                        .toString());
        // 判断是否要拼接上客户签名相关字串
        BoxesService boxesService = new BoxesService();
        Boxes boxes = boxesService.findBoxesByBoxId(boxId);
        if (boxes != null) {
            // 根据商户ID查询商户合作模式
            CustomerService customerService = new CustomerService();
            Customer customer = customerService.findCustomerByCustomerId(boxes
                    .getCustomerId());
            String mchId = customer.getMchId();
            if (mchId != null) {
                // 小e微店
                if (mchId.equals(CustomConfig.XIAOE_MCHID)
                        || mchId.equals(CustomConfig.XIAOE_TEST_MCHID)) {
                    Map<String, Object> signParamMap = CustomCallbackSignatureParamsStructureUtil
                            .getDoorClosedCallbackSignatureParamsMap(boxId,
                                    ResponseStatus.PERMISSION_MANAGER,
                                    String.valueOf(customerWorkerId), mchId);
                    if (signParamMap != null) {
                        JSONObject responseToCustomJsonObject = JSONObject
                                .fromObject(responseToCustom);
                        for (Map.Entry<String, Object> entry : signParamMap
                                .entrySet()) {
                            responseToCustomJsonObject.put(entry.getKey(),
                                    entry.getValue());

                            log.info("notifyCustomServerDoorClosed for manager:->paras for sign"
                                    + "Key: "
                                    + entry.getKey()
                                    + " Value: "
                                    + entry.getValue());
                        }
                        responseToCustom = responseToCustomJsonObject
                                .toString();
                        log.info("notifyCustomServerDoorClosed for manager:->responseTocustom="
                                + responseToCustom);
                    }
                }
            }
        }
        String doorClosedUrl = JSONObject.fromObject(doorClosedUrlObject)
                .getString("url");
        HttpUtil.sendCallbackResultMsg(doorClosedUrl,
                JSONObject.fromObject(responseToCustom));
    }

    // 回调商户服务器：发送有购物订单orderType=1表示有购物订单
    public void notifyCustomServerWhatGoodsSaled(String orderCallbackUrlObject,
            String boxId, String phoneNumber,
            String openDoorRequestSerialNumber, JSONObject orderJsonObject) {
        CustomResponseService customResponseService = new CustomResponseService();
        JSONObject orderJsonObjectNew = new JSONObject();
        JSONObject orderBeanJsonObjectNew = new JSONObject();
        JSONObject orderBeanJsonObject = orderJsonObject.getJSONObject("Order");
        orderBeanJsonObjectNew.put("orderType", 1);
        orderBeanJsonObjectNew.put("orderId",
                orderBeanJsonObject.get("orderId"));
        orderBeanJsonObjectNew.put("orderDate",
                orderBeanJsonObject.get("orderDate"));
        orderBeanJsonObjectNew.put("phoneNumber", phoneNumber);
        orderBeanJsonObjectNew.put("boxId", boxId);
        orderBeanJsonObjectNew.put("goodsTotalNumber",
                orderBeanJsonObject.get("goodsTotalNumber"));
        orderBeanJsonObjectNew.put("payTotal",
                orderBeanJsonObject.get("payTotal"));
        orderBeanJsonObjectNew.put("totalFavourable",
                orderBeanJsonObject.get("totalFavourable"));
        orderBeanJsonObjectNew.put("actualPayTotal",
                orderBeanJsonObject.get("actualPayTotal"));
        orderBeanJsonObjectNew.put("Goods",
                orderBeanJsonObject.getJSONArray("Goods"));
        orderJsonObjectNew.put("Order", orderBeanJsonObjectNew);
        String responseToCustom = customResponseService
                .getReturnSuccessAndResultSuccessResponse(orderJsonObjectNew
                        .toString());
        // 判断是否要拼接上客户签名相关字串
        BoxesService boxesService = new BoxesService();
        Boxes boxes = boxesService.findBoxesByBoxId(boxId);
        if (boxes != null) {
            // 根据商户ID查询商户合作模式
            CustomerService customerService = new CustomerService();
            Customer customer = customerService.findCustomerByCustomerId(boxes
                    .getCustomerId());
            String mchId = customer.getMchId();
            if (mchId != null) {
                // 小e微店
                if (mchId.equals(CustomConfig.XIAOE_MCHID)
                        || mchId.equals(CustomConfig.XIAOE_TEST_MCHID)) {
                    Map<String, Object> signParamMap = CustomCallbackSignatureParamsStructureUtil
                            .getOrderCallbackSignatureParamsMap(boxId,
                                    phoneNumber, openDoorRequestSerialNumber,
                                    mchId);
                    if (signParamMap != null) {
                        JSONObject responseToCustomJsonObject = JSONObject
                                .fromObject(responseToCustom);
                        for (Map.Entry<String, Object> entry : signParamMap
                                .entrySet()) {
                            responseToCustomJsonObject.put(entry.getKey(),
                                    entry.getValue());
                            log.info("notifyCustomServerWhatGoodsSaled:->paras for sign"
                                    + "Key: "
                                    + entry.getKey()
                                    + " Value: "
                                    + entry.getValue());
                        }
                        responseToCustom = responseToCustomJsonObject
                                .toString();
                        log.info("notifyCustomServerWhatGoodsSaled:->responseTocustom="
                                + responseToCustom);
                    }
                }
            }
        }
        String orderCallbackUrl = JSONObject.fromObject(orderCallbackUrlObject)
                .getString("url");
        HttpUtil.sendCallbackResultMsg(orderCallbackUrl,
                JSONObject.fromObject(responseToCustom));
    }

    // 回调商户服务器：修改订单（修改后为有购物订单）orderType=2表示有购物订单
    public void notifyCustomServerUpdateWhatGoodsSaled(String oldOrderId,
            String orderCallbackUrlObject, String boxId, String phoneNumber,
            JSONObject orderJsonObject) {
        CustomResponseService customResponseService = new CustomResponseService();
        JSONObject orderJsonObjectNew = new JSONObject();
        JSONObject orderBeanJsonObjectNew = new JSONObject();
        JSONObject orderBeanJsonObject = orderJsonObject.getJSONObject("Order");
        orderBeanJsonObjectNew.put("orderType", 2);
        orderBeanJsonObjectNew.put("oldOrderId", oldOrderId);
        orderBeanJsonObjectNew.put("orderId",
                orderBeanJsonObject.get("orderId"));
        orderBeanJsonObjectNew.put("orderDate",
                orderBeanJsonObject.get("orderDate"));
        orderBeanJsonObjectNew.put("phoneNumber", phoneNumber);
        orderBeanJsonObjectNew.put("boxId", boxId);
        orderBeanJsonObjectNew.put("goodsTotalNumber",
                orderBeanJsonObject.get("goodsTotalNumber"));
        orderBeanJsonObjectNew.put("payTotal",
                orderBeanJsonObject.get("payTotal"));
        orderBeanJsonObjectNew.put("totalFavourable",
                orderBeanJsonObject.get("totalFavourable"));
        orderBeanJsonObjectNew.put("actualPayTotal",
                orderBeanJsonObject.get("actualPayTotal"));
        orderBeanJsonObjectNew.put("Goods",
                orderBeanJsonObject.getJSONArray("Goods"));
        orderJsonObjectNew.put("Order", orderBeanJsonObjectNew);
        String responseToCustom = customResponseService
                .getReturnSuccessAndResultSuccessResponse(orderJsonObjectNew
                        .toString());
        // 判断是否要拼接上客户签名相关字串
        BoxesService boxesService = new BoxesService();
        Boxes boxes = boxesService.findBoxesByBoxId(boxId);
        if (boxes != null) {
            // 根据商户ID查询商户合作模式
            CustomerService customerService = new CustomerService();
            Customer customer = customerService.findCustomerByCustomerId(boxes
                    .getCustomerId());
            String mchId = customer.getMchId();
            // if (mchId != null) {
            // // 小e微店
            // if (mchId.equals(CustomConfig.XIAOE_MCHID)
            // || mchId.equals(CustomConfig.XIAOE_TEST_MCHID)) {
            // Map<String, Object> signParamMap =
            // CustomCallbackSignatureParamsStructureUtil
            // .getOrderCallbackSignatureParamsMap(boxId,
            // phoneNumber, mchId);
            // if (signParamMap != null) {
            // JSONObject responseToCustomJsonObject = JSONObject
            // .fromObject(responseToCustom);
            // for (Map.Entry<String, Object> entry : signParamMap
            // .entrySet()) {
            // responseToCustomJsonObject.put(entry.getKey(),
            // entry.getValue());
            // log.info("notifyCustomServerUpdateWhatGoodsSaled:->paras for sign"
            // + "Key: "
            // + entry.getKey()
            // + " Value: "
            // + entry.getValue());
            // }
            // responseToCustom = responseToCustomJsonObject
            // .toString();
            // log.info("notifyCustomServerUpdateWhatGoodsSaled:->responseTocustom="
            // + responseToCustom);
            // }
            // }
            // }
        }
        String orderCallbackUrl = JSONObject.fromObject(orderCallbackUrlObject)
                .getString("url");
        HttpUtil.sendCallbackResultMsg(orderCallbackUrl,
                JSONObject.fromObject(responseToCustom));
    }

    // 回调商户服务器： 发送无购物订单orderType=0表示无购物订单
    public void notifyCustomServerNoGoodsSaled(String orderCallbackUrlObject,
            String orderId, String boxId, String phoneNumber,
            String openDoorRequestSerialNumber,
            JSONObject noGoodsOrderJsonObject) {
        CustomResponseService customResponseService = new CustomResponseService();
        JSONObject noGoodsOrderJsonObjectNew = new JSONObject();
        JSONObject noGoodsOrderBeanJsonObjectNew = new JSONObject();
        JSONObject noGoodsOrderBeanJsonObject = noGoodsOrderJsonObject
                .getJSONObject("Order");
        noGoodsOrderBeanJsonObjectNew.put("orderType", 0);
        noGoodsOrderBeanJsonObjectNew.put("phoneNumber", phoneNumber);
        noGoodsOrderBeanJsonObjectNew.put("orderId", orderId);
        noGoodsOrderBeanJsonObjectNew.put("boxId", boxId);
        noGoodsOrderBeanJsonObjectNew.put("Goods",
                noGoodsOrderBeanJsonObject.get("Goods"));
        noGoodsOrderJsonObjectNew.put("Order", noGoodsOrderBeanJsonObjectNew);
        String responseToCustom = customResponseService
                .getReturnSuccessAndResultSuccessResponse(noGoodsOrderJsonObjectNew
                        .toString());
        // 判断是否要拼接上客户签名相关字串
        BoxesService boxesService = new BoxesService();
        Boxes boxes = boxesService.findBoxesByBoxId(boxId);
        if (boxes != null) {
            // 根据商户ID查询商户合作模式
            CustomerService customerService = new CustomerService();
            Customer customer = customerService.findCustomerByCustomerId(boxes
                    .getCustomerId());
            String mchId = customer.getMchId();
            if (mchId != null) {
                // 小e微店
                if (mchId.equals(CustomConfig.XIAOE_MCHID)
                        || mchId.equals(CustomConfig.XIAOE_TEST_MCHID)) {
                    Map<String, Object> signParamMap = CustomCallbackSignatureParamsStructureUtil
                            .getOrderCallbackSignatureParamsMap(boxId,
                                    phoneNumber, openDoorRequestSerialNumber,
                                    mchId);
                    if (signParamMap != null) {
                        JSONObject responseToCustomJsonObject = JSONObject
                                .fromObject(responseToCustom);
                        for (Map.Entry<String, Object> entry : signParamMap
                                .entrySet()) {
                            responseToCustomJsonObject.put(entry.getKey(),
                                    entry.getValue());
                            log.info("notifyCustomServerWhatGoodsSaled:->paras for sign"
                                    + "Key: "
                                    + entry.getKey()
                                    + " Value: "
                                    + entry.getValue());
                        }
                        responseToCustom = responseToCustomJsonObject
                                .toString();
                        log.info("notifyCustomServerWhatGoodsSaled:->responseTocustom="
                                + responseToCustom);
                    }
                }
            }
        }
        String orderCallbackUrl = JSONObject.fromObject(orderCallbackUrlObject)
                .getString("url");
        HttpUtil.sendCallbackResultMsg(orderCallbackUrl,
                JSONObject.fromObject(responseToCustom));
    }

    // 回调商户服务器：修改订单（修改后为无购物订单）orderType=3表示修改后订单为无购物订单
    public void notifyCustomServerUpdateNoGoodsSaled(String oldOrderId,
            String orderId, String orderCallbackUrlObject, String boxId,
            String phoneNumber, JSONObject noGoodsOrderJsonObject) {
        CustomResponseService customResponseService = new CustomResponseService();
        JSONObject noGoodsOrderJsonObjectNew = new JSONObject();
        JSONObject noGoodsOrderBeanJsonObjectNew = new JSONObject();
        JSONObject noGoodsOrderBeanJsonObject = noGoodsOrderJsonObject
                .getJSONObject("Order");
        noGoodsOrderBeanJsonObjectNew.put("orderType", 3);
        noGoodsOrderBeanJsonObjectNew.put("phoneNumber", phoneNumber);
        noGoodsOrderBeanJsonObjectNew.put("oldOrderId", oldOrderId);
        noGoodsOrderBeanJsonObjectNew.put("orderId", orderId);
        noGoodsOrderBeanJsonObjectNew.put("boxId", boxId);
        noGoodsOrderBeanJsonObjectNew.put("Goods",
                noGoodsOrderBeanJsonObject.get("Goods"));
        noGoodsOrderJsonObjectNew.put("Order", noGoodsOrderBeanJsonObjectNew);
        String responseToCustom = customResponseService
                .getReturnSuccessAndResultSuccessResponse(noGoodsOrderJsonObjectNew
                        .toString());
        // 判断是否要拼接上客户签名相关字串
        BoxesService boxesService = new BoxesService();
        Boxes boxes = boxesService.findBoxesByBoxId(boxId);
        if (boxes != null) {
            // 根据商户ID查询商户合作模式
            CustomerService customerService = new CustomerService();
            Customer customer = customerService.findCustomerByCustomerId(boxes
                    .getCustomerId());
            String mchId = customer.getMchId();
            if (mchId != null) {
                // 小e微店
                // if (mchId.equals(CustomConfig.XIAOE_MCHID)
                // || mchId.equals(CustomConfig.XIAOE_TEST_MCHID)) {
                // Map<String, Object> signParamMap =
                // CustomCallbackSignatureParamsStructureUtil
                // .getOrderCallbackSignatureParamsMap(boxId,
                // phoneNumber, mchId);
                // if (signParamMap != null) {
                // JSONObject responseToCustomJsonObject = JSONObject
                // .fromObject(responseToCustom);
                // for (Map.Entry<String, Object> entry : signParamMap
                // .entrySet()) {
                // responseToCustomJsonObject.put(entry.getKey(),
                // entry.getValue());
                // log.info("notifyCustomServerUpdateNoGoodsSaled:->paras for sign"
                // + "Key: "
                // + entry.getKey()
                // + " Value: "
                // + entry.getValue());
                // }
                // responseToCustom = responseToCustomJsonObject
                // .toString();
                // log.info("notifyCustomServerUpdateNoGoodsSaled:->responseTocustom="
                // + responseToCustom);
                // }
                // }
            }
        }
        String orderCallbackUrl = JSONObject.fromObject(orderCallbackUrlObject)
                .getString("url");
        HttpUtil.sendCallbackResultMsg(orderCallbackUrl,
                JSONObject.fromObject(responseToCustom));
    }

    /**
     * 针对Yoho合作模式，直接将售出商品信息发送至商户服务器以便生成订单
     */
    // whichCustomToSendMsg,
    // boxId,
    // ResponseStatus.BOX_TYPE_HIGH_FREQUENCY_RFID,
    // goodsIdsJsonList, true
    // saledGoodsType 标记订单类型：为正常订单还是人工处理后发出的订单
    public void notifyCustomerServerWhatGoodsSaled(String saledGoodsType,
            String orderCallbackUrlObject, JSONObject orderJsonObject,
            Date orderDate, String boxId, String phoneNumber,
            boolean addPersonCreditOrPointsRecord) {
        CustomResponseService customResponseService = new CustomResponseService();
        JSONObject saledGoodsJsonObject = new JSONObject();
        JSONObject saledGoodsJsonObjectBean = new JSONObject();
        saledGoodsJsonObjectBean.put("saledGoodsType", saledGoodsType);
        saledGoodsJsonObjectBean.put("orderType", 1);
        saledGoodsJsonObjectBean.put("boxId", boxId);
        saledGoodsJsonObjectBean.put("phoneNumber", phoneNumber);
        saledGoodsJsonObjectBean.put("orderDate", new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss").format(orderDate));
        saledGoodsJsonObjectBean.put("orderJsonObject", orderJsonObject);
        saledGoodsJsonObjectBean.put("addPersonCreditOrPointsRecord",
                addPersonCreditOrPointsRecord);
        saledGoodsJsonObject.put("Order", saledGoodsJsonObjectBean);
        String responseToCustom = customResponseService
                .getReturnSuccessAndResultSuccessResponse(saledGoodsJsonObject
                        .toString());
        String orderCallbackUrl = JSONObject.fromObject(orderCallbackUrlObject)
                .getString("url");
        HttpUtil.sendCallbackResultMsg(orderCallbackUrl,
                JSONObject.fromObject(responseToCustom));
    }

    /**
     * 针对有惑合作模式通知商户服务器退款并更新订单 oldOrderId 为需要更新的原来的订单号
     * 
     * @param orderCallbackUrlObject
     * @param orderJsonObject
     * @param orderDate
     * @param boxId
     * @param phoneNumber
     * @param addPersonCreditOrPointsRecord
     */
    public void notifyCustomerServerUpdateWhatGoodsSaled(String oldOrderId,
            String orderCallbackUrlObject, JSONObject orderJsonObject,
            String resolverOwner, Date orderDate, String boxId) {
        CustomResponseService customResponseService = new CustomResponseService();
        JSONObject saledGoodsJsonObject = new JSONObject();
        JSONObject saledGoodsJsonObjectBean = new JSONObject();
        saledGoodsJsonObjectBean.put("orderType", 2);
        saledGoodsJsonObjectBean.put("oldOrderId", oldOrderId);
        saledGoodsJsonObjectBean.put("boxId", boxId);
        saledGoodsJsonObjectBean.put("resolverOwner", resolverOwner);
        saledGoodsJsonObjectBean.put("orderDate", new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss").format(orderDate));
        saledGoodsJsonObjectBean.put("orderJsonObject", orderJsonObject);
        saledGoodsJsonObject.put("Order", saledGoodsJsonObjectBean);
        String responseToCustom = customResponseService
                .getReturnSuccessAndResultSuccessResponse(saledGoodsJsonObject
                        .toString());
        String orderCallbackUrl = JSONObject.fromObject(orderCallbackUrlObject)
                .getString("url");
        HttpUtil.sendCallbackResultMsg(orderCallbackUrl,
                JSONObject.fromObject(responseToCustom));
    }

    /**
     * 针对Yoho合作模式，直接将售出商品信息发送至商户服务器以便生成订单
     */
    // saledGoodsType 标记订单类型：为正常订单还是人工处理后发出的订单
    public void notifyCustomerServerNoGoodsSaled(String saledGoodsType,
            String orderId, Date orderDate, String orderCallbackUrlObject,
            String boxId, String phoneNumber) {
        CustomResponseService customResponseService = new CustomResponseService();
        JSONObject noGoodsSaledGoodsJsonObject = new JSONObject();
        JSONObject noGoodsSaledJsonObjectBean = new JSONObject();
        noGoodsSaledJsonObjectBean.put("saledGoodsType", saledGoodsType);
        noGoodsSaledJsonObjectBean.put("orderType", 0);
        noGoodsSaledJsonObjectBean.put("orderId", orderId);
        noGoodsSaledJsonObjectBean.put("orderDate", new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss").format(orderDate));
        noGoodsSaledJsonObjectBean.put("boxId", boxId);
        noGoodsSaledJsonObjectBean.put("phoneNumber", phoneNumber);
        noGoodsSaledJsonObjectBean.put("orderJsonObject", "noGoodsObjects");
        noGoodsSaledGoodsJsonObject.put("Order", noGoodsSaledJsonObjectBean);
        String responseToCustom = customResponseService
                .getReturnSuccessAndResultSuccessResponse(noGoodsSaledGoodsJsonObject
                        .toString());
        String orderCallbackUrl = JSONObject.fromObject(orderCallbackUrlObject)
                .getString("url");
        HttpUtil.sendCallbackResultMsg(orderCallbackUrl,
                JSONObject.fromObject(responseToCustom));
    }

    /**
     * 针对Yoho合作模式，修改订单并退款，订单有中生无
     * 
     * @param oldOrderId需要修改的订单ID
     *            ，orderId新的订单ID
     * @param orderCallbackUrlObject
     * @param boxId
     * @param phoneNumber
     */
    public void notifyCustomerServerUpdateNoGoodsSaled(String oldOrderId,
            String orderId, String orderCallbackUrlObject, Date orderDate,
            String boxId, String resolverOwner) {
        CustomResponseService customResponseService = new CustomResponseService();
        JSONObject noGoodsSaledGoodsJsonObject = new JSONObject();
        JSONObject noGoodsSaledJsonObjectBean = new JSONObject();
        noGoodsSaledJsonObjectBean.put("orderType", 3);
        noGoodsSaledJsonObjectBean.put("oldOrderId", oldOrderId);
        noGoodsSaledJsonObjectBean.put("orderId", orderId);
        noGoodsSaledJsonObjectBean.put("boxId", boxId);
        noGoodsSaledJsonObjectBean.put("orderJsonObject", "noGoodsObjects");
        noGoodsSaledJsonObjectBean.put("resolverOwner", resolverOwner);
        noGoodsSaledJsonObjectBean.put("orderDate", new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss").format(orderDate));
        noGoodsSaledGoodsJsonObject.put("Order", noGoodsSaledJsonObjectBean);
        String responseToCustom = customResponseService
                .getReturnSuccessAndResultSuccessResponse(noGoodsSaledGoodsJsonObject
                        .toString());
        String orderCallbackUrl = JSONObject.fromObject(orderCallbackUrlObject)
                .getString("url");
        HttpUtil.sendCallbackResultMsg(orderCallbackUrl,
                JSONObject.fromObject(responseToCustom));
    }

    /**
     * 针对Yoho合作模式，将异常客户端处理的异常信息回调至商户服务器进行数据库更新和通知商户微信客户端
     */
    public void notifyCustomerServerExceptionShoppingInfo(
            String orderCallbackUrlObject, String boxId, String phoneNumber,
            int personalScores) {
        CustomResponseService customResponseService = new CustomResponseService();
        JSONObject exceptionShoppingInfoJsonObject = new JSONObject();
        JSONObject exceptionShoppingInfoJsonObjectBean = new JSONObject();
        // orderType=-1代表回调只是异常信息
        exceptionShoppingInfoJsonObjectBean.put("orderType", -1);
        exceptionShoppingInfoJsonObjectBean.put("boxId", boxId);
        exceptionShoppingInfoJsonObjectBean.put("phoneNumber", phoneNumber);
        exceptionShoppingInfoJsonObjectBean.put("personalScores",
                personalScores);
        exceptionShoppingInfoJsonObject.put("Order",
                exceptionShoppingInfoJsonObjectBean);
        String responseToCustom = customResponseService
                .getReturnSuccessAndResultSuccessResponse(exceptionShoppingInfoJsonObject
                        .toString());
        String orderCallbackUrl = JSONObject.fromObject(orderCallbackUrlObject)
                .getString("url");
        HttpUtil.sendCallbackResultMsg(orderCallbackUrl,
                JSONObject.fromObject(responseToCustom));
    }

    /**
     * 小e模式 回调给小e有上下货理货清单(type=1)
     * 
     * @param replenishmentCallbackUrlObject
     * @param boxId
     * @param customerWorkerId
     * @param boxGoodsJsonObject
     */
    public void notifyCustomServerWhatGoodsUpdated(
            String replenishmentCallbackUrlObject, String boxId,
            int customerWorkerId, JSONObject boxGoodsJsonObject) {
        CustomResponseService customResponseService = new CustomResponseService();
        JSONObject replenishmentJsonObject = new JSONObject();
        JSONObject replenishmentJsonObjectBean = new JSONObject();
        JSONArray boxGoodsJsonArray = boxGoodsJsonObject
                .getJSONArray("BoxGoods");
        JSONObject updateJsonObject = boxGoodsJsonArray.getJSONObject(0)
                .getJSONObject("UpdatedGoods");
        JSONObject allGoodsJsonObject = boxGoodsJsonArray.getJSONObject(1)
                .getJSONObject("AllBoxGoods");
        replenishmentJsonObjectBean.put("replenishOrderType", 1);
        replenishmentJsonObjectBean.put("replenishOrderId",
                updateJsonObject.getString("updateId"));
        replenishmentJsonObjectBean.put("boxId", boxId);
        replenishmentJsonObjectBean.put("workerId", customerWorkerId);
        replenishmentJsonObjectBean.put("UpdatedGoods", updateJsonObject);
        replenishmentJsonObjectBean.put("AllBoxGoods", allGoodsJsonObject);
        replenishmentJsonObject.put("ReplenishOrder",
                replenishmentJsonObjectBean);
        log.info("notifyCustomServerWhatGoodsUpdated:boxId=" + boxId
                + "replenishmentJsonObject="
                + replenishmentJsonObject.toString());
        String responseToCustom = customResponseService
                .getReturnSuccessAndResultSuccessResponse(replenishmentJsonObject
                        .toString());
        // 判断是否要拼接上客户签名相关字串
        BoxesService boxesService = new BoxesService();
        Boxes boxes = boxesService.findBoxesByBoxId(boxId);
        if (boxes != null) {
            // 根据商户ID查询商户合作模式
            CustomerService customerService = new CustomerService();
            Customer customer = customerService.findCustomerByCustomerId(boxes
                    .getCustomerId());
            String mchId = customer.getMchId();
            if (mchId != null) {
                // 小e微店
                if (mchId.equals(CustomConfig.XIAOE_MCHID)
                        || mchId.equals(CustomConfig.XIAOE_TEST_MCHID)) {
                    Map<String, Object> signParamMap = CustomCallbackSignatureParamsStructureUtil
                            .getReplenishOrderCallbackSignatureParamsMap(boxId,
                                    String.valueOf(customerWorkerId), mchId);
                    if (signParamMap != null) {
                        JSONObject responseToCustomJsonObject = JSONObject
                                .fromObject(responseToCustom);
                        for (Map.Entry<String, Object> entry : signParamMap
                                .entrySet()) {
                            responseToCustomJsonObject.put(entry.getKey(),
                                    entry.getValue());
                            log.info("notifyCustomServerWhatGoodsUpdated:->paras for sign"
                                    + "Key: "
                                    + entry.getKey()
                                    + " Value: "
                                    + entry.getValue());
                        }
                        responseToCustom = responseToCustomJsonObject
                                .toString();
                        log.info("notifyCustomServerWhatGoodsUpdated:->responseTocustom="
                                + responseToCustom);
                    }
                }
            }
        }
        String orderCallbackUrl = JSONObject.fromObject(
                replenishmentCallbackUrlObject).getString("url");
        HttpUtil.sendCallbackResultMsg(orderCallbackUrl,
                JSONObject.fromObject(responseToCustom));
    }

    /**
     * 小e模式 回调给小e无上下货理货清单(type=0)
     * 
     * @param replenishmentCallbackUrlObject
     * @param boxId
     * @param customerWorkerId
     * @param boxGoodsJsonObject
     */
    public void notifyCustomServerNoGoodsUpdated(
            String replenishmentCallbackUrlObject, String boxId,
            int customerWorkerId, JSONObject boxGoodsJsonObject) {
        CustomResponseService customResponseService = new CustomResponseService();
        JSONObject replenishmentJsonObject = new JSONObject();
        JSONObject replenishmentJsonObjectBean = new JSONObject();
        JSONArray boxGoodsJsonArray = boxGoodsJsonObject
                .getJSONArray("BoxGoods");
        JSONObject allGoodsJsonObject = boxGoodsJsonArray.getJSONObject(1)
                .getJSONObject("AllBoxGoods");
        replenishmentJsonObjectBean.put("replenishOrderType", 0);
        replenishmentJsonObjectBean.put("replenishOrderId", boxGoodsJsonArray
                .getJSONObject(0).getString("updateId"));
        replenishmentJsonObjectBean.put("boxId", boxId);
        replenishmentJsonObjectBean.put("workerId", customerWorkerId);
        replenishmentJsonObjectBean.put("UpdatedGoods", "noGoods");
        replenishmentJsonObjectBean.put("AllBoxGoods", allGoodsJsonObject);
        replenishmentJsonObject.put("ReplenishOrder",
                replenishmentJsonObjectBean);
        log.info("notifyCustomServerNoGoodsUpdated:boxId=" + boxId
                + "replenishmentJsonObject="
                + replenishmentJsonObject.toString());
        String responseToCustom = customResponseService
                .getReturnSuccessAndResultSuccessResponse(replenishmentJsonObject
                        .toString());
        // 判断是否要拼接上客户签名相关字串
        BoxesService boxesService = new BoxesService();
        Boxes boxes = boxesService.findBoxesByBoxId(boxId);
        if (boxes != null) {
            // 根据商户ID查询商户合作模式
            CustomerService customerService = new CustomerService();
            Customer customer = customerService.findCustomerByCustomerId(boxes
                    .getCustomerId());
            String mchId = customer.getMchId();
            if (mchId != null) {
                // 小e微店
                if (mchId.equals(CustomConfig.XIAOE_MCHID)
                        || mchId.equals(CustomConfig.XIAOE_TEST_MCHID)) {
                    Map<String, Object> signParamMap = CustomCallbackSignatureParamsStructureUtil
                            .getReplenishOrderCallbackSignatureParamsMap(boxId,
                                    String.valueOf(customerWorkerId), mchId);
                    if (signParamMap != null) {
                        JSONObject responseToCustomJsonObject = JSONObject
                                .fromObject(responseToCustom);
                        for (Map.Entry<String, Object> entry : signParamMap
                                .entrySet()) {
                            responseToCustomJsonObject.put(entry.getKey(),
                                    entry.getValue());
                            log.info("notifyCustomServerWhatGoodsUpdated:->paras for sign"
                                    + "Key: "
                                    + entry.getKey()
                                    + " Value: "
                                    + entry.getValue());
                        }
                        responseToCustom = responseToCustomJsonObject
                                .toString();
                        log.info("notifyCustomServerNoGoodsUpdated:->responseTocustom="
                                + responseToCustom);
                    }
                }
            }
        }
        String orderCallbackUrl = JSONObject.fromObject(
                replenishmentCallbackUrlObject).getString("url");
        HttpUtil.sendCallbackResultMsg(orderCallbackUrl,
                JSONObject.fromObject(responseToCustom));
    }

}
