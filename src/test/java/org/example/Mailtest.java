package org.example;

import org.example.dto.MailSendDTO;

import java.util.Arrays;

public class Mailtest {
    public static void main(String[] args) {
        // 创建邮件发送参数示例
        MailSendDTO mailSendDTO = new MailSendDTO();
        mailSendDTO.setSubject("测试邮件 - 默认HTML格式");
        mailSendDTO.setContent("<h2>HTML格式邮件</h2><p>这是一封<strong>HTML格式</strong>的测试邮件。</p>");
        // contentType 默认为 "text/html"，无需显式设置
        
        // 设置收件人、抄送人
        mailSendDTO.setToAddresses(Arrays.asList("1919972092@qq.com"));
        mailSendDTO.setCcAddresses(Arrays.asList("13906448033@163.com"));
        
        System.out.println("邮件参数已设置完成");
        System.out.println("主题: " + mailSendDTO.getSubject());
        System.out.println("内容: " + mailSendDTO.getContent());
        System.out.println("内容类型: " + mailSendDTO.getContentType()); // 应该是 "text/html"
        System.out.println("收件人: " + mailSendDTO.getToAddresses());
        System.out.println("抄送人: " + mailSendDTO.getCcAddresses());
        
        System.out.println("要发送邮件，请调用 /api/mail/send 接口");
        System.out.println("使用 multipart/form-data 格式，mailInfo 作为 JSON 参数传递");
    }
}