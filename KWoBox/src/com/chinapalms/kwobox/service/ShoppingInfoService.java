package com.chinapalms.kwobox.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.dao.impl.ShoppingInfoDAOImpl;
import com.chinapalms.kwobox.javabean.Boxes;
import com.chinapalms.kwobox.javabean.PersonalCreditRecord;
import com.chinapalms.kwobox.javabean.ShoppingInfo;
import com.chinapalms.kwobox.log.LogRandomStringGenerator;
import com.chinapalms.kwobox.servelet.SessionMapFactory;
import com.chinapalms.kwobox.utils.ResponseStatus;
import com.github.qcloudsms.SmsStatusPullerResult.Data;

public class ShoppingInfoService extends ShoppingInfoDAOImpl {

    Log log = LogFactory.getLog(ShoppingInfoService.class);

    @Override
    public boolean addShoppingInfo(ShoppingInfo shoppingInfo) {
        return super.addShoppingInfo(shoppingInfo);
    }

    @Override
    public ShoppingInfo findShoppingInfoByShoppingInfoId(String shoppingInfoId) {
        return super.findShoppingInfoByShoppingInfoId(shoppingInfoId);
    }

    @Override
    public int findShoppInfoCountByBoxIdAndState(String boxId, int state,
            String startTime, String endTime) {
        return super.findShoppInfoCountByBoxIdAndState(boxId, state, startTime,
                endTime);
    }

    @Override
    public List<ShoppingInfo> findShoppingInfosByBoxIdAndStateAndPageNumber(
            String boxId, int state, String startTime, String endTime,
            int pageNumber, int pageSize) {
        return super.findShoppingInfosByBoxIdAndStateAndPageNumber(boxId,
                state, startTime, endTime, pageNumber, pageSize);
    }

    @Override
    public ShoppingInfo findShoppingInfoByOrderId(String orderId) {
        return super.findShoppingInfoByOrderId(orderId);
    }

    @Override
    public boolean updateShoppingInfoState(ShoppingInfo shoppingInfo) {
        return super.updateShoppingInfoState(shoppingInfo);
    }

    @Override
    public boolean updateShoppingInfoVideosUrl(ShoppingInfo shoppingInfo) {
        return super.updateShoppingInfoVideosUrl(shoppingInfo);
    }

    @Override
    public boolean updateShoppingInfoOrderId(ShoppingInfo shoppingInfo) {
        return super.updateShoppingInfoOrderId(shoppingInfo);
    }

    @Override
    public List<ShoppingInfo> findShoppInfoByBoxIdAndState(String boxId,
            int state) {
        return super.findShoppInfoByBoxIdAndState(boxId, state);
    }

    @Override
    public List<ShoppingInfo> findShoppingInfosByCustomerIdAndPhoneNumberAndState(
            int customerId, String phoneNumber, int state) {
        return super.findShoppingInfosByCustomerIdAndPhoneNumberAndState(
                customerId, phoneNumber, state);
    }

    @Override
    public int findShoppingInfosBoxesCountByCustomerIdAndState(int customerId,
            int state) {
        return super.findShoppingInfosBoxesCountByCustomerIdAndState(
                customerId, state);
    }

    @Override
    public int findShoppingInfosCountByBoxIdAndState(String boxId, int state) {
        return super.findShoppingInfosCountByBoxIdAndState(boxId, state);
    }

    public String doUploadShoppingInfo(HttpServletRequest request,
            HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        try {
            // 获取文件需要上传到的路径
            // String path = request.getRealPath("/upload");
            // String path = Thread.currentThread().getContextClassLoader()
            // .getResource("/").getPath();
            String path = request.getSession().getServletContext()
                    .getRealPath("/")
                    + "../" + "ROOT" + File.separator + "shoppingInfo";
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
            FileItem shoppingInfoItem = null;
            for (FileItem item : list) {
                // 获取表单的属性名字
                String name = item.getFieldName();
                // 如果获取的表单信息是普通的 文本 信息
                if (item.isFormField()) {
                    // 获取用户具体输入的字符串
                    String value = item.getString();
                    request.setAttribute(name, value);
                } else {
                    shoppingInfoItem = item;
                }
            }

            // private String shoppingInfoId;// 本次购物信息ID
            // private String orderId;// 订单ID
            // private String boxId;// 售货机Id
            // private String boxName;// 售货机名字
            // private String shoppingInfo;// 本次购物相关信息
            // private String videoUrlInfo;// 购物监控视频路径
            // private int referScore;// 视频处理参考分值
            // private String resolveUser;// 视频处理人员
            // private int state;// 该次购物视频状态
            // private Date openTime;// 购物视频发生时间
            // private Date closeTime;// 该视频处理完成时间

            // 自定义上传图片的名字为userId.jpg
            String shoppingInfoId = request.getAttribute("shoppingInfoId")
                    .toString();
            String orderId = request.getAttribute("orderId").toString();
            String boxId = request.getAttribute("boxId").toString();
            // String boxName = request.getAttribute("boxName").toString();
            String phoneNumber = request.getAttribute("phoneNumber") != null ? request
                    .getAttribute("phoneNumber").toString() : null;
            String requestSerialNumber = request
                    .getAttribute("requestSerialNumber") != null ? request
                    .getAttribute("requestSerialNumber").toString() : null;
            String videoUrlInfo = request.getAttribute("videoUrlInfo") != null ? request
                    .getAttribute("videoUrlInfo").toString() : null;
            String referScore = request.getAttribute("referScore").toString();
            // String resolveUser =
            // request.getAttribute("resolveUser").toString();
            // String state = request.getAttribute("state").toString();
            path = path + File.separator + boxId;
            dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String filePre = "shoppingInfoId-" + shoppingInfoId + "_"
                    + "orderId-" + orderId + "_"
                    + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String fileSuffix = shoppingInfoItem.getName().substring(
                    shoppingInfoItem.getName().lastIndexOf("."));
            String fileName = filePre + fileSuffix;
            String destPath = path + File.separator + fileName;
            log.info("destPath=" + destPath);

            // 真正写到磁盘上
            File file = new File(destPath);
            OutputStream out = new FileOutputStream(file);
            InputStream in = shoppingInfoItem.getInputStream();
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
            String shoppingInfoPath = "shoppingInfo/" + boxId + "/" + fileName;

            // private String shoppingInfoId;// 本次购物信息ID
            // private String orderId;// 订单ID
            // private String boxId;// 售货机Id
            // private String boxName;// 售货机名字
            // private String shoppingInfo;// 本次购物相关信息
            // private String videoUrlInfo;// 购物监控视频路径
            // private int referScore;// 视频处理参考分值
            // private String resolveUser;// 视频处理人员
            // private int state;// 该次购物视频状态
            // private Date openTime;// 购物视频发生时间
            // private Date closeTime;// 该视频处理完成时间
            ShoppingInfoService shoppingInfoService = new ShoppingInfoService();
            ShoppingInfo shoppingInfoBean = new ShoppingInfo();
            shoppingInfoBean.setShoppingInfoId(shoppingInfoId);
            shoppingInfoBean.setOrderId(orderId);
            // 根据boxId查询出customerId
            BoxesService boxesService = new BoxesService();
            Boxes boxes = boxesService.findBoxesByBoxId(boxId);
            shoppingInfoBean.setCustomerId(boxes != null ? boxes
                    .getCustomerId() : 0);
            shoppingInfoBean.setBoxId(boxId);
            shoppingInfoBean.setPhoneNumber(phoneNumber);
            shoppingInfoBean.setRequestSerialNumber(requestSerialNumber);
            shoppingInfoBean.setShoppingInfo(shoppingInfoPath);
            shoppingInfoBean.setVideoUrlInfo(videoUrlInfo);
            shoppingInfoBean.setReferScore(Integer.valueOf(referScore));
            shoppingInfoBean.setResolveUser("nobody");
            if (Integer.valueOf(referScore) == 100) {
                shoppingInfoBean
                        .setState(ResponseStatus.SHOPPING_INFO_STATE_CLOSED);
            } else {
                shoppingInfoBean
                        .setState(ResponseStatus.SHOPPING_INFO_STATE_OPENED);
            }
            Date openDate = new Date();
            shoppingInfoBean.setOpenTime(openDate);
            shoppingInfoBean.setCloseTime(openDate);

            ShoppingInfo shoppingInfoQuery = shoppingInfoService
                    .findShoppingInfoByShoppingInfoId(shoppingInfoId);
            // 如果该ID购物信息已经保存至数据库则不再保存，否则保存至数据库
            if (shoppingInfoQuery != null) {
                log.info("shoppingInfoId is exist, no need to save, return success!!!!!!!!!!");
                jsonObject.put("response", ResponseStatus.SUCCESS);
                jsonObject.put("shoppingInfoId", shoppingInfoId);
            } else {
                if (shoppingInfoService.addShoppingInfo(shoppingInfoBean)) {
                    jsonObject.put("response", ResponseStatus.SUCCESS);
                    jsonObject.put("shoppingInfoId", shoppingInfoId);
                } else {
                    jsonObject.put("response", ResponseStatus.FAIL);
                    jsonObject.put("shoppingInfoId", shoppingInfoId);
                }
            }
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    // 分页查询逻辑
    public String doFindShoppingInfos(String boxId, String state,
            String startTime, String endTime, int pageNumber, int pageSize)
            throws Exception {
        int count = findShoppInfoCountByBoxIdAndState(boxId,
                Integer.valueOf(state), startTime, endTime);
        int totalPages = 0;
        // 计算总页数
        if (count % pageSize == 0) {
            // 对总页数赋值
            totalPages = count / pageSize;
        } else {
            // 对总页数赋值
            totalPages = count / pageSize + 1;
        }

        List<ShoppingInfo> shoppingInfosList = findShoppingInfosByBoxIdAndStateAndPageNumber(
                boxId, Integer.valueOf(state), startTime, endTime, pageNumber,
                pageSize);
        if (pageNumber == 1 && shoppingInfosList.size() <= 0) {
            return ResponseStatus.NO_SHOPPING_INFO;
        } else if (pageNumber <= totalPages && shoppingInfosList.size() > 0) {
            JSONObject shoppingInfoJsonObject = new JSONObject();
            JSONArray shoppingInfosJsonArray = new JSONArray();
            JSONObject personalCreditRecordsJsonObject = new JSONObject();
            for (int i = 0; i < shoppingInfosList.size(); i++) {
                ShoppingInfo shoppingInfo = shoppingInfosList.get(i);
                shoppingInfoJsonObject = JSONObject.fromObject(shoppingInfo);
                shoppingInfoJsonObject
                        .put("openTime",
                                dateToStamp(new SimpleDateFormat(
                                        "yyyy-MM-dd HH:mm:ss").format(
                                        shoppingInfo.getOpenTime()).toString()));
                shoppingInfoJsonObject
                        .put("closeTime",
                                dateToStamp(new SimpleDateFormat(
                                        "yyyy-MM-dd HH:mm:ss").format(
                                        shoppingInfo.getCloseTime()).toString()));
                shoppingInfosJsonArray.add(shoppingInfoJsonObject);
            }
            personalCreditRecordsJsonObject.put("ShoppingInfos",
                    shoppingInfosJsonArray);
            return personalCreditRecordsJsonObject.toString();
        } else {
            return ResponseStatus.LAST_PAGE;
        }
    }

    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    public String doUpdateShoppingInfoState(String shoppingInfoId,
            String resolverUser, String state) {
        ShoppingInfo shoppingInfo = new ShoppingInfo();
        shoppingInfo.setShoppingInfoId(shoppingInfoId);
        shoppingInfo.setResolveUser(resolverUser);
        shoppingInfo.setState(Integer.valueOf(state));
        shoppingInfo.setCloseTime(new Date());
        JSONObject jsonObject = new JSONObject();
        // 查询当前购物信息记录是否别其他人处理中
        ShoppingInfo shoppingInfoQuery = findShoppingInfoByShoppingInfoId(shoppingInfoId);
        if (shoppingInfoQuery != null) {
            if (shoppingInfoQuery.getResolveUser().equals(
                    ResponseStatus.DO_SHOPPING_INFO_NOBODY)
                    || shoppingInfoQuery.getResolveUser().equals(resolverUser)) {
                ShoppingInfoService shoppingInfoService = new ShoppingInfoService();
                if (shoppingInfoService.updateShoppingInfoState(shoppingInfo)) {
                    jsonObject.put("response", ResponseStatus.SUCCESS);
                } else {
                    jsonObject.put("response", ResponseStatus.FAIL);
                    jsonObject
                            .put("errorCode",
                                    ResponseStatus.UPDATE_SHOPPING_INFO_STATE_FAIL_ERROR_CODE_444);
                    jsonObject
                            .put("errorMsg",
                                    ResponseStatus.UPDATE_SHOPPING_INFO_STATE_FAIL_ERROR_MSG_444);
                }
            } else {
                jsonObject.put("response", ResponseStatus.FAIL);
                jsonObject
                        .put("errorCode",
                                ResponseStatus.UPDATE_SHOPPING_INFO_STATE_FAIL_ERROR_CODE_445);
                jsonObject
                        .put("errorMsg",
                                ResponseStatus.UPDATE_SHOPPING_INFO_STATE_FAIL_ERROR_MSG_445
                                        + "->用户:"
                                        + shoppingInfoQuery.getResolveUser()
                                        + ",状态：->"
                                        + shoppingInfoQuery.getState());
            }
        }
        return jsonObject.toString();
    }

    public String doGetShoppingInfoVideos(String shoppingInfoId,
            String videoUrlInfo, String boxId) {
        JSONObject jsonObject = new JSONObject();
        ShoppingInfo shoppingInfo = findShoppingInfoByShoppingInfoId(shoppingInfoId);
        if (shoppingInfo != null) {
            String videoUrlInfoQuery = shoppingInfo.getVideoUrlInfo();
            if (videoUrlInfoQuery != null) {
                if (videoUrlInfoQuery
                        .equals(ResponseStatus.SHOPPING_INFO_VIDEO_IS_DELETED)) {
                    jsonObject.put("response", ResponseStatus.FAIL);
                    jsonObject
                            .put("errorCode",
                                    ResponseStatus.SHOPPING_INFO_VIDEO_IS_DELETED_CODE_445);
                    jsonObject
                            .put("errorMsg",
                                    ResponseStatus.SHOPPING_INFO_VIDEO_IS_DELETED_MSG_445);
                } else {
                    jsonObject.put("response", ResponseStatus.SUCCESS);
                    jsonObject.put("videoUrlInfo", videoUrlInfoQuery);
                }
            } else {
                jsonObject.put("response", ResponseStatus.FAIL);
                jsonObject
                        .put("errorCode",
                                ResponseStatus.SHOPPING_INFO_VIDEO_IS_NOT_EXIST_CODE_444);
                jsonObject
                        .put("errorMsg",
                                ResponseStatus.SHOPPING_INFO_VIDEO_IS_NOT_EXIST_MSG_444);
                // 如果视频不存在则通知工控机上传视频至本地视频服务器
                JSONObject jsonObjectToAndroid = new JSONObject();
                jsonObjectToAndroid.put("response", "getShoppingInfoVideos");
                jsonObjectToAndroid.put("shoppingInfoId", shoppingInfoId);
                jsonObjectToAndroid.put("videoUrlInfo", videoUrlInfo);
                Session session = SessionMapFactory.getInstance()
                        .getCurrentSession(
                                ResponseStatus.CUSTOM_CATEGORY_ANDROID, boxId);
                try {
                    SessionMapFactory.getInstance().sendMessage(session,
                            jsonObjectToAndroid.toString());
                } catch (IOException e) {
                    log.error("try->catch exception:", e);
                    e.printStackTrace();
                }
            }
        } else {
            jsonObject.put("response", ResponseStatus.FAIL);
            jsonObject.put("errorCode",
                    ResponseStatus.SHOPPING_INFO_VIDEO_IS_NOT_EXIST_CODE_444);
            jsonObject.put("errorMsg",
                    ResponseStatus.SHOPPING_INFO_VIDEO_IS_NOT_EXIST_MSG_444);
        }
        return jsonObject.toString();
    }

    public String doUpdateShoppingInfoVideosUrl(String shoppingInfoId,
            String videoUrlInfo, String boxId) {
        ShoppingInfo shoppingInfo = new ShoppingInfo();
        shoppingInfo.setShoppingInfoId(shoppingInfoId);
        shoppingInfo.setVideoUrlInfo(videoUrlInfo);
        if (updateShoppingInfoVideosUrl(shoppingInfo)) {
            return ResponseStatus.SUCCESS;
        } else {
            return ResponseStatus.FAIL;
        }
    }

    public void doUpdateShoppingInfoOrderId(String orderId,
            String shoppingInfoId) {
        ShoppingInfo shoppingInfo = new ShoppingInfo();
        shoppingInfo.setOrderId(orderId);
        shoppingInfo.setShoppingInfoId(shoppingInfoId);
        updateShoppingInfoOrderId(shoppingInfo);
    }

}
