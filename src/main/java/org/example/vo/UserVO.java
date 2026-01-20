package org.example.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户视图对象
 * 用于向前端展示用户信息，不包含敏感字段
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {
    private Integer userId;
    private Integer deptId;
    private String userName;
    private String nickName;
    private String userType;
    private String email;
    private String phonenumber;
    private String sex;                //0 男 1女
    private String avatar;             //头像
    private String status;             //0 正常 1 停用
    private String loginIp;            //登录IP
    private String loginDate;          //最后登录时间
    private String createTime;         //创建时间
    private String updateTime;         //更新时间
    private String remark;             //备注
}