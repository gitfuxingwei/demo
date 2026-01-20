package org.example.dto;

import com.alibaba.excel.annotation.ExcelProperty;

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

    @ExcelProperty("用户ID")
    private Integer userId;

    @ExcelProperty("部门ID")
    private Integer deptId;

    @ExcelProperty("用户名")
    private String userName;

    @ExcelProperty("昵称")
    private String nickName;

    @ExcelProperty("用户类型")
    private Integer userType;

    @ExcelProperty("邮箱")
    private String email;

    @ExcelProperty("手机号")
    private String phonenumber;

    @ExcelProperty("性别")
    private String sex;                //0 男 1女

    @ExcelProperty("头像")
    private String avatar;             //头像

    @ExcelProperty("密码")
    private String userPassword;       //密码

    @ExcelProperty("状态")
    private String status;             //0 正常 1 停用

    @ExcelProperty("删除标志")
    private String delFlag;            //删除标志 0 正常 1 删除

    @ExcelProperty("登录IP")
    private String loginIp;            //登录IP

    @ExcelProperty("最后登录时间")
    private String loginDate;          //最后登录时间

    @ExcelProperty("创建者")
    private String createBy;           //创建者

    @ExcelProperty("创建时间")
    private String createTime;         //创建时间

    @ExcelProperty("更新者")
    private String updateBy;           //更新者

    @ExcelProperty("更新时间")
    private String updateTime;         //更新时间

    @ExcelProperty("备注")
    private String remark;             //备注
}
