package org.example.utils;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

public class MailUtils {
    private MailUtils() {}
    
    public static Session createSession() {
        //账号信息
        final String account = "1927563652@qq.com";
        //授权码
        final String password = "ulwgcwehsmkzbchh";
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", "smtp.qq.com");     //QQ邮箱的SMTP服务器
        props.setProperty("mail.smtp.port", "465");             //端口号
        props.setProperty("mail.smtp.auth", "true");            //用户认证
        props.setProperty("mail.smtp.ssl.enable", "true");      //开启SSL加密
        Session session = Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(account, password);
            }
        });
        session.setDebug(true);
        return  session;
    }

    /**
     * 创建自定义会话
     * @param smtpHost SMTP服务器地址
     * @param port 端口
     * @param username 用户名
     * @param password 密码或授权码
     * @param enableSSL 是否启用SSL
     * @return 邮件会话
     */
    public static Session createSession(String smtpHost, String port, String username, String password, boolean enableSSL) {
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", smtpHost);
        props.setProperty("mail.smtp.port", port);
        props.setProperty("mail.smtp.auth", "true");
        if (enableSSL) {
            props.setProperty("mail.smtp.ssl.enable", "true");
        } else {
            props.setProperty("mail.smtp.starttls.enable", "true");
        }

        Session session = Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        session.setDebug(true);
        return session;
    }

    // 获取默认发送者邮箱
    public static String getDefaultSender() {
        return "1927563652@qq.com";
    }

}