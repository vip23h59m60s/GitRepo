package com.custom.kwobox.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.baidu.face.FaceDetectConfig;
import com.chinapalms.kwobox.baidu.face.FaceDetectService;
import com.chinapalms.kwobox.servelet.FaceSettingServelet;
import com.custom.kwobox.baidu.face.CustomBaiduFaceDetectService;

public class CustomFaceSettingServelet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    Log log = LogFactory.getLog(CustomFaceSettingServelet.class);

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setCharacterEncoding("utf-8");
            response.setContentType("application/json;charset=utf-8");
            doUploadFaceDetectSettingPhoto(request, response);
        } catch (Exception e) {
            log.error("FaceSettingServelet->exception:" + e.toString());
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private void doUploadFaceDetectSettingPhoto(HttpServletRequest request,
            HttpServletResponse response) {
        CustomBaiduFaceDetectService customFaceDetectService = new CustomBaiduFaceDetectService(
                FaceDetectConfig.APP_ID, FaceDetectConfig.API_KEY,
                FaceDetectConfig.SECRET_KEY);
        String uploadResult = customFaceDetectService
                .doUploadFaceDetectSettingPhotoAndFaceRegister(request,
                        response);
        try {
            response.getWriter().write(uploadResult);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

}
