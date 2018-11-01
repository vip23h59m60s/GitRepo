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

public class MonitorServerServlet extends HttpServlet {

    /**
     * 作为监控Tomcat作用
     */

    private static final long serialVersionUID = 1L;

    Log log = LogFactory.getLog(MonitorServerServlet.class);

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setCharacterEncoding("utf-8");
            response.setContentType("application/json;charset=utf-8");
            // 对Tomcat的ping做出反馈
            doPingResponse(request, response);
        } catch (Exception e) {
            log.error("MonitorServerServlet->exception:" + e.toString());
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private void doPingResponse(HttpServletRequest request,
            HttpServletResponse response) {
        String action = request.getParameter("action");
        if (action != null && action.equals("ping")) {
            log.info("Tomcat ping:->pingServer");
            System.out.println("Tomcat ping:->pingServer");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("response", "success");
            try {
                response.getWriter().write(jsonObject.toString());
            } catch (Exception e) {
                log.error("try->catch exception:", e);
                e.printStackTrace();
            }
        }
    }

}
