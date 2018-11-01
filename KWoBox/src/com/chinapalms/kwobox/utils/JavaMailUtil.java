package com.chinapalms.kwobox.utils;

import javax.mail.Message.RecipientType;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.javabean.SenderEmail;
import com.chinapalms.kwobox.service.SenderEmailService;

import java.util.Date;
import java.util.List;
import java.util.Properties;

public class JavaMailUtil {

    static Log log = LogFactory.getLog(JavaMailUtil.class);

    private static int emailSenderIndex = 0;

    /**
     * JavaMail 版本: 1.6.0 JDK 版本: JDK 1.7 以上（必须）
     */
    public static void sendEmail(String receiver, String[] copyReceiversArray,
            String subject, String emailContent) throws Exception {

        try {
            String SMTPSERVER = "smtp.163.com";
            String SMTPPORT = "465";
            String ACCOUT = "pa_more@163.com";
            String PWD = "19910209lrl";

            // 创建邮件配置
            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", "smtp"); // 使用的协议（JavaMail规范要求）
            props.setProperty("mail.smtp.host", SMTPSERVER); // 发件人的邮箱的 SMTP
                                                             // 服务器地址
            props.setProperty("mail.smtp.port", SMTPPORT);
            props.put("mail.smtp.socketFactory.class",
                    "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.smtp.auth", "true"); // 需要请求认证
            props.setProperty("mail.smtp.ssl.enable", "true");// 开启ssl

            // 根据邮件配置创建会话，注意session别导错包
            Session session = Session.getDefaultInstance(props);
            // 开启debug模式，可以看到更多详细的输入日志
            session.setDebug(false);
            // 创建邮件
            MimeMessage message = createEmail(session, ACCOUT, subject,
                    emailContent, receiver, copyReceiversArray);
            // 获取传输通道
            Transport transport = session.getTransport();
            transport.connect(SMTPSERVER, ACCOUT, PWD);
            // 连接，并发送邮件
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }

    }

    public static void sendEmailToDatabaseSender(String receiver,
            String[] copyReceiversArray, String subject, String emailContent)
            throws Exception {

        try {
            String SMTPSERVER = "smtp.163.com";
            String SMTPPORT = "465";
            // String ACCOUT = "pa_more@163.com";
            // String PWD = "19910209lrl";

            // 从数据库中查出收件人
            SenderEmailService senderEmailService = new SenderEmailService();
            List<SenderEmail> senderEmailsList = senderEmailService
                    .findSenderEmails();

            SenderEmail senderEmail = null;

            if (emailSenderIndex < senderEmailsList.size()) {
                senderEmail = senderEmailsList.get(emailSenderIndex);
                emailSenderIndex++;
            } else {
                emailSenderIndex = 0;
                senderEmail = senderEmailsList.get(emailSenderIndex);
                emailSenderIndex++;
            }

            if (senderEmail.getEmailAccount().contains("126.com")) {
                SMTPSERVER = "smtp.126.com";
            } else {
                SMTPSERVER = "smtp.163.com";
            }

            // 创建邮件配置
            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", "smtp"); // 使用的协议（JavaMail规范要求）
            props.setProperty("mail.smtp.host", SMTPSERVER); // 发件人的邮箱的 SMTP
                                                             // 服务器地址
            props.setProperty("mail.smtp.port", SMTPPORT);
            props.put("mail.smtp.socketFactory.class",
                    "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.smtp.auth", "true"); // 需要请求认证
            props.setProperty("mail.smtp.ssl.enable", "true");// 开启ssl

            // 根据邮件配置创建会话，注意session别导错包
            Session session = Session.getDefaultInstance(props);
            // 开启debug模式，可以看到更多详细的输入日志
            session.setDebug(false);
            // 创建邮件
            MimeMessage message = createEmail(session,
                    senderEmail.getEmailAccount(), subject, emailContent,
                    receiver, copyReceiversArray);
            // 获取传输通道
            Transport transport = session.getTransport();
            transport.connect(SMTPSERVER, senderEmail.getEmailAccount(),
                    senderEmail.getEmailPassword());
            // 连接，并发送邮件
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (Exception e) {
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }

    }

    private static MimeMessage createEmail(Session session, String fromAccount,
            String emailSubject, String emailContent, String receiver,
            String[] copyReceiversArray) throws Exception {
        // 根据会话创建邮件
        MimeMessage msg = new MimeMessage(session);
        // address邮件地址, personal邮件昵称, charset编码方式
        InternetAddress fromAddress = new InternetAddress(fromAccount,
                "智购猫邮件通知", "utf-8");
        // 设置发送邮件方
        msg.setFrom(fromAddress);
        // 设置邮件接收方
        msg.setRecipient(RecipientType.TO, new InternetAddress(receiver, "RIO",
                "utf-8"));
        for (int i = 0; i < copyReceiversArray.length; i++) {
            // 截取名字
            String name = copyReceiversArray[i].split("@")[0];
            msg.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(
                    copyReceiversArray[i], name != null ? name : "zhigoumao",
                    "utf-8"));
        }
        // 设置邮件标题
        msg.setSubject(emailSubject, "utf-8");
        msg.setText(emailContent, "utf-8");
        // 设置显示的发件时间
        msg.setSentDate(new Date());
        // 保存设置
        msg.saveChanges();
        // 将该邮件保存到本地
        // OutputStream out = new FileOutputStream("D:\\MyEmail.eml");
        // msg.writeTo(out);
        // out.flush();
        // out.close();
        return msg;
    }

}
