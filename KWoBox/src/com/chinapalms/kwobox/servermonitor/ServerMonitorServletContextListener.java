package com.chinapalms.kwobox.servermonitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.naming.java.javaURLContextFactory;
import org.junit.Test;

import com.chinapalms.kwobox.javabean.SenderEmail;
import com.chinapalms.kwobox.service.SenderEmailService;
import com.chinapalms.kwobox.utils.JavaMailUtil;
import com.chinapalms.kwobox.utils.ResponseStatus;

public class ServerMonitorServletContextListener implements
        ServletContextListener {

    Log log = LogFactory.getLog(ServerMonitorServletContextListener.class);

    // 尝试ping服务器的失败的次数
    private static int pingErrorTimes = 0;
    // 是否需要重启Tomcat
    private static boolean needRebootTomcat = false;
    // 定时器
    private static ScheduledExecutorService service;
    // 重复ping服务器次数
    private final static int pingServerRetryTimes = 5;
    // 每次ping服务器失败以后的时间间隔(s)
    private final static int retryDuring = 60;
    // Tomcat 启动完成以后delay多长时间开启监控线程(s)
    private final static int startPingServerDelay = 60;
    // 测试网络连接的url
    private final static String urlString = "https://www.zigoomo.com/KWoBox/MonitorServerServlet?action=ping";
    // 启动Tomcat脚本路径
    private final static String startupTomcatPath = "/usr/local/tomcat/apache-tomcat-8.5.24/bin/startup.sh";
    // 关闭Tomcat脚本路径
    private final static String shutdownTomcatPath = "/usr/local/tomcat/apache-tomcat-8.5.24/bin/shutdown.sh";
    // Linux 服务器root账户密码
    private final static String rootPassword = "PHtech2018@!!";

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        try {
            String contextPath = event.getServletContext().getContextPath();
            String resourcePath = this.getClass().getResource("").getPath();
            if (contextPath != null && resourcePath != null
                    && resourcePath.contains(contextPath)) {
                String projectName = contextPath.substring(contextPath
                        .lastIndexOf("/") + 1);
                log.info("contextDestroyed:" + projectName);
                if (service != null && !service.isShutdown()) {
                    service.shutdown();
                }
                // 发送服务器停止运行或项目正在部署
                String receiver = "wangyi@zhigoumao.cn";
                String[] copyReceiversArray = {};
                JavaMailUtil.sendEmailToDatabaseSender(receiver,
                        copyReceiversArray, "Tomcat已停止运行", "Tomcat已停止运行或 "
                                + projectName + " 项目正在部署");
            }
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        try {
            String contextPath = event.getServletContext().getContextPath();
            String resourcePath = this.getClass().getResource("").getPath();
            // 发送服务器启动完成或项目部署完成邮件通知
            String receiver = "wangyi@zhigoumao.cn";
            String[] receiversArray = {};

            if (contextPath != null && resourcePath != null
                    && resourcePath.contains(contextPath)) {
                String projectName = contextPath.substring(contextPath
                        .lastIndexOf("/") + 1);
                JavaMailUtil.sendEmailToDatabaseSender(receiver,
                        receiversArray, "Tomcat启动完成", "Tomcat启动完成或 "
                                + projectName + " 项目部署完成");
                startKeepTomcatAliveRunnable();
            }
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    Runnable runnable = new Runnable() {
        public void run() {
            try {
                // task to run goes here
                if (needRebootTomcat) {
                    if (service != null && !service.isShutdown()) {
                        service.shutdown();
                    }
                    // 发送服务器异常邮件
                    String receiver = "wangyi@zhigoumao.cn";
                    String[] receiversArray = {};

                    JavaMailUtil.sendEmailToDatabaseSender(receiver,
                            receiversArray, "Tomcat服务器异常",
                            "监测到Tomcat服务器异常，正在自动重启服务器...");
                    log.info("start to reboot tomcat miao.....");
                    // 关闭Tomcat
                    shutdownTomcat();
                    // 开启Tomcat
                    startUpTomcat();
                } else {
                    keepTomcatAlive();
                }
            } catch (Exception e) {
                log.error("try->catch exception:", e);
                e.printStackTrace();
            }
        }
    };

    private void startKeepTomcatAliveRunnable() {
        try {
            pingErrorTimes = 0;
            needRebootTomcat = false;
            service = Executors.newSingleThreadScheduledExecutor();
            // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
            service.scheduleAtFixedRate(runnable, startPingServerDelay,
                    retryDuring, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
    }

    public void keepTomcatAlive() throws NullPointerException {
        log.info("try to keepTomcatAlive......................");
        boolean tomatAlive = false;
        HttpURLConnection connection = null;
        OutputStream os = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString); // url地址
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();

            os = connection.getOutputStream();
            JSONObject pingServerJsonObject = new JSONObject();
            pingServerJsonObject.put("action", "ping");
            os.write(pingServerJsonObject.toString().getBytes("UTF-8"));
            os.flush();
            os.close();
            int responseCode = connection.getResponseCode();
            reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String lines = "";
            StringBuffer sbf = new StringBuffer();
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sbf.append(lines);
            }
            String resp = sbf.toString();
            if (responseCode == 200) {
                if (resp != null) {
                    JSONObject respJsonObject = JSONObject.fromObject(resp);
                    if (respJsonObject.containsKey("response")) {
                        String response = respJsonObject.getString("response");
                        if (response.equals("success")) {
                            tomatAlive = true;
                        }
                    }
                }
            }
            log.info("returnFromPingServer=" + resp);
        } catch (Exception e) {
            tomatAlive = false;
            log.error("try->catch exception:", e);
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    reader = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                if (null != connection) {
                    // 显式关闭inputStream，达到释放资源目的
                    InputStream isInputStream = connection.getInputStream();
                    isInputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (null != connection) {
                    connection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!tomatAlive) {
            if (pingErrorTimes < pingServerRetryTimes) {
                needRebootTomcat = false;
                pingErrorTimes++;
            } else {
                needRebootTomcat = true;
                pingErrorTimes = 0;
            }
        } else {
            needRebootTomcat = false;
            pingErrorTimes = 0;
        }
    }

    public void startUpTomcat() {
        String cmdStart = "echo " + rootPassword + " | sudo -S "
                + startupTomcatPath; // 把密码root给sudo用
        String[] cmds = { "/bin/sh", "-c", cmdStart };
        BufferedReader br = null;
        try {
            Process proc = Runtime.getRuntime().exec(cmds);
            proc.waitFor();
            InputStream in = proc.getInputStream();
            br = new BufferedReader(new InputStreamReader(in));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            log.info("startTomcat result:" + sb.toString());
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
    }

    public void shutdownTomcat() {
        String cmdStop = "echo " + rootPassword + " | sudo -S "
                + shutdownTomcatPath;
        String[] cmds = { "/bin/sh", "-c", cmdStop };
        BufferedReader br = null;
        try {
            Process proc = Runtime.getRuntime().exec(cmds);
            proc.waitFor();
            InputStream in = proc.getInputStream();
            br = new BufferedReader(new InputStreamReader(in));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            log.info("shutdownTomcat result:" + sb.toString());
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
    }

}
