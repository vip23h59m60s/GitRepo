package com.chinapalms.kwobox.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.processors.JsonBeanProcessor;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.impl.ExceptionShoppingDAOImpl;
import com.chinapalms.kwobox.devicemonitor.DeviceMonitorService;
import com.chinapalms.kwobox.javabean.BoxStatus;
import com.chinapalms.kwobox.javabean.Boxes;
import com.chinapalms.kwobox.javabean.CallCenterNormal;
import com.chinapalms.kwobox.javabean.CurrentUser;
import com.chinapalms.kwobox.javabean.Customer;
import com.chinapalms.kwobox.javabean.ExceptionShopping;
import com.chinapalms.kwobox.servelet.SessionMapFactory;
import com.chinapalms.kwobox.utils.JavaMailUtil;
import com.chinapalms.kwobox.utils.JsonParseUtil;
import com.chinapalms.kwobox.utils.ResponseStatus;
import com.custom.kwobox.service.CustomWebSocketService;

public class ExceptionShoppingService extends ExceptionShoppingDAOImpl {

    @Override
    public boolean addExceptionShopping(ExceptionShopping exceptionShopping) {
        return super.addExceptionShopping(exceptionShopping);
    }

    @Override
    public boolean updateExceptionShoppingState(
            ExceptionShopping exceptionShopping) {
        return super.updateExceptionShoppingState(exceptionShopping);
    }

    @Override
    public List<ExceptionShopping> findExceptionShoppingsByState(int state) {
        return super.findExceptionShoppingsByState(state);
    }

    Log log = LogFactory.getLog(ExceptionShoppingService.class);

    public String doHandleExceptionVideo(HttpServletRequest request,
            HttpServletResponse response) {
        try {
            String path = request.getSession().getServletContext()
                    .getRealPath("/")
                    + "../"
                    + "ROOT"
                    + File.separator
                    + "videos"
                    + File.separator + "exceptionShoppingVideos";
            log.info(path);
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            // 获得磁盘文件条目工厂
            DiskFileItemFactory factory = new DiskFileItemFactory();

            // 如果没以下两行设置的话,上传大的文件会占用很多内存，
            // 设置暂时存放的存储室,这个存储室可以和最终存储文件的目录不同
            /**
             * 原理: 它是先存到暂时存储室，然后再真正写到对应目录的硬盘上， 按理来说当上传一个文件时，其实是上传了两份，第一个是以 .tem
             * 格式的 然后再将其真正写到对应目录的硬盘上
             */
            factory.setRepository(dir);
            // 设置缓存的大小，当上传文件的容量超过该缓存时，直接放到暂时存储室
            factory.setSizeThreshold(1024 * 1024);
            // 高水平的API文件上传处理
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> list = upload.parseRequest(request);
            List<FileItem> videosList = new ArrayList<FileItem>();
            FileItem video = null;
            for (FileItem item : list) {
                // 获取表单的属性名字
                String name = item.getFieldName();
                // 如果获取的表单信息是普通的 文本 信息
                if (item.isFormField()) {
                    // 获取用户具体输入的字符串
                    String value = item.getString();
                    request.setAttribute(name, value);
                } else {
                    // 此处需要传输两个视频文件，需要进行分开处理，基本思路是先把两个video放在List中通过遍历取出，再分别存储
                    video = item;
                    videosList.add(video);
                }
            }

            // 自定义上传图片的名字为userId.jpg
            String boxId = request.getAttribute("boxId").toString();
            String shopExceptionInfo = request
                    .getAttribute("shopExceptionInfo").toString();
            log.info("ExceptionShoppingService boxId=" + boxId
                    + ",  shopExceptionInfo=" + shopExceptionInfo);
            path = path + File.separator + boxId;
            dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            CurrentUserService currentUserService = new CurrentUserService();
            CurrentUser currentUser = currentUserService
                    .findCurrentUserByBoxId(boxId);

            String video1Path = null;
            String video2Path = null;
            for (int i = 0; i < videosList.size(); i++) {
                FileItem videoFileItem = videosList.get(i);

                String fileSuffix = videoFileItem.getName().substring(
                        videoFileItem.getName().lastIndexOf("."));
                // 判断文件中是否包含.ignore 可以忽略的文件,如果包含则直接忽略
                // if (fileSuffix != null && fileSuffix.endsWith("ignore")) {
                // continue;
                // }

                String fileName = null;
                if (currentUser != null) {
                    fileName = currentUser.getPhoneNumber()
                            + "_"
                            + new SimpleDateFormat("yyyyMMddHHmmss")
                                    .format(new Date()) + "-" + (i + 1)
                            + fileSuffix;
                } else {
                    fileName = new SimpleDateFormat("yyyyMMddHHmmss")
                            .format(new Date()) + "-" + (i + 1) + fileSuffix;
                }

                String destPath = path + File.separator + fileName;
                log.info("video destPath=" + destPath);

                // 真正写到磁盘上
                File file = new File(destPath);
                OutputStream out = new FileOutputStream(file);
                InputStream in = videoFileItem.getInputStream();
                int length = 0;
                byte[] buf = new byte[1024];
                // in.read(buf) 每次读到的数据存放在buf 数组中
                while ((length = in.read(buf)) != -1) {
                    // 在buf数组中取出数据写到（输出流）磁盘上
                    out.write(buf, 0, length);
                }
                in.close();
                in = null;
                out.close();
                out = null;

                String videosRelativePath = "videos/exceptionShoppingVideos/"
                        + boxId + "/" + fileName;
                if (i == 0) {
                    video1Path = videosRelativePath;
                } else if (i == 1) {
                    video2Path = videosRelativePath;
                }
            }
            if (sendExceptionShoppingInfoToExceptionCustom(boxId,
                    shopExceptionInfo, video1Path, video2Path)) {
                return ResponseStatus.SUCCESS;
            } else {
                return ResponseStatus.FAIL;
            }
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        return ResponseStatus.FAIL;
    }

    private boolean sendExceptionShoppingInfoToExceptionCustom(String boxId,
            String exceptionShoppingInfo, String video1Path, String video2Path) {
        JSONObject exceptionShoppingJsonObject = JSONObject
                .fromObject(exceptionShoppingInfo);
        JSONObject exceptionShoppingBeanJsonObject = exceptionShoppingJsonObject
                .getJSONObject("shopException");
        JSONObject exceptionShoppingJsonObjectNew = new JSONObject();
        // 当两个视频Url至少一个不为空时才保存，当两个都为空时表示主控没有上传视频或者时异常视频需要后台重复确认，此时只用把现有信息返回（包含有原有视频URL），不用重复传输视频
        if (video1Path != null || video1Path != null) {
            JSONObject videosUrlBeanJsonObject = new JSONObject();
            videosUrlBeanJsonObject.put("url1", ResponseStatus.SERVER_URL + "/"
                    + video1Path);
            videosUrlBeanJsonObject.put("url2", ResponseStatus.SERVER_URL + "/"
                    + video2Path);
            exceptionShoppingBeanJsonObject.put("videosUrl",
                    videosUrlBeanJsonObject);

            exceptionShoppingJsonObjectNew.put("shopException",
                    exceptionShoppingBeanJsonObject);
        } else {
            if (video1Path == null) {
                log.error("video1Path is not exit!!!!!!");
            }
            if (video2Path == null) {
                log.error("video2Path is not exit!!!!!!");
            }
            exceptionShoppingJsonObjectNew = exceptionShoppingJsonObject;
        }

        try {
            // 获取异常购物相关信息保存到数据库中
            String exceptionId = exceptionShoppingBeanJsonObject
                    .getString("exceptionId");
            String exceptionBoxId = exceptionShoppingBeanJsonObject
                    .getString("boxId");
            String exceptionInfo = exceptionShoppingJsonObjectNew.toString();
            CurrentUserService currentUserService = new CurrentUserService();
            CurrentUser currentUser = currentUserService
                    .findCurrentUserByBoxId(exceptionBoxId);
            String phoneNumber = currentUser.getPhoneNumber();
            Date exceptionTime = new Date();
            ExceptionShopping exceptionShopping = new ExceptionShopping();
            exceptionShopping.setExceptionId(exceptionId);
            exceptionShopping.setBoxId(exceptionBoxId);
            exceptionShopping.setExceptionShoppingInfo(exceptionInfo);
            exceptionShopping.setPhoneNumber(phoneNumber);
            exceptionShopping
                    .setState(ResponseStatus.EXCEPTION_HANDLE_STATE_NO_HANDLE);
            exceptionShopping.setExceptionTime(exceptionTime);
            exceptionShopping.setHandleTime(exceptionTime);
            addExceptionShopping(exceptionShopping);
            // 发送邮件给指定联系人
            sendExceptionEmail(boxId);
        } catch (Exception e1) {
            log.error("try->catch exception:", e1);
            e1.printStackTrace();
        }

        Session currentSession = SessionMapFactory.getInstance()
                .getCurrentSession(ResponseStatus.CUSTOM_CATEGORY_EXCEPTION,
                        "exceptionCustomBoxId");
        try {
            SessionMapFactory.getInstance().sendMessage(currentSession,
                    exceptionShoppingJsonObjectNew.toString());
            log.info("exceptioShoppingService->"
                    + "send exceptionShoppingJsonObjectNew to exception moniter servier");
            return true;
        } catch (Exception e) {
            log.error("sendExceptionShoppingInfoToExceptionCustom error:"
                    + e.toString());
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        return false;
    }

    public String doGetExceptionShoppingsByState(int state) {
        List<ExceptionShopping> exceptionShoppingsList = findExceptionShoppingsByState(state);
        JSONObject exceptionShoppingsJsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        if (exceptionShoppingsList.size() > 0) {
            jsonArray = JSONArray.fromObject(exceptionShoppingsList);
            exceptionShoppingsJsonObject.put("ExceptionShoppings", jsonArray);
            return exceptionShoppingsJsonObject.toString();
        } else {
            return ResponseStatus.FAIL;
        }
    }

    // 发送异常信息邮件
    public void sendExceptionEmail(String boxId) {
        BoxesService boxesService = new BoxesService();
        Boxes boxes = boxesService.findBoxesByBoxId(boxId);
        if (boxes != null) {
            String boxName = boxes.getBoxName();
            try {
                CallCenterNormalService callCenterNormalService = new CallCenterNormalService();
                // 查找邮件抄送人
                List<CallCenterNormal> callCenterNormalsCopyList = callCenterNormalService
                        .findEmailCopyReceiver();
                String[] copyReceiversArray = new String[callCenterNormalsCopyList
                        .size()];
                for (int i = 0; i < callCenterNormalsCopyList.size(); i++) {
                    copyReceiversArray[i] = callCenterNormalsCopyList.get(i)
                            .getEmail();
                }
                // 查找邮件收件人
                List<CallCenterNormal> callCenterNormalsList = callCenterNormalService
                        .findEmailReceiver();
                String receiver = callCenterNormalsList.size() > 0 ? callCenterNormalsList
                        .get(0).getEmail() : "panan@zhigoumao.cn";
                JavaMailUtil.sendEmailToDatabaseSender(receiver,
                        copyReceiversArray, "智购猫有新异常购物视频，请及时处理", boxName
                                + "(boxId:" + boxId + ")" + " 有新异常购物视频，请及时处理");
            } catch (Exception e) {
                log.error("try->catch exception:", e);
                e.printStackTrace();
            }
        }
    }

    /**
     * 售货机因消费者行为导致购物异常时通知管理员,并扣除当前消费者信用积分
     * 
     * @param boxId
     * @param phoneNumber
     * @param creditScores
     * @return
     */
    public String doNotifyManager(String boxId, String phoneNumber,
            String creditScores) {
        try {
            int creditScore = Integer.valueOf(creditScores);
            if (Integer.valueOf(creditScores) != 0) {
                // ============对接商户服务器逻辑==========================================================================
                // 判断是否是第三方对接模式(Yoho模式),如果是Yoho模式，将异常信息回调至商户服务器订单回调页面，以便于商户服务器更新个人征信分和通知商户微信客户端
                // 通过boxId查找商户ID，再根据商户ID查找回调Url，返回商户服务器，与商户服务器对接,如果无回调Url，则默认走智购猫平台逻辑
                CustomWebSocketService customWebSocketService = new CustomWebSocketService();
                String orderCallbackUrlObject = customWebSocketService
                        .getCustomerOrderCallbackUrl(boxId);
                if (orderCallbackUrlObject.equals(ResponseStatus.FAIL)) {
                    // 异常购物行为更新个人征信
                    updatePersonalCredit(phoneNumber, boxId,
                            ResponseStatus.PERSONAL_CREDIT_DEDUCT, -creditScore);
                    // 发送异常信息到微信客户端
                    // notifyWXBoxException(
                    // ResponseStatus.CUSTOM_CATEGORY_WX,
                    // boxId, creditScore);
                } else {
                    int urlType = JSONObject.fromObject(orderCallbackUrlObject)
                            .getInt("urlType");
                    // 如果是Yoho代理模式,则把订单信息回调给代理开发服务器
                    if (urlType == ResponseStatus.COOPERATIONMODE_AGENT) {
                        customWebSocketService
                                .notifyCustomerServerExceptionShoppingInfo(
                                        orderCallbackUrlObject, boxId,
                                        phoneNumber, creditScore);
                    }
                }
                // ============对接商户服务器逻辑==========================================================================
            }

            // 发短信给理货员，售货柜已发生异常，尽快理货
            notifyBoxManagerToManageExceptionShoppingBox(
                    ResponseStatus.CUSTOM_CATEGORY_ANDROID, boxId);
            return ResponseStatus.SUCCESS;
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        return ResponseStatus.FAIL;
    }

    /**
     * // 接收异常视频处理工控机发送异常处理结果
     * 
     * @param exceptionShoppingInfo
     */
    public String doMonitorExceptionShopping(String orderType,
            String exceptionShoppingInfo, String phoneNumber,
            String openDoorRequestSerialNumber) {
        try {
            if (exceptionShoppingInfo != null) {
                JSONObject jsonObject = JSONObject
                        .fromObject(exceptionShoppingInfo);
                if (jsonObject.containsKey("shopException")) {
                    log.info("ExceptionShoppingService->start sendExceptionShoppingInfoBackToAndroid!!!!->exceptionShoppingInfo="
                            + exceptionShoppingInfo);
                    String exceptionBoxId = jsonObject.getJSONObject(
                            "shopException").getString("boxId");
                    String exceptionType = jsonObject.getJSONObject(
                            "shopException").getString("ExceptionType");
                    String exceptionId = null;
                    try {
                        exceptionId = jsonObject.getJSONObject("shopException")
                                .getString("exceptionId");
                    } catch (Exception e) {
                        log.error("try->catch exception:", e);
                        e.printStackTrace();
                    }
                    if (phoneNumber == null) {
                        CurrentUserService currentUserService = new CurrentUserService();
                        CurrentUser currentUser = currentUserService
                                .findCurrentUserByBoxId(exceptionBoxId);
                        phoneNumber = currentUser.getPhoneNumber();
                    }
                    JSONArray shoppingArray = jsonObject.getJSONObject(
                            "shopException").getJSONArray("Shopping");
                    int personalScores = jsonObject.getJSONObject(
                            "shopException").getInt("score");
                    // CurrentUserService currentUserService = new
                    // CurrentUserService();
                    // CurrentUser currentUser = currentUserService
                    // .findCurrentUserByBoxId(exceptionBoxId);
                    if (personalScores != 0) {
                        // ============对接商户服务器逻辑==========================================================================
                        // 判断是否是第三方对接模式(Yoho模式),如果是Yoho模式，将异常信息回调至商户服务器订单回调页面，以便于商户服务器更新个人征信分和通知商户微信客户端
                        // 通过boxId查找商户ID，再根据商户ID查找回调Url，返回商户服务器，与商户服务器对接,如果无回调Url，则默认走智购猫平台逻辑
                        CustomWebSocketService customWebSocketService = new CustomWebSocketService();
                        String orderCallbackUrlObject = customWebSocketService
                                .getCustomerOrderCallbackUrl(exceptionBoxId);
                        if (orderCallbackUrlObject.equals(ResponseStatus.FAIL)) {
                            // 异常购物行为更新个人征信
                            updatePersonalCredit(phoneNumber, exceptionBoxId,
                                    ResponseStatus.PERSONAL_CREDIT_DEDUCT,
                                    -personalScores);
                            // 发送异常信息到微信客户端
                            notifyWXBoxException(
                                    ResponseStatus.CUSTOM_CATEGORY_WX,
                                    exceptionBoxId, personalScores);
                        } else {
                            int urlType = JSONObject.fromObject(
                                    orderCallbackUrlObject).getInt("urlType");
                            // 如果是Yoho代理模式,则把订单信息回调给代理开发服务器
                            if (urlType == ResponseStatus.COOPERATIONMODE_AGENT) {
                                customWebSocketService
                                        .notifyCustomerServerExceptionShoppingInfo(
                                                orderCallbackUrlObject,
                                                exceptionBoxId, phoneNumber,
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
                                cardgoRoadList.add(shoppingArray.getJSONObject(
                                        i).getInt("position"));
                            }
                            Set<Integer> cardgoRoadSet = new HashSet<Integer>(
                                    cardgoRoadList);
                            for (int uniqueCardgoRoadId : cardgoRoadSet) {
                                JSONObject exceptionShoppingJsonObject = new JSONObject();
                                float cardgoRoadGoodsTotalWeight = 0.0f;
                                for (int i = 0; i < shoppingArray.size(); i++) {
                                    JSONObject shoppingJsonObject = shoppingArray
                                            .getJSONObject(i);
                                    if (shoppingJsonObject.getInt("position") == uniqueCardgoRoadId) {
                                        exceptionShoppingJsonObject.put(
                                                "barCodeId", shoppingJsonObject
                                                        .getString("barcode"));
                                        exceptionShoppingJsonObject.put(
                                                "cardgoRoadId",
                                                uniqueCardgoRoadId);
                                        exceptionShoppingJsonObject.put(
                                                "goodsNumber",
                                                Collections.frequency(
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
                            notifyWXWhatGoodsSaled(orderType,
                                    ResponseStatus.CUSTOM_CATEGORY_WX,
                                    exceptionBoxId, phoneNumber,
                                    openDoorRequestSerialNumber,
                                    ResponseStatus.BOX_TYPE_WEIGHT,
                                    goodsBarCodeIdsJsonList, false);
                        } else {
                            notifyWXNoGoodsSaled(orderType,
                                    ResponseStatus.CUSTOM_CATEGORY_WX,
                                    exceptionBoxId, phoneNumber,
                                    openDoorRequestSerialNumber);
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
                }
                return ResponseStatus.SUCCESS;
            } else {
                return ResponseStatus.FAIL;
            }
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        return ResponseStatus.FAIL;
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
            log.info("ExceptionShoppingService->"
                    + "exceptionShoppingInfoJsonObject="
                    + exceptionShoppingInfoJsonObject.toString());
        } catch (Exception e) {
            log.error("ExceptionShoppingService sendExceptionShoppingInfoBackToAndroid error:"
                    + e.toString());
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

}
