package org.example.controller;

import org.example.dto.MailSendDTO;
import org.example.result.Result;
import org.example.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

@RestController
@RequestMapping("/api/mail")
public class MailController {

    @Autowired
    private MailService mailService;

    /**
     * 邮件发送
     * 使用 HttpServletRequest 判断请求类型并相应处理
     */
    @PostMapping("/send")
    public Result<String> sendMail(HttpServletRequest request) {
        try {
            String contentType = request.getContentType();

            if (contentType != null && contentType.toLowerCase().contains("multipart/form-data")) {
                // 处理带附件的请求
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

                // 获取邮件信息
                String mailInfoJson = multipartRequest.getParameter("mailInfo");
                if (mailInfoJson == null || mailInfoJson.isEmpty()) {
                    return Result.error("邮件信息不能为空");
                }

                // 将JSON字符串转换为对象
                MailSendDTO mailInfo = new com.fasterxml.jackson.databind.ObjectMapper()
                    .readValue(mailInfoJson, MailSendDTO.class);

                // 获取附件
                List<MultipartFile> attachments = new ArrayList<>();
                java.util.Enumeration<String> fileNamesEnum = (Enumeration<String>) multipartRequest.getFileNames();
                List<String> fileNames = Collections.list(fileNamesEnum);

                for (String fileName : fileNames) {
                    if (!fileName.equals("mailInfo")) { // 排除mailInfo参数
                        MultipartFile file = multipartRequest.getFile(fileName);
                        if (file != null && !file.isEmpty()) {
                            attachments.add(file);
                        }
                    }
                }

                boolean success = mailService.sendMail(mailInfo, attachments);

                if (success) {
                    return Result.success("邮件发送成功");
                } else {
                    return Result.error("邮件发送失败");
                }
            } else {
                // 处理普通JSON请求（不带附件）
                StringBuilder buffer = new StringBuilder();
                String line;
                BufferedReader reader = request.getReader();
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                if (buffer.length() == 0) {
                    return Result.error("请求体不能为空");
                }

                // 将JSON字符串转换为对象
                MailSendDTO mailSendDTO = new com.fasterxml.jackson.databind.ObjectMapper()
                    .readValue(buffer.toString(), MailSendDTO.class);

                boolean success = mailService.sendMail(mailSendDTO, null);

                if (success) {
                    return Result.success("邮件发送成功");
                } else {
                    return Result.error("邮件发送失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("邮件发送过程中发生错误: " + e.getMessage());
        }
    }
}
