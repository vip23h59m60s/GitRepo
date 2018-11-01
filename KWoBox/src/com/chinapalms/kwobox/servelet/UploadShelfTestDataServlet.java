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

import com.chinapalms.kwobox.calibration.CalibrationService;
import com.chinapalms.kwobox.calibration.ShelfTestDataService;

public class UploadShelfTestDataServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    Log log = LogFactory.getLog(UploadShelfTestDataServlet.class);

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setCharacterEncoding("utf-8");
            response.setContentType("application/json;charset=utf-8");
            doUploadShelfTestDataFile(request, response);
        } catch (Exception e) {
            log.error("UploadShelfTestDataServlet->exception:" + e.toString());
            e.printStackTrace();
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private void doUploadShelfTestDataFile(HttpServletRequest request,
            HttpServletResponse response) {
        log.info("doUploadShelfTestDataFile................");
        ShelfTestDataService shelfTestDataService = new ShelfTestDataService();
        String result = shelfTestDataService.doUploadShelfTestDataFile(request,
                response);

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
