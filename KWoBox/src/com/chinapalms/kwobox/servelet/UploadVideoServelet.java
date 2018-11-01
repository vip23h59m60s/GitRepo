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

import com.chinapalms.kwobox.log.LogService;
import com.chinapalms.kwobox.video.VideoService;

public class UploadVideoServelet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    Log log = LogFactory.getLog(UploadVideoServelet.class);

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setCharacterEncoding("utf-8");
            response.setContentType("application/json;charset=utf-8");
            doUploadLog(request, response);
        } catch (Exception e) {
            log.error("UploadVideoServelet->exception:" + e.toString());
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private void doUploadLog(HttpServletRequest request,
            HttpServletResponse response) {
        log.info("doUploadVideo................");
        VideoService videoService = new VideoService();
        String result = videoService.doUploadVideo(request, response);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", result);

        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

}
