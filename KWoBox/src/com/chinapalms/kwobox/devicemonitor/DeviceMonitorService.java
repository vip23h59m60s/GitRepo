package com.chinapalms.kwobox.devicemonitor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.sql.Array;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.chinapalms.kwobox.javabean.BoxStatus;
import com.chinapalms.kwobox.javabean.Boxes;
import com.chinapalms.kwobox.javabean.CustomerWorker;
import com.chinapalms.kwobox.javabean.ICS;
import com.chinapalms.kwobox.javabean.User;
import com.chinapalms.kwobox.servelet.SessionMapFactory;
import com.chinapalms.kwobox.service.BoxManagerService;
import com.chinapalms.kwobox.service.BoxesService;
import com.chinapalms.kwobox.service.CustomerWorkerService;
import com.chinapalms.kwobox.service.ICSService;
import com.chinapalms.kwobox.service.UserService;
import com.chinapalms.kwobox.utils.IdentifyCodeUtil;
import com.chinapalms.kwobox.utils.ResponseStatus;

public class DeviceMonitorService {

    Log log = LogFactory.getLog(DeviceMonitorService.class);

    public void notifyManagerDeviceNotCharging(String customCategory,
            String boxId) {
        BoxesService boxesService = new BoxesService();
        Boxes boxes = boxesService.findBoxesByBoxId(boxId);
        if (boxes != null) {
            BoxManagerService boxManagerService = new BoxManagerService();
            List<Integer> deliveryIdList = boxManagerService
                    .doFindUniqueDeliveryIdOnSameBox(boxId);
            for (int i = 0; i < deliveryIdList.size(); i++) {
                CustomerWorkerService customerWorkerService = new CustomerWorkerService();
                CustomerWorker customerWorker = customerWorkerService
                        .findCustomerWorkerByCustomerWorkerId(deliveryIdList
                                .get(i));
                if (customerWorker != null) {
                    // 获取送货员的电话号码
                    String customerWorkerPhoneNumber = customerWorker
                            .getPhoneNumber();
                    String boxName = boxes.getBoxName();
                    // 通知理货员设备已断电
                    IdentifyCodeUtil.sendTencentQQDeviceNotChargingSms(
                            customerWorkerPhoneNumber, boxName);
                }
            }
        }
    }

    public void notifyBoxManagerToManageExceptionShoppingBox(
            String customCategory, String boxId) {
        BoxesService boxesService = new BoxesService();
        Boxes boxes = boxesService.findBoxesByBoxId(boxId);
        if (boxes != null) {
            BoxManagerService boxManagerService = new BoxManagerService();
            List<Integer> deliveryIdList = boxManagerService
                    .doFindUniqueDeliveryIdOnSameBox(boxId);
            for (int i = 0; i < deliveryIdList.size(); i++) {
                CustomerWorkerService customerWorkerService = new CustomerWorkerService();
                CustomerWorker customerWorker = customerWorkerService
                        .findCustomerWorkerByCustomerWorkerId(deliveryIdList
                                .get(i));
                if (customerWorker != null) {
                    // 获取送货员的电话号码
                    String customerWorkerPhoneNumber = customerWorker
                            .getPhoneNumber();
                    String boxName = boxes.getBoxName();
                    IdentifyCodeUtil
                            .sendTencentQQToBoxManagerToManageExceptionShoppingBox(
                                    customerWorkerPhoneNumber, boxName);
                }
            }
        }
    }

    public void doLingmaoUpgrade(String action, String boxIdsString,
            String apUrl, String appVersion) {
        List<String> boxIdsList = Arrays.asList(boxIdsString.split(","));
        for (int i = 0; i < boxIdsList.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("response", action);
            jsonObject.put("apkUrl", apUrl);
            jsonObject.put("appVersion", appVersion);
            Session session = SessionMapFactory.getInstance()
                    .getCurrentSession(ResponseStatus.CUSTOM_CATEGORY_ANDROID,
                            boxIdsList.get(i));
            try {
                SessionMapFactory.getInstance().sendMessage(session,
                        jsonObject.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void doLingmaoCoreUpgrade(String action, String boxIdsString,
            String apUrl, String appVersion) {
        List<String> boxIdsList = Arrays.asList(boxIdsString.split(","));
        for (int i = 0; i < boxIdsList.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("response", action);
            jsonObject.put("apkUrl", apUrl);
            jsonObject.put("appVersion", appVersion);
            Session session = SessionMapFactory.getInstance()
                    .getCurrentSession(ResponseStatus.CUSTOM_CATEGORY_ANDROID,
                            boxIdsList.get(i));
            try {
                SessionMapFactory.getInstance().sendMessage(session,
                        jsonObject.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void doLingmaoToolsUpgrade(String action, String boxIdsString,
            String apUrl, String appVersion) {
        List<String> boxIdsList = Arrays.asList(boxIdsString.split(","));
        for (int i = 0; i < boxIdsList.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("response", action);
            jsonObject.put("apkUrl", apUrl);
            jsonObject.put("appVersion", appVersion);
            Session session = SessionMapFactory.getInstance()
                    .getCurrentSession(ResponseStatus.CUSTOM_CATEGORY_ANDROID,
                            boxIdsList.get(i));
            try {
                SessionMapFactory.getInstance().sendMessage(session,
                        jsonObject.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void doLingmaoAllAppsUpgrade(String action, String boxIdStr,
            String appsInfo) {
        List<String> boxIdsList = Arrays.asList(boxIdStr.split(","));
        for (int i = 0; i < boxIdsList.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("response", action);
            jsonObject.put("appsInfo", JSONArray.fromObject(appsInfo));
            Session session = SessionMapFactory.getInstance()
                    .getCurrentSession(ResponseStatus.CUSTOM_CATEGORY_ANDROID,
                            boxIdsList.get(i));
            try {
                SessionMapFactory.getInstance().sendMessage(session,
                        jsonObject.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void doFotaUpgrade(String action, String boxIdsString,
            String appVersion) {
        List<String> boxIdsList = Arrays.asList(boxIdsString.split(","));
        for (int i = 0; i < boxIdsList.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("response", action);
            jsonObject.put("appVersion", appVersion);
            Session session = SessionMapFactory.getInstance()
                    .getCurrentSession(ResponseStatus.CUSTOM_CATEGORY_ANDROID,
                            boxIdsList.get(i));
            try {
                SessionMapFactory.getInstance().sendMessage(session,
                        jsonObject.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void doSetDebugMode(String action, String boxIdsString, String mode) {
        List<String> boxIdsList = Arrays.asList(boxIdsString.split(","));
        for (int i = 0; i < boxIdsList.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("response", action);
            jsonObject.put("value", mode);
            Session session = SessionMapFactory.getInstance()
                    .getCurrentSession(ResponseStatus.CUSTOM_CATEGORY_ANDROID,
                            boxIdsList.get(i));
            try {
                SessionMapFactory.getInstance().sendMessage(session,
                        jsonObject.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void doSetLogLevel(String action, String boxIdsString,
            String logLevel) {
        List<String> boxIdsList = Arrays.asList(boxIdsString.split(","));
        for (int i = 0; i < boxIdsList.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("response", action);
            jsonObject.put("value", logLevel);
            Session session = SessionMapFactory.getInstance()
                    .getCurrentSession(ResponseStatus.CUSTOM_CATEGORY_ANDROID,
                            boxIdsList.get(i));
            try {
                SessionMapFactory.getInstance().sendMessage(session,
                        jsonObject.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void doPushAdv(String action, String boxIdsString, String apUrl,
            String appVersion) {
        List<String> boxIdsList = Arrays.asList(boxIdsString.split(","));
        for (int i = 0; i < boxIdsList.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("response", action);
            jsonObject.put("advUrl", apUrl);
            jsonObject.put("advVersion", appVersion);
            Session session = SessionMapFactory.getInstance()
                    .getCurrentSession(ResponseStatus.CUSTOM_CATEGORY_ANDROID,
                            boxIdsList.get(i));
            try {
                SessionMapFactory.getInstance().sendMessage(session,
                        jsonObject.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void doRemoveAdv(String action, String boxIdsString) {
        List<String> boxIdsList = Arrays.asList(boxIdsString.split(","));
        for (int i = 0; i < boxIdsList.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("response", action);
            Session session = SessionMapFactory.getInstance()
                    .getCurrentSession(ResponseStatus.CUSTOM_CATEGORY_ANDROID,
                            boxIdsList.get(i));
            try {
                SessionMapFactory.getInstance().sendMessage(session,
                        jsonObject.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void doRestart(String action, String boxIdsString, String value) {
        List<String> boxIdsList = Arrays.asList(boxIdsString.split(","));
        for (int i = 0; i < boxIdsList.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("response", action);
            jsonObject.put("value", value);
            Session session = SessionMapFactory.getInstance()
                    .getCurrentSession(ResponseStatus.CUSTOM_CATEGORY_ANDROID,
                            boxIdsList.get(i));
            try {
                SessionMapFactory.getInstance().sendMessage(session,
                        jsonObject.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void doBootCheckSelf(String action, String boxIdsString, String value) {
        List<String> boxIdsList = Arrays.asList(boxIdsString.split(","));
        for (int i = 0; i < boxIdsList.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("response", action);
            jsonObject.put("value", value);
            Session session = SessionMapFactory.getInstance()
                    .getCurrentSession(ResponseStatus.CUSTOM_CATEGORY_ANDROID,
                            boxIdsList.get(i));
            try {
                SessionMapFactory.getInstance().sendMessage(session,
                        jsonObject.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void doGetVideo(String action, String boxIdsString,
            String startTime, String endTime) {
        List<String> boxIdsList = Arrays.asList(boxIdsString.split(","));
        for (int i = 0; i < boxIdsList.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("response", action);
            jsonObject.put("startTime", startTime);
            jsonObject.put("endTime", endTime);
            Session session = SessionMapFactory.getInstance()
                    .getCurrentSession(ResponseStatus.CUSTOM_CATEGORY_ANDROID,
                            boxIdsList.get(i));
            try {
                SessionMapFactory.getInstance().sendMessage(session,
                        jsonObject.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void doGetLog(String action, String boxIdsString, String startTime,
            String endTime) {
        List<String> boxIdsList = Arrays.asList(boxIdsString.split(","));
        for (int i = 0; i < boxIdsList.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("response", action);
            jsonObject.put("startTime", startTime);
            jsonObject.put("endTime", endTime);
            Session session = SessionMapFactory.getInstance()
                    .getCurrentSession(ResponseStatus.CUSTOM_CATEGORY_ANDROID,
                            boxIdsList.get(i));
            try {
                SessionMapFactory.getInstance().sendMessage(session,
                        jsonObject.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String doBoxRegister(HttpServletRequest request,
            HttpServletResponse response) {
        try {
            // 获取文件需要上传到的路径
            // String path = request.getRealPath("/upload");
            // String path = Thread.currentThread().getContextClassLoader()
            // .getResource("/").getPath();
            String path = request.getSession().getServletContext()
                    .getRealPath("/")
                    + "../"
                    + "ROOT"
                    + File.separator
                    + "pictures"
                    + File.separator + "boxEnvironmentPicture";
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
            FileItem picture = null;
            for (FileItem item : list) {
                // 获取表单的属性名字
                String name = item.getFieldName();
                // 如果获取的表单信息是普通的 文本 信息
                if (item.isFormField()) {
                    // 获取用户具体输入的字符串
                    String value = item.getString();
                    request.setAttribute(name, value);
                } else {
                    picture = item;
                }
            }

            // 自定义上传图片的名字为userId.jpg
            String boxId = request.getAttribute("boxId").toString();
            String customerId = request.getAttribute("customerId").toString();
            int shopId = request.getAttribute("shopId") != null ? Integer
                    .valueOf(request.getAttribute("shopId").toString()) : 0;
            String boxName = request.getAttribute("boxName").toString();
            String boxAddress = request.getAttribute("boxAddress").toString();
            // path = path + File.separator + boxId;
            dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String fileSuffix = picture.getName().substring(
                    picture.getName().lastIndexOf("."));
            String fileName = boxId + fileSuffix;
            String destPath = path + File.separator + fileName;
            log.info("destPath=" + destPath);

            // 真正写到磁盘上
            File file = new File(destPath);
            OutputStream out = new FileOutputStream(file);
            InputStream in = picture.getInputStream();
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
            String boxPicturePath = "pictures/boxEnvironmentPicture/"
                    + fileName;

            BoxesService boxesService = new BoxesService();
            Boxes boxes = new Boxes();
            boxes.setBoxId(boxId);
            boxes.setBoxName(URLDecoder.decode(boxName, "utf-8"));// 解决中文乱码问题
            boxes.setBoxAddress(URLDecoder.decode(boxAddress, "utf-8"));// 解决中文乱码问题
            boxes.setCustomerId(Integer.valueOf(customerId));
            boxes.setEnvironmentPicture(boxPicturePath);
            boxes.setBoxState(ResponseStatus.BOX_STATE_FINISHED_MERCHANT_DEPLOYMENT);
            boxes.setShopId(shopId);
            boxes.setPlaceDateTime(new Date());

            log.info("boxRegister boxInfo->=" + boxes.toString());

            if (boxesService.updateBoxRegisterBoxesInfo(boxes)) {
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

    public String doReportSystemInfo(String boxId, String systemInfo) {
        BoxesService boxesService = new BoxesService();
        Boxes boxes = boxesService.findBoxesByBoxId(boxId);
        if (boxes != null) {
            ICSService icsService = new ICSService();
            JSONObject systemInfoJsonObject = JSONObject.fromObject(systemInfo);
            ICS ics = new ICS();
            ics.setIcsId(boxes.getIcsId());
            ics.setFotaVersion(systemInfoJsonObject.getString("fotaVersion"));
            ics.setLingMaoVersion(systemInfoJsonObject
                    .getString("lingMaoVersion"));
            ics.setLingMaoCoreVersion(systemInfoJsonObject
                    .getString("lingMaoCoreVersion"));
            ics.setLingMaoToolsVersion(systemInfoJsonObject
                    .getString("lingMaoToolsVersion"));
            ics.setAdvVersion(systemInfoJsonObject.getString("advVersion"));
            ics.setDebugStatus(systemInfoJsonObject.getInt("debugStatus"));
            ics.setLogLevel(systemInfoJsonObject.getString("logLevel"));
            ics.setCheckSelf(systemInfoJsonObject.getInt("checkSelf"));
            if (icsService.updateICS(ics)) {
                return ResponseStatus.SUCCESS;
            } else {
                return ResponseStatus.FAIL;
            }
        } else {
            return ResponseStatus.FAIL;
        }
    }

    public String doShutDownDevice(String boxId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", "shutdown");
        Session session = SessionMapFactory.getInstance().getCurrentSession(
                ResponseStatus.CUSTOM_CATEGORY_ANDROID, boxId);
        try {
            SessionMapFactory.getInstance().sendMessage(session,
                    jsonObject.toString());
            return ResponseStatus.SUCCESS;
        } catch (IOException e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        return ResponseStatus.FAIL;
    }

}
