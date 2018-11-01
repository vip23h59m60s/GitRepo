package com.chinapalms.kwobox.servelet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONObject;

import com.chinapalms.kwobox.devicemonitor.DeviceMonitorService;
import com.chinapalms.kwobox.javabean.Boxes;
import com.chinapalms.kwobox.javabean.CurrentUser;
import com.chinapalms.kwobox.javabean.IdentifyCode;
import com.chinapalms.kwobox.javabean.Order;
import com.chinapalms.kwobox.javabean.User;
import com.chinapalms.kwobox.pay.wxpay.WXPayService;
import com.chinapalms.kwobox.pay.wxpay.papay.WxPapayService;
import com.chinapalms.kwobox.service.BoxGoodsXXXNewService;
import com.chinapalms.kwobox.service.BoxGoodsXXXService;
import com.chinapalms.kwobox.service.BoxShopesService;
import com.chinapalms.kwobox.service.BoxStatusService;
import com.chinapalms.kwobox.service.BoxStockSyncStatusService;
import com.chinapalms.kwobox.service.BoxesService;
import com.chinapalms.kwobox.service.CallCenterNormalService;
import com.chinapalms.kwobox.service.CurrentUserService;
import com.chinapalms.kwobox.service.CustomerService;
import com.chinapalms.kwobox.service.CustomerWorkerService;
import com.chinapalms.kwobox.service.DynamicPasswordService;
import com.chinapalms.kwobox.service.ExceptionShoppingService;
import com.chinapalms.kwobox.service.GoodsCategoryService;
import com.chinapalms.kwobox.service.GoodsAverageWeightService;
import com.chinapalms.kwobox.service.IdentifyCodeService;
import com.chinapalms.kwobox.service.InOutGoodsService;
import com.chinapalms.kwobox.service.OrderService;
import com.chinapalms.kwobox.service.PersonalCreditRecordService;
import com.chinapalms.kwobox.service.ReplenishGoodsService;
import com.chinapalms.kwobox.service.ShoppingInfoService;
import com.chinapalms.kwobox.service.SignInService;
import com.chinapalms.kwobox.service.TokenService;
import com.chinapalms.kwobox.service.UserService;
import com.chinapalms.kwobox.utils.ResponseStatus;
import com.chinapalms.kwobox.wxjssdk.WxJsSDKService;

public class KWoBoxServelet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    Log log = LogFactory.getLog(KWoBoxServelet.class);

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

    private void doRequestAndReponseAction(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String action = request.getParameter("action");
        log.info("KWoBoxServelet: action=" + action);
        if (action != null) {
            // for wx
            // 获取验证码
            if (action.equals("identifyCode")) {
                doGetIdentifyCode(request, response);
                // 校验验证码并登陆1.正确性;2.是否超时
            } else if (action.equals("identifyCheck")) {
                doCheckIdentifyCode(request, response);
            } else if (action.equals("quickLogin")) {
                // 通过微信直接获取微信绑定的手机号码直接登录
                doQuickLogin(request, response);
            } else if (action.equals("checkUserPermission")) {
                doCheckUserPermission(request, response);
                // 检查是否有未支付订单
            } else if (action.equals("checkNoPayOrder")) {
                doCheckNoPayOrder(request, response);
                // 检验售货柜状态1.是否服务中;2.是否异常等
            } else if (action.equals("openDoorIdentify")) {
                doOpenDoorIdentify(request, response);
                // 补货员扫码验证，1.是否满足为该商家补货员，2.是否服务中;3.是否异常等;4.后台货道商品或价格是否已发生变化
            } else if (action.equals("managerOpenDoorIdentify")) {
                doManagerOpenDoorIdentify(request, response);
                // 注册当前扫码使用柜子的用户信息
            } else if (action.equals("registerCurrentUser")) {
                doRegisterCurrentUser(request, response);
                // 按条件和分页查询我的订单
            } else if (action.equals("myOrders")) {
                doFindMyOrders(request, response);
                // 查询我的所有订单
            } else if (action.equals("allMyOrders")) {
                doFindAllMyOrders(request, response);
                // 获取最新Top N 个订单
            } else if (action.equals("dynamicOrders")) {
                doFindDynamicOrders(request, response);
                // 获取当前售货柜中所有商品信息
            } else if (action.equals("currentBoxAllGoods")) {
                doGetCurrentBoxAllGoods(request, response);
                // 检查当前理货员登录身份
            } else if (action.equals("checkManagerLoginIdentify")) {
                doCheckManagerLoginIdentify(request, response);
                // 获取当前理货员的理货单
            } else if (action.equals("myReplenishGoodsOrder")) {
                doFindMyReplenishGoodsOrder(request, response);
                // 获取当前理货员所管理的所有售货柜
            } else if (action.equals("myManageBoxes")) {
                doFindMyManageBoxes(request, response);
                // 根据boxId获取当前管理售货柜的详细信息描述
            } else if (action.equals("boxDetailsSummary")) {
                doGetBoxDetailsSummary(request, response);
                // 获取用户信息
            } else if (action.equals("getUserInfo")) {
                doGetUserInfo(request, response);
                // 检查是否开通人脸识别功能
            } else if (action.equals("checkFaceDetectFunction")) {
                doCheckFaceDetectFunction(request, response);
                // 查询我的信用记录
            } else if (action.equals("myCreditRecords")) {
                doFindMyCreditRecords(request, response);
                // 针对称重类型柜子，获取当前货柜商品结构
            } else if (action.equals("getCurrentAndNewBoxGoods")) {
                doGetCurrentAndNewBoxGoods(request, response);
                // 更新当前售货柜货道商品结构和价格
            } else if (action.equals("updateCurrentBoxGoods")) {
                doUpdateCurrentBoxGoods(request, response);
                // 更新商品价格
            } else if (action.equals("updateCurrentBoxGoodsPrice")) {
                doUpdateCurrentBoxGoodsPrice(request, response);
                // 获取当前称重柜子每个货道对应商品信息
            } else if (action.equals("getCurrentBoxCardgoRoadGoodsInfo")) {
                doGetCurrentBoxGoods(request, response);
                // 获取个人订单实际消费总额和总减免金额
            } else if (action.equals("findOrderTotalDescription")) {
                doFindOrderTotalDescription(request, response);
                // 更新单个货道商品库存，以达到去皮效果
            } else if (action.equals("updateCardgoRoadGoodsStockNumber")) {
                doUpdateCardgoRoadGoodsStockNumber(request, response);
            } else if (action.equals("findNearbyBoxes")) {
                // 查找附近售货柜
                doFindNearbyBoxes(request, response);
            } else if (action.equals("findNearbyBoxesOnMap")) {
                // 查找附近售货柜,以显示在Map上
                doFindNearbyBoxesOnMap(request, response);
            } else if (action.equals("nearByBoxDetailsSummary")) {
                // 根据boxId获取附近当前管理售货柜的详细信息描述
                doGetNearbyBoxDetailsSummary(request, response);
                // 获取附近当前售货柜中所有商品信息
            } else if (action.equals("nearByCurrentBoxAllGoods")) {
                doGetNearbyCurrentBoxAllGoods(request, response);
            } else if (action.equals("getSignInInfo")) {
                // 获取每日签到当前签到信息
                doGetSignInInfo(request, response);
            } else if (action.equals("resetSignIn")) {
                // 当签到有中断或者一个周期完成时进行reset操作
                doResetSignIn(request, response);
            } else if (action.equals("signIn")) {
                // 处理签到逻辑
                doSignIn(request, response);
            } else if (action.equals("getPhoneNumberFromWX")) {
                // 通过微信授权直接获取该微信绑定的电话号码
                doGetPhoneNumberFromWX(request, response);
            } else if (action.equals("getDynamicPassword")) {
                // 生成8位用于开门的动态密码
                doGetDynamicPassword(request, response);
            } else if (action.equals("checkDynamicPasswordState")) {
                // 检查动态密码状态，展示剩余有效时间或已过期
                doCheckDynamicPasswordState(request, response);
            } else if (action.equals("getBoxName")) {
                // 通过boxId获取扫码以后的boxName,显示在状态栏
                doGetBoxName(request, response);
            } else if (action.equals("getPlatformAdInfo")) {
                // 获取系统默认智狗猫平台的轮播广告信息
                doGetPlatformAdInfo(request, response);
            } else if (action.equals("getCustomerInfo")) {
                // 通过boxId获取扫码以后的商户信息，包括商户boxName，商户广告信息等
                doGetCustomerInfo(request, response);
            } else if (action.equals("shutDownIdentifyWithBoxId")) {
                // 智购云关闭设备验证（通过boxId）
                doShutDownIdentifyWithBoxId(request, response);
            } else if (action.equals("shutDownIdentifyWithBoxBodySnNumber")) {
                // 智购云关闭设备验证（通过柜体序列号）
                doShutDownIdentifyWithBoxBodySnNumber(request, response);
            } else if (action.equals("shutDownDevice")) {
                // 关闭设备
                doShutDownDevice(request, response);
            }

            // for Android
            // 商品入库
            if (action.equals("androidGetAllGoodsCategory")) {
                doGetAllGoodsCategory(request, response);
            } else if (action.equals("androidInGoodss")) {
                doInGoodss(request, response);
                // Android工控机获取称重方案柜子货道商品信息
            } else if (action.equals("androidGetBoxInfo")) {
                doGetBoxInfo(request, response);
                // 当后台管理售货机货道商品更新时，Android请求最新数据
            } else if (action.equals("androidGetNewBoxInfo")) {
                doGetNewBoxInfo(request, response);
                // 系统开机时Android向服务器请求柜子初始化信息：1.boxId，2.box类型，3.是否开通人脸识别功能等
            } else if (action.equals("androidInitBoxConfig")) {
                doInitBoxConfig(request, response);
            } else if (action.equals("androidReportSystemInfo")) {
                // Android工控机上报系统状态信息（包括系统版本，应用版本等信息）
                doReportSystemInfo(request, response);
            } else if (action.equals("androidCheckDynamicPassword")) {
                // 通过工控机传过来的动态密码进行开门购物逻辑处理
                doCheckDynamicPassword(request, response);
                // 工控机盘点完成以后发送本次购物售卖商品信息，服务器解析生成订单(将原来在WebSocket中的逻辑通过Http发送，保证订单发送成功)
            } else if (action.equals("androidSaledGoodsInfo")) {
                doSaledGoodsInfo(request, response);
                // 视频客户端发来修改本次购物订单的购物信息，服务器解析生成新订单
            } else if (action.equals("androidUpdateSaledGoodsInfo")) {
                doUpdateSaledGoodsInfo(request, response);
                // 工控机盘点完成以后发送本次理货信息，服务器解析生成订单(将原来在WebSocket中的逻辑通过Http发送，保证理货信息发送成功)
            } else if (action.equals("androidUpdatedGoodsInfo")) {
                doUpdatedGoodsInfo(request, response);
            }

            // 异常视频监控客户端逻辑
            if (action.equals("monitorUserLogin")) {
                // 异常视频监控工控机用户登录
                doMonitorUserLogin(request, response);
            } else if (action.equals("monitorGetExceptionShoppings")) {
                // 异常视频处理工控机获取异常购物列表
                doGetExceptionShoppings(request, response);
            } else if (action.equals("monitorExceptionShopping")) {
                // 接收异常视频处理工控机发送异常处理结果(将原来在WebSocket中的逻辑通过Http发送，保证异常购物订单发送成功)
                doMonitorExceptionShopping(request, response);
            } else if (action.equals("getShoppingInfo")) {
                // 获取购物信息列表
                doGetShoppingInfo(request, response);
            } else if (action.equals("getShoppingInfoVideos")) {
                // 视频客户端获取本次购物的视频路径信息
                doGetShoppingInfoVideos(request, response);
            } else if (action.equals("updateShoppingInfoVideosUrl")) {
                // 接收工控机上传的购物信息视频的url信息并更新至数据库
                doUpdateShoppingInfoVideosUrl(request, response);
            } else if (action.equals("updateShoppingInfoState")) {
                // 更新购物信息处理状态（处理人和状态）
                doUpdateShoppingInfoState(request, response);
            } else if (action.equals("notifyManager")) {
                // 售货机因消费者行为导致购物异常时通知管理员
                doNotifyManager(request, response);
            } else if (action.equals("getBoxStatus")) {
                // 获取售货机当前状态
                doGetBoxStatus(request, response);
            } else if (action.equals("syncBoxStockInfo")) {
                // 视频端处理完一组异常以后同步服务器与主控工控机端进行库存信息同步
                doSyncBoxStockInfo(request, response);
            } else if (action.equals("syncBoxStockInfoFinished")) {
                // 工控机同步库存完成以后回调反馈
                doSyncBoxStockInfoFinished(request, response);
            } else if (action.equals("getBoxSyncStockStatus")) {
                // 视频处理端获取当前售货机和工控机库存同步状态
                doGetBoxSyncStockStatus(request, response);
            } else if (action.equals("getAllShoppingInfoCustomers")) {
                // 视频处理端获取当前售货机购物信息相关的所有商户列表以及异常未处理条数
                doGetAllShoppingInfoCustomers(request, response);
            } else if (action.equals("getCustomerShoppingInfoBoxes")) {
                // 视频处理端获取当前商户对应的购物信息售货机以及当前售货机异常未处理条数
                doGetCustomerShoppingInfoBoxes(request, response);
            }

            // 接收后台网页客户端请求来控制工控机FOTA升级、应用升级、权限控制等
            if (action.equals("LingmaoUpgrade")) {
                // 灵猫应用升级
                doLingmaoUpgrade(request, response);
            } else if (action.equals("LingmaoCoreUpgrade")) {
                // 灵猫盘点核心模块升级
                doLingmaoCoreUpgrade(request, response);
            } else if (action.equals("LingmaoToolsUpgrade")) {
                // 灵猫调试工具升级
                doLingmaoToolsUpgrade(request, response);
            } else if (action.equals("fotaUpgrade")) {
                // FOTA升级
                doFotaUpgrade(request, response);
            } else if (action.equals("LingmaoAllAppsUpgrade")) {
                // 灵猫所有应用(Lingmao,LingmaoTools,LingmaoCore)一体化升级
                doLingmaoAllAppsUpgrade(request, response);
            } else if (action.equals("setDebugStatus")) {
                // 工控机调试模式切换控制
                doSetDebugMode(request, response);
            } else if (action.equals("setLogLevel")) {
                // 设置工控机的Log等级
                doSetLogLevel(request, response);
            } else if (action.equals("pushAdv")) {
                // 广告推送
                doPushAdv(request, response);
            } else if (action.equals("removeAdv")) {
                // 广告删除
                doRemoveAdv(request, response);
            } else if (action.equals("restart")) {
                // 控制工控机重启
                doRestart(request, response);
            } else if (action.equals("bootCheckSelf")) {
                // 控制工控机自检
                doBootCheckSelf(request, response);
            } else if (action.equals("getVideo")) {
                // 控制工控机上传购物视频
                doGetVideo(request, response);
            } else if (action.equals("getLog")) {
                // 控制工控机上传Log
                doGetLog(request, response);
            }

            // 扫码装配逻辑
            if (action.equals("deviceRegister")) {
                // 分别扫码工控机条形码、柜体型号条形码、柜体序列号条形码进行信息注册
                doDeviceRegister(request, response);
            } else if (action.equals("boxRegisterBoxIdentify")) {
                // 售货机商户处部署时box校验：校验boxId是否存在
                doBoxRegisterBoxIdentify(request, response);
            } else if (action.equals("findAllCustomers")) {
                // 售货机商户处部署时，获取所有商户的列表
                doFindAllCustomers(request, response);
                // 查找指定商户的所有门店
            } else if (action.equals("findCustomerShopes")) {
                doFindCustomerShopes(request, response);
                // 查找指定商户的售货柜
            } else if (action.equals("findCustomerBoxes")) {
                doFindCustomerBoxes(request, response);
            }

            // 获取微信JS-SDK config
            if (action.equals("wxJsSDKConfig")) {
                doGetWxJsSDKConfig(request, response);
            } else if (action.equals("wxGetOpenSession")) {
                // 获取小程序open session,暂时用于获取用户手机号码
                doGetWxOpenSession(request, response);
            }

            // 微信手动支付相关
            if (action.equals("wxPayGetOpenId")) {
                doWXPayGetOpenId(request, response);
            } else if (action.equals("wxPayXiaDan")) {
                doWXPayXiaDan(request, response);
            } else if (action.equals("wxPaySign")) {
                doWXPaySign(request, response);
            } else if (action.equals("wxPaySuccess")) {
                doWXPaySuccess(request, response);
            }

            // 微信H5委托代扣
            if (action.equals("wxPaypayGetEntrustUrl")) {
                doWxPapayGetEntrustRequestUrl(request, response);
            }

            // 微信小程序委托代扣
            if (action.equals("wxPaypayGetEntrustBeanInfo")) {
                doWxPapayGetEntrustBeanInfo(request, response);
            } else if (action.equals("wxPaypayQuerySignContract")) {
                doWxPapayQuerySignContract(request, response);
            }

            // 微信支付宝委托代扣通用
            if (action.equals("updateContractId")) {
                doUpdateContractId(request, response);
            }
        }
    }

    private void doGetIdentifyCode(HttpServletRequest request,
            HttpServletResponse response) {
        IdentifyCodeService identifyCodeService = new IdentifyCodeService();
        String phoneNumber = request.getParameter("phoneNumber");
        IdentifyCode identifyCode = new IdentifyCode();
        identifyCode.setPhoneNumber(phoneNumber);

        JSONObject jsonObject = new JSONObject();
        String getIdentifyCodeFlag = identifyCodeService
                .doGetIdentifyCode(identifyCode);
        jsonObject.put("response", getIdentifyCodeFlag);

        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doCheckIdentifyCode(HttpServletRequest request,
            HttpServletResponse response) {
        IdentifyCodeService identifyCodeService = new IdentifyCodeService();
        String phoneNumber = request.getParameter("phoneNumber");
        String identifyCode = request.getParameter("identifyCode");
        String contractId = request.getParameter("contractId");
        String customType = request.getParameter("customType");
        IdentifyCode identifyCodeObject = new IdentifyCode();
        identifyCodeObject.setPhoneNumber(phoneNumber);
        identifyCodeObject.setIdentifyCode(identifyCode);

        JSONObject jsonObject = new JSONObject();
        String checkFlag = identifyCodeService.doCheckIdentifyCode(
                identifyCodeObject, customType, contractId);
        jsonObject.put("response", checkFlag);

        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doQuickLogin(HttpServletRequest request,
            HttpServletResponse response) {
        String phoneNumber = request.getParameter("phoneNumber");
        String contractId = request.getParameter("contractId");
        String customType = request.getParameter("customType");

        UserService userService = new UserService();
        String quickLoginResult = userService.doQuickLogin(phoneNumber,
                customType, contractId);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", quickLoginResult);

        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doCheckUserPermission(HttpServletRequest request,
            HttpServletResponse response) {
        String phoneNumber = request.getParameter("phoneNumber");
        User user = new User();
        user.setPhoneNumber(phoneNumber);

        // UserService userService = new UserService();
        JSONObject jsonObject = new JSONObject();
        // String checkFlag = userService.doCheckUserPermission(user);
        jsonObject.put("response", ResponseStatus.SUCCESS);

        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doCheckNoPayOrder(HttpServletRequest request,
            HttpServletResponse response) {
        String phoneNumber = request.getParameter("phoneNumber");

        OrderService orderService = new OrderService();
        JSONObject jsonObject = new JSONObject();
        String checkFlag = orderService.doCheckNoPayOrder(phoneNumber);
        jsonObject.put("response", checkFlag);

        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doOpenDoorIdentify(HttpServletRequest request,
            HttpServletResponse response) {
        BoxesService boxesService = new BoxesService();
        String boxId = request.getParameter("boxId");
        String phoneNumber = request.getParameter("phoneNumber");
        Boxes box = new Boxes();
        box.setBoxId(boxId);

        JSONObject jsonObject = new JSONObject();
        String openDoorIdentityFlag = boxesService.doOpenDoorIdentify(box,
                phoneNumber);
        jsonObject.put("response", openDoorIdentityFlag);

        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doManagerOpenDoorIdentify(HttpServletRequest request,
            HttpServletResponse response) {
        String boxId = request.getParameter("boxId");
        String customerWorkerId = request.getParameter("customerWorkerId");
        String sortOrAddGoods = request.getParameter("sortOrAddGoods");

        BoxesService boxesService = new BoxesService();
        String openDoorIdentityFlag = boxesService.doManagerOpenDoorIdentify(
                boxId, customerWorkerId, sortOrAddGoods);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", openDoorIdentityFlag);
        Boxes boxes = boxesService.findBoxesByBoxId(boxId);
        if (boxes != null) {
            jsonObject.put("boxName", boxesService.findBoxesByBoxId(boxId)
                    .getBoxName());
        }

        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doRegisterCurrentUser(HttpServletRequest request,
            HttpServletResponse response) {
        CurrentUserService currentUserService = new CurrentUserService();
        String phoneNumber = request.getParameter("phoneNumber");
        String customerWorkerId = request.getParameter("customerWorkerId");
        String boxId = request.getParameter("boxId");
        String customType = request.getParameter("customType");
        String userType = request.getParameter("userType");
        String agreementNO = request.getParameter("agreementNO");
        CurrentUser currentUser = new CurrentUser();
        currentUser.setPhoneNumber(phoneNumber);
        if (customerWorkerId != null) {
            currentUser.setCustomerWorkerId(Integer.valueOf(customerWorkerId));
        }
        currentUser.setBoxId(boxId);
        currentUser.setCustomType(customType);
        currentUser.setUserType(userType);
        currentUser.setAgreementNO(agreementNO);

        JSONObject jsonObject = new JSONObject();
        String registerCurrentUserFlag = currentUserService
                .doRegisterCurrentUser(currentUser);
        jsonObject.put("response", registerCurrentUserFlag);

        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doWXPayGetOpenId(HttpServletRequest request,
            HttpServletResponse response) {
        String code = request.getParameter("code");

        WXPayService wxPayService = new WXPayService();
        JSONObject jsonObject = new JSONObject();
        String returnOpenId = "";
        try {
            returnOpenId = wxPayService.doWXPayGetOpenId(code);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }

        jsonObject.put("response", returnOpenId);
        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doWXPayXiaDan(HttpServletRequest request,
            HttpServletResponse response) {
        String payOrderId = request.getParameter("payOrderId");
        String openId = request.getParameter("openId");
        String payTotal = request.getParameter("payTotal");

        WXPayService wxPayService = new WXPayService();

        JSONObject jsonObject = new JSONObject();
        String returnPrepayId = wxPayService.doWXPayXiaDan(payOrderId, openId,
                payTotal);
        jsonObject.put("response", returnPrepayId);

        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doWXPaySign(HttpServletRequest request,
            HttpServletResponse response) {
        String prePayId = request.getParameter("prePayId");

        WXPayService wxPayService = new WXPayService();
        JSONObject jsonObject = new JSONObject();
        String returnSign = wxPayService.doWXPaySign(prePayId);
        jsonObject.put("response", returnSign);

        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }

    }

    private void doWXPaySuccess(HttpServletRequest request,
            HttpServletResponse response) {
        String payOrderId = request.getParameter("payOrderId");
        Order order = new Order();
        order.setOrderId(payOrderId);
        order.setPayState(1);

        OrderService orderService = new OrderService();
        orderService.doWXPaySuccess(order);
    }

    private void doFindMyOrders(HttpServletRequest request,
            HttpServletResponse response) {
        String phoneNumber = request.getParameter("phoneNumber");
        String orderType = request.getParameter("orderType");
        String pageNumber = request.getParameter("pageNumber");
        int page = 0;
        try {
            page = Integer.valueOf(pageNumber);
        } catch (Exception e) {
            page = 0;
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }

        OrderService orderService = new OrderService();
        JSONObject jsonObject = new JSONObject();
        String findMyOrdersResult = orderService.doFindMyOrders(phoneNumber,
                orderType, page);

        if (findMyOrdersResult.equals(ResponseStatus.NO_ORDERS)) {
            jsonObject.put("response", "noOrders");
            try {
                response.getWriter().write(jsonObject.toString());
            } catch (Exception e) {
                log.error("try->catch exception:", e);
                e.printStackTrace();
            }
        } else if (findMyOrdersResult.equals(ResponseStatus.LAST_PAGE)) {
            jsonObject.put("response", "lastPage");
            try {
                response.getWriter().write(jsonObject.toString());
            } catch (Exception e) {
                log.error("try->catch exception:", e);
                e.printStackTrace();
            }
        } else {
            try {
                response.getWriter().write(findMyOrdersResult);
            } catch (Exception e) {
                log.error("try->catch exception:", e);
                e.printStackTrace();
            }
        }
    }

    private void doFindDynamicOrders(HttpServletRequest request,
            HttpServletResponse response) {
        OrderService orderService = new OrderService();
        JSONObject jsonObject = new JSONObject();
        String findDynamicOrdersResult = orderService.doFindDynamicOrders();
        if (findDynamicOrdersResult.equals(ResponseStatus.NO_ORDERS)) {
            jsonObject.put("response", "noOrders");
            try {
                response.getWriter().write(jsonObject.toString());
            } catch (Exception e) {
                log.error("try->catch exception:", e);
                e.printStackTrace();
            }
        } else {
            try {
                response.getWriter().write(findDynamicOrdersResult);
            } catch (Exception e) {
                log.error("try->catch exception:", e);
                e.printStackTrace();
            }
        }
    }

    private void doFindAllMyOrders(HttpServletRequest request,
            HttpServletResponse response) {
        String phoneNumber = request.getParameter("phoneNumber");

        OrderService orderService = new OrderService();
        String findMyOrdersResult = orderService.doFindAllMyOrders(phoneNumber);

        try {
            response.getWriter().write(findMyOrdersResult);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doFindMyReplenishGoodsOrder(HttpServletRequest request,
            HttpServletResponse response) {
        String boxDeliveryId = request.getParameter("boxDeliveryId");
        String pageNumber = request.getParameter("pageNumber");
        String boxId = request.getParameter("boxId");
        int page = 0;
        try {
            page = Integer.valueOf(pageNumber);
        } catch (Exception e) {
            page = 0;
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }

        ReplenishGoodsService replenishGoodsService = new ReplenishGoodsService();
        JSONObject jsonObject = new JSONObject();
        String findMyReplenishGoodsResult = ResponseStatus.NO_ORDERS;
        if (boxId != null && boxId.length() > 0) {
            findMyReplenishGoodsResult = replenishGoodsService
                    .doFindMyReplenishGoodsOrder(boxDeliveryId, boxId, page);
        } else {
            findMyReplenishGoodsResult = replenishGoodsService
                    .doFindMyReplenishGoodsOrder(boxDeliveryId, page);
        }

        if (findMyReplenishGoodsResult.equals(ResponseStatus.NO_ORDERS)) {
            jsonObject.put("response", "noOrders");
            try {
                response.getWriter().write(jsonObject.toString());
            } catch (Exception e) {
                log.error("try->catch exception:", e);
                e.printStackTrace();
            }
        } else if (findMyReplenishGoodsResult.equals(ResponseStatus.LAST_PAGE)) {
            jsonObject.put("response", "lastPage");
            try {
                response.getWriter().write(jsonObject.toString());
            } catch (Exception e) {
                log.error("try->catch exception:", e);
                e.printStackTrace();
            }
        } else {
            try {
                response.getWriter().write(findMyReplenishGoodsResult);
            } catch (Exception e) {
                log.error("try->catch exception:", e);
                e.printStackTrace();
            }
        }
    }

    private void doFindMyManageBoxes(HttpServletRequest request,
            HttpServletResponse response) {
        String boxDeliveryId = request.getParameter("boxDeliveryId");
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
        BoxesService boxesService = new BoxesService();
        String findMyManageBoxesResult = boxesService.doFindMyManageBoxes(
                boxDeliveryId, page);

        if (findMyManageBoxesResult.equals(ResponseStatus.NO_BOXES)) {
            jsonObject.put("response", "noBoxes");
            try {
                response.getWriter().write(jsonObject.toString());
            } catch (Exception e) {
                log.error("try->catch exception:", e);
                e.printStackTrace();
            }
        } else if (findMyManageBoxesResult.equals(ResponseStatus.LAST_PAGE)) {
            jsonObject.put("response", "lastPage");
            try {
                response.getWriter().write(jsonObject.toString());
            } catch (Exception e) {
                log.error("try->catch exception:", e);
                e.printStackTrace();
            }
        } else {
            try {
                response.getWriter().write(findMyManageBoxesResult);
            } catch (Exception e) {
                log.error("try->catch exception:", e);
                e.printStackTrace();
            }
        }
    }

    private void doGetBoxDetailsSummary(HttpServletRequest request,
            HttpServletResponse response) {
        String boxId = request.getParameter("boxId");

        BoxesService boxesService = new BoxesService();
        String boxDetailsSummaryResult = boxesService
                .doGetBoxDetailsSummary(boxId);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", boxDetailsSummaryResult);

        try {
            response.getWriter().write(jsonObject.toString());
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

        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doGetCurrentBoxAllGoods(HttpServletRequest request,
            HttpServletResponse response) {
        String boxId = request.getParameter("boxId");

        BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
        JSONObject jsonObject = new JSONObject();

        String boxGoodsResult = boxGoodsXXXService
                .doGetCurrentBoxAllGoods(boxId);
        jsonObject.put("response", boxGoodsResult);

        try {
            response.getWriter().write(jsonObject.toString());
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

        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doGetAllGoodsCategory(HttpServletRequest request,
            HttpServletResponse response) {
        GoodsCategoryService goodsCategoryService = new GoodsCategoryService();
        String findResultString = goodsCategoryService
                .doGetAllGoodsCategoryJSONString();

        try {
            response.getWriter().write(findResultString);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doInGoodss(HttpServletRequest request,
            HttpServletResponse response) {
        String inGoodss = request.getParameter("inGoodss");

        InOutGoodsService inOutGoodsService = new InOutGoodsService();
        String doInGoodssReturnFlagString = null;
        try {
            doInGoodssReturnFlagString = inOutGoodsService.doInGoodss(inGoodss);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }

        JSONObject jsonObject = new JSONObject();
        if (doInGoodssReturnFlagString != null) {
            jsonObject.put("response", doInGoodssReturnFlagString);
        } else {
            jsonObject.put("response", ResponseStatus.FAIL);
        }

        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doGetBoxInfo(HttpServletRequest request,
            HttpServletResponse response) {
        String boxId = request.getParameter("boxId");

        BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
        String boxGoodsInfoString = boxGoodsXXXService.doGetBoxInfo(boxId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", boxGoodsInfoString);

        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doGetNewBoxInfo(HttpServletRequest request,
            HttpServletResponse response) {
        String boxId = request.getParameter("boxId");

        BoxGoodsXXXNewService boxGoodsXXXNewService = new BoxGoodsXXXNewService();
        String boxGoodsInfoString = boxGoodsXXXNewService
                .doGetNewBoxInfo(boxId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", boxGoodsInfoString);

        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doGetWxJsSDKConfig(HttpServletRequest request,
            HttpServletResponse response) {
        String customURL = request.getParameter("url");
        String tokenType = request.getParameter("tokenType");

        TokenService tokenService = new TokenService();
        String wxJsSDKConfigResult = tokenService.doGetWxJsSDKConfig(customURL,
                tokenType);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", wxJsSDKConfigResult);

        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doGetWxOpenSession(HttpServletRequest request,
            HttpServletResponse response) {
        String code = request.getParameter("code");

        try {
            WxJsSDKService wxJsSDKService = new WxJsSDKService();
            String openSessionResult = wxJsSDKService.doWXGetOpenSession(code);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("response", openSessionResult);

            try {
                response.getWriter().write(jsonObject.toString());
            } catch (Exception e) {
                log.error("try->catch exception:", e);
                e.printStackTrace();
            }
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doWxPapayGetEntrustRequestUrl(HttpServletRequest request,
            HttpServletResponse response) {
        WxPapayService wxPapayService = new WxPapayService();
        String returnResult = wxPapayService.doGetEntrustRequestUrl();

        try {
            response.sendRedirect(returnResult);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doWxPapayGetEntrustBeanInfo(HttpServletRequest request,
            HttpServletResponse response) {
        WxPapayService wxPapayService = new WxPapayService();
        String returnResult = wxPapayService.doGetEntrustBeanInfo();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", returnResult);

        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doWxPapayQuerySignContract(HttpServletRequest request,
            HttpServletResponse response) {
        String code = request.getParameter("code");
        String phoneNumber = request.getParameter("phoneNumber");

        WxPapayService wxPapayService = new WxPapayService();
        String returnContractInfo = wxPapayService.doQuerySignContract(code,
                phoneNumber);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", returnContractInfo);

        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doUpdateContractId(HttpServletRequest request,
            HttpServletResponse response) {
        String phoneNumber = request.getParameter("phoneNumber");
        String contractId = request.getParameter("contractId");
        String customType = request.getParameter("customType");

        UserService userService = new UserService();
        String updateResult = userService.doUpdateContractId(phoneNumber,
                customType, contractId);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", updateResult);

        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doCheckFaceDetectFunction(HttpServletRequest request,
            HttpServletResponse response) {
        String phoneNumber = request.getParameter("phoneNumber");

        UserService userService = new UserService();
        String checkResult = userService.doCheckFaceDetectFunction(phoneNumber);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", checkResult);

        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doCheckManagerLoginIdentify(HttpServletRequest request,
            HttpServletResponse response) {
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");

        CustomerWorkerService customerWorkerService = new CustomerWorkerService();
        String checkResult = customerWorkerService.doCheckManagerLoginIdentify(
                userName, password);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", checkResult);

        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doInitBoxConfig(HttpServletRequest request,
            HttpServletResponse response) {
        String imeiNumber = request.getParameter("imeiNumber");

        BoxesService boxesService = new BoxesService();
        String initResult = boxesService.doInitBoxConfig(imeiNumber);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", initResult);

        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doGetUserInfo(HttpServletRequest request,
            HttpServletResponse response) {
        String phoneNumber = request.getParameter("phoneNumber");

        UserService userService = new UserService();
        String userInfo = userService.doGetUserInfo(phoneNumber);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", userInfo);

        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doFindMyCreditRecords(HttpServletRequest request,
            HttpServletResponse response) {
        String phoneNumber = request.getParameter("phoneNumber");
        String pageNumber = request.getParameter("pageNumber");
        int page = 0;
        try {
            page = Integer.valueOf(pageNumber);
        } catch (Exception e) {
            page = 0;
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }

        PersonalCreditRecordService personalCreditRecordService = new PersonalCreditRecordService();
        JSONObject jsonObject = new JSONObject();
        String findMyCrditRecordsResult = personalCreditRecordService
                .doFindMyCreditRecords(phoneNumber, page);
        if (findMyCrditRecordsResult
                .equals(ResponseStatus.NO_PERSONAL_CREDIT_RECORD)) {
            jsonObject.put("response", "noCreditRecords");
            try {
                response.getWriter().write(jsonObject.toString());
            } catch (Exception e) {
                log.error("try->catch exception:", e);
                e.printStackTrace();
            }
        } else if (findMyCrditRecordsResult.equals(ResponseStatus.LAST_PAGE)) {
            jsonObject.put("response", "lastPage");
            try {
                response.getWriter().write(jsonObject.toString());
            } catch (Exception e) {
                log.error("try->catch exception:", e);
                e.printStackTrace();
            }
        } else {
            try {
                response.getWriter().write(findMyCrditRecordsResult);
            } catch (Exception e) {
                log.error("try->catch exception:", e);
                e.printStackTrace();
            }
        }
    }

    private void doGetCurrentAndNewBoxGoods(HttpServletRequest request,
            HttpServletResponse response) {
        String boxId = request.getParameter("boxId");

        BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
        String getCurrentAndNewBoxGoodsResult = boxGoodsXXXService
                .doGetCurrentAndNewBoxGoods(boxId);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", getCurrentAndNewBoxGoodsResult);
        // log.info("doGetCurrentAndNewBoxGoods:->" + jsonObject.toString());
        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doUpdateCurrentBoxGoods(HttpServletRequest request,
            HttpServletResponse response) {
        String boxId = request.getParameter("boxId");

        BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
        String getCurrentAndNewBoxGoodsResult = boxGoodsXXXService
                .doUpdateBoxCardgoRoadGoods(boxId);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", getCurrentAndNewBoxGoodsResult);
        // log.info("doUpdateCurrentBoxGoods:->" + jsonObject.toString());
        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doUpdateCurrentBoxGoodsPrice(HttpServletRequest request,
            HttpServletResponse response) {
        String boxIdsStr = request.getParameter("boxId");

        // 通知Android主控同步更新货道商品价格信息
        BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
        boxGoodsXXXService.notifyAndroidUpdateCurrentBoxGoodsPrice(boxIdsStr);
    }

    private void doGetCurrentBoxGoods(HttpServletRequest request,
            HttpServletResponse response) {
        String boxId = request.getParameter("boxId");

        BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
        String getCurrentBoxGoodsResult = boxGoodsXXXService
                .doGetCurrentBoxGoods(boxId);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", getCurrentBoxGoodsResult);
        // log.info("doGetCurrentAndNewBoxGoods:->" + jsonObject.toString());
        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doFindOrderTotalDescription(HttpServletRequest request,
            HttpServletResponse response) {
        String phoneNumber = request.getParameter("phoneNumber");

        OrderService orderService = new OrderService();
        String findResult = orderService
                .doFindOrderTotalDescription(phoneNumber);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", findResult);
        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doUpdateCardgoRoadGoodsStockNumber(HttpServletRequest request,
            HttpServletResponse response) {
        String boxId = request.getParameter("boxId");
        String currentReplenishId = request.getParameter("currentReplenishId");
        String cardgoRoadId = request.getParameter("cardgoRoadId");
        String barCodeId = request.getParameter("barCodeId");
        String updateStockNumber = request.getParameter("updateStockNumber");

        BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
        try {
            String updateResult = boxGoodsXXXService
                    .doNotifyAndroidUpdateCardgoRoadGoodsStockNumber(
                            currentReplenishId, boxId, cardgoRoadId, barCodeId,
                            updateStockNumber);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("response", updateResult);
            try {
                response.getWriter().write(jsonObject.toString());
            } catch (Exception e) {
                log.error("try->catch exception:", e);
                e.printStackTrace();
            }
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doFindNearbyBoxes(HttpServletRequest request,
            HttpServletResponse response) {
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
        BoxesService boxesService = new BoxesService();
        String findNearbyBoxesResult = boxesService.doFindNearbyBoxes(fromLat,
                fromLng, ResponseStatus.NEARBY_BOXES_NEAYBYRANGE, page);

        if (findNearbyBoxesResult.equals(ResponseStatus.NO_BOXES)) {
            jsonObject.put("response", "noBoxes");
            try {
                response.getWriter().write(jsonObject.toString());
            } catch (Exception e) {
                log.error("try->catch exception:", e);
                e.printStackTrace();
            }
        } else if (findNearbyBoxesResult.equals(ResponseStatus.LAST_PAGE)) {
            jsonObject.put("response", "lastPage");
            try {
                response.getWriter().write(jsonObject.toString());
            } catch (Exception e) {
                log.error("try->catch exception:", e);
                e.printStackTrace();
            }
        } else {
            try {
                response.getWriter().write(findNearbyBoxesResult);
            } catch (Exception e) {
                log.error("try->catch exception:", e);
                e.printStackTrace();
            }
        }
    }

    private void doFindNearbyBoxesOnMap(HttpServletRequest request,
            HttpServletResponse response) {
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
        BoxesService boxesService = new BoxesService();
        String findNearbyBoxesResult = boxesService
                .doFindNearbyBoxesOnMap(fromLat, fromLng,
                        ResponseStatus.NEARBY_BOXES_NEAYBYRANGE, page);

        if (findNearbyBoxesResult.equals(ResponseStatus.NO_BOXES)) {
            jsonObject.put("response", "noBoxes");
            try {
                response.getWriter().write(jsonObject.toString());
            } catch (Exception e) {
                log.error("try->catch exception:", e);
                e.printStackTrace();
            }
        } else if (findNearbyBoxesResult.equals(ResponseStatus.LAST_PAGE)) {
            jsonObject.put("response", "lastPage");
            try {
                response.getWriter().write(jsonObject.toString());
            } catch (Exception e) {
                log.error("try->catch exception:", e);
                e.printStackTrace();
            }
        } else {
            try {
                response.getWriter().write(findNearbyBoxesResult);
            } catch (Exception e) {
                log.error("try->catch exception:", e);
                e.printStackTrace();
            }
        }
    }

    private void doLingmaoUpgrade(HttpServletRequest request,
            HttpServletResponse response) {
        String action = request.getParameter("action");
        String boxIdStr = request.getParameter("boxId");
        String apkUrl = request.getParameter("apkUrl");
        String appVersion = request.getParameter("appVersion");
        DeviceMonitorService deviceMonitorService = new DeviceMonitorService();
        deviceMonitorService.doLingmaoUpgrade(action, boxIdStr, apkUrl,
                appVersion);
    }

    private void doLingmaoCoreUpgrade(HttpServletRequest request,
            HttpServletResponse response) {
        String action = request.getParameter("action");
        String boxIdStr = request.getParameter("boxId");
        String apkUrl = request.getParameter("apkUrl");
        String appVersion = request.getParameter("appVersion");
        DeviceMonitorService deviceMonitorService = new DeviceMonitorService();
        deviceMonitorService.doLingmaoCoreUpgrade(action, boxIdStr, apkUrl,
                appVersion);
    }

    private void doLingmaoToolsUpgrade(HttpServletRequest request,
            HttpServletResponse response) {
        String action = request.getParameter("action");
        String boxIdStr = request.getParameter("boxId");
        String apkUrl = request.getParameter("apkUrl");
        String appVersion = request.getParameter("appVersion");
        DeviceMonitorService deviceMonitorService = new DeviceMonitorService();
        deviceMonitorService.doLingmaoToolsUpgrade(action, boxIdStr, apkUrl,
                appVersion);
    }

    private void doLingmaoAllAppsUpgrade(HttpServletRequest request,
            HttpServletResponse response) {
        String action = request.getParameter("action");
        String boxIdStr = request.getParameter("boxId");
        String appsInfo = request.getParameter("appsInfo");
        log.info("doLingmaoAllAppsUpgrade:->action=" + action + ",boxIdStr="
                + boxIdStr + ",appsInfo=" + appsInfo);
        DeviceMonitorService deviceMonitorService = new DeviceMonitorService();
        deviceMonitorService
                .doLingmaoAllAppsUpgrade(action, boxIdStr, appsInfo);
    }

    private void doFotaUpgrade(HttpServletRequest request,
            HttpServletResponse response) {
        String action = request.getParameter("action");
        String boxIdStr = request.getParameter("boxId");
        String appVersion = request.getParameter("appVersion");
        DeviceMonitorService deviceMonitorService = new DeviceMonitorService();
        deviceMonitorService.doFotaUpgrade(action, boxIdStr, appVersion);
    }

    private void doSetDebugMode(HttpServletRequest request,
            HttpServletResponse response) {
        String action = request.getParameter("action");
        String boxIdStr = request.getParameter("boxId");
        String mode = request.getParameter("value");
        DeviceMonitorService deviceMonitorService = new DeviceMonitorService();
        deviceMonitorService.doSetDebugMode(action, boxIdStr, mode);
    }

    private void doSetLogLevel(HttpServletRequest request,
            HttpServletResponse response) {
        String action = request.getParameter("action");
        String boxIdStr = request.getParameter("boxId");
        String logLevel = request.getParameter("value");
        DeviceMonitorService deviceMonitorService = new DeviceMonitorService();
        deviceMonitorService.doSetLogLevel(action, boxIdStr, logLevel);
    }

    private void doPushAdv(HttpServletRequest request,
            HttpServletResponse response) {
        String action = request.getParameter("action");
        String boxIdStr = request.getParameter("boxId");
        String advUrl = request.getParameter("advUrl");
        String advVersion = request.getParameter("advVersion");
        DeviceMonitorService deviceMonitorService = new DeviceMonitorService();
        deviceMonitorService.doPushAdv(action, boxIdStr, advUrl, advVersion);
    }

    private void doRemoveAdv(HttpServletRequest request,
            HttpServletResponse response) {
        String action = request.getParameter("action");
        String boxIdStr = request.getParameter("boxId");
        DeviceMonitorService deviceMonitorService = new DeviceMonitorService();
        deviceMonitorService.doRemoveAdv(action, boxIdStr);
    }

    private void doRestart(HttpServletRequest request,
            HttpServletResponse response) {
        String action = request.getParameter("action");
        String boxIdStr = request.getParameter("boxId");
        String value = request.getParameter("value");
        DeviceMonitorService deviceMonitorService = new DeviceMonitorService();
        deviceMonitorService.doRestart(action, boxIdStr, value);
    }

    private void doBootCheckSelf(HttpServletRequest request,
            HttpServletResponse response) {
        String action = request.getParameter("action");
        String boxIdStr = request.getParameter("boxId");
        String value = request.getParameter("value");
        DeviceMonitorService deviceMonitorService = new DeviceMonitorService();
        deviceMonitorService.doBootCheckSelf(action, boxIdStr, value);
    }

    private void doDeviceRegister(HttpServletRequest request,
            HttpServletResponse response) {
        String icsIMEI = request.getParameter("icsIMEI");
        String boxBodyModel = request.getParameter("boxBodyModel");
        String boxBodySnNumber = request.getParameter("boxBodySnNumber");
        String workerId = request.getParameter("workerId");

        JSONObject jsonObject = new JSONObject();
        BoxesService boxesService = new BoxesService();
        String registerResult = boxesService.doDeviceRegister(icsIMEI,
                boxBodyModel, boxBodySnNumber, workerId);

        jsonObject.put("response", registerResult);
        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doBoxRegisterBoxIdentify(HttpServletRequest request,
            HttpServletResponse response) {
        String boxId = request.getParameter("boxId");

        BoxesService boxesService = new BoxesService();
        String identifyResult = boxesService.doBoxRegisterBoxIdentify(boxId);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", identifyResult);
        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doFindAllCustomers(HttpServletRequest request,
            HttpServletResponse response) {
        CustomerService customerService = new CustomerService();
        String findResult = customerService.doFindAllCustomers();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", findResult);
        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doFindCustomerShopes(HttpServletRequest request,
            HttpServletResponse response) {
        String customerId = request.getParameter("customerId");

        BoxShopesService boxShopesService = new BoxShopesService();
        String customerShopesResult = boxShopesService
                .doFindCustomerShopes(customerId);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", customerShopesResult);
        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doReportSystemInfo(HttpServletRequest request,
            HttpServletResponse response) {
        String boxId = request.getParameter("boxId");
        String systemInfo = request.getParameter("systemInfo");

        DeviceMonitorService deviceMonitorService = new DeviceMonitorService();
        String reportResult = deviceMonitorService.doReportSystemInfo(boxId,
                systemInfo);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", reportResult);
        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }

    }

    private void doGetSignInInfo(HttpServletRequest request,
            HttpServletResponse response) {
        String phoneNumber = request.getParameter("phoneNumber");

        SignInService signInService = new SignInService();
        String signInfoResult = signInService.doGetSignInInfo(phoneNumber);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", signInfoResult);
        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doResetSignIn(HttpServletRequest request,
            HttpServletResponse response) {
        String phoneNumber = request.getParameter("phoneNumber");

        SignInService signInService = new SignInService();
        String resetResult = signInService.doResetSignIn(phoneNumber);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", resetResult);
        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doSignIn(HttpServletRequest request,
            HttpServletResponse response) {
        String phoneNumber = request.getParameter("phoneNumber");
        String willGetScroes = request.getParameter("willGetScroes");
        String signInLatitude = request.getParameter("signInLatitude");
        String signInLongitude = request.getParameter("signInLongitude");

        SignInService signInService = new SignInService();
        String signInResult = signInService.doSignIn(phoneNumber,
                willGetScroes, signInLatitude, signInLongitude);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", signInResult);
        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doGetPhoneNumberFromWX(HttpServletRequest request,
            HttpServletResponse response) {
        String sessionKey = request.getParameter("sessionKey");
        String encryptedData = request.getParameter("encryptedData");
        String iv = request.getParameter("iv");

        UserService userService = new UserService();
        String phoneNumberInfo = userService.doGetPhoneNumberFromWX(
                encryptedData, iv, sessionKey);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", phoneNumberInfo);
        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doGetVideo(HttpServletRequest request,
            HttpServletResponse response) {
        String action = request.getParameter("action");
        String boxIdStr = request.getParameter("boxId");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");

        DeviceMonitorService deviceMonitorService = new DeviceMonitorService();
        deviceMonitorService.doGetVideo(action, boxIdStr, startTime, endTime);
    }

    private void doGetLog(HttpServletRequest request,
            HttpServletResponse response) {
        String action = request.getParameter("action");
        String boxIdStr = request.getParameter("boxId");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");

        DeviceMonitorService deviceMonitorService = new DeviceMonitorService();
        deviceMonitorService.doGetLog(action, boxIdStr, startTime, endTime);
    }

    private void doGetDynamicPassword(HttpServletRequest request,
            HttpServletResponse response) {
        String phoneNumber = request.getParameter("phoneNumber");

        DynamicPasswordService dynamicPasswordService = new DynamicPasswordService();
        String dynamicPassword = dynamicPasswordService
                .doGetDynamicPassword(phoneNumber);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", dynamicPassword);
        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doCheckDynamicPasswordState(HttpServletRequest request,
            HttpServletResponse response) {
        String dynamicPassword = request.getParameter("dynamicPassword");

        DynamicPasswordService dynamicPasswordService = new DynamicPasswordService();
        String checkResult = dynamicPasswordService
                .doCheckDynamicPasswordState(dynamicPassword);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", checkResult);
        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doCheckDynamicPassword(HttpServletRequest request,
            HttpServletResponse response) {
        String boxId = request.getParameter("boxId");
        String dynamicPassword = request.getParameter("dynamicPassword");

        DynamicPasswordService dynamicPasswordService = new DynamicPasswordService();
        String checkResult = dynamicPasswordService.doCheckDynamicPassword(
                boxId, dynamicPassword);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", checkResult);
        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doGetBoxName(HttpServletRequest request,
            HttpServletResponse response) {
        String boxId = request.getParameter("boxId");
        BoxesService boxesService = new BoxesService();

        String boxName = boxesService.doGetBoxName(boxId);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", boxName);
        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doGetPlatformAdInfo(HttpServletRequest request,
            HttpServletResponse response) {
        BoxesService boxesService = new BoxesService();

        String adInfo = boxesService.doGetPlatformAdInfo();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", adInfo);
        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doGetCustomerInfo(HttpServletRequest request,
            HttpServletResponse response) {
        String boxId = request.getParameter("boxId");

        BoxesService boxesService = new BoxesService();

        String customerInfo = boxesService.doGetCustomerInfo(boxId);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", customerInfo);
        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doShutDownIdentifyWithBoxId(HttpServletRequest request,
            HttpServletResponse response) {
        String boxId = request.getParameter("boxId");

        BoxesService boxesService = new BoxesService();
        String identifyResult = boxesService.doShutDownIdentifyWithBoxId(boxId);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", identifyResult);
        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doShutDownIdentifyWithBoxBodySnNumber(
            HttpServletRequest request, HttpServletResponse response) {
        String boxBodySnNumber = request.getParameter("boxBodySnNumber");

        BoxesService boxesService = new BoxesService();
        String identifyResult = boxesService
                .doShutDownIdentifyWithBoxBodySnNumber(boxBodySnNumber);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", identifyResult);
        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doShutDownDevice(HttpServletRequest request,
            HttpServletResponse response) {
        String boxId = request.getParameter("boxId");

        DeviceMonitorService deviceMonitorService = new DeviceMonitorService();
        String shutdownResult = deviceMonitorService.doShutDownDevice(boxId);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", shutdownResult);
        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doGetExceptionShoppings(HttpServletRequest request,
            HttpServletResponse response) {
        String state = request.getParameter("state");

        ExceptionShoppingService exceptionShoppingService = new ExceptionShoppingService();
        String exceptionShoppingsResult = exceptionShoppingService
                .doGetExceptionShoppingsByState(Integer.valueOf(state));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", exceptionShoppingsResult);
        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doMonitorUserLogin(HttpServletRequest request,
            HttpServletResponse response) {
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");

        CallCenterNormalService callCenterNormalService = new CallCenterNormalService();
        String loginResult = callCenterNormalService.doMonitorUserLogin(
                userName, password);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", loginResult);
        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doMonitorExceptionShopping(HttpServletRequest request,
            HttpServletResponse response) {
        String exceptionId = request.getParameter("exceptionId");
        String shopExceptionInfo = request.getParameter("shopExceptionInfo");
        // String phoneNumber = request.getParameter("phoneNumber");
        // String openDoorRequestSerialNumber = request
        // .getParameter("requestSerialNumber");

        ExceptionShoppingService exceptionShoppingService = new ExceptionShoppingService();
        String handleExceptionShoppingResult = exceptionShoppingService
                .doMonitorExceptionShopping(null, shopExceptionInfo, null, null);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", handleExceptionShoppingResult);
        jsonObject.put("exceptionId", exceptionId);
        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doGetShoppingInfo(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String boxId = request.getParameter("boxId");
        String state = request.getParameter("state");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String pageNumber = request.getParameter("pageNumber");
        String pageSize = request.getParameter("pageSize");

        int page = 0;
        try {
            page = Integer.valueOf(pageNumber);
        } catch (Exception e) {
            page = 0;
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }

        ShoppingInfoService shoppingInfoService = new ShoppingInfoService();
        JSONObject jsonObject = new JSONObject();
        String findShoppingInfosResult = shoppingInfoService
                .doFindShoppingInfos(boxId, state, startTime, endTime, page,
                        Integer.valueOf(pageSize));
        if (findShoppingInfosResult.equals(ResponseStatus.NO_SHOPPING_INFO)) {
            jsonObject.put("response", ResponseStatus.NO_SHOPPING_INFO);
            try {
                response.getWriter().write(jsonObject.toString());
            } catch (Exception e) {
                log.error("try->catch exception:", e);
                e.printStackTrace();
            }
        } else if (findShoppingInfosResult.equals(ResponseStatus.LAST_PAGE)) {
            jsonObject.put("response", ResponseStatus.LAST_PAGE);
            try {
                response.getWriter().write(jsonObject.toString());
            } catch (Exception e) {
                log.error("try->catch exception:", e);
                e.printStackTrace();
            }
        } else {
            try {
                jsonObject.put("response", "shoppingInfo");
                jsonObject.put("shoppingInfo", findShoppingInfosResult);
                response.getWriter().write(jsonObject.toString());
            } catch (Exception e) {
                log.error("try->catch exception:", e);
                e.printStackTrace();
            }
        }

    }

    private void doGetShoppingInfoVideos(HttpServletRequest request,
            HttpServletResponse response) {
        String shoppingInfoId = request.getParameter("shoppingInfoId");
        String videoUrlInfo = request.getParameter("videoUrlInfo");
        String boxId = request.getParameter("boxId");

        ShoppingInfoService shoppingInfoService = new ShoppingInfoService();
        String getVideosResult = shoppingInfoService.doGetShoppingInfoVideos(
                shoppingInfoId, videoUrlInfo, boxId);

        try {
            response.getWriter().write(getVideosResult);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doUpdateShoppingInfoVideosUrl(HttpServletRequest request,
            HttpServletResponse response) {
        String shoppingInfoId = request.getParameter("shoppingInfoId");
        String videoUrlInfo = request.getParameter("videoUrlInfo");
        String boxId = request.getParameter("boxId");

        ShoppingInfoService shoppingInfoService = new ShoppingInfoService();
        String updateResult = shoppingInfoService
                .doUpdateShoppingInfoVideosUrl(shoppingInfoId, videoUrlInfo,
                        boxId);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", updateResult);
        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doUpdateShoppingInfoState(HttpServletRequest request,
            HttpServletResponse response) {
        String shoppingInfoId = request.getParameter("shoppingInfoId");
        String resolverUser = request.getParameter("resolverUser");
        String state = request.getParameter("state");

        ShoppingInfoService shoppingInfoService = new ShoppingInfoService();
        String updateStateResult = shoppingInfoService
                .doUpdateShoppingInfoState(shoppingInfoId, resolverUser, state);

        try {
            response.getWriter().write(updateStateResult);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doSaledGoodsInfo(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String orderType = request.getParameter("orderType");
        String boxId = request.getParameter("boxId");
        String saledGoodsInfoId = request.getParameter("saledGoodsInfoId");
        String saledGoodsInfo = request.getParameter("saledGoodsInfo");
        String phoneNumber = request.getParameter("phoneNumber");
        String openDoorRequestSerialNumber = request
                .getParameter("requestSerialNumber");

        // 目前线上版本无orderType参数，该逻辑为了兼容线上版本
        if (orderType == null) {
            BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
            String saledGoodsInfoResult = boxGoodsXXXService.doSaledGoodsInfo(
                    orderType, boxId, phoneNumber, openDoorRequestSerialNumber,
                    saledGoodsInfo);

            JSONObject jsonObject = new JSONObject();
            if (!(saledGoodsInfoResult.equals(ResponseStatus.FAIL))) {
                jsonObject.put("response", ResponseStatus.SUCCESS);
                jsonObject.put("saledGoodsInfoId", saledGoodsInfoId);
                jsonObject.put("orderId", saledGoodsInfoResult);
            } else {
                jsonObject.put("response", ResponseStatus.FAIL);
                jsonObject.put("saledGoodsInfoId", saledGoodsInfoId);
            }
            try {
                response.getWriter().write(jsonObject.toString());
            } catch (Exception e) {
                log.error("try->catch exception:", e);
                e.printStackTrace();
            }
        } else {
            // 做不锁门逻辑处理
            // 正常订单（正常盘点的明确订单）或者从视频端人工处理后的订单
            if (orderType.equals(ResponseStatus.ORDER_TYPE_NORMAL)
                    || orderType.equals(ResponseStatus.ORDER_TYPE_DELAY)) {
                BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
                String saledGoodsInfoResult = boxGoodsXXXService
                        .doSaledGoodsInfo(orderType, boxId, phoneNumber,
                                openDoorRequestSerialNumber, saledGoodsInfo);

                JSONObject jsonObject = new JSONObject();
                if (!(saledGoodsInfoResult.equals(ResponseStatus.FAIL))) {
                    // 当前订单由视频端发出时即orderType=delayOrder时，目前数据库表中记录的该shoppingInfo的orderId=工控机异常订单直接返回的shoppingInfoId，服务器更新该shoppingInfo中的orderId为当前生成的orderId，方便以后修改订单操作
                    if (orderType.equals(ResponseStatus.ORDER_TYPE_DELAY)) {
                        ShoppingInfoService shoppingInfoService = new ShoppingInfoService();
                        shoppingInfoService.doUpdateShoppingInfoOrderId(
                                saledGoodsInfoResult, saledGoodsInfoId);
                    }
                    jsonObject.put("response", ResponseStatus.SUCCESS);
                    jsonObject.put("saledGoodsInfoId", saledGoodsInfoId);
                    jsonObject.put("orderId", saledGoodsInfoResult);
                } else {
                    jsonObject.put("response", ResponseStatus.FAIL);
                    jsonObject.put("saledGoodsInfoId", saledGoodsInfoId);
                }
                try {
                    response.getWriter().write(jsonObject.toString());
                } catch (Exception e) {
                    log.error("try->catch exception:", e);
                    e.printStackTrace();
                }
            } else {
                // 异常订单（未能正常盘点已经报了视频的异常订单）
                // 异常订单直接返回不做结单操作,返回的orderId用saledGoodsInfoId替代
                // 发送异常购物异常给处理人员
                // 异常视频订单，先恢复售货机状态，以保证后续消费者继续购物
                BoxStatusService boxStatusService = new BoxStatusService();
                boxStatusService.setOpenDoorState(boxId, false);
                ExceptionShoppingService exceptionShoppingService = new ExceptionShoppingService();
                exceptionShoppingService.sendExceptionEmail(boxId);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("response", ResponseStatus.SUCCESS);
                jsonObject.put("saledGoodsInfoId", saledGoodsInfoId);
                jsonObject.put("orderId", saledGoodsInfoId);
                try {
                    response.getWriter().write(jsonObject.toString());
                } catch (Exception e) {
                    log.error("try->catch exception:", e);
                    e.printStackTrace();
                }
            }
        }
    }

    private void doNotifyManager(HttpServletRequest request,
            HttpServletResponse response) {
        String boxId = request.getParameter("boxId");
        String phoneNumber = request.getParameter("phoneNumber");
        String creditScores = request.getParameter("creditScore");

        ExceptionShoppingService exceptionShoppingService = new ExceptionShoppingService();
        String notifyResult = exceptionShoppingService.doNotifyManager(boxId,
                phoneNumber, creditScores);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", notifyResult);
        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doGetBoxStatus(HttpServletRequest request,
            HttpServletResponse response) {
        String boxId = request.getParameter("boxId");

        BoxStatusService boxStatusService = new BoxStatusService();
        int boxState = boxStatusService.doGetBoxStatus(boxId);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", ResponseStatus.SUCCESS);
        jsonObject.put("boxState", boxState);
        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doSyncBoxStockInfo(HttpServletRequest request,
            HttpServletResponse response) {
        String boxId = request.getParameter("boxId");
        String stockInfo = request.getParameter("stockInfo");

        BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
        String syncResult = boxGoodsXXXService.doSyncBoxStockInfo(boxId,
                stockInfo);

        try {
            response.getWriter().write(syncResult);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doSyncBoxStockInfoFinished(HttpServletRequest request,
            HttpServletResponse response) {
        String boxId = request.getParameter("boxId");
        String state = request.getParameter("state");

        BoxStockSyncStatusService boxStockSyncStatusService = new BoxStockSyncStatusService();
        String syncResult = boxStockSyncStatusService
                .doSyncBoxStockInfoFinished(boxId, state);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", syncResult);
        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doGetBoxSyncStockStatus(HttpServletRequest request,
            HttpServletResponse response) {
        String boxId = request.getParameter("boxId");

        BoxStockSyncStatusService boxStockSyncStatusService = new BoxStockSyncStatusService();
        String syncStatus = boxStockSyncStatusService
                .doGetBoxSyncStockStatus(boxId);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", syncStatus);
        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doGetAllShoppingInfoCustomers(HttpServletRequest request,
            HttpServletResponse response) {
        CustomerService customerService = new CustomerService();
        String findResult = customerService.doFindAllShoppingInfoCustomers();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", findResult);
        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doGetCustomerShoppingInfoBoxes(HttpServletRequest request,
            HttpServletResponse response) {
        String customerId = request.getParameter("customerId");

        BoxesService boxesService = new BoxesService();
        String customerBoxesResult = boxesService
                .doGetCustomerShoppingInfoBoxes(customerId);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", customerBoxesResult);
        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doUpdateSaledGoodsInfo(HttpServletRequest request,
            HttpServletResponse response) {
        String orderId = request.getParameter("orderId");
        String boxId = request.getParameter("boxId");
        String saledGoodsInfoId = request.getParameter("saledGoodsInfoId");
        String saledGoodsInfo = request.getParameter("saledGoodsInfo");

        BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
        String updateSaledGoodsInfoResult = boxGoodsXXXService
                .doUpdateSaledGoodsInfo(orderId, boxId, saledGoodsInfo);

        JSONObject jsonObject = new JSONObject();
        if (!(updateSaledGoodsInfoResult.equals(ResponseStatus.FAIL))) {
            jsonObject.put("response", ResponseStatus.SUCCESS);
            jsonObject.put("saledGoodsInfoId", saledGoodsInfoId);
            jsonObject.put("orderId", updateSaledGoodsInfoResult);
        } else {
            jsonObject.put("response", ResponseStatus.FAIL);
            jsonObject.put("saledGoodsInfoId", saledGoodsInfoId);
        }
        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doUpdatedGoodsInfo(HttpServletRequest request,
            HttpServletResponse response) {
        String boxId = request.getParameter("boxId");
        String updatedGoodsInfoId = request.getParameter("updatedGoodsInfoId");
        String updatedGoodsInfo = request.getParameter("updatedGoodsInfo");

        BoxGoodsXXXService boxGoodsXXXService = new BoxGoodsXXXService();
        String updatedGoodsInfoResult = boxGoodsXXXService.doUpdatedGoodsInfo(
                boxId, updatedGoodsInfo);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", updatedGoodsInfoResult);
        jsonObject.put("updatedGoodsInfoId", updatedGoodsInfoId);
        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doFindCustomerBoxes(HttpServletRequest request,
            HttpServletResponse response) {
        String customerId = request.getParameter("customerId");

        BoxesService boxesService = new BoxesService();
        String customerBoxesResult = boxesService
                .doFindCustomerBoxes(customerId);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", customerBoxesResult);
        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

}
