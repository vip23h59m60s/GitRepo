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

import com.chinapalms.kwobox.service.ExceptionShoppingService;

public class ExceptionShoppingServelet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    Log log = LogFactory.getLog(ExceptionShoppingServelet.class);

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setCharacterEncoding("utf-8");
            response.setContentType("application/json;charset=utf-8");
            doHandleExceptionVideo(request, response);
        } catch (Exception e) {
            log.error("ExceptionShoppingServelet->exception:" + e.toString());
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private void doHandleExceptionVideo(HttpServletRequest request,
            HttpServletResponse response) {
        log.info("doHandleExceptionVideo................");
        ExceptionShoppingService exceptionShoppingService = new ExceptionShoppingService();
        String result = exceptionShoppingService.doHandleExceptionVideo(
                request, response);

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
