package com.custom.kwobox.baidu.face;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.chinapalms.kwobox.baidu.face.FaceDetectConfig;
import com.chinapalms.kwobox.baidu.face.FaceDetectRandomStringGenerator;
import com.chinapalms.kwobox.javabean.BoxBody;
import com.chinapalms.kwobox.javabean.Boxes;
import com.chinapalms.kwobox.javabean.CurrentUser;
import com.chinapalms.kwobox.javabean.Customer;
import com.chinapalms.kwobox.javabean.DoorOpenedCallback;
import com.chinapalms.kwobox.javabean.User;
import com.chinapalms.kwobox.servelet.KWoBoxServelet;
import com.chinapalms.kwobox.service.BoxesService;
import com.chinapalms.kwobox.service.CurrentUserService;
import com.chinapalms.kwobox.service.CustomerService;
import com.chinapalms.kwobox.service.DoorOpenedCallbackService;
import com.chinapalms.kwobox.service.OrderService;
import com.chinapalms.kwobox.service.UserService;
import com.chinapalms.kwobox.utils.ResponseStatus;

public class CustomBaiduFaceDetectService extends AipFace {

    Log log = LogFactory.getLog(CustomBaiduFaceDetectService.class);

    public CustomBaiduFaceDetectService(String appId, String apiKey,
            String secretKey) {
        super(appId, apiKey, secretKey);
    }

    // 小程序中调用上传并注册人脸
    public String doUploadFaceDetectSettingPhotoAndFaceRegister(
            HttpServletRequest request, HttpServletResponse response) {
        JSONObject responseJsonObject = new JSONObject();
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
            String mchId = request.getAttribute("mchId").toString();
            CustomerService customerService = new CustomerService();
            Customer customer = customerService.findCustomerByMchId(mchId);
            // 商户传递过来的人脸信息，在注册人脸时在分组前加上customerId，注意在识别是如果判断是第三方商户对接模式，在进行匹配时密码前也要加上customerId
            int customerId = 0;
            if (customer != null) {
                customerId = customer.getCustomerId();
            }
            String phoneNumber = request.getAttribute("phoneNumber").toString();
            String nickName = request.getAttribute("nickName").toString();
            String oldFaceDetectPassword = request.getAttribute(
                    "oldFaceDetectPassword").toString();
            String faceDetectPassword = request.getAttribute(
                    "faceDetectPassword").toString();
            path = path + File.separator + customerId + "_"
                    + faceDetectPassword;
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
            // 注册人脸时检查当前注册人脸是否与已注册同密码人脸切phoneNumber（userId）不同的用户，如果有则不允许注册，并且同时关闭之前用户,避免风险
            String multiIdentifyResult = doMultiIdentifyOnly(customerId + "_"
                    + faceDetectPassword, faceImage, false);
            log.info("FaceDetectService:->doUploadFaceDetectSettingPhotoAndFaceRegister->"
                    + multiIdentifyResult);
            // 注册人脸时检查当前注册人脸是否与已注册同密码人脸切phoneNumber（userId）不同的用户，如果有则不允许注册，并且同时关闭之前用户,避免风险
            if (!multiIdentifyResult.equals(ResponseStatus.FAIL)) {
                String phoneNumberMatch = JSONObject.fromObject(
                        multiIdentifyResult).getString("phoneNumber");
                if (!phoneNumberMatch.equals(phoneNumber)) {
                    // 匹配到相同的其他人的人脸，暂停其开通，并将另外一个人的人脸功能关闭,设置为2，异常
                    // 先将本人人脸识别功能关闭，设置为2，异常状态
                    doDeleteUser(customerId + "_" + faceDetectPassword,
                            phoneNumber, faceImage, true);
                    doDeleteUser(customerId + "_" + faceDetectPassword,
                            phoneNumberMatch, faceImage, true);
                    responseJsonObject.put("response",
                            ResponseStatus.FACE_FUNCTION_NOT_ALLOW);
                    responseJsonObject.put("phoneNumber1", phoneNumber);
                    responseJsonObject.put("phoneNumber2", phoneNumberMatch);
                }
            }
            // 在注册之前先删除原来的注册信息
            doDeleteUser(customerId + "_" + oldFaceDetectPassword, phoneNumber,
                    faceImage, true);
            if (doAddUser(phoneNumber, nickName,
                    customerId + "_" + faceDetectPassword, faceImage).equals(
                    ResponseStatus.SUCCESS)) {
                String faceFunctionPicture = "pictures/faceDetect/"
                        + customerId + "_" + faceDetectPassword + "/"
                        + fileName;
                responseJsonObject.put("response", ResponseStatus.SUCCESS);
                responseJsonObject.put("faceFunctionPicture",
                        faceFunctionPicture);
            } else {
                responseJsonObject.put("response", ResponseStatus.FAIL);
            }
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        return responseJsonObject.toString();
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

}
