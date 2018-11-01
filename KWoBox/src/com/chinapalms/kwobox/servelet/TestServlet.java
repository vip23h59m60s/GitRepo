package com.chinapalms.kwobox.servelet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.service.BoxesService;
import com.chinapalms.kwobox.utils.IPUtils;
import com.chinapalms.kwobox.utils.ResponseStatus;

public class TestServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    Log log = LogFactory.getLog(TestServlet.class);

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setCharacterEncoding("utf-8");
            response.setContentType("application/json;charset=utf-8");
            doTest(request, response);
        } catch (Exception e) {
            log.error("TestServlet->exception:" + e.toString());
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private void doTest(HttpServletRequest request, HttpServletResponse response) {
        String action = request.getParameter("action");
        log.info("TestServlet: action=" + action);

        if (action != null) {
            if (action.equals("shoppingTest")) {
                doShoppingTest(request, response);
            }
        }
    }

    public void doShoppingTest(HttpServletRequest request,
            HttpServletResponse response) {
        String boxId = request.getParameter("boxId");
        notifyAndroidOpenDoor(ResponseStatus.CUSTOM_CATEGORY_ANDROID, boxId,
                ResponseStatus.PERMISSION_NORMAL_USER);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", "success");
        try {
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通知Android设备去开门
     * 
     * @param customCategory
     * @param boxId
     */
    private void notifyAndroidOpenDoor(String customCategory, String boxId,
            String userPermission) {
        BoxesService boxService = new BoxesService();
        try {
            boxService.notifyAndroidOpenDoor(
                    ResponseStatus.OPEN_DOOR_REQUEST_SERIALNUMBER_NORMAL,
                    customCategory, boxId, userPermission);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    private void doLingmaoUpgrade(HttpServletRequest request,
            HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", "LingmaoUpgrade");
        jsonObject.put("apkUrl",
                "http://www.zigoomo.com/apks/Lingmao/Lingmao_v1.2.5_test.apk");
        jsonObject.put("appVersion", "Lingmao_v1.2.5_test");
        Session session = SessionMapFactory.getInstance().getCurrentSession(
                ResponseStatus.CUSTOM_CATEGORY_ANDROID, "M03-000010");
        try {
            SessionMapFactory.getInstance().sendMessage(session,
                    jsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doLingmaoToolsUpgrade(HttpServletRequest request,
            HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", "LingmaoToolsUpgrade");
        jsonObject
                .put("apkUrl",
                        "http://www.zigoomo.com/apks/LingmaoTools/LingmaoTools_v1.1.2_test.apk");
        jsonObject.put("appVersion", "LingmaoTools_v1.1.2_test");
        Session session = SessionMapFactory.getInstance().getCurrentSession(
                ResponseStatus.CUSTOM_CATEGORY_ANDROID, "M03-000010");
        try {
            SessionMapFactory.getInstance().sendMessage(session,
                    jsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doFotaUpgrade(HttpServletRequest request,
            HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", "fotaUpgrade");
        jsonObject.put("appVersion", "T2000_V2.0.0_20180505_test");
        Session session = SessionMapFactory.getInstance().getCurrentSession(
                ResponseStatus.CUSTOM_CATEGORY_ANDROID, "M03-000010");
        try {
            SessionMapFactory.getInstance().sendMessage(session,
                    jsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doSetDebugMode(HttpServletRequest request,
            HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", "debugMode");
        jsonObject.put("mode", "normal");
        Session session = SessionMapFactory.getInstance().getCurrentSession(
                ResponseStatus.CUSTOM_CATEGORY_ANDROID, "M03-000010");
        try {
            SessionMapFactory.getInstance().sendMessage(session,
                    jsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doTestIp(HttpServletRequest request,
            HttpServletResponse response) {
        try {
            log.info("start doTestIP!!!!!!!!!!!!!!!!!!!!!!!!!!");
            InetAddress address = InetAddress.getLocalHost();// 获取的是本地的IP地址
            // //PC-20140317PXKX/192.168.0.121
            String hostAddress = address.getHostAddress();// 192.168.0.121
            InetAddress address1 = InetAddress.getByName("www.zigoomo.com");

            log.info("hostAddress=" + hostAddress + ",address1=" + address1);

            InetAddress[] addresses = InetAddress
                    .getAllByName("www.zigoomo.com");// 根据主机名返回其可能的所有InetAddress对象
            for (InetAddress add : addresses) {
                log.info("add=" + add.getHostAddress());
            }
        } catch (UnknownHostException e) {
            log.error("exception:", e);
            e.printStackTrace();
        }
    }

    private static String getLinuxLocalIp() throws SocketException {
        String ip = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                String name = intf.getName();
                if (!name.contains("docker") && !name.contains("lo")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf
                            .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            String ipaddress = inetAddress.getHostAddress()
                                    .toString();
                            if (!ipaddress.contains("::")
                                    && !ipaddress.contains("0:0:")
                                    && !ipaddress.contains("fe80")) {
                                ip = ipaddress;
                                System.out.println(ipaddress);
                            }
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            System.out.println("获取ip地址异常");
            ip = "127.0.0.1";
            ex.printStackTrace();
        }
        System.out.println("IP:" + ip);
        return ip;
    }

}
