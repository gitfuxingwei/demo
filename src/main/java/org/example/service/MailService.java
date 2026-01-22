package org.example.service;

import org.example.dto.MailSendDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MailService {
    /**
     * 发送邮件
     * @param mailSendDTO 邮件发送参数
     * @param attachments 附件列表
     * @return 发送结果
     */
    boolean sendMail(MailSendDTO mailSendDTO, List<MultipartFile> attachments);
}