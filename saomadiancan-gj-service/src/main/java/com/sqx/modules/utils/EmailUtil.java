package com.sqx.modules.utils;

import lombok.extern.slf4j.Slf4j;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;

/**
 * @author chengming
 * @since 2025/3/26
 */
@Slf4j
public class EmailUtil {
    /**
     * 发送文本邮件
     * @param host 邮箱服务器
     * @param port 邮箱端口
     * @param fromUser 发件人
     * @param password 密码
     * @param toUser 收件人
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    public static void sendTextEmail(String host, String port, String fromUser, String password, String toUser, String subject, String content) {
        try {
            Session session = createSession(host, port, fromUser, password);
            // 创建邮件
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromUser)); // 发件人地址
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toUser)); // 收件人地址
            message.setSubject(subject); // 邮件主题
            message.setText(content); // 邮件正文
            message.setSentDate(new Date());

            // 发送邮件
            Transport.send(message);
            log.info("邮件发送成功");
        } catch (MessagingException | GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建会话
     * @param host 邮箱服务器
     * @param port 邮箱端口
     * @param fromUser 发件人
     * @param password 密码
     * @return Session
     */
    private static Session createSession(String host, String port, String fromUser, String password) throws GeneralSecurityException {
        // 设置属性
        Properties props = new Properties();
        props.put("mail.smtp.host", host); // SMTP 服务器地址
        props.put("mail.smtp.port", port); // SMTP 端口
        props.put("mail.smtp.auth", "true"); // 启用身份验证
        props.put("mail.smtp.ssl.enable", "true"); // 启用 TLS
        props.put("mail.smtp.ssl.trust", host); // 信任的 SMTP 服务器

        // 创建会话
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromUser, password);
            }
        });

        return session;
    }
}
