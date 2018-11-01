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

import com.chinapalms.kwobox.devicemonitor.DeviceMonitorService;
import com.chinapalms.kwobox.service.ShoppingInfoService;

public class UploadShoppingInfoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    Log log = LogFactory.getLog(UploadShoppingInfoServlet.class);

    // 工控机上传购物过程相关信息,包括购物信息、时间和视频
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setCharacterEncoding("utf-8");
            response.setContentType("application/json;charset=utf-8");
            doUploadShoppingInfo(request, response);
        } catch (Exception e) {
            log.error("UploadShoppingInfoServlet->exception:" + e.toString());
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * 上传购物信息和视频等信息
     * 
     * @param request
     * @param response
     */
    private void doUploadShoppingInfo(HttpServletRequest request,
            HttpServletResponse response) {
        ShoppingInfoService shoppingInfoService = new ShoppingInfoService();
        String uploadShoppingInfoRegisterResult = shoppingInfoService
                .doUploadShoppingInfo(request, response);

        try {
            response.getWriter().write(uploadShoppingInfoRegisterResult);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

}
