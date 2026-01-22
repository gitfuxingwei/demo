package org.example.service.impl;

import org.example.dto.MailSendDTO;
import org.example.service.MailService;
import org.example.utils.MailUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.List;

@Service
public class MailServiceImpl implements MailService {

    @Override
    public boolean sendMail(MailSendDTO mailSendDTO, List<MultipartFile> attachments) {
        try {
            // 验证必要参数
            if (mailSendDTO == null || mailSendDTO.getSubject() == null || mailSendDTO.getSubject().trim().isEmpty()) {
                throw new IllegalArgumentException("邮件主题不能为空");
            }
            
            if (mailSendDTO.getToAddresses() == null || mailSendDTO.getToAddresses().isEmpty()) {
                throw new IllegalArgumentException("收件人不能为空");
            }
            
            // 获取邮件会话
            Session session = MailUtils.createSession();
            
            // 创建邮件消息
            MimeMessage message = new MimeMessage(session);
            
            // 设置邮件主题
            message.setSubject(mailSendDTO.getSubject(), "UTF-8");
            
            // 设置发送者（使用配置中的默认发送者）
            message.setFrom(new InternetAddress(MailUtils.getDefaultSender()));
            
            // 设置收件人
            for (String toAddress : mailSendDTO.getToAddresses()) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress.trim()));
            }
            
            // 设置抄送人
            if (mailSendDTO.getCcAddresses() != null) {
                for (String ccAddress : mailSendDTO.getCcAddresses()) {
                    message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccAddress.trim()));
                }
            }
            
            // 设置密送人
            if (mailSendDTO.getBccAddresses() != null) {
                for (String bccAddress : mailSendDTO.getBccAddresses()) {
                    message.addRecipient(Message.RecipientType.BCC, new InternetAddress(bccAddress.trim()));
                }
            }
            
            // 创建混合类型的多部分消息
            MimeMultipart mixedMultipart = new MimeMultipart("mixed");
            
            // 添加邮件正文
            MimeBodyPart textPart = new MimeBodyPart();
            String contentType = mailSendDTO.getContentType();
            if (contentType != null && contentType.toLowerCase().contains("html")) {
                textPart.setContent(mailSendDTO.getContent(), "text/html;charset=UTF-8");
            } else {
                textPart.setText(mailSendDTO.getContent(), "UTF-8");
            }
            mixedMultipart.addBodyPart(textPart);
            
            // 添加附件
            if (attachments != null && !attachments.isEmpty()) {
                for (MultipartFile attachment : attachments) {
                    if (attachment != null && !attachment.isEmpty()) {
                        MimeBodyPart attachmentPart = new MimeBodyPart();
                        attachmentPart.setFileName(new String(attachment.getOriginalFilename().getBytes("UTF-8"), "ISO-8859-1")); // 解决中文文件名乱码
                        attachmentPart.setContent(attachment.getBytes(), "application/octet-stream");
                        attachmentPart.setDisposition(MimeBodyPart.ATTACHMENT); // 作为附件
                        mixedMultipart.addBodyPart(attachmentPart);
                    }
                }
            }
            
            // 将多部分消息设置到邮件中
            message.setContent(mixedMultipart);
            
            // 发送邮件
            Transport.send(message);
            
            return true;
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("邮件发送失败: " + e.getMessage(), e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("邮件发送过程中发生未知错误: " + e.getMessage(), e);
        }
    }
}