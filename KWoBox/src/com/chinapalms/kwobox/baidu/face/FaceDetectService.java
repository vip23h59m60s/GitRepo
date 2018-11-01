package com.chinapalms.kwobox.baidu.face;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;

import com.baidu.aip.face.AipFace;
import com.chinapalms.kwobox.javabean.BoxBody;
import com.chinapalms.kwobox.javabean.BoxStatus;
import com.chinapalms.kwobox.javabean.Boxes;
import com.chinapalms.kwobox.javabean.CurrentUser;
import com.chinapalms.kwobox.javabean.Customer;
import com.chinapalms.kwobox.javabean.DoorOpenedCallback;
import com.chinapalms.kwobox.javabean.ShopFaceDetect;
import com.chinapalms.kwobox.javabean.User;
import com.chinapalms.kwobox.servelet.KWoBoxServelet;
import com.chinapalms.kwobox.service.BoxStatusService;
import com.chinapalms.kwobox.service.BoxesService;
import com.chinapalms.kwobox.service.CurrentUserService;
import com.chinapalms.kwobox.service.CustomerService;
import com.chinapalms.kwobox.service.DoorOpenedCallbackService;
import com.chinapalms.kwobox.service.OrderService;
import com.chinapalms.kwobox.service.ShopFaceDetectService;
import com.chinapalms.kwobox.service.UserService;
import com.chinapalms.kwobox.utils.GlobalUtils;
import com.chinapalms.kwobox.utils.ResponseStatus;
import com.custom.kwobox.service.CustomCurrentUserService;
import com.custom.kwobox.service.CustomDynamicPasswordService;
import com.custom.kwobox.service.CustomFaceDetectService;

public class FaceDetectService extends AipFace {

    Log log = LogFactory.getLog(FaceDetectService.class);

    public FaceDetectService(String appId, String apiKey, String secretKey) {
        super(appId, apiKey, secretKey);
    }

    // 小程序中调用上传并注册人脸
    public String doUploadFaceDetectSettingPhotoAndFaceRegister(
            HttpServletRequest request, HttpServletResponse response) {
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
                    + File.separator + "faceDetect";
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
            String phoneNumber = request.getAttribute("phoneNumber").toString();
            String nickName = request.getAttribute("nickName").toString();
            String faceDetectPassword = request.getAttribute(
                    "faceDetectPassword").toString();
            path = path + File.separator + faceDetectPassword;
            dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String fileSuffix = picture.getName().substring(
                    picture.getName().lastIndexOf("."));
            String fileName = phoneNumber + fileSuffix;
            String destPath = path + File.separator + fileName;
            log.info("destPath=" + destPath);
            log.info("nickName=" + nickName);

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
            String faceImage = destPath;

            // 若果之前有注册过，先删除之前注册数据后，重新注册
            UserService userService = new UserService();
            User userQuery = userService.queryUserByPhoneNumber(phoneNumber);
            // 注册人脸时检查当前注册人脸是否与已注册同密码人脸切phoneNumber（userId）不同的用户，如果有则不允许注册，并且同时关闭之前用户,避免风险
            if (userQuery != null
                    && (userQuery.getFaceFunction() == 0 || userQuery
                            .getFaceFunction() == 1)) {
                String multiIdentifyResult = doMultiIdentify(
                        faceDetectPassword, faceImage, false);
                log.info("FaceDetectService:->doUploadFaceDetectSettingPhotoAndFaceRegister->"
                        + multiIdentifyResult);
                // 注册人脸时检查当前注册人脸是否与已注册同密码人脸切phoneNumber（userId）不同的用户，如果有则不允许注册，并且同时关闭之前用户,避免风险
                if (!multiIdentifyResult.equals(ResponseStatus.FAIL)) {
                    String phoneNumberMatch = JSONObject.fromObject(
                            multiIdentifyResult).getString("phoneNumber");
                    if (!phoneNumberMatch.equals(phoneNumber)) {
                        // 匹配到相同的其他人的人脸，暂停其开通，并将另外一个人的人脸功能关闭,设置为2，异常
                        // 先将本人人脸识别功能关闭，设置为2，异常状态
                        User updateCurrentUser = new User();
                        updateCurrentUser.setPhoneNumber(phoneNumber);
                        updateCurrentUser.setFaceFunction(2);
                        updateCurrentUser.setFaceFunctionPassword("000000");
                        updateCurrentUser
                                .setFaceFunctionPicture("./images/000.png");
                        userService.updateFaceFuntion(updateCurrentUser);

                        // 同时将另外一个人的人脸功能关闭,设置为2，异常
                        String phoneNumberFromJson = JSONObject.fromObject(
                                multiIdentifyResult).getString("phoneNumber");
                        User updateOtherUser = new User();
                        updateOtherUser.setPhoneNumber(phoneNumberFromJson);
                        updateOtherUser.setFaceFunction(2);
                        updateOtherUser.setFaceFunctionPassword("000000");
                        updateOtherUser
                                .setFaceFunctionPicture("./images/000.png");
                        userService.updateFaceFuntion(updateOtherUser);

                        // 同时将该两个人脸注册信息从百度人脸库中删除
                        doDeleteUser(faceDetectPassword, phoneNumber,
                                faceImage, true);
                        doDeleteUser(faceDetectPassword, phoneNumberFromJson,
                                faceImage, true);
                        return ResponseStatus.FACE_FUNCTION_NOT_ALLOW;
                    }
                }
            } else if (userQuery != null && userQuery.getFaceFunction() == 2) {
                return ResponseStatus.FAIL;
            }
            if (userQuery != null && userQuery.getFaceFunction() == 1
                    && userQuery.getFaceFunctionPassword() != null
                    && userQuery.getFaceFunctionPicture() != null) {
                String deleteFaceImage = request.getSession()
                        .getServletContext().getRealPath("/")
                        + "../"
                        + "ROOT"
                        + File.separator
                        + userQuery.getFaceFunctionPicture();
                // if (!deleteFaceImage.equals(faceImage)) {
                doDeleteUser(userQuery.getFaceFunctionPassword(), phoneNumber,
                        deleteFaceImage, deleteFaceImage.equals(faceImage));
                // }
                // 注册人脸时检查当前注册人脸是否与已注册同密码人脸切phoneNumber（userId）不同的用户，如果有则不允许注册，并且同时关闭之前用户,避免风险
            }
            if (doAddUser(phoneNumber, nickName, faceDetectPassword, faceImage)
                    .equals(ResponseStatus.SUCCESS)
                    && updateFaceFuntion(phoneNumber, 1, faceDetectPassword,
                            "pictures/faceDetect/" + faceDetectPassword + "/"
                                    + fileName)) {
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

    public String doFaceDetect(HttpServletRequest request,
            HttpServletResponse response) {
        try {
            String startUploadTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .format(new Date(System.currentTimeMillis()));
            log.info("FaceDetect:->startUploadTime=" + startUploadTime);
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
                    + File.separator + "faceDetectTemp";
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
            // 人脸识别类型：门店免密模式还是单售货机非免密模式
            String faceType = request.getAttribute(FaceDetectConfig.FACE_TYPE) != null ? request
                    .getAttribute(FaceDetectConfig.FACE_TYPE).toString()
                    : FaceDetectConfig.FACE_TYPE_NORMAL;

            // 针对Yoho和小e模式的柜子临时做处理，不让开门
            // BoxesService boxesServiceTemp = new BoxesService();
            // Boxes boxesTemp = boxesServiceTemp.findBoxesByBoxId(boxId);
            // if (boxesTemp != null) {
            // // 根据商户ID查询商户合作模式
            // CustomerService customerService = new CustomerService();
            // Customer customer = customerService
            // .findCustomerByCustomerId(boxesTemp.getCustomerId());
            // // 判断是否是小e服务器对接模式
            // if (customer != null) {
            // DoorOpenedCallbackService doorOpenedCallbackService = new
            // DoorOpenedCallbackService();
            // DoorOpenedCallback doorOpenedCallback = doorOpenedCallbackService
            // .findDoorOpenedCallbackByCustomerId(boxesTemp
            // .getCustomerId());
            // int cooperationMode = customer.getCooperationMode();
            // if (doorOpenedCallback != null) {
            // // 判断是否是小e服务器堆积或Yoho代理模式
            // if (cooperationMode ==
            // ResponseStatus.COOPERATIONMODE_THIRD_INTERFACE
            // || cooperationMode == ResponseStatus.COOPERATIONMODE_AGENT) {
            // return ResponseStatus.FAIL;
            // }
            // }
            // }
            // }
            // 针对Yoho和小e模式的柜子临时做处理，不让开门

            // 如果不是门店免密模式，需要获取密码进行验证
            String faceDetectPassword = "000000";
            if (!faceType.equals(FaceDetectConfig.FACE_TYPE_SHOP)) {
                faceDetectPassword = request.getAttribute("faceDetectPassword")
                        .toString();
            }

            log.info("boxId=" + boxId + ", faceDetectPassword="
                    + faceDetectPassword + ",picture.getName="
                    + picture.getName());
            dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String filePre = FaceDetectRandomStringGenerator
                    .makeSystemCurrentTimeMillisStyleRandomString(20);
            String fileSuffix = picture.getName().substring(
                    picture.getName().lastIndexOf("."));
            log.info("FaceDetect:fileSuffix=" + fileSuffix);
            String fileName = filePre + fileSuffix;
            String destPath = path + File.separator + fileName;

            // 真正写到磁盘上
            File file = new File(destPath);
            OutputStream out = new FileOutputStream(file);
            InputStream in = picture.getInputStream();
            int length = 0;
            byte[] buf = new byte[1024];
            // byte[] buf = new byte[2048];
            // in.read(buf) 每次读到的数据存放在buf 数组中
            while ((length = in.read(buf)) != -1) {
                // 在buf数组中取出数据写到（输出流）磁盘上
                out.write(buf, 0, length);
            }
            in.close();
            in = null;
            out.close();
            out = null;
            String endUploadTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .format(new Date(System.currentTimeMillis()));
            log.info("FaceDetect:->endUploadTime=" + endUploadTime);
            String faceImage = destPath;

            String groupId = faceDetectPassword;

            // ============对接商户服务器逻辑==========================================================================
            // 判断是否是第三方对接模式(Yoho模式),如果是Yoho模式，将异常信息回调至商户服务器订单回调页面，以便于商户服务器更新个人征信分和通知商户微信客户端
            // 通过boxId查找商户ID，再根据商户ID查找回调Url，返回商户服务器，与商户服务器对接,如果无回调Url，则默认走智购猫平台逻辑
            CustomFaceDetectService customFaceDetectService = new CustomFaceDetectService();
            String faceDetectCallbackUrlObject = customFaceDetectService
                    .getCustomerFaceDetectCallbackUrl(boxId);
            if (faceDetectCallbackUrlObject.equals(ResponseStatus.FAIL)) {
                String multiIdentifyResult = ResponseStatus.FAIL;
                // 先判断是否是门店人脸识别免密模式
                if (faceType.equals(FaceDetectConfig.FACE_TYPE_SHOP)) {
                    ShopFaceDetectService shopFaceDetectService = new ShopFaceDetectService();
                    // 根据boxId查询shopId
                    BoxesService boxesService = new BoxesService();
                    Boxes box = boxesService.findBoxesByBoxId(boxId);
                    // 密码使用门店风格密码
                    groupId = "shop_" + box.getShopId();
                    multiIdentifyResult = doMultiIdentify(groupId, faceImage,
                            false);
                    if (!multiIdentifyResult.equals(ResponseStatus.FAIL)) {
                        log.info("shop FaceDetect success multiIdentifyResult="
                                + multiIdentifyResult);
                        String phoneNumber = JSONObject.fromObject(
                                multiIdentifyResult).getString("phoneNumber");
                        // 判断当前门店免密模式识别出来的user是否属于有效状态
                        if (!shopFaceDetectService.checkShopDetectState(
                                box.getShopId(), phoneNumber)) {
                            log.info("shopFace Detect checkShopDetectState fail");
                            multiIdentifyResult = ResponseStatus.FAIL;
                        }
                    } else {
                        log.info("shop FaceDetect fail multiIdentifyResult="
                                + multiIdentifyResult);
                        multiIdentifyResult = ResponseStatus.FAIL;
                    }
                } else {
                    // 普通售货机非免密识别模式
                    multiIdentifyResult = doMultiIdentify(groupId, faceImage,
                            false);
                }

                if (!multiIdentifyResult.equals(ResponseStatus.FAIL)) {
                    String phoneNumber = JSONObject.fromObject(
                            multiIdentifyResult).getString("phoneNumber");
                    log.info("multiIdentifyResult->phoneNumber=userId=="
                            + phoneNumber);
                    String contractId = findPapayContractIdByPhoneNumber(phoneNumber);
                    // 判断是否开通人脸识别功能
                    // (暂时忽略是否开通免密)
                    if (!contractId.equals(ResponseStatus.NO_OPEN_PAPAY)) {
                        log.info("FaceDetect ->>start doRegisterCurrentUser...");
                        CurrentUserService currentUserService = new CurrentUserService();
                        CurrentUser currentUser = new CurrentUser();
                        currentUser.setPhoneNumber(phoneNumber);
                        currentUser.setCustomerWorkerId(0);
                        currentUser.setBoxId(boxId);
                        currentUser
                                .setCustomType(ResponseStatus.CUSTOM_CATEGORY_WX);
                        currentUser
                                .setUserType(ResponseStatus.PERMISSION_NORMAL_USER);
                        currentUser.setAgreementNO(contractId);
                        // 检查是否有未支付订单
                        OrderService orderService = new OrderService();
                        if (!orderService.doCheckNoPayOrder(phoneNumber)
                                .equals(ResponseStatus.FAIL)) {
                            // 验证柜子状态
                            BoxesService boxesService = new BoxesService();
                            Boxes boxes = new Boxes();
                            boxes.setBoxId(boxId);
                            String identifyResult = boxesService
                                    .doOpenDoorIdentify(boxes, phoneNumber);
                            log.info("identifyResult=" + identifyResult);

                            if (identifyResult
                                    .equals(ResponseStatus.PERSONAL_CREDIT_TOO_LOW)) {
                                // 个人征信过低，暂时用BOX_BROKEN_DOWN 代替
                                return ResponseStatus.PERSONAL_CREDIT_TOO_LOW;
                            } else if (identifyResult
                                    .equals(ResponseStatus.BOX_BUSY)) {
                                return ResponseStatus.BOX_BUSY;
                            } else if (identifyResult
                                    .equals(ResponseStatus.BOX_BROKEN_DOWN)) {
                                return ResponseStatus.BOX_BROKEN_DOWN;
                            } else {
                                // 注册当前柜子使用用户
                                if (currentUserService.doRegisterCurrentUser(
                                        currentUser).equals(
                                        ResponseStatus.SUCCESS)) {
                                    log.info("FaceDetect ->>doRegisterCurrentUser success...");
                                    // 验证通过，通知主控开门
                                    log.info("FaceDetect->notify Android to open door!!!");
                                    notifyAndroidOpenDoor(
                                            ResponseStatus.OPEN_DOOR_REQUEST_SERIALNUMBER_NORMAL,
                                            ResponseStatus.CUSTOM_CATEGORY_ANDROID,
                                            boxId,
                                            ResponseStatus.PERMISSION_NORMAL_USER);
                                    // 将匹配成功并验证开门成功的人脸加入到门店人脸库
                                    // 并且当前识别为非shop识别方式
                                    if (!faceType
                                            .equals(FaceDetectConfig.FACE_TYPE_SHOP)) {
                                        addShopFaceDetectUser(request,
                                                phoneNumber, boxId, true);
                                    }
                                    return ResponseStatus.SUCCESS;
                                } else {
                                    return ResponseStatus.FAIL;
                                }
                            }
                        } else {
                            return ResponseStatus.NO_PAYED_ORDER;
                        }
                    } else {
                        // 未开通免密委托代扣
                        log.info("FaceDetecrt->NO_OPEN_PAPAY");
                        return ResponseStatus.NO_OPEN_PAPAY;
                    }
                } else {
                    // 人脸匹配失败
                    log.info("FaceDetecrt->FACE_MATCH_ERROR");
                    return ResponseStatus.FACE_MATCH_ERROR;
                }

            } else {
                // 走回调商户服务器流程
                // 走商户识别流程时，需要在password（groupId）前加customerId xx_groupId
                // 根据boxId查询商户customerId
                int customerId = 0;
                BoxesService boxesService = new BoxesService();
                Boxes boxes = boxesService.findBoxesByBoxId(boxId);
                if (boxes != null) {
                    CustomerService customerService = new CustomerService();
                    Customer customer = customerService
                            .findCustomerByCustomerId(boxes.getCustomerId());
                    if (customer != null) {
                        customerId = customer.getCustomerId();
                    }
                    String multiIdentifyResult = doMultiIdentifyOnly(customerId
                            + "_" + groupId, faceImage, false);
                    if (!multiIdentifyResult.equals(ResponseStatus.FAIL)) {
                        String phoneNumber = JSONObject.fromObject(
                                multiIdentifyResult).getString("phoneNumber");
                        log.info("doCheckFaceDetect multiIdentifyResult->phoneNumber=userId=="
                                + phoneNumber);
                        int urlType = JSONObject.fromObject(
                                faceDetectCallbackUrlObject).getInt("urlType");
                        // 如果是Yoho代理模式,则把订单信息回调给代理开发服务器
                        if (urlType == ResponseStatus.COOPERATIONMODE_AGENT) {
                            String checkResult = customFaceDetectService
                                    .notifyCustomerServerToCheckFaceDetect(
                                            faceDetectCallbackUrlObject, boxId,
                                            phoneNumber);
                            JSONObject checkResultJsonObject = JSONObject
                                    .fromObject(checkResult);
                            String checkResultResponse = checkResultJsonObject
                                    .getString("response");
                            log.info("checkResultResponse="
                                    + checkResultResponse);
                            if (checkResultResponse
                                    .equals(ResponseStatus.SUCCESS)) {
                                String phoneNumberFromCustom = checkResultJsonObject
                                        .getString("phoneNumber");
                                String boxIdFromCustom = checkResultJsonObject
                                        .getString("boxId");
                                // 检查柜子状态
                                BoxStatusService boxStatusService = new BoxStatusService();
                                BoxStatus boxStatus = boxStatusService
                                        .findBoxStatusByBoxId(boxIdFromCustom);
                                if (boxStatus != null) {
                                    int state = boxStatus.getState();
                                    if (state == ResponseStatus.BOX_STATUS_BUSY) {
                                        return ResponseStatus.BOX_BUSY;
                                    } else if (state == ResponseStatus.BOX_STATUS_BROKEN_DOWN) {
                                        return ResponseStatus.BOX_BROKEN_DOWN;
                                    } else {
                                        // 注册当前用户
                                        log.info("doCheckFaceDetectFromCustomServer ->>start doRegisterCurrentUser...");
                                        CustomCurrentUserService customCurrentUserService = new CustomCurrentUserService();
                                        CurrentUser currentUser = new CurrentUser();
                                        currentUser
                                                .setPhoneNumber(phoneNumberFromCustom);
                                        currentUser.setCustomerWorkerId(0);
                                        currentUser.setBoxId(boxIdFromCustom);
                                        currentUser
                                                .setCustomType(ResponseStatus.CUSTOM_CATEGORY_WX);
                                        currentUser
                                                .setUserType(ResponseStatus.PERMISSION_NORMAL_USER);
                                        currentUser.setVipLevel(1);
                                        currentUser.setAgreementNO("0");
                                        if (customCurrentUserService
                                                .registerCurrentUser(
                                                        currentUser).equals(
                                                        ResponseStatus.SUCCESS)) {
                                            // 返回工控机结果，工控机开门
                                            log.info("FaceDetect->notify Android to open door!!!");
                                            notifyAndroidOpenDoor(
                                                    ResponseStatus.OPEN_DOOR_REQUEST_SERIALNUMBER_NORMAL,
                                                    ResponseStatus.CUSTOM_CATEGORY_ANDROID,
                                                    boxId,
                                                    ResponseStatus.PERMISSION_NORMAL_USER);
                                            return ResponseStatus.SUCCESS;
                                        } else {
                                            return ResponseStatus.FAIL;
                                        }
                                    }
                                } else {
                                    return ResponseStatus.SUCCESS;
                                }
                            } else {
                                // 从商户服务器返回的错误验证状态结果返回给工控机进行处理
                                return checkResultResponse;
                            }
                        } else {
                            return ResponseStatus.FAIL;
                        }
                    } else {
                        // 人脸匹配失败
                        log.info("FaceDetecrt->FACE_MATCH_ERROR");
                        return ResponseStatus.FACE_MATCH_ERROR;
                    }
                }
            }
        } catch (Exception e) {
            log.info("doFaceDetect exception;->" + e.toString());
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        return ResponseStatus.FAIL;
    }

    // 调用百度API:进行人脸注册
    private String doAddUser(String userId, String userInfo, String groupId,
            String faceImage) {
        org.json.JSONObject jsonObject = addUser(userId, userInfo, groupId,
                faceImage, new HashMap<String, String>());
        if (jsonObject != null) {
            log.info("addUser->jsonObject=" + jsonObject.toString());
            if (jsonObject.has("error_code")) {
                return ResponseStatus.FAIL;
            } else {
                return ResponseStatus.SUCCESS;
            }
        } else {
            return ResponseStatus.FAIL;
        }
    }

    // 调用百度API：进行人脸删除操作
    private String doDeleteUser(String groupId, String uid, String faceImage,
            boolean isFaceImageSamePath) {
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("group_id", groupId);
        org.json.JSONObject jsonObject = deleteUser(uid, options);
        if (jsonObject != null) {
            log.info("doDeleteUser->jsonObject=" + jsonObject.toString());
            if (jsonObject.has("error_code")) {
                return ResponseStatus.FAIL;
            } else {
                // 如果重新录入的照片和原来的照片不在一个目录（group）中，则删除本地照片，否则不删除直接覆盖，不然一边删除，一边注册人脸会有问题
                if (!isFaceImageSamePath) {
                    deleteLocalFaceImage(faceImage);
                }
                return ResponseStatus.SUCCESS;
            }
        } else {
            return ResponseStatus.FAIL;
        }
    }

    // 调用百度API:根据userId进行单个验证进行人脸验证
    public org.json.JSONObject doVerifyUser(HttpServletRequest request) {
        String photo = request.getSession().getServletContext()
                .getRealPath("/")
                + "../"
                + "ROOT"
                + File.separator
                + "pictures"
                + File.separator
                + "faceDetect"
                + File.separator
                + "123456"
                + File.separator
                + "18621716031.jpg";
        org.json.JSONObject jsonObject = verifyUser("18621716032", "123456",
                photo, new HashMap<String, String>());
        log.info("verifyUser->jsonObject=" + jsonObject.toString());
        return jsonObject;
    }

    // 调用百度API:根据groupId进行M:N验证
    public String doMultiIdentify(String findGroupId, String faceImage,
            boolean deleteLocalImage) {
        String matchResult = ResponseStatus.FAIL;
        UserService userService = new UserService();
        try {
            String startFaceDetectTime = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss").format(new Date(System
                    .currentTimeMillis()));
            log.info("FaceDetect:->startFaceDetectTime=" + startFaceDetectTime);
            HashMap<String, String> faceParaMap = new HashMap<String, String>();
            // 活体检测参数
            faceParaMap.put("ext_fields", "faceliveness");
            org.json.JSONObject jsonObject = multiIdentify(findGroupId,
                    faceImage, faceParaMap);
            log.info("multiIdentify->jsonObject=" + jsonObject.toString());
            if (jsonObject.has("result") && jsonObject.has("result_num")
                    && jsonObject.getInt("result_num") > 0) {
                String groupId = jsonObject.getJSONArray("result")
                        .getJSONObject(0).getString("group_id");
                String userId = jsonObject.getJSONArray("result")
                        .getJSONObject(0).getString("uid");
                String userInfo = jsonObject.getJSONArray("result")
                        .getJSONObject(0).getString("user_info");
                Double scores = jsonObject.getJSONArray("result")
                        .getJSONObject(0).getJSONArray("scores").getDouble(0);
                Double facelivenessScores = Double.valueOf(jsonObject
                        .getJSONObject("ext_info").get("faceliveness")
                        .toString());
                log.info("FaceDetect->" + "groupId=" + groupId + ",userId="
                        + userId + ",userInfo=" + userInfo + ",scores="
                        + scores + ",facelivenessScores=" + facelivenessScores);
                User user = userService.queryUserByPhoneNumber(userId);
                if (user != null) {
                    log.info("FaceDetect-> phoneNumber=" + userId
                            + ",faceFunction=" + user.getFaceFunction());
                }
                if ((user != null && user.getFaceFunction() == 1)
                        && scores >= FaceDetectConfig.FACE_MATCH_BASELINE_SCORES
                        && facelivenessScores > FaceDetectConfig.FACE_MATCH_FACELIVENESS_BASELINE_SCORES) {
                    org.json.JSONObject resultJsonObject = new org.json.JSONObject();
                    resultJsonObject.put("phoneNumber", userId);
                    matchResult = resultJsonObject.toString();
                } else {
                    matchResult = ResponseStatus.FAIL;
                }
            } else {
                matchResult = ResponseStatus.FAIL;
            }
            String endFaceDetectTime = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss").format(new Date(System
                    .currentTimeMillis()));
            log.info("FaceDetect:->endFaceDetectTime=" + endFaceDetectTime);
            log.info("FaceDetect match=->" + matchResult);
            // 人脸匹配完成后，不管成功还是失败，删除该本地文件
            if (deleteLocalImage) {
                deleteLocalFaceImage(faceImage);
            }
        } catch (Exception e) {
            log.error("FaceDetect:->doMultiIdentify happen exception!!!!!,"
                    + e.toString());
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        return matchResult;
    }

    // 纯百度api识别，真对Yoho等第三方对接识别不需要判断平台数据库中是否开通人脸识别功能，而是从第三方拿是否开通等数据
    // 调用百度API:根据groupId进行M:N验证
    public String doMultiIdentifyOnly(String findGroupId, String faceImage,
            boolean deleteLocalImage) {
        String matchResult = ResponseStatus.FAIL;
        UserService userService = new UserService();
        try {
            String startFaceDetectTime = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss").format(new Date(System
                    .currentTimeMillis()));
            log.info("FaceDetect:->startFaceDetectTime=" + startFaceDetectTime);
            HashMap<String, String> faceParaMap = new HashMap<String, String>();
            // 活体检测参数
            faceParaMap.put("ext_fields", "faceliveness");
            org.json.JSONObject jsonObject = multiIdentify(findGroupId,
                    faceImage, faceParaMap);
            log.info("multiIdentify->jsonObject=" + jsonObject.toString());
            if (jsonObject.has("result") && jsonObject.has("result_num")
                    && jsonObject.getInt("result_num") > 0) {
                String groupId = jsonObject.getJSONArray("result")
                        .getJSONObject(0).getString("group_id");
                String userId = jsonObject.getJSONArray("result")
                        .getJSONObject(0).getString("uid");
                String userInfo = jsonObject.getJSONArray("result")
                        .getJSONObject(0).getString("user_info");
                Double scores = jsonObject.getJSONArray("result")
                        .getJSONObject(0).getJSONArray("scores").getDouble(0);
                Double facelivenessScores = Double.valueOf(jsonObject
                        .getJSONObject("ext_info").get("faceliveness")
                        .toString());
                log.info("FaceDetect->" + "groupId=" + groupId + ",userId="
                        + userId + ",userInfo=" + userInfo + ",scores="
                        + scores + ",facelivenessScores=" + facelivenessScores);
                User user = userService.queryUserByPhoneNumber(userId);
                if (user != null) {
                    log.info("FaceDetect-> phoneNumber=" + userId
                            + ",faceFunction=" + user.getFaceFunction());
                }
                if (scores >= FaceDetectConfig.FACE_MATCH_BASELINE_SCORES
                        && facelivenessScores > FaceDetectConfig.FACE_MATCH_FACELIVENESS_BASELINE_SCORES) {
                    org.json.JSONObject resultJsonObject = new org.json.JSONObject();
                    resultJsonObject.put("phoneNumber", userId);
                    matchResult = resultJsonObject.toString();
                } else {
                    matchResult = ResponseStatus.FAIL;
                }
            } else {
                matchResult = ResponseStatus.FAIL;
            }
            String endFaceDetectTime = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss").format(new Date(System
                    .currentTimeMillis()));
            log.info("FaceDetect:->endFaceDetectTime=" + endFaceDetectTime);
            log.info("FaceDetect match=->" + matchResult);
            // 人脸匹配完成后，不管成功还是失败，删除该本地文件
            if (deleteLocalImage) {
                deleteLocalFaceImage(faceImage);
            }
        } catch (Exception e) {
            log.error("FaceDetect:->doMultiIdentify happen exception!!!!!,"
                    + e.toString());
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        return matchResult;
    }

    // 人脸匹配完成后，不管成功还是失败，删除该本地文件
    private boolean deleteLocalFaceImage(String faceImage) {
        boolean deleteFlag = false;
        try {
            File file = new File(faceImage);
            if (file.exists() && file.isFile()) {
                if (file.delete()) {
                    log.info("FaceDetect->delete temp faceImage:" + faceImage
                            + " success");
                    deleteFlag = true;
                } else {
                    log.info("FaceDetect->delete temp faceImage:" + faceImage
                            + " fail");
                    deleteFlag = false;
                }
            } else {
                log.info("FaceDetect->delete temp faceImage else:" + faceImage
                        + " fail");
                deleteFlag = false;
            }
        } catch (Exception e) {
            deleteFlag = false;
            log.error("FaceDetect->delete temp faceImage else:" + faceImage
                    + " fail" + ", Exception:" + e.toString());
        }
        return deleteFlag;
    }

    // 数据库人脸识别信息同步更新
    public boolean updateFaceFuntion(String phoneNumber, int faceFunction,
            String faceDetectPassword, String faceFunctionPicture) {
        UserService userService = new UserService();
        User user = new User();
        user.setPhoneNumber(phoneNumber);
        user.setFaceFunction(1);
        user.setFaceFunctionPassword(faceDetectPassword);
        user.setFaceFunctionPicture(faceFunctionPicture);
        return userService.updateFaceFuntion(user);
    }

    private String findPapayContractIdByPhoneNumber(String phoneNumber) {
        UserService userService = new UserService();
        User user = userService.queryUserByPhoneNumber(phoneNumber);
        if (user != null
                && !user.getWxId().equals(ResponseStatus.NO_CONTRACT_ID)) {
            return user.getWxId();
        } else {
            return ResponseStatus.NO_OPEN_PAPAY;
        }
    }

    /**
     * 通知Android设备去开门
     * 
     * @param customCategory
     * @param boxId
     */
    private void notifyAndroidOpenDoor(String openDoorRequestSerial,
            String customCategory, String boxId, String userPermission) {
        BoxesService boxService = new BoxesService();
        try {
            boxService.notifyAndroidOpenDoor(openDoorRequestSerial,
                    customCategory, boxId, userPermission);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    /**
     * 将在当前门店识别人脸并开门成功的用户注册进门店人脸库，以实现门店人脸识别免密功能
     * 
     * @param request
     * @param phoneNumber
     * @param boxId
     * @param isSelf
     *            是否自营平台还是第三方（Yoho或者小e对接模式）
     * @return
     */
    private String addShopFaceDetectUser(HttpServletRequest request,
            String phoneNumber, String boxId, boolean isSelf) {
        try {
            // 根据boxId查询shopId
            int shopId = 0;
            BoxesService boxesService = new BoxesService();
            Boxes boxes = boxesService.findBoxesByBoxId(boxId);
            if (boxes != null) {
                shopId = boxes.getShopId();
            } else {
                return ResponseStatus.FAIL;
            }
            String faceImage = null;
            // 自营店并且shopId != 0 时注册门店人脸识别
            if (isSelf && shopId != 0) {
                // 如果是自营平台,从当前数据库中查找出人脸照片进行注册，否则从商户对接服务器请求人脸图片路径后进行人脸注册
                UserService userService = new UserService();
                User user = userService.queryUserByPhoneNumber(phoneNumber);
                if (user != null) {
                    faceImage = request.getSession().getServletContext()
                            .getRealPath("/")
                            + "../"
                            + "ROOT"
                            + File.separator
                            + user.getFaceFunctionPicture();
                }
            } else {
                // 如果不是自营模式，人脸图片路径请求商户对接服务器获取
                // ============对接商户服务器逻辑==========================================================================
                // 判断是否是第三方对接模式(Yoho模式),如果是Yoho模式，将异常信息回调至商户服务器订单回调页面，以便于商户服务器更新个人征信分和通知商户微信客户端
                // 通过boxId查找商户ID，再根据商户ID查找回调Url，返回商户服务器，与商户服务器对接,如果无回调Url，则默认走智购猫平台逻辑
                // CustomFaceDetectService customFaceDetectService = new
                // CustomFaceDetectService();
                // String faceDetectCallbackUrlObject = customFaceDetectService
                // .getCustomerFaceDetectCallbackUrl(boxId);
                // customFaceDetectService.notifyCustomerServerToGetFaceDetectImage(faceDetectCallbackUrlObject,
                // phoneNumber);
                // 非自营的先忽略
                return ResponseStatus.FAIL;
            }

            // 门店人脸识别库密码定义为shop_xxx xxx为shopId
            String multiIdentifyResult = doMultiIdentify("shop_" + shopId,
                    faceImage, false);
            log.info("shop multiIdentifyResult111=" + multiIdentifyResult);
            // 注册人脸时检查当前注册人脸是否与已注册同密码人脸切phoneNumber（userId）不同的用户，如果有则不允许注册，并且同时关闭之前用户,避免风险
            if (!multiIdentifyResult.equals(ResponseStatus.FAIL)) {
                String phoneNumberMatch = JSONObject.fromObject(
                        multiIdentifyResult).getString("phoneNumber");
                if (!phoneNumberMatch.equals(phoneNumber)) {
                    // 匹配到相同的其他人的人脸，暂停其开通，并将另外一个人的门店人脸功能关闭,设置为0，异常
                    // 先将本人人脸识别功能关闭，设置为0，关闭状态，不再允许该用户再次在该门店进行门店免密购物
                    ShopFaceDetectService shopFaceDetectService = new ShopFaceDetectService();
                    ShopFaceDetect currentShopFaceDetect = new ShopFaceDetect();
                    currentShopFaceDetect.setPhoneNumber(phoneNumber);
                    currentShopFaceDetect.setState(0);
                    shopFaceDetectService
                            .updateShopFaceDetectState(currentShopFaceDetect);

                    // 同时将另外一个人的门店人脸功能关闭,设置为0
                    String phoneNumberFromJson = JSONObject.fromObject(
                            multiIdentifyResult).getString("phoneNumber");
                    ShopFaceDetect otherShopFaceDetect = new ShopFaceDetect();
                    otherShopFaceDetect.setPhoneNumber(phoneNumberFromJson);
                    otherShopFaceDetect.setState(0);
                    shopFaceDetectService
                            .updateShopFaceDetectState(otherShopFaceDetect);

                    // 同时将该两个人脸注册信息从百度人脸库中删除
                    doDeleteUser("shop_" + shopId, phoneNumber, faceImage, true);
                    doDeleteUser("shop_" + shopId, phoneNumberFromJson,
                            faceImage, true);
                    return ResponseStatus.FAIL;
                } else {
                    // 如果在该门店中匹配到的人脸是唯一的并且已经注册，只用更新最后一次购物时间即可,否则提添加该用户记录
                    ShopFaceDetectService shopFaceDetectService = new ShopFaceDetectService();
                    ShopFaceDetect shopFaceDetect = new ShopFaceDetect();
                    shopFaceDetect.setShopId(shopId);
                    shopFaceDetect.setPhoneNumber(phoneNumber);
                    shopFaceDetect.setState(1);
                    shopFaceDetect.setLastFaceDetectShoppingTime(new Date());
                    ShopFaceDetect shopFaceDetectQuery = shopFaceDetectService
                            .findShopFaceDetectByShopIdAndPhoneNumber(shopId,
                                    phoneNumber);
                    // 如果该手机号码存在并且没有没有被封杀
                    if (shopFaceDetectQuery != null) {
                        if (shopFaceDetectQuery.getState() != 0) {
                            shopFaceDetectService
                                    .updateLastShopFaceDetectShoppingTime(shopFaceDetect);
                            return ResponseStatus.SUCCESS;
                        } else {
                            return ResponseStatus.FAIL;
                        }
                    } else {
                        // 将该条人脸记录加入到门店数据库记录中
                        shopFaceDetectService.addShopFaceDetect(shopFaceDetect);
                        return ResponseStatus.SUCCESS;
                    }
                }
            } else {
                // 没有在该门店中未匹配到当前人脸，则将当前成功开门的用户人脸注册进门店人脸库中,并保存记录
                // 如果当前记录是已经被封杀的记录，则不再做注册处理，直接返回false
                ShopFaceDetectService shopFaceDetectService = new ShopFaceDetectService();

                ShopFaceDetect shopFaceDetect = new ShopFaceDetect();
                shopFaceDetect.setShopId(shopId);
                shopFaceDetect.setPhoneNumber(phoneNumber);
                shopFaceDetect.setState(1);
                shopFaceDetect.setLastFaceDetectShoppingTime(new Date());

                ShopFaceDetect shopFaceDetectQuery = shopFaceDetectService
                        .findShopFaceDetectByShopIdAndPhoneNumber(shopId,
                                phoneNumber);
                if (shopFaceDetectQuery != null) {
                    if (shopFaceDetectQuery.getState() == 1) {
                        // 注册人脸
                        if (doAddUser(phoneNumber, phoneNumber,
                                "shop_" + shopId, faceImage).equals(
                                ResponseStatus.SUCCESS)) {
                            shopFaceDetectService
                                    .updateLastShopFaceDetectShoppingTime(shopFaceDetect);
                            return ResponseStatus.SUCCESS;
                        }
                    } else {
                        return ResponseStatus.FAIL;
                    }
                } else {
                    // 注册人脸
                    if (doAddUser(phoneNumber, phoneNumber, "shop_" + shopId,
                            faceImage).equals(ResponseStatus.SUCCESS)) {
                        // 保存记录
                        shopFaceDetectService.addShopFaceDetect(shopFaceDetect);
                        return ResponseStatus.SUCCESS;
                    }
                }
            }
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        return ResponseStatus.FAIL;
    }

}
