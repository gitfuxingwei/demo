package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户数据传输对象
 * 用于在不同层之间传输用户数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Integer userId;
    private Integer deptId;
    private String userName;
    private String nickName;
    private String userType;
    private String email;
    private String phonenumber;
    private String sex;                //0 男 1女
    private String avatar;             //头像
    private String userPassword;       //密码
    private String status;             //0 正常 1 停用
    private String delFlag;            //删除标志 0 正常 1 删除
    private String loginIp;            //登录IP
    private String loginDate;          //最后登录时间
    private String createBy;           //创建者
    private String createTime;         //创建时间
    private String updateBy;           //更新者
    private String updateTime;         //更新时间
    private String remark;             //备注
}