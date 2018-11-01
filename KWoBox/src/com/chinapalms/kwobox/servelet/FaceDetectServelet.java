package com.chinapalms.kwobox.servelet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONObject;

import com.chinapalms.kwobox.baidu.face.FaceDetectConfig;
import com.chinapalms.kwobox.baidu.face.FaceDetectService;

public class FaceDetectServelet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    Log log = LogFactory.getLog(FaceDetectServelet.class);

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setCharacterEncoding("utf-8");
            response.setContentType("application/json;charset=utf-8");
            doFaceDetect(request, response);
        } catch (Exception e) {
            log.error("FaceDetectServelet->exception:" + e.toString());
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private void doFaceDetect(HttpServletRequest request,
            HttpServletResponse response) {
        FaceDetectService faceDetectService = new FaceDetectService(
                FaceDetectConfig.APP_ID, FaceDetectConfig.API_KEY,
                FaceDetectConfig.SECRET_KEY);
        String faceDetectResult = faceDetectService.doFaceDetect(request,
                response);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", faceDetectResult);
        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

}
