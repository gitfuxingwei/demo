package org.example.dto;

import lombok.Data;

/**
 * 密码更新数据传输对象
 * 用于封装密码更新请求参数
 */
@Data
public class PasswordUpdateDTO {
    private String username;
    private String oldPassword;
    private String newPassword;
}