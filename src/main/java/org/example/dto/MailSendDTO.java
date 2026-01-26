package org.example.dto;

import java.util.List;



public class MailSendDTO {
    private String subject;           // 邮件主题
    private String content;          // 邮件正文
    private String contentType = "text/html";      // 内容类型，默认为HTML
    private List<String> toAddresses; // 收件人列表
    private List<String> ccAddresses; // 抄送人列表
    private List<String> bccAddresses; // 密送人列表

    // 构造函数
    public MailSendDTO() {}

    // Getter 和 Setter 方法
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType != null ? contentType : "text/html";
    }

    public List<String> getToAddresses() {
        return toAddresses;
    }

    public void setToAddresses(List<String> toAddresses) {
        this.toAddresses = toAddresses;
    }

    public List<String> getCcAddresses() {
        return ccAddresses;
    }

    public void setCcAddresses(List<String> ccAddresses) {
        this.ccAddresses = ccAddresses;
    }

    public List<String> getBccAddresses() {
        return bccAddresses;
    }

    public void setBccAddresses(List<String> bccAddresses) {
        this.bccAddresses = bccAddresses;
    }
}
