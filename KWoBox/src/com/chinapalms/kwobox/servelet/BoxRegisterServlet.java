package com.chinapalms.kwobox.servelet;

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
import com.chinapalms.kwobox.devicemonitor.DeviceMonitorService;

public class BoxRegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    Log log = LogFactory.getLog(BoxRegisterServlet.class);

    // 部署商户售货机上传售货机相关信息
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setCharacterEncoding("utf-8");
            response.setContentType("application/json;charset=utf-8");
            doBoxRegister(request, response);
        } catch (Exception e) {
            log.error("BoxRegisterServlet->exception:" + e.toString());
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private void doBoxRegister(HttpServletRequest request,
            HttpServletResponse response) {
        DeviceMonitorService deviceMonitorService = new DeviceMonitorService();
        String registerResult = deviceMonitorService.doBoxRegister(request,
                response);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", registerResult);
        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

}
