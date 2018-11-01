package com.custom.kwobox.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.javabean.Boxes;
import com.chinapalms.kwobox.service.BoxGoodsXXXService;
import com.chinapalms.kwobox.service.BoxesService;
import com.chinapalms.kwobox.utils.ResponseStatus;
import com.custom.kwobox.service.CustomBoxesService;
import com.custom.kwobox.service.CustomResponseService;
import com.custom.kwobox.utils.CustomRequestSignatureUtil;

public class KWoBoxCustomServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    Log log = LogFactory.getLog(KWoBoxCustomServlet.class);

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            request.setCharacterEncoding("utf-8");
            response.setContentType("application/json;charset=utf-8");
            doRequestAndReponseAction(request, response);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        doGet(request, response);
    }

    // AactionRequest
    // action:openDoor
    // boxId:xxxxx
    // phoneNumber:xxxx
    // userType:xxxxxx 1普通用户，2理货员
    // nonceStr:xxxxx
    // requestSerial:xxxx
    // timeStamp:xxx(int)十位时间戳
    // sign:xxx
    // secretKey:xxxxx(不用传递，只在生成签名的时候附带)

    // 返回结果规范
    // return_code:SUCCESS
    // return_msg:xxx
    // result_code:xxxx
    // result_msg:xxxxxx
    private void doRequestAndReponseAction(HttpServletRequest request,
            HttpServletResponse response) {
        String action = request.getParameter("action");
        log.info("KWoBoxCustomServlet: action=" + action);

        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(
                    request.getInputStream(), "UTF-8"));
            StringBuffer sb = new StringBuffer();
            String line = null;
            log.info("start receivee message");
            while ((line = br.readLine()) != null) {
                log.info("start receiving message line=" + line);
                sb.append(line);
            }
            log.info("KWoBoxCustomServlet->sb=" + sb.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                    br = null;
                } catch (Exception e) {
                    log.error("try->catch exception:", e);
                    e.printStackTrace();
                }
            }
        }

        // 首先校验签名
        if (checkSign(request, response)) {
            if (action != null) {
                // 请求行为判断分流处理
                if (action.equals("openDoor")) {
                    // ================Yoho和小共用接口开始==========================//
                    // 请求开门
                    doOpenDoor(request, response);
                    // ================Yoho和小共用接口结束==========================//
                    // =================仅小e相关接口开始================================//
                } else if (action.equals("managerOpenDoor")) {
                    // 理货员请求开门
                    doManagerOpenDoor(request, response);
                } else if (action.equals("getBoxCardgoRoadGoodsInfo")) {
                    // 获取当前售货机货道商品信息(主要用于扫码理货开门后展示)
                    doGetCurrentBoxCardgoRoadGoodsInfo(request, response);
                    // =================仅小e相关接口结束================================//
                    // ===============仅Yoho相关接口开始=========================//
                    // 商户查询附近门店入口
                } else if (action.equals("findNearbyBoxes")) {
                    doFindNearbyBoxes(request, response);
                    // 商户查询附近门店在地图上显示
                } else if (action.equals("findNearbyBoxesOnMap")) {
                    doFindNearbyBoxesOnMap(request, response);
                    // 商户获取当前附近售货柜的概述信息
                } else if (action.equals("nearByBoxDetailsSummary")) {
                    doGetNearbyBoxDetailsSummary(request, response);
                    // 商户获取当前附近售货柜的商品价格库存等信息
                } else if (action.equals("nearByCurrentBoxAllGoods")) {
                    doGetNearbyCurrentBoxAllGoods(request, response);
                    // 商户请求获取售货机当前状态
                } else if (action.equals("openDoorIdentify")) {
                    doOpenDoorIdentify(request, response);
                }
                // ===============仅Yoho相关接口结束=========================//
            }
        }

    }

    private boolean checkSign(HttpServletRequest request,
            HttpServletResponse response) {
        // 首先校验签名
        String mchId = request.getParameter("mchId");
        String nonceStr = request.getParameter("nonceStr");
        String requestSerial = request.getParameter("requestSerial");
        String timeStamp = request.getParameter("timeStamp");
        String signFromCustom = request.getParameter("sign");

        String checkSignResult = CustomRequestSignatureUtil.checkSign(mchId,
                nonceStr, requestSerial, timeStamp, signFromCustom);
        JSONObject openDoorResultJsonObject = JSONObject
                .fromObject(checkSignResult);
        String actionResult = openDoorResultJsonObject
                .getString("actionResult");
        String resultMsg = openDoorResultJsonObject.getString("resultMsg");

        CustomResponseService customResponseService = new CustomResponseService();
        String responseToCustom = null;
        if (actionResult.equals(ResponseStatus.SUCCESS)) {
            return true;
        } else {
            int errorCode = openDoorResultJsonObject.getInt("errCode");
            String errorCodeDes = openDoorResultJsonObject
                    .getString("errCodeDes");
            responseToCustom = customResponseService
                    .getReturnSuccessButResultFailResponse(resultMsg,
                            errorCode, errorCodeDes);
            try {
                response.getWriter().write(responseToCustom);
            } catch (Exception e) {
                log.error("try->catch exception:", e);
                e.printStackTrace();
            }
            return false;
        }
    }

    /**
     * 普通消费者扫码开门
     * 
     * @param request
     * @param response
     */
    private void doOpenDoor(HttpServletRequest request,
            HttpServletResponse response) {
        String boxId = request.getParameter("boxId");
        String mchId = request.getParameter("mchId");
        String phoneNumber = request.getParameter("phoneNumber");
        String userType = request.getParameter("userType");
        String requestSerial = request.getParameter("requestSerial");
        String doorOpenedNotifyUrl = request
                .getParameter("doorOpenedNotifyUrl");
        String doorClosedNotifyUrl = request
                .getParameter("doorClosedNotifyUrl");
        String orderNotifyUrl = request.getParameter("orderNotifyUrl");

        CustomBoxesService customBoxesService = new CustomBoxesService();
        String openDoorResult = customBoxesService.doOpenDoor(boxId, mchId,
                phoneNumber, userType, requestSerial, doorOpenedNotifyUrl,
                doorClosedNotifyUrl, orderNotifyUrl);

        try {
            log.info("openDoorResult responseTocustom=" + openDoorResult);
            response.getWriter().write(openDoorResult);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    /**
     * 理货员扫码开门
     * 
     * @param request
     * @param response
     */
    private void doManagerOpenDoor(HttpServletRequest request,
            HttpServletResponse response) {
        String boxId = request.getParameter("boxId");
        String mchId = request.getParameter("mchId");
        String customerWorkerId = request.getParameter("workerId");
        String userType = request.getParameter("userType");
        String requestSerial = request.getParameter("requestSerial");
        String doorOpenedNotifyUrl = request
                .getParameter("doorOpenedNotifyUrl");
        String doorClosedNotifyUrl = request
                .getParameter("doorClosedNotifyUrl");
        String orderNotifyUrl = request.getParameter("orderNotifyUrl");

        CustomBoxesService customBoxesService = new CustomBoxesService();
        String openDoorResult = customBoxesService.doManagerOpenDoor(boxId,
                mchId, customerWorkerId, userType, requestSerial,
                doorOpenedNotifyUrl, doorClosedNotifyUrl, orderNotifyUrl);

        try {
            log.info("doManagerOpenDoorResult responseTocustom="
                    + openDoorResult);
            response.getWriter().write(openDoorResult);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doFindNearbyBoxes(HttpServletRequest request,
            HttpServletResponse response) {
        String mchId = request.getParameter("mchId");
        String fromLat = request.getParameter("latitude");
        String fromLng = request.getParameter("longitude");
        String pageNumber = request.getParameter("pageNumber");
        int page = 0;
        try {
            page = Integer.valueOf(pageNumber);
        } catch (Exception e) {
            page = 0;
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }

        JSONObject jsonObject = new JSONObject();
        CustomBoxesService customBoxesService = new CustomBoxesService();
        String findNearbyBoxesResult = customBoxesService.doFindNearbyBoxes(
                mchId, fromLat, fromLng,
                ResponseStatus.NEARBY_BOXES_NEAYBYRANGE, page);
        CustomResponseService customResponseService = new CustomResponseService();
        String responseToCustom = null;
        if (findNearbyBoxesResult.equals(ResponseStatus.NO_BOXES)) {
            jsonObject.put("response", "noBoxes");
            responseToCustom = customResponseService
                    .getReturnSuccessAndResultSuccessResponse(jsonObject
                            .toString());
        } else if (findNearbyBoxesResult.equals(ResponseStatus.LAST_PAGE)) {
            jsonObject.put("response", "lastPage");
            responseToCustom = customResponseService
                    .getReturnSuccessAndResultSuccessResponse(jsonObject
                            .toString());
        } else {
            responseToCustom = customResponseService
                    .getReturnSuccessAndResultSuccessResponse(findNearbyBoxesResult);
        }
        try {
            response.getWriter().write(responseToCustom);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doFindNearbyBoxesOnMap(HttpServletRequest request,
            HttpServletResponse response) {
        String mchId = request.getParameter("mchId");
        String fromLat = request.getParameter("latitude");
        String fromLng = request.getParameter("longitude");
        String pageNumber = request.getParameter("pageNumber");
        int page = 0;
        try {
            page = Integer.valueOf(pageNumber);
        } catch (Exception e) {
            page = 0;
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }

        JSONObject jsonObject = new JSONObject();
        CustomBoxesService customBoxesService = new CustomBoxesService();
        String findNearbyBoxesResult = customBoxesService
                .doFindNearbyBoxesOnMap(mchId, fromLat, fromLng,
                        ResponseStatus.NEARBY_BOXES_NEAYBYRANGE, page);
        CustomResponseService customResponseService = new CustomResponseService();
        String responseToCustom = null;
        if (findNearbyBoxesResult.equals(ResponseStatus.NO_BOXES)) {
            jsonObject.put("response", "noBoxes");
            responseToCustom = customResponseService
                    .getReturnSuccessAndResultSuccessResponse(jsonObject
                            .toString());
        } else if (findNearbyBoxesResult.equals(ResponseStatus.LAST_PAGE)) {
            jsonObject.put("response", "lastPage");
            responseToCustom = customResponseService
                    .getReturnSuccessAndResultSuccessResponse(jsonObject
                            .toString());
        } else {
            responseToCustom = customResponseService
                    .getReturnSuccessAndResultSuccessResponse(findNearbyBoxesResult);
        }
        try {
            response.getWriter().write(responseToCustom);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doGetNearbyBoxDetailsSummary(HttpServletRequest request,
            HttpServletResponse response) {
        String boxId = request.getParameter("boxId");

        BoxesService boxesService = new BoxesService();
        String boxDetailsSummaryResult = boxesService
                .doGetNearbyBoxDetailsSummary(boxId);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", boxDetailsSummaryResult);

        CustomResponseService customResponseService = new CustomResponseService();
        String responseToCustom = customResponseService
                .getReturnSuccessAndResultSuccessResponse(jsonObject.toString());
        try {
            response.getWriter().write(responseToCustom);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doGetNearbyCurrentBoxAllGoods(HttpServletRequest request,
            HttpServletResponse response) {
        String boxId = request.getParameter("boxId");

        BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
        JSONObject jsonObject = new JSONObject();

        String boxGoodsResult = boxGoodsXXXService
                .doGetNearbyCurrentBoxAllGoods(boxId);
        jsonObject.put("response", boxGoodsResult);

        CustomResponseService customResponseService = new CustomResponseService();
        String responseToCustom = customResponseService
                .getReturnSuccessAndResultSuccessResponse(jsonObject.toString());

        try {
            response.getWriter().write(responseToCustom);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doOpenDoorIdentify(HttpServletRequest request,
            HttpServletResponse response) {
        CustomBoxesService customBoxesService = new CustomBoxesService();
        String mchId = request.getParameter("mchId");
        String boxId = request.getParameter("boxId");
        Boxes box = new Boxes();
        box.setBoxId(boxId);

        JSONObject jsonObject = new JSONObject();
        String openDoorIdentityFlag = customBoxesService.openDoorIdentify(
                boxId, mchId);
        jsonObject.put("response", openDoorIdentityFlag);

        CustomResponseService customResponseService = new CustomResponseService();
        String responseToCustom = customResponseService
                .getReturnSuccessAndResultSuccessResponse(jsonObject.toString());

        try {
            response.getWriter().write(responseToCustom);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doGetCurrentBoxCardgoRoadGoodsInfo(HttpServletRequest request,
            HttpServletResponse response) {
        String boxId = request.getParameter("boxId");
        String mchId = request.getParameter("mchId");
        // 获取当前售货机货道商品信息(主要用于扫码理货开门后展示)
        CustomBoxesService customBoxesService = new CustomBoxesService();
        String getBoxCardgoRoadGoodsResult = customBoxesService
                .doGetBoxCardgoRoadGoods(boxId, mchId);

        try {
            log.info("doGetCurrentBoxCardgoRoadGoodsInfo responseTocustom="
                    + getBoxCardgoRoadGoodsResult);
            response.getWriter().write(getBoxCardgoRoadGoodsResult);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

}
